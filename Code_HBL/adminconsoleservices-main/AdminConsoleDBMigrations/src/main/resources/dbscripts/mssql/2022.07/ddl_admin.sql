GO
ALTER TABLE [${dbxschemaname}].[policycontent]
ALTER COLUMN Locale_Code NVARCHAR(10);

GO
ALTER TABLE [${dbxschemaname}].[policycontent]
ADD CONSTRAINT FK_LocaleCode_ID
FOREIGN KEY (Locale_Code) REFERENCES [dbxdb].[locale](Code);

GO
CREATE TABLE [${dbxschemaname}].[approvalrequests] (
	[requestId] varchar(50) ,
	[recordId] VARCHAR(max),
	[module] varchar(50),
	[feature] varchar(50),
	[expAPIOperationName] varchar(250),
	[expAPINickName] VARCHAR(100),		
	[permissionId] VARCHAR(50) DEFAULT NULL,
	[permissionName] VARCHAR(50) DEFAULT NULL,
	[createdby] varchar(50),
	[createdts] datetime2(0) DEFAULT GETDATE(),
	[status] varchar(50),
	[reason] varchar(255),
	[checkedBy] varchar(50),
	[checkedts] datetime2(0) NULL DEFAULT NULL,
	PRIMARY KEY(requestId));
	
	
CREATE TABLE [${dbxschemaname}].[permissionapprovals] (
  [id] INT NOT NULL IDENTITY,		
  [expAPIOperationName] VARCHAR(250),
  [expAPINickName] VARCHAR(100),	
  [permissionId] VARCHAR(50) DEFAULT NULL,
  [permissionName] VARCHAR(50) DEFAULT NULL,
  [approvalPermissionId] VARCHAR(50) NULL,
  [approvalPermissionName] VARCHAR(50) NULL,
  [isApprovalRequired] SMALLINT NOT NULL,
  [createdby] varchar(50),
  [createdts] datetime2(0) DEFAULT GETDATE(),
  PRIMARY KEY ([id]));
  
  
CREATE TABLE [${dbxschemaname}].[permissionapprovalconfig] (
  [id] int NOT NULL IDENTITY,
  [expAPIOperationName] varchar(250),
  [expAPINickName] varchar(100),
  [permissionId] varchar(50) DEFAULT NULL,
  [permissionName] varchar(50) DEFAULT NULL,
  [approvalTableName] varchar(200) NOT NULL,
  [originalTableName] varchar(200) NOT NULL,
  [keyColumnNames] varchar(200) NOT NULL,
  [updateSequence] int,
  [cleanup] smallint,
  [update] smallint,
  [sqlquery] varchar(max),
  [createdby] varchar(50),
  [createdts] datetime2(0) DEFAULT GETDATE(),
  PRIMARY KEY ([id])
);



CREATE TABLE [${dbxschemaname}].[role_approval] (
  [id] varchar(50) NOT NULL,
  [Type_id] varchar(50) NOT NULL,
  [Status_id] varchar(50) NOT NULL,
  [Parent_id] varchar(50) DEFAULT NULL,
  [Name] varchar(50) NOT NULL,
  [Description] varchar(300) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL',
  [aprRequestId] varchar(50) NOT NULL,
  [crudAction] varchar(5) NOT NULL,
  PRIMARY KEY ([id],[aprRequestId])
) ;

CREATE INDEX [IXFK_Role_Role_Approval] ON [${dbxschemaname}].[role_approval] ([Parent_id]);
CREATE INDEX [IXFK_Role_RoleType] ON [${dbxschemaname}].[role_approval] ([Type_id]);
CREATE INDEX [IXFK_Role_Status] ON [${dbxschemaname}].[role_approval] ([Status_id]);



CREATE TABLE [${dbxschemaname}].[rolepermission_approval] (
  [Role_id] varchar(50) NOT NULL,
  [Permission_id] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL',
  [aprRequestId] varchar(50) NOT NULL,
  [crudAction] varchar(5) NOT NULL,
  PRIMARY KEY ([Role_id],[Permission_id],[aprRequestId])
) ;

CREATE INDEX [IXFK_RolePermission_Permission] ON [${dbxschemaname}].[rolepermission_approval] ([Permission_id]);
CREATE INDEX [IXFK_RolePermission_Role] ON [${dbxschemaname}].[rolepermission_approval] ([Role_id]);



CREATE TABLE [${dbxschemaname}].[userrole_approval] (
  [User_id] varchar(50) NOT NULL,
  [Role_id] varchar(50) NOT NULL,
  [hasSuperAdminPrivilages] smallint NOT NULL DEFAULT '0',
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL',
  [aprRequestId] varchar(50) NOT NULL,
  [crudAction] varchar(5) NOT NULL,
  PRIMARY KEY ([User_id],[Role_id],[aprRequestId])
) ;

CREATE INDEX [IXFK_UserRole_Role] ON [${dbxschemaname}].[userrole_approval] ([Role_id]);
CREATE INDEX [IXFK_UserRole_SystemUser] ON [${dbxschemaname}].[userrole_approval] ([User_id]);




CREATE TABLE [${dbxschemaname}].[userroleservicedefinition_approval] (
  [UserRole_id] varchar(50) NOT NULL,
  [servicedefinitionId] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL',
  [aprRequestId] varchar(50) NOT NULL,
  [crudAction] varchar(5) NOT NULL,
  PRIMARY KEY ([UserRole_id],[servicedefinitionId])
) ;

CREATE INDEX [userroleservicedefinition_servicedefinition_id_idx] ON [${dbxschemaname}].[userroleservicedefinition_approval] ([servicedefinitionId]);
CREATE INDEX [userroleservicedefinition_userrole_id_idx] ON [${dbxschemaname}].[userroleservicedefinition_approval] ([UserRole_id]);


CREATE TABLE [${dbxschemaname}].[rolecompositeaction_approval] (
  [Role_id] varchar(50) NOT NULL,
  [CompositeAction_id] varchar(50) NOT NULL,
  [isEnabled] smallint DEFAULT '0' NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint DEFAULT '0' NOT NULL,
  [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL',
  [aprRequestId] varchar(50) NOT NULL,
  [crudAction] varchar(5) NOT NULL,
  PRIMARY KEY ([Role_id],[CompositeAction_id],[aprRequestId])
);

CREATE INDEX FK_RoleCompositeAction_Approval_CompositeAction_idx ON [${dbxschemaname}].[rolecompositeaction_approval] (CompositeAction_id);




GO
ALTER TABLE [${dbxschemaname}].[accesspolicy] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[accounttype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[actionlevel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[app] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[application] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[city] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[configurations] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[country] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customer] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customerservice] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[eventtype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[facility] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[faqs] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[feature] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[featureaction] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[groupservicedefinition] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[internalusermanager] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[internalusertype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[limitgroupdisplaynamedescription] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[location] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[locationfile] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[locationservice] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[locationtype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[logview] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[media] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[mfa] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[mfakey] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[mfatype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[permission] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[policycontent] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[region] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[role] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[roletype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[service] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[membergroup] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[servicedefinition] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[systemuser] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[useraddress] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[userlob] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[userpermission] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[userrole] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[usertype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[limitgroup] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';
ALTER TABLE [${dbxschemaname}].[dependentactions] ADD [companyLegalUnit] VARCHAR(50) NOT NULL default 'ALL';



DROP VIEW IF EXISTS [${dbxschemaname}].[permissions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[permissions_view] AS
SELECT [${dbxschemaname}].permission.id AS Permission_id, [${dbxschemaname}].permission.Type_id AS PermissionType_id, [${dbxschemaname}].permission.Name AS Permission_Name, [${dbxschemaname}].permission.Description AS Permission_Desc, [${dbxschemaname}].permission.Status_id, [${dbxschemaname}].permission.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].status.Description AS Status_Desc,
                 (SELECT COUNT(Role_id) AS Expr1
                 FROM    [${dbxschemaname}].rolepermission
                 WHERE (Permission_id = [${dbxschemaname}].permission.id)) AS Role_Count,
                 (SELECT COUNT(Permission_id) AS Expr1
                 FROM    [${dbxschemaname}].userpermission
                 WHERE (Permission_id = [${dbxschemaname}].permission.id)) +
                 (SELECT COUNT(User_id) AS Expr1
                 FROM    [${dbxschemaname}].userrole
                 WHERE (Role_id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].rolepermission
                                  WHERE (Permission_id = [${dbxschemaname}].permission.id)))) AS Users_Count, CASE (permission.Status_id) WHEN N'SID_ACTIVE' THEN N'Active' ELSE N'Inactive' END AS Status
FROM   [${dbxschemaname}].permission INNER JOIN
             [${dbxschemaname}].status ON [${dbxschemaname}].permission.Status_id = [${dbxschemaname}].status.id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[rolepermission_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[rolepermission_view] AS
SELECT [${dbxschemaname}].role.Name AS Role_Name, [${dbxschemaname}].role.Description AS Role_Description, [${dbxschemaname}].role.Status_id AS Role_Status_id, [${dbxschemaname}].rolepermission.Role_id, [${dbxschemaname}].permission.id AS Permission_id, [${dbxschemaname}].permission.Type_id AS Permission_Type_id, 
             [${dbxschemaname}].permission.Status_id AS Permission_Status_id, [${dbxschemaname}].permission.DataType_id, [${dbxschemaname}].permission.Name AS Permission_Name, [${dbxschemaname}].permission.Description AS Permission_Description, [${dbxschemaname}].permission.isComposite AS Permission_isComposite, 
             [${dbxschemaname}].permission.PermissionValue,[${dbxschemaname}].permission.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].permission.createdby AS Permission_createdby, [${dbxschemaname}].permission.modifiedby AS Permission_modifiedby, [${dbxschemaname}].permission.createdts AS Permission_createdts, [${dbxschemaname}].permission.lastmodifiedts AS Permission_lastmodifiedts, 
             [${dbxschemaname}].permission.synctimestamp AS Permission_synctimestamp, [${dbxschemaname}].permission.softdeleteflag AS Permission_softdeleteflag
FROM   [${dbxschemaname}].rolepermission INNER JOIN
             [${dbxschemaname}].permission ON [${dbxschemaname}].rolepermission.Permission_id = [${dbxschemaname}].permission.id INNER JOIN
             [${dbxschemaname}].role ON [${dbxschemaname}].role.id = [${dbxschemaname}].rolepermission.Role_id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[userdirectpermission_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[userdirectpermission_view] AS
SELECT [${dbxschemaname}].userpermission.User_id, [${dbxschemaname}].userpermission.Permission_id, [${dbxschemaname}].permission.Name AS Permission_Name, [${dbxschemaname}].permission.Status_id AS Permission_Status_id, [${dbxschemaname}].permission.Description AS Permission_Description, 
             [${dbxschemaname}].permission.isComposite AS Permission_isComposite, [${dbxschemaname}].systemuser.Status_id AS User_Status_id, [${dbxschemaname}].systemuser.Username, [${dbxschemaname}].systemuser.Email, [${dbxschemaname}].systemuser.FirstName, [${dbxschemaname}].systemuser.MiddleName, [${dbxschemaname}].systemuser.LastName, [${dbxschemaname}].systemuser.companyLegalUnit AS companyLegalUnit, 
             [${dbxschemaname}].systemuser.createdby, [${dbxschemaname}].systemuser.modifiedby AS updatedby, [${dbxschemaname}].systemuser.createdts, [${dbxschemaname}].systemuser.lastmodifiedts AS updatedts, [${dbxschemaname}].systemuser.softdeleteflag
FROM   [${dbxschemaname}].userpermission INNER JOIN
             [${dbxschemaname}].systemuser ON [${dbxschemaname}].userpermission.User_id = [${dbxschemaname}].systemuser.id INNER JOIN
             [${dbxschemaname}].permission ON [${dbxschemaname}].userpermission.Permission_id = [${dbxschemaname}].permission.id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[internalusers_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internalusers_view] AS
SELECT [${dbxschemaname}].systemuser.id AS User_id, [${dbxschemaname}].systemuser.Username, [${dbxschemaname}].systemuser.companyLegalUnit AS companyLegalUnit,
                 (SELECT TOP (1) userType
                 FROM    [${dbxschemaname}].internalusertype
                 WHERE ([${dbxschemaname}].systemuser.id = userId)) AS UserType,
                 (SELECT TOP (1) manager
                 FROM    [${dbxschemaname}].internalusermanager
                 WHERE ([${dbxschemaname}].systemuser.id = userId)) AS ReportingManager,
                 (SELECT name
                 FROM    [${dbxschemaname}].usertype
                 WHERE (id IN
                                  (SELECT userType
                                  FROM    [${dbxschemaname}].internalusertype
                                  WHERE ([${dbxschemaname}].systemuser.id = userId)))) AS userTypeName, [${dbxschemaname}].systemuser.Status_id, [${dbxschemaname}].status.Description AS Status_Desc, [${dbxschemaname}].systemuser.FirstName, [${dbxschemaname}].systemuser.MiddleName, [${dbxschemaname}].systemuser.LastName, [${dbxschemaname}].systemuser.lastLogints, 
             [${dbxschemaname}].systemuser.FirstName + ' ' + [${dbxschemaname}].systemuser.LastName AS Name, [${dbxschemaname}].systemuser.Email,
                 (SELECT TOP (1) Role_id
                 FROM    [${dbxschemaname}].userrole
                 WHERE ([${dbxschemaname}].systemuser.id = User_id)) AS Role_id,
                 (SELECT TOP (1) Description
                 FROM    [${dbxschemaname}].role
                 WHERE (id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].userrole
                                  WHERE ([${dbxschemaname}].systemuser.id = User_id)))) AS Role_Desc,
                 (SELECT TOP (1) Name
                 FROM    [${dbxschemaname}].role
                 WHERE (id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].userrole
                                  WHERE ([${dbxschemaname}].systemuser.id = User_id)))) AS Role_Name,
                 (SELECT COUNT(Permission_id) AS Expr1
                 FROM    [${dbxschemaname}].userpermission
                 WHERE ([${dbxschemaname}].systemuser.id = User_id)) +
                 (SELECT COUNT(Permission_id) AS Expr1
                 FROM    [${dbxschemaname}].rolepermission
                 WHERE (Role_id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].userrole
                                  WHERE ([${dbxschemaname}].systemuser.id = User_id)))) AS Permission_Count, [${dbxschemaname}].systemuser.lastmodifiedts, [${dbxschemaname}].systemuser.createdts,
                 (SELECT workaddress.addressLine1 + ', ' + ISNULL(workaddress.addressLine2, '') + ', ' +
                                  (SELECT Name
                                  FROM    [${dbxschemaname}].city
                                  WHERE (id = workaddress.City_id)) + ', ' +
                                  (SELECT Name
                                  FROM    [${dbxschemaname}].region
                                  WHERE (id = workaddress.Region_id)) + ', ' +
                                  (SELECT Name
                                  FROM    [${dbxschemaname}].country
                                  WHERE (id IN
                                                   (SELECT Country_id
                                                   FROM    [${dbxschemaname}].city
                                                   WHERE (id = workaddress.City_id)))) + ', ' + workaddress.zipCode AS Expr1) AS Work_Addr,
                 (SELECT homeaddress.addressLine1 + N', ' + ISNULL(homeaddress.addressLine2, '') + N', ' + ISNULL(homeaddress.cityName, '') + N', ' +
                                  (SELECT Name
                                  FROM    [${dbxschemaname}].region
                                  WHERE (id = homeaddress.Region_id)) + N', ' +
                                  (SELECT Name
                                  FROM    [${dbxschemaname}].country
                                  WHERE (id IN
                                                   (SELECT Country_id
                                                   FROM    [${dbxschemaname}].region
                                                   WHERE (id = homeaddress.Region_id)))) + N', ' + homeaddress.zipCode AS Expr1) AS Home_Addr,
                 (SELECT ISNULL([${dbxschemaname}].systemuser.id, N'') AS Expr1) AS Home_AddressID,
                 (SELECT ISNULL(homeaddress.addressLine1, N'') AS Expr1) AS Home_AddressLine1,
                 (SELECT ISNULL(homeaddress.addressLine2, N'') AS Expr1) AS Home_AddressLine2,
                 (SELECT ISNULL(homeaddress.cityName, N'') AS Expr1) AS Home_CityName,
                 (SELECT ISNULL(homeaddress.City_id, N'') AS Expr1) AS Home_CityID,
                 (SELECT ISNULL
                                  ((SELECT Name
                                   FROM    [${dbxschemaname}].region
                                   WHERE (id = homeaddress.Region_id)), N'') AS Expr1) AS Home_StateName,
                 (SELECT ISNULL(homeaddress.Region_id, N'') AS Expr1) AS Home_StateID,
                 (SELECT ISNULL
                                  ((SELECT Name
                                   FROM    [${dbxschemaname}].country
                                   WHERE (id IN
                                                    (SELECT Country_id
                                                    FROM    [${dbxschemaname}].region
                                                    WHERE (id = homeaddress.Region_id)))), N'') AS Expr1) AS Home_CountryName,
                 (SELECT ISNULL
                                  ((SELECT id
                                   FROM    [${dbxschemaname}].country
                                   WHERE (id IN
                                                    (SELECT Country_id
                                                    FROM    [${dbxschemaname}].region
                                                    WHERE (id = homeaddress.Region_id)))), N'') AS Expr1) AS Home_CountryID,
                 (SELECT ISNULL(homeaddress.zipCode, N'') AS Expr1) AS Home_Zipcode,
                 (SELECT ISNULL([${dbxschemaname}].systemuser.id, N'') AS Expr1) AS Work_AddressID,
                 (SELECT ISNULL(workaddress.addressLine1, N'') AS Expr1) AS Work_AddressLine1,
                 (SELECT ISNULL(workaddress.addressLine2, N'') AS Expr1) AS Work_AddressLine2,
                 (SELECT ISNULL
                                  ((SELECT Name
                                   FROM    [${dbxschemaname}].city
                                   WHERE (id = workaddress.City_id)), N'') AS Expr1) AS Work_CityName,
                 (SELECT ISNULL(workaddress.City_id, N'') AS Expr1) AS Work_CityID,
                 (SELECT ISNULL
                                  ((SELECT Name
                                   FROM    [${dbxschemaname}].region
                                   WHERE (id = workaddress.Region_id)), N'') AS Expr1) AS Work_StateName,
                 (SELECT ISNULL(workaddress.Region_id, N'') AS Expr1) AS Work_StateID,
                 (SELECT ISNULL
                                  ((SELECT Name
                                   FROM    [${dbxschemaname}].country
                                   WHERE (id IN
                                                    (SELECT Country_id
                                                    FROM    [${dbxschemaname}].city
                                                    WHERE (id = workaddress.City_id)))), N'') AS Expr1) AS Work_CountryName,
                 (SELECT ISNULL
                                  ((SELECT id
                                   FROM    [${dbxschemaname}].country
                                   WHERE (id IN
                                                    (SELECT Country_id
                                                    FROM    [${dbxschemaname}].city
                                                    WHERE (id = workaddress.City_id)))), N'') AS Expr1) AS Work_CountryID,
                 (SELECT ISNULL(workaddress.zipCode, N'') AS Expr1) AS Work_Zipcode,
                 (SELECT string_agg(lobId, ',') AS Expr1
                 FROM    [${dbxschemaname}].userlob
                 WHERE ([${dbxschemaname}].systemuser.id = userId)) AS lobId,
                 (SELECT string_agg(displayName, ',') AS Expr1
                 FROM    [${dbxschemaname}].lobtext
                 WHERE (lobId IN
                                  (SELECT lobId
                                  FROM    [${dbxschemaname}].userlob
                                  WHERE ([${dbxschemaname}].systemuser.id = userId)))) AS lobName,
                 (SELECT Name
                 FROM    [${dbxschemaname}].location
                 WHERE (workaddress.id = Address_id)) AS branchName
FROM   [${dbxschemaname}].systemuser INNER JOIN
             [${dbxschemaname}].status ON [${dbxschemaname}].systemuser.Status_id = [${dbxschemaname}].status.id LEFT OUTER JOIN
             [${dbxschemaname}].address AS homeaddress ON homeaddress.id IN
                 (SELECT Address_id
                 FROM    [${dbxschemaname}].useraddress
                 WHERE ([${dbxschemaname}].systemuser.id = User_id) AND (Type_id = 'ADR_TYPE_HOME')) LEFT OUTER JOIN
             [${dbxschemaname}].address AS workaddress ON workaddress.id IN
                 (SELECT Address_id
                 FROM    [${dbxschemaname}].useraddress
                 WHERE ([${dbxschemaname}].systemuser.id = User_id) AND (Type_id = 'ADR_TYPE_WORK'));
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[internaluserdetails_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internaluserdetails_view] AS
SELECT [${dbxschemaname}].systemuser.id, [${dbxschemaname}].systemuser.Username, [${dbxschemaname}].systemuser.Email, [${dbxschemaname}].systemuser.Status_id, [${dbxschemaname}].systemuser.Password, [${dbxschemaname}].systemuser.Code, [${dbxschemaname}].systemuser.FirstName, [${dbxschemaname}].systemuser.MiddleName, [${dbxschemaname}].systemuser.LastName, 
             [${dbxschemaname}].systemuser.FailedCount, [${dbxschemaname}].systemuser.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].systemuser.LastPasswordChangedts, [${dbxschemaname}].systemuser.ResetpasswordLink, [${dbxschemaname}].systemuser.ResetPasswordExpdts, [${dbxschemaname}].systemuser.lastLogints, [${dbxschemaname}].systemuser.createdby, [${dbxschemaname}].systemuser.createdts, [${dbxschemaname}].systemuser.modifiedby, 
             [${dbxschemaname}].systemuser.lastmodifiedts, [${dbxschemaname}].systemuser.synctimestamp, [${dbxschemaname}].systemuser.softdeleteflag, [${dbxschemaname}].userrole.Role_id, [${dbxschemaname}].userrole.hasSuperAdminPrivilages, [${dbxschemaname}].role.Name AS Role_Name, [${dbxschemaname}].role.Status_id AS Role_Status_id
FROM   [${dbxschemaname}].systemuser LEFT OUTER JOIN
             [${dbxschemaname}].userrole ON [${dbxschemaname}].userrole.User_id = [${dbxschemaname}].systemuser.id LEFT OUTER JOIN
             [${dbxschemaname}].role ON [${dbxschemaname}].userrole.Role_id = [${dbxschemaname}].role.id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[systemuser_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[systemuser_view] AS
SELECT id AS UserID, Username, FirstName, MiddleName, LastName, Email, Status_id, companyLegalUnit AS companyLegalUnit, modifiedby AS UpdatedBy, lastmodifiedts AS LastModifiedTimeStamp
FROM   [${dbxschemaname}].systemuser;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[roles_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[roles_view] AS
SELECT [${dbxschemaname}].role.id AS role_id, [${dbxschemaname}].role.Type_id AS roleType_id, [${dbxschemaname}].role.Name AS role_Name, [${dbxschemaname}].role.Description AS role_Desc, [${dbxschemaname}].role.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].role.Status_id, [${dbxschemaname}].status.Description AS Status_Desc,
                 (SELECT COUNT(Role_id) AS Expr1
                 FROM    [${dbxschemaname}].rolepermission
                 WHERE (Role_id = [${dbxschemaname}].role.id)) AS permission_Count,
                 (SELECT COUNT(User_id) AS Expr1
                 FROM    [${dbxschemaname}].userrole
                 WHERE (Role_id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].rolepermission
                                  WHERE (Role_id = [${dbxschemaname}].role.id)))) AS Users_Count, CASE (role.Status_id) WHEN N'SID_ACTIVE' THEN N'Active' ELSE N'Inactive' END AS Status
FROM   [${dbxschemaname}].role INNER JOIN
             [${dbxschemaname}].status ON [${dbxschemaname}].role.Status_id = [${dbxschemaname}].status.id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[internal_role_to_serviceDefinition_mapping_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internal_role_to_serviceDefinition_mapping_view] AS
SELECT [${dbxschemaname}].servicedefinition.id AS ServiceDefinition_id, [${dbxschemaname}].servicedefinition.name AS ServiceDefinition_Name, [${dbxschemaname}].servicedefinition.description AS ServiceDefinition_Description, [${dbxschemaname}].servicedefinition.serviceType AS ServiceDefinition_Type_id, 
             [${dbxschemaname}].servicedefinition.status AS ServiceDefinition_Status_id, [${dbxschemaname}].role.id AS InternalRole_id, [${dbxschemaname}].role.Type_id AS InternalRole_Type_id, [${dbxschemaname}].role.Status_id AS InternalRole_Status_id, [${dbxschemaname}].role.Name AS InternalRole_Name, 
             [${dbxschemaname}].role.Description AS InternalRole_Description, [${dbxschemaname}].role.companyLegalUnit AS companyLegalUnit
FROM   [${dbxschemaname}].userroleservicedefinition LEFT OUTER JOIN
             [${dbxschemaname}].servicedefinition ON [${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].userroleservicedefinition.servicedefinitionId LEFT OUTER JOIN
             [${dbxschemaname}].role ON [${dbxschemaname}].role.id = [${dbxschemaname}].userroleservicedefinition.UserRole_id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[roleuser_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[roleuser_view] AS
SELECT [${dbxschemaname}].userrole.User_id, [${dbxschemaname}].userrole.Role_id, [${dbxschemaname}].systemuser.Status_id, [${dbxschemaname}].systemuser.Username, [${dbxschemaname}].systemuser.FirstName, [${dbxschemaname}].systemuser.MiddleName, [${dbxschemaname}].systemuser.LastName, [${dbxschemaname}].systemuser.Email, [${dbxschemaname}].systemuser.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].systemuser.modifiedby AS UpdatedBy, 
             [${dbxschemaname}].systemuser.lastmodifiedts AS LastModifiedTimeStamp
FROM   [${dbxschemaname}].userrole INNER JOIN
             [${dbxschemaname}].systemuser ON [${dbxschemaname}].userrole.User_id = [${dbxschemaname}].systemuser.id;
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[outagemessage_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [${dbxschemaname}].[outagemessage_view] (
   [id], 
   [name], 
   [startTime], 
   [endTime], 
   [app_id], 
   [app_name], 
   [Status_id], 
   [MessageText], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts],
   [companyLegalUnit])
AS 
   SELECT 
      outagemessage.id AS id, 
      outagemessage.name AS name, 
      outagemessage.startTime AS startTime, 
      outagemessage.endTime AS endTime,
      
         (
            SELECT String_agg(CAST(outagemessageapp.App_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].outagemessageapp
            WHERE (outagemessageapp.Outagemessage_id = outagemessage.id)
         ) AS app_id, 
      
         (
          
            SELECT String_Agg(CAST(app.Name as nvarchar(max)),',')
            FROM [${dbxschemaname}].app
            WHERE app.id IN 
               (
                  SELECT outagemessageapp.App_id
                  FROM [${dbxschemaname}].outagemessageapp
                  WHERE (outagemessageapp.Outagemessage_id = outagemessage.id)
               )
         
         ) AS app_name, 
      outagemessage.Status_id AS Status_id, 
      outagemessage.MessageText AS MessageText, 
      outagemessage.createdby AS createdby, 
      outagemessage.modifiedby AS modifiedby, 
      outagemessage.createdts AS createdts, 
      outagemessage.lastmodifiedts AS lastmodifiedts,
      outagemessage.companyLegalUnit AS companyLegalUnit
   FROM [${dbxschemaname}].outagemessage;
GO



DROP VIEW IF EXISTS [${dbxschemaname}].[customerservice_communication_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customerservice_communication_view]
      AS 
         SELECT 
            
               [${dbxschemaname}].customerservice.id AS Service_id, 
               [${dbxschemaname}].customerservice.Name AS Service_Name, 
               [${dbxschemaname}].customerservice.Status_id AS Service_Status_id, 
               [${dbxschemaname}].customerservice.Description AS Service_Description, 
               [${dbxschemaname}].customerservice.softdeleteflag AS Service_SoftDeleteFlag, 
               [${dbxschemaname}].servicecommunication.id AS ServiceCommunication_id, 
               [${dbxschemaname}].servicecommunication.Type_id AS ServiceCommunication_Typeid, 
               [${dbxschemaname}].servicecommunication.Value AS ServiceCommunication_Value, 
               [${dbxschemaname}].servicecommunication.Extension AS ServiceCommunication_Extension, 
               [${dbxschemaname}].servicecommunication.Description AS ServiceCommunication_Description, 
               [${dbxschemaname}].servicecommunication.Status_id AS ServiceCommunication_Status_id, 
               [${dbxschemaname}].servicecommunication.Priority AS ServiceCommunication_Priority, 
               [${dbxschemaname}].servicecommunication.createdby AS ServiceCommunication_createdby, 
               [${dbxschemaname}].servicecommunication.modifiedby AS ServiceCommunication_modifiedby, 
               [${dbxschemaname}].servicecommunication.createdts AS ServiceCommunication_createdts, 
               [${dbxschemaname}].servicecommunication.lastmodifiedts AS ServiceCommunication_lastmodifiedts, 
               [${dbxschemaname}].servicecommunication.synctimestamp AS ServiceCommunication_synctimestamp, 
			   [${dbxschemaname}].servicecommunication.companyLegalUnit AS companyLegalUnit,
               [${dbxschemaname}].servicecommunication.softdeleteflag AS ServiceCommunication_SoftDeleteFlag
         FROM ([${dbxschemaname}].servicecommunication 
            JOIN customerservice ON (([${dbxschemaname}].servicecommunication.Service_id = [${dbxschemaname}].customerservice.id)));
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
	  
	  
	  
	  
DROP VIEW IF EXISTS [${dbxschemaname}].[actiondependency_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[actiondependency_view] AS
SELECT 
        [${dbxschemaname}].[dependentactions].[dependentactionId] AS [actionName],
        [${dbxschemaname}].[dependentactions].[actionId] AS [dependencyAction],
        [${dbxschemaname}].[dependentactions].[featureId] AS [featureId],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus],
		[${dbxschemaname}].[feature].[companyLegalUnit] AS [companyLegalUnit]
    FROM
        ((dependentactions
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[dependentactions].[dependentactionId] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[dependentactions].[featureId] = [${dbxschemaname}].[feature].[id])));
GO
		
		
		
DROP VIEW IF EXISTS [${dbxschemaname}].[get_all_features_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[get_all_features_view] AS
    SELECT 
        [${dbxschemaname}].[feature].[id] AS [id],
        [${dbxschemaname}].[feature].[name] AS [name],
        [${dbxschemaname}].[feature].[description] AS [description],
        [${dbxschemaname}].[feature].[Type_id] AS [Type_id],
        [${dbxschemaname}].[feature].[Service_Fee] AS [Service_Fee],
        [${dbxschemaname}].[feature].[Status_id] AS [Status_id],
		[${dbxschemaname}].[feature].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [roleTypeId],
        [${dbxschemaname}].[featuredisplaynamedescription].[Locale_id] AS [languageId],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                [featureaction]
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'MONETARY'))) AS [monetaryActions],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                featureaction
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'NON_MONETARY'))) AS nonMonetaryActions
    FROM
        (((feature
        LEFT JOIN [featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [featuredisplaynamedescription] ON (([${dbxschemaname}].[featuredisplaynamedescription].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [membergrouptype] ON (([${dbxschemaname}].[featureroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])));
GO
		
		
		
		
DROP VIEW IF EXISTS [${dbxschemaname}].[limitgroups_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[limitgroups_view] AS
    SELECT 
        [${dbxschemaname}].[limitgroup].[id] AS [id],
        [${dbxschemaname}].[limitgroup].[name] AS [name],
		[${dbxschemaname}].[limitgroup].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[limitgroup].[description] AS [description],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[localeId] AS [localeId],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[displayDescription] AS [displayDescription]
    FROM
        ([${dbxschemaname}].[limitgroup]
       LEFT JOIN [${dbxschemaname}].[limitgroupdisplaynamedescription] ON (([${dbxschemaname}].[limitgroupdisplaynamedescription].[limitGroupId] = [${dbxschemaname}].[limitgroup].[id]))) ;
GO
	   
	   

DROP VIEW IF EXISTS [${dbxschemaname}].[internaluserskc_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internaluserskc_view] AS
    SELECT 
        [internalusertype].[userId] AS [User_id],
		[internalusertype].[companyLegalUnit] AS [companyLegalUnit],
        [internalusertype].[userType] AS [UserType],
		(SELECT TOP (1) internalusermanager.manager FROM internalusermanager 
		WHERE ([internalusertype].[userId] = [internalusermanager].[userId])) AS [ReportingManager],
        (SELECT 
			String_agg(CAST([userlob].[lobId] AS nvarchar(max)), ',')  
            FROM
                [${dbxschemaname}].userlob
            WHERE
                ([internalusertype].[userId] = [userlob].[userId])) AS [lobId],
        (SELECT 
				String_agg(CAST(lobtext.displayName AS nvarchar(max)), ',')                 
            FROM
                [${dbxschemaname}].[lobtext]
            WHERE
                [lobtext].[lobId] IN (SELECT 
                        [userlob].[lobId]
                    FROM
                        [${dbxschemaname}].userlob
                    WHERE
                        ([internalusertype].[userId] = [userlob].[userId]))) AS [lobName],
        (SELECT 
                [usertype].[name]
            FROM
                [usertype]
            WHERE
                [usertype].[id] IN (SELECT TOP (1)
                        [internalusertype].[userType]
                    FROM
                        internalusertype
                    WHERE
                        ([internalusertype].[userId] = [internalusertype].[userId]))) AS [userTypeName],
        (SELECT CASE WHEN [workaddress].[id] IS NULL THEN  '' ELSE NULL END) AS [Work_AddressID],
        (SELECT CASE WHEN [workaddress].[addressLine1] IS NULL THEN  '' ELSE NULL END) AS [Work_AddressLine1],
        (SELECT CASE WHEN [workaddress].[addressLine2] IS NULL THEN  '' ELSE NULL END) AS [Work_AddressLine2],
        (SELECT 
                CASE WHEN (SELECT 
                                    [city].[Name]
                                FROM
                                    [${dbxschemaname}].city
                                WHERE
                                    ([city].[id] = [workaddress].[City_id])) IS NULL THEN 
                            '' ELSE NULL END
            ) AS [Work_CityName],
        (SELECT CASE WHEN [workaddress].[City_id] IS NULL THEN  '' ELSE NULL END) AS [Work_CityID],
        (SELECT 
                CASE WHEN (SELECT 
                                    [region].[Name]
                                FROM
                                    [${dbxschemaname}].region
                                WHERE
                                    ([region].[id] = [workaddress].[Region_id])) IS NULL THEN 
                            '' ELSE NULL END
            ) AS [Work_StateName],
        (SELECT CASE WHEN [workaddress].[Region_id] IS NULL THEN  '' ELSE NULL END) AS [Work_StateID],
        (SELECT 
                CASE WHEN (SELECT 
                                    [country].[Name]
                                FROM
                                    [${dbxschemaname}].country
                                WHERE
                                    [country].[id] IN (SELECT 
                                            [city].[Country_id]
                                        FROM
                                            [${dbxschemaname}].city
                                        WHERE
                                            ([city].[id] = [workaddress].[City_id]))) IS NULL THEN 
                            '' ELSE NULL END
            ) AS [Work_CountryName],
        (SELECT 
                CASE WHEN (SELECT 
                                    [country].[id]
                                FROM
                                    [${dbxschemaname}].country
                                WHERE
                                    [country].[id] IN (SELECT 
                                            [city].[Country_id]
                                        FROM
                                            [${dbxschemaname}].city
WHERE
                                            ([city].[id] = [workaddress].[City_id]))) IS NULL THEN 
                            '' ELSE NULL END
            ) AS [Work_CountryID],
        (SELECT CASE WHEN [workaddress].[zipCode] IS NULL THEN  '' ELSE NULL END) AS [Work_Zipcode],
        (SELECT 
                [location].[Name]
            FROM
                [${dbxschemaname}].location
            WHERE
                ([workaddress].[id] = [location].[Address_id])) AS [branchName],
(SELECT (workaddress.addressLine1)+ ', '+ ISNULL((workaddress.addressLine2),'')+ ', '+ 
				  (SELECT (city.Name) AS Name
						 FROM [${dbxschemaname}].city
						 WHERE (city.id = (workaddress.City_id)))+', '+ 
				  (SELECT (region.Name) AS Name
						 FROM [${dbxschemaname}].region
						 WHERE (region.id = (workaddress.Region_id)))+', '+ 
				  (SELECT (country.Name) AS Name
						 FROM [${dbxschemaname}].country
						 WHERE country.id IN (SELECT (city.Country_id) AS Country_id
							   FROM [${dbxschemaname}].city
							   WHERE (city.id = (workaddress.City_id))))+', '+(workaddress.zipCode)
			 ) AS Work_Addr
		      FROM
        (( internalusertype
        JOIN  internalusermanager ON ((internalusertype.userId = internalusermanager.userId)))
        LEFT JOIN [${dbxschemaname}].address  AS workaddress 
		  ON (workaddress.id IN 
		(
				SELECT useraddress.Address_id
				FROM [${dbxschemaname}].useraddress
				WHERE ((internalusertype.userId = useraddress.User_id) AND (useraddress.Type_id = 'ADR_TYPE_WORK'))
			 )));
GO
			 
		  
	  
	  
DROP VIEW IF EXISTS [${dbxschemaname}].[customer_request_csr_count_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_request_csr_count_view] (
   [customerrequest_Status_id], 
   [status_Description], 
   [customerrequest_assignedTo], 
   [request_count],[companyLegalUnit])
AS 
   SELECT customerrequest.Status_id AS customerrequest_Status_id, 
      (
        
         SELECT TOP (1) (status.Description) AS Description
         FROM [${dbxschemaname}].status
         WHERE status.id = customerrequest.Status_id
      ) AS status_Description, customerrequest.AssignedTo AS customerrequest_assignedTo, count_big(customerrequest.id) AS request_count,
	  customerrequest.companyLegalUnit AS companyLegalUnit
   FROM [${dbxschemaname}].customerrequest
   GROUP BY customerrequest.AssignedTo, customerrequest.Status_id, customerrequest.companyLegalUnit;
GO
   

DROP VIEW IF EXISTS [${dbxschemaname}].[servicedefinition_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[servicedefinition_view] AS
     SELECT 
    [${dbxschemaname}].[servicedefinition].[id] AS [id],
    [${dbxschemaname}].[servicedefinition].[name] AS [name],
    [${dbxschemaname}].[servicedefinition].[description] AS [description],
    [${dbxschemaname}].[servicedefinition].[serviceType] AS [serviceType],
    [${dbxschemaname}].[servicedefinition].[status] AS [status],
	[${dbxschemaname}].[servicedefinition].[companyLegalUnit] AS [companyLegalUnit],
    (SELECT COUNT([${dbxschemaname}].[groupservicedefinition].[Group_id]) FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfRoles],
	(SELECT COUNT([${dbxschemaname}].[membergroup].[id]) FROM [${dbxschemaname}].[membergroup] WHERE Status_id LIKE 'SID_ACTIVE' AND id in (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM 
		[${dbxschemaname}].[groupservicedefinition] WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]))) AS [numberOfActiveRoles],
    (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE (([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]) AND ([${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = 1))) AS [defaultRole],
    (SELECT COUNT(DISTINCT [${dbxschemaname}].[servicedefinition_features_actions_view].[featureId]) FROM [${dbxschemaname}].[servicedefinition_features_actions_view]
        WHERE (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinition_features_actions_view].[serviceDefinitionId]) AND ([${dbxschemaname}].[servicedefinition_features_actions_view].[softdelete] = '0'))) AS [numberOfFeatures],
    (SELECT COUNT([${dbxschemaname}].[contract].[id]) FROM [${dbxschemaname}].[contract] 
      WHERE ([${dbxschemaname}].[contract].[servicedefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfContracts]
  FROM [${dbxschemaname}].[servicedefinition];
GO
  
  
DROP VIEW IF EXISTS [${dbxschemaname}].[internal_role_to_customer_role_mapping_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[internal_role_to_customer_role_mapping_view] (
   [CustomerRole_id], 
   [CustomerRole_Name], 
   [CustomerRole_Description], 
   [CustomerRole_Type_id], 
   [CustomerRole_Status_id], 
   [InternalRole_id], 
   [InternalRole_Type_id], 
   [InternalRole_Status_id], 
   [InternalRole_Name],
   [companyLegalUnit], 
   [InternalRole_Description])
AS 
   SELECT 
      membergroup.id AS CustomerRole_id, 
      membergroup.Name AS CustomerRole_Name, 
      membergroup.Description AS CustomerRole_Description, 
      membergroup.Type_id AS CustomerRole_Type_id, 
      membergroup.Status_id AS CustomerRole_Status_id, 
      role.id AS InternalRole_id, 
      role.Type_id AS InternalRole_Type_id, 
      role.Status_id AS InternalRole_Status_id, 
      role.Name AS InternalRole_Name, 
	  role.companyLegalUnit AS companyLegalUnit,
      role.Description AS InternalRole_Description
   FROM (([${dbxschemaname}].userrolecustomerrole 
      LEFT JOIN [${dbxschemaname}].membergroup 
      ON ((membergroup.id = userrolecustomerrole.CustomerRole_id))) 
      LEFT JOIN [${dbxschemaname}].role 
      ON ((role.id = userrolecustomerrole.UserRole_id)));
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[configuration_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [${dbxschemaname}].[configuration_view]
      AS 
         SELECT 
            
               configurations.configuration_id AS configuration_id, 
               configurations.bundle_id AS bundle_id, 
               configurationbundles.bundle_name AS bundle_name, 
               configurations.config_type AS [type], 
               configurations.config_key AS [key], 
               configurations.description AS description, 
               configurations.config_value AS [value], 
               configurations.target AS target, 
               configurations.isPreLoginConfiguration AS isPreLoginConfiguration,
			   configurations.companyLegalUnit AS companyLegalUnit,
               configurationbundles.app_id AS app_id
         FROM [${dbxschemaname}].configurations 
            LEFT JOIN [${dbxschemaname}].configurationbundles ON configurationbundles.bundle_id = configurations.bundle_id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[group_features_actions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[group_features_actions_view] AS
    SELECT 
        [${dbxschemaname}].[groupactionlimit].[Group_id] AS [Group_id],
        [${dbxschemaname}].[groupactionlimit].[Action_id] AS [Action_id],
        [${dbxschemaname}].[groupactionlimit].[LimitType_id] AS [LimitType_id],
		[${dbxschemaname}].[groupactionlimit].[value] AS [value],
        [${dbxschemaname}].[groupactionlimit].[id] AS [groupactionlimit_id],
        [${dbxschemaname}].[groupactionlimit].[softdeleteflag] AS [softdelete],
		[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
        [${dbxschemaname}].[membergroup].[Name] AS [Group_name],
        [${dbxschemaname}].[membergroup].[Description] AS [Group_description],
        [${dbxschemaname}].[featureaction].[name] AS [Action_name],
        [${dbxschemaname}].[featureaction].[description] AS [Action_description],
        [${dbxschemaname}].[featureaction].[Type_id] AS [Action_Type_id],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [Feature_id],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [Action_displaysequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [Action_dependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
		[${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
		[${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
		[${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[name] AS [Feature_name],
        [${dbxschemaname}].[feature].[description] AS [Feature_description],
        [${dbxschemaname}].[feature].[Type_id] AS [Feature_Type_id],
        [${dbxschemaname}].[feature].[Status_id] AS [Feature_Status_id],
        [${dbxschemaname}].[feature].[DisplaySequence] AS [Feature_displaysequence],
		[${dbxschemaname}].[feature].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[feature].[isPrimary] AS [Feature_isPrimary]
    FROM
        (((((([${dbxschemaname}].[groupactionlimit]
        LEFT JOIN [${dbxschemaname}].[membergroup] ON (([${dbxschemaname}].[membergroup].[id] = [${dbxschemaname}].[groupactionlimit].[Group_id])))
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[groupactionlimit].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
		LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))	
		LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [limitgroup].[id])))
		LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [actionlevel].[id])))
        ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;
GO	

DROP VIEW IF EXISTS [${dbxschemaname}].[servicedefinition_features_actions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[servicedefinition_features_actions_view] AS
    SELECT 
    [${dbxschemaname}].[servicedefinitionactionlimit].[serviceDefinitionId] AS [serviceDefinitionId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[actionId] AS [actionId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[limitTypeId] AS [limitTypeId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[value] AS [value],
    [${dbxschemaname}].[servicedefinitionactionlimit].[id] AS [serviceDefinitionActionLimitId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[softdeleteflag] AS [softdelete],
    [${dbxschemaname}].[servicedefinition].[serviceType] AS [serviceType],
    [${dbxschemaname}].[servicedefinition].[name] AS [serviceDefinitionName],
    [${dbxschemaname}].[servicedefinition].[description] AS [serviceDefinitionDescription],
    [${dbxschemaname}].[featureaction].[name] AS [actionName],
    [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
    [${dbxschemaname}].[featureaction].[Type_id] AS [actionTypeId],
    [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
    [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
    [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
    [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
    [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaysequence],
    [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
    [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
	[${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
	[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
    [${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
    [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
    [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
    [${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
	[${dbxschemaname}].[dependentactions].[dependentactionId] AS [dependentactionId],
    [${dbxschemaname}].[dependentactions].[featureId] AS [dependentFeatureId],
    [${dbxschemaname}].[dependentactions].[actionName] AS [dependentActionName],
    [${dbxschemaname}].[dependentactions].[featureName] AS [dependentFeatureName],
    [${dbxschemaname}].[feature].[name] AS [featureName],
    [${dbxschemaname}].[feature].[description] AS [featureDescription],
    [${dbxschemaname}].[feature].[Type_id] AS [featureTypeId],
    [${dbxschemaname}].[feature].[Status_id] AS [featureStatusId],
    [${dbxschemaname}].[feature].[DisplaySequence] AS [featureDisplaysequence],
	[${dbxschemaname}].[feature].[companyLegalUnit] AS [companyLegalUnit],
    [${dbxschemaname}].[feature].[isPrimary] AS [featureIsPrimary]
  FROM
    (((((((servicedefinitionactionlimit
    LEFT JOIN [${dbxschemaname}].[servicedefinition] ON (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinitionactionlimit].[serviceDefinitionId])))
    LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[servicedefinitionactionlimit].[actionId])))
	LEFT JOIN [${dbxschemaname}].[dependentactions] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions].[actionId])))
    LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
	LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
	LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
    LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])));
GO
	
DROP VIEW IF EXISTS [${dbxschemaname}].[get_feature_actions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[get_feature_actions_view] AS
    SELECT 
        [${dbxschemaname}].[featureaction].[id] AS [actionId],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[notes] AS [notes],
        [${dbxschemaname}].[featureaction].[Type_id] AS [typeId],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaySequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[featureactionroletype].[RoleType_id] AS [actionType],
        [${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
        [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus],
        [${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[description] AS [featureDescription],
        [${dbxschemaname}].[feature].[Type_id] AS [featureType],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [featureGroup],
        [${dbxschemaname}].[feature].[DisplaySequence] AS featureDisplaySequence,
        [${dbxschemaname}].[feature].[isPrimary] AS [isFeaturePrimary],
        [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
        [${dbxschemaname}].[actiondisplaynamedescription].[Locale_id] AS [localeId],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[actionlimit].[LimitType_id] AS [limitTypeId],
        [${dbxschemaname}].[actionlimit].[value] AS [value],
        [${dbxschemaname}].[dependentactions_view].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[dependentactions_view].[featureName] AS [dependentFeatureName],
        [${dbxschemaname}].[dependentactions_view].[actionName] AS [dependentActionName],
		[${dbxschemaname}].[dependentactions_view].[featureId] AS [dependentFeatureId],
        [${dbxschemaname}].[termandcondition].[Code] AS [termsAndConditionCode],
        [${dbxschemaname}].[termandcondition].[Title] AS [termsAndConditionTitle],
        [${dbxschemaname}].[termandcondition].[Description] AS [termsAndConditionDescription],
		[${dbxschemaname}].[termandcondition].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName]
    FROM
        (((((((((((featureaction
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
        LEFT JOIN [${dbxschemaname}].[actiondisplaynamedescription] ON (([${dbxschemaname}].[actiondisplaynamedescription].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[featureactionroletype].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[termandcondition] ON (([${dbxschemaname}].[featureaction].[TermsAndConditions_id] = [${dbxschemaname}].[termandcondition].[id])))
        LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])))
        LEFT JOIN [${dbxschemaname}].[dependentactions_view] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions_view].[actionId])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureactionroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
		ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;;
GO

		
DROP VIEW IF EXISTS [${dbxschemaname}].[policy_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[policy_view] (
   [id], 
   [Type_id], 
   [Locale], 
   [companyLegalUnit],
   [PolicyContent])
AS 
   SELECT policycontent.id AS id, policytype.id AS Type_id, policycontent.Locale_Code AS Locale, policycontent.companyLegalUnit AS companyLegalUnit, policycontent.Content AS PolicyContent
   FROM ([${dbxschemaname}].policycontent 
      INNER JOIN [${dbxschemaname}].policytype 
      ON ((policycontent.Type_id = policytype.id)));
GO

	  
DROP VIEW IF EXISTS [${dbxschemaname}].[locationservices_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE
      VIEW [${dbxschemaname}].[locationservices_view]
      AS 
         SELECT 
            
               location.id AS Location_id, 
               location.Name AS Location_Name, 
               location.DisplayName AS Location_Display_Name, 
               location.Description AS Location_Description, 
               location.PhoneNumber AS Location_Phone_Number, 
               location.EmailId AS Location_EmailId, 
               address.latitude AS Location_Latitude, 
               address.logitude AS Location_Longitude, 
               address.id AS Location_Address_id, 
               location.IsMainBranch AS Location_IsMainBranch, 
               location.isMobile AS Location_IsMobile, 
               location.Status_id AS Location_Status_id, 
               location.Type_id AS Location_Type_id, 
               location.Code AS Location_Code, 
               location.softdeleteflag AS Location_DeleteFlag, 
               location.WorkSchedule_id AS Location_WorkScheduleId, 
               facility.id AS Facility_id, 
               facility.code AS Facility_code, 
               facility.name AS Facility_name, 
			   facility.companyLegalUnit AS companyLegalUnit,
               facility.description AS Facility_description, 
               
                  (
                     SELECT 
                        string_agg(currency.code ,',')
                     FROM [${dbxschemaname}].currency 
                        JOIN locationcurrency
                     on currency.code = locationcurrency.currency_code AND locationcurrency.Location_id = location.id
                  ) AS currencies, 
               
                  (
                     SELECT 
                        string_agg(CAST(customersegment.id as nvarchar(max)) , ',')
                     FROM customersegment 
                        JOIN locationcustomersegment
                     on customersegment.id = locationcustomersegment.segment_id AND locationcustomersegment.Location_id = location.id
                  ) AS Location_CustomerSegement, 
               weekday.StartTime AS Weekday_StartTime, 
               weekday.EndTime AS Weekday_EndTime, 
               sunday.StartTime AS Sunday_StartTime, 
               sunday.EndTime AS Sunday_EndTime, 
               saturday.StartTime AS Saturday_StartTime, 
               saturday.EndTime AS Saturday_EndTime, 
               (address.addressLine1+
                  ', '+
                  isnull(address.cityName,'')+ 
                  ', '+
                     (
                        SELECT 
                           region.Name
                        FROM region
                        WHERE (region.id = address.Region_id)
                     )+
                  ', '+
                     (
                        SELECT 
                           country.Name
                        FROM country
                        WHERE country.id IN 
                           (
                              SELECT 
                                 region.Country_id
                              FROM region
                              WHERE (region.id = address.Region_id)
                           )
                     )+
                  ', '+
                  address.zipCode) AS ADDRESS
         FROM [${dbxschemaname}].location 
            LEFT JOIN [${dbxschemaname}].locationfacility ON location.id = locationfacility.Location_id
            LEFT JOIN [${dbxschemaname}].facility ON locationfacility.facility_id = facility.id
            LEFT JOIN [${dbxschemaname}].dayschedule  weekday ON location.WorkSchedule_id = weekday.WorkSchedule_id AND weekday.WeekDayName = 'MONDAY'
     LEFT JOIN [${dbxschemaname}].dayschedule  sunday ON location.WorkSchedule_id = sunday.WorkSchedule_id AND sunday.WeekDayName = 'SUNDAY'
            LEFT JOIN [${dbxschemaname}].dayschedule  saturday ON location.WorkSchedule_id = saturday.WorkSchedule_id AND saturday.WeekDayName = 'SATURDAY' 
            JOIN [${dbxschemaname}].address ON address.id = location.Address_id;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[locationfacility_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[locationfacility_view] (
   [Location_id], 
   [Facility_id], 
   [Facility_code], 
   [Facility_Name], 
   [Facility_description],
   [companyLegalUnit],   
   [Facility_facilitytype])
AS 
   SELECT 
      location.id AS Location_id, 
      facility.id AS Facility_id, 
      facility.code AS Facility_code, 
      facility.name AS Facility_Name, 
      facility.description AS Facility_description,
      facility.companyLegalUnit AS companyLegalUnit,	  
      facility.facilitytype AS Facility_facilitytype
   FROM (([${dbxschemaname}].location 
      LEFT JOIN [${dbxschemaname}].locationfacility 
      ON ((location.id = locationfacility.Location_id))) 
      INNER JOIN [${dbxschemaname}].facility 
      ON ((locationfacility.facility_id = facility.id)));
GO

	  
DROP VIEW IF EXISTS [${dbxschemaname}].[location_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[location_view] (
   [id], 
   [Name], 
   [Code], 
   [Description], 
   [PhoneNumber], 
   [Type_id], 
   [companyLegalUnit],
   [Status_id])
AS 
   SELECT DISTINCT 
      location.id AS id, 
      location.Name AS Name, 
      location.Code AS Code, 
      location.Description AS Description, 
      location.PhoneNumber AS PhoneNumber, 
	  location.companyLegalUnit AS companyLegalUnit,
      location.Type_id AS Type_id, 
      CASE location.Status_id
         WHEN N'SID_ACTIVE' THEN N'Active'
         ELSE N'Inactive'
      END AS Status_id
   FROM [${dbxschemaname}].location;
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[groups_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[groups_view] AS 
SELECT [${dbxschemaname}].[membergroup].[id] AS [Group_id],
[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
[${dbxschemaname}].[membergrouptype].[description] AS [Type_Name],
[${dbxschemaname}].[membergroup].[Description] AS [Group_Desc],
[${dbxschemaname}].[membergroup].[Status_id] AS [Status_id],
[${dbxschemaname}].[membergroup].[companyLegalUnit] AS [companyLegalUnit],
[${dbxschemaname}].[membergroup].[Name] AS [Group_Name],
[${dbxschemaname}].[membergroup].[isEAgreementActive] AS [isEAgreementActive],
(case [${dbxschemaname}].[membergroup].[isApplicabletoAllServices] when '1' then 'true' else 'false' end) As [isApplicabletoAllServices],
(SELECT COUNT([${dbxschemaname}].[groupentitlement].[Group_id]) FROM [${dbxschemaname}].[groupentitlement] WHERE
 ([${dbxschemaname}].[groupentitlement].[Group_id] = [${dbxschemaname}].[membergroup].[id])) AS [Entitlements_Count],
 (SELECT COUNT(distinct([${dbxschemaname}].[customergroup].[Customer_id])) FROM [${dbxschemaname}].[customergroup] 
 WHERE ([${dbxschemaname}].[customergroup].[Group_id] = [membergroup].[id])) AS [Customers_Count],
 (case [${dbxschemaname}].[membergroup].[Status_id] when 'SID_ACTIVE' then 'Active' else 'Inactive' end) AS [Status] 
 FROM ([${dbxschemaname}].[membergroup] join [membergrouptype] ON(([${dbxschemaname}].[membergroup].[Type_id] = [membergrouptype].[id])));
GO
 

DROP VIEW IF EXISTS [${dbxschemaname}].[customergroups_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[customergroups_view] AS
SELECT  COUNT(distinct([${dbxschemaname}].[customergroup].[Customer_id])) AS [customerCount], 
 [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] AS [servicedefinitionId],
 [${dbxschemaname}].[groupservicedefinition].[companyLegalUnit] AS [companyLegalUnit],
 [${dbxschemaname}].[groupservicedefinition].[Group_id] AS [groupId] FROM
 [${dbxschemaname}].[groupservicedefinition] 
 join  [${dbxschemaname}].[contract]  ON  [${dbxschemaname}].[contract].[servicedefinitionId]=  [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] 
 join  [${dbxschemaname}].[customergroup]   ON [${dbxschemaname}].[customergroup].[Group_id] = [${dbxschemaname}].[groupservicedefinition].[Group_id] AND  
 [${dbxschemaname}].[customergroup].[contractId]=[${dbxschemaname}].[contract].[id]
 group by  [${dbxschemaname}].[groupservicedefinition].[Group_id],  [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId],  [${dbxschemaname}].[groupservicedefinition].[companyLegalUnit];
GO

		
DROP VIEW IF EXISTS [${dbxschemaname}].[feature_actions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[feature_actions_view] (
   [id], 
   [Feature_id], 
   [action_name], 
   [action_description], 
   [isAccountLevel], 
   [isMFAApplicable], 
   [isPrimary], 
   [notes], 
   [action_Type_id], 
   [action_displaysequence], 
   [action_dependency], 
   [feature_status_id], 
   [feature_name], 
   [feature_description], 
   [feature_Type_id], 
   [feature_displaysequence], 
   [feature_isPrimary], 
   [LimitType_id], 
   [companyLegalUnit],
   [value])
AS 
   SELECT 
      featureaction.id AS id, 
	  featureaction.companyLegalUnit AS companyLegalUnit,
      featureaction.Feature_id AS Feature_id, 
      featureaction.name AS action_name, 
      featureaction.description AS action_description, 
      featureaction.isAccountLevel AS isAccountLevel, 
      featureaction.isMFAApplicable AS isMFAApplicable, 
      featureaction.isPrimary AS isPrimary, 
      featureaction.notes AS notes, 
      featureaction.Type_id AS action_Type_id, 
      featureaction.DisplaySequence AS action_displaysequence, 
      featureaction.dependency AS action_dependency, 
      feature.Status_id AS feature_status_id, 
      feature.name AS feature_name, 
      feature.description AS feature_description, 
      feature.Type_id AS feature_Type_id, 
      feature.DisplaySequence AS feature_displaysequence, 
      feature.isPrimary AS feature_isPrimary, 
      actionlimit.LimitType_id AS LimitType_id, 
      actionlimit.value AS value
   FROM (([${dbxschemaname}].featureaction 
      LEFT JOIN [${dbxschemaname}].feature 
      ON ((feature.id = featureaction.Feature_id))) 
      LEFT JOIN [${dbxschemaname}].actionlimit 
      ON ((actionlimit.Action_id = featureaction.id)));
GO
  
DROP VIEW IF EXISTS [${dbxschemaname}].[customer_request_detailed_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_request_detailed_view] (
   [customerrequest_id],
   [companyLegalUnit],
   [customerrequest_RequestCategory_id], 
   [customerrequest_lastupdatedbycustomer], 
   [requestcategory_Name], 
   [customerrequest_Customer_id], 
   [customer_FirstName], 
   [customer_MiddleName], 
   [customer_Fullname], 
   [customerrequest_AssignedTo_Name], 
   [customer_LastName], 
   [customer_Username], 
   [customer_Salutation], 
   [customer_Gender], 
   [customer_DateOfBirth], 
   [customer_Status_id], 
   [customer_Ssn], 
   [customer_MaritalStatus_id], 
   [customer_SpouseName], 
   [customer_EmployementStatus_id], 
   [customer_IsEnrolledForOlb], 
   [customer_IsStaffMember], 
   [customer_Location_id], 
   [customer_PreferredContactMethod], 
   [customer_PreferredContactTime], 
   [customerrequest_Priority], 
   [customerrequest_Status_id], 
   [customerrequest_AssignedTo], 
   [customerrequest_RequestSubject], 
   [customerrequest_Accountid], 
   [customerrequest_createdby], 
   [customerrequest_modifiedby], 
   [customerrequest_createdts], 
   [customerrequest_lastmodifiedts], 
   [customerrequest_synctimestamp], 
   [customerrequest_softdeleteflag], 
   [requestmessage_id], 
   [requestmessage_isPriorityMessage],
   [requestmessage_RepliedBy], 
   [requestmessage_RepliedBy_Name], 
   [requestmessage_MessageDescription], 
   [requestmessage_ReplySequence], 
   [requestmessage_IsRead], 
   [requestmessage_createdby], 
   [requestmessage_modifiedby], 
   [requestmessage_createdts], 
   [requestmessage_lastmodifiedts], 
   [requestmessage_synctimestamp], 
   [requestmessage_softdeleteflag], 
   [messageattachment_id], 
   [messageattachment_AttachmentType_id], 
   [messageattachment_Media_id], 
   [messageattachment_createdby], 
   [messageattachment_modifiedby], 
   [messageattachment_createdts], 
   [messageattachment_lastmodifiedts], 
   [messageattachment_softdeleteflag], 
   [media_id], 
   [media_Name], 
   [media_Size], 
   [media_Type], 
   [media_Description], 
   [media_Url], 
   [media_createdby], 
   [media_modifiedby], 
   [media_lastmodifiedts], 
   [media_synctimestamp],  
   [media_softdeleteflag])
AS 
   SELECT 
      customerrequest.id AS customerrequest_id, 
	  customerrequest.companyLegalUnit AS companyLegalUnit, 
      customerrequest.RequestCategory_id AS customerrequest_RequestCategory_id, 
      customerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer, 
      requestcategory.Name AS requestcategory_Name, 
      customerrequest.Customer_id AS customerrequest_Customer_id, 
      customer.FirstName AS customer_FirstName, 
      customer.MiddleName AS customer_MiddleName, 
      customer.FirstName + N' ' + customer.LastName AS customer_Fullname, 
      systemuser.FirstName + N' ' + systemuser.LastName AS customerrequest_AssignedTo_Name, 
      customer.LastName AS customer_LastName, 
      customer.UserName AS customer_Username, 
      customer.Salutation AS customer_Salutation, 
      customer.Gender AS customer_Gender, 
      customer.DateOfBirth AS customer_DateOfBirth, 
      customer.Status_id AS customer_Status_id, 
      NULL AS customer_Ssn, 
      customer.MaritalStatus_id AS customer_MaritalStatus_id, 
      customer.SpouseName AS customer_SpouseName, 
      customer.EmployementStatus_id AS customer_EmployementStatus_id, 
      customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb, 
      customer.IsStaffMember AS customer_IsStaffMember, 
      customer.Location_id AS customer_Location_id, 
      customer.PreferredContactMethod AS customer_PreferredContactMethod, 
      customer.PreferredContactTime AS customer_PreferredContactTime, 
      customerrequest.Priority AS customerrequest_Priority, 
      customerrequest.Status_id AS customerrequest_Status_id, 
      customerrequest.AssignedTo AS customerrequest_AssignedTo, 
      customerrequest.RequestSubject AS customerrequest_RequestSubject, 
      customerrequest.Accountid AS customerrequest_Accountid, 
      customerrequest.createdby AS customerrequest_createdby, 
      customerrequest.modifiedby AS customerrequest_modifiedby, 
      customerrequest.createdts AS customerrequest_createdts, 
      customerrequest.lastmodifiedts AS customerrequest_lastmodifiedts, 
      customerrequest.synctimestamp AS customerrequest_synctimestamp, 
      customerrequest.softdeleteflag AS customerrequest_softdeleteflag, 
      requestmessage.id AS requestmessage_id, 
	  requestmessage.isPriorityMessage AS requestmessage_isPriorityMessage, 
      requestmessage.RepliedBy AS requestmessage_RepliedBy, 
      requestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name, 
      requestmessage.MessageDescription AS requestmessage_MessageDescription, 
      requestmessage.ReplySequence AS requestmessage_ReplySequence, 
      requestmessage.IsRead AS requestmessage_IsRead, 
      requestmessage.createdby AS requestmessage_createdby, 
      requestmessage.modifiedby AS requestmessage_modifiedby, 
      requestmessage.createdts AS requestmessage_createdts, 
      requestmessage.lastmodifiedts AS requestmessage_lastmodifiedts, 
      requestmessage.synctimestamp AS requestmessage_synctimestamp, 
      requestmessage.softdeleteflag AS requestmessage_softdeleteflag, 
      messageattachment.id AS messageattachment_id, 
      messageattachment.AttachmentType_id AS messageattachment_AttachmentType_id, 
      messageattachment.Media_id AS messageattachment_Media_id, 
      messageattachment.createdby AS messageattachment_createdby, 
      messageattachment.modifiedby AS messageattachment_modifiedby, 
      messageattachment.createdts AS messageattachment_createdts, 
      messageattachment.lastmodifiedts AS messageattachment_lastmodifiedts, 
      messageattachment.softdeleteflag AS messageattachment_softdeleteflag, 
      media.id AS media_id, 
      media.Name AS media_Name, 
      media.Size AS media_Size, 
      media.Type AS media_Type, 
      media.Description AS media_Description, 
      media.Url AS media_Url, 
      media.createdby AS media_createdby, 
      media.modifiedby AS media_modifiedby, 
      media.lastmodifiedts AS media_lastmodifiedts, 
      media.synctimestamp AS media_synctimestamp, 
      media.softdeleteflag AS media_softdeleteflag
   FROM (((((([${dbxschemaname}].customerrequest 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customerrequest.Customer_id = customer.id))) 
      INNER JOIN [${dbxschemaname}].requestcategory 
      ON ((customerrequest.RequestCategory_id = requestcategory.id))) 
      INNER JOIN [${dbxschemaname}].requestmessage 
      ON ((customerrequest.id = requestmessage.CustomerRequest_id))) 
      LEFT JOIN [${dbxschemaname}].systemuser 
      ON ((customerrequest.AssignedTo = systemuser.id))) 
      LEFT JOIN [${dbxschemaname}].messageattachment 
      ON ((requestmessage.id = messageattachment.RequestMessage_id))) 
      LEFT JOIN [${dbxschemaname}].media 
      ON ((messageattachment.Media_id = media.id)));
GO
  
DROP VIEW IF EXISTS [${dbxschemaname}].[customer_request_status_count_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_request_status_count_view] ([Count], [Status_id])
AS 
   SELECT tbl$1.Count, tbl$1.Status_id
   FROM 
      (
         SELECT TOP (9223372036854775807) count_big(cr.id) AS Count, s1.id AS Status_id
         FROM ([${dbxschemaname}].status  AS s1 
            INNER JOIN [${dbxschemaname}].customerrequest  AS cr 
            ON ((s1.id = cr.Status_id)))
         WHERE ((s1.Type_id = 'STID_CUSTOMERREQUEST') AND (s1.id IN ( 
            N'SID_CANCELLED', 
            N'SID_DELETED', 
            N'SID_INPROGRESS', 
            N'SID_ONHOLD', 
            N'SID_OPEN', 
            N'SID_RESOLVED' )))
         GROUP BY s1.id
            ORDER BY s1.id
      )  AS tbl$1
    UNION ALL
   SELECT TOP (9223372036854775807) count_big(acr.id) AS Count, s2.id AS Status_id
   FROM ([${dbxschemaname}].status  AS s2 
      INNER JOIN [${dbxschemaname}].archivedcustomerrequest  AS acr 
      ON ((s2.id = acr.Status_id)))
   WHERE ((s2.Type_id = 'STID_CUSTOMERREQUEST') AND (s2.id = 'SID_ARCHIVED'))
   GROUP BY s2.id
      ORDER BY s2.id;
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[customerbasicinfo_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customerbasicinfo_view] (
   [Username], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [Name], 
   [Salutation], 
   [Customer_id], 
   [SSN], 
   [CustomerSince], 
   [Gender], 
   [DateOfBirth], 
   [CustomerStatus_id], 
   [CustomerStatus_name], 
   [MaritalStatus_id], 
   [MaritalStatus_name], 
   [SpouseName], 
   [DrivingLicenseNumber], 
   [lockedOn], 
   [lockCount], 
   [EmployementStatus_id], 
   [EmployementStatus_name], 
   [CustomerFlag_ids], 
   [CustomerFlag], 
   [IsEnrolledForOlb], 
   [IsStaffMember], 
   [Branch_id], 
   [Branch_name], 
   [Branch_code], 
   [IsOlbAllowed], 
   [IsAssistConsented], 
   [isEagreementSigned],
   [isCombinedUser],   
   [CustomerType_id], 
   [CustomerType_Name], 
   [CustomerType_Description], 
   [Customer_Role], 
   [isEAgreementRequired], 
   [organisation_id], 
   [organisation_name], 
   [PrimaryPhoneNumber], 
   [PrimaryEmailAddress], 
   [DocumentsSubmitted], 
   [ApplicantChannel], 
   [Product], 
   [companyLegalUnit],
   [Reason])
AS 
   SELECT 
      customer.UserName AS Username, 
      min(customer.FirstName) AS FirstName, 
      min(customer.MiddleName) AS MiddleName, 
      min(customer.LastName) AS LastName, 
      ISNULL(customer.FirstName, N'') + ' '+ ISNULL(customer.MiddleName, N'') + ' '+ ISNULL(customer.LastName, N'') AS Name, 
      customer.Salutation AS Salutation, 
      customer.id AS Customer_id, 
      customer.Ssn AS SSN, 
      customer.createdts AS CustomerSince, 
      customer.Gender AS Gender, 
      customer.DateOfBirth AS DateOfBirth, 
      customer.Status_id AS CustomerStatus_id, 
      customerstatus.Description AS CustomerStatus_name, 
      customer.MaritalStatus_id AS MaritalStatus_id, 
      maritalstatus.Description AS MaritalStatus_name, 
      customer.SpouseName AS SpouseName, 
      customer.DrivingLicenseNumber AS DrivingLicenseNumber, 
      customer.lockedOn AS lockedOn, 
	  customer.companyLegalUnit AS companyLegalUnit,
      customer.lockCount AS lockCount, 
      customer.EmployementStatus_id AS EmployementStatus_id, 
      employementstatus.Description AS EmployementStatus_name, 
      
         (
            
            SELECT String_agg(CAST(customerflagstatus.status_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].customerflagstatus
            WHERE (customerflagstatus.Customer_id = customer.id)
           

         ) AS CustomerFlag_ids, 
      
         (
          
            SELECT String_agg(CAST(status.description as nvarchar(max)),',') 
            FROM [${dbxschemaname}].status
            WHERE status.id IN 
               (
                  SELECT customerflagstatus.Status_id
                  FROM [${dbxschemaname}].customerflagstatus
                  WHERE (customerflagstatus.Customer_id = customer.id)
               )
            
         ) AS CustomerFlag, 
      customer.IsEnrolledForOlb AS IsEnrolledForOlb, 
      customer.IsStaffMember AS IsStaffMember, 
      customer.Location_id AS Branch_id, 
      location.Name AS Branch_name, 
      location.Code AS Branch_code, 
      customer.IsOlbAllowed AS IsOlbAllowed, 
      customer.IsAssistConsented AS IsAssistConsented, 
      customer.isEagreementSigned AS isEagreementSigned, 
	  customer.isCombinedUser AS isCombinedUser,
      IIF((customer.isCombinedUser = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            customer.CustomerType_id) AS CustomerType_id,
        IIF((customer.isCombinedUser = '1'),
            'Retail Banking,Business Banking',
            customertype.Name) AS CustomerType_Name,
        IIF((customer.isCombinedUser = '1'),
    'Retail and Business Banking User',
            customertype.Description) AS CustomerType_Description, 
      
         (
            SELECT TOP (1) membergroup.Name
            FROM [${dbxschemaname}].membergroup
            WHERE membergroup.id IN 
               (
                  SELECT customergroup.Group_id
                  FROM [${dbxschemaname}].customergroup
                  WHERE (customer.id = customergroup.Customer_id)
               )
         ) AS Customer_Role, 
      
         (
            SELECT TOP (1) membergroup.isEAgreementActive
            FROM [${dbxschemaname}].membergroup
            WHERE membergroup.id IN 
               (
                  SELECT customergroup.Group_id
                  FROM [${dbxschemaname}].customergroup
                  WHERE (customer.id = customergroup.Customer_id)
               )
         ) AS isEAgreementRequired, 
      customer.Organization_Id AS organisation_id, 
      organisation.Name AS organisation_name, 
      primaryphone.value AS PrimaryPhoneNumber, 
      primaryemail.value AS PrimaryEmailAddress, 
      customer.DocumentsSubmitted AS DocumentsSubmitted, 
      customer.ApplicantChannel AS ApplicantChannel, 
      customer.Product AS Product, 
      customer.Reason AS Reason
   FROM (((((((([${dbxschemaname}].customer 
      LEFT JOIN [${dbxschemaname}].location 
      ON ((customer.Location_id = location.id))) 
      LEFT JOIN [${dbxschemaname}].organisation 
      ON ((customer.Organization_Id = organisation.id))) 
      LEFT JOIN [${dbxschemaname}].customertype 
      ON ((customer.CustomerType_id = customertype.id))) 
      LEFT JOIN [${dbxschemaname}].status  AS customerstatus 
      ON ((customer.Status_id = customerstatus.id))) 
      LEFT JOIN [${dbxschemaname}].status  AS maritalstatus 
      ON ((customer.MaritalStatus_id = maritalstatus.id))) 
      LEFT JOIN [${dbxschemaname}].status  AS employementstatus 
      ON ((customer.EmployementStatus_id = employementstatus.id))) 
      LEFT JOIN [${dbxschemaname}].customercommunication  AS primaryphone 
      ON ((
         (primaryphone.Customer_id = customer.id) AND 
         (primaryphone.isPrimary = 1) AND 
         (primaryphone.Type_id = 'COMM_TYPE_PHONE')))) 
      LEFT JOIN [${dbxschemaname}].customercommunication  AS primaryemail 
      ON ((
         (primaryemail.Customer_id = customer.id) AND 
         (primaryemail.isPrimary = 1) AND 
         (primaryemail.Type_id = 'COMM_TYPE_EMAIL'))))
   GROUP BY customer.id,customer.UserName,customer.FirstName,customer.MiddleName,customer.LastName,customer.Salutation,customer.Ssn,
   customer.createdts,customer.Gender,customer.DateOfBirth,customer.Status_id,customerstatus.Description,
   customer.MaritalStatus_id,maritalstatus.Description,customer.SpouseName,customer.DrivingLicenseNumber,customer.lockedOn,customer.companylegalunit
   ,customer.lockCount,customer.EmployementStatus_id,customer.EmployementStatus_id,employementstatus.Description,
   customer.IsEnrolledForOlb,customer.IsStaffMember,customer.Location_id,location.Name,location.Code,customer.IsOlbAllowed
   ,customer.IsAssistConsented,customer.IsAssistConsented,customer.isEagreementSigned,customer.isCombinedUser, customer.CustomerType_id,
   customertype.Name,customertype.Description,customer.Organization_Id,organisation.Name,primaryphone.Value,primaryemail.Value,
   customer.DocumentsSubmitted,customer.ApplicantChannel,customer.Product,customer.Reason;
GO 



DROP procedure IF EXISTS [${dbxschemaname}].[bulkassign_permissions_to_role_approval]
GO

CREATE PROCEDURE [${dbxschemaname}].[bulkassign_permissions_to_role_approval]  
    @_roleId VARCHAR(50),
	@_permissions VARCHAR(MAX),
	@_requestId VARCHAR(50)

AS 
   BEGIN
	DECLARE @index1 INT;
	DECLARE @numOfPermissions INT;
	DECLARE @permissionsData NVARCHAR(MAX);
	DECLARE @query NVARCHAR(MAX);
	
	if(@numOfPermissions IS NULL or @numOfPermissions IS NULL)
		return;

	SET @numOfPermissions = LEN(@_permissions) - LEN(REPLACE(@_permissions, '|', '')) + 1;
	SET @index1 = 0;
    
	WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPermissions + 1
               BREAK
            ELSE 
               BEGIN
				SET @permissionsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_permissions, '|', @index1), '|', -1 );
				SET @query = ('insert into [${dbxschemaname}].rolepermission_approval(role_id,permission_id,softdeleteflag,aprRequestId,crudAction) 
				values (''') +@_roleId + (''',''') + @permissionsData + (''',0,''')+@_requestId +(''',''INS''')+(');');
			EXEC(@query)
			END 
	END
	  
   END

GO


CREATE PROCEDURE [${dbxschemaname}].[rolepermission_data_movement_approval_proc](
@_requestId VARCHAR(50),
@_context VARCHAR(50))
AS
BEGIN
	IF @_context = 'Approved' BEGIN
		INSERT INTO rolepermission select Role_id,Permission_id,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag,companyLegalUnit from rolepermission_approval where aprRequestId = @_requestId;
	END 
	delete from rolepermission_approval where aprRequestId = @_requestId and Role_id !='' and Permission_id != '';
END



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolepermission_delete_approval_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolepermission_delete_approval_proc] 
 @_roleId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000),
 @_requestId VARCHAR(50),
 @_context VARCHAR(50)
AS 
   BEGIN
    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
	DECLARE @caid VARCHAR(50);
	DECLARE @isEnabled VARCHAR(10);
	DECLARE @finished INTEGER = 0 ;
	DECLARE @caid_count INTEGER = 0 ;
	DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM [${dbxschemaname}].[compositeaction] c WHERE [${dbxschemaname}].FIND_IN_SET([Permission_id], @_PermissionIds) > 0);
	
	OPEN caids; 
	MANAGECAIDS:
	WHILE 1=1
	BEGIN
	FETCH caids INTO @caid, @isEnabled;
	IF @@FETCH_STATUS <> 0 BEGIN 
		 GOTO MANAGECAIDS$LEAVE
	END 
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  [${dbxschemaname}].[rolepermission] rp WHERE rp.Role_id= @_roleId
	AND rp.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(rp.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	IF @_context = 'Approved' BEGIN
	DELETE FROM [${dbxschemaname}].[rolecompositeaction] WHERE Role_id=@_roleId AND CompositeAction_id=@caid;
	END
	DELETE FROM [${dbxschemaname}].[rolecompositeaction_approval] WHERE Role_id=@_roleId AND CompositeAction_id=@caid AND
	aprRequestId = @_requestId;
	END 
	END
	MANAGECAIDS$LEAVE:
	CLOSE caids
	DEALLOCATE caids;
	IF @_context = 'Approved' BEGIN
	DELETE FROM [${dbxschemaname}].[rolepermission] WHERE Role_id=@_roleId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
	DELETE FROM [${dbxschemaname}].[rolepermission_approval] WHERE Role_id=@_roleId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0 AND aprRequestId = @_requestId;
	END
GO


CREATE PROCEDURE [${dbxschemaname}].[role_data_movement_to_approval_proc]
 @_requestId VARCHAR(50),
 @_roleId VARCHAR(50)
 AS
BEGIN
	Declare @columnnames varchar(max);
	Declare @msquery nvarchar(3000);
	Set @columnnames = (SELECT STRING_AGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY ORDINAL_POSITION) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'role');
    Set @msquery  = (select (concat('insert into [${dbxschemaname}].[role_approval] select ',@columnnames,',''',@_requestId
    ,''',','''','NONE','''',' from [${dbxschemaname}].[role] where id=','''',@_roleId,''';')));
    EXECUTE sp_executesql @msquery;
    
    Set @columnnames = (SELECT STRING_AGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY ORDINAL_POSITION) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'rolepermission');
    Set @msquery = (select(concat('insert into [${dbxschemaname}].[rolepermission_approval] select ',@columnnames,',''',@_requestId
    ,''',','''','NONE','''',' from [${dbxschemaname}].[rolepermission] where Role_id=','''',@_roleId,''';')));
    EXECUTE sp_executesql @msquery;

    
    Set @columnnames = (SELECT STRING_AGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY ORDINAL_POSITION) 
	FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'userrole');    
    Set @msquery = (select(concat('insert into [${dbxschemaname}].[userrole_approval] select ',@columnnames,',''',@_requestId
    ,''',','''','NONE','''',' from [${dbxschemaname}].[userrole] where Role_id=','''',@_roleId,''';')));
    EXECUTE sp_executesql @msquery;
    
    Set @columnnames = (SELECT STRING_AGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY ORDINAL_POSITION) 
	FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'userroleservicedefinition');
    Set @msquery = (select (concat('insert into [${dbxschemaname}].[userroleservicedefinition_approval] select ',@columnnames,',''',@_requestId
    ,''',','''','NONE','''',' from [${dbxschemaname}].[userroleservicedefinition] where UserRole_id=','''',@_roleId,''';')));
     EXECUTE sp_executesql @msquery;

    Set @columnnames = (SELECT STRING_AGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY ORDINAL_POSITION) 
	FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'rolecompositeaction');
    Set @msquery = (select (concat('insert into [${dbxschemaname}].[rolecompositeaction_approval] select ',@columnnames,',''',@_requestId
    ,''',','''','NONE','''',' from [${dbxschemaname}].[rolecompositeaction] where Role_id=','''',@_roleId,''';')));
     EXECUTE sp_executesql @msquery;
END



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolepermission_update_approval_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolepermission_update_approval_proc] 
 @_roleId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000),
 @_requestId VARCHAR(50)
AS 
   BEGIN
    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
	DECLARE @caid VARCHAR(50);
	DECLARE @isEnabled VARCHAR(10);
	DECLARE @finished INTEGER = 0 ;
	DECLARE @caid_count INTEGER = 0 ;
	DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM [${dbxschemaname}].[compositeaction] c WHERE [${dbxschemaname}].FIND_IN_SET([Permission_id], @_PermissionIds) > 0);
	
	OPEN caids; 
	MANAGECAIDS:
	WHILE 1=1
	BEGIN
	FETCH caids INTO @caid, @isEnabled;
	IF @@FETCH_STATUS <> 0 BEGIN 
		 GOTO MANAGECAIDS$LEAVE
	END 
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  [${dbxschemaname}].[rolepermission] rp WHERE rp.Role_id= @_roleId
	AND rp.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(rp.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	UPDATE [${dbxschemaname}].[rolecompositeaction_approval] SET crudAction= 'DEL' WHERE aprRequestId=@_requestId AND CompositeAction_id=@caid;
	END 
	END
	MANAGECAIDS$LEAVE:
	CLOSE caids
	DEALLOCATE caids;
	UPDATE [${dbxschemaname}].[rolepermission_approval] SET crudAction= 'DEL' 
	WHERE aprRequestId=@_requestId AND Role_id=@_roleId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
GO


CREATE VIEW [${dbxschemaname}].[internal_role_to_servicedefinition_mapping_view_approval] AS
    SELECT DISTINCT
        [${dbxschemaname}].[servicedefinition].id AS ServiceDefinition_id,
        [${dbxschemaname}].[servicedefinition].name AS ServiceDefinition_Name,
        [${dbxschemaname}].[servicedefinition].description AS ServiceDefinition_Description,
        [${dbxschemaname}].[servicedefinition].serviceType AS ServiceDefinition_Type_id,
        [${dbxschemaname}].[servicedefinition].status AS ServiceDefinition_Status_id,
        [${dbxschemaname}].[role_approval].id AS InternalRole_id,
        [${dbxschemaname}].[role_approval].Type_id AS InternalRole_Type_id,
        [${dbxschemaname}].[role_approval].Status_id AS InternalRole_Status_id,
        [${dbxschemaname}].[role_approval].Name AS InternalRole_Name,
        [${dbxschemaname}].[role_approval].Description AS InternalRole_Description,
        [${dbxschemaname}].[userroleservicedefinition_approval].aprRequestId AS RequestIdForApprovalContext
    FROM
        (([${dbxschemaname}].[servicedefinition]
        LEFT JOIN [${dbxschemaname}].[userroleservicedefinition_approval] ON (([${dbxschemaname}].[servicedefinition].id = [${dbxschemaname}].[userroleservicedefinition_approval].servicedefinitionId)))
        LEFT JOIN [${dbxschemaname}].[role_approval] ON (([${dbxschemaname}].[role_approval].id = [${dbxschemaname}].[userroleservicedefinition_approval].UserRole_id))) 
        WHERE ([${dbxschemaname}].[userroleservicedefinition_approval].crudAction != 'DEL' AND [${dbxschemaname}].[role_approval].crudAction != 'DEL');
GO

CREATE VIEW [${dbxschemaname}].[rolepermission_view_approval] AS
    SELECT DISTINCT
        [${dbxschemaname}].[role_approval].Name AS Role_Name,
        [${dbxschemaname}].[role_approval].Description AS Role_Description,
        [${dbxschemaname}].[role_approval].Status_id AS Role_Status_id,
        [${dbxschemaname}].[rolepermission_approval].Role_id AS Role_id,
        [${dbxschemaname}].[permission].id AS Permission_id,
        [${dbxschemaname}].[permission].Type_id AS Permission_Type_id,
        [${dbxschemaname}].[permission].Status_id AS Permission_Status_id,
        [${dbxschemaname}].[permission].DataType_id AS DataType_id,
        [${dbxschemaname}].[permission].Name AS Permission_Name,
        [${dbxschemaname}].[permission].Description AS Permission_Description,
        [${dbxschemaname}].[permission].isComposite AS Permission_isComposite,
        [${dbxschemaname}].[permission].PermissionValue AS PermissionValue,
        [${dbxschemaname}].[permission].createdby AS Permission_createdby,
        [${dbxschemaname}].[permission].modifiedby AS Permission_modifiedby,
        [${dbxschemaname}].[permission].createdts AS Permission_createdts,
        [${dbxschemaname}].[permission].lastmodifiedts AS Permission_lastmodifiedts,
        [${dbxschemaname}].[permission].synctimestamp AS Permission_synctimestamp,
        [${dbxschemaname}].[permission].softdeleteflag AS Permission_softdeleteflag,
        [${dbxschemaname}].[rolepermission_approval].aprRequestId AS RequestIdForApprovalContext
    FROM
        (([${dbxschemaname}].[rolepermission_approval]
        JOIN [${dbxschemaname}].[permission] ON ((([${dbxschemaname}].[rolepermission_approval].Permission_id = [${dbxschemaname}].[permission].id))))
        JOIN [${dbxschemaname}].[role_approval] ON (([${dbxschemaname}].[role_approval].id = [${dbxschemaname}].[rolepermission_approval].Role_id))) 
        WHERE ([${dbxschemaname}].[rolepermission_approval].crudAction != 'DEL' AND [${dbxschemaname}].[role_approval].crudAction != 'DEL');
GO

CREATE VIEW [${dbxschemaname}].[roleuser_view_approval] AS
    SELECT DISTINCT
        [${dbxschemaname}].[userrole_approval].User_id AS User_id,
        [${dbxschemaname}].[userrole_approval].Role_id AS Role_id,
        [${dbxschemaname}].[systemuser].Status_id AS Status_id,
        [${dbxschemaname}].[systemuser].Username AS Username,
        [${dbxschemaname}].[systemuser].FirstName AS FirstName,
        [${dbxschemaname}].[systemuser].MiddleName AS MiddleName,
        [${dbxschemaname}].[systemuser].LastName AS LastName,
        [${dbxschemaname}].[systemuser].Email AS Email,
        [${dbxschemaname}].[systemuser].modifiedby AS UpdatedBy,
        [${dbxschemaname}].[systemuser].lastmodifiedts AS LastModifiedTimeStamp,
        [${dbxschemaname}].[userrole_approval].aprRequestId AS RequestIdForApprovalContext
    FROM
        ([${dbxschemaname}].[userrole_approval]
        JOIN [${dbxschemaname}].[systemuser] ON (([${dbxschemaname}].[userrole_approval].User_id = [${dbxschemaname}].[systemuser].id)))
        WHERE ([${dbxschemaname}].[userrole_approval].crudAction != 'DEL');
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalrequests_counts_proc];
GO
CREATE PROCEDURE [fetch_approvalrequests_counts_proc](
	@_permissionListArr NVARCHAR(MAX),
	@_userId NVARCHAR(256)
)
AS BEGIN
	DECLARE @req_pending_status VARCHAR(64);
	DECLARE @execStmt NVARCHAR(MAX);
	SET @req_pending_status = 'Pending For Approval';	-- set it to the current string used to denote the Pending status
	SET @execStmt = ('
		select ''pendingRequests'' as category, count(*) as counts from [${dbxschemaname}].[approvalrequests] where createdby = ''' + @_userId + ''' and status = ''' + @req_pending_status + '''
		union
		select ''requestHistory'' as category, count(*) as counts from [${dbxschemaname}].[approvalrequests] where createdby = ''' + @_userId + '''
		union
		select ''approvalHistory'' as category, count(*) as counts from [${dbxschemaname}].[approvalrequests] where checkedBy = ''' + @_userId + '''
		union
		select ''pendingApprovals'' as category, count(*) as counts from [${dbxschemaname}].[approvalrequests] where status = ''' + @req_pending_status + ''' and createdby != ''' + @_userId + ''' and expAPIOperationName in 
		(select expAPIOperationName from [${dbxschemaname}].[permissionapprovals] where approvalPermissionName in (' + @_permissionListArr + '))');
	EXEC(@execStmt);
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_pending_approvals_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_pending_approvals_proc](
	@_permissionListArr NVARCHAR(MAX), 
    @_userId NVARCHAR(256), 
    @_moduleListArr NVARCHAR(MAX),
    @_featureListArr NVARCHAR(MAX),
	@_actionListArr NVARCHAR(MAX),
    @_searchStartDate NVARCHAR(16),
    @_searchEndDate NVARCHAR(16),
    @_sortParam NVARCHAR(32), -- status / checkedBy / createdby | DEFAULT : checkedby (for approval history) & createdby (for pending approvals)
    @_sortOrder NVARCHAR(8) -- ASC / DESC
)
AS BEGIN
	DECLARE @dateFilterStmt NVARCHAR(MAX);
	DECLARE @dateField NVARCHAR(128);
	DECLARE @req_pending_status NVARCHAR(64);
	DECLARE @sqlStmt NVARCHAR(MAX);
	SET @dateField = 'ar.createdts';
    
    IF @_sortParam IS NOT NULL
		SET @_sortParam = CONCAT('ar.', @_sortParam);

    IF @_searchStartDate IS NULL	-- since no start date, no date filtering and show for all dates
		SET @dateFilterStmt = '';
    ELSE
		SET @dateFilterStmt = CONCAT(' AND FORMAT(', @dateField ,', ''yyyy-MM-dd'') BETWEEN ''', @_searchStartDate, 
        ''' AND ', IIF( @_searchEndDate IS NOT NULL, CONCAT('''', @_searchEndDate ,''''), 'GETDATE()'));
    
	SET @req_pending_status = 'Pending For Approval';
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, cru.Username as ''createdby'', ar.createdts, ar.status, cku.Username as ''checkedBy'', ar.checkedts, ar.reason
					FROM [${dbxschemaname}].[approvalrequests] ar
					LEFT JOIN [${dbxschemaname}].[systemuser] cru ON cru.id = ar.createdby
					LEFT JOIN [${dbxschemaname}].[systemuser] cku ON cku.id = ar.checkedBy',
				' WHERE ar.status = ''', @req_pending_status ,''' AND ar.createdby != ''', @_userId, '''',
				IIF( @_moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', @_moduleListArr, ')'), ''),
                IIF( @_featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', @_featureListArr, ')'), ''),
				IIF( @_actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', @_actionListArr, ')'), ''),
                @dateFilterStmt,
                ' AND expAPIOperationName IN ( 
	SELECT expAPIOperationName FROM [${dbxschemaname}].[permissionapprovals] 
	WHERE approvalPermissionName IN (', @_permissionListArr, '))',
    ' ORDER BY ', IIF( @_sortParam IS NOT NULL AND @_sortParam IN (@dateField), @_sortParam, @dateField) , ' ', IIF( @_sortOrder IS NOT NULL AND (UPPER(@_sortOrder) IN ('DESC', 'ASC')), @_sortOrder, 'DESC'));
	EXEC(@sqlStmt);
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approval_history_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approval_history_proc](
    @_userId NVARCHAR(256),
    @_moduleListArr NVARCHAR(MAX),
    @_featureListArr NVARCHAR(MAX),
	@_actionListArr NVARCHAR(MAX),
    @_searchStartDate VARCHAR(32),
    @_searchEndDate VARCHAR(32),
    @_sortParam VARCHAR(32), -- status / checkedts / createdts | DEFAULT : checkedts (for approval history) & createdts (for pending approvals)
    @_sortOrder VARCHAR(8) -- ASC / DESC
)
AS BEGIN
	DECLARE @dateFilterStmt NVARCHAR(MAX);
	DECLARE @dateField NVARCHAR(128);
	DECLARE @req_pending_status NVARCHAR(64);
	DECLARE @sqlStmt NVARCHAR(MAX);
	SET @dateField = 'ar.checkedts';
    
    IF @_sortParam IS NOT NULL
		SET @_sortParam = CONCAT('ar.', @_sortParam);

    IF @_searchStartDate IS NULL	-- since no start date, no date filtering and show for all dates
		SET @dateFilterStmt = '';
    ELSE
		SET @dateFilterStmt = CONCAT(' AND FORMAT(', @dateField ,', ''yyyy-MM-dd'') BETWEEN ''', @_searchStartDate, 
        ''' AND ', IIF( @_searchEndDate IS NOT NULL, CONCAT('''', @_searchEndDate ,''''), 'GETDATE()'));
    
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, cru.Username as ''createdby'', ar.createdts, ar.status, cku.Username as ''checkedBy'', ar.checkedts, ar.reason
					FROM [${dbxschemaname}].[approvalrequests] ar
					LEFT JOIN [${dbxschemaname}].[systemuser] cru ON cru.id = ar.createdby
					LEFT JOIN [${dbxschemaname}].[systemuser] cku ON cku.id = ar.checkedBy',
				' WHERE ar.checkedby=''', @_userId, '''',
				IIF( @_moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', @_moduleListArr, ')'), ''),
                IIF( @_featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', @_featureListArr, ')'), ''),
				IIF( @_actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', @_actionListArr, ')'), ''),
                @dateFilterStmt, 
                ' ORDER BY ', IIF( @_sortParam IS NOT NULL AND @_sortParam IN ('ar.status', @dateField), @_sortParam, @dateField) , ' ', IIF( @_sortOrder IS NOT NULL AND (UPPER(@_sortOrder) IN ('DESC', 'ASC')), @_sortOrder, 'DESC'));
	EXEC(@sqlStmt);
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[useractions_relative_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].useractions_relative_create_proc  
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
      FOR (SELECT customeraccounts.Account_id FROM [${dbxschemaname}].customeraccounts WHERE 
      customeraccounts.contractId = @_contractId AND
      customeraccounts.coreCustomerId = @_coreCustomerId AND
      customeraccounts.Customer_id = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
	  DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '1'));
      DECLARE nonaccountlevelactions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '0'));
	  DECLARE limits CURSOR LOCAL
	  FOR (select LimitType_id from [${dbxschemaname}].actionlimit where Action_id = @featureActionId);

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (
            SELECT String_agg(CAST(customeraccounts.Account_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraccounts WHERE 
                               [${dbxschemaname}].customeraccounts.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraccounts.contractId = @_contractId AND
                               [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId
             )
    
    SET @serviceDefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)
    SET @serviceType = (SELECT servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE servicedefinition.id = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition
						WHERE groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId)
						 
    SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction)
						
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                             
    SET @_groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId)  

                                
    SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
                                
    SET @validActionsList =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
    OPEN accounts     
	FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
	OPEN actions
	FETCH NEXT FROM actions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
	      SET @entryStatus = 0
	OPEN limits
	FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN
	        SET @limitvalue = (SELECT contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE contractactionlimit.actionId = @featureActionId AND 
                               contractactionlimit.limitTypeId = @limitId AND
                               contractactionlimit.contractId = @_contractId AND
                               contractactionlimit.coreCustomerId = @_coreCustomerId)

            IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
	           SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                             [${dbxschemaname}].customeraction.RoleType_id, 
                                             [${dbxschemaname}].customeraction.Customer_id, 
                                             [${dbxschemaname}].customeraction.contractId, 
                                             [${dbxschemaname}].customeraction.coreCustomerId, 
                                             [${dbxschemaname}].customeraction.featureId, 
                                             [${dbxschemaname}].customeraction.Action_id, 
                                             [${dbxschemaname}].customeraction.Account_id, 
                                             [${dbxschemaname}].customeraction.isAllowed, 
                                             [${dbxschemaname}].customeraction.LimitType_id, 
                                             [${dbxschemaname}].customeraction.[value])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
			   SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
			 IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
			 SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
			 SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                           [${dbxschemaname}].customeraction.RoleType_id, 
                                           [${dbxschemaname}].customeraction.Customer_id, 
                                           [${dbxschemaname}].customeraction.contractId, 
                                           [${dbxschemaname}].customeraction.coreCustomerId, 
                                           [${dbxschemaname}].customeraction.featureId,
                                           [${dbxschemaname}].customeraction.Action_id, 
                                           [${dbxschemaname}].customeraction.Account_id, 
                                           [${dbxschemaname}].customeraction.isAllowed, 
                                           [${dbxschemaname}].customeraction.LimitType_id, 
                                           [${dbxschemaname}].customeraction.[value])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
			 SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
			 END
             END
		SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                      [${dbxschemaname}].customeraction.RoleType_id, 
                                      [${dbxschemaname}].customeraction.Customer_id,
                                      [${dbxschemaname}].customeraction.contractId, 
                                      [${dbxschemaname}].customeraction.coreCustomerId, 
                                      [${dbxschemaname}].customeraction.featureId, 
                                      [${dbxschemaname}].customeraction.Action_id, 
                                      [${dbxschemaname}].customeraction.Account_id, 
                                      [${dbxschemaname}].customeraction.isAllowed, 
                                      [${dbxschemaname}].customeraction.LimitType_id, 
                                      [${dbxschemaname}].customeraction.[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                                      
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                      [${dbxschemaname}].customeraction.RoleType_id, 
                                      [${dbxschemaname}].customeraction.Customer_id,
                                      [${dbxschemaname}].customeraction.contractId, 
                                      [${dbxschemaname}].customeraction.coreCustomerId, 
                                      [${dbxschemaname}].customeraction.featureId, 
                                      [${dbxschemaname}].customeraction.Action_id, 
                                      [${dbxschemaname}].customeraction.Account_id, 
                                      [${dbxschemaname}].customeraction.isAllowed, 
                                      [${dbxschemaname}].customeraction.LimitType_id, 
                                      [${dbxschemaname}].customeraction.[value])
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
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.Account_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
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
		   
OPEN nonaccountlevelactions
	FETCH NEXT FROM nonaccountlevelactions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );

          SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,1);
			FETCH NEXT FROM nonaccountlevelactions into @featureActionId
            CONTINUE

          END          
          
    CLOSE nonaccountlevelactions
		   DEALLOCATE nonaccountlevelactions
   END
GO


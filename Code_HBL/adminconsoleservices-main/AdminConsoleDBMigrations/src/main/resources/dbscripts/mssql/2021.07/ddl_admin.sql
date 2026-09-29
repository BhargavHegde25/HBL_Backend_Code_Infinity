
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[bulkpaymentrecord]', 'totalAmount';
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ALTER COLUMN totalAmount VARCHAR(50);

EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[bulkpaymentfiles]', 'totalAmount';
ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] ALTER COLUMN totalAmount VARCHAR(50);

GO

ALTER TABLE [${dbxschemaname}].[featureaction] ADD [isApprovalAction] BIT NOT NULL DEFAULT '0';
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[usertype];

CREATE TABLE [${dbxschemaname}].[usertype] (
  [id] varchar(50) NOT NULL,
  [name] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,  
  [createdts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] SMALLINT NULL DEFAULT '0',  
  PRIMARY KEY ([id]),
  CONSTRAINT [usertype_name_UNIQUE] UNIQUE  ([name])
) ;
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[lob];

CREATE TABLE [${dbxschemaname}].[lob] (
  [id] [nvarchar](50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,  
  [createdts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] SMALLINT NULL DEFAULT '0',  
  PRIMARY KEY ([id])) ;

GO
DROP TABLE IF EXISTS [${dbxschemaname}].[lobtext];

CREATE TABLE [${dbxschemaname}].[lobtext] (
  [lobId] [nvarchar](50) NOT NULL,
  [languageCode] [nvarchar](10) NOT NULL,
  [displayName] VARCHAR(255) NULL DEFAULT NULL,
  [description] VARCHAR(1000) NULL DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,  
  [createdts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] SMALLINT NULL DEFAULT '0',  
  PRIMARY KEY ([lobId], [languageCode]),
  CONSTRAINT [FK_lobtext_lob]
    FOREIGN KEY ([lobId])
    REFERENCES [${dbxschemaname}].lob ([id])
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT [FK_lobtext_locale]
    FOREIGN KEY ([languageCode])
    REFERENCES [${dbxschemaname}].locale ([Code])
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ;

GO
DROP TABLE IF EXISTS [${dbxschemaname}].[userlob];

CREATE TABLE [${dbxschemaname}].[userlob] (
  [userId] [nvarchar](50) NOT NULL,
  [lobId] [nvarchar](50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([userId],[lobId]),
  CONSTRAINT [userlob_lobId] FOREIGN KEY ([lobId]) REFERENCES [${dbxschemaname}].lob ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION);
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[internalusermanager];
GO
CREATE TABLE [${dbxschemaname}].[internalusermanager] (
  [userId] varchar(50) NOT NULL,
  [manager] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([userId],[manager])
) ;

CREATE INDEX [IXFK_internalusermanager_manager] ON [${dbxschemaname}].[internalusermanager] ([manager]);
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[internalusertype];
GO
CREATE TABLE [${dbxschemaname}].[internalusertype] (
  [userId] varchar(50) NOT NULL,
  [userType] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([userId],[userType])
 ,
  CONSTRAINT [FK_internalusertype_userType] FOREIGN KEY ([userType]) REFERENCES [${dbxschemaname}].usertype ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
) ;

CREATE INDEX [IXFK_internalusertype_usertype] ON [${dbxschemaname}].[internalusertype] ([userType]);
GO

CREATE INDEX [userlob_lobId_idx] ON [${dbxschemaname}].[userlob] ([lobId]);
CREATE INDEX [userlob_systemuserId_idx] ON [${dbxschemaname}].[userlob] ([userId]);
GO
--ALTER TABLE [${dbxschemaname}].[systemuser] ADD  INDEX [FK_SystemUser_Manager_idx] ON [systemuser] ([Manager]);
--ALTER TABLE [${dbxschemaname}].[systemuser] ADD  INDEX [FK_SystemUser_UserType] ON [systemuser] ([UserType]);

/****** Object:  View [${dbxschemaname}].[lob_view]    Script Date: 6/22/2021 12:30:46 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[lob_view];
GO
CREATE VIEW [${dbxschemaname}].[lob_view] (
    [lob_id],
    [lobtext_languageCode], 
	[lobtext_description],
	[lobtext_displayName]) AS 
	 SELECT
		[${dbxschemaname}].lob.id AS lob_id,
		[${dbxschemaname}].lobtext.languageCode AS lobtext_languageCode,
		[${dbxschemaname}].lobtext.description AS lobtext_description,
		[${dbxschemaname}].lobtext.displayName AS lobtext_displayName
     FROM
	   ([${dbxschemaname}].lobtext
       INNER JOIN [${dbxschemaname}].lob ON((lobtext.lobId = lob.id)));
GO		

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_accountdetails_proc];
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] DROP CONSTRAINT [usercompositeaction$FK_UserCompositeAction_User];
GO
ALTER TABLE [${dbxschemaname}].[useraddress] DROP CONSTRAINT [useraddress$FK_UserAddress_SystemUser];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_accountdetails_proc](
	@_contractId NVARCHAR(50), 
	@_cif NVARCHAR(50),
	@_accountIds NVARCHAR(max)
) AS
BEGIN		
	IF @_cif = '' BEGIN
		SET @_cif = '%';
    END 
    
	SELECT 
		[customeraccounts].[Account_id],
		[customeraccounts].[AccountName],
		[customeraccounts].[accountType],
		[contractaccounts].[ownerType] AS [ownershipType]
	FROM ([${dbxschemaname}].[customeraccounts] AS [customeraccounts]
		LEFT JOIN
		[${dbxschemaname}].[contractaccounts] AS [contractaccounts]
		ON [customeraccounts].[Account_id] = [contractaccounts].[accountId])
	WHERE 
		[customeraccounts].[contractId] = @_contractId AND
		[customeraccounts].[coreCustomerId] LIKE @_cif AND
		[${dbxschemaname}].FIND_IN_SET([customeraccounts].[Account_id],@_accountIds) > 0
	ORDER BY 
		[customeraccounts].[contractId],[customeraccounts].[coreCustomerId];
END
GO
	
CREATE UNIQUE INDEX [IDX_coreCustomerID_ContractId] ON [${dbxschemaname}].[approvalmode] ([contractId] ASC,[coreCustomerId] ASC)
GO
CREATE INDEX [IDX_contractId_coreCustomerID_Mul] ON [${dbxschemaname}].[customeraction] ([contractId] ASC,[coreCustomerId] ASC)

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalusers_view];
GO
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[internalusers_view] 
	AS 
	SELECT (systemuser.id) AS [User_id], 
		   (systemuser.Username) AS Username, 
		  (SELECT TOP(1)
		[internalusertype].[userType]
	FROM
		[${dbxschemaname}].internalusertype
	WHERE
		([systemuser].[id] = [internalusertype].[userId])
	) AS [UserType],
	(SELECT TOP(1)
		[internalusermanager].[manager]
	FROM
		[${dbxschemaname}].internalusermanager
	WHERE
		([systemuser].[id] = [internalusermanager].[userId])
	) AS [ReportingManager],
	(SELECT 
		[usertype].[name]
	FROM
		[${dbxschemaname}].usertype
	WHERE
		[usertype].[id] IN (SELECT 
				[internalusertype].[userType]
			FROM
				[${dbxschemaname}].internalusertype
			WHERE
				([systemuser].[id] = [internalusertype].[userId]))) AS [userTypeName],
		  (systemuser.Status_id) AS Status_id, 
		  (status.Description) AS Status_Desc, 
		  (systemuser.FirstName) AS FirstName, 
		  (systemuser.MiddleName) AS MiddleName, 
		  (systemuser.LastName) AS LastName, 
		  (systemuser.lastLogints) AS lastLogints, 
		  (systemuser.FirstName) + ' ' + (systemuser.LastName) AS Name, 		
		  (systemuser.Email) AS Email, 
		  (SELECT TOP (1) userrole.Role_id AS Role_id FROM [${dbxschemaname}].userrole WHERE ((systemuser.id) = userrole.User_id)) AS Role_id, 
		  (SELECT TOP (1) role.Description AS Description FROM [${dbxschemaname}].role WHERE role.id IN 
				   (  SELECT userrole.Role_id AS Role_id
					  FROM [${dbxschemaname}].userrole
					  WHERE ((systemuser.id) = userrole.User_id)
				   )) AS Role_Desc, 
			(SELECT TOP (1) role.Name AS Name FROM [${dbxschemaname}].role WHERE role.id IN 
				   (  SELECT userrole.Role_id AS Role_id
					  FROM [${dbxschemaname}].userrole
					  WHERE ((systemuser.id )=userrole.User_id)
				   )) AS Role_Name, 
		  ((	SELECT count(userpermission.Permission_id)
				FROM [${dbxschemaname}].userpermission
				WHERE ((systemuser.id) = userpermission.User_id)
			 ) + 
			 (	SELECT count(rolepermission.Permission_id)
				FROM [${dbxschemaname}].rolepermission
				WHERE rolepermission.Role_id IN 
				   (  SELECT userrole.Role_id AS Role_id
					  FROM [${dbxschemaname}].userrole
					  WHERE ((systemuser.id) = userrole.User_id)
			))) AS Permission_Count, 
		  (systemuser.lastmodifiedts) AS lastmodifiedts, 
		  (systemuser.createdts) AS createdts, 
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
			 ) AS Work_Addr, 
		  
			 (SELECT (homeaddress.addressLine1)
					+ 
				   N', '
					+ 
				   ISNULL((homeaddress.addressLine2), '')
					+ 
				   N', '
					+ 
				   ISNULL((homeaddress.cityName), '')
					+ 
				   N', '
					+ 
				   
					  (
						 SELECT (region.Name) AS Name
						 FROM [${dbxschemaname}].region
						 WHERE (region.id = (homeaddress.Region_id))
					  )
					+ 
				   N', '
					+ 
				   
					  (
						 SELECT (country.Name) AS Name
						 FROM [${dbxschemaname}].country
						 WHERE country.id IN 
							(
							   SELECT (region.Country_id) AS Country_id
							   FROM [${dbxschemaname}].region
							   WHERE (region.id = (homeaddress.Region_id))
							)
					  )
					+ 
				   N', '
					+ 
				   (homeaddress.zipCode)
			 ) AS Home_Addr, 
		  
			 (
				SELECT ISNULL((homeaddress.id), N'')
			 )  AS Home_AddressID, 
		  
			 (
				SELECT ISNULL((homeaddress.addressLine1), N'')
			 )  AS Home_AddressLine1, 
		  
			 (
				SELECT ISNULL((homeaddress.addressLine2), N'')
			 )  AS Home_AddressLine2, 
		  
			 (
				SELECT ISNULL((homeaddress.cityName), N'')
			 )  AS Home_CityName, 
		  
			 (
				SELECT ISNULL((homeaddress.City_id), N'')
			 )  AS Home_CityID, 
		  
			 (
				SELECT ISNULL(
				   (
					  SELECT (region.Name) AS Name
					  FROM [${dbxschemaname}].region
					  WHERE (region.id = (homeaddress.Region_id))
				   ), N'')
			 ) AS Home_StateName, 
		  
			 (
				SELECT ISNULL((homeaddress.Region_id), N'')
			 )  AS Home_StateID, 
		  
			 (
				SELECT ISNULL(
				   (
					  SELECT (country.Name) AS Name
					  FROM [${dbxschemaname}].country
					  WHERE country.id IN 
						 (
							SELECT (region.Country_id) AS Country_id
							FROM [${dbxschemaname}].region
							WHERE (region.id = (homeaddress.Region_id))
						 )
				   ), N'')
			 ) AS Home_CountryName, 
		  
			 (
				SELECT ISNULL(
				   (
					  SELECT (country.id) AS id
					  FROM [${dbxschemaname}].country
					  WHERE country.id IN 
						 (
							SELECT (region.Country_id) AS Country_id
							FROM [${dbxschemaname}].region
							WHERE (region.id = (homeaddress.Region_id))
						 )
				   ), N'')
			 ) AS Home_CountryID, 
		  
			 (
			SELECT ISNULL((homeaddress.zipCode), N'')
			 ) AS Home_Zipcode, 
		  
			 (
				SELECT ISNULL((workaddress.id), N'')
			 ) AS Work_AddressID, 
		  
			 (
				SELECT ISNULL((workaddress.addressLine1), N'')
			 ) AS Work_AddressLine1, 
		  
			 (
				SELECT ISNULL((workaddress.addressLine2), N'')
			 ) AS Work_AddressLine2, 
		  
			 (
				SELECT ISNULL(
				   (
					  SELECT (city.Name) AS Name
					  FROM [${dbxschemaname}].city
					  WHERE (city.id = (workaddress.City_id))
				   ), N'')
			 ) AS Work_CityName, 
		  
			 (
				SELECT ISNULL((workaddress.City_id), N'')
			 ) AS Work_CityID, 
		  
			 (
				SELECT ISNULL(
				   (
					  
					  SELECT (region.Name) AS Name
					  FROM [${dbxschemaname}].region
					  WHERE (region.id = (workaddress.Region_id))
				   ), N'')
			 ) AS Work_StateName, 
		  
			 (

				SELECT ISNULL((workaddress.Region_id), N'')
			 ) AS Work_StateID, 
		  
			 (
				SELECT ISNULL(
				   (
					  SELECT (country.Name) AS Name
					  FROM [${dbxschemaname}].country
					  WHERE country.id IN 
						 (
							SELECT (city.Country_id) AS Country_id
							FROM [${dbxschemaname}].city
							WHERE (city.id = (workaddress.City_id))
						 )
				   ), N'')
			 ) AS Work_CountryName, 
		  
			 (
				
				SELECT ISNULL(
				   (
					  
					  SELECT (country.id) AS id
					  FROM [${dbxschemaname}].country
					  WHERE country.id IN 
						 (
							SELECT (city.Country_id) AS Country_id
							FROM [${dbxschemaname}].city
							WHERE (city.id = (workaddress.City_id))
						 )
				   ), N'')
			 ) AS Work_CountryID, 
		  
			 (
				SELECT ISNULL((workaddress.zipCode), N'')
			 ) AS Work_Zipcode,
			 
	(SELECT 
                string_agg([userlob].[lobId], ',')
            FROM
                [${dbxschemaname}].[userlob]
            WHERE
                ([systemuser].[id] = [userlob].[userId])) AS [lobId],
        (SELECT 
                string_agg([lobtext].[displayName]
                        ,',')
            FROM
                [${dbxschemaname}].[lobtext]
            WHERE
                [lobtext].[lobId] IN (SELECT 
                        [userlob].[lobId]
                    FROM
                        [${dbxschemaname}].[userlob]
                    WHERE
                        ([systemuser].[id] = [userlob].[userId]))) AS [lobName],
            (SELECT 
                [${dbxschemaname}].location.Name
            FROM
                [${dbxschemaname}].location
            WHERE
                ( workaddress.id = [${dbxschemaname}].location.Address_id)) AS branchName
	   FROM ((([${dbxschemaname}].systemuser 
		  INNER JOIN [${dbxschemaname}].status 
		  ON ((systemuser.Status_id = status.id))) 
		  LEFT JOIN [${dbxschemaname}].address  AS homeaddress 
		  ON (homeaddress.id IN 
			 (
				SELECT useraddress.Address_id
				FROM [${dbxschemaname}].useraddress
				WHERE ((systemuser.id = useraddress.User_id) AND (useraddress.Type_id = 'ADR_TYPE_HOME'))
			 ))) 
		  LEFT JOIN [${dbxschemaname}].address  AS workaddress 
		  ON (workaddress.id IN 
			 (
				SELECT useraddress.Address_id
				FROM [${dbxschemaname}].useraddress
				WHERE ((systemuser.id = useraddress.User_id) AND (useraddress.Type_id = 'ADR_TYPE_WORK'))
			 )));
GO


DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc]
@_matrixValues NVARCHAR(max),
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
set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;

WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1;
IF @index1 = @length + 1 
BREAK
ELSE
BEGIN
set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
set @matrixComma = REPLACE(@matrixRecord, ';', ',');
set @matrixComma = REPLACE(@matrixComma,'"','''');
set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.approvalruleId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit) VALUES (',@matrixComma,');');
execute (@query);
set @sigValues = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_signatorymatrixValues, '#', @index1), '#', -1 );
SET @id = @@IDENTITY;
SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1 );
SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1 );
set @id = REPLACE(@id,'"','''');
set @groupList = REPLACE(@groupList,'"','''');
set @groupRule = REPLACE(@groupRule,'"','''');
set @query = ('INSERT INTO [${dbxschemaname}].signatorygroupmatrix(signatorygroupmatrix.approvalMatrixId,signatorygroupmatrix.groupList,signatorygroupmatrix.groupRule) VALUES (')+@id+','+''''+@groupList+''''+','+''''+@groupRule+''''+')';
execute (@query);
CONTINUE
END
END
END
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internaluserskc_view];
GO
CREATE VIEW [${dbxschemaname}].[internaluserskc_view] AS
    SELECT 
        [internalusertype].[userId] AS [User_id],
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
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[systemroles_permission_proc]  
   @_roleIds varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 SELECT distinct
        [p].[id] AS id,
        [p].[Name] AS name,
        [p].[Status_id] AS status,
        [p].[PermissionValue] AS PermissionValue,
        [p].[isComposite] AS isComposite,
        [rp].[Role_id] AS Role_id,
		[p].[softdeleteflag] AS softdeleteflag
    FROM
    rolepermission rp , permission p
    WHERE [p].[id] = [rp].[Permission_id] AND [p].[Status_id]='SID_ACTIVE' 
	and 
		 [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) > 0 ORDER BY [id];
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[account_action_approvers_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc](
 @_contractId varchar(50),
 @_cif varchar(50),
 @_accountIds text,
 @_approvalActionList varchar(50),
 @_featureId varchar(50)
)
AS
BEGIN
 declare @customerIdList nvarchar(max);
 declare @customerIdListWithNoAccountAccess nvarchar(max);
 declare @NumberOfAccounts int;
 SELECT @customerIdList = STRING_AGG( CAST(temp.Customer_id as nvarchar(max)), ',') from (SELECT DISTINCT [customeraction].Customer_id as Customer_id from [${dbxschemaname}].[customeraction]
			where
				[${dbxschemaname}].[customeraction].isAllowed = '0'
				and [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
				and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].[Account_id], @_accountIds) > 0
				and [${dbxschemaname}].[customeraction].contractId = @_contractId
				and [${dbxschemaname}].[customeraction].coreCustomerId = @_cif ) as temp;

SET @customerIdList = CASE WHEN @customerIdList is null THEN  '' ELSE  @customerIdList END;
SET @NumberOfAccounts = LEN(cast(@_accountIds as nvarchar(max))) - LEN(REPLACE(cast(@_accountIds as nvarchar(max)), ',', '')) + 1;

 select @customerIdListWithNoAccountAccess = STRING_AGG(CAST(temp.Customer_id as nvarchar(max)), ',') from (SELECT DISTINCT tempCustomers.Customer_id from (SELECT Customer_id, count(Account_id) as countAccounts
				from [${dbxschemaname}].[customeraccounts]
				where [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountIds) > 0
				group by [customeraccounts].Customer_id) as tempCustomers
				where tempCustomers.countAccounts != @NumberOfAccounts) as temp;
										  
SET @customerIdListWithNoAccountAccess = CASE WHEN @customerIdListWithNoAccountAccess is null THEN  '' ELSE  @customerIdListWithNoAccountAccess END;

SELECT 
	DISTINCT ([${dbxschemaname}].[customer].id ) AS id , ([${dbxschemaname}].[customer].username) AS userName , ([${dbxschemaname}].[membergroup].Name) AS groupId,
										([customer].[FirstName]) AS firstName , ([customer].[LastName]) AS lastName
from 
	([${dbxschemaname}].[customer]
LEFT JOIN [${dbxschemaname}].[contractcustomers] ON ([${dbxschemaname}].[contractcustomers].customerId = [${dbxschemaname}].[customer].[id] and 
[${dbxschemaname}].[contractcustomers].[contractId] = @_contractId and [${dbxschemaname}].[contractcustomers].[coreCustomerId] = @_cif)
LEFT JOIN [${dbxschemaname}].[customergroup] ON ([${dbxschemaname}].[customergroup].[Customer_id] = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[customeraction] ON ([${dbxschemaname}].[customeraction].[Customer_id] = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[membergroup] ON ([${dbxschemaname}].[membergroup].id = [${dbxschemaname}].[customergroup].Group_id)
LEFT JOIN [${dbxschemaname}].[groupactionlimit] ON ([${dbxschemaname}].[groupactionlimit].Group_id = [${dbxschemaname}].[customergroup].Group_id)
INNER JOIN [${dbxschemaname}].[customeraccounts] ON ([${dbxschemaname}].[customeraccounts].Customer_id = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[contractfeatures] ON ([${dbxschemaname}].[contractfeatures].contractId = @_contractId and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif))
	where 
        [${dbxschemaname}].[contractfeatures].contractId = @_contractId
        and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif
        and [${dbxschemaname}].[contractfeatures].featureId = @_featureId
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id,  @_accountIds) > 0
		and [${dbxschemaname}].[customer].Status_id = 'SID_CUS_ACTIVE'
        and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].[id],  @customerIdList) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].id,  @customerIdListWithNoAccountAccess) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[groupactionlimit].Action_id,@_approvalActionList) > 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].Action_id,@_approvalActionList) > 0;      
END
GO


DROP procedure IF EXISTS [${dbxschemaname}].[fetch_achfiles_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_achfiles_proc]  
   @_customerId nvarchar(50),
   @_achFile_id nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 

   BEGIN

     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
	 DECLARE @isSelfApprovalEnabled bit
	 DECLARE @notCreatedBySelf nvarchar(max)
     
      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

     SET @combinedIds = (select String_agg(CAST(customer.id as nvarchar(max)), ',') from [${dbxschemaname}].customer where customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)

	   SET @isSelfApprovalEnabled = (select [isSelfApprovalEnabled] from [${dbxschemaname}].application)

	   IF(@isSelfApprovalEnabled = 0)
		BEGIN
			SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(achfile.createdby, ''',@combinedIds,''')=0')
		END
	   ELSE 
		BEGIN
			SET @notCreatedBySelf = '';
		END
		
		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='' ) THEN  '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE  @_filterByValue END
        SET @_achFile_id = CASE WHEN (@_achFile_id = '' OR @_achFile_id IS NULL) THEN '%' ELSE @_achFile_id END


      IF datalength(@_filterByParam) > 0
         SET @numOfParams = datalength(@_filterByParam) - datalength(replace(@_filterByParam, ',', '')) + 1
      
      WHILE (@idx < @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByParam, @idx), -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByValue,   @idx), -1 );
            SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
            SET @idx = @idx + 1   
         END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

        SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam IS NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder IS NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
      SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END


      SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							)

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, ''' + (@combinedIds) + ''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achfile.requestId AS nvarchar(max)),  ''' + (@approvalRequestIds) + ''')<>0  AND [${dbxschemaname}].achfile.status = ''Pending'' ') + @notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN ('AND [${dbxschemaname}].achfile.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_achFile_id = '%') THEN (' AND NOT [${dbxschemaname}].achfile.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END


		SET @searchQuery = CASE WHEN (@_searchString IS NULL OR @_searchString = '') THEN @searchQuery ELSE 
                (@searchQuery+ ' AND (achfile.achFileName LIKE ''%'+@_searchString+'%'' OR achfile.requestType LIKE ''%'+@_searchString+'%'')') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset IS NULL OR @_pageOffset = '' OR @_pageSize IS NULL OR @_pageSize = '') THEN
		'' ELSE (' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT  ' +@_pageSize+'  ROWS ONLY') END


      SET @features = ( SELECT String_agg([${dbxschemaname}].featureaction.Feature_id ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg([${dbxschemaname}].featureaction.id,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg([${dbxschemaname}].bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END


	 SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='''' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

	  
	  
	 SET @select_statement = ' 
	  SELECT * FROM
        (SELECT 
         achfile.achFile_id AS achFile_id,
         achfile.achFileName AS achFileName,
         achfile.featureActionId AS featureActionId,
         achfile.debitAmount AS debitAmount,
         achfile.approvalAccounts AS approvalAccounts,
         achfile.debitAccounts AS debitAccounts,
         achfile.createdby AS createdby,
         achfile.createdts AS createdts,
         achfile.requestType AS requestType,
         achfile.numberOfCredits AS numberOfCredits,
         achfile.numberOfDebits AS numberOfDebits,
         achfile.numberOfPrenotes AS numberOfPrenotes,
         achfile.requestId AS requestId,
         achfile.contents AS contents,
         achfile.fileSize AS fileSize,
         achfile.softDelete AS softDelete,
         achfile.creditAmount AS creditAmount,
         achfile.numberOfRecords AS numberOfRecords,
         achfile.achFileFormatType_id AS achFileFormatType_id,
		 ( CASE 
				WHEN bbrequest.status is NULL THEN achfile.status
                ELSE bbrequest.status 
			END ) AS status,
         achfile.companyId AS companyId,
         achfile.confirmationNumber AS confirmationNumber,
         customer.UserName AS userName,
         achfileformattype.fileType AS achFileFormatType,
         bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         ((([${dbxschemaname}].achfile
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achfile.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].achfileformattype ON ([${dbxschemaname}].achfile.achFileFormatType_id = [${dbxschemaname}].achfileformattype.id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achfile.softDelete = ''0''
            AND ([${dbxschemaname}].FIND_IN_SET(achfile.companyId, '''+ @companyId + ''') <> 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, '''+ @combinedIds + ''')<>0)
            AND [${dbxschemaname}].achfile.achFile_id LIKE '''+@_achFile_id + '''
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.featureActionId, '''+ @createActions + ''')<>0 '+
                @queryTypecondition + ' '+
                @searchQuery + ') AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+ @companyRequestIds +''')<>0 
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' '+ @_sortOrder + ' ' +
            @paginationQuery
            EXEC(@select_statement)
	DROP TABLE #approvalCount1;
	DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_bbtemplate_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_bbtemplate_proc]  
   @_customerId nvarchar(50),
   @_templateId nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   BEGIN

     MAINLABEL: 
      
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     

     SET @group_concat_max_len = 100000000
     SET @combinedIds = (select String_agg(id, ',') from [${dbxschemaname}].customer where combinedUserId = @_customerId)

     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
  
    SET @_filterByParam = CASE WHEN @_filterByParam IS NULL OR @_filterByParam='' THEN  '' ELSE @_filterByParam END
    SET @_filterByValue = CASE WHEN @_filterByValue IS NULL OR @_filterByValue='' THEN '' ELSE @_filterByValue END
    SET @_templateId = CASE WHEN @_templateId = '' OR @_templateId IS NULL THEN '%' ELSE @_templateId END
    SET @numOfParams = 0

    IF datalength(@_filterByParam) > 0
      SET @numOfParams = datalength(@_filterByParam) - datalength(REPLACE(@_filterByParam, ',', '')) + 1

    SET @searchQuery = ''
    SET @idx = 1
    WHILE(@idx <= @numOfParams) 
    BEGIN       
    SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx),',', -1 );
      SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',',   @idx),',', -1 );
      SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
      SET @idx = @idx + 1   
    END

    SET @companyId = ( SELECT String_agg(CAST(concat(trim(contractcustomers.contractId),'_',trim(contractcustomers.coreCustomerId)) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
	

    IF @companyId IS NULL
      BEGIN
        SET @companyId = ''
      END

    IF @_featureactionlist IS NULL
      BEGIN
         GOTO MAINLABEL$leave
      END

    SET @_sortByParam = CASE WHEN @_sortByParam = '' OR @_sortByParam is NULL THEN 'createdts' ELSE @_sortByParam END
    SET @_sortOrder = CASE WHEN @_sortOrder = '' OR @_sortOrder is NULL THEN 'DESC' ELSE @_sortOrder END  
    SET @customerMatrixIds = ( SELECT String_agg(CAST(approvalMatrixId  as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerId, @combinedIds) <> 0)

    IF @customerMatrixIds IS NULL
      BEGIN
        SET @customerMatrixIds = ''
      END
  
    SET @alreadyApprovedIds = (SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')

    IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END

    SET @approvalRequestIds = (  
         SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) = 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

    IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

    SET @queryTypecondition = 
      CASE WHEN (@_queryType = 'myRequests') THEN 
            (' AND  [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby,') + (@combinedIds) + (') <> 0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'') ')
        ELSE 
            CASE WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.requestId,  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].bbtemplate.status = ''Pending'' ')
              ELSE 
               CASE WHEN @_templateId = '%' THEN ' AND NOT [${dbxschemaname}].bbtemplate.status = ''Withdrawn''' 
                ELSE '' 
               END
            END
        END

      SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery 
                  ELSE 
                   @searchQuery+ ' AND ([${dbxschemaname}].bbtemplate.templateName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].customeraccounts.AccountName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplate.fromAccount LIKE ''%'+@_searchString+'%'')' 
                  END

      SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' 
                  ELSE
                    ' OFFSET '+ @_pageOffset +'  ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY' 
                  END

      SET @features = ( SELECT String_agg(CAST(Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureactionlist) <> 0 )
      
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE_TEMPLATE')

      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END
      SET @customerAcounts = ( SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].customeraccounts WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id , @_customerId) <> 0)

      IF @customerAcounts IS NULL
        BEGIN
          SET @customerAcounts = ''
        END

		SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2


      SET @select_statement = 
        '
		SELECT * FROM
          (SELECT 
            bbtemplate.templateId AS templateId,
            bbtemplate.templateName AS templateName,
            bbtemplate.templateDescription AS templateDescription,
            bbtemplate.featureActionId AS featureActionId,
            bbtemplate.fromAccount AS fromAccount,
            bbtemplate.effectiveDate AS effectiveDate,
            bbtemplate.requestId AS requestId,
            bbtemplate.createdby AS createdby,
            bbtemplate.updatedBy AS updatedBy,
            bbtemplate.createdts AS createdts,
            bbtemplate.maxAmount AS maxAmount,
            bbtemplate.status AS status,
            bbtemplate.transactionType_id AS transactionType_id,
            bbtemplate.templateType_id AS templateType_id,
            bbtemplate.companyId AS companyId,
            bbtemplate.templateRequestType_id AS templateRequestType_id,
            bbtemplate.softDelete AS softDelete,
            bbtemplate.totalAmount AS totalAmount,
            ( 
            CASE 
               WHEN customeraccounts.AccountName is NULL THEN ''AccountName'' 
               ELSE customeraccounts.AccountName 
            END 
            ) AS accountName,
            customer.UserName AS userName,
            bbtransactiontype.transactionTypeName AS transactionTypeName,
            bbtemplatetype.templateTypeName AS templateTypeName,
            bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
            bbrequest.createdby AS requestCreatedby,
            (
            CASE
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbtemplate.createdby as nvarchar(max)),CAST('''+ @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amICreator,
            (
            CASE 
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),CAST('''+@approvalRequestIds+''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amIApprover
            FROM
            ((((((([${dbxschemaname}].bbtemplate
            LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customer.id))
            LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customeraccounts.Customer_id) AND ([${dbxschemaname}].bbtemplate.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
            LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].bbtemplate.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].bbtemplate.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].bbtemplate.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
            LEFT JOIN [${dbxschemaname}].organisation ON ([${dbxschemaname}].bbtemplate.companyId = [${dbxschemaname}].organisation.id))
            LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbtemplate.requestId = [${dbxschemaname}].bbrequest.requestId))
            WHERE [${dbxschemaname}].bbtemplate.softDelete = ''0''
            AND ([${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.companyId, '''+ @companyId +''') > 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby, '''+@combinedIds+''')>0)
            AND [${dbxschemaname}].bbtemplate.templateId LIKE '''+@_templateId +'''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.fromAccount,'''+ @customerAcounts+ ''')<>0
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.featureActionId,'''+ @createActions+''')<>0'+
                @queryTypecondition+ ' '+
                @searchQuery+ ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               [${dbxschemaname}].bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
            [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+@companyRequestIds+''')<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId,[${dbxschemaname}].requestapprovalmatrix.approvalMatrixId,[${dbxschemaname}].approvalrule.numberOfApprovals
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam+' '+ @_sortOrder + ' '+
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	  DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:

GO

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolePermissionDelete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolePermissionDelete_proc] 
 @_roleId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000)
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
	DELETE FROM [${dbxschemaname}].[rolecompositeaction] WHERE Role_id=@_roleId AND CompositeAction_id=@caid;
	END 
	END
	MANAGECAIDS$LEAVE:
	CLOSE caids
	DEALLOCATE caids;
	DELETE FROM [${dbxschemaname}].[rolepermission] WHERE Role_id=@_roleId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
GO

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[userPermissionDelete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[userPermissionDelete_proc] 
 @_userId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000)
AS 
   BEGIN
    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
	DECLARE @caid VARCHAR(50);
	DECLARE @isEnabled VARCHAR(10);
	DECLARE @finished INTEGER = 0 ;
	DECLARE @caid_count INTEGER = 0 ;
	DECLARE caids CURSOR FOR (SELECT c.id FROM [${dbxschemaname}].[compositeaction] c WHERE [${dbxschemaname}].FIND_IN_SET([Permission_id], @_PermissionIds) > 0);

	OPEN caids; 
	MANAGECAIDS: 
	WHILE 1=1
	BEGIN
	FETCH caids INTO @caid;
	IF @@FETCH_STATUS <> 0 BEGIN 
		 GOTO MANAGECAIDS$LEAVE
	END
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  [${dbxschemaname}].[userpermission] up WHERE up.User_id = @_userId
	AND up.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(up.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	DELETE FROM [${dbxschemaname}].[usercompositeaction] WHERE User_id=@_userId AND CompositeAction_id=@caid;
	END 
	END
	MANAGECAIDS$LEAVE:
	CLOSE caids;
	DEALLOCATE caids;
	DELETE FROM [${dbxschemaname}].[userpermission] WHERE User_id=@_userId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;

	END
GO

ALTER TABLE [${dbxschemaname}].[alertsubtype] ALTER COLUMN [isGlobal] TINYINT NOT NULL;
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].[application]', 'isKeyCloakEnabled';
GO
ALTER TABLE [${dbxschemaname}].[application] ALTER COLUMN isKeyCloakEnabled bit not null;
GO
UPDATE [${dbxschemaname}].application SET isKeyCloakEnabled = 1 WHERE (id = '2');
GO
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].[groupservicedefinition]', 'isDefaultGroup';
GO
ALTER TABLE [${dbxschemaname}].[groupservicedefinition] ALTER COLUMN isDefaultGroup bit not null; 
GO

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_reports_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[get_reports_proc]
	@p_userId nvarchar(50),
	@p_roleId nvarchar(250)
AS
	BEGIN	
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
		select * from [${dbxschemaname}].report rp where rp.createdby=@p_userId
		UNION
		SELECT rp.* from [${dbxschemaname}].report rp,[${dbxschemaname}].sharedreport sr
		where sr.reportId=rp.id
		and sr.userid=@p_userId
		union
		SELECT rp.* from [${dbxschemaname}].report rp,[${dbxschemaname}].sharedreport sr
		where sr.reportId=rp.id
		and [${dbxschemaname}].FIND_IN_SET(sr.roleId,@p_roleId) > 0 order by createdts desc;
 END
GO


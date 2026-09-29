GO
CREATE TABLE [${dbxschemaname}].[sca_actions](
	[id] int IDENTITY NOT NULL,
	[App_id] NVARCHAR(50) NOT NULL,
	[Action_id] NVARCHAR(50) NOT NULL,
	[Status_id] NVARCHAR(50) NOT NULL,
	[Description] NVARCHAR(500) DEFAULT NULL,
	[createdby] NVARCHAR(50) DEFAULT NULL,
	[modifiedby] NVARCHAR(50) DEFAULT NULL,
	[createdts] datetime2 NOT NULL,
	[lastmodifiedts] datetime2 NOT NULL,
	[synctimestamp] datetime2 NOT NULL,
	[softdeleteflag] bit NOT NULL DEFAULT '0',
	[risk_score] NVARCHAR(50) DEFAULT NULL,
  	PRIMARY KEY ([id])
)

GO

GO

CREATE TABLE [${dbxschemaname}].[external_feature_actions](
	[id] NVARCHAR(100),
	[Feature_id] NVARCHAR(50),
	[App_id] NVARCHAR(50),
	[Type_id] NVARCHAR(50),
	[Rrole_id] NVARCHAR(50),
	[name] NVARCHAR(50),
	[description] NVARCHAR(500),
	[isAccountLevel] int NULL,
	[isMFAApplicable] int NULL,
	[MFA_id] NVARCHAR(50),
	[TermsAndConditions_id] NVARCHAR(50),
	[notes] NVARCHAR(50),
	[isPrimary] int NULL,
	[DisplaySequence] int NULL,
	[dependency] NVARCHAR(50),
	[createdby] NVARCHAR(50),
	[modifiedby] NVARCHAR(50),
	[createdts] datetime2,
	[lastmodifiedts] datetime2,
	[synctimestamp] datetime2,
	[softdeleteflag] bit NOT NULL DEFAULT '0',
	[status] NVARCHAR(50),
	[limitgroupId] NVARCHAR(50),
	[accesspolicyId] NVARCHAR(50),
	[actionlevelId] NVARCHAR(50),
	[approveFeatureAction] NVARCHAR(50),
	[isApprovalAction] int NULL
)
GO

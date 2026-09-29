USE [${dbxdbname}]
GO

/****** Object:  Table [${dbxschemaname}].[customview]    Script Date: 08-09-2020 12:50:03 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS ( SELECT  * FROM    sys.schemas WHERE   name = N'${dbxschemaname}' )
    EXEC('CREATE SCHEMA [${dbxschemaname}]');
GO
CREATE TABLE [${dbxschemaname}].[customview](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](300) NULL,
	[customerId] [varchar](50) NULL,
	[accountIds] [nvarchar](max) NULL,
CONSTRAINT [PK_customview_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customview] ADD  DEFAULT (NULL) FOR [customerId]
GO

ALTER TABLE [${dbxschemaname}].[customview] ADD  DEFAULT (NULL) FOR [accountIds]
GO


/****** Object:  Table [${dbxschemaname}].[p2ppayee]    Script Date: 08-09-2020 12:50:24 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[p2ppayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [varchar](45) NULL,
	[payeeId] [varchar](50) NOT NULL,
	[customerId] [varchar](50) NOT NULL,
	[createdts] [datetime] NOT NULL,
	[updatedts] [datetime] NOT NULL,
CONSTRAINT [PK_p2ppayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[p2ppayee] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[p2ppayee] ADD  DEFAULT (getdate()) FOR [updatedts]
GO

/****** Object:  Table [${dbxschemaname}].[rrole]    Script Date: 15-09-2020 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[rrole];
CREATE TABLE [${dbxschemaname}].[rrole] (
  [id] VARCHAR(50) NOT NULL,
  [createdts]DATETIME2(0) NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [softdeleteflag] SMALLINT NULL DEFAULT '0',
  PRIMARY KEY ([id])) ;
 GO 


ALTER TABLE [${dbxschemaname}].[featureaction] ADD [Rrole_id] VARCHAR(50) NULL
CREATE INDEX [FK_action_rrole_id_idx] ON [${dbxschemaname}].[featureaction] ([Rrole_id] ASC);
GO

ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT [FK_featureaction_rrole] FOREIGN KEY ([Rrole_id]) REFERENCES [${dbxschemaname}].[rrole] ([id]) 
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentfiles];
CREATE TABLE [${dbxschemaname}].[bulkpaymentfiles] (
  [fileId] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [fileName] varchar(50) NOT NULL,
  [description] varchar(100) DEFAULT NULL,
  [content] varchar(max) NOT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [uploadedBy] nvarchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [uploadedDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] varchar(50) DEFAULT NULL,
  [totalAmount] bigint DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [requestId] varchar(50) DEFAULT NULL,
  [fileSize] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,  
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([fileId])
 ,
  CONSTRAINT [FK_bulkpaymentfiles_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentfiles_uploadedBy_idx] FOREIGN KEY ([uploadedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentfiles_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentfiles_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentfiles_requestId] ON [${dbxschemaname}].[bulkpaymentfiles] ([requestId]);
CREATE INDEX [FK_bulkpaymentfiles_fromAccount] ON [${dbxschemaname}].[bulkpaymentfiles] ([fromAccount]);
CREATE INDEX [FK_bulkpaymentfiles_uploadedDate] ON [${dbxschemaname}].[bulkpaymentfiles] ([uploadedDate]);
CREATE INDEX [FK_bulkpaymentfiles_featureActionId] ON [${dbxschemaname}].[bulkpaymentfiles] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentfiles_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentfiles] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentfiles_companyId] ON [${dbxschemaname}].[bulkpaymentfiles] ([companyId]);
CREATE INDEX [FK_bulkpaymentfiles_uploadedBy] ON [${dbxschemaname}].[bulkpaymentfiles] ([uploadedBy]);
CREATE INDEX [FK_bulkpaymentfiles_status] ON [${dbxschemaname}].[bulkpaymentfiles] ([status]);
CREATE INDEX [FK_bulkpaymentfiles_roleId] ON [${dbxschemaname}].[bulkpaymentfiles] ([roleId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentrecord];
CREATE TABLE [${dbxschemaname}].[bulkpaymentrecord] (
  [recordId] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [paymentId] varchar(50) DEFAULT NULL,
  [description] varchar(100) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [paymentDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [scheduledDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [fileId] varchar(50) NULL,

  [reviewedBy] nvarchar(50) DEFAULT NULL,
  [initiatedBy] varchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] varchar(50) DEFAULT NULL,
  [totalAmount] bigint DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [requestId] varchar(50) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([recordId])
 ,
  CONSTRAINT [FK_bulkpaymentrecord_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrecord_reviewedBy_idx] FOREIGN KEY ([reviewedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentrecord_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrecord_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) 

CREATE INDEX [FK_bulkpaymentrecord_requestId] ON [${dbxschemaname}].[bulkpaymentrecord] ([requestId]);
CREATE INDEX [FK_bulkpaymentrecord_fromAccount] ON [${dbxschemaname}].[bulkpaymentrecord] ([fromAccount]);
CREATE INDEX [FK_bulkpaymentrecord_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentrecord] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentrecord_featureActionId] ON [${dbxschemaname}].[bulkpaymentrecord] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentrecord_companyId] ON [${dbxschemaname}].[bulkpaymentrecord] ([companyId]);
CREATE INDEX [FK_bulkpaymentrecord_reviewedBy] ON [${dbxschemaname}].[bulkpaymentrecord] ([reviewedBy]);
CREATE INDEX [FK_bulkpaymentrecord_initiatedBy] ON [${dbxschemaname}].[bulkpaymentrecord] ([initiatedBy]);
CREATE INDEX [FK_bulkpaymentrecord_status] ON [${dbxschemaname}].[bulkpaymentrecord] ([status]);
CREATE INDEX [FK_bulkpaymentrecord_roleId] ON [${dbxschemaname}].[bulkpaymentrecord] ([roleId]);
CREATE INDEX [FK_bulkpaymentrecord_fileId] ON [${dbxschemaname}].[bulkpaymentrecord] ([fileId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentsubrecord];
CREATE TABLE [${dbxschemaname}].[bulkpaymentsubrecord] (
  [paymentOrderId] varchar(50) NOT NULL,
  [recordId] varchar(50) DEFAULT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [recipientName] varchar(50) NOT NULL,
  [acountNumber] varchar(50) NOT NULL,
  [bankName] varchar(50) DEFAULT NULL,
  [swift] varchar(50) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
 
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  
  [status] varchar(20) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL,
  [amount] bigint DEFAULT NULL,
  [feesPaidBy] varchar(50) DEFAULT NULL,
  [paymentReference] varchar(50) DEFAULT NULL,
 
  [createdby] nvarchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',

  PRIMARY KEY ([paymentOrderId])
 ,
  CONSTRAINT [FK_bulkpaymentsubrecord_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentsubrecord_createdby_idx] FOREIGN KEY ([createdby]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentsubrecord_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentsubrecord_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentsubrecord_acountNumber] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([acountNumber]);
CREATE INDEX [FK_bulkpaymentsubrecord_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentsubrecord_featureActionId] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentsubrecord_companyId] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([companyId]);
CREATE INDEX [FK_bulkpaymentsubrecord_createdby] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([createdby]);
CREATE INDEX [FK_bulkpaymentsubrecord_recipientName] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([recipientName]);
CREATE INDEX [FK_bulkpaymentsubrecord_status] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([status]);
CREATE INDEX [FK_bulkpaymentsubrecord_roleId] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([roleId]);
CREATE INDEX [FK_bulkpaymentsubrecord_recordId] ON [${dbxschemaname}].[bulkpaymentsubrecord] ([recordId]);
GO 

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentfilesmock];
CREATE TABLE [${dbxschemaname}].[bulkpaymentfilesmock] (
  [fileId] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [fileName] varchar(50) NOT NULL,
  [description] varchar(250) DEFAULT NULL,
  [content] varchar(max) NOT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [uploadedBy] nvarchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [uploadedDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] varchar(50) DEFAULT NULL,
  [totalAmount] varchar(50) DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [requestId] varchar(50) DEFAULT NULL,
  [fileSize] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([fileId])
 ,
  CONSTRAINT [FK_bulkpaymentfilesmock_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentfilesmock_uploadedBy_idx] FOREIGN KEY ([uploadedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentfilesmock_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentfilesmock_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentfilesmock_requestId] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([requestId]);
CREATE INDEX [FK_bulkpaymentfilesmock_fromAccount] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([fromAccount]);
CREATE INDEX [FK_bulkpaymentfilesmock_uploadedDate] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([uploadedDate]);
CREATE INDEX [FK_bulkpaymentfilesmock_featureActionId] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentfilesmock_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentfilesmock_companyId] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([companyId]);
CREATE INDEX [FK_bulkpaymentfilesmock_uploadedBy] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([uploadedBy]);
CREATE INDEX [FK_bulkpaymentfilesmock_status] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([status]);
CREATE INDEX [FK_bulkpaymentfilesmock_roleId] ON [${dbxschemaname}].[bulkpaymentfilesmock] ([roleId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentrecordmock];
CREATE TABLE [${dbxschemaname}].[bulkpaymentrecordmock] (
  [recordId] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [paymentId] varchar(50) DEFAULT NULL,
  [description] varchar(250) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [paymentDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [scheduledDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [fileId] varchar(50) NULL,

  [reviewedBy] nvarchar(50) DEFAULT NULL,
  [initiatedBy] varchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] varchar(50) DEFAULT NULL,
  [totalAmount] varchar(50) DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [requestId] varchar(50) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([recordId])
 ,
  CONSTRAINT [FK_bulkpaymentrecordmock_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrecordmock_reviewedBy_idx] FOREIGN KEY ([reviewedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentrecordmock_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrecordmock_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentrecordmock_requestId] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([requestId]);
CREATE INDEX [FK_bulkpaymentrecordmock_fromAccount] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([fromAccount]);
CREATE INDEX [FK_bulkpaymentrecordmock_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentrecordmock_featureActionId] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentrecordmock_companyId] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([companyId]);
CREATE INDEX [FK_bulkpaymentrecordmock_reviewedBy] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([reviewedBy]);
CREATE INDEX [FK_bulkpaymentrecordmock_initiatedBy] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([initiatedBy]);
CREATE INDEX [FK_bulkpaymentrecordmock_status] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([status]);
CREATE INDEX [FK_bulkpaymentrecordmock_roleId] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([roleId]);
CREATE INDEX [FK_bulkpaymentrecordmock_fileId] ON [${dbxschemaname}].[bulkpaymentrecordmock] ([fileId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentsubrecordmock];
CREATE TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] (
  [paymentOrderId] varchar(50) NOT NULL,
  [recordId] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [recipientName] varchar(50) NOT NULL,
  [acountNumber] varchar(50) NOT NULL,
  [bankName] varchar(50) DEFAULT NULL,
  [swift] varchar(50) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
 
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  
  [status] varchar(20) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL,
  [amount] bigint DEFAULT NULL,
  [feesPaidBy] varchar(50) DEFAULT NULL,
  [paymentReference] varchar(50) DEFAULT NULL,
 
  [createdby] nvarchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',

  PRIMARY KEY ([paymentOrderId])
 ,
  CONSTRAINT [FK_bulkpaymentsubrecordmock_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentsubrecordmock_createdby_idx] FOREIGN KEY ([createdby]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentsubrecordmock_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentsubrecordmock_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentsubrecordmock_acountNumber] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([acountNumber]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_featureActionId] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_companyId] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([companyId]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_createdby] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([createdby]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_recipientName] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([recipientName]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_status] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([status]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_roleId] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([roleId]);
CREATE INDEX [FK_bulkpaymentsubrecordmock_recordId] ON [${dbxschemaname}].[bulkpaymentsubrecordmock] ([recordId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentsamplefiles];
CREATE TABLE [${dbxschemaname}].[bulkpaymentsamplefiles] (
  [fileId] varchar(50) NOT NULL,
  [fileName] varchar(50) NOT NULL,
  [description] varchar(100) DEFAULT NULL,
  [content] varchar(max) NOT NULL,
  PRIMARY KEY ([fileId])
) ;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ADD [sysGeneratedFileName] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] ADD [paymentStatus] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] ADD [paymentStatus] VARCHAR(50) NULL;
GO

EXEC sp_rename '[${dbxschemaname}].[bulkpaymentsubrecordmock].acountNumber', 'accountNumber', 'COLUMN';
EXEC sp_rename '[${dbxschemaname}].[bulkpaymentsubrecord].acountNumber', 'accountNumber', 'COLUMN';
GO


ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] ADD [paymentMethod] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] ADD [paymentMethod] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] ADD [accType] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] ADD [accType] VARCHAR(50) NULL;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[verify_user_proc]
	@_phone nvarchar(max),
	@_email nvarchar(max),
	@_dateOfBirth nvarchar(max)
AS
	BEGIN
	
	SET  XACT_ABORT  ON

    SET  NOCOUNT  ON
     
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
		and primaryphone.Customer_id = primaryemail.Customer_id;
		
	END
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [currency] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [currency] VARCHAR(50) NULL;


GO

/****** Object:  Table [${dbxschemaname}].[datasourcetype]    Script Date: 10/5/2020 11:34:01 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[datasourcetype](
	[id] [varchar](20) NOT NULL,
	[name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


/****** Object:  Table [${dbxschemaname}].[datasource]    Script Date: 10/5/2020 11:34:21 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[datasource](
	[id] [varchar](20) NOT NULL,
	[dataSourceTypeId] [varchar](20) NOT NULL,
	[name] [varchar](50) NOT NULL,
	[schema] [varchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[datasource]  WITH CHECK ADD  CONSTRAINT [FK_datasource_datasourcetype] FOREIGN KEY([dataSourceTypeId])
REFERENCES [${dbxschemaname}].[datasourcetype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[datasource] CHECK CONSTRAINT [FK_datasource_datasourcetype]
GO


/****** Object:  Table [${dbxschemaname}].[reportdatasource]    Script Date: 10/5/2020 11:34:50 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[reportdatasource](
	[id] [varchar](36) NOT NULL,
	[dataSourceId] [varchar](20) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[reportdatasource] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[reportdatasource] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[reportdatasource]  WITH CHECK ADD  CONSTRAINT [FK_reportdatasource_datasource] FOREIGN KEY([dataSourceId])
REFERENCES [${dbxschemaname}].[datasource] ([id])
GO

ALTER TABLE [${dbxschemaname}].[reportdatasource] CHECK CONSTRAINT [FK_reportdatasource_datasource]
GO


/****** Object:  Table [${dbxschemaname}].[report]    Script Date: 10/5/2020 11:35:12 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[report](
	[id] [varchar](36) NOT NULL,
	[externalId] [varchar](100) NOT NULL,
	[reportDataSourceId] [varchar](36) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[description] [varchar](255) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL	
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[report] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[report] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[report]  WITH CHECK ADD  CONSTRAINT [FK_report_reportdatasource] FOREIGN KEY([reportDataSourceId])
REFERENCES [${dbxschemaname}].[reportdatasource] ([id])
GO

ALTER TABLE [${dbxschemaname}].[report] CHECK CONSTRAINT [FK_report_reportdatasource]
GO


/****** Object:  Table [${dbxschemaname}].[sharedreport]    Script Date: 10/5/2020 11:37:06 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[sharedreport](
	[reportId] [varchar](36) NOT NULL,
	[userId] [varchar](50) NOT NULL,
	[roleId] [varchar](50) NOT NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[reportId] ASC,
	[userId] ASC,
	[roleId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[sharedreport] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[sharedreport] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[sharedreport] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

/****** Object:  Table [${dbxschemaname}].[customeractionlimits]    Script Date: 10/6/2020 12:11:53 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[customeractionlimits](
	[id] [varchar](50) NOT NULL,
	[RoleType_id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](255) NOT NULL,
	[Account_id] [varchar](50) NULL,
	[isAllowed] [smallint] NOT NULL,
	[LimitType_id] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NOT NULL,
	[lastmodifiedts] [datetime2](0) NOT NULL,
	[synctimestamp] [datetime2](0) NOT NULL,
	[softdeleteflag] [smallint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_customeractionlimits] UNIQUE NONCLUSTERED 
(
	[RoleType_id] ASC,
	[Customer_id] ASC,
	[Action_id] ASC,
	[Account_id] ASC,
	[LimitType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (NULL) FOR [Account_id]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (NULL) FOR [LimitType_id]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (NULL) FOR [value]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits]  WITH CHECK ADD  CONSTRAINT [FK_CustomerActionLimits_Action] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] CHECK CONSTRAINT [FK_CustomerActionLimits_Action]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits]  WITH CHECK ADD  CONSTRAINT [FK_CustomerActionLimits_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] CHECK CONSTRAINT [FK_CustomerActionLimits_Customer]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits]  WITH CHECK ADD  CONSTRAINT [FK_CustomerActionLimits_customertype] FOREIGN KEY([RoleType_id])
REFERENCES [${dbxschemaname}].[membergrouptype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] CHECK CONSTRAINT [FK_CustomerActionLimits_customertype]
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits]  WITH CHECK ADD  CONSTRAINT [FK_CustomerActionLimits_limittype] FOREIGN KEY([LimitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeractionlimits] CHECK CONSTRAINT [FK_CustomerActionLimits_limittype]
GO

DROP procedure IF EXISTS [${dbxschemaname}].[customeractionlimits_create_proc]
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customeractionlimits_create_proc]    Script Date: 10/6/2020 12:10:48 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeractionlimits_create_proc]  
   @_customerActionsCSV nvarchar(max),
   @_accountsCSV nvarchar(max),
   @_customerId nvarchar(50),
   @_businessTypeId nvarchar(50),
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE @finished int = 0

	  DECLARE @id int

      DECLARE @featureActionId varchar(max)

      DECLARE @actionslist varchar(max)

      DECLARE @limitId varchar(max)

      DECLARE @entryStatus int = 0

      DECLARE @accountId varchar(max)

      DECLARE @actualLimitId varchar(max)

	  DECLARE @groupId varchar(max)
	   
	  DECLARE @validActionsList varchar(max)

	  DECLARE @limitvalue varchar(max)

      IF (
         CASE 
            WHEN @_groupId IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_groupId = '')

         SET @groupId = 
            (
               SELECT groupbusinesstype.Group_id
               FROM [${dbxschemaname}].groupbusinesstype
               WHERE groupbusinesstype.BusinessType_id = @_businessTypeId AND groupbusinesstype.isDefaultGroup = 1
            )

      ELSE 
        
         SET @groupId = @_groupId

		 SET @groupId = 
         (
            SELECT membergroup.id
            FROM [${dbxschemaname}].membergroup
            WHERE 
               membergroup.id = @groupId AND 
               membergroup.Type_id = 'TYPE_ID_BUSINESS' AND 
               membergroup.Status_id = 'SID_ACTIVE'
         )

		 SET @validActionsList = 
         (
            SELECT NULL
            FROM [${dbxschemaname}].groupactionlimit
            WHERE groupactionlimit.Group_id = @groupId AND ([${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, @_customerActionsCSV) <> 0)
         )

		 DECLARE
          accounts CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT Account_id
               FROM [${dbxschemaname}].accounts
               WHERE [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountsCSV) <> 0
             )
     
      WHILE (1 = 1)
      
         BEGIN

            FETCH accounts
                INTO @accountId

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            
			ELSE 
               BEGIN

                  DECLARE
                      actions CURSOR LOCAL FORWARD_ONLY FOR 
                         (
                           SELECT featureaction.id
                           FROM [${dbxschemaname}].featureaction
                           WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validActionsList) <> 0
                         )

                  WHILE (1 = 1)
                  
                     BEGIN

                        SET @entryStatus = 0

                        IF @@FETCH_STATUS <> 0
                           SET @finished = 1

                        IF @finished = 1
                           BREAK
                        ELSE 
                           BEGIN

                              DECLARE
                                  limits CURSOR LOCAL FORWARD_ONLY FOR 
                                     (
                                       SELECT actionlimit.LimitType_id
                                       FROM [${dbxschemaname}].actionlimit
                                       WHERE actionlimit.Action_id = @featureActionId
                                     )

                              OPEN limits

                              WHILE (1 = 1)
                              
                                 BEGIN

                                    FETCH limits
                                        INTO @limitId

                                    IF @@FETCH_STATUS <> 0
                                       SET @finished = 1

                                    IF @finished = 1
                                       BREAK
                                    ELSE 
                                       BEGIN

                                          SET @limitvalue = 
                                             (
                                                SELECT actionlimit.value
                                                FROM [${dbxschemaname}].actionlimit
                                                WHERE actionlimit.Action_id = @featureActionId AND actionlimit.LimitType_id = @limitId
                                             )

                                          IF (@limitId = 'MAX_TRANSACTION_LIMIT')
                                             SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
                                          ELSE 
                                             IF (@limitId = 'MIN_TRANSACTION_LIMIT')
                                                SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
                                             ELSE 
                                               
                                                IF (@limitId = 'DAILY_LIMIT')
                                                   BEGIN

                                                      SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'

                                                      SET @id = 
                                                         (
                                                            SELECT left(newid(), 50)
                                                         )

                                                      INSERT [${dbxschemaname}].customeractionlimits(
                                                         [${dbxschemaname}].customeractionlimits.id, 
                                                         [${dbxschemaname}].customeractionlimits.RoleType_id, 
                                                         [${dbxschemaname}].customeractionlimits.Customer_id, 
                                                         [${dbxschemaname}].customeractionlimits.Action_id, 
                                                         [${dbxschemaname}].customeractionlimits.Account_id, 
                                                         [${dbxschemaname}].customeractionlimits.isAllowed, 
                                                         [${dbxschemaname}].customeractionlimits.LimitType_id, 
                                                         [${dbxschemaname}].customeractionlimits.[value])
                                                         VALUES (
                                                            @id, 
                                                            N'TYPE_ID_BUSINESS', 
                                                            @_customerId, 
                                                            @featureActionId, 
                                                            @accountId, 
                                                            1, 
                                                            @actualLimitId, 
                                                            0.00)

														SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

                                                   END
                                                ELSE 
                                                   BEGIN

                                                      IF (@limitId = 'WEEKLY_LIMIT')
                                                         BEGIN

                                                            SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'

                                                            SET @id = 
                                                               (
                                                                  SELECT left(newid(), 50)
                                                               )
                                                            INSERT [${dbxschemaname}].customeractionlimits(
                                                               [${dbxschemaname}].customeractionlimits.id, 
                                                               [${dbxschemaname}].customeractionlimits.RoleType_id, 
                                                               [${dbxschemaname}].customeractionlimits.Customer_id, 
                                                               [${dbxschemaname}].customeractionlimits.Action_id, 
                                                               [${dbxschemaname}].customeractionlimits.Account_id, 
                                                               [${dbxschemaname}].customeractionlimits.isAllowed, 
                                                               [${dbxschemaname}].customeractionlimits.LimitType_id, 
                                                               [${dbxschemaname}].customeractionlimits.[value])
                                                               VALUES (
                                                                  @id, 
                                                                  N'TYPE_ID_BUSINESS', 
                                                                  @_customerId, 
                                                                  @featureActionId, 
                                                                  @accountId, 
                                                                  1, 
                                                                  @actualLimitId, 
                                                                  0.00)

                                                            SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'

                                                         END
                                                   END

                                          SET @id = 
                                             (
                                                SELECT left(newid(), 50)
                                             )
                                          
                                          INSERT [${dbxschemaname}].customeractionlimits(
                                             [${dbxschemaname}].customeractionlimits.id, 
                                             [${dbxschemaname}].customeractionlimits.RoleType_id, 
                                             [${dbxschemaname}].customeractionlimits.Customer_id, 
                                             [${dbxschemaname}].customeractionlimits.Action_id, 
                                             [${dbxschemaname}].customeractionlimits.Account_id, 
                                             [${dbxschemaname}].customeractionlimits.isAllowed, 
                                             [${dbxschemaname}].customeractionlimits.LimitType_id, 
                                             [${dbxschemaname}].customeractionlimits.[value])
                                             VALUES (
                                                @id, 
                                                N'TYPE_ID_BUSINESS', 
                                                @_customerId, 
                                                @featureActionId, 
                                                @accountId, 
                                                1, 
                                                @actualLimitId, 
                                                @limitvalue)

                                          SET @entryStatus = 1

                                          CONTINUE

                                       END

                                 END

                              CLOSE limits

                              DEALLOCATE limits

                              SET @finished = 0

                              IF @entryStatus = 0
                                 BEGIN

                                    SET @id = 
                                       (
                                          SELECT left(newid(), 50)
                                       )

                                    INSERT [${dbxschemaname}].customeractionlimits(
                                       [${dbxschemaname}].customeractionlimits.id, 
                                       [${dbxschemaname}].customeractionlimits.RoleType_id, 
                                       [${dbxschemaname}].customeractionlimits.Customer_id, 
                                       [${dbxschemaname}].customeractionlimits.Action_id, 
                                       [${dbxschemaname}].customeractionlimits.Account_id, 
                                       [${dbxschemaname}].customeractionlimits.isAllowed)
                                       VALUES (
                                          @id, 
                                          N'TYPE_ID_BUSINESS', 
                                          @_customerId, 
                                          @featureActionId, 
                                          @accountId, 
                                          1)

                                 END

                              SET @actionslist = @featureActionId + N',' + @actionslist

                              CONTINUE

                           END

                     END

                  CLOSE actions
                  
                  DEALLOCATE actions

                  CONTINUE

               END

         END

      CLOSE accounts

      DEALLOCATE accounts

      SET @actionslist = 
         (
            SELECT substring(@actionslist, 1, (len(@actionslist) - 1))
         )

      SELECT @actionslist

   END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].[customer_action_limits_delete]
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_action_limits_delete]    Script Date: 10/6/2020 12:19:05 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customer_action_limits_delete]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeractionlimits
      WHERE customeractionlimits.Customer_id = @_customerId

   END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].customer_action_limits_proc;
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_action_limits_proc]    Script Date: 10/6/2020 12:32:55 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customer_action_limits_proc]  
   @_customerId nvarchar(50),
   @_actionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @group_concat_max_len bigint
	  declare @orgId nvarchar(max)
      SET @group_concat_max_len = 100000000

      SET @orgId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

      IF @orgId IS NULL
 
         SET @orgId = N''
		 declare @active_features nvarchar(max)
      SET @active_features = 
         (

            SELECT String_agg(cast(id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].feature
            WHERE feature.Status_id = 'SID_FEATURE_ACTIVE'

         )

      IF @active_features IS NULL

         SET @active_features = N''
		 declare @active_actions nvarchar(max)
      SET @active_actions = 
         (

            SELECT String_agg(cast(id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @active_features) <> 0

         )
 
      IF @active_actions IS NULL

         SET @active_actions = N''
		 declare @organization_active_features nvarchar(max)
      SET @organization_active_features = 
         (

            SELECT String_agg(CAST(organisationfeatures.featureId as nvarchar(max)),',')
            FROM [${dbxschemaname}].organisationfeatures
            WHERE 
               (organisationfeatures.featureStatus IS NULL OR organisationfeatures.featureStatus = 'SID_FEATURE_ACTIVE') AND 
               [${dbxschemaname}].FIND_IN_SET(organisationfeatures.featureId, @active_features) <> 0 AND 
               organisationfeatures.organisationId = @orgId

         )

      IF @organization_active_features IS NULL

         SET @organization_active_features = N''
       
	   declare @organization_active_actions nvarchar(max)
      SET @organization_active_actions = 
         (

            SELECT String_agg(cast(id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @organization_active_features) <> 0

         )

      IF @organization_active_actions IS NULL

         SET @organization_active_actions = N''
		 declare @business_customer_enabled_actions nvarchar(max)
      SET @business_customer_enabled_actions = 
         (

            SELECT String_agg(cast(Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeractionlimits
            WHERE 
               customeractionlimits.isAllowed = 1 AND 
               customeractionlimits.Customer_id = @_customerId AND 
               [${dbxschemaname}].FIND_IN_SET(customeractionlimits.Action_id, @organization_active_actions) <> 0
  
         )

      IF @business_customer_enabled_actions IS NULL

         SET @business_customer_enabled_actions = N''
		 declare @customer_disabled_actions nvarchar(max)
      SET @customer_disabled_actions = 
         (

            SELECT String_agg(cast(Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeractionlimits
            WHERE 
               customeractionlimits.isAllowed = 0 AND 
               customeractionlimits.Account_id IS NULL AND 
               customeractionlimits.Customer_id = @_customerId
  
         )

      IF @customer_disabled_actions IS NULL

         SET @customer_disabled_actions = N''
		 declare @customer_groups nvarchar(max)
      SET @customer_groups = 
         (

            SELECT String_agg(cast(Group_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Customer_id = @_customerId
 
         )

      IF @customer_groups IS NULL
 
         SET @customer_groups = N''
		 declare @group_actions nvarchar(max)
      SET @group_actions = 
         (

            SELECT String_agg(cast(Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].groupactionlimit
            WHERE [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Group_id, @customer_groups) <> 0

         )

      IF @group_actions IS NULL

         SET @group_actions = N''

 declare @active_feature_condition nvarchar(max)
 declare @select_statement nvarchar(max)
      IF @orgId = ''
	  
         SET @active_feature_condition = 'feature.Status_id =''SID_FEATURE_ACTIVE'' AND [${dbxschemaname}].FIND_IN_SET(featureaction.id,''' + (@group_actions) + (''') <> 0')
      
	  ELSE 
      SET @active_feature_condition = '[${dbxschemaname}].FIND_IN_SET(featureaction.id, ''' + (@organization_active_actions) + (''') <> 0 AND [${dbxschemaname}].FIND_IN_SET(featureaction.id, ''' + (@group_actions) + (''') <> 0'))
      SET @select_statement = 'SELECT customeractionlimits.Customer_id AS Customer_id,customeractionlimits.Account_id AS Account_id, case when customeractionlimits.isAllowed = ''1'' then ''true'' else ''false'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,customeractionlimits.RoleType_id AS RoleType_id,customeractionlimits.LimitType_id AS LimitType_id,customeractionlimits.value AS value FROM [${dbxschemaname}].customeractionlimits LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = customeractionlimits.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id) where customeractionlimits.Customer_id = ' + (@_customerId) + (' and ') + (@active_feature_condition)

      IF (@_actionId <> '')
 
         SET @select_statement = (@select_statement) + (N' and customeractionlimits.Action_id = ') + QUOTENAME(@_actionId, '''')

      SET @select_statement = 
         (@select_statement)
          + 
         ' UNION SELECT customergroup.Customer_id AS Customer_id,NULL AS Account_id, case when (membergroup.Type_id = ''TYPE_ID_SMALL_BUSINESS'' OR membergroup.Type_id = ''TYPE_ID_MICRO_BUSINESS'') then (case when [${dbxschemaname}].FIND_IN_SET(featureaction.id, '''
          + 
         (@business_customer_enabled_actions)
          + 
         (''') <> 0 then ''true'' else ''false'' end) else ''true'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,membergroup.Type_id AS RoleType_id,groupactionlimit.LimitType_id AS LimitType_id,groupactionlimit.value AS value FROM ([${dbxschemaname}].customergroup LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.Group_id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = groupactionlimit.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id)) where customergroup.Customer_id ='''
          + 
         (@_customerId)
          + 
         ''' and feature.Status_id = ''SID_FEATURE_ACTIVE'' and [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, ''')
          + 
         (@customer_disabled_actions)
          + 
         (''') = 0 and ')
          + 
         (@active_feature_condition)
  

      IF (@_actionId <> '')

         SET @select_statement = (@select_statement) + (N' and groupactionlimit.Action_id = ') + QUOTENAME(@_actionId, '''')
 
      exec(@select_statement)

   END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].[get_reports_proc]
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[get_reports_proc]    Script Date: 10/6/2020 12:41:56 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[get_reports_proc]
	@p_userId nvarchar(50),
	@p_roleId nvarchar(50)
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
		and sr.roleId=@p_roleId order by createdts desc;
 END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].[account_action_approvers_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc]  
   @_organizationId nvarchar(50),
   @_accountId nvarchar(50),
   @_approvalActionList nvarchar(50),
   @_featureId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      declare @group_concat_max_len bigint
      declare @customerIdList nvarchar(max)
      SET  NOCOUNT  ON

      SET @group_concat_max_len = 1000000
      SET @customerIdList = 
         (
            SELECT String_agg(CAST(customeraction.Customer_id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.isAllowed = 0 AND 
               [${dbxschemaname}].customeraction.Action_id = @_approvalActionList AND 
               [${dbxschemaname}].customeraction.Account_id = @_accountId
         )
		 
      SET @customerIdList = 
         CASE 
            WHEN (@customerIdList IS NULL) THEN ''
            ELSE @customerIdList
         END

      SELECT DISTINCT 
         ([${dbxschemaname}].customer.id) AS id, 
         ([${dbxschemaname}].customer.UserName) AS userName, 
         ([${dbxschemaname}].membergroup.Name) AS groupId, 
         ([${dbxschemaname}].customer.FirstName) AS firstName, 
         ([${dbxschemaname}].customer.LastName) AS lastName
      FROM ([${dbxschemaname}].customer 
         LEFT JOIN [${dbxschemaname}].organisation 
         ON ([${dbxschemaname}].organisation.id = [${dbxschemaname}].customer.Organization_Id) 
         LEFT JOIN [${dbxschemaname}].customergroup 
         ON ([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].membergroup 
         ON ([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].groupactionlimit 
         ON ([${dbxschemaname}].groupactionlimit.Group_id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].customeraccounts 
         ON ([${dbxschemaname}].customeraccounts.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].organisationfeatures 
         ON ([${dbxschemaname}].organisationfeatures.organisationId = [${dbxschemaname}].customer.Organization_Id))
      WHERE 
         [${dbxschemaname}].organisation.id = @_organizationId AND 
         [${dbxschemaname}].membergroup.Type_id = 'TYPE_ID_BUSINESS' AND
         [${dbxschemaname}].organisationfeatures.featureId = @_featureId AND 
         ([${dbxschemaname}].organisationfeatures.featureStatus IS NULL OR len([${dbxschemaname}].organisationfeatures.featureStatus) = 0) AND 
         [${dbxschemaname}].customeraccounts.Account_id = @_accountId AND 
         [${dbxschemaname}].customer.Status_id = 'SID_CUS_ACTIVE' AND 
         [${dbxschemaname}].customer.id NOT IN ( (@customerIdList) ) AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id, @_approvalActionList) > 0

   END
GO

GO

/****** Object:  View [${dbxschemaname}].[alerttype_view]    Script Date: 10/7/2020 12:35:14 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[alerttype_view];   
GO
CREATE VIEW [${dbxschemaname}].[alerttype_view] (
   [alerttype_id], 
   [alerttype_Name], 
   [alerttype_AlertCategoryId], 
   [alerttype_Status_id], 
   [alerttype_IsGlobal], 
   [alerttype_DisplaySequence], 
   [alerttype_freqId], 
   [alerttype_freqValue], 
   [alerttype_freqTime], 
   [alerttype_isAccountLevel],
   [Alerts_count], 
   [alerttype_softdeleteflag], 
   [alerttypetext_LanguageCode], 
   [alerttypetext_DisplayName], 
   [alerttypetext_Description], 
   [alerttypetext_createdby], 
   [alerttypetext_modifiedby], 
   [alerttypetext_createdts], 
   [alerttypetext_lastmodifiedts], 
   [alerttypetext_synctimestamp],
   [alerttypetext_softdeleteflag])AS
    SELECT 
        [${dbxschemaname}].dbxalerttype.id AS alerttype_id,
        [${dbxschemaname}].dbxalerttype.Name AS alerttype_Name,
        [${dbxschemaname}].dbxalerttype.AlertCategoryId AS alerttype_AlertCategoryId,
        [${dbxschemaname}].dbxalerttype.Status_id AS alerttype_Status_id,
        [${dbxschemaname}].dbxalerttype.IsGlobal AS alerttype_IsGlobal,
        [${dbxschemaname}].dbxalerttype.DisplaySequence AS alerttype_DisplaySequence,
		[${dbxschemaname}].dbxalerttype.defaultFrequencyId AS alerttype_freqId,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyValue AS alerttype_freqValue,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyTime AS alerttype_freqTime,
        [${dbxschemaname}].dbxalerttype.isAccountLevel AS alerttype_isAccountLevel,        
		(SELECT 
                COUNT(alertsubtype.id)
            FROM
                [${dbxschemaname}].alertsubtype
            WHERE
                (alertsubtype.AlertTypeId = dbxalerttype.id)) AS Alerts_count,
        [${dbxschemaname}].dbxalerttype.softdeleteflag AS alerttype_softdeleteflag,
        [${dbxschemaname}].dbxalerttypetext.LanguageCode AS alerttypetext_LanguageCode,
        [${dbxschemaname}].dbxalerttypetext.DisplayName AS alerttypetext_DisplayName,
        [${dbxschemaname}].dbxalerttypetext.Description AS alerttypetext_Description,
        [${dbxschemaname}].dbxalerttypetext.createdby AS alerttypetext_createdby,
        [${dbxschemaname}].dbxalerttypetext.modifiedby AS alerttypetext_modifiedby,
        [${dbxschemaname}].dbxalerttypetext.createdts AS alerttypetext_createdts,
        [${dbxschemaname}].dbxalerttypetext.lastmodifiedts AS alerttypetext_lastmodifiedts,
        [${dbxschemaname}].dbxalerttypetext.synctimestamp AS alerttypetext_synctimestamp,
        [${dbxschemaname}].dbxalerttypetext.softdeleteflag AS alerttypetext_softdeleteflag
    FROM
        ([${dbxschemaname}].dbxalerttypetext
        INNER JOIN [${dbxschemaname}].dbxalerttype ON (dbxalerttypetext.AlertTypeId = dbxalerttype.id));
GO


/****** Object:  View [${dbxschemaname}].[alertcategory_view]    Script Date: 10/7/2020 12:54:17 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[alertcategory_view]; 
GO

CREATE VIEW [${dbxschemaname}].[alertcategory_view] AS
    SELECT 
        [${dbxschemaname}].dbxalertcategory.id AS alertcategory_id,
        [${dbxschemaname}].dbxalertcategory.status_id AS alertcategory_status_id,
        [${dbxschemaname}].dbxalertcategory.accountLevel AS alertcategory_accountLevel,
        [${dbxschemaname}].dbxalertcategory.DisplaySequence AS alertcategory_DisplaySequence,
        [${dbxschemaname}].dbxalertcategory.softdeleteflag AS alertcategory_softdeleteflag,
        [${dbxschemaname}].dbxalertcategory.Name AS alertcategory_Name,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyId AS alertcategory_freqId,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyValue AS alertcategory_freqValue,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyTime AS alertcategory_freqTime,
        (SELECT 
                COUNT(dbxalerttype.id)
            FROM
                [${dbxschemaname}].dbxalerttype
            WHERE
                (dbxalerttype.AlertCategoryId = dbxalertcategory.id)) AS Groups_count,
        (SELECT 
                SUM([${dbxschemaname}].alerttype_view.Alerts_count)
            FROM
                [${dbxschemaname}].alerttype_view
            WHERE
                (alerttype_view.alerttype_AlertCategoryId = dbxalertcategory.id)) AS Alerts_count,
        [${dbxschemaname}].dbxalertcategorytext.LanguageCode AS alertcategorytext_LanguageCode,
        [${dbxschemaname}].dbxalertcategorytext.DisplayName AS alertcategorytext_DisplayName,
        [${dbxschemaname}].dbxalertcategorytext.Description AS alertcategorytext_Description,
        [${dbxschemaname}].dbxalertcategorytext.createdby AS alertcategorytext_createdby,
        [${dbxschemaname}].dbxalertcategorytext.modifiedby AS alertcategorytext_modifiedby,
        [${dbxschemaname}].dbxalertcategorytext.createdts AS alertcategorytext_createdts,
        [${dbxschemaname}].dbxalertcategorytext.lastmodifiedts AS alertcategorytext_lastmodifiedts,
        [${dbxschemaname}].dbxalertcategorytext.synctimestamp AS alertcategorytext_synctimestamp,
        [${dbxschemaname}].dbxalertcategorytext.softdeleteflag AS alertcategorytext_softdeleteflag
    FROM
        ([${dbxschemaname}].dbxalertcategorytext
        JOIN [${dbxschemaname}].dbxalertcategory ON (([${dbxschemaname}].dbxalertcategorytext.AlertCategoryId = [${dbxschemaname}].dbxalertcategory.id)));

GO


/****** Object:  View [${dbxschemaname}].[alertsubtypetext_view]    Script Date: 10/7/2020 1:29:09 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[alertsubtypetext_view] AS select
    [${dbxschemaname}].alertsubtype.id AS alertsubtype_id,
    [${dbxschemaname}].alertsubtype.AlertTypeId AS alertsubtype_alertTypeId,
    [${dbxschemaname}].alertsubtype.Name AS alertsubtype_Name,
    [${dbxschemaname}].alertsubtype.Status_id AS alertsubtype_StatusId,
    [${dbxschemaname}].alertsubtype.isAccountLevel AS alertsubtype_isAccountLevel,
    [${dbxschemaname}].alertsubtype.attributeId AS alertsubtype_attributeId,
    [${dbxschemaname}].alertsubtype.alertConditionId AS alertsubtype_alertConditionId,
    [${dbxschemaname}].alertsubtype.value1 AS alertsubtype_value1,
    [${dbxschemaname}].alertsubtype.value2 AS alertsubtype_value2,
    [${dbxschemaname}].alertsubtype.isGlobal AS alertsubtype_isGlobal,
    [${dbxschemaname}].alertsubtype.defaultFrequencyId AS alertsubtype_defaultFrequencyId,
    [${dbxschemaname}].alertsubtype.defaultFrequencyValue AS alertsubtype_defaultFrequencyValue,
    [${dbxschemaname}].alertsubtype.defaultFrequencyTime AS alertsubtype_defaultFrequencyTime,
    [${dbxschemaname}].alertsubtype.createdby AS alertsubtype_createdby,
    [${dbxschemaname}].alertsubtype.modifiedby AS alertsubtype_modifiedby,
    [${dbxschemaname}].alertsubtype.createdts AS alertsubtype_createdts,
    [${dbxschemaname}].alertsubtype.lastmodifiedts AS alertsubtype_lastmodifiedts,
    [${dbxschemaname}].alertsubtype.synctimestamp AS alertsubtype_synctimestamp,
    [${dbxschemaname}].alertsubtype.softdeleteflag AS alertsubtype_softdeleteflag,
    [${dbxschemaname}].alertsubtypetext.languageCode AS alertsubtypetext_languageCode,
    [${dbxschemaname}].alertsubtypetext.description AS alertsubtypetext_description,
    [${dbxschemaname}].alertsubtypetext.displayName AS alertsubtypetext_displayName
from
    ([${dbxschemaname}].[alertsubtype]
join [${dbxschemaname}].[alertsubtypetext] on
    ((alertsubtypetext.alertSubTypeId = alertsubtype.id)));

GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_request_history_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_request_history_proc]  
   @_customerId nvarchar(50),
   @_requestId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @companyId nvarchar(max)

      SET @companyId = ( SELECT [customer].[Organization_Id] FROM [${dbxschemaname}].[customer] WHERE [customer].[id] = @_customerId)
      SELECT 
         [bbactedrequest].[approvalId] AS approvalId, 
         [bbactedrequest].[requestId] AS requestId, 
         [bbactedrequest].[companyId] AS companyId, 
         [bbactedrequest].[createdby] AS requestActedby, 
         [bbactedrequest].[status] AS status, 
         [bbactedrequest].[comments] AS comments, 
         [bbactedrequest].[action] AS action, 
         [bbactedrequest].[createdts] AS actionts, 
         [bbactedrequest].[softdeleteflag] AS softdeleteflag, 
         [customer].[UserName] AS userName, 
         CASE 
            WHEN [${dbxschemaname}].[bbactedrequest].[createdby] IS NULL THEN 'System'
            ELSE (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[FirstName]) <> 0) THEN [${dbxschemaname}].[customer].[FirstName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[MiddleName]) <> 0) THEN [${dbxschemaname}].[customer].[MiddleName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[LastName]) <> 0) THEN [${dbxschemaname}].[customer].[LastName]
               ELSE ''
            END)
         END AS customerName, 
         [customer].[FullName] AS customerFullName
      FROM ([${dbxschemaname}].[bbactedrequest] 
         LEFT JOIN [${dbxschemaname}].[customer] 
         ON ([bbactedrequest].[createdby] = [customer].[id]))
      WHERE 
         CAST([bbactedrequest].[requestId] as nvarchar(max))= @_requestId AND 
         [bbactedrequest].[companyId] = @companyId
   END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]
GO  

CREATE PROCEDURE [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]  
   @_requestId nvarchar(50),
   @_companyId nvarchar(50),
   @_featureactionlist nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @group_concat_max_len bigint
      SET @group_concat_max_len = 100000000
      DECLARE @features nvarchar(max)
      DECLARE @createActions nvarchar(max)

      SET @features = 
         (
            SELECT string_agg(CAST(featureaction.Feature_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) > 0
         )

      IF @features IS NULL
         SET @features = ''
		 SET @createActions = 
         (
            SELECT string_agg(CAST(featureaction.id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) > 0 AND [${dbxschemaname}].featureaction.Type_id = 'MONETARY'
         )
         
      IF @createActions IS NULL
         SET @createActions = ''
      SELECT 
         [${dbxschemaname}].bbrequest.requestId, 
         [${dbxschemaname}].bbrequest.transactionId, 
         [${dbxschemaname}].bbrequest.featureActionId, 
         [${dbxschemaname}].bbrequest.createdby, 
         [${dbxschemaname}].bbrequest.companyId, 
         [${dbxschemaname}].bbrequest.requiredSets, 
         [${dbxschemaname}].bbrequest.receivedSets, 
         [${dbxschemaname}].bbrequest.createdts, 
         [${dbxschemaname}].bbrequest.status, 
         [${dbxschemaname}].bbrequest.softDelete, 
         [${dbxschemaname}].bbrequest.accountId
      FROM [${dbxschemaname}].bbrequest
      WHERE 
         [${dbxschemaname}].bbrequest.requestId = CAST(@_requestId AS float(53)) AND 
         [${dbxschemaname}].bbrequest.companyId = @_companyId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
   END
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvers_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvers_proc]
 @_requestId nvarchar(50) 
AS

BEGIN

	SELECT [customerapprovalmatrix].customerId AS approver 
    FROM [${dbxschemaname}].[customerapprovalmatrix]  WHERE 
    [customerapprovalmatrix].approvalMatrixId 
    IN (
		SELECT [requestapprovalmatrix].approvalMatrixId 
        FROM [${dbxschemaname}].[requestapprovalmatrix] 
		WHERE [requestapprovalmatrix].requestId = @_requestId
	)
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalrequest_counts_proc]
GO 
CREATE PROCEDURE [${dbxschemaname}].[approvalrequest_counts_proc]  
   @_customerId nvarchar(50),
   @_approveActionList nvarchar(max),
   @_createActionList nvarchar(max)
AS 

   BEGIN

    MAINLABEL: 
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	   DECLARE @approvalRequestIds nvarchar(max)
      DECLARE @companyId nvarchar(max)
      DECLARE @features nvarchar(max)
      DECLARE @createApproveActions nvarchar(max)
      DECLARE @customerMatrixIds nvarchar(max)
      DECLARE @select_statement nvarchar(max)
      

      SELECT 0 AS count, 'ACHFilesForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'ACHTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'GeneralTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'GeneralTransactionsForMyApproval' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsWaiting' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsRejected' AS TransactionType
       UNION
      SELECT 0 AS count, 'myRequestsApproved' AS TransactionType

      SET @companyId = 
         (
            SELECT [${dbxschemaname}].customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE [${dbxschemaname}].customer.id = @_customerId
         )
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_approveActionList IS NULL
         SET @_approveActionList = ''
      IF @_createActionList IS NULL
         SET @_createActionList = ''
      SET @features = 
         (
            SELECT STRING_AGG(CAST(featureaction.Feature_id  as nvarchar(max)) , ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_approveActionList) > 0
         )
      IF @features IS NULL
         SET @features = ''
      SET @createApproveActions = 
         (
            SELECT STRING_AGG(CAST(featureaction.id   as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) > 0 AND ([${dbxschemaname}].featureaction.id LIKE '%_CREATE' OR [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
         )
      IF @createApproveActions IS NULL
         SET @createApproveActions = ''
		 
      SET @customerMatrixIds = 
         (
            SELECT STRING_AGG(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE [${dbxschemaname}].customerapprovalmatrix.customerId = @_customerId
         )

      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
         
        declare @alreadyApprovedIds nvarchar(max)
      SET @alreadyApprovedIds = 
         (
            SELECT STRING_AGG(CAST(bbactedrequest.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].bbactedrequest
            WHERE [${dbxschemaname}].bbactedrequest.createdby = @_customerId AND [${dbxschemaname}].bbactedrequest.action = 'Approved'
         )
		 
      IF @alreadyApprovedIds IS NULL
         SET @alreadyApprovedIds = ''		
      SET @approvalRequestIds = 
         (
            SELECT STRING_AGG(CAST(requestapprovalmatrix.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].requestapprovalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)), @customerMatrixIds) > 0 AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
         )
         
        IF @approvalRequestIds IS NULL
         SET @approvalRequestIds = ''
         
      SET @select_statement = 
         ('select count(tablea.requestCountA) as count, tablea.TransactionType 
		 from (
         	select DISTINCT([${dbxschemaname}].bbrequest.requestId) as requestCountA, 
         		iif([${dbxschemaname}].bbrequest.featureActionId LIKE ''ACH_FILE%'',
					''ACHFilesForMyApproval'',
         			iif([${dbxschemaname}].bbrequest.featureActionId LIKE ''ACH%'',
						''ACHTransactionsForMyApproval'',
						''GeneralTransactionsForMyApproval''
						)
				) as TransactionType,
         	    [${dbxschemaname}].bbrequest.createdby,
         	    [${dbxschemaname}].bbrequest.companyId,
         	    [${dbxschemaname}].bbrequest.status
         	    FROM (
         	    	[${dbxschemaname}].bbrequest LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON 
         	    	[${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId
         	    )
         	    WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId as nvarchar(max)),''' + (@approvalRequestIds) + ''')>0 AND [${dbxschemaname}].bbrequest.companyId = ' + (@companyId) + 
         	    ' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId,''' + (@createApproveActions) + ''')>0' + 
         	    ' AND [${dbxschemaname}].bbrequest.status = ''Pending'') as tablea GROUP BY TransactionType')
      SET @select_statement = (@select_statement) + ' UNION select count(tableb.requestCountB) as count, 
      	tableb.TransactionType from (
      		select  
      			DISTINCT([${dbxschemaname}].bbrequest.requestId) as requestCountB,
      			iif([${dbxschemaname}].bbrequest.status = ''Pending'', ''myRequestsWaiting'', 
      				iif([${dbxschemaname}].bbrequest.status = ''Rejected'', ''myRequestsRejected'', 
      					iif([${dbxschemaname}].bbrequest.status = ''Approved'', ''myRequestsApproved'', ''myRequestsWithdrawn''))) as TransactionType,
      			[${dbxschemaname}].bbrequest.createdby,
      			[${dbxschemaname}].bbrequest.companyId,
      			[${dbxschemaname}].bbrequest.status
      			FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.companyId = ''' + (@companyId) + '''' + 
         		' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId,''' + (@_createActionList) + 
         		''')>0 AND [${dbxschemaname}].bbrequest.createdby = ' + (QUOTENAME(@_customerId, '''')) + ') as tableb group BY TransactionType'
      IF @select_statement IS NULL
         GOTO MAINLABEL$leave
		exec(@select_statement)
	  End
   MAINLABEL$leave:
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc]  
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc]  
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(50),
   @_requestIds nvarchar(max),
   @_featureactionlist nvarchar(50)
AS 
   BEGIN
     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
	 DECLARE @companyId nvarchar(max)
	 DECLARE @customerMatrixIds nvarchar(max)
	 DECLARE @approvalRequestIds nvarchar(max)
	 DECLARE @features nvarchar(max)
	 DECLARE @monetaryActions nvarchar(max)

     
     SET @combinedIds = (select String_agg(customer.id, ',') from [${dbxschemaname}].customer where [${dbxschemaname}].customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
	IF (@combinedIds IS NULL OR @combinedIds = '')
		SET @combinedIds = ''''
	ELSE
		SET @combinedIds = @combinedIds

	IF (@_transactionIds IS NULL OR @_transactionIds = '')
		SET @_transactionIds = ''''
	ELSE
		SET @_transactionIds = @_transactionIds
	
	IF (@_requestIds IS NULL OR @_requestIds = '')
		SET @_requestIds = ''''
	ELSE
		SET @_requestIds = @_requestIds

      SET @companyId = ( SELECT customer.Organization_Id FROM [${dbxschemaname}].customer WHERE customer.id = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') <> 1 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''''
      END

      SET @approvalRequestIds = 
         (  
         SELECT String_agg(CAST(requestapprovalmatrix.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

     
      SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.Type_id = 'MONETARY')
      IF @monetaryActions IS NULL
        BEGIN
          SET @monetaryActions = ''
        END

	DECLARE @companyRequestIds nvarchar(50)
      SET @companyRequestIds = ( SELECT String_agg(bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.companyId = @companyId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @monetaryActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END

	DECLARE @requestIds nvarchar(50)
	IF @_requestIds = ''''
          SET @requestIds = @companyRequestIds
    ELSE
		SET @requestIds = @_requestIds

	DECLARE @query nvarchar(50)
	IF @_transactionIds = '''' 
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.requestId,',@_requestIds) 
	ELSE
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.transactionId,',@_transactionIds,'AND [${dbxschemaname}].FIND_IN_SET(bbrequest.featureActionId,',@monetaryActions);

   DECLARE @select_statement nvarchar(50)
	 SET @select_statement = CONCAT(
			'SELECT 
             bbrequest.requestId,
             bbrequest.transactionId,
             bbrequest.status,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
            END) as amICreator,
         
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = Approved AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.requestId'
				);
    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
    exec(@select_statement)
	End
    MAINLABEL$leave: 
  
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[auto_reject_invalid_pending_requests_proc] 
GO

CREATE PROCEDURE [${dbxschemaname}].[auto_reject_invalid_pending_requests_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      declare @oldMatrixIds nvarchar(max)
	  declare @newMatrixIds nvarchar(max)
	  declare @invalidMatrixIds nvarchar(max)
	  declare @invalidRequestIds nvarchar(max)
	  declare @companyId nvarchar(max)
	  declare @numOfParams int
	  declare @idx int
	  declare @select_statement nvarchar(max)
	  declare @SQL_SAFE_UPDATES nvarchar(max)
	  declare @requestId nvarchar(max)

      SET @oldMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE customerapprovalmatrix.customerId = @_customerId)

      IF (@oldMatrixIds IS NULL)
         GOTO MAINLABEL$leave

      SET @newMatrixIds = 
         (
            SELECT STRING_AGG(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',')
            FROM 
               [${dbxschemaname}].customerapprovalmatrix 
                  LEFT JOIN [${dbxschemaname}].approvalmatrix 
                  ON ([${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id) 
                  INNER JOIN 
                  (SELECT DISTINCT [${dbxschemaname}].customeraction.Account_id,
                  [${dbxschemaname}].featureaction.Feature_id AS FeatureId,
				  map.monetaryAction as Action_id
				  FROM
				[${dbxschemaname}].customeraction
				INNER JOIN featureaction ON ([${dbxschemaname}].customeraction.Action_id = [${dbxschemaname}].featureaction.id )
				INNER JOIN (SELECT a.id as monetaryAction, b.id as approveAction
            FROM
            [${dbxschemaname}].featureaction as a
            JOIN featureaction as b ON (a.Feature_id = b.Feature_id)
            WHERE a.Type_id = 'MONETARY') as map ON (customeraction.Action_id = map.approveAction)             
                WHERE
                [${dbxschemaname}].customeraction.Customer_id = @_customerId AND
                [${dbxschemaname}].customeraction.isAllowed = 1 AND
                [${dbxschemaname}].customeraction.Account_id IS NOT NULL AND
                [${dbxschemaname}].customeraction.Action_id LIKE '%_APPROVE'
                  )  AS can 
                  ON (([${dbxschemaname}].approvalmatrix.actionId = can.Action_id) AND ([${dbxschemaname}].approvalmatrix.accountId = can.Account_id))
            WHERE [${dbxschemaname}].customerapprovalmatrix.customerId = @_customerId
         )

      IF (@newMatrixIds IS NULL)

         SET @newMatrixIds = ''

	  SET @invalidMatrixIds = 
         (
            SELECT STRING_AGG(CAST(id as nvarchar(max)),',')
            FROM [${dbxschemaname}].approvalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].approvalmatrix.id AS nvarchar(max)), @oldMatrixIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].approvalmatrix.id AS nvarchar(max)), @oldMatrixIds) = 0
         )

      IF (@invalidMatrixIds IS NULL)
         GOTO MAINLABEL$leave

      SET @invalidRequestIds = (
			SELECT STRING_AGG(CAST(bbrequest.requestId as nvarchar(max)),',')
            FROM
               [${dbxschemaname}].bbrequest
                  LEFT JOIN [${dbxschemaname}].requestapprovalmatrix
                  ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
                  INNER JOIN
                  (
                    ( SELECT [${dbxschemaname}].approvalmatrix.id AS approvalMatrixId, [${dbxschemaname}].approvalrule.numberOfApprovals AS numberOfApprovalsRequired,
                        numberOfApprovers = (
                           SELECT count_big(DISTINCT ([${dbxschemaname}].customerapprovalmatrix.customerId))
                           FROM [${dbxschemaname}].customerapprovalmatrix
                           WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
                        )
                     FROM
                        [${dbxschemaname}].approvalmatrix
                           LEFT JOIN [${dbxschemaname}].approvalrule
                           ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id))
                  ) AS temp_request_table
                  ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = temp_request_table.approvalMatrixId)
            WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)), @invalidMatrixIds) <> 0 AND (temp_request_table.numberOfApprovalsRequired = -1 OR temp_request_table.numberOfApprovalsRequired >= temp_request_table.numberOfApprovers)
			)

      SET @SQL_SAFE_UPDATES = 0

      SET @select_statement = ('DELETE FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId in (' + (@invalidMatrixIds) + ') AND [${dbxschemaname}].customerapprovalmatrix.customerId = ''') + (@_customerId) + ('''')

      SET @select_statement = ('UPDATE [${dbxschemaname}].approvalmatrix SET invalid = ''1'' WHERE [${dbxschemaname}].approvalmatrix.id in  (') + (@invalidMatrixIds) + (')')

      IF (@invalidRequestIds IS NULL)
         BEGIN

            SET @SQL_SAFE_UPDATES = 1

            GOTO MAINLABEL$leave

         END

      SET @select_statement = ('UPDATE [${dbxschemaname}].bbrequest SET status = ''Rejected'' WHERE [${dbxschemaname}].bbrequest.requestId in  (') + (@invalidRequestIds) + (')')

	  SET @select_statement = ('UPDATE [${dbxschemaname}].billpaytransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].billpaytransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].billpaytransfers.status =  ''Rejected'' [${dbxschemaname}].WHERE billpaytransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].ownaccounttransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].ownaccounttransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].ownaccounttransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].ownaccounttransfers.requestId in (') + (@invalidRequestIds) + (')')

	  SET @select_statement = ('UPDATE [${dbxschemaname}].interbankfundtransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].interbankfundtransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].interbankfundtransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].interbankfundtransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].intrabanktransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].intrabanktransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].intrabanktransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].intrabanktransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].p2ptransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].p2ptransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].p2ptransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].p2ptransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].wiretransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].wiretransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].wiretransfers.status =  ''Rejected'' WHERE [${dbxschemaname}].wiretransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].internationalfundtransfers INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].internationalfundtransfers.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].internationalfundtransfers.status = ''Rejected'' WHERE [${dbxschemaname}].internationalfundtransfers.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].achtransaction INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].achtransaction.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].achtransaction.status =  ''Rejected'' WHERE [${dbxschemaname}].achtransaction.requestId in (') + (@invalidRequestIds) + (')')

      SET @select_statement = ('UPDATE [${dbxschemaname}].achfile INNER JOIN [${dbxschemaname}].bbrequest ON [${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId SET [${dbxschemaname}].achfile.status = ''Rejected'' WHERE [${dbxschemaname}].achfile.requestId in (') + (@invalidRequestIds) + (')')

      SET @companyId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE [${dbxschemaname}].customer.id = @_customerId
         )
     
	 IF (@companyId IS NULL)
         GOTO MAINLABEL$leave
    
	 SET @numOfParams = 0
     
	 IF (datalength(@invalidRequestIds) > 0)
         SET @numOfParams = datalength(@invalidRequestIds) - datalength(replace(@invalidRequestIds, ',', '')) + 1
         
		 SET @select_statement = ''

		 SET @idx = 1
		 WHILE (1 = 1)
      
         BEGIN

            IF (@idx > @numOfParams)
               BREAK
			 
			/* SET @requestId = substring(@invalidRequestIds, , @idx) */
			SET @requestId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@invalidRequestIds, ',', @idx),',', -1 );

			/* SET @requestId = m2ss.substring_index(m2ss.substring_index([@invalidRequestIds], N',', [@idx]), N',', -1) */
			
            SET @select_statement = ('
               INSERT INTO [${dbxschemaname}].bbactedrequest(
                  [${dbxschemaname}].bbactedrequest.requestId, 
                  [${dbxschemaname}].bbactedrequest.companyId, 
                  [${dbxschemaname}].bbactedrequest.comments, 
                  [${dbxschemaname}].bbactedrequest.status, 
                  [${dbxschemaname}].bbactedrequest.action
               ) VALUES (') + (@requestId) + (',') + (@companyId) + (''' , ''Rejected by system as one of the approver lost his permission'', ''Rejected'', ''Rejected'');')
           
			SET @idx = @idx + 1

         END

      SET @SQL_SAFE_UPDATES = 1

   END
   MAINLABEL$leave:
GO

ALTER TABLE [${dbxschemaname}].[credentialchecker] ADD [retryCount] VARCHAR(50) NULL
GO
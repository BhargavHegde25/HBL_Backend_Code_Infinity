USE [${logdbname}]
go
IF NOT EXISTS ( SELECT  * FROM    sys.schemas WHERE   name = N'${logschemaname}' )
    EXEC('CREATE SCHEMA [${logschemaname}]');
GO
/****** Object:  Table [${logschemaname}].[adminactivity]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[adminactivity](
	[id] [nvarchar](50) NOT NULL,
	[event] [nvarchar](50) NULL,
	[description] [nvarchar](1000) NULL,
	[username] [nvarchar](50) NULL,
	[userRole] [nvarchar](50) NULL,
	[moduleName] [nvarchar](50) NULL,
	[eventts] [datetime] NOT NULL,
	[status] [nvarchar](50) NULL,
	[createdBy] [nvarchar](50) NULL,
	[createdOn] [datetime] NOT NULL,
 CONSTRAINT [PK_adminactivity_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[admincustomeractivity]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[admincustomeractivity](
	[id] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NULL,
	[adminName] [nvarchar](255) NULL,
	[adminRole] [nvarchar](50) NULL,
	[activityType] [nvarchar](50) NULL,
	[description] [nvarchar](1000) NULL,
	[eventts] [datetime] NOT NULL,
	[status] [nvarchar](50) NULL,
	[createdBy] [nvarchar](50) NULL,
	[createdOn] [datetime] NOT NULL,
 CONSTRAINT [PK_admincustomeractivity_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[auditactivity]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[auditactivity](
	[Id] [nvarchar](255) NOT NULL,
	[EventId] [nvarchar](45) NULL,
	[EventType] [nvarchar](255) NULL,
	[EventSubType] [nvarchar](255) NULL,
	[Status_Id] [nvarchar](255) NULL,
	[sessionId] [nvarchar](255) NULL,
	[AppId] [nvarchar](255) NULL,
	[UserName] [nvarchar](255) NULL,
	[Customer_Id] [nvarchar](255) NULL,
	[partyid] [nvarchar](255) NULL,
	[corecustomerid] [nvarchar](255) NULL,
	[isCSRAssist] [smallint] NOT NULL,
	[appSessionId] [nvarchar](255) NULL,
	[payeeNickName] [nvarchar](255) NULL,
	[relationshipNumber] [nvarchar](255) NULL,
	[AdminUserName] [nvarchar](255) NULL,
	[AdminUserRole] [nvarchar](255) NULL,
	[Producer] [nvarchar](255) NULL,
	[MoneyMovementRefId] [nvarchar](255) NULL,
	[EventData] [nvarchar](max) NULL,
	[creditcardnumber] [nvarchar](255) NULL,
	[mfa_State] [nvarchar](255) NULL,
	[mfa_ServiceKey] [nvarchar](255) NULL,
	[mfa_Type] [nvarchar](255) NULL,
	[nonSearchable] [nvarchar](max) NULL,
	[phoneNumber] [nvarchar](255) NULL,
	[email] [nvarchar](255) NULL,
	[deviceModel] [nvarchar](255) NULL,
	[operatingSystem] [nvarchar](255) NULL,
	[browser] [nvarchar](255) NULL,
	[deviceId] [nvarchar](255) NULL,
	[channel] [nvarchar](255) NULL,
	[appVersion] [nvarchar](255) NULL,
	[platform] [nvarchar](255) NULL,
	[ipAddress] [nvarchar](255) NULL,
	[eventts] [datetime] NULL,
	[createdby] [nvarchar](255) NULL,
	[createdts] [datetime] NULL,
	[softdeleteflag] [smallint] NULL,
 CONSTRAINT [PK_auditactivity_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[billcategory]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[billcategory](
	[billcategId] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[billcategId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[customeractivity]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[customeractivity](
	[id] [nvarchar](50) NOT NULL,
	[sessionId] [nvarchar](100) NULL,
	[username] [nvarchar](50) NULL,
	[moduleName] [nvarchar](100) NULL,
	[activityType] [nvarchar](50) NULL,
	[description] [nvarchar](1000) NULL,
	[eventts] [datetime] NOT NULL,
	[status] [nvarchar](50) NULL,
	[channel] [nvarchar](50) NULL,
	[ipAddress] [nvarchar](50) NULL,
	[device] [nvarchar](50) NULL,
	[deviceId] [nvarchar](100) NULL,
	[operatingSystem] [nvarchar](50) NULL,
	[browser] [nvarchar](50) NULL,
	[referenceId] [nvarchar](50) NULL,
	[errorCode] [nvarchar](50) NULL,
	[createdBy] [nvarchar](50) NULL,
	[createdOn] [datetime] NOT NULL,
	[customerId] [nvarchar](50) NULL,
	[typeOfMFA] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[accountNumber] [nvarchar](50) NULL,
	[relationshipNumber] [nvarchar](50) NULL,
	[phoneNumber] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[bankName] [nvarchar](50) NULL,
	[maskedAccountNumber] [nvarchar](50) NULL,
 CONSTRAINT [PK_customeractivity_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${logschemaname}].[freq1](
	[freqId] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[freqId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[moneymovementlog]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[moneymovementlog](
	[Id] [varchar](50) NOT NULL,
	[isScheduled] [bit] NOT NULL,
	[Customer_id] [varchar](50) NULL,
	[ExpenseCategory_id] [int] NULL,
	[Payee_id] [int] NULL,
	[Bill_id] [int] NULL,
	[Type_id] [int] NULL,
	[Reference_id] [varchar](50) NULL,
	[fromAccountNumber] [varchar](50) NULL,
	[fromAccountBalance] [decimal](10, 2) NULL,
	[toAccountNumber] [varchar](50) NULL,
	[toAccountBalance] [decimal](10, 2) NULL,
	[amount] [decimal](20, 2) NULL,
	[convertedAmount] [decimal](20, 2) NULL,
	[transactionCurrency] [varchar](45) NULL,
	[baseCurrency] [varchar](45) NULL,
	[Status_id] [varchar](50) NULL,
	[statusDesc] [varchar](50) NULL,
	[notes] [varchar](150) NULL,
	[checkNumber] [int] NULL,
	[imageURL1] [text] NULL,
	[imageURL2] [text] NULL,
	[hasDepositImage] [tinyint] NULL,
	[description] [varchar](100) NULL,
	[scheduledDate] [datetime] NULL,
	[transactionDate] [datetime] NULL,
	[createdDate] [datetime] NULL,
	[transactionComments] [varchar](100) NULL,
	[toExternalAccountNumber] [varchar](45) NULL,
	[Person_Id] [int] NULL,
	[frequencyType] [varchar](20) NULL,
	[numberOfRecurrences] [int] NULL,
	[frequencyStartDate] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[checkImage] [nvarchar](1) NULL,
	[checkImageBack] [nvarchar](1) NULL,
	[cashlessOTPValidDate] [datetime] NULL,
	[cashlessOTP] [varchar](50) NULL,
	[cashlessPhone] [varchar](50) NULL,
	[cashlessEmail] [varchar](50) NULL,
	[cashlessPersonName] [varchar](50) NULL,
	[cashlessMode] [varchar](50) NULL,
	[cashlessSecurityCode] [varchar](50) NULL,
	[cashWithdrawalTransactionStatus] [varchar](50) NULL,
	[cashlessPin] [varchar](50) NULL,
	[category] [varchar](30) NULL,
	[billCategory] [varchar](20) NULL,
	[recurrenceDesc] [varchar](50) NULL,
	[deliverBy] [varchar](50) NULL,
	[p2pContact] [varchar](50) NULL,
	[p2pRequiredDate] [datetime] NULL,
	[requestCreatedDate] [varchar](50) NULL,
	[penaltyFlag] [bit] NULL,
	[payoffFlag] [bit] NULL,
	[viewReportLink] [varchar](150) NULL,
	[isPaypersonDeleted] [bit] NULL,
	[fee] [varchar](50) NULL,
	[feeCurrency] [varchar](45) NULL,
	[feePaidByReceipent] [bit] NULL,
	[frontImage1] [varchar](100) NULL,
	[frontImage2] [varchar](100) NULL,
	[backImage1] [varchar](100) NULL,
	[backImage2] [varchar](100) NULL,
	[checkDesc] [varchar](50) NULL,
	[checkNumber1] [varchar](50) NULL,
	[checkNumber2] [varchar](50) NULL,
	[bankName1] [varchar](50) NULL,
	[bankName2] [varchar](50) NULL,
	[withdrawlAmount1] [varchar](50) NULL,
	[withdrawlAmount2] [varchar](50) NULL,
	[cashAmount] [varchar](50) NULL,
	[payeeCurrency] [varchar](50) NULL,
	[billid] [bigint] NULL,
	[isDisputed] [bit] NULL,
	[disputeDescription] [varchar](50) NULL,
	[disputeReason] [varchar](50) NULL,
	[disputeStatus] [varchar](50) NULL,
	[disputeDate] [datetime] NULL,
	[payeeName] [varchar](50) NULL,
	[checkDateOfIssue] [datetime] NULL,
	[checkReason] [varchar](50) NULL,
	[isPayeeDeleted] [bit] NULL,
	[amountRecieved] [varchar](50) NULL,
	[requestValidity] [datetime] NULL,
	[statementReference] [varchar](35) NULL,
	[transCreditDebitIndicator] [varchar](45) NULL,
	[bookingDateTime] [datetime] NULL,
	[valueDateTime] [datetime] NULL,
	[transactionInformation] [varchar](500) NULL,
	[addressLine] [varchar](70) NULL,
	[transactionAmount] [varchar](50) NULL,
	[chargeAmount] [varchar](50) NULL,
	[chargeCurrency] [varchar](50) NULL,
	[sourceCurrency] [varchar](45) NULL,
	[targetCurrency] [varchar](50) NULL,
	[unitCurrency] [varchar](50) NULL,
	[exchangeRate] [varchar](45) NULL,
	[contractIdentification] [varchar](35) NULL,
	[quotationDate] [datetime] NULL,
	[instructedAmount] [varchar](45) NULL,
	[instructedCurrency] [varchar](45) NULL,
	[transactionCode] [varchar](35) NULL,
	[transactionSubCode] [varchar](45) NULL,
	[proprietaryTransactionCode] [varchar](35) NULL,
	[proprietaryTransactionIssuer] [varchar](35) NULL,
	[balanceCreditDebitIndicator] [varchar](45) NULL,
	[balanceType] [varchar](45) NULL,
	[balanceAmount] [varchar](45) NULL,
	[balanceCurrency] [varchar](45) NULL,
	[merchantName] [varchar](45) NULL,
	[merchantCategoryCode] [varchar](45) NULL,
	[creditorAgentSchemeName] [varchar](45) NULL,
	[creditorAgentIdentification] [varchar](45) NULL,
	[creditorAgentName] [varchar](140) NULL,
	[creditorAgentaddressType] [varchar](45) NULL,
	[creditorAgentDepartment] [varchar](45) NULL,
	[creditorAgentSubDepartment] [varchar](45) NULL,
	[creditorAgentStreetName] [varchar](45) NULL,
	[creditorAgentBuildingNumber] [varchar](45) NULL,
	[creditorAgentPostCode] [varchar](45) NULL,
	[creditorAgentTownName] [varchar](45) NULL,
	[creditorAgentCountrySubDivision] [varchar](45) NULL,
	[creditorAgentCountry] [varchar](45) NULL,
	[creditorAgentAddressLine] [varchar](45) NULL,
	[creditorAccountSchemeName] [varchar](45) NULL,
	[creditorAccountIdentification] [varchar](45) NULL,
	[creditorAccountName] [varchar](45) NULL,
	[creditorAccountSeconIdentification] [varchar](45) NULL,
	[debtorAgentSchemeName] [varchar](45) NULL,
	[debtorAgentIdentification] [varchar](45) NULL,
	[debtorAgentName] [varchar](45) NULL,
	[debtorAgentAddressType] [varchar](45) NULL,
	[debtorAgentDepartment] [varchar](45) NULL,
	[debtorAgentSubDepartment] [varchar](45) NULL,
	[debtorAgentStreetName] [varchar](45) NULL,
	[debtorAgentBuildingNumber] [varchar](45) NULL,
	[dedtorAgentPostCode] [varchar](45) NULL,
	[debtorAgentTownName] [varchar](45) NULL,
	[debtorAgentCountrySubDivision] [varchar](45) NULL,
	[debtorAgentCountry] [varchar](45) NULL,
	[debtorAgentAddressLine] [varchar](45) NULL,
	[debtorAccountSchemeName] [varchar](45) NULL,
	[debtorAccountIdentification] [varchar](45) NULL,
	[debtorAccountName] [varchar](45) NULL,
	[debtorAccountSeconIdentification] [varchar](45) NULL,
	[cardInstrumentSchemeName] [varchar](45) NULL,
	[cardInstrumentAuthorisationType] [varchar](45) NULL,
	[cardInstrumentName] [varchar](45) NULL,
	[cardInstrumentIdentification] [varchar](45) NULL,
	[IBAN] [varchar](45) NULL,
	[sortCode] [varchar](45) NULL,
	[FirstPaymentDateTime] [datetime] NULL,
	[NextPaymentDateTime] [datetime] NULL,
	[FinalPaymentDateTime] [datetime] NULL,
	[StandingOrderStatusCode] [varchar](6) NULL,
	[FP_Amount] [decimal](12, 2) NULL,
	[FP_Currency] [varchar](45) NULL,
	[NP_Amount] [decimal](12, 2) NULL,
	[NP_Currency] [varchar](45) NULL,
	[FPA_Amount] [decimal](12, 2) NULL,
	[FPA_Currency] [varchar](45) NULL,
	[ConsentId] [varchar](45) NULL,
	[Initiation_InstructionIdentification] [varchar](45) NULL,
	[Initiation_EndToEndIdentification] [varchar](45) NULL,
	[RI_Reference] [varchar](45) NULL,
	[RI_Unstructured] [varchar](45) NULL,
	[RiskPaymentContextCode] [varchar](45) NULL,
	[MerchantCustomerIdentification] [varchar](200) NULL,
	[beneficiaryName] [varchar](50) NULL,
	[bankName] [varchar](50) NULL,
	[swiftCode] [varchar](45) NULL,
	[DomesticPaymentId] [varchar](45) NULL,
	[linkSelf] [varchar](45) NULL,
	[StatusUpdateDateTime] [datetime] NULL,
	[dataStatus] [varchar](45) NULL,
	[serviceName] [varchar](50) NULL,
	[payPersonName] [varchar](45) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[tcategories]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[tcategories](
	[categId] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[categId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${logschemaname}].[transactionlog]    Script Date: 6/10/2020 6:16:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${logschemaname}].[transactionlog](
	[id] [nvarchar](50) NOT NULL,
	[transactionId] [nvarchar](50) NULL,
	[username] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[serviceName] [nvarchar](50) NULL,
	[type] [nvarchar](50) NULL,
	[fromAccount] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[toAccount] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[amount] [decimal](20, 2) NULL,
	[currencyCode] [nvarchar](50) NULL,
	[channel] [nvarchar](50) NULL,
	[status] [nvarchar](50) NULL,
	[description] [nvarchar](1000) NULL,
	[routingNumber] [nvarchar](50) NULL,
	[batchId] [nvarchar](45) NULL,
	[transactionDate] [datetime] NOT NULL,
	[fromMobileOrEmail] [nvarchar](50) NULL,
	[toMobileOrEmail] [nvarchar](50) NULL,
	[swiftCode] [nvarchar](50) NULL,
	[internationalRoutingCode] [nvarchar](50) NULL,
	[ibanNumber] [nvarchar](50) NULL,
	[createdBy] [nvarchar](50) NULL,
	[createdOn] [datetime] NOT NULL,
	[module] [nvarchar](255) NULL,
	[customerId] [nvarchar](50) NULL,
	[device] [nvarchar](50) NULL,
	[operatingSystem] [nvarchar](50) NULL,
	[deviceId] [nvarchar](50) NULL,
	[ipAddress] [nvarchar](50) NULL,
	[referenceNumber] [nvarchar](50) NULL,
	[transactionDescription] [nvarchar](1000) NULL,
	[errorCode] [nvarchar](50) NULL,
	[recipientType] [nvarchar](50) NULL,
	[recipientBankName] [nvarchar](50) NULL,
	[recipientAddress] [nvarchar](1000) NULL,
	[recipientBankAddress] [nvarchar](1000) NULL,
	[checkNumber] [nvarchar](50) NULL,
	[cashWithdrawalFor] [nvarchar](50) NULL,
 CONSTRAINT [PK_transactionlog_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${logschemaname}].[archivedauditactivity](
	[Id] [varchar](255) NOT NULL,
	[EventId] [varchar](45) NULL,
	[EventType] [varchar](255) NULL,
	[EventSubType] [varchar](255) NULL,
	[Status_Id] [varchar](255) NULL,
	[sessionId] [varchar](255) NULL,
	[AppId] [varchar](255) NULL,
	[UserName] [varchar](255) NULL,
	[Customer_Id] [varchar](255) NULL,
	[partyid] [varchar](255) NULL,
	[corecustomerid] [varchar](255) NULL,
	[isCSRAssist] [smallint] NOT NULL,
	[appSessionId] [varchar](255) NULL,
	[payeeNickName] [varchar](255) NULL,
	[relationshipNumber] [varchar](255) NULL,
	[AdminUserName] [varchar](255) NULL,
	[AdminUserRole] [varchar](255) NULL,
	[Producer] [varchar](255) NULL,
	[MoneyMovementRefId] [varchar](255) NULL,
	[EventData] [varchar](max) NULL,
	[creditcardnumber] [varchar](255) NULL,
	[mfa_State] [varchar](255) NULL,
	[mfa_ServiceKey] [varchar](255) NULL,
	[mfa_Type] [varchar](255) NULL,
	[nonSearchable] [varchar](max) NULL,
	[phoneNumber] [varchar](255) NULL,
	[email] [varchar](255) NULL,
	[deviceModel] [varchar](255) NULL,
	[operatingSystem] [varchar](255) NULL,
	[browser] [varchar](255) NULL,
	[deviceId] [varchar](255) NULL,
	[channel] [varchar](255) NULL,
	[appVersion] [varchar](255) NULL,
	[platform] [varchar](255) NULL,
	[ipAddress] [varchar](255) NULL,
	[eventts] [datetime] NULL,
	[createdby] [varchar](255) NULL,
	[createdts] [datetime] NULL,
	[softdeleteflag] [smallint] NULL,
 CONSTRAINT [PK_archivedauditactivity_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${logschemaname}].[archivedmoneymovementlog](
	[Id] [varchar](50) NOT NULL,
	[isScheduled] [bit] NOT NULL,
	[Customer_id] [varchar](50) NULL,
	[ExpenseCategory_id] [int] NULL,
	[Payee_id] [int] NULL,
	[Bill_id] [int] NULL,
	[Type_id] [int] NULL,
	[Reference_id] [varchar](50) NULL,
	[fromAccountNumber] [varchar](50) NULL,
	[fromAccountBalance] [decimal](10, 2) NULL,
	[toAccountNumber] [varchar](50) NULL,
	[toAccountBalance] [decimal](10, 2) NULL,
	[amount] [decimal](20, 2) NULL,
	[convertedAmount] [decimal](20, 2) NULL,
	[transactionCurrency] [varchar](45) NULL,
	[baseCurrency] [varchar](45) NULL,
	[Status_id] [varchar](50) NULL,
	[statusDesc] [varchar](50) NULL,
	[notes] [varchar](150) NULL,
	[checkNumber] [int] NULL,
	[imageURL1] [text] NULL,
	[imageURL2] [text] NULL,
	[hasDepositImage] [tinyint] NULL,
	[description] [varchar](100) NULL,
	[scheduledDate] [datetime] NULL,
	[transactionDate] [datetime] NULL,
	[createdDate] [datetime] NULL,
	[transactionComments] [varchar](100) NULL,
	[toExternalAccountNumber] [varchar](45) NULL,
	[Person_Id] [int] NULL,
	[frequencyType] [varchar](20) NULL,
	[numberOfRecurrences] [int] NULL,
	[frequencyStartDate] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[checkImage] [nvarchar](1) NULL,
	[checkImageBack] [nvarchar](1) NULL,
	[cashlessOTPValidDate] [datetime] NULL,
	[cashlessOTP] [varchar](50) NULL,
	[cashlessPhone] [varchar](50) NULL,
	[cashlessEmail] [varchar](50) NULL,
	[cashlessPersonName] [varchar](50) NULL,
	[cashlessMode] [varchar](50) NULL,
	[cashlessSecurityCode] [varchar](50) NULL,
	[cashWithdrawalTransactionStatus] [varchar](50) NULL,
	[cashlessPin] [varchar](50) NULL,
	[category] [varchar](30) NULL,
	[billCategory] [varchar](20) NULL,
	[recurrenceDesc] [varchar](50) NULL,
	[deliverBy] [varchar](50) NULL,
	[p2pContact] [varchar](50) NULL,
	[p2pRequiredDate] [datetime] NULL,
	[requestCreatedDate] [varchar](50) NULL,
	[penaltyFlag] [bit] NULL,
	[payoffFlag] [bit] NULL,
	[viewReportLink] [varchar](150) NULL,
	[isPaypersonDeleted] [bit] NULL,
	[fee] [varchar](50) NULL,
	[feeCurrency] [varchar](45) NULL,
	[feePaidByReceipent] [bit] NULL,
	[frontImage1] [varchar](100) NULL,
	[frontImage2] [varchar](100) NULL,
	[backImage1] [varchar](100) NULL,
	[backImage2] [varchar](100) NULL,
	[checkDesc] [varchar](50) NULL,
	[checkNumber1] [varchar](50) NULL,
	[checkNumber2] [varchar](50) NULL,
	[bankName1] [varchar](50) NULL,
	[bankName2] [varchar](50) NULL,
	[withdrawlAmount1] [varchar](50) NULL,
	[withdrawlAmount2] [varchar](50) NULL,
	[cashAmount] [varchar](50) NULL,
	[payeeCurrency] [varchar](50) NULL,
	[billid] [bigint] NULL,
	[isDisputed] [bit] NULL,
	[disputeDescription] [varchar](50) NULL,
	[disputeReason] [varchar](50) NULL,
	[disputeStatus] [varchar](50) NULL,
	[disputeDate] [datetime] NULL,
	[payeeName] [varchar](50) NULL,
	[checkDateOfIssue] [datetime] NULL,
	[checkReason] [varchar](50) NULL,
	[isPayeeDeleted] [bit] NULL,
	[amountRecieved] [varchar](50) NULL,
	[requestValidity] [datetime] NULL,
	[statementReference] [varchar](35) NULL,
	[transCreditDebitIndicator] [varchar](45) NULL,
	[bookingDateTime] [datetime] NULL,
	[valueDateTime] [datetime] NULL,
	[transactionInformation] [varchar](500) NULL,
	[addressLine] [varchar](70) NULL,
	[transactionAmount] [varchar](50) NULL,
	[chargeAmount] [varchar](50) NULL,
	[chargeCurrency] [varchar](50) NULL,
	[sourceCurrency] [varchar](45) NULL,
	[targetCurrency] [varchar](50) NULL,
	[unitCurrency] [varchar](50) NULL,
	[exchangeRate] [varchar](45) NULL,
	[contractIdentification] [varchar](35) NULL,
	[quotationDate] [datetime] NULL,
	[instructedAmount] [varchar](45) NULL,
	[instructedCurrency] [varchar](45) NULL,
	[transactionCode] [varchar](35) NULL,
	[transactionSubCode] [varchar](45) NULL,
	[proprietaryTransactionCode] [varchar](35) NULL,
	[proprietaryTransactionIssuer] [varchar](35) NULL,
	[balanceCreditDebitIndicator] [varchar](45) NULL,
	[balanceType] [varchar](45) NULL,
	[balanceAmount] [varchar](45) NULL,
	[balanceCurrency] [varchar](45) NULL,
	[merchantName] [varchar](45) NULL,
	[merchantCategoryCode] [varchar](45) NULL,
	[creditorAgentSchemeName] [varchar](45) NULL,
	[creditorAgentIdentification] [varchar](45) NULL,
	[creditorAgentName] [varchar](140) NULL,
	[creditorAgentaddressType] [varchar](45) NULL,
	[creditorAgentDepartment] [varchar](45) NULL,
	[creditorAgentSubDepartment] [varchar](45) NULL,
	[creditorAgentStreetName] [varchar](45) NULL,
	[creditorAgentBuildingNumber] [varchar](45) NULL,
	[creditorAgentPostCode] [varchar](45) NULL,
	[creditorAgentTownName] [varchar](45) NULL,
	[creditorAgentCountrySubDivision] [varchar](45) NULL,
	[creditorAgentCountry] [varchar](45) NULL,
	[creditorAgentAddressLine] [varchar](45) NULL,
	[creditorAccountSchemeName] [varchar](45) NULL,
	[creditorAccountIdentification] [varchar](45) NULL,
	[creditorAccountName] [varchar](45) NULL,
	[creditorAccountSeconIdentification] [varchar](45) NULL,
	[debtorAgentSchemeName] [varchar](45) NULL,
	[debtorAgentIdentification] [varchar](45) NULL,
	[debtorAgentName] [varchar](45) NULL,
	[debtorAgentAddressType] [varchar](45) NULL,
	[debtorAgentDepartment] [varchar](45) NULL,
	[debtorAgentSubDepartment] [varchar](45) NULL,
	[debtorAgentStreetName] [varchar](45) NULL,
	[debtorAgentBuildingNumber] [varchar](45) NULL,
	[dedtorAgentPostCode] [varchar](45) NULL,
	[debtorAgentTownName] [varchar](45) NULL,
	[debtorAgentCountrySubDivision] [varchar](45) NULL,
	[debtorAgentCountry] [varchar](45) NULL,
	[debtorAgentAddressLine] [varchar](45) NULL,
	[debtorAccountSchemeName] [varchar](45) NULL,
	[debtorAccountIdentification] [varchar](45) NULL,
	[debtorAccountName] [varchar](45) NULL,
	[debtorAccountSeconIdentification] [varchar](45) NULL,
	[cardInstrumentSchemeName] [varchar](45) NULL,
	[cardInstrumentAuthorisationType] [varchar](45) NULL,
	[cardInstrumentName] [varchar](45) NULL,
	[cardInstrumentIdentification] [varchar](45) NULL,
	[IBAN] [varchar](45) NULL,
	[sortCode] [varchar](45) NULL,
	[FirstPaymentDateTime] [datetime] NULL,
	[NextPaymentDateTime] [datetime] NULL,
	[FinalPaymentDateTime] [datetime] NULL,
	[StandingOrderStatusCode] [varchar](6) NULL,
	[FP_Amount] [decimal](12, 2) NULL,
	[FP_Currency] [varchar](45) NULL,
	[NP_Amount] [decimal](12, 2) NULL,
	[NP_Currency] [varchar](45) NULL,
	[FPA_Amount] [decimal](12, 2) NULL,
	[FPA_Currency] [varchar](45) NULL,
	[ConsentId] [varchar](45) NULL,
	[Initiation_InstructionIdentification] [varchar](45) NULL,
	[Initiation_EndToEndIdentification] [varchar](45) NULL,
	[RI_Reference] [varchar](45) NULL,
	[RI_Unstructured] [varchar](45) NULL,
	[RiskPaymentContextCode] [varchar](45) NULL,
	[MerchantCustomerIdentification] [varchar](200) NULL,
	[beneficiaryName] [varchar](50) NULL,
	[bankName] [varchar](50) NULL,
	[swiftCode] [varchar](45) NULL,
	[DomesticPaymentId] [varchar](45) NULL,
	[linkSelf] [varchar](45) NULL,
	[StatusUpdateDateTime] [datetime] NULL,
	[dataStatus] [varchar](45) NULL,
	[serviceName] [varchar](50) NULL,
	[payPersonName] [varchar](45) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [event]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [username]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [userRole]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [moduleName]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (getdate()) FOR [eventts]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (NULL) FOR [createdBy]
GO
ALTER TABLE [${logschemaname}].[adminactivity] ADD  DEFAULT (getdate()) FOR [createdOn]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [customerId]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [adminName]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [adminRole]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [activityType]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (getdate()) FOR [eventts]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (NULL) FOR [createdBy]
GO
ALTER TABLE [${logschemaname}].[admincustomeractivity] ADD  DEFAULT (getdate()) FOR [createdOn]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [EventId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [EventType]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [EventSubType]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [Status_Id]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [sessionId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [AppId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [UserName]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [Customer_Id]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [partyid]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [corecustomerid]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT ((0)) FOR [isCSRAssist]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [appSessionId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [payeeNickName]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [relationshipNumber]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [AdminUserName]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [AdminUserRole]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [Producer]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [MoneyMovementRefId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [EventData]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [creditcardnumber]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [mfa_State]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [mfa_ServiceKey]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [mfa_Type]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [nonSearchable]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [deviceModel]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [operatingSystem]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [browser]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [deviceId]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [channel]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [appVersion]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [platform]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [ipAddress]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [eventts]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${logschemaname}].[auditactivity] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [sessionId]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [username]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [moduleName]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [activityType]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (getdate()) FOR [eventts]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [channel]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [ipAddress]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [device]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [deviceId]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [operatingSystem]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [browser]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [referenceId]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [errorCode]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [createdBy]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (getdate()) FOR [createdOn]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [customerId]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [typeOfMFA]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [accountNumber]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [relationshipNumber]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${logschemaname}].[customeractivity] ADD  DEFAULT (NULL) FOR [maskedAccountNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [isScheduled]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [ExpenseCategory_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Payee_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Bill_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Reference_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [fromAccountBalance]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [toAccountBalance]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [amount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [convertedAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [baseCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [statusDesc]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('') FOR [notes]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [checkNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [hasDepositImage]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (' ') FOR [description]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [createdDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionComments]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [toExternalAccountNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Person_Id]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('Once') FOR [frequencyType]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [numberOfRecurrences]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [frequencyStartDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessOTPValidDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessOTP]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPhone]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessEmail]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPersonName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessMode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessSecurityCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashWithdrawalTransactionStatus]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPin]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('Uncategorised') FOR [category]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [billCategory]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [recurrenceDesc]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [deliverBy]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [p2pContact]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [p2pRequiredDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [requestCreatedDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [penaltyFlag]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [payoffFlag]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('http://pmqa.konylabs.net/KonyWebBanking/view_report.png') FOR [viewReportLink]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [isPaypersonDeleted]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [fee]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [feeCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [feePaidByReceipent]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [frontImage1]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [frontImage2]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [backImage1]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [backImage2]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [checkDesc]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [checkNumber1]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [checkNumber2]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [bankName1]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [bankName2]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [withdrawlAmount1]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [withdrawlAmount2]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0.00') FOR [cashAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('INR') FOR [payeeCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [billid]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [isDisputed]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [disputeDescription]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [disputeReason]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [disputeStatus]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [disputeDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [checkDateOfIssue]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [checkReason]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [isPayeeDeleted]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT ('0') FOR [amountRecieved]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [requestValidity]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [statementReference]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transCreditDebitIndicator]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [bookingDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [valueDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionInformation]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [addressLine]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [chargeAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [chargeCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [sourceCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [targetCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [unitCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [exchangeRate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [contractIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [quotationDate]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [instructedAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [instructedCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [transactionSubCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [proprietaryTransactionCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [proprietaryTransactionIssuer]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [balanceCreditDebitIndicator]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [balanceType]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [balanceAmount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [balanceCurrency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [merchantName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [merchantCategoryCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentSchemeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentaddressType]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentDepartment]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentSubDepartment]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentStreetName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentBuildingNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentPostCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentTownName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentCountrySubDivision]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentCountry]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentAddressLine]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountSchemeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountSeconIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentSchemeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentAddressType]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentDepartment]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentSubDepartment]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentStreetName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentBuildingNumber]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [dedtorAgentPostCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentTownName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentCountrySubDivision]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentCountry]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentAddressLine]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountSchemeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountSeconIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentSchemeName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentAuthorisationType]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [sortCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FirstPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [NextPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FinalPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [StandingOrderStatusCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FP_Amount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FP_Currency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [NP_Amount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [NP_Currency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FPA_Amount]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [FPA_Currency]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [ConsentId]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Initiation_InstructionIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [Initiation_EndToEndIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [RI_Reference]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [RI_Unstructured]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [RiskPaymentContextCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [MerchantCustomerIdentification]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [DomesticPaymentId]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [linkSelf]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [StatusUpdateDateTime]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [dataStatus]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [serviceName]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [transactionId]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [username]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [serviceName]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [fromAccount]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [toAccount]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [currencyCode]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [channel]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [batchId]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (getdate()) FOR [transactionDate]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [fromMobileOrEmail]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [toMobileOrEmail]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [internationalRoutingCode]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [ibanNumber]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [createdBy]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (getdate()) FOR [createdOn]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [module]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [customerId]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [device]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [operatingSystem]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [deviceId]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [ipAddress]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [referenceNumber]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [transactionDescription]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [errorCode]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [recipientType]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [recipientBankName]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [recipientAddress]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [recipientBankAddress]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [checkNumber]
GO
ALTER TABLE [${logschemaname}].[transactionlog] ADD  DEFAULT (NULL) FOR [cashWithdrawalFor]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog]  WITH CHECK ADD  CONSTRAINT [billcateg] FOREIGN KEY([billCategory])
REFERENCES [${logschemaname}].[billcategory] ([billcategId])
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] CHECK CONSTRAINT [billcateg]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog]  WITH CHECK ADD  CONSTRAINT [categ] FOREIGN KEY([category])
REFERENCES [${logschemaname}].[tcategories] ([categId])
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] CHECK CONSTRAINT [categ]
GO
ALTER TABLE [${logschemaname}].[moneymovementlog]  WITH CHECK ADD  CONSTRAINT [freqType] FOREIGN KEY([frequencyType])
REFERENCES [${logschemaname}].[freq1] ([freqId])
GO
ALTER TABLE [${logschemaname}].[moneymovementlog] CHECK CONSTRAINT [freqType]
GO
ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [EventId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [EventType]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [EventSubType]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [Status_Id]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [sessionId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [AppId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [UserName]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [Customer_Id]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [partyid]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [corecustomerid]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT ((0)) FOR [isCSRAssist]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [appSessionId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [payeeNickName]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [relationshipNumber]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [AdminUserName]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [AdminUserRole]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [Producer]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [MoneyMovementRefId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [EventData]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [creditcardnumber]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [mfa_State]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [mfa_ServiceKey]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [mfa_Type]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [nonSearchable]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [email]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [deviceModel]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [operatingSystem]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [browser]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [deviceId]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [channel]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [appVersion]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [platform]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [ipAddress]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [eventts]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [createdts]
GO

ALTER TABLE [${logschemaname}].[archivedauditactivity] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO

ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [isScheduled]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [ExpenseCategory_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Payee_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Bill_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Reference_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [fromAccountBalance]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [toAccountBalance]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [amount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [convertedAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [baseCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [statusDesc]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('') FOR [notes]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [checkNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [hasDepositImage]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (' ') FOR [description]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [createdDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionComments]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [toExternalAccountNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Person_Id]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('Once') FOR [frequencyType]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [numberOfRecurrences]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [frequencyStartDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessOTPValidDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessOTP]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPhone]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessEmail]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPersonName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessMode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessSecurityCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashWithdrawalTransactionStatus]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cashlessPin]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('Uncategorised') FOR [category]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [billCategory]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [recurrenceDesc]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [deliverBy]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [p2pContact]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [p2pRequiredDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [requestCreatedDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [penaltyFlag]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [payoffFlag]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('http://pmqa.konylabs.net/KonyWebBanking/view_report.png') FOR [viewReportLink]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [isPaypersonDeleted]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [fee]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [feeCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [feePaidByReceipent]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [frontImage1]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [frontImage2]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [backImage1]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [backImage2]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [checkDesc]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [checkNumber1]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [checkNumber2]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [bankName1]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [bankName2]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [withdrawlAmount1]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [withdrawlAmount2]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0.00') FOR [cashAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('INR') FOR [payeeCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [billid]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [isDisputed]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [disputeDescription]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [disputeReason]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [disputeStatus]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [disputeDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [checkDateOfIssue]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [checkReason]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [isPayeeDeleted]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT ('0') FOR [amountRecieved]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [requestValidity]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [statementReference]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transCreditDebitIndicator]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [bookingDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [valueDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionInformation]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [addressLine]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [chargeAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [chargeCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [sourceCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [targetCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [unitCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [exchangeRate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [contractIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [quotationDate]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [instructedAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [instructedCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [transactionSubCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [proprietaryTransactionCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [proprietaryTransactionIssuer]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [balanceCreditDebitIndicator]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [balanceType]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [balanceAmount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [balanceCurrency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [merchantName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [merchantCategoryCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentSchemeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentaddressType]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentDepartment]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentSubDepartment]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentStreetName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentBuildingNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentPostCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentTownName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentCountrySubDivision]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentCountry]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAgentAddressLine]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountSchemeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [creditorAccountSeconIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentSchemeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentAddressType]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentDepartment]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentSubDepartment]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentStreetName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentBuildingNumber]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [dedtorAgentPostCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentTownName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentCountrySubDivision]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentCountry]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAgentAddressLine]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountSchemeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [debtorAccountSeconIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentSchemeName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentAuthorisationType]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [cardInstrumentIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [sortCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FirstPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [NextPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FinalPaymentDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [StandingOrderStatusCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FP_Amount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FP_Currency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [NP_Amount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [NP_Currency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FPA_Amount]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [FPA_Currency]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [ConsentId]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Initiation_InstructionIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [Initiation_EndToEndIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [RI_Reference]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [RI_Unstructured]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [RiskPaymentContextCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [MerchantCustomerIdentification]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [DomesticPaymentId]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [linkSelf]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [StatusUpdateDateTime]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [dataStatus]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [serviceName]
GO
ALTER TABLE [${logschemaname}].[archivedmoneymovementlog] ADD  DEFAULT (NULL) FOR [payPersonName]
GO

CREATE PROCEDURE [${logschemaname}].[auditlogs_update_proc]  
   @_partyId nvarchar(50),
   @_customerId nvarchar(50),
   @_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      IF (@_customerId <> '')
         UPDATE [${logschemaname}].auditactivity
            SET 
               Customer_Id = @_customerId
         WHERE auditactivity.partyid = @_partyId

      IF (@_coreCustomerId <> '')
         UPDATE [${logschemaname}].auditactivity
            SET 
               corecustomerid = @_coreCustomerId
         WHERE auditactivity.partyid = @_partyId

   END
GO
/* EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.auditlogs_update_proc' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'PROCEDURE',@level1name=N'auditlogs_update_proc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.adminactivity' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'adminactivity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.admincustomeractivity' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'admincustomeractivity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.auditactivity' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'auditactivity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.customeractivity' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'customeractivity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.transactionlog' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'transactionlog'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.archivedauditactivity' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'archivedauditactivity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'${logschemaname}.archivedmoneymovementlog' , @level0type=N'SCHEMA',@level0name=N'${logschemaname}', @level1type=N'TABLE',@level1name=N'archivedmoneymovementlog'
GO */
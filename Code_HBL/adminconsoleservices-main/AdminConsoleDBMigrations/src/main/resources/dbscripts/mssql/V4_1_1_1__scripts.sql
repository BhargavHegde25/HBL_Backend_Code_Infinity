USE [${dbxdbname}]
GO
/****** Object:  Table [${dbxschemaname}].[accountcommunication]    Script Date: 6/9/2020 1:57:29 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS ( SELECT  * FROM    sys.schemas WHERE   name = N'${dbxschemaname}' )
    EXEC('CREATE SCHEMA [${dbxschemaname}]');
GO
CREATE TABLE [${dbxschemaname}].[accountcommunication](
	[Type_id] [nvarchar](50) NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Account_id] [bigint] NULL,
	[sequence] [int] NULL,
	[value] [nvarchar](100) NULL,
	[extension] [nvarchar](50) NULL,
	[description] [nvarchar](50) NULL,
 CONSTRAINT [PK_accountcommunication_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transaction](
	[Id] [int] IDENTITY(0,1) NOT NULL,
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
	[scheduledDate] [date] NULL,
	[transactionDate] [datetime] NOT NULL,
	[postedDate] [datetime] NULL,
	[createdDate] [datetime] NOT NULL,
	[transactionComments] [varchar](100) NULL,
	[toExternalAccountNumber] [varchar](45) NULL,
	[Person_Id] [int] NULL,
	[frequencyType] [varchar](20) NULL,
	[numberOfRecurrences] [int] NULL,
	[frequencyStartDate] [date] NULL,
	[frequencyEndDate] [date] NULL,
	[checkImage] [nvarchar](max) NULL,
	[checkImageBack] [nvarchar](max) NULL,
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
	[p2pRequiredDate] [date] NULL,
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
	[serviceName] [varchar](45) NULL,
	[payPersonName] [varchar](45) NULL,
	[payPersonNickName] [varchar](45) NULL,
	[p2pAlternateContact] [varchar](45) NULL,
	[billerId] [varchar](45) NULL,
	[bicCode] [varchar](50) NULL,
	[paidBy] [varchar](50) DEFAULT NULL,
    [paymentType] [varchar](50) DEFAULT NULL,
    [feeAmount] [varchar](50) DEFAULT NULL,
    [beneficiaryAddressNickName] [varchar](50) DEFAULT NULL,
    [beneficiaryAddressLine1] [varchar](200) DEFAULT NULL,
    [beneficiaryCity] [varchar](50) DEFAULT NULL,
    [beneficiaryZipcode] [varchar](50) DEFAULT NULL,
    [beneficiarycountry] [varchar](50) DEFAULT NULL,
    [bankId] [varchar](50) DEFAULT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionfee]    Script Date: 6/9/2020 1:57:29 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[accounts](
	[Account_id] [varchar](50) NOT NULL,
	[AccountName] [varchar](50) NULL,
	[UserName] [varchar](40) NULL,
	[ExternalBankidentity_id] [nvarchar](50) NULL,
	[CurrencyCode] [varchar](50) NULL,
	[AvailableBalance] [varchar](50) NULL,
	[AccountHolder] [varchar](500) NULL,
	[Address] [varchar](45) NULL,
	[Scheme] [varchar](5) NULL,
	[Number] [varchar](50) NULL,
	[error] [varchar](1) NULL,
	[Type_id] [varchar](50) NULL,
	[Product_id] [int] NULL,
	[Bank_id] [varchar](50) NOT NULL,
	[User_id] [varchar](50) NULL,
	[Name] [varchar](100) NULL,
	[isBusinessAccount] [bit] NOT NULL,
	[Status_id] [bigint] NULL,
	[StatusDesc] [varchar](50) NULL,
	[SupportDeposit] [int] NULL,
	[SupportBillPay] [int] NULL,
	[SupportTransferFrom] [int] NULL,
	[SupportTransferTo] [int] NULL,
	[ShowTransactions] [bit] NULL,
	[CurrentBalance] [decimal](10, 2) NULL,
	[InterestRate] [decimal](10, 2) NULL,
	[AvailableCredit] [decimal](10, 2) NULL,
	[MinimumDue] [decimal](10, 2) NULL,
	[DueDate] [date] NULL,
	[PrincipalValue] [decimal](10, 2) NULL,
	[FirstPaymentDate] [date] NULL,
	[ClosingDate] [date] NULL,
	[PaymentTerm] [varchar](50) NULL,
	[OpeningDate] [date] NULL,
	[MaturityDate] [date] NULL,
	[TransactionLimit] [decimal](10, 2) NULL,
	[TransferLimit] [decimal](10, 2) NULL,
	[NickName] [varchar](50) NULL,
	[LastStatementBalance] [decimal](10, 2) NULL,
	[AvailablePoints] [int] NULL,
	[OutstandingBalance] [decimal](10, 2) NULL,
	[CreditCardNumber] [bigint] NULL,
	[IsPFM] [bit] NOT NULL,
	[SupportCardlessCash] [int] NULL,
	[FavouriteStatus] [int] NULL,
	[MaturityOption] [varchar](50) NULL,
	[RoutingNumber] [varchar](50) NULL,
	[SwiftCode] [varchar](50) NULL,
	[JointHolders] [varchar](500) NULL,
	[DividendRate] [varchar](50) NULL,
	[DividendYTD] [varchar](50) NULL,
	[LastDividendPaidAmount] [varchar](50) NULL,
	[LastDividendPaidDate] [varchar](50) NULL,
	[PreviousYearDividend] [varchar](50) NULL,
	[BondInterest] [varchar](50) NULL,
	[BondInterestLastYear] [varchar](50) NULL,
	[TotalCreditMonths] [varchar](50) NULL,
	[TotalDebitsMonth] [varchar](50) NULL,
	[CurrentAmountDue] [varchar](50) NULL,
	[PaymentDue] [varchar](50) NULL,
	[LastPaymentDate] [varchar](30) NULL,
	[LastPaymentAmount] [varchar](50) NULL,
	[LateFeesDue] [varchar](50) NULL,
	[CreditLimit] [varchar](50) NULL,
	[InterestPaidYTD] [varchar](50) NULL,
	[InterestPaidPreviousYTD] [varchar](50) NULL,
	[UnpaidInterest] [varchar](50) NULL,
	[PaymentMethod] [varchar](50) NULL,
	[RegularPaymentAmount] [varchar](50) NULL,
	[DividendPaidYTD] [varchar](50) NULL,
	[DividendLastPaidAmount] [varchar](50) NULL,
	[DividendLastPaidDate] [varchar](50) NULL,
	[PreviousYearsDividends] [varchar](50) NULL,
	[PendingDeposit] [varchar](50) NULL,
	[PendingWithdrawal] [varchar](50) NULL,
	[InterestEarned] [varchar](50) NULL,
	[maturityAmount] [varchar](50) NULL,
	[principalBalance] [varchar](50) NULL,
	[OriginalAmount] [varchar](50) NULL,
	[payoffAmount] [varchar](50) NULL,
	[BsbNum] [int] NULL,
	[PayOffCharge] [varchar](50) NULL,
	[InterestPaidLastYear] [varchar](50) NULL,
	[EStatementmentEnable] [bit] NOT NULL,
	[Phone_id] [int] NULL,
	[LastUpdated] [varchar](14) NULL,
	[BankName] [varchar](50) NULL,
	[AccountPreference] [int] NOT NULL,
	[InternalAccount] [varchar](1) NULL,
	[softdeleteflag] [bit] NOT NULL,
	[product] [ntext] NULL,
	[email] [varchar](150) NULL,
	[jointAccountHolder1] [varchar](50) NULL,
	[jointAccountHolder2] [varchar](50) NULL,
	[bankAddress] [varchar](50) NULL,
	[intermediaryBankName] [varchar](50) NULL,
	[intermediaryBankAddress] [varchar](50) NULL,
	[intermediaryBankSwiftCode] [varchar](50) NULL,
	[phone] [varchar](50) NULL,
	[accountSubType] [varchar](45) NULL,
	[description] [varchar](45) NULL,
	[schemeName] [varchar](40) NULL,
	[identification] [varchar](256) NULL,
	[secondaryIdentification] [varchar](34) NULL,
	[servicerSchemeName] [varchar](45) NULL,
	[servicerIdentification] [varchar](45) NULL,
	[dataCreditDebitIndicator] [varchar](45) NULL,
	[dataType] [varchar](45) NULL,
	[dataDateTime] [datetime] NULL,
	[dataCreditLineIncluded] [varchar](45) NULL,
	[dataCreditLineType] [varchar](45) NULL,
	[dataCreditLineAmount] [varchar](45) NULL,
	[dataCreditLineCurrency] [varchar](45) NULL,
	[IBAN] [varchar](45) NULL,
	[adminProductId] [varchar](50) NULL,
	[TaxId] [varchar](45) NULL,
	[UpdatedBy] [varchar](45) NULL,
	[ActualUpdatedBY] [varchar](45) NULL,
	[Organization_id] [varchar](50) NULL,
	[Membership_id] [varchar](50) DEFAULT NULL,
    [MembershipName] [varchar](45) DEFAULT NULL,
    [ownership] [varchar](45) DEFAULT NULL,
    [arrangementId] [varchar](45) DEFAULT NULL,
PRIMARY KEY CLUSTERED 
(
	[Account_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[accountstatement]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[accountstatement](
	[Id] [int] IDENTITY(1306,1) NOT NULL,
	[description] [nvarchar](100) NULL,
	[statementLink] [nvarchar](100) NULL,
	[Account_id] [nvarchar](50) NOT NULL,
	[month] [datetime] NULL,
 CONSTRAINT [PK_accountstatement_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[accounttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[accounttype](
	[TypeID] [nvarchar](50) NOT NULL,
	[TypeDescription] [nvarchar](100) NULL,
	[displayName] [nvarchar](100) NULL,
	[transactionLimit] [decimal](10, 2) NULL,
	[transferLimit] [decimal](10, 2) NULL,
	[dailyDepositLimit] [decimal](10, 2) NULL,
	[monthlyDepositLimit] [decimal](10, 2) NULL,
	[termsAndConditions] [nvarchar](2000) NULL,
	[features] [nvarchar](2000) NULL,
	[rates] [nvarchar](2000) NULL,
	[info] [nvarchar](2000) NULL,
	[supportChecks] [int] NULL,
	[countryCode] [nvarchar](45) NULL,
 CONSTRAINT [PK_accounttype_TypeID] PRIMARY KEY CLUSTERED 
(
	[TypeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achaccountstype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achaccountstype](
	[id] [int] NOT NULL,
	[accountType] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_achaccountstype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achfile]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achfile](
	[achFile_id] [int] IDENTITY(14,1) NOT NULL,
	[achFileName] [nvarchar](45) NULL,
	[featureActionId] [nvarchar](50) NULL,
	[softDelete] [bit] NULL,
	[debitAmount] [float] NULL,
	[createdts] [datetime] NULL,
	[requestType] [nvarchar](45) NULL,
	[numberOfCredits] [int] NULL,
	[numberOfDebits] [int] NULL,
	[numberOfPrenotes] [int] NULL,
	[requestId] [bigint] NULL,
	[createdby] [nvarchar](50) NULL,
	[contents] [varbinary](max) NULL,
	[fileSize] [real] NOT NULL,
	[creditAmount] [float] NULL,
	[numberOfRecords] [int] NULL,
	[achFileFormatType_id] [int] NULL,
	[status] [nvarchar](50) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[actedBy] [nvarchar](45) NULL,
	[updatedts] [datetime] NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[approvalAccounts] [nvarchar](max) NULL,
	[debitAccounts] [nvarchar](max) NULL,
 CONSTRAINT [PK_achfile_achFile_id] PRIMARY KEY CLUSTERED 
(
	[achFile_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achfileformattype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achfileformattype](
	[id] [int] IDENTITY(5,1) NOT NULL,
	[fileType] [nvarchar](45) NOT NULL,
	[fileextension] [nvarchar](10) NULL,
	[mimetype] [nvarchar](100) NULL,
 CONSTRAINT [PK_achfileformattype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achfilerecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[tcategories](
	[categId] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[categId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
CREATE TABLE [${dbxschemaname}].[billcategory](
	[billcategId] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[billcategId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
CREATE TABLE [${dbxschemaname}].[freq1](
	[freqId] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[freqId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
CREATE TABLE [${dbxschemaname}].[achfilerecord](
	[achFileRecordId] [int] IDENTITY(54,1) NOT NULL,
	[achFileId] [int] NULL,
	[transactionType] [nvarchar](50) NULL,
	[requestType] [nvarchar](50) NULL,
	[totalDebitAmount] [float] NULL,
	[totalCreditAmount] [float] NULL,
	[effectiveDate] [datetime] NULL,
	[offsetAccountNumber] [nvarchar](50) NULL,
	[offsetTransactionType] [nvarchar](50) NULL,
	[offsetAmount] [float] NULL,
 CONSTRAINT [PK_achfilerecord_achFileRecordId] PRIMARY KEY CLUSTERED 
(
	[achFileRecordId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achfilesubrecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achfilesubrecord](
	[achFileSubRecordId] [int] IDENTITY(54,1) NOT NULL,
	[achFileRecordId] [int] NULL,
	[receiverTransactionType] [nvarchar](50) NULL,
	[receiverAccountType] [nvarchar](50) NULL,
	[receiverAccountNumber] [nvarchar](50) NULL,
	[receiverName] [nvarchar](50) NULL,
	[amount] [float] NULL,
 CONSTRAINT [PK_achfilesubrecord_achFileSubRecordId] PRIMARY KEY CLUSTERED 
(
	[achFileSubRecordId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achtransaction]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achtransaction](
	[transaction_id] [int] IDENTITY(44,1) NOT NULL,
	[fromAccount] [nvarchar](45) NULL,
	[effectiveDate] [datetime] NULL,
	[requestId] [bigint] NULL,
	[createdby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[maxAmount] [float] NULL,
	[status] [nvarchar](50) NULL,
	[transactionType_id] [int] NULL,
	[templateType_id] [int] NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[templateRequestType_id] [int] NULL,
	[softDelete] [int] NOT NULL,
	[templateName] [nvarchar](45) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[actedBy] [nvarchar](50) NULL,
	[template_id] [int] NULL,
	[updatedts] [datetime] NULL,
	[totalAmount] [float] NULL,
	[featureActionId] [nvarchar](50) NULL,
 CONSTRAINT [PK_achtransaction_transaction_id] PRIMARY KEY CLUSTERED 
(
	[transaction_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achtransactionrecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achtransactionrecord](
	[transactionRecord_id] [int] IDENTITY(40,1) NOT NULL,
	[toAccountNumber] [nvarchar](45) NULL,
	[toAccountType] [int] NULL,
	[abatrcNumber] [nvarchar](45) NULL,
	[detail_id] [nvarchar](45) NULL,
	[amount] [float] NULL,
	[additionalInfo] [nvarchar](500) NULL,
	[eIN] [nvarchar](45) NULL,
	[isZeroTaxDue] [tinyint] NULL,
	[taxType_id] [int] NULL,
	[transaction_id] [int] NULL,
	[softDelete] [int] NOT NULL,
	[templateRequestType_id] [int] NULL,
	[record_Name] [nvarchar](45) NULL,
 CONSTRAINT [PK_achtransactionrecord_transactionRecord_id] PRIMARY KEY CLUSTERED 
(
	[transactionRecord_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[achtransactionsubrecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[achtransactionsubrecord](
	[transcationSubRecord_id] [int] IDENTITY(1,1) NOT NULL,
	[amount] [float] NOT NULL,
	[transactionRecord_id] [int] NOT NULL,
	[taxSubCategory_id] [int] NOT NULL,
	[softDelete] [int] NOT NULL,
 CONSTRAINT [PK_achtransactionsubrecord_transcationSubRecord_id] PRIMARY KEY CLUSTERED 
(
	[transcationSubRecord_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[action]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[action](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_action_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[actiondisplaynamedescription]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[actiondisplaynamedescription](
	[Action_id] [nvarchar](255) NOT NULL,
	[Locale_id] [nvarchar](50) NOT NULL,
	[displayName] [nvarchar](max) NOT NULL,
	[displayDescription] [nvarchar](max) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_actiondisplaynamedescription_Action_id] PRIMARY KEY CLUSTERED 
(
	[Action_id] ASC,
	[Locale_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[actionlimit]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[actionlimit](
	[Action_id] [nvarchar](255) NOT NULL,
	[LimitType_id] [nvarchar](50) NOT NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_actionlimit_Action_id] PRIMARY KEY CLUSTERED 
(
	[Action_id] ASC,
	[LimitType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[actionprofile]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[actionprofile](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_actionprofile_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[actiontype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[actiontype](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_actiontype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[addetails]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[addetails](
	[id] [bigint] NOT NULL,
	[action1] [nvarchar](100) NULL,
	[action2] [nvarchar](100) NULL,
	[imageURL] [nvarchar](500) NULL,
	[description] [nvarchar](100) NULL,
	[adType] [nvarchar](45) NULL,
	[title] [nvarchar](45) NULL,
	[user_id] [int] NULL,
	[actionType] [nvarchar](50) NULL,
	[imageURL2] [nvarchar](500) NULL,
 CONSTRAINT [PK_addetails_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionaldata]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionaldata](
	[id] [nvarchar](50) NOT NULL,
	[Object_id] [nvarchar](50) NOT NULL,
	[ObjectType] [nvarchar](50) NOT NULL,
	[AdditionalField_id] [nvarchar](50) NULL,
	[FieldValue] [nvarchar](2000) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_additionaldata_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionalemploymentsresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionalemploymentsresponse](
	[id] [nvarchar](50) NOT NULL,
	[IncomeResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[EmploymentType_id] [nvarchar](50) NULL,
	[ProvideEmploymentDetails] [bit] NULL,
	[EmployerName] [nvarchar](50) NULL,
	[EmployerAddressLine1] [nvarchar](100) NULL,
	[EmployerAddressLine2] [nvarchar](100) NULL,
	[EmployerAddressCity] [nvarchar](50) NULL,
	[EmployerAddressState] [nvarchar](50) NULL,
	[EmployerAddressCountry] [nvarchar](50) NULL,
	[EmployerAddressZipCode] [int] NULL,
	[EmployerPhoneNumber] [nvarchar](15) NULL,
	[TotalGrossIncome] [decimal](10, 2) NULL,
	[EmployeeDesignation] [nvarchar](50) NULL,
	[BusinessShare_id] [nvarchar](50) NULL,
	[BusinessMonthlyLoss] [decimal](10, 2) NULL,
	[ProfessionStartDate] [date] NULL,
	[LineOfWorkDuration] [nvarchar](10) NULL,
	[IsOtherParty] [bit] NULL,
	[PrevStartDate] [date] NULL,
	[PrevEndDate] [date] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_additionalemploymentsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [additionalemploymentsresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionalfield]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionalfield](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NOT NULL,
	[ObjectType] [nvarchar](50) NOT NULL,
	[DataType] [nvarchar](50) NULL,
	[Length] [nvarchar](50) NULL,
	[FieldLabel] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_additionalfield_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionalincomesresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionalincomesresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[AdditionalEmploymentsResponse_id] [nvarchar](50) NULL,
	[IncomeDetail_id] [nvarchar](50) NULL,
	[PayPeriod_id] [nvarchar](50) NULL,
	[Amount] [decimal](10, 2) NULL,
	[WorkingHours] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_additionalincomesresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionalmortgageheaderresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionalmortgageheaderresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[HasAdditionalMortgage] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_additionalmortgageheaderresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [additionalmortgageheaderresponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[additionalmortgageresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[additionalmortgageresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[CreditorName] [nvarchar](50) NULL,
	[LienType] [nvarchar](50) NULL,
	[MonthlyMortgage] [int] NULL,
	[AmountToBeDrawn] [int] NULL,
	[CreditLimit] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[AdditionalMortgageHeaderResponse_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_additionalmortgageresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [additionalmortgageresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[address]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[address](
	[id] [nvarchar](50) NOT NULL,
	[Region_id] [nvarchar](50) NULL,
	[City_id] [nvarchar](50) NULL,
	[addressLine1] [nvarchar](100) NULL,
	[addressLine2] [nvarchar](100) NULL,
	[addressLine3] [nvarchar](100) NULL,
	[zipCode] [nvarchar](20) NULL,
	[latitude] [nvarchar](20) NULL,
	[logitude] [nvarchar](20) NULL,
	[isPreferredAddress] [bit] NULL,
	[cityName] [nvarchar](100) NULL,
	[User_id] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[type] [nvarchar](6) NULL,
	[state] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_address_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[addresstype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[addresstype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_addresstype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[adminnotification]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[adminnotification](
	[Id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[Description] [nvarchar](1000) NULL,
	[StartDate] [date] NULL,
	[ExpirationDate] [date] NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_adminnotification_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[advertisements]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[advertisements](
	[id] [bigint] NOT NULL,
	[actionType] [nvarchar](45) NULL,
	[action] [nvarchar](45) NULL,
	[adimagesrc] [nvarchar](45) NULL,
	[url] [nvarchar](45) NULL,
	[user_id] [int] NULL,
 CONSTRAINT [PK_advertisements_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alert]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alert](
	[id] [nvarchar](50) NOT NULL,
	[AlertType_id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](250) NOT NULL,
	[IsSubscriptionNeeded] [tinyint] NULL,
	[Status_id] [nvarchar](50) NULL,
	[IsSmsActive] [bit] NOT NULL,
	[IsEmailActive] [bit] NOT NULL,
	[IsPushActive] [bit] NOT NULL,
	[AlertContent] [nvarchar](250) NULL,
	[Account_id] [nvarchar](50) NULL,
	[isActive] [bit] NULL,
	[hasValue] [bit] NULL,
	[currentValue] [nvarchar](50) NULL,
	[defaultValue] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alert_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertattribute]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertattribute](
	[id] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[name] [nvarchar](255) NULL,
	[type] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertattribute_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[LanguageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertattributelistvalues]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertattributelistvalues](
	[id] [nvarchar](50) NOT NULL,
	[AlertAttributeId] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[name] [nvarchar](250) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertattributelistvalues_AlertAttributeId] PRIMARY KEY CLUSTERED 
(
	[AlertAttributeId] ASC,
	[LanguageCode] ASC,
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertcategorychannel]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertcategorychannel](
	[ChannelID] [nvarchar](50) NOT NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertcategorychannel_ChannelID] PRIMARY KEY CLUSTERED 
(
	[ChannelID] ASC,
	[AlertCategoryId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertcondition]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertcondition](
	[id] [nvarchar](25) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[NoOfFields] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertcondition_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[LanguageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertcontentfields]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertcontentfields](
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[DefaultValue] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertcontentfields_Code] PRIMARY KEY CLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alerthistory]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alerthistory](
	[id] [nvarchar](100) NOT NULL,
	[EventId] int NOT NULL,
	[AlertSubTypeId] [nvarchar](75) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[AlertStatusId] [nvarchar](50) NOT NULL,
	[Customer_Id] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NULL,
	[ChannelId] [nvarchar](50) NOT NULL,
	[Status] [nvarchar](50) NULL,
	[Subject] [nvarchar](255) NULL,
	[Message] [nvarchar](max) NULL,
	[SenderName] [nvarchar](255) NULL,
	[SenderEmail] [nvarchar](255) NULL,
	[ReferenceNumber] [nvarchar](100) NULL,
	[DispatchDate] [datetime] NOT NULL,
	[ErrorMessage] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alerthistory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alertsubtype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alertsubtype](
	[id] [nvarchar](75) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[Description] [nvarchar](1000) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alertsubtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alerttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alerttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](30) NULL,
	[Description] [nvarchar](250) NULL,
	[IsSubscriptionNeeded] [tinyint] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alerttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alerttypeaccounttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alerttypeaccounttype](
	[AccountTypeId] [nvarchar](50) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alerttypeaccounttype_AccountTypeId] PRIMARY KEY CLUSTERED 
(
	[AccountTypeId] ASC,
	[AlertTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alerttypeapp]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alerttypeapp](
	[AppId] [nvarchar](50) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alerttypeapp_AppId] PRIMARY KEY CLUSTERED 
(
	[AppId] ASC,
	[AlertTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[alerttypecustomertype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[alerttypecustomertype](
	[CustomerTypeId] [nvarchar](50) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_alerttypecustomertype_CustomerTypeId] PRIMARY KEY CLUSTERED 
(
	[CustomerTypeId] ASC,
	[AlertTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[annualpercentagerate]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[annualpercentagerate](
	[id] [nvarchar](50) NOT NULL,
	[LoanType_id] [nvarchar](50) NULL,
	[APRValue] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_annualpercentagerate_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[app]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[app](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_app_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appaction]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appaction](
	[id] [nvarchar](50) NOT NULL,
	[App_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_appaction_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [appaction$appaction_unique_index] UNIQUE NONCLUSTERED 
(
	[App_id] ASC,
	[Action_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appchannel]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appchannel](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_appchannel_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[application]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[application](
	[id] [int] IDENTITY(3,1) NOT NULL,
	[OSType] [nvarchar](100) NULL,
	[OSversion] [decimal](10, 2) NULL,
	[BannerURL] [nvarchar](100) NULL,
	[VersionLink] [nvarchar](100) NULL,
	[currencyCode] [nvarchar](10) NULL,
	[BusinessDays] [int] NULL,
	[BankName] [nvarchar](45) NULL,
	[DistanceUnit] [nvarchar](10) NULL,
	[ocrApiKey] [nvarchar](100) NULL,
	[ocrSecretKey] [nvarchar](100) NULL,
	[facialLicenseString] [nvarchar](100) NULL,
	[facialLicenseServerUrl] [nvarchar](100) NULL,
	[appStoreLink] [nvarchar](200) NULL,
	[playStoreLink] [nvarchar](200) NULL,
	[ipadNativeAppLink] [nvarchar](200) NULL,
	[androidTabletNativeAppLink] [nvarchar](200) NULL,
	[isLanguageSelectionEnabled] [bit] NULL,
	[isBackEndCurencySymbolEnabled] [bit] NULL,
	[isCountryCodeEnabled] [bit] NULL,
	[isSortCodeVisible] [bit] NULL,
	[currenciesSupported] [nvarchar](1000) NULL,
	[deploymentGeography] [nvarchar](45) NULL,
	[isUTCDateFormattingEnabled] [bit] NULL,
	[language] [nvarchar](16) NULL,
	[defaultAccountType] [nvarchar](45) NULL,
	[fundingAmount] [nvarchar](45) NULL,
	[isBusinessBankingEnabled] [bit] NOT NULL,
	[isAccountAggregationEnabled] [nvarchar](45) NULL,
	[defaultCountryDialCode] [nvarchar](45) NULL,
	[isFeedbackEnabled] [bit] NULL,
	[noOfDaysForRatingFromProfile] [int] NULL,
	[noOfDaysForRatingFromTransactions] [int] NULL,
	[noOfDaysForAnotherAttemptForRating] [int] NULL,
	[maxtimesFeedbackperversion] [nvarchar](45) NULL,
	[majorVersionsForFeedback] [nvarchar](45) NULL,
	[bannerImageURL] [nvarchar](100) NULL,
	[desktopBannerImageURL] [nvarchar](100) NULL,
	[mobileBannerImageURL] [nvarchar](100) NULL,
	[viewMoreDBXLink] [nvarchar](100) NULL,
	[showAdsPostLogin] [bit] NOT NULL,
	[isAlertAccountIDLevel] [bit] NOT NULL,
	[isAccountTypeLevelAlerts] [nvarchar](45) NULL,
	[isprofileImageAvailable] [nvarchar](45) NULL,
	[cardStatementYears] [nvarchar](45) NULL,
	[bwFileTransactionsLimit] [int] NULL,
	[isAccountCentricCore] [bit] NOT NULL,
	[timeZoneOffset] [nvarchar](50) NOT NULL,
	[stopReasons] [nvarchar](400) NULL,
 CONSTRAINT [PK_application_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appmappingaid]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appmappingaid](
	[id] [int] NOT NULL,
	[Appid] [nvarchar](45) NULL,
	[Channel] [nvarchar](45) NULL,
	[aid] [nvarchar](45) NULL,
 CONSTRAINT [PK_appmappingaid_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appointment]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appointment](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[appointmentTime] [nvarchar](50) NULL,
	[appointmentWith] [nvarchar](50) NULL,
	[dob] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[phone] [nvarchar](50) NULL,
	[uid] [nvarchar](50) NULL,
	[branch_id] [int] NULL,
	[user_id] [int] NULL,
 CONSTRAINT [PK_appointment_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[approvalmatrix]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[approvalmatrix](
	[id] [bigint] IDENTITY(109,1) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[companyId] [nvarchar](50) NOT NULL,
	[actionId] [nvarchar](255) NOT NULL,
	[accountId] [nvarchar](50) NOT NULL,
	[approvalruleId] [nvarchar](50) NULL,
	[limitTypeId] [nvarchar](50) NOT NULL,
	[lowerlimit] [decimal](20, 2) NOT NULL,
	[upperlimit] [decimal](20, 2) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[invalid] [bit] NOT NULL,
 CONSTRAINT [PK_approvalmatrix_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[approvalrule]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[approvalrule](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[numberOfApprovals] [int] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_approvalrule_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appversion]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appversion](
	[id] [nvarchar](50) NOT NULL,
	[version] [nvarchar](50) NOT NULL,
	[versionType] [nvarchar](50) NOT NULL,
	[isSupported] [bit] NOT NULL,
	[app] [nvarchar](50) NOT NULL,
	[channel] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_appversion_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[appversiontype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[appversiontype](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_appversiontype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedalerthistory]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedalerthistory](
	[Id] [nvarchar](100) NOT NULL,
	[EventId] [int] NOT NULL,
	[AlertSubTypeId] [nvarchar](75) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[AlertStatusId] [nvarchar](50) NOT NULL,
	[Customer_Id] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NULL,
	[ChannelId] [nvarchar](50) NOT NULL,
	[Status] [nvarchar](50) NULL,
	[Subject] [nvarchar](255) NULL,
	[Message] [nvarchar](max) NULL,
	[SenderName] [nvarchar](255) NULL,
	[SenderEmail] [nvarchar](255) NULL,
	[ReferenceNumber] [nvarchar](100) NULL,
	[DispatchDate] [datetime] NOT NULL,
	[ErrorMessage] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedalerthistory_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedcustomerrequest]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedcustomerrequest](
	[id] [nvarchar](50) NOT NULL,
	[RequestCategory_id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Priority] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[RequestSubject] [nvarchar](50) NULL,
	[AssignedTo] [nvarchar](50) NULL,
	[Accountid] [nvarchar](50) NULL,
	[lastupdatedbycustomer] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedcustomerrequest_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedlead]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedlead](
	[id] [nvarchar](50) NOT NULL,
	[firstName] [nvarchar](50) NOT NULL,
	[middleName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[salutation] [nvarchar](45) NULL,
	[isCustomer] [bit] NOT NULL,
	[customerId] [nvarchar](50) NULL,
	[product_id] [nvarchar](50) NULL,
	[csr_id] [nvarchar](50) NULL,
	[status_id] [nvarchar](50) NULL,
	[countryCode] [nvarchar](10) NULL,
	[phoneNumber] [nvarchar](50) NULL,
	[extension] [nvarchar](10) NULL,
	[email] [nvarchar](50) NULL,
	[closureReason] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedlead_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedleadnote]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedleadnote](
	[id] [nvarchar](50) NOT NULL,
	[lead_Id] [nvarchar](50) NULL,
	[note] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedleadnote_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedmedia]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedmedia](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NOT NULL,
	[Type] [nvarchar](300) NOT NULL,
	[Description] [nvarchar](100) NOT NULL,
	[Url] [nvarchar](200) NULL,
	[Content] [nvarchar](max) NULL,
	[Size] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedmedia_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedmessageattachment]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedmessageattachment](
	[id] [nvarchar](50) NOT NULL,
	[RequestMessage_id] [nvarchar](50) NULL,
	[AttachmentType_id] [nvarchar](50) NULL,
	[Media_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedmessageattachment_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[archivedrequestmessage]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[archivedrequestmessage](
	[id] [nvarchar](50) NOT NULL,
	[CustomerRequest_id] [nvarchar](50) NULL,
	[MessageDescription] [nvarchar](max) NULL,
	[RepliedBy] [nvarchar](50) NULL,
	[RepliedBy_id] [nvarchar](50) NULL,
	[RepliedBy_Name] [nvarchar](100) NULL,
	[ReplySequence] [int] NULL,
	[IsRead] [nvarchar](10) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_archivedrequestmessage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[assetsresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[assetsresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[HasBankAssets] [bit] NULL,
	[HasRealEstateAssets] [bit] NULL,
	[HasGiftedAssets] [bit] NULL,
	[HasOtherAssets] [bit] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_assetsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [assetsresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[attachmenttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[attachmenttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_attachmenttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[attribute]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[attribute](
	[id] [nvarchar](50) NOT NULL,
	[endpoint_attribute_id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[attributetype] [nvarchar](12) NOT NULL,
	[options] [nvarchar](1000) NULL,
	[range] [nvarchar](100) NULL,
	[criterias] [nvarchar](1000) NOT NULL,
	[helptext] [nvarchar](1000) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_attribute_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[attributecriteria]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[attributecriteria](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_attributecriteria_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[attributeoption]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[attributeoption](
	[id] [nvarchar](50) NOT NULL,
	[endpoint_attributeoption_id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_attributeoption_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[attributetype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[attributetype](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_attributetype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[backendcertificate]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[backendcertificate](
	[id] [int] IDENTITY(2,1) NOT NULL,
	[BackendName] [varchar](45) NOT NULL,
	[CertName] [varchar](45) NOT NULL,
	[CertPrivateKey] [varchar](max) NOT NULL,
	[CertPublicKey] [varchar](max) NOT NULL,
	[createdby] [varchar](45) NULL,
	[modifiedby] [varchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_backendcertificate_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[backendidentifier]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[backendidentifier](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[sequenceNumber] [nvarchar](45) NULL,
	[BackendId] [nvarchar](45) NULL,
	[BackendType] [nvarchar](45) NULL,
	[identifier_name] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
 CONSTRAINT [PK_backendidentifier_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bank]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bank](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[Oauth2] [bit] NOT NULL,
	[IdentityProvider] [nvarchar](60) NULL,
 CONSTRAINT [PK_bank_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bankaccountsresponse]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bankaccountsresponse](
	[id] [nvarchar](50) NOT NULL,
	[AssetsResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[FinancialInstituteName] [nvarchar](100) NULL,
	[AccountType] [nvarchar](50) NULL,
	[AccountNumber] [nvarchar](100) NULL,
	[CashOrMarketValue] [int] NULL,
	[InternationalAccountFlag] [bit] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_bankaccountsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [bankaccountsresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bankbranch]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bankbranch](
	[id] [int] NOT NULL,
	[address1] [nvarchar](50) NULL,
	[address2] [nvarchar](50) NULL,
	[city] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[zipCode] [int] NULL,
	[phone] [nvarchar](50) NULL,
	[workingHours] [nvarchar](500) NULL,
	[services] [nvarchar](500) NULL,
	[latitude] [nvarchar](100) NULL,
	[longitude] [nvarchar](50) NULL,
	[status] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[Type_id] [int] NULL,
 CONSTRAINT [PK_bankbranch_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bankcommunication]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bankcommunication](
	[Type_id] [nvarchar](50) NOT NULL,
	[Bank_id] [nvarchar](50) NULL,
	[sequence] [int] NOT NULL,
	[value] [nvarchar](100) NULL,
	[extension] [nvarchar](50) NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_bankcommunication_Type_id] PRIMARY KEY CLUSTERED 
(
	[Type_id] ASC,
	[sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bankfortransfer]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bankfortransfer](
	[id] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](200) NOT NULL,
	[Description] [nvarchar](200) NULL,
	[Logo] [varbinary](max) NULL,
	[Url] [nvarchar](100) NULL,
	[GLAccount] [nvarchar](50) NOT NULL,
	[Address_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bankfortransfer_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bankservice]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bankservice](
	[id] [nvarchar](50) NOT NULL,
	[BankForTransfer_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL,
	[RoutingNumber] [nvarchar](50) NULL,
	[RoutingCode] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bankservice_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[banner]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[banner](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Category_id] [nvarchar](50) NULL,
	[Type_id] [nvarchar](50) NULL,
	[description] [nvarchar](max) NULL,
	[bannerImage] [nvarchar](max) NULL,
	[destinationURL] [nvarchar](max) NULL,
 CONSTRAINT [PK_banner_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[batchalertdefinition]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[batchalertdefinition](
	[alertType] [nvarchar](50) NOT NULL,
	[objectType] [nvarchar](50) NOT NULL,
	[columnName] [nvarchar](255) NULL,
	[condition] [nvarchar](25) NULL,
	[value] [nvarchar](255) NULL,
	[dueDateChecktype] [nvarchar](4) NULL,
	[dueDateParamName] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[updatedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_batchalertdefinition_alertType] PRIMARY KEY CLUSTERED 
(
	[alertType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[batchalertobject]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[batchalertobject](
	[objectType] [varchar](50) NOT NULL,
	[operationName] [varchar](100) NULL,
	[lastSyncTimestamp] [datetime] NULL,
 CONSTRAINT [PK_batchalertobject_objectType] PRIMARY KEY CLUSTERED 
(
	[objectType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbactedrequest]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbactedrequest](
	[approvalId] [bigint] IDENTITY(109,1) NOT NULL,
	[requestId] [bigint] NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[status] [nvarchar](50) NULL,
	[comments] [nvarchar](500) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[action] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bbactedrequest_approvalId] PRIMARY KEY CLUSTERED 
(
	[approvalId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbrequest]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbrequest](
	[requestId] [bigint] IDENTITY(44,1) NOT NULL,
	[transactionId] [nvarchar](50) NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[companyId] [nvarchar](50) NULL,
	[requiredSets] [int] NULL,
	[receivedSets] [int] NULL,
	[createdts] [datetime] NULL,
	[status] [nvarchar](50) NULL,
	[softDelete] [bit] NULL,
	[accountId] [nvarchar](45) NULL,
 CONSTRAINT [PK_bbrequest_requestId] PRIMARY KEY CLUSTERED 
(
	[requestId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtaxsubtype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtaxsubtype](
	[id] [int] IDENTITY(119,1) NOT NULL,
	[taxSubType] [nvarchar](300) NOT NULL,
	[taxType] [int] NOT NULL,
 CONSTRAINT [PK_bbtaxsubtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtaxtype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtaxtype](
	[id] [int] IDENTITY(47,1) NOT NULL,
	[taxType] [nvarchar](300) NOT NULL,
 CONSTRAINT [PK_bbtaxtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtemplate]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtemplate](
	[templateId] [int] IDENTITY(33,1) NOT NULL,
	[templateName] [nvarchar](45) NULL,
	[templateDescription] [nvarchar](45) NULL,
	[fromAccount] [nvarchar](45) NULL,
	[effectiveDate] [date] NULL,
	[maxAmount] [float] NULL,
	[requestId] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[status] [nvarchar](50) NULL,
	[updatedBy] [nvarchar](50) NULL,
	[updatedts] [datetime] NULL,
	[transactionType_id] [int] NULL,
	[templateType_id] [int] NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[templateRequestType_id] [int] NULL,
	[softDelete] [int] NOT NULL,
	[actedBy] [nvarchar](45) NULL,
	[totalAmount] [float] NULL,
	[featureActionId] [nvarchar](50) NULL,
 CONSTRAINT [PK_bbtemplate_templateId] PRIMARY KEY CLUSTERED 
(
	[templateId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtemplaterecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtemplaterecord](
	[templateRecord_id] [int] IDENTITY(54,1) NOT NULL,
	[record_Name] [nvarchar](45) NULL,
	[toAccountNumber] [nvarchar](45) NULL,
	[abatrcNumber] [nvarchar](45) NULL,
	[detail_id] [nvarchar](45) NULL,
	[amount] [float] NULL,
	[additionalInfo] [nvarchar](500) NULL,
	[ein] [nvarchar](45) NULL,
	[isZeroTaxDue] [tinyint] NULL,
	[template_id] [int] NULL,
	[taxType_id] [int] NULL,
	[templateRequestType_id] [int] NULL,
	[softDelete] [int] NULL,
	[toAccountType] [int] NULL,
 CONSTRAINT [PK_bbtemplaterecord_templateRecord_id] PRIMARY KEY CLUSTERED 
(
	[templateRecord_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtemplaterequesttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtemplaterequesttype](
	[templateRequestType_id] [int] IDENTITY(9,1) NOT NULL,
	[templateRequestTypeName] [nvarchar](45) NOT NULL,
	[transactionType_id] [int] NOT NULL,
 CONSTRAINT [PK_bbtemplaterequesttype_templateRequestType_id] PRIMARY KEY CLUSTERED 
(
	[templateRequestType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtemplatesubrecord]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtemplatesubrecord](
	[templateSubRecord_id] [int] IDENTITY(81,1) NOT NULL,
	[amount] [float] NULL,
	[templateRecord_id] [int] NOT NULL,
	[taxSubCategory_id] [int] NOT NULL,
	[softDelete] [int] NULL,
 CONSTRAINT [PK_bbtemplatesubrecord_templateSubRecord_id] PRIMARY KEY CLUSTERED 
(
	[templateSubRecord_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtemplatetype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtemplatetype](
	[templateType_id] [int] IDENTITY(3,1) NOT NULL,
	[templateTypeName] [nvarchar](45) NOT NULL,
 CONSTRAINT [PK_bbtemplatetype_templateType_id] PRIMARY KEY CLUSTERED 
(
	[templateType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bbtransactiontype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bbtransactiontype](
	[transactionType_id] [int] IDENTITY(4,1) NOT NULL,
	[transactionTypeName] [nvarchar](45) NOT NULL,
 CONSTRAINT [PK_bbtransactiontype_transactionType_id] PRIMARY KEY CLUSTERED 
(
	[transactionType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bill]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bill](
	[id] [int] IDENTITY(1346,1) NOT NULL,
	[Payee_id] [int] NULL,
	[Account_id] [bigint] NULL,
	[billDueDate] [date] NULL,
	[paidDate] [date] NULL,
	[description] [nvarchar](50) NULL,
	[dueAmount] [decimal](10, 2) NULL,
	[paidAmount] [decimal](10, 2) NULL,
	[balanceAmount] [decimal](10, 2) NULL,
	[minimumDue] [decimal](10, 2) NULL,
	[ebillURL] [nvarchar](max) NULL,
	[Status_id] [nvarchar](50) NULL,
	[statusDesc] [nvarchar](50) NULL,
	[billerMaster_id] [int] NULL,
	[billGeneratedDate] [date] NULL,
	[currencyCode] [nvarchar](45) NULL,
 CONSTRAINT [PK_bill_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[billercategory]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[billercategory](
	[id] [int] IDENTITY(5,1) NOT NULL,
	[categoryName] [nvarchar](100) NULL,
 CONSTRAINT [PK_billercategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[billercompany]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[billercompany](
	[id] [int] IDENTITY(3,1) NOT NULL,
	[companyName] [nvarchar](50) NULL,
 CONSTRAINT [PK_billercompany_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[billermaster]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[billermaster](
	[id] [int] IDENTITY(11,1) NOT NULL,
	[billerName] [nvarchar](100) NULL,
	[accountNumber] [nvarchar](50) NULL,
	[zipCode] [nvarchar](50) NULL,
	[mobileNumber] [nvarchar](50) NULL,
	[phoneNumber] [nvarchar](50) NULL,
	[address] [nvarchar](100) NULL,
	[relationshipNumber] [nvarchar](50) NULL,
	[policyNumber] [nvarchar](100) NULL,
	[city] [nvarchar](45) NULL,
	[state] [nvarchar](45) NULL,
	[billerCategoryId] [int] NULL,
	[ebillSupport] [bit] NULL,
 CONSTRAINT [PK_billermaster_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[billpaypayee]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[billpaypayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[cif] [nvarchar](50) NULL,
	[isBusinessPayee] [nvarchar](50) NULL,
 CONSTRAINT [PK_billpaypayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[billpaytransfers]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[billpaytransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[transactionCurrency] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](20) NULL,
	[billerId] [nvarchar](50) NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[requestId] [bigint] NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[amount] [float] NOT NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[description] [nvarchar](255) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencystartdate] [datetime] NULL,
	[frequencyenddate] [datetime] NULL,
	[numberOfRecurrences] [int] NULL,
	[scheduledDate] [datetime] NULL,
	[deliverBy] [datetime] NULL,
	[zipCode] [nvarchar](45) NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_billpaytransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[borrower]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[borrower](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Customer_id] [nvarchar](50) NULL,
	[FirstName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[Email] [nvarchar](50) NULL,
	[PhoneNumber] [nvarchar](50) NULL,
	[CreditType] [nvarchar](50) NULL,
	[BorrowerType] [nvarchar](50) NULL,
	[InviteChallenge] [nvarchar](50) NULL,
	[ApplicantType] [nvarchar](50) NULL,
	[IsAgreementAccepted] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_borrower_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [borrower$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[branchtype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[branchtype](
	[id] [int] IDENTITY(4,1) NOT NULL,
	[Type] [nvarchar](45) NULL,
 CONSTRAINT [PK_branchtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[browsersupport]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[browsersupport](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NULL,
	[icon] [nvarchar](max) NULL,
	[minimumVersionSupported] [nvarchar](50) NULL,
	[downloadLink] [nvarchar](500) NULL,
	[supportedOperatingSystems] [nvarchar](100) NULL,
	[supportType] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_browsersupport_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[browsersupportdisplaynametext]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[browsersupportdisplaynametext](
	[BrowserSupport_id] [nvarchar](50) NOT NULL,
	[Locale_id] [nvarchar](50) NOT NULL,
	[displayNameText] [nvarchar](500) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_browsersupportdisplaynametext_BrowserSupport_id] PRIMARY KEY CLUSTERED 
(
	[BrowserSupport_id] ASC,
	[Locale_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[browsersupporttype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[browsersupporttype](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_browsersupporttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[budget]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[budget](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ExpenseCategory_id] [int] NULL,
	[description] [nvarchar](50) NULL,
	[totalBudget] [decimal](10, 2) NULL,
	[usedBudget] [decimal](10, 2) NULL,
 CONSTRAINT [PK_budget_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwirefileformattype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwirefileformattype](
	[bulkWiresFileFormatTypeCode] [nvarchar](50) NOT NULL,
	[bulkWiresFileFormatTypeName] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bulkwirefileformattype_bulkWiresFileFormatTypeCode] PRIMARY KEY CLUSTERED 
(
	[bulkWiresFileFormatTypeCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwirefilelineitems]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwirefilelineitems](
	[bulkWireFileLineItemID] [int] IDENTITY(1,1) NOT NULL,
	[bulkWireFileID] [nvarchar](50) NOT NULL,
	[swiftCode] [nvarchar](50) NULL,
	[bulkWireTransferType] [nvarchar](50) NULL,
	[transactionType] [nvarchar](50) NULL,
	[internationalRoutingNumber] [nvarchar](50) NULL,
	[amount] [decimal](20, 2) NULL,
	[fromAccountNumber] [nvarchar](50) NULL,
	[note] [nvarchar](100) NULL,
	[recipientName] [nvarchar](100) NULL,
	[recipientAddressLine1] [nvarchar](100) NULL,
	[recipientAddressLine2] [nvarchar](100) NULL,
	[recipientCity] [nvarchar](100) NULL,
	[recipientState] [nvarchar](100) NULL,
	[recipientCountryName] [nvarchar](100) NULL,
	[recipientZipCode] [nvarchar](20) NULL,
	[recipientBankName] [nvarchar](100) NULL,
	[recipientBankAddress1] [nvarchar](100) NULL,
	[recipientBankAddress2] [nvarchar](100) NOT NULL,
	[recipientBankZipCode] [nvarchar](20) NULL,
	[recipientBankcity] [nvarchar](100) NULL,
	[recipientBankstate] [nvarchar](100) NULL,
	[currency] [nvarchar](10) NOT NULL,
	[accountNickname] [nvarchar](50) NULL,
	[recipientAccountNumber] [nvarchar](50) NULL,
	[routingNumber] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bulkwirefilelineitems_bulkWireFileLineItemID] PRIMARY KEY CLUSTERED 
(
	[bulkWireFileLineItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwirefiles]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwirefiles](
	[bulkWireFileID] [nvarchar](50) NOT NULL,
	[bulkWireFileName] [nvarchar](150) NOT NULL,
	[noOfTransactions] [int] NOT NULL,
	[noOfDomesticTransactions] [int] NOT NULL,
	[noOfInternationalTransactions] [int] NOT NULL,
	[fileFormatCode] [nvarchar](50) NOT NULL,
	[createdBy] [nvarchar](50) NOT NULL,
	[modifiedBy] [nvarchar](50) NOT NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[company_id] [nvarchar](50) NULL,
	[softdeleteflag] [bit] NULL,
	[bulkWireFileContents] [nvarchar](max) NOT NULL,
	[lastExecutedOn] [datetime] NULL,
 CONSTRAINT [PK_bulkwirefiles_bulkWireFileID] PRIMARY KEY CLUSTERED 
(
	[bulkWireFileID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [bulkwirefiles$bulkWireFileID_UNIQUE] UNIQUE NONCLUSTERED 
(
	[bulkWireFileID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwirefiletransactdetails]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwirefiletransactdetails](
	[bulkWireTransactionID] [int] IDENTITY(1,1) NOT NULL,
	[bulkWireFileID] [nvarchar](50) NOT NULL,
	[transactionDate] [datetime] NOT NULL,
	[initiatedBy] [nvarchar](50) NOT NULL,
	[totalCountOfTransactions] [int] NOT NULL,
	[totalCountOfDomesticTransactions] [int] NULL,
	[totalCountOfInternationalTransactions] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bulkwirefiletransactdetails_bulkWireTransactionID] PRIMARY KEY CLUSTERED 
(
	[bulkWireTransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwiresamplefile]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwiresamplefile](
	[bulkWireSampleFileID] [int] IDENTITY(3,1) NOT NULL,
	[bulkWireSampleFileName] [nvarchar](50) NOT NULL,
	[bulkWireSampleFileFormatCode] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[sampleFileContents] [nvarchar](max) NOT NULL,
	[fileCategory] [nvarchar](17) NOT NULL,
 CONSTRAINT [PK_bulkwiresamplefile_bulkWireSampleFileID] PRIMARY KEY CLUSTERED 
(
	[bulkWireSampleFileID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwiretemplate]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwiretemplate](
	[bulkWireTemplateID] [nvarchar](50) NOT NULL,
	[bulkWireTemplateName] [nvarchar](150) NOT NULL,
	[noOfTransactions] [int] NOT NULL,
	[noOfDomesticTransactions] [int] NOT NULL,
	[noOfInternationalTransactions] [int] NOT NULL,
	[createdBy] [nvarchar](50) NOT NULL,
	[modifiedBy] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[company_id] [nvarchar](50) NULL,
	[softdeleteflag] [bit] NULL,
	[lastExecutedOn] [datetime] NULL,
	[defaultFromAccount] [nvarchar](50) NOT NULL,
	[defaultCurrency] [nvarchar](10) NOT NULL,
	[deleteUniqueValue] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_bulkwiretemplate_bulkWireTemplateID] PRIMARY KEY CLUSTERED 
(
	[bulkWireTemplateID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [bulkwiretemplate$bulkWireTemplateID] UNIQUE NONCLUSTERED 
(
	[bulkWireTemplateID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [bulkwiretemplate$TemplateName_ForCompany] UNIQUE NONCLUSTERED 
(
	[bulkWireTemplateName] ASC,
	[company_id] ASC,
	[deleteUniqueValue] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [bulkwiretemplate$TemplateName_ForUser] UNIQUE NONCLUSTERED 
(
	[bulkWireTemplateName] ASC,
	[createdBy] ASC,
	[deleteUniqueValue] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwiretemplatelineitems]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwiretemplatelineitems](
	[bulkWireTemplateLineItemID] [int] IDENTITY(1,1) NOT NULL,
	[bulkWireTemplateID] [nvarchar](50) NOT NULL,
	[swiftCode] [nvarchar](50) NULL,
	[bulkWireTransferType] [nvarchar](50) NULL,
	[transactionType] [nvarchar](50) NULL,
	[internationalRoutingNumber] [nvarchar](50) NULL,
	[recipientName] [nvarchar](100) NULL,
	[recipientAddressLine1] [nvarchar](100) NULL,
	[recipientAddressLine2] [nvarchar](100) NULL,
	[recipientCity] [nvarchar](100) NULL,
	[recipientState] [nvarchar](100) NULL,
	[recipientCountryName] [nvarchar](100) NULL,
	[recipientZipCode] [nvarchar](20) NULL,
	[recipientBankName] [nvarchar](100) NULL,
	[recipientBankAddress1] [nvarchar](100) NULL,
	[recipientBankAddress2] [nvarchar](100) NULL,
	[recipientBankZipCode] [nvarchar](20) NULL,
	[recipientBankcity] [nvarchar](100) NULL,
	[recipientBankstate] [nvarchar](100) NULL,
	[accountNickname] [nvarchar](50) NULL,
	[recipientAccountNumber] [nvarchar](50) NULL,
	[routingNumber] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NOT NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[payeeId] [int] NULL,
	[templateRecipientCategory] [nvarchar](17) NOT NULL,
 CONSTRAINT [PK_bulkwiretemplatelineitems_bulkWireTemplateLineItemID] PRIMARY KEY CLUSTERED 
(
	[bulkWireTemplateLineItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[bulkwiretemplatetransactdetails]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails](
	[bulkWireTransactionID] [int] IDENTITY(1,1) NOT NULL,
	[bulkWireTemplateID] [nvarchar](50) NOT NULL,
	[transactionDate] [datetime] NOT NULL,
	[initiatedBy] [nvarchar](50) NOT NULL,
	[totalCountOfTransactions] [int] NOT NULL,
	[totalCountOfDomesticTransactions] [int] NULL,
	[totalCountOfInternationalTransactions] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_bulkwiretemplatetransactdetails_bulkWireTransactionID] PRIMARY KEY CLUSTERED 
(
	[bulkWireTransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[businessconfiguration]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[businessconfiguration](
	[id] [nvarchar](255) NOT NULL,
	[key] [nvarchar](50) NOT NULL,
	[displayname] [nvarchar](255) NOT NULL,
	[description] [nvarchar](max) NULL,
	[value] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_businessconfiguration_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [businessconfiguration$key] UNIQUE NONCLUSTERED 
(
	[key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[businesssignatory]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[businesssignatory](
	[BusinessType_id] [nvarchar](50) NOT NULL,
	[Signatory_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_businesssignatory_BusinessType_id] PRIMARY KEY CLUSTERED 
(
	[BusinessType_id] ASC,
	[Signatory_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[businesstype]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[businesstype](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NULL,
	[minAuthSignatory] [int] NULL,
	[maxAuthSignatory] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[businesstypecol] [nvarchar](45) NULL,
 CONSTRAINT [PK_businesstype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[campaign]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[campaign](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[status_id] [nvarchar](50) NOT NULL,
	[priority] [int] NOT NULL,
	[start_datetime] [datetime] NULL,
	[end_datetime] [datetime] NULL,
	[description] [nvarchar](150) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_campaign_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[campaigngroup]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[campaigngroup](
	[campaign_id] [nvarchar](50) NOT NULL,
	[group_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_campaigngroup_campaign_id] PRIMARY KEY CLUSTERED 
(
	[campaign_id] ASC,
	[group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[campaignplaceholder]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[campaignplaceholder](
	[id] [nvarchar](50) NOT NULL,
	[channel] [nvarchar](50) NOT NULL,
	[screen] [nvarchar](50) NOT NULL,
	[image_resolution] [nvarchar](10) NOT NULL,
	[image_scale] [nvarchar](10) NOT NULL,
	[image_size] [nvarchar](20) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_campaignplaceholder_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[campaignspecification]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[campaignspecification](
	[campaign_id] [nvarchar](50) NOT NULL,
	[campaignplaceholder_id] [nvarchar](50) NOT NULL,
	[image_url] [nvarchar](200) NOT NULL,
	[destination_url] [nvarchar](200) NULL,
	[display_count] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_campaignspecification_campaign_id] PRIMARY KEY CLUSTERED 
(
	[campaign_id] ASC,
	[campaignplaceholder_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[card]    Script Date: 7/28/2020 3:10:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[card](
	[Id] [int] IDENTITY(953,1) NOT NULL,
	[card_Status] [nvarchar](20) NOT NULL,
	[User_id] [nvarchar](50) NOT NULL,
	[expirationDate] [date] NOT NULL,
	[pinNumber] [nvarchar](10) NOT NULL,
	[reason] [nvarchar](100) NULL,
	[cardNumber] [bigint] NOT NULL,
	[cardType] [nvarchar](6) NULL,
	[action] [nvarchar](15) NULL,
	[account_id] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[creditLimit] [nvarchar](50) NULL,
	[availableCredit] [nvarchar](50) NULL,
	[serviceProvider] [nvarchar](50) NULL,
	[billingAddress] [nvarchar](50) NULL,
	[cardProductName] [nvarchar](50) NULL,
	[secondaryCardHolder] [nvarchar](50) NULL,
	[withdrawlLimit] [nvarchar](50) NULL,
	[isInternational] [bit] NULL,
	[bankName] [nvarchar](100) NULL,
	[cardHolderName] [nvarchar](20) NULL,
	[cvv] [int] NULL,
	[currentBalance] [nvarchar](45) NULL,
	[rewardsPoint] [nvarchar](45) NULL,
	[paymentDueDate] [datetime] NULL,
	[availableBalance] [nvarchar](45) NULL,
	[currencyCode] [nvarchar](45) NULL,
 CONSTRAINT [PK_card_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[cardaccountrequest]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[cardaccountrequest](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[CardAccountNumber] [nvarchar](50) NULL,
	[CardAccountName] [nvarchar](50) NULL,
	[AccountType] [nvarchar](50) NULL,
	[RequestType_id] [nvarchar](50) NULL,
	[RequestReason] [nvarchar](100) NULL,
	[Date] [datetime] NOT NULL,
	[Channel_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Address_id] [nvarchar](50) NULL,
	[Communication_id] [nvarchar](50) NULL,
	[AdditionalNotes] [nvarchar](200) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
 CONSTRAINT [PK_cardaccountrequest_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[cardaccountrequesttype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[cardaccountrequesttype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[Code] [nvarchar](50) NULL,
	[DisplayName] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_cardaccountrequesttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[cardstatements]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[cardstatements](
	[id] [int] IDENTITY(73,1) NOT NULL,
	[description] [varchar](100) NULL,
	[statementLink] [varchar](100) NULL,
	[Card_id] [bigint] NOT NULL,
	[month] [varchar](100) NULL,
 CONSTRAINT [PK_cardstatements_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[cardtransaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[cardtransaction](
	[cardNumber] [nvarchar](45) NOT NULL,
	[transactionDescription] [nvarchar](20) NULL,
	[transactionBalance] [decimal](11, 2) NULL,
	[transactionMerchantAddressName] [nvarchar](25) NULL,
	[transactionMerchantCity] [nvarchar](16) NULL,
	[merchantCategory] [nvarchar](16) NULL,
	[transactionStatus] [nvarchar](1) NULL,
	[transactionType] [nvarchar](1) NULL,
	[transactionCategory] [nvarchar](1) NULL,
	[transactionDetailDescription] [nvarchar](45) NULL,
	[transactionIndicator] [nvarchar](1) NULL,
	[transactionDate] [datetime] NULL,
	[transactionTime] [time](7) NULL,
	[transactionAmount] [decimal](18, 2) NULL,
	[transactionReferenceNumber] [nvarchar](22) NOT NULL,
	[transactionCurrencyCode] [nvarchar](3) NULL,
	[transactionExchangeRate] [decimal](16, 7) NULL,
	[exchangeCurrency] [nvarchar](3) NULL,
	[exchangeAmount] [decimal](18, 2) NULL,
	[transactionTaxIndicator] [nvarchar](1) NULL,
	[taxPercentage] [decimal](8, 5) NULL,
	[transactionTaxAmount] [decimal](18, 2) NULL,
	[transactionTerminalID] [nvarchar](20) NULL,
	[cardType] [nvarchar](45) NULL,
 CONSTRAINT [PK_cardtransaction_transactionReferenceNumber] PRIMARY KEY CLUSTERED 
(
	[transactionReferenceNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[category]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[category](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_category_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[channel]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[channel](
	[id] [nvarchar](50) NOT NULL,
	[status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_channel_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[channeltext]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[channeltext](
	[channelID] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[Description] [nvarchar](1000) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_channeltext_channelID] PRIMARY KEY CLUSTERED 
(
	[channelID] ASC,
	[LanguageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[check]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[check](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[frontImage] [nvarchar](500) NULL,
	[backImage] [nvarchar](500) NULL,
	[transactionId] [int] NOT NULL,
 CONSTRAINT [PK_check_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[checkorder]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[checkorder](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[account_id] [bigint] NOT NULL,
	[orderTime] [datetime] NOT NULL,
	[accountName] [nvarchar](50) NULL,
	[accountNickName] [nvarchar](50) NULL,
	[leafCount] [int] NULL,
	[status] [nvarchar](50) NULL,
	[name] [nvarchar](50) NULL,
	[postBoxNumber] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[zipCode] [nvarchar](50) NULL,
 CONSTRAINT [PK_checkorder_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[city]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[city](
	[id] [nvarchar](50) NOT NULL,
	[Region_id] [nvarchar](50) NULL,
	[Country_id] [nvarchar](50) NULL,
	[Name] [nvarchar](256) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_city_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[closurereason]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[closurereason](
	[id] [nvarchar](50) NOT NULL,
	[reason] [nvarchar](max) NULL,
	[status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_closurereason_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[communicationtemplate]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[communicationtemplate](
	[Id] [nvarchar](100) NOT NULL,
	[LanguageCode] [nvarchar](10) NULL,
	[AlertSubTypeId] [nvarchar](75) NULL,
	[ChannelID] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Name] [nvarchar](255) NULL,
	[Text] [nvarchar](max) NULL,
	[Subject] [nvarchar](255) NULL,
	[SenderName] [nvarchar](255) NULL,
	[SenderEmail] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_communicationtemplate_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[communicationtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[communicationtype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_communicationtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[compositeaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[compositeaction](
	[id] [nvarchar](50) NOT NULL,
	[Permission_id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](300) NULL,
	[Action_id] [nvarchar](255) NULL,
	[Feature_id] [nvarchar](255) NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_compositeaction_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[compositepermission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[compositepermission](
	[id] [nvarchar](50) NOT NULL,
	[Permission_id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](300) NULL,
	[Entitlement_id] [nvarchar](50) NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_compositepermission_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[configurationbundles]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[configurationbundles](
	[bundle_id] [varchar](50) NOT NULL,
	[bundle_name] [varchar](255) NOT NULL,
	[app_id] [varchar](255) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_configurationbundles_bundle_id] PRIMARY KEY CLUSTERED 
(
	[bundle_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [configurationbundles$bundle_name_UNIQUE] UNIQUE NONCLUSTERED 
(
	[bundle_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[configurationmasters]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[configurationmasters](
	[bundle_id] [nvarchar](255) NOT NULL,
	[app_id] [nvarchar](255) NULL,
	[channels] [nvarchar](255) NULL,
	[user_id] [nvarchar](255) NULL,
	[role] [nvarchar](255) NULL,
	[device_id] [nvarchar](255) NULL,
	[app_version] [nvarchar](255) NULL,
 CONSTRAINT [PK_configurationmasters_bundle_id] PRIMARY KEY CLUSTERED 
(
	[bundle_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[configurations]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[configurations](
	[configuration_id] [varchar](255) NOT NULL,
	[bundle_id] [varchar](50) NOT NULL,
	[config_type] [varchar](10) NOT NULL,
	[config_key] [varchar](600) NOT NULL,
	[description] [varchar](max) NULL,
	[config_value] [varchar](max) NULL,
	[target] [varchar](6) NOT NULL,
	[isPreLoginConfiguration] [bit] NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_configurations_configuration_id] PRIMARY KEY CLUSTERED 
(
	[configuration_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [configurations$UK_bundleId_configKey] UNIQUE NONCLUSTERED 
(
	[bundle_id] ASC,
	[config_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[contenttype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[contenttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_contenttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[coremembership]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[coremembership](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[MemberId] [nvarchar](50) NULL,
	[MemberType] [nvarchar](50) NULL,
	[IDType_id] [nvarchar](50) NULL,
	[IDValue] [nvarchar](50) NULL,
	[createdts] [nvarchar](50) NULL,
	[lastmodifiedts] [nvarchar](50) NULL,
 CONSTRAINT [PK_coremembership_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[country]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[country](
	[id] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](128) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_country_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[countrycode]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[countrycode](
	[id] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](250) NOT NULL,
	[ISDCode] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_countrycode_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[credentialchecker]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[credentialchecker](
	[id] [nvarchar](50) NOT NULL,
	[UserName] [nvarchar](45) NOT NULL,
	[linktype] [nvarchar](45) NOT NULL,
	[createdts] [datetime2](0) NOT NULL,
 CONSTRAINT [PK_credentialchecker_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[csrassistgrant]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[csrassistgrant](
	[id] [nvarchar](50) NOT NULL,
	[userId] [nvarchar](50) NOT NULL,
	[userRoleId] [nvarchar](50) NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[CustomerType] [nvarchar](50) NULL,
	[userName] [nvarchar](50) NULL,
	[internalUserName] [nvarchar](50) NULL,
	[isConsumed] [bit] NOT NULL,
	[appId] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[lastretrievedts] [datetime] NULL,
	[tokenconsumedts] [datetime] NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_csrassistgrant_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[currency]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[currency](
	[code] [nvarchar](10) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[symbol] [nvarchar](5) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_currency_code] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[custcompletedcampaign]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[custcompletedcampaign](
	[customer_id] [nvarchar](50) NOT NULL,
	[campaign_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_custcompletedcampaign_customer_id] PRIMARY KEY CLUSTERED 
(
	[customer_id] ASC,
	[campaign_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customer]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customer](
	[id] [nvarchar](50) NOT NULL,
	[Classification_id] [nvarchar](50) NULL,
	[CustomerType_id] [nvarchar](50) NOT NULL,
	[isCombinedUser] [bit] NULL,
	[FirstName] [nvarchar](200) NULL,
	[MiddleName] [nvarchar](50) NULL,
	[LastName] [nvarchar](200) NULL,
	[FullName] [nvarchar](150) NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[UserName] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](100) NULL,
	[unsuccessfulLoginAttempts] [int] NULL,
	[lockCount] [int] NULL,
	[Organization_Id] [nvarchar](50) NULL,
	[organizationType] [nvarchar](45) NULL,
	[Salutation] [nvarchar](50) NULL,
	[Gender] [nvarchar](50) NULL,
	[DateOfBirth] [date] NULL,
	[DrivingLicenseNumber] [nvarchar](50) NULL,
	[Ssn] [nvarchar](50) NULL,
	[Cvv] [nvarchar](50) NULL,
	[Token] [nvarchar](200) NULL,
	[Pin] [nvarchar](10) NULL,
	[PreferredContactMethod] [nvarchar](50) NULL,
	[PreferredContactTime] [nvarchar](50) NULL,
	[MaritalStatus_id] [nvarchar](50) NULL,
	[SpouseName] [nvarchar](50) NULL,
	[NoOfDependents] [nvarchar](50) NULL,
	[EmployementStatus_id] [nvarchar](50) NULL,
	[UserCompany] [nvarchar](50) NULL,
	[SecurityImage_id] [nvarchar](50) NULL,
	[Location_id] [nvarchar](50) NULL,
	[IsOlbAllowed] [bit] NOT NULL,
	[IsStaffMember] [bit] NOT NULL,
	[CountryCode] [nvarchar](50) NULL,
	[UserImage] [nvarchar](max) NULL,
	[UserImageURL] [nvarchar](200) NULL,
	[OlbEnrolmentStatus_id] [nvarchar](50) NULL,
	[Otp] [nvarchar](50) NULL,
	[PreferedOtpMethod] [nvarchar](50) NULL,
	[OtpGenaratedts] [datetime] NULL,
	[ValidDate] [datetime] NULL,
	[isUserAccountLocked] [bit] NOT NULL,
	[IsPinSet] [bit] NOT NULL,
	[IsEnrolledForOlb] [bit] NOT NULL,
	[IsAssistConsented] [bit] NOT NULL,
	[IsPhoneEnabled] [bit] NOT NULL,
	[IsEmailEnabled] [bit] NOT NULL,
	[isEnrolled] [bit] NOT NULL,
	[isSuperAdmin] [bit] NOT NULL,
	[CurrentLoginTime] [datetime] NULL,
	[Lastlogintime] [datetime] NULL,
	[IDType_id] [nvarchar](50) NULL,
	[IDValue] [nvarchar](50) NULL,
	[IDState] [nvarchar](50) NULL,
	[IDCountry] [nvarchar](50) NULL,
	[IDIssueDate] [date] NULL,
	[IDExpiryDate] [date] NULL,
	[IsCoreIdentityScope] [nvarchar](50) NULL,
	[Is_MemberEligibile] [bit] NULL,
	[MemberEligibilityData] [nvarchar](100) NULL,
	[Is_BBOA] [bit] NULL,
	[CreditUnionMemberSince] [date] NULL,
	[AtionProfile_id] [nvarchar](50) NULL,
	[RegistrationLink] [nvarchar](200) NULL,
	[RegLinkResendCount] [int] NULL,
	[RegLinkValidity] [datetime] NULL,
	[areDepositTermsAccepted] [nvarchar](50) NULL,
	[areAccountStatementTermsAccepted] [nvarchar](50) NULL,
	[areUserAlertsTurnedOn] [bit] NULL,
	[isBillPaySupported] [bit] NULL,
	[isBillPayActivated] [bit] NULL,
	[isP2PSupported] [bit] NULL,
	[isP2PActivated] [bit] NULL,
	[isWireTransferEligible] [bit] NULL,
	[isWireTransferActivated] [bit] NULL,
	[lockedOn] [datetime] NULL,
	[isEagreementSigned] [bit] NOT NULL,
	[MothersMaidenName] [nvarchar](50) NULL,
	[AddressValidationStatus] [nvarchar](50) NULL,
	[Product] [nvarchar](300) NULL,
	[EligbilityCriteria] [nvarchar](max) NULL,
	[Reason] [nvarchar](max) NULL,
	[ApplicantChannel] [nvarchar](50) NULL,
	[DocumentsSubmitted] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[Bank_id] [nvarchar](50) NULL,
	[Session_id] [nvarchar](50) NULL,
	[MaritalStatus] [nvarchar](50) NULL,
	[SpouseFirstName] [nvarchar](50) NULL,
	[SpouseLastName] [nvarchar](50) NULL,
	[EmploymentInfo] [nvarchar](50) NULL,
	[isEngageProvisioned] [bit] NOT NULL,
	[DefaultLanguage] [nvarchar](45) NULL,
	[isVIPCustomer] [bit] NULL,
	[isdcode] [nvarchar](10) NULL,
	[taxid] [nvarchar](10) NULL,
	[isSignatory] [bit] NULL,
	[sigtype] [nvarchar](50) NULL,
	[combinedUserId] [nvarchar](45) NULL,
 CONSTRAINT [PK_customer_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [customer$Username_UNIQUE] UNIQUE NONCLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeraccounts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeraccounts](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Membership_id] [nvarchar](50) NULL,
	[Account_id] [nvarchar](50) NULL,
	[Organization_id] [nvarchar](45) NULL,
	[AccountName] [nvarchar](50) NULL,
	[FavouriteStatus] [int] NOT NULL,
	[IsViewAllowed] [bit] NOT NULL,
	[IsDepositAllowed] [bit] NOT NULL,
	[IsWithdrawAllowed] [bit] NOT NULL,
	[IsOrganizationAccount] [bit] NOT NULL,
	[IsOrgAccountUnLinked] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_customeraccounts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeraction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeraction](
	[id] [nvarchar](50) NOT NULL,
	[RoleType_id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](255) NOT NULL,
	[Account_id] [nvarchar](50) NULL,
	[isAllowed] [bit] NOT NULL,
	[LimitType_id] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customeraction_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [customeraction$UNIQUE_customeractionlimit] UNIQUE NONCLUSTERED 
(
	[RoleType_id] ASC,
	[Customer_id] ASC,
	[Action_id] ASC,
	[Account_id] ASC,
	[LimitType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeraddress]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeraddress](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Address_id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[isPrimary] [bit] NOT NULL,
	[DurationOfStay] [nvarchar](50) NULL,
	[HomeOwnership] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customeraddress_Address_id] PRIMARY KEY CLUSTERED 
(
	[Address_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeralertcategorychannel]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeralertcategorychannel](
	[Customer_id] [nvarchar](50) NOT NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[ChannelId] [nvarchar](50) NOT NULL,
	[AccountId] [nvarchar](50) NOT NULL,
	[AccountType] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customeralertcategorychannel_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[AlertCategoryId] ASC,
	[ChannelId] ASC,
	[AccountId] ASC,
	[AccountType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeralertentitlement]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeralertentitlement](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Alert_id] [nvarchar](50) NOT NULL,
	[IsSmsActive] [bit] NOT NULL,
	[IsEmailActive] [bit] NOT NULL,
	[IsPushActive] [bit] NOT NULL,
	[Value] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customeralertentitlement_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Alert_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customeralertswitch]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customeralertswitch](
	[Customer_id] [nvarchar](50) NOT NULL,
	[AccountID] [nvarchar](50) NOT NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[AccountType] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customeralertswitch_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[AlertCategoryId] ASC,
	[AccountType] ASC,
	[AccountID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerapplication]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerapplication](
	[Customer_id] [nvarchar](50) NULL,
	[Party_id] [varchar](50) NULL,
	[CoreCustomer_id] [varchar](50) NULL,
	[ApplicationId] [varchar](50) NOT NULL,
	[ProductId] [nvarchar](200) NULL,
	[isTypeBusiness] [varchar](45) NULL,
	[ApplicationStatus] [varchar](50) NULL,
	[RequestKey] [varchar](45) NULL,
	[JSESSIONID] [varchar](45) NULL,
	[SaveChallengeAnswer] [varchar](45) NULL,
	[FundingStatus] [varchar](50) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerapplication_ApplicationId] PRIMARY KEY CLUSTERED 
(
	[ApplicationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerapprovalmatrix]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerapprovalmatrix](
	[id] [bigint] IDENTITY(109,1) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[approvalMatrixId] [bigint] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerapprovalmatrix_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerbusinesstype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerbusinesstype](
	[Customer_id] [nvarchar](50) NOT NULL,
	[BusinessType_id] [nvarchar](50) NOT NULL,
	[SignatoryType_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerbusinesstype_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[BusinessType_id] ASC,
	[SignatoryType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customercommunication]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customercommunication](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[isPrimary] [bit] NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[Value] [nvarchar](100) NULL,
	[Extension] [nvarchar](50) NULL,
	[Description] [nvarchar](50) NULL,
	[IsPreferredContactMethod] [bit] NULL,
	[PreferredContactTime] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[type] [nvarchar](50) NULL,
	[countryType] [nvarchar](50) NULL,
	[receivePromotions] [nvarchar](45) NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
 CONSTRAINT [PK_customercommunication_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerdevice]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerdevice](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[DeviceName] [nvarchar](50) NULL,
	[LastLoginTime] [datetime] NULL,
	[LastUsedIp] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[OperatingSystem] [nvarchar](50) NULL,
	[Channel_id] [nvarchar](50) NULL,
	[EnrollmentDate] [date] NULL,
	[appid] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
 CONSTRAINT [PK_customerdevice_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[Customer_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerdpdata]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerdpdata](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[CreditScore] [nvarchar](50) NULL,
	[EmploymentType] [nvarchar](50) NULL,
	[AnnualIncome] [nvarchar](50) NULL,
	[AccountBalance] [nvarchar](50) NULL,
	[Age] [nvarchar](50) NULL,
	[DebtToIncomeRatio] [nvarchar](50) NULL,
	[TimeofEmployment] [nvarchar](50) NULL,
	[State] [nvarchar](50) NULL,
	[PrequalifyScore] [nvarchar](50) NULL,
	[ActionProfile_id] [nvarchar](50) NULL,
	[Input1] [nvarchar](50) NULL,
	[Input2] [nvarchar](50) NULL,
	[Input3] [nvarchar](50) NULL,
	[Input4] [nvarchar](50) NULL,
	[Input5] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_customerdpdata_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerentitlement]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerentitlement](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Service_id] [nvarchar](50) NOT NULL,
	[MaxTransactionLimit] [decimal](20, 2) NULL,
	[MaxDailyLimit] [decimal](20, 2) NULL,
	[TransactionFee_id] [nvarchar](50) NULL,
	[TransactionLimit_id] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerentitlement_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Service_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerexpense]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerexpense](
	[Amount] [decimal](20, 2) NULL,
	[id] [nvarchar](50) NOT NULL,
	[Type] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_customerexpense_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerfile]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerfile](
	[id] [nvarchar](50) NOT NULL,
	[customerfileclob] [nvarchar](max) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerfile_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerflagstatus]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerflagstatus](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerflagstatus_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Status_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customergroup]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customergroup](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Group_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customergroup_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerimage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerimage](
	[Customer_id] [nvarchar](50) NOT NULL,
	[UserImage] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerimage_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customernote]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customernote](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Note] [nvarchar](1000) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customernote_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customernotification]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customernotification](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Notification_id] [nvarchar](50) NOT NULL,
	[IsRead] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customernotification_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Notification_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerpreference]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerpreference](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[DefaultAccountDeposit] [nvarchar](45) NULL,
	[DefaultAccountTransfers] [nvarchar](50) NULL,
	[DefaultModule_id] [nvarchar](50) NULL,
	[DefaultAccountPayments] [nvarchar](50) NULL,
	[DefaultAccountCardless] [nvarchar](50) NULL,
	[DefaultAccountBillPay] [nvarchar](50) NULL,
	[DefaultToAccountP2P] [nvarchar](50) NULL,
	[DefaultFromAccountP2P] [nvarchar](50) NULL,
	[DefaultAccountWire] [nvarchar](50) NULL,
	[areUserAlertsTurnedOn] [nvarchar](50) NULL,
	[areDepositTermsAccepted] [nvarchar](50) NULL,
	[areAccountStatementTermsAccepted] [nvarchar](50) NULL,
	[isBillPaySupported] [nvarchar](50) NULL,
	[isP2PSupported] [nvarchar](50) NULL,
	[isBillPayActivated] [nvarchar](50) NULL,
	[isP2PActivated] [nvarchar](50) NULL,
	[isWireTransferActivated] [nvarchar](50) NULL,
	[isWireTransferEligible] [nvarchar](50) NULL,
	[ShowBillPayFromAccPopup] [bit] NOT NULL,
	[PreferedOtpMethod] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerpreference_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerprequalifypackage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerprequalifypackage](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[PrequalifyPackage_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_customerprequalifypackage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerpreviouspasswords]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerpreviouspasswords](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[PwdSequence] [int] NULL,
	[Password] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[createdts] [nvarchar](50) NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerpreviouspasswords_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerproduct]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerproduct](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Product_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customerproduct_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerquerysectionstatus]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerquerysectionstatus](
	[id] [nvarchar](50) NOT NULL,
	[User_id] [varchar](45) NULL,
	[QueryResponse_id] [nvarchar](45) NULL,
	[QuerySection_id] [nvarchar](45) NULL,
	[Status] [nvarchar](45) NULL,
	[PercentageCompletion] [nvarchar](45) NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [nvarchar](45) NULL,
	[LastQuerySectionQuestion_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_customerquerysectionstatus_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerrequest]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerrequest](
	[id] [nvarchar](50) NOT NULL,
	[RequestCategory_id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Priority] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[RequestSubject] [nvarchar](1000) NULL,
	[AssignedTo] [nvarchar](50) NULL,
	[Accountid] [nvarchar](50) NULL,
	[lastupdatedbycustomer] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
 CONSTRAINT [PK_customerrequest_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customersecurityimages]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customersecurityimages](
	[Customer_id] [nvarchar](50) NOT NULL,
	[Image_id] [nvarchar](45) NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_customersecurityimages_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[Image_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customersecurityquestions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customersecurityquestions](
	[Customer_id] [nvarchar](50) NOT NULL,
	[SecurityQuestion_id] [nvarchar](50) NOT NULL,
	[CustomerAnswer] [nvarchar](250) NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
 CONSTRAINT [PK_customersecurityquestions_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[SecurityQuestion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customersegment]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customersegment](
	[id] [nvarchar](50) NOT NULL,
	[type] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customersegment_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customerservice]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customerservice](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](300) NULL,
	[Status_id] [nvarchar](50) NULL,
	[HasWeekendOperation] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[WorkSchedule_id] [nvarchar](50) NULL,
	[TransactionFee_id] [nvarchar](50) NULL,
	[TransactionLimit_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_customerservice_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customertermsandconditions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customertermsandconditions](
	[id] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[termsAndConditionsCode] [nvarchar](45) NOT NULL,
	[languageCode] [nvarchar](10) NOT NULL,
	[versionId] [nvarchar](45) NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[appId] [nvarchar](45) NULL,
	[channel] [nvarchar](45) NULL,
	[platform] [nvarchar](45) NULL,
	[browser] [nvarchar](45) NULL,
	[createdby] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_customertermsandconditions_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [customertermsandconditions$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customertype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customertype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customertype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customertypeconfig]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customertypeconfig](
	[id] [int] IDENTITY(26,1) NOT NULL,
	[CustomerType_id] [nvarchar](45) NULL,
	[Appid] [nvarchar](45) NULL,
	[AccessPermitted] [bit] NOT NULL,
 CONSTRAINT [PK_customertypeconfig_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customrole]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customrole](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[status_id] [nvarchar](50) NOT NULL,
	[organization_id] [nvarchar](50) NOT NULL,
	[parent_id] [nvarchar](50) NULL,
	[name] [nvarchar](50) NOT NULL,
	[description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customrole_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[customroleactionlimits]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[customroleactionlimits](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[customRole_id] [bigint] NOT NULL,
	[action_id] [nvarchar](255) NOT NULL,
	[account_id] [nvarchar](50) NULL,
	[isAllowed] [bit] NOT NULL,
	[limitType_id] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_customroleactionlimits_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dashboardalerts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dashboardalerts](
	[id] [nvarchar](50) NOT NULL,
	[Title] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[Type] [nvarchar](50) NOT NULL,
	[Priority] [nvarchar](50) NOT NULL,
	[created] [nvarchar](50) NULL,
 CONSTRAINT [PK_dashboardalerts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[datatype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[datatype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_datatype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dayschedule]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dayschedule](
	[id] [nvarchar](50) NOT NULL,
	[WorkSchedule_id] [nvarchar](50) NOT NULL,
	[WeekDayName] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](50) NULL,
	[StartTime] [time](7) NULL,
	[EndTime] [time](7) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dayschedule_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbpconfig]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbpconfig](
	[id] [int] IDENTITY(10,1) NOT NULL,
	[Module] [nvarchar](50) NULL,
	[FieldName] [nvarchar](50) NULL,
	[FieldValue] [nvarchar](200) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
 CONSTRAINT [PK_dbpconfig_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbxalertcategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbxalertcategory](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[accountLevel] [bit] NULL,
	[status_id] [nvarchar](50) NULL,
	[DisplaySequence] [tinyint] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dbxalertcategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbxalertcategorytext]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbxalertcategorytext](
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[DisplayName] [nvarchar](255) NULL,
	[Description] [nvarchar](1000) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dbxalertcategorytext_AlertCategoryId] PRIMARY KEY CLUSTERED 
(
	[AlertCategoryId] ASC,
	[LanguageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbxalerttype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbxalerttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[AlertCategoryId] [nvarchar](50) NOT NULL,
	[AttributeId] [nvarchar](50) NULL,
	[AlertConditionId] [nvarchar](25) NULL,
	[Value1] [nvarchar](255) NULL,
	[Value2] [nvarchar](255) NULL,
	[Status_id] [nvarchar](50) NULL,
	[IsGlobal] [bit] NULL,
	[DisplaySequence] [tinyint] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dbxalerttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbxalerttypetext]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbxalerttypetext](
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[DisplayName] [nvarchar](255) NULL,
	[Description] [nvarchar](1000) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dbxalerttypetext_AlertTypeId] PRIMARY KEY CLUSTERED 
(
	[AlertTypeId] ASC,
	[LanguageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dbxcustomeralertentitlement]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dbxcustomeralertentitlement](
	[Customer_id] [nvarchar](50) NOT NULL,
	[AlertTypeId] [nvarchar](50) NOT NULL,
	[AccountId] [nvarchar](50) NOT NULL,
	[AccountType] [nvarchar](50) NOT NULL,
	[Value1] [nvarchar](255) NULL,
	[Value2] [nvarchar](255) NULL,
	[LastEventPushed] [date] NULL,
	[Balance] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_dbxcustomeralertentitlement_Customer_id] PRIMARY KEY CLUSTERED 
(
	[Customer_id] ASC,
	[AlertTypeId] ASC,
	[AccountType] ASC,
	[AccountId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[decisionbatchrun]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[decisionbatchrun](
	[job_id] [nvarchar](50) NOT NULL,
	[completedRecordCounter] [nvarchar](50) NULL,
	[successflag] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_decisionbatchrun_job_id] PRIMARY KEY CLUSTERED 
(
	[job_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[decisionfailure]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[decisionfailure](
	[id] [int] IDENTITY(676,1) NOT NULL,
	[job_id] [nvarchar](50) NULL,
	[decision_id] [nvarchar](50) NULL,
	[baseAttributeName] [nvarchar](50) NULL,
	[baseAttributeValue] [nvarchar](50) NULL,
	[resultAttributeName] [nvarchar](50) NULL,
	[errmsg] [nvarchar](500) NULL,
	[exception] [nvarchar](500) NULL,
	[failureTriggerJob_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_decisionfailure_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[decisionresult]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[decisionresult](
	[id] [int] IDENTITY(16018,1) NOT NULL,
	[job_id] [nvarchar](50) NULL,
	[decision_id] [nvarchar](50) NULL,
	[baseAttributeName] [nvarchar](50) NULL,
	[baseAttributeValue] [nvarchar](50) NULL,
	[resultAttributeName] [nvarchar](50) NULL,
	[resultAttributeValue] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[errmsg] [nvarchar](500) NULL,
	[exception] [nvarchar](500) NULL,
 CONSTRAINT [PK_decisionresult_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[defaultcampaign]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[defaultcampaign](
	[name] [nvarchar](50) NOT NULL,
	[description] [nvarchar](150) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_defaultcampaign_name] PRIMARY KEY CLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[defaultcampaignspecification]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[defaultcampaignspecification](
	[campaignplaceholder_id] [nvarchar](50) NOT NULL,
	[image_index] [int] NOT NULL,
	[image_url] [nvarchar](200) NULL,
	[destination_url] [nvarchar](200) NULL,
	[display_count] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_defaultcampaignspecification_campaignplaceholder_id] PRIMARY KEY CLUSTERED 
(
	[campaignplaceholder_id] ASC,
	[image_index] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[demographicsresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[demographicsresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[EthinicityDontWish] [bit] NULL,
	[Ethinicity] [bit] NULL,
	[EthinicityOptions] [nvarchar](500) NULL,
	[GenderDontWish] [bit] NULL,
	[Gender] [nvarchar](50) NULL,
	[RaceDontWish] [bit] NULL,
	[RaceOptions] [nvarchar](500) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_demographicsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [demographicsresponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[deviceregistration]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[deviceregistration](
	[id] [int] IDENTITY(1263,1) NOT NULL,
	[User_id] [int] NULL,
	[Status_id] [nvarchar](50) NULL,
	[DeviceId] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_deviceregistration_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [deviceregistration$unique_index] UNIQUE NONCLUSTERED 
(
	[User_id] ASC,
	[DeviceId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[digitalprofile]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[digitalprofile](
	[Id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[CreditScore] [int] NULL,
	[NumberOfInquiries_6M] [int] NULL,
	[NumberOfInquiries_12M] [int] NULL,
	[NumberOfInquiries_24M] [int] NULL,
	[TotalRevolvingOpenToBuyBalance] [decimal](10, 2) NULL,
	[UtilizationPercentOfRevolvingTrades] [nvarchar](50) NULL,
	[SinceRecentDelinquency_M] [int] NULL,
	[TotalNumberOfDerogatory] [int] NULL,
	[SinceRecentlyFiledCollection_M] [int] NULL,
	[TotalNumberOfTrades] [int] NULL,
	[TotalNumberOfActiveTrades] [int] NULL,
	[NumberOfTradesOpened_24M] [int] NULL,
	[NumberOfTradeswithUtilization] [int] NULL,
	[OldestOpenPersonalFinanceTrade_M] [int] NULL,
	[LoanToIncomeRatio] [decimal](10, 2) NULL,
	[NumberOfLoanAapplications_24M] [nvarchar](50) NULL,
	[DebtToIncomeRatio] [decimal](10, 2) NULL,
	[PrequalifyScore] [int] NULL,
	[YearsOfMembership] [int] NULL,
	[AccountsBalance] [decimal](10, 2) NULL,
	[Age] [nvarchar](50) NULL,
	[City] [nvarchar](50) NULL,
	[State] [nvarchar](50) NULL,
	[ZipCode] [int] NULL,
	[DurationOfStay] [decimal](10, 2) NULL,
	[HomeOwnership] [nvarchar](50) NULL,
	[GrossMonthlyIncome] [decimal](10, 2) NULL,
	[AnnualIncome] [decimal](12, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_digitalprofile_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[disclaimer]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[disclaimer](
	[id] [varchar](50) NOT NULL,
	[App_id] [varchar](50) NULL,
	[ModuleName] [varchar](50) NULL,
	[DisclaimerName] [varchar](50) NULL,
	[DisclaimerText] [varchar](max) NULL,
	[DisclaimerUrl] [varchar](200) NULL,
	[ContentType] [varchar](5) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_disclaimer_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dmaddinteractions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dmaddinteractions](
	[id] [int] IDENTITY(7,1) NOT NULL,
	[navigationType] [nvarchar](50) NOT NULL,
	[navigationURL] [nvarchar](200) NOT NULL,
	[navigationId] [nvarchar](200) NOT NULL,
	[text] [nvarchar](200) NOT NULL,
	[colour] [nvarchar](200) NOT NULL,
	[dm_add_id] [int] NOT NULL,
	[textcolor] [nvarchar](50) NULL,
	[buttonType] [nvarchar](200) NOT NULL,
 CONSTRAINT [PK_dmaddinteractions_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[dmadvertisements]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[dmadvertisements](
	[id] [int] IDENTITY(8,1) NOT NULL,
	[description] [nvarchar](1000) NULL,
	[imageURL] [nvarchar](200) NULL,
	[adType] [nvarchar](50) NULL,
	[navigationType] [nvarchar](50) NULL,
	[navigationURL] [nvarchar](200) NULL,
	[visible] [bit] NULL,
	[model] [nvarchar](50) NULL,
	[flowPosition] [nvarchar](100) NULL,
	[adTitle] [nvarchar](1000) NULL,
 CONSTRAINT [PK_dmadvertisements_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eligibilitycriteria]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eligibilitycriteria](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](max) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_eligibilitycriteria_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[emailtemplates]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[emailtemplates](
	[id] [int] IDENTITY(22,1) NOT NULL,
	[TemplateName] [nvarchar](100) NULL,
	[TemplateText] [nvarchar](max) NULL,
	[Subject] [nvarchar](500) NULL,
	[SenderName] [nvarchar](500) NULL,
	[SenderEmail] [nvarchar](500) NULL,
	[AlertChannel] [nvarchar](50) NULL,
	[AlertLanguageCode] [nvarchar](50) NULL,
	[Alert_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_emailtemplates_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[employementdetails]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[employementdetails](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[EmploymentType] [nvarchar](50) NULL,
	[CurrentEmployer] [nvarchar](50) NULL,
	[Designation] [nvarchar](50) NULL,
	[PayPeriod] [nvarchar](50) NULL,
	[GrossIncome] [decimal](10, 2) NULL,
	[WeekWorkingHours] [nvarchar](50) NULL,
	[EmploymentStartDate] [date] NULL,
	[PreviousEmployer] [nvarchar](50) NULL,
	[PreviousDesignation] [nvarchar](50) NULL,
	[OtherEmployementType] [nvarchar](50) NULL,
	[OtherEmployementDescription] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_employementdetails_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[employmentdetailsresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[employmentdetailsresponse](
	[id] [nvarchar](50) NOT NULL,
	[IncomeResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[EmploymentDetailType_id] [nvarchar](50) NOT NULL,
	[EmploymentType_id] [nvarchar](50) NULL,
	[ProvideEmploymentDetails] [bit] NULL,
	[EmployerName] [nvarchar](50) NULL,
	[EmployerAddressLine1] [nvarchar](100) NULL,
	[EmployerAddressLine2] [nvarchar](100) NULL,
	[EmployerAddressCity] [nvarchar](50) NULL,
	[EmployerAddressState] [nvarchar](50) NULL,
	[EmployerAddressCountry] [nvarchar](50) NULL,
	[EmployerAddressZipCode] [int] NULL,
	[EmployerPhoneNumber] [nvarchar](15) NULL,
	[TotalGrossIncome] [decimal](10, 2) NULL,
	[EmployeeDesignation] [nvarchar](50) NULL,
	[BusinessShare_id] [nvarchar](50) NULL,
	[BusinessMonthlyLoss] [decimal](10, 2) NULL,
	[ProfessionStartDate] [date] NULL,
	[LineOfWorkDuration] [nvarchar](10) NULL,
	[IsOtherParty] [bit] NULL,
	[PrevStartDate] [date] NULL,
	[PrevEndDate] [date] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_employmentdetailsresponse_IncomeResponse_id] PRIMARY KEY CLUSTERED 
(
	[IncomeResponse_id] ASC,
	[QueryResponse_id] ASC,
	[Borrower_id] ASC,
	[EmploymentDetailType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [employmentdetailsresponse$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[entitystatus]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[entitystatus](
	[Entity_id] [nvarchar](50) NOT NULL,
	[Status] [nvarchar](50) NULL,
 CONSTRAINT [PK_entitystatus_Entity_id] PRIMARY KEY CLUSTERED 
(
	[Entity_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[errorstatuscode]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[errorstatuscode](
	[SNo] [nvarchar](10) NULL,
	[Opstatus] [nvarchar](50) NULL,
	[HttpStatusCode] [nvarchar](50) NULL,
	[ErrorMsg] [nvarchar](200) NULL,
	[Remarks] [nvarchar](100) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[event]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[event](
	[Event_id] [int] IDENTITY(2,1) NOT NULL,
	[EventType] [nvarchar](50) NOT NULL,
	[EventSubType] [nvarchar](75) NULL,
	[Status_id] [nvarchar](50) NULL,
	[EventData] [nvarchar](max) NULL,
	[OtherData] [nvarchar](max) NULL,
	[IsProcessed] [bit] NOT NULL,
	[Producer] [nvarchar](255) NOT NULL,
	[PreProcessorResult] [nvarchar](50) NULL,
	[PostProcessorResult] [nvarchar](50) NULL,
	[Session] [nvarchar](max) NULL,
	[Timestamp] [datetime2](0) NULL,
 CONSTRAINT [PK_event_Event_id] PRIMARY KEY CLUSTERED 
(
	[Event_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventactivitytype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventactivitytype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](255) NULL,
	[createdby] [nvarchar](255) NULL,
	[modifiedby] [nvarchar](255) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_eventactivitytype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventconsumer]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventconsumer](
	[ServiceId] [nvarchar](50) NOT NULL,
	[OperationId] [nvarchar](50) NOT NULL,
	[BatchLimit] [int] NOT NULL,
	[LastEventId] [int] NOT NULL,
 CONSTRAINT [PK_eventconsumer_ServiceId] PRIMARY KEY CLUSTERED 
(
	[ServiceId] ASC,
	[OperationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventconsumertypes]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventconsumertypes](
	[ServiceId] [nvarchar](50) NOT NULL,
	[OperationId] [nvarchar](50) NOT NULL,
	[EventType] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_eventconsumertypes_ServiceId] PRIMARY KEY CLUSTERED 
(
	[ServiceId] ASC,
	[OperationId] ASC,
	[EventType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventsubtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventsubtype](
	[id] [nvarchar](75) NOT NULL,
	[eventtypeid] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[Description] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_eventsubtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[eventtypeid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventtopicconfiguration]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventtopicconfiguration](
	[eventCode] [nvarchar](100) NOT NULL,
	[topic] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_eventtopicconfiguration_eventCode] PRIMARY KEY CLUSTERED 
(
	[eventCode] ASC,
	[topic] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[eventtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[eventtype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](255) NOT NULL,
	[ActivityType] [nvarchar](50) NULL,
	[Description] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_eventtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[exchangerates]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[exchangerates](
	[id] [int] IDENTITY(7,1) NOT NULL,
	[currency] [nvarchar](50) NULL,
	[toCurrency] [nvarchar](50) NULL,
	[currencyType] [nvarchar](50) NULL,
	[exchangeRate] [decimal](10, 4) NULL,
 CONSTRAINT [PK_exchangerates_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[expensecategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[expensecategory](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](50) NULL,
	[isUndefined] [bit] NULL,
 CONSTRAINT [PK_expensecategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[expenseperiod]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[expenseperiod](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](50) NULL,
	[startDate] [date] NULL,
	[endDate] [date] NULL,
	[amount] [decimal](20, 2) NULL,
 CONSTRAINT [PK_expenseperiod_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[expensesheaderresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[expensesheaderresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[HasExpenses] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_expensesheaderresponse_QueryResponse_id] PRIMARY KEY CLUSTERED 
(
	[QueryResponse_id] ASC,
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [expensesheaderresponse$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[expensesresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[expensesresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[ExpensesHeaderResponse_id] [nvarchar](50) NULL,
	[Sequence] [int] IDENTITY(8,1) NOT NULL,
	[ExpenseType] [nvarchar](50) NULL,
	[MonthlyPayment] [decimal](10, 0) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_expensesresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [expensesresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[externalaccount]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[externalaccount](
	[Id] [int] IDENTITY(1720,1) NOT NULL,
	[User_id] [nvarchar](50) NULL,
	[organizationId] [nvarchar](45) NULL,
	[Bank_id] [nvarchar](50) NULL,
	[nickName] [nvarchar](100) NULL,
	[firstName] [nvarchar](100) NULL,
	[lastName] [nvarchar](100) NULL,
	[routingNumber] [nvarchar](30) NULL,
	[accountNumber] [nvarchar](45) NULL,
	[accountType] [nvarchar](45) NULL,
	[notes] [nvarchar](100) NULL,
	[countryName] [nvarchar](100) NULL,
	[swiftCode] [nvarchar](45) NULL,
	[user_Account] [nvarchar](100) NULL,
	[beneficiaryName] [nvarchar](100) NULL,
	[isInternationalAccount] [bit] NULL,
	[bankName] [nvarchar](50) NULL,
	[isSameBankAccount] [bit] NULL,
	[softDelete] [bit] NOT NULL,
	[isVerified] [bit] NULL,
	[createdOn] [date] NULL,
	[externalaccount] [varbinary](255) NULL,
	[IBAN] [nvarchar](45) NULL,
	[sortCode] [nvarchar](45) NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
	[phoneNumber] [nvarchar](15) NULL,
	[phoneExtension] [nvarchar](10) NULL,
	[addressNickName] [nvarchar](45) NULL,
	[addressLine1] [nvarchar](200) NULL,
	[city] [nvarchar](45) NULL,
	[zipcode] [nvarchar](45) NULL,
	[country] [nvarchar](45) NULL,
	[externalaccountcol] [nvarchar](45) NULL,
 CONSTRAINT [PK_externalaccount_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[externalbank]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[externalbank](
	[id] [nvarchar](50) NOT NULL,
	[BankId] [nvarchar](50) NULL,
	[Scheme] [nvarchar](5) NULL,
	[Address] [nvarchar](45) NULL,
	[BankName] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime2](6) NULL,
	[lastmodifiedts] [datetime2](6) NULL,
	[synctimestamp] [datetime2](6) NULL,
	[softdeleteflag] [bit] NOT NULL,
	[IdentityProvider] [nvarchar](60) NULL,
	[Oauth2] [bit] NULL,
	[logo] [nvarchar](200) NULL,
 CONSTRAINT [PK_externalbank_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[externalbankidentity]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[externalbankidentity](
	[id] [nvarchar](50) NOT NULL,
	[ExternalBank_id] [nvarchar](50) NULL,
	[MainUser_id] [nvarchar](50) NOT NULL,
	[User_id] [nvarchar](50) NULL,
	[Password] [nvarchar](50) NULL,
	[SessionToken] [nvarchar](200) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime2](6) NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_externalbankidentity_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[facility]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[facility](
	[id] [nvarchar](50) NOT NULL,
	[code] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[description] [nvarchar](1000) NOT NULL,
	[facilitytype] [nvarchar](10) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_facility_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [facility$code] UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[faqcategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[faqcategory](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_faqcategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[faqs]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[faqs](
	[id] [nvarchar](50) NOT NULL,
	[Channel_id] [nvarchar](50) NULL,
	[QuestionCode] [nvarchar](50) NOT NULL,
	[Question] [nvarchar](1000) NULL,
	[Answer] [nvarchar](4000) NULL,
	[Status_id] [nvarchar](50) NULL,
	[FaqCategory_Id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_faqs_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [faqs$QuestionCode_UNIQUE] UNIQUE NONCLUSTERED 
(
	[QuestionCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[feature]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[feature](
	[id] [nvarchar](255) NOT NULL,
	[App_id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[description] [nvarchar](400) NULL,
	[Type_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Service_Fee] [decimal](20, 2) NULL,
	[DisplaySequence] [int] NULL,
	[isPrimary] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_feature_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featureaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featureaction](
	[id] [nvarchar](255) NOT NULL,
	[Feature_id] [nvarchar](255) NULL,
	[App_id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[name] [nvarchar](100) NULL,
	[description] [nvarchar](300) NULL,
	[isAccountLevel] [bit] NOT NULL,
	[isMFAApplicable] [bit] NOT NULL,
	[MFA_id] [nvarchar](50) NULL,
	[TermsAndConditions_id] [nvarchar](50) NULL,
	[notes] [nvarchar](2000) NULL,
	[isPrimary] [bit] NOT NULL,
	[DisplaySequence] [int] NULL,
	[dependency] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_featureaction_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [featureaction$featureaction_unique_index] UNIQUE NONCLUSTERED 
(
	[App_id] ASC,
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featureactionroletype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featureactionroletype](
	[RoleType_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](255) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_featureactionroletype_RoleType_id] PRIMARY KEY CLUSTERED 
(
	[RoleType_id] ASC,
	[Action_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featuredisplaynamedescription]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featuredisplaynamedescription](
	[Feature_id] [nvarchar](255) NOT NULL,
	[Locale_id] [nvarchar](50) NOT NULL,
	[displayName] [nvarchar](max) NOT NULL,
	[displayDescription] [nvarchar](max) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_featuredisplaynamedescription_Feature_id] PRIMARY KEY CLUSTERED 
(
	[Feature_id] ASC,
	[Locale_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featurepreferences]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featurepreferences](
	[id] [nvarchar](50) NULL,
	[FeatureName] [nvarchar](50) NULL,
	[IsAddAllowed] [nvarchar](50) NULL,
	[IsEditAllowed] [nvarchar](50) NULL,
	[IsDeleteAllowed] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [nvarchar](50) NULL,
	[lastmodifiedts] [nvarchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featureroletype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featureroletype](
	[RoleType_id] [nvarchar](50) NOT NULL,
	[Feature_id] [nvarchar](255) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_featureroletype_RoleType_id] PRIMARY KEY CLUSTERED 
(
	[RoleType_id] ASC,
	[Feature_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featureservices]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featureservices](
	[id] [nvarchar](50) NULL,
	[FeaturePrefernce_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[featuretype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[featuretype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_featuretype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[feedback]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[feedback](
	[id] [int] IDENTITY(7,1) NOT NULL,
	[user_id] [nvarchar](50) NULL,
	[rating] [float] NULL,
	[featureRequest] [nvarchar](1000) NULL,
	[description] [nvarchar](1000) NULL,
	[likeMost] [nvarchar](100) NULL,
	[improvement] [nvarchar](500) NULL,
 CONSTRAINT [PK_feedback_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[feedbackstatus]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[feedbackstatus](
	[id] [nvarchar](50) NOT NULL,
	[UserName] [nvarchar](50) NULL,
	[feedbackID] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[status] [bit] NULL,
	[deviceID] [nvarchar](50) NULL,
	[customerID] [nvarchar](50) NULL,
 CONSTRAINT [PK_feedbackstatus_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[flyway_schema_history]    Script Date: 7/28/2020 3:10:10 PM ******/

/****** Object:  Table [${dbxschemaname}].[frequencytype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[frequencytype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_frequencytype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[giftsandgrantsresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[giftsandgrantsresponse](
	[id] [nvarchar](50) NOT NULL,
	[AssetsResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[AssetType] [nvarchar](50) NULL,
	[AssetSource] [nvarchar](50) NULL,
	[CashOrMarketValue] [int] NULL,
	[DepositedStatus] [bit] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_giftsandgrantsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [giftsandgrantsresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[globalalert]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[globalalert](
	[id] [nvarchar](255) NOT NULL,
	[AlertSubTypeId] [nvarchar](255) NULL,
	[AlertAttributeId] [nvarchar](255) NULL,
	[AlertConditionId] [nvarchar](255) NULL,
	[Value1] [nvarchar](255) NULL,
	[Value2] [nvarchar](255) NULL,
	[Frequency] [nvarchar](255) NULL,
	[DaysBeforeReminder] [nvarchar](255) NULL,
	[isUserCustomizable] [nvarchar](255) NULL,
	[IsSmsActive] [nvarchar](255) NULL,
	[IsEmailActive] [nvarchar](255) NULL,
	[IsPushActive] [nvarchar](255) NULL,
	[createdby] [nvarchar](255) NULL,
	[modifiedby] [nvarchar](255) NULL,
	[createdts] [nvarchar](255) NULL,
	[lastmodifiedts] [nvarchar](255) NULL,
	[synctimestamp] [nvarchar](255) NULL,
	[softdeleteflag] [nvarchar](255) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[groupactionlimit]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[groupactionlimit](
	[id] [nvarchar](50) NOT NULL,
	[Group_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](255) NOT NULL,
	[LimitType_id] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_groupactionlimit_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [groupactionlimit$UNIQUE_groupactionlimit] UNIQUE NONCLUSTERED 
(
	[Group_id] ASC,
	[Action_id] ASC,
	[LimitType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[groupattribute]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[groupattribute](
	[group_id] [nvarchar](50) NOT NULL,
	[admin_attributes] [nvarchar](max) NULL,
	[endpoint_model_urls] [nvarchar](max) NULL,
	[customer_count] [int] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_groupattribute_group_id] PRIMARY KEY CLUSTERED 
(
	[group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[groupbusinesstype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[groupbusinesstype](
	[Group_id] [nvarchar](50) NOT NULL,
	[BusinessType_id] [nvarchar](255) NOT NULL,
	[isDefaultGroup] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_groupbusinesstype_Group_id] PRIMARY KEY CLUSTERED 
(
	[Group_id] ASC,
	[BusinessType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[groupentitlement]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[groupentitlement](
	[Group_id] [nvarchar](50) NOT NULL,
	[Service_id] [nvarchar](50) NOT NULL,
	[TransactionFee_id] [nvarchar](50) NULL,
	[TransactionLimit_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_groupentitlement_Group_id] PRIMARY KEY CLUSTERED 
(
	[Group_id] ASC,
	[Service_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[holidays]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[holidays](
	[id] [int] IDENTITY(216,1) NOT NULL,
	[holidayDate] [datetime] NULL,
	[createdOn] [datetime] NULL,
	[updatedOn] [datetime] NULL,
	[createdBy] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_holidays_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[iban]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[iban](
	[id] [int] NOT NULL,
	[IBAN] [nvarchar](45) NULL,
	[bankName] [nvarchar](45) NULL,
 CONSTRAINT [PK_iban_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[idmconfiguration]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[idmconfiguration](
	[id] [nvarchar](10) NOT NULL,
	[IDMKey] [nvarchar](45) NULL,
	[IDMValue] [nvarchar](45) NULL,
 CONSTRAINT [PK_idmconfiguration_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[idtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[idtype](
	[IDType] [nvarchar](50) NOT NULL,
	[IDName] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_idtype_IDType] PRIMARY KEY CLUSTERED 
(
	[IDType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[incomedistributionresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[incomedistributionresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[EmploymentDetailsResponse_id] [nvarchar](50) NULL,
	[IncomeDetail_id] [nvarchar](50) NULL,
	[PayPeriod_id] [nvarchar](50) NULL,
	[Amount] [decimal](10, 2) NULL,
	[WorkingHours] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_incomedistributionresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[incomeresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[incomeresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[HasAdditionalEmployment] [bit] NULL,
	[HasPreviousEmployment] [bit] NULL,
	[HasOtherSourcesOfIncome] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_incomeresponse_QueryResponse_id] PRIMARY KEY CLUSTERED 
(
	[QueryResponse_id] ASC,
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [incomeresponse$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[informationcontent]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[informationcontent](
	[id] [int] IDENTITY(5,1) NOT NULL,
	[informationType] [nvarchar](50) NULL,
	[informationContent] [nvarchar](max) NULL,
 CONSTRAINT [PK_informationcontent_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[interbankfundtransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[interbankfundtransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[requestId] [bigint] NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[numberOfRecurrences] [int] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[transactionCurrency] [nvarchar](50) NULL,
	[fromAccountCurrency] [nvarchar](50) NULL,
	[scheduledDate] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[beneficiarycountry] [nvarchar](50) NULL,
	[beneficiaryZipcode] [nvarchar](50) NULL,
	[beneficiaryCity] [nvarchar](50) NULL,
	[beneficiaryAddressLine1] [nvarchar](200) NULL,
	[beneficiaryAddressNickName] [nvarchar](50) NULL,
	[feeAmount] [nvarchar](50) NULL,
	[paymentType] [nvarchar](50) NULL,
	[paidBy] [nvarchar](50) NULL,
	[beneficiaryName] [nvarchar](50) NULL,
	[feeCurrency] [nvarchar](50) NULL,
	[bankId] [nvarchar](50) NULL,
	[bankName] [nvarchar](50) NULL,
	[bicCode] [nvarchar](50) NULL,
	[swiftCode] [nvarchar](50) NULL,
	[iban] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[pdf] [varbinary](max) NULL,
 CONSTRAINT [PK_interbankfundtransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[interbankpayee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[interbankpayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[cif] [nvarchar](50) NULL,
	[isBusinessPayee] [nvarchar](50) NULL,
 CONSTRAINT [PK_interbankpayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[interestrate]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[interestrate](
	[id] [nvarchar](50) NOT NULL,
	[LoanProduct_id] [nvarchar](50) NULL,
	[Name] [nvarchar](100) NOT NULL,
	[RateValue] [decimal](5, 2) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_interestrate_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[interestrates]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[interestrates](
	[id] [int] IDENTITY(10,1) NOT NULL,
	[cdterm] [nvarchar](50) NULL,
	[apy] [nvarchar](50) NULL,
	[minimumDeposit] [decimal](10, 2) NULL,
 CONSTRAINT [PK_interestrates_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[internationalfundtransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[internationalfundtransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[requestId] [bigint] NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[numberOfRecurrences] [int] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[transactionCurrency] [nvarchar](50) NULL,
	[fromAccountCurrency] [nvarchar](50) NULL,
	[scheduledDate] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[beneficiarycountry] [nvarchar](50) NULL,
	[beneficiaryZipcode] [nvarchar](50) NULL,
	[beneficiaryCity] [nvarchar](50) NULL,
	[beneficiaryAddressLine1] [nvarchar](200) NULL,
	[beneficiaryAddressNickName] [nvarchar](50) NULL,
	[feeAmount] [nvarchar](50) NULL,
	[paymentType] [nvarchar](50) NULL,
	[paidBy] [nvarchar](50) NULL,
	[beneficiaryName] [nvarchar](50) NULL,
	[feeCurrency] [nvarchar](50) NULL,
	[bankId] [nvarchar](50) NULL,
	[bankName] [nvarchar](50) NULL,
	[bicCode] [nvarchar](50) NULL,
	[swiftCode] [nvarchar](50) NULL,
	[iban] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[pdf] [varbinary](max) NULL,
 CONSTRAINT [PK_internationalfundtransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[internationalpayee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[internationalpayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[cif] [nvarchar](50) NULL,
	[isBusinessPayee] [nvarchar](50) NULL,
 CONSTRAINT [PK_internationalpayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[intrabankpayee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[intrabankpayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[cif] [nvarchar](50) NULL,
	[isBusinessPayee] [nvarchar](50) NULL,
 CONSTRAINT [PK_intrabankpayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[intrabanktransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[intrabanktransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[requestId] [bigint] NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[onetime_id] [bigint] NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[transactionCurrency] [nvarchar](45) NULL,
	[fromAccountCurrency] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[numberOfRecurrences] [int] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[toAccountCurrency] [nvarchar](50) NULL,
	[scheduledDate] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[pdf] [varbinary](max) NULL,
 CONSTRAINT [PK_intrabanktransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[issuerimage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[issuerimage](
	[id] [nvarchar](50) NOT NULL,
	[issuerName] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Image] [nvarchar](max) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_issuerimage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[lead]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[lead](
	[id] [nvarchar](50) NOT NULL,
	[firstName] [nvarchar](50) NOT NULL,
	[middleName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[salutation] [nvarchar](45) NULL,
	[isCustomer] [bit] NOT NULL,
	[customerId] [nvarchar](50) NULL,
	[product_id] [nvarchar](50) NULL,
	[csr_id] [nvarchar](50) NULL,
	[status_id] [nvarchar](50) NULL,
	[countryCode] [nvarchar](10) NULL,
	[phoneNumber] [nvarchar](50) NULL,
	[extension] [nvarchar](10) NULL,
	[email] [nvarchar](50) NULL,
	[closureReason] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_lead_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[leadnote]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[leadnote](
	[id] [nvarchar](50) NOT NULL,
	[lead_Id] [nvarchar](50) NULL,
	[note] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_leadnote_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[legaldeclarationsresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[legaldeclarationsresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[IsPrimaryResident] [bit] NULL,
	[HasRelationshipWithSeller] [bit] NULL,
	[TypeOfProperty] [nvarchar](50) NULL,
	[TitleOfProperty] [nvarchar](50) NULL,
	[IsBorrowingMoney] [bit] NULL,
	[HasAnyOtherMortgageLoan] [bit] NULL,
	[OtherMortgageLoanAmount] [nvarchar](50) NULL,
	[HasNewCredit] [bit] NULL,
	[HasPropertyLien] [bit] NULL,
	[IsGuarantorOfAnyLoan] [bit] NULL,
	[HasOutsandingJudgements] [bit] NULL,
	[HasFinancialLiability] [bit] NULL,
	[IsDelinquent] [bit] NULL,
	[HasForeclosureInPast] [bit] NULL,
	[HasPreForeclosureSale] [bit] NULL,
	[HasPropertyForeclosedInPast] [bit] NULL,
	[HasDeclaredBankruptcyInPast] [bit] NULL,
	[BankruptcyType] [nvarchar](60) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_legaldeclarationsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [legaldeclarationsresponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[liabilitiesheaderresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[liabilitiesheaderresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[HasLiabilities] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_liabilitiesheaderresponse_QueryResponse_id] PRIMARY KEY CLUSTERED 
(
	[QueryResponse_id] ASC,
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [liabilitiesheaderresponse$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[liabilitiesresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[liabilitiesresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[LiabilitiesHeaderResponse_id] [nvarchar](50) NULL,
	[Sequence] [int] IDENTITY(11,1) NOT NULL,
	[AccountType] [nvarchar](50) NULL,
	[CompanyName] [nvarchar](50) NULL,
	[AccountNumber] [nvarchar](50) NULL,
	[UnpaidBalance] [decimal](10, 0) NULL,
	[MonthlyPayment] [decimal](10, 0) NULL,
	[IsPaidOff] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_liabilitiesresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [liabilitiesresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[limittype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[limittype](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_limittype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[linkconfig]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[linkconfig](
	[EncodedLink] [nvarchar](100) NOT NULL,
	[LinkType] [nvarchar](50) NULL,
	[UserName] [nvarchar](50) NULL,
 CONSTRAINT [PK_linkconfig_EncodedLink] PRIMARY KEY CLUSTERED 
(
	[EncodedLink] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loaninforesponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loaninforesponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[LoanPurpose] [nvarchar](50) NULL,
	[HasIdentifiedHomeToPurchase] [bit] NULL,
	[PurchasePrice] [int] NULL,
	[DownPaymentValue] [int] NULL,
	[DownPaymentUnit] [nvarchar](50) NULL,
	[EstimatedLoanAmount] [int] NULL,
	[RefinanceType] [nvarchar](50) NULL,
	[MortgageBalance] [int] NULL,
	[PropertyValue] [int] NULL,
	[RequiredLoanAmount] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_loaninforesponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [loaninforesponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loanofficers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loanofficers](
	[id] [nvarchar](50) NOT NULL,
	[FirstName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[EmailId] [nvarchar](50) NULL,
	[PhoneNo] [int] NULL,
	[AddressLine] [nvarchar](50) NULL,
	[City] [nvarchar](50) NULL,
	[State] [nvarchar](50) NULL,
	[Zipcode] [int] NULL,
	[EmpNo] [nvarchar](50) NULL,
	[Country] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_loanofficers_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loanofficersresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loanofficersresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[HasLoanOfficer] [bit] NULL,
	[LoansOfficers_EmpNo] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_loanofficersresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [loanofficersresponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loanproduct]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loanproduct](
	[id] [nvarchar](50) NOT NULL,
	[LoanType_id] [nvarchar](50) NULL,
	[MinLimitAmount] [decimal](10, 2) NULL,
	[MaxLimitAmount] [decimal](10, 2) NULL,
	[Name] [nvarchar](100) NULL,
	[Description] [nvarchar](100) NULL,
	[Code] [nvarchar](50) NULL,
	[Image] [nvarchar](max) NULL,
	[AtAGlance] [nvarchar](max) NULL,
	[APR] [nvarchar](50) NULL,
	[AnnualFee] [decimal](10, 2) NULL,
	[Rewards] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
	[InterestRate] [nvarchar](50) NULL,
	[Point] [nvarchar](50) NULL,
	[BestChoiceIf] [nvarchar](max) NULL,
	[Disadvantages] [nvarchar](max) NULL,
	[APRRef] [nvarchar](50) NULL,
	[InterestRateRef] [nvarchar](50) NULL,
	[PointRef] [nvarchar](50) NULL,
	[Priority] [nvarchar](45) NULL,
 CONSTRAINT [PK_loanproduct_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loansconfigurationmasters]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loansconfigurationmasters](
	[bundle_id] [nvarchar](255) NOT NULL,
	[app_id] [nvarchar](255) NULL,
	[channels] [nvarchar](255) NULL,
	[user_id] [nvarchar](255) NULL,
	[role] [nvarchar](255) NULL,
	[device_id] [nvarchar](255) NULL,
	[app_version] [nvarchar](255) NULL,
 CONSTRAINT [PK_loansconfigurationmasters_bundle_id] PRIMARY KEY CLUSTERED 
(
	[bundle_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loansconfigurations]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loansconfigurations](
	[bundle_id] [nvarchar](255) NOT NULL,
	[config_type] [nvarchar](255) NULL,
	[config_key] [nvarchar](255) NULL,
	[config_value] [nvarchar](max) NULL,
	[description] [nvarchar](255) NULL,
	[lastUpdatedTime] [datetime2](6) NULL,
 CONSTRAINT [PK_loansconfigurations_bundle_id] PRIMARY KEY CLUSTERED 
(
	[bundle_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loanselectorresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loanselectorresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[Product_id] [nvarchar](50) NULL,
	[ProductName] [nvarchar](50) NULL,
	[ProductRate] [nvarchar](50) NULL,
	[ProductAPR] [nvarchar](50) NULL,
	[MonthlyPayment] [nvarchar](50) NULL,
	[SelectProductLater] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[loantype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[loantype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
	[APRValue] [nvarchar](50) NULL,
 CONSTRAINT [PK_loantype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locale]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locale](
	[Code] [nvarchar](10) NOT NULL,
	[Language] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_locale_Code] PRIMARY KEY CLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[location]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[location](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[DisplayName] [nvarchar](100) NULL,
	[Description] [nvarchar](300) NULL,
	[PhoneNumber] [nvarchar](50) NULL,
	[EmailId] [nvarchar](50) NULL,
	[Address_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[WorkingDays] [nvarchar](50) NULL,
	[WorkSchedule_id] [nvarchar](50) NULL,
	[IsMainBranch] [bit] NOT NULL,
	[MainBranchCode] [nvarchar](50) NULL,
	[WebSiteUrl] [nvarchar](100) NULL,
	[isMobile] [tinyint] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_location_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [location$Code_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationcurrency]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationcurrency](
	[Location_id] [nvarchar](50) NOT NULL,
	[currency_code] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_locationcurrency_Location_id] PRIMARY KEY CLUSTERED 
(
	[Location_id] ASC,
	[currency_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationcustomersegment]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationcustomersegment](
	[Location_id] [nvarchar](50) NOT NULL,
	[segment_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_locationcustomersegment_Location_id] PRIMARY KEY CLUSTERED 
(
	[Location_id] ASC,
	[segment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationfacility]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationfacility](
	[Location_id] [nvarchar](50) NOT NULL,
	[facility_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_locationfacility_Location_id] PRIMARY KEY CLUSTERED 
(
	[Location_id] ASC,
	[facility_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationfile]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationfile](
	[id] [nvarchar](50) NOT NULL,
	[successcount] [int] NULL,
	[failurecount] [int] NULL,
	[locationfilestatus] [int] NULL,
	[locationfileclob] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_locationfile_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationlanguage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationlanguage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Location_id] [int] NOT NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_locationlanguage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[Location_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationservice]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationservice](
	[Location_id] [nvarchar](50) NOT NULL,
	[Service_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_locationservice_Location_id] PRIMARY KEY CLUSTERED 
(
	[Location_id] ASC,
	[Service_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[locationtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[locationtype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [nvarchar](50) NULL,
	[lastmodifiedts] [nvarchar](50) NULL,
	[synctimestamp] [nvarchar](50) NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_locationtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[lockobjects]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[lockobjects](
	[ObjectId] [nvarchar](45) NOT NULL,
	[User] [nvarchar](15) NOT NULL,
	[ExternalId] [nvarchar](45) NOT NULL,
	[ObjectName] [nvarchar](45) NULL,
	[Mode] [nvarchar](45) NULL,
	[Locked] [nvarchar](1) NULL,
	[currenttimestamp] [datetime] NULL,
 CONSTRAINT [PK_lockobjects_ObjectId] PRIMARY KEY CLUSTERED 
(
	[ObjectId] ASC,
	[User] ASC,
	[ExternalId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[logview]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[logview](
	[id] [nvarchar](50) NOT NULL,
	[User_id] [nvarchar](50) NULL,
	[ViewName] [nvarchar](200) NULL,
	[Description] [nvarchar](200) NULL,
	[viewData] [nvarchar](500) NULL,
	[LogType] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_logview_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[losapplications]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[losapplications](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastupdatedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_losapplications_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[media]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[media](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NOT NULL,
	[Type] [nvarchar](300) NOT NULL,
	[Description] [nvarchar](100) NOT NULL,
	[Url] [nvarchar](200) NULL,
	[Content] [varbinary](max) NULL,
	[Size] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_media_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membereligibility]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membereligibility](
	[id] [nvarchar](50) NOT NULL,
	[ConditionName] [nvarchar](50) NOT NULL,
	[ConditionValues] [nvarchar](200) NULL,
	[ConditionLabel] [nvarchar](200) NULL,
	[AdditionalConsideration] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_membereligibility_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membergroup]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membergroup](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[Description] [nvarchar](250) NULL,
	[Type_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[isEAgreementActive] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_membergroup_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membergrouptype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membergrouptype](
	[id] [nvarchar](50) NOT NULL,
	[description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_membergrouptype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membership]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membership](
	[id] [nvarchar](50) NOT NULL,
	[isCustomerCentric] [bit] NOT NULL,
	[name] [nvarchar](45) NULL,
	[taxId] [nvarchar](45) NULL,
	[phone] [nvarchar](45) NULL,
	[email] [nvarchar](45) NULL,
	[isBusinessType] [bit] NOT NULL,
	[addressId] [nvarchar](50) NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
 CONSTRAINT [PK_membership_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [membership$taxId_UNIQUE] UNIQUE NONCLUSTERED 
(
	[taxId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membershipaccounts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membershipaccounts](
	[id] [nvarchar](50) NOT NULL,
	[membershipId] [nvarchar](50) NOT NULL,
	[accountId] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
 CONSTRAINT [PK_membershipaccounts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[membershipowner]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[membershipowner](
	[id] [nvarchar](50) NOT NULL,
	[membershipId] [nvarchar](50) NOT NULL,
	[userName] [nvarchar](50) NOT NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NOT NULL,
	[dateOfBirth] [date] NOT NULL,
	[ssn] [nvarchar](50) NULL,
	[taxId] [nvarchar](50) NULL,
	[phone] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[memberType] [nvarchar](45) NULL,
	[salutation] [nvarchar](45) NULL,
	[maritalStatus] [nvarchar](45) NULL,
	[employmentStatus] [nvarchar](45) NULL,
	[memberTypeId] [nvarchar](45) NULL,
	[memberTypeName] [nvarchar](45) NULL,
 CONSTRAINT [PK_membershipowner_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[message]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[message](
	[id] [int] IDENTITY(402,1) NOT NULL,
	[Account_id] [bigint] NULL,
	[Category_id] [int] NULL,
	[Subcategory_id] [int] NULL,
	[subject] [nvarchar](100) NULL,
	[message] [nvarchar](512) NULL,
	[sentDate] [datetime] NULL,
	[status] [nvarchar](7) NULL,
	[isSoftDeleted] [bit] NULL,
	[isRead] [bit] NULL,
	[createdDate] [datetime] NULL,
	[receivedDate] [datetime] NULL,
	[softdeletedDate] [datetime] NULL,
 CONSTRAINT [PK_message_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[messageattachment]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[messageattachment](
	[id] [nvarchar](50) NOT NULL,
	[RequestMessage_id] [nvarchar](50) NULL,
	[AttachmentType_id] [nvarchar](50) NULL,
	[Media_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_messageattachment_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[messagecategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[messagecategory](
	[Id] [int] IDENTITY(6,1) NOT NULL,
	[category] [nvarchar](45) NOT NULL,
 CONSTRAINT [PK_messagecategory_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[messagesubcategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[messagesubcategory](
	[Id] [int] IDENTITY(21,1) NOT NULL,
	[subcategory] [nvarchar](45) NOT NULL,
	[Category_id] [int] NOT NULL,
 CONSTRAINT [PK_messagesubcategory_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[messagetemplate]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[messagetemplate](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](80) NOT NULL,
	[Body] [nvarchar](max) NULL,
	[AdditionalInfo] [nvarchar](500) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[creadtedts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_messagetemplate_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[messagetype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[messagetype](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Description] [nvarchar](100) NULL,
 CONSTRAINT [PK_messagetype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfa]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfa](
	[id] [nvarchar](50) NOT NULL,
	[App_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](50) NOT NULL,
	[FrequencyType_id] [nvarchar](50) NOT NULL,
	[FrequencyValue] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[PrimaryMFAType] [nvarchar](50) NOT NULL,
	[SecondaryMFAType] [nvarchar](50) NOT NULL,
	[SMSText] [nvarchar](max) NULL,
	[EmailSubject] [nvarchar](max) NULL,
	[EmailBody] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_mfa_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[App_id] ASC,
	[Action_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfaconfigurations]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfaconfigurations](
	[MFA_id] [nvarchar](50) NOT NULL,
	[MFAKey_id] [nvarchar](50) NOT NULL,
	[value] [nvarchar](300) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_mfaconfigurations_MFA_id] PRIMARY KEY CLUSTERED 
(
	[MFA_id] ASC,
	[MFAKey_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfakey]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfakey](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_mfakey_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfaservice]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfaservice](
	[serviceKey] [nvarchar](50) NOT NULL,
	[serviceName] [nvarchar](50) NULL,
	[User_id] [nvarchar](50) NULL,
	[Createddts] [datetime2](0) NULL,
	[retryCount] [int] NULL,
	[payload] [nvarchar](max) NULL,
	[securityQuestions] [nvarchar](1000) NULL,
	[isVerified] [nvarchar](5) NULL,
 CONSTRAINT [PK_mfaservice_serviceKey] PRIMARY KEY CLUSTERED 
(
	[serviceKey] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfaserviceconfig]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfaserviceconfig](
	[id] [int] IDENTITY(26,1) NOT NULL,
	[serviceName] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](120) NULL,
	[field] [nvarchar](50) NULL,
	[value] [nvarchar](50) NULL,
	[appId] [nvarchar](50) NULL,
 CONSTRAINT [PK_mfaserviceconfig_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfatype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfatype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_mfatype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[mfavariablereference]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[mfavariablereference](
	[Code] [nvarchar](255) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[createdby] [nvarchar](255) NULL,
	[modifiedby] [nvarchar](255) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_mfavariablereference_Code] PRIMARY KEY CLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[model]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[model](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[endpoint_url] [nvarchar](200) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_model_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[modelattribute]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[modelattribute](
	[model_id] [nvarchar](50) NOT NULL,
	[attribute_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_modelattribute_model_id] PRIMARY KEY CLUSTERED 
(
	[model_id] ASC,
	[attribute_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[module]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[module](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_module_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[newaccount]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[newaccount](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[address] [nvarchar](50) NULL,
	[dateofbirth] [date] NULL,
	[ssn] [nvarchar](50) NULL,
	[accountType] [int] NULL,
	[locationId] [int] NULL,
	[productId] [int] NULL,
	[userId] [int] NULL,
 CONSTRAINT [PK_newaccount_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[newuser]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[newuser](
	[id] [int] IDENTITY(87,1) NOT NULL,
	[userName] [nvarchar](50) NULL,
	[passWord] [nvarchar](50) NULL,
	[role] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[phone] [nvarchar](50) NULL,
 CONSTRAINT [PK_newuser_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [newuser$username] UNIQUE NONCLUSTERED 
(
	[userName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[notification]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[notification](
	[notificationId] [int] IDENTITY(26,1) NOT NULL,
	[notificationModule] [nvarchar](100) NULL,
	[notificationSubModule] [nvarchar](100) NULL,
	[notificationSubject] [nvarchar](1000) NULL,
	[notificationText] [nvarchar](max) NULL,
	[notificationActionLink] [nvarchar](500) NULL,
	[notificationCategory] [nvarchar](255) NULL,
	[actionButtonLabelName] [nvarchar](255) NULL,
	[imageURL] [nvarchar](200) NULL,
	[isRead] [nvarchar](100) NULL,
	[receivedDate] [datetime2](0) NULL,
	[user] [varbinary](255) NULL,
 CONSTRAINT [PK_notification_notificationId] PRIMARY KEY CLUSTERED 
(
	[notificationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[notificationcardinfo]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[notificationcardinfo](
	[id] [nvarchar](50) NOT NULL,
	[Notification_id] [nvarchar](50) NULL,
	[Customer_id] [nvarchar](50) NULL,
	[CardNumber] [nvarchar](100) NOT NULL,
	[CardName] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
 CONSTRAINT [PK_notificationcardinfo_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[numberrange]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[numberrange](
	[ObjectId] [nvarchar](45) NOT NULL,
	[Length] [int] NULL,
	[BankId] [nvarchar](50) NULL,
	[ObjectName] [nvarchar](45) NULL,
	[CurrentValue] [int] NULL,
	[StartValue] [int] NULL,
	[EndValue] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_numberrange_ObjectId] PRIMARY KEY CLUSTERED 
(
	[ObjectId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[onboardingtermsandconditions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[onboardingtermsandconditions](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](max) NULL,
	[description] [nvarchar](max) NULL,
	[status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_onboardingtermsandconditions_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[onetimepayee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[onetimepayee](
	[onetime_id] [bigint] IDENTITY(109,1) NOT NULL,
	[payeeName] [nvarchar](50) NOT NULL,
	[payeeNickName] [nvarchar](50) NOT NULL,
	[payeeType] [nvarchar](200) NULL,
	[wireAccountType] [nvarchar](50) NOT NULL,
	[swiftCode] [nvarchar](50) NULL,
	[routingNumber] [nvarchar](50) NULL,
	[zipCode] [nvarchar](50) NULL,
	[cityName] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[payeeAddressLine1] [nvarchar](50) NULL,
	[payeeAddressLine2] [nvarchar](50) NULL,
	[bankName] [nvarchar](50) NULL,
	[internationalRoutingCode] [nvarchar](50) NULL,
	[bankAddressLine1] [nvarchar](50) NULL,
	[bankAddressLine2] [nvarchar](50) NULL,
	[bankCity] [nvarchar](50) NULL,
	[bankState] [nvarchar](50) NULL,
	[bankZip] [nvarchar](200) NULL,
 CONSTRAINT [PK_onetimepayee_onetime_id] PRIMARY KEY CLUSTERED 
(
	[onetime_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[operatinghours]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[operatinghours](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Location_id] [int] NOT NULL,
	[description] [nvarchar](100) NULL,
	[operatingDay] [nvarchar](50) NULL,
	[startHour] [nvarchar](10) NULL,
	[endHour] [nvarchar](10) NULL,
 CONSTRAINT [PK_operatinghours_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[Location_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[option]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[option](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Label] [nvarchar](100) NULL,
	[Code] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_option_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[optiongroup]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[optiongroup](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_optiongroup_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[optionitem]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[optionitem](
	[id] [nvarchar](50) NOT NULL,
	[OptionGroup_id] [nvarchar](50) NULL,
	[Label] [nvarchar](200) NULL,
	[Code] [nvarchar](50) NULL,
	[DefaultValue] [nvarchar](100) NULL,
	[Sequence] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_optionitem_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[optionitemresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[optionitemresponse](
	[id] [nvarchar](50) NOT NULL,
	[QuestionResponse_id] [nvarchar](100) NULL,
	[OptionItem_id] [nvarchar](50) NULL,
	[ItemValue] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_optionitemresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[optionmetadata]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[optionmetadata](
	[id] [int] IDENTITY(83,1) NOT NULL,
	[LoanType_id] [nvarchar](50) NULL,
	[OptionGroup_id] [nvarchar](50) NULL,
	[FieldIdentifier] [nvarchar](50) NULL,
	[Section_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_optionmetadata_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [optionmetadata$id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisation]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisation](
	[id] [nvarchar](50) NOT NULL,
	[Type_Id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](200) NULL,
	[BusinessType_id] [nvarchar](50) NULL,
	[StatusId] [nvarchar](50) NOT NULL,
	[FaxId] [nvarchar](45) NULL,
	[createdby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[rejectedby] [nvarchar](50) NULL,
	[rejectedts] [datetime] NULL,
	[rejectedReason] [nvarchar](45) NULL,
 CONSTRAINT [PK_organisation_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [organisation$Name_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationaccounts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationaccounts](
	[id] [nvarchar](50) NOT NULL,
	[Organization_id] [nvarchar](50) NOT NULL,
	[Account_id] [nvarchar](50) NOT NULL,
	[AccountName] [nvarchar](50) NULL,
	[TypeID] [nvarchar](50) NOT NULL,
	[Membership_id] [nvarchar](50) NOT NULL,
	[Taxid] [nvarchar](50) NOT NULL,
	[SearchCriteria] [nvarchar](13) NULL,
	[SearchValue] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[StatusDesc] [nvarchar](45) NULL,
 CONSTRAINT [PK_organisationaccounts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [organisationaccounts$UNIQUE_AccountId_TypeId] UNIQUE NONCLUSTERED 
(
	[Account_id] ASC,
	[TypeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationactionlimit]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationactionlimit](
	[id] [nvarchar](50) NOT NULL,
	[Organisation_id] [nvarchar](50) NOT NULL,
	[Action_id] [nvarchar](255) NOT NULL,
	[LimitType_id] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_organisationactionlimit_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [organisationactionlimit$UNIQUE_organisationactionlimit] UNIQUE NONCLUSTERED 
(
	[Organisation_id] ASC,
	[Action_id] ASC,
	[LimitType_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationaddress]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationaddress](
	[id] [int] IDENTITY(4,1) NOT NULL,
	[Organization_id] [nvarchar](50) NULL,
	[Address_id] [nvarchar](45) NULL,
	[DurationOfStay] [nvarchar](45) NULL,
	[IsPrimary] [bit] NOT NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[Type_id] [nvarchar](45) NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_organisationaddress_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationcommunication]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationcommunication](
	[id] [int] IDENTITY(6,1) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[Organization_id] [nvarchar](50) NOT NULL,
	[Sequence] [int] NULL,
	[Value] [nvarchar](100) NULL,
	[Extension] [nvarchar](45) NULL,
	[Description] [nvarchar](45) NULL,
	[IsPreferredContactMethod] [bit] NULL,
	[PreferredContactTime] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_organisationcommunication_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationemployees]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationemployees](
	[id] [nvarchar](50) NOT NULL,
	[Organization_id] [nvarchar](50) NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Is_Admin] [bit] NOT NULL,
	[Is_Owner] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
	[isAuthSignatory] [bit] NULL,
 CONSTRAINT [PK_organisationemployees_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationfeatures]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationfeatures](
	[id] [nvarchar](50) NOT NULL,
	[organisationId] [nvarchar](50) NOT NULL,
	[featureId] [nvarchar](255) NOT NULL,
	[featureStatus] [nvarchar](45) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
 CONSTRAINT [PK_organisationfeatures_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [organisationfeatures$UNIQUE_organisationfeatures] UNIQUE NONCLUSTERED 
(
	[organisationId] ASC,
	[featureId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationmembership]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationmembership](
	[id] [nvarchar](50) NOT NULL,
	[Organization_id] [nvarchar](50) NULL,
	[Taxid] [nvarchar](50) NULL,
	[Membership_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_organisationmembership_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationowner]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationowner](
	[id] [int] IDENTITY(3,1) NOT NULL,
	[Organization_id] [nvarchar](50) NULL,
	[FirstName] [nvarchar](50) NULL,
	[MidleName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[DateOfBirth] [date] NULL,
	[IDType_id] [nvarchar](50) NULL,
	[IdValue] [nvarchar](50) NULL,
	[Email] [nvarchar](50) NULL,
	[Phone] [nvarchar](20) NULL,
	[Ssn] [nvarchar](45) NULL,
 CONSTRAINT [PK_organisationowner_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[organisationtype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[organisationtype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](200) NULL,
 CONSTRAINT [PK_organisationtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[otherassetsresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[otherassetsresponse](
	[id] [nvarchar](50) NOT NULL,
	[AssetsResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[AssetType] [nvarchar](50) NULL,
	[CashOrMarketValue] [int] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_otherassetsresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [otherassetsresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[otherincomesresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[otherincomesresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[IncomeResponse_id] [nvarchar](50) NOT NULL,
	[IncomeSource_id] [nvarchar](50) NULL,
	[IncomePayPeriod_id] [nvarchar](50) NULL,
	[Amount] [decimal](10, 2) NULL,
	[WorkingHours] [int] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_otherincomesresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [otherincomesresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[otherproducttype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[otherproducttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](200) NOT NULL,
	[Description] [nvarchar](1000) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_otherproducttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[othersourceofincome]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[othersourceofincome](
	[id] [nvarchar](50) NOT NULL,
	[IncomeInfo_id] [nvarchar](50) NULL,
	[SourceType] [nvarchar](50) NOT NULL,
	[PayPeriod] [nvarchar](50) NULL,
	[GrossIncome] [decimal](10, 2) NULL,
	[WeekWorkingHours] [nvarchar](50) NULL,
	[Customer_id] [nvarchar](50) NULL,
	[SourceOfIncomeName] [nvarchar](50) NULL,
	[SourceofIncomeDescription] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_othersourceofincome_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[otp]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[otp](
	[securityKey] [nvarchar](50) NOT NULL,
	[Otp] [nvarchar](10) NULL,
	[OtpType] [nvarchar](45) NULL,
	[InvalidAttempt] [int] NULL,
	[createdts] [datetime] NOT NULL,
	[Phone] [nvarchar](45) NULL,
	[User_id] [nvarchar](50) NULL,
	[serviceKey] [nvarchar](50) NULL,
	[NumberOfRetries] [int] NULL,
	[Email] [nvarchar](100) NULL,
 CONSTRAINT [PK_otp_securityKey] PRIMARY KEY CLUSTERED 
(
	[securityKey] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[otpcount]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[otpcount](
	[key] [nvarchar](50) NOT NULL,
	[User_id] [nvarchar](50) NULL,
	[Date] [nvarchar](20) NOT NULL,
	[Count] [int] NULL,
	[Phone] [nvarchar](15) NULL,
	[Email] [nvarchar](50) NULL,
 CONSTRAINT [PK_otpcount_key] PRIMARY KEY CLUSTERED 
(
	[key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[outagemessage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[outagemessage](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](100) NULL,
	[Channel_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[MessageText] [nvarchar](max) NULL,
	[startTime] [datetime] NULL,
	[endTime] [datetime] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_outagemessage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[outagemessageapp]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[outagemessageapp](
	[Outagemessage_id] [nvarchar](50) NOT NULL,
	[App_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_outagemessageapp_Outagemessage_id] PRIMARY KEY CLUSTERED 
(
	[Outagemessage_id] ASC,
	[App_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[ownaccounttransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[ownaccounttransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[requestId] [bigint] NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[onetime_id] [bigint] NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[transactionCurrency] [nvarchar](45) NULL,
	[fromAccountCurrency] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[numberOfRecurrences] [int] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[toAccountCurrency] [nvarchar](50) NULL,
	[scheduledDate] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[pdf] [varbinary](max) NULL,
 CONSTRAINT [PK_ownaccounttransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[p2pregistration]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[p2pregistration](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[displayName] [nvarchar](50) NULL,
	[account_id] [bigint] NULL,
	[isNpp] [bit] NULL,
	[isZell] [bit] NULL,
	[email] [nvarchar](50) NULL,
	[user_id] [int] NULL,
	[phone] [nvarchar](50) NULL,
	[account] [varbinary](255) NULL,
 CONSTRAINT [PK_p2pregistration_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[p2ptransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[p2ptransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[requestId] [bigint] NULL,
	[frequencyTypeId] [nvarchar](50) NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[toAccountNumber] [nvarchar](50) NULL,
	[transactionCurrency] [nvarchar](45) NULL,
	[fromAccountCurrency] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[numberOfRecurrences] [int] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[description] [nvarchar](255) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[frequencyEndDate] [datetime] NULL,
	[p2pContact] [nvarchar](50) NULL,
	[scheduledDate] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_p2ptransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[passwordhistory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[passwordhistory](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[PreviousPassword] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[lastsynctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_passwordhistory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[passwordlockoutsettings]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[passwordlockoutsettings](
	[id] [nvarchar](50) NOT NULL,
	[passwordValidity] [int] NOT NULL,
	[passwordExpiryWarningRequired] [bit] NOT NULL,
	[passwordExpiryWarningThreshold] [int] NOT NULL,
	[passwordHistoryCount] [int] NOT NULL,
	[accountLockoutThreshold] [int] NOT NULL,
	[accountLockoutTime] [int] NOT NULL,
	[recoveryEmailLinkValidity] [int] NOT NULL,
	[createdby] [nvarchar](64) NULL,
	[modifiedby] [nvarchar](64) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_passwordlockoutsettings_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[passwordpolicy]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[passwordpolicy](
	[id] [nvarchar](50) NOT NULL,
	[PolicyName] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_passwordpolicy_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[passwordrules]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[passwordrules](
	[id] [nvarchar](50) NOT NULL,
	[IsCustomer] [bit] NOT NULL,
	[minLength] [int] NOT NULL,
	[maxLength] [int] NOT NULL,
	[atleastOneLowerCase] [bit] NOT NULL,
	[atleastOneUpperCase] [bit] NOT NULL,
	[atleastOneNumber] [bit] NOT NULL,
	[atleastOneSymbol] [bit] NOT NULL,
	[charRepeatCount] [int] NOT NULL,
	[supportedSymbols] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[lastsynctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_passwordrules_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[payee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[payee](
	[Id] [int] IDENTITY(1753,1) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[name] [nvarchar](50) NOT NULL,
	[accountNumber] [nvarchar](50) NOT NULL,
	[companyName] [nvarchar](50) NULL,
	[phone] [nvarchar](50) NULL,
	[email] [nvarchar](100) NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[eBillEnable] [int] NULL,
	[Region_id] [int] NULL,
	[City_id] [int] NULL,
	[cityName] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[addressLine1] [nvarchar](50) NULL,
	[addressLine2] [nvarchar](50) NULL,
	[zipCode] [nvarchar](20) NULL,
	[User_Id] [nvarchar](50) NULL,
	[nickName] [nvarchar](50) NOT NULL,
	[softDelete] [bit] NULL,
	[billermaster_id] [int] NULL,
	[isAutoPayEnabled] [bit] NULL,
	[nameOnBill] [nvarchar](50) NULL,
	[notes] [nvarchar](50) NULL,
	[billerId] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[swiftCode] [nvarchar](50) NULL,
	[routingCode] [nvarchar](50) NULL,
	[bankName] [nvarchar](50) NULL,
	[bankAddressLine1] [nvarchar](50) NULL,
	[bankAddressLine2] [nvarchar](50) NULL,
	[bankCity] [nvarchar](50) NULL,
	[bankState] [nvarchar](50) NULL,
	[bankZip] [nvarchar](50) NULL,
	[isWiredRecepient] [bit] NULL,
	[internationalAccountNumber] [nvarchar](50) NULL,
	[wireAccountType] [nvarchar](50) NULL,
	[internationalRoutingCode] [nvarchar](50) NULL,
	[isManuallyAdded] [bit] NULL,
	[phoneExtension] [nvarchar](10) NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
	[IBAN] [nvarchar](45) NULL,
	[transitDays] [nvarchar](50) NULL,
	[organizationId] [nvarchar](45) NULL,
 CONSTRAINT [PK_payee_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[payeeaddress]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[payeeaddress](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Region_id] [int] NULL,
	[City_id] [int] NULL,
	[cityName] [nvarchar](100) NULL,
	[addressLine1] [nvarchar](100) NULL,
	[addressLine2] [nvarchar](100) NULL,
	[zipCode] [nvarchar](20) NULL,
	[latitude] [nvarchar](50) NULL,
	[logitude] [nvarchar](50) NULL,
 CONSTRAINT [PK_payeeaddress_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[payeetype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[payeetype](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](50) NULL,
 CONSTRAINT [PK_payeetype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[payperson]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[payperson](
	[id] [int] IDENTITY(1483,1) NOT NULL,
	[firstName] [nvarchar](45) NULL,
	[lastName] [nvarchar](45) NULL,
	[phone] [nvarchar](45) NULL,
	[email] [nvarchar](45) NULL,
	[User_id] [nvarchar](50) NOT NULL,
	[secondaryEmail] [nvarchar](100) NULL,
	[secondoryPhoneNumber] [nvarchar](100) NULL,
	[secondaryEmail2] [nvarchar](100) NULL,
	[secondaryPhoneNumber2] [nvarchar](100) NULL,
	[primaryContactForSending] [nvarchar](100) NULL,
	[nickName] [nvarchar](50) NULL,
	[name] [nvarchar](45) NULL,
	[isSoftDelete] [bit] NULL,
	[phoneExtension] [nvarchar](10) NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
 CONSTRAINT [PK_payperson_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[period]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[period](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[DayCount] [int] NULL,
	[Order] [int] NULL,
	[Code] [nvarchar](50) NULL,
	[isEditable] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_period_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [period$Name_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[periodiclimit]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[periodiclimit](
	[id] [nvarchar](50) NOT NULL,
	[TransactionLimit_id] [nvarchar](50) NOT NULL,
	[Period_id] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NULL,
	[MaximumLimit] [decimal](20, 2) NULL,
	[Currency] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_periodiclimit_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[permission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[permission](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[DataType_id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[isComposite] [smallint] NOT NULL,
	[PermissionValue] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [smallint] NOT NULL,
 CONSTRAINT [PK_permission_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/**
ALTER TABLE [${dbxschemaname}].[permission]
ADD CONSTRAINT [df_City]
DEFAULT 0 FOR [isComposite];
GO
ALTER TABLE [${dbxschemaname}].[permission]
ADD CONSTRAINT [df_Softdeleteflag]
DEFAULT 0 FOR [softdeleteflag];
GO
**/

/****** Object:  Table [${dbxschemaname}].[permissiontype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[permissiontype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_permissiontype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[personalinfoaddressresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[personalinfoaddressresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[AddressLine1] [nvarchar](50) NULL,
	[AddressLine2] [nvarchar](50) NULL,
	[AddressCity] [nvarchar](50) NULL,
	[AddressState] [nvarchar](50) NULL,
	[AddressZip] [nvarchar](50) NULL,
	[AddressCountry] [nvarchar](50) NULL,
	[AddressType_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[PersonalInfoResponse_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_personalinfoaddressresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[personalinforesponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[personalinforesponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[FirstName] [nvarchar](50) NULL,
	[MiddleName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[Suffix] [nvarchar](50) NULL,
	[DOB] [date] NULL,
	[CitizenshipStatus] [nvarchar](50) NULL,
	[MaritalStatus] [nvarchar](50) NULL,
	[NoOfDependents] [nvarchar](50) NULL,
	[ValuesForDependents] [nvarchar](50) NULL,
	[CurrentAddressType] [nvarchar](50) NULL,
	[CurrentAddressDuration] [nvarchar](50) NULL,
	[SSN] [int] NULL,
	[Email] [nvarchar](50) NULL,
	[MobileNumber] [nvarchar](10) NULL,
	[OfficeNumber] [nvarchar](10) NULL,
	[HomeNumber] [nvarchar](10) NULL,
	[PrimaryContact] [nvarchar](50) NULL,
	[IsUSArmedForce] [bit] NULL,
	[IsCurrOnService] [bit] NULL,
	[IsCurrOnServiceDOS] [bit] NULL,
	[ExpDOS] [date] NULL,
	[IsSurvivingSpouse] [bit] NULL,
	[IsNonActiveMemResorNationalGaurd] [bit] NULL,
	[IsCurrRetiredorDischargedorSeprated] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[IsMailingAddCheckbox] [bit] NULL,
 CONSTRAINT [PK_personalinforesponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [personalinforesponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmbargraph]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmbargraph](
	[id] [int] IDENTITY(13,1) NOT NULL,
	[totalCashFlow] [decimal](10, 2) NULL,
	[monthId] [int] NULL,
	[userId] [nvarchar](50) NULL,
	[year] [int] NULL,
 CONSTRAINT [PK_pfmbargraph_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmbudgetsnapshot]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmbudgetsnapshot](
	[id] [int] IDENTITY(11,1) NOT NULL,
	[Category_Id] [int] NULL,
	[allocatedAmount] [int] NULL,
	[amountSpent] [int] NULL,
 CONSTRAINT [PK_pfmbudgetsnapshot_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmcategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmcategory](
	[id] [int] IDENTITY(11,1) NOT NULL,
	[categoryName] [nvarchar](50) NULL,
 CONSTRAINT [PK_pfmcategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmmonth]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmmonth](
	[id] [int] IDENTITY(13,1) NOT NULL,
	[monthName] [nvarchar](20) NULL,
 CONSTRAINT [PK_pfmmonth_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmpiechart]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmpiechart](
	[id] [int] IDENTITY(145,1) NOT NULL,
	[cashSpent] [decimal](10, 2) NULL,
	[userId] [nvarchar](50) NULL,
	[monthId] [int] NULL,
	[categoryId] [int] NULL,
	[year] [int] NULL,
 CONSTRAINT [PK_pfmpiechart_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[pfmtransactions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[pfmtransactions](
	[id] [int] IDENTITY(253,1) NOT NULL,
	[userId] [nvarchar](50) NULL,
	[monthId] [int] NULL,
	[categoryId] [int] NULL,
	[transactionDate] [datetime] NOT NULL,
	[fromAccountNumber] [bigint] NULL,
	[amount] [decimal](10, 2) NULL,
	[notes] [nvarchar](100) NULL,
	[description] [nvarchar](100) NULL,
	[fromAccountName] [nvarchar](100) NULL,
	[isMappedToMerchant] [bit] NULL,
	[isAnalyzed] [bit] NULL,
	[toAccountNumber] [bigint] NULL,
	[toAccountName] [nvarchar](100) NULL,
	[year] [int] NULL,
 CONSTRAINT [PK_pfmtransactions_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[phone]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[phone](
	[id] [int] IDENTITY(110,1) NOT NULL,
	[type] [nvarchar](50) NULL,
	[countryType] [nvarchar](50) NULL,
	[extension] [nvarchar](50) NULL,
	[phoneNumber] [nvarchar](50) NULL,
	[isPrimary] [nvarchar](50) NULL,
	[receivePromotions] [nvarchar](50) NULL,
	[user_id] [nvarchar](50) NOT NULL,
	[account_id] [bigint] NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
 CONSTRAINT [PK_phone_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[policycontent]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[policycontent](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[Locale_Code] [nvarchar](5) NOT NULL,
	[Content] [nvarchar](1000) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[lastsynctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_policycontent_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[policytype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[policytype](
	[id] [nvarchar](50) NOT NULL,
	[Type] [nvarchar](50) NOT NULL,
	[IsCustomer] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[lastsynctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_policytype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[preferredaccount]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[preferredaccount](
	[Type_id] [int] IDENTITY(1,1) NOT NULL,
	[Account_id] [bigint] NULL,
	[description] [nvarchar](50) NULL,
 CONSTRAINT [PK_preferredaccount_Type_id] PRIMARY KEY CLUSTERED 
(
	[Type_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[prequalifypackage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[prequalifypackage](
	[id] [nvarchar](50) NOT NULL,
	[LoanType_id] [nvarchar](50) NULL,
	[LoanProduct_id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NULL,
	[Description] [nvarchar](100) NULL,
	[LoanAmount] [nvarchar](100) NULL,
	[LoanTerms] [nvarchar](50) NULL,
	[APR] [nvarchar](50) NULL,
	[MonthlyPayment] [nvarchar](50) NULL,
	[AnnualFee] [nvarchar](50) NULL,
	[BenfitsandRewards] [nvarchar](max) NULL,
	[TransferInformation] [nvarchar](50) NULL,
	[PrequalifyCondition] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[rate] [nvarchar](45) NULL,
 CONSTRAINT [PK_prequalifypackage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[privacypolicy]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[privacypolicy](
	[id] [nvarchar](50) NOT NULL,
	[Channel_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Description] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_privacypolicy_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[product]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[product](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[OtherProductType_id] [nvarchar](50) NULL,
	[ProductCode] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](200) NOT NULL,
	[Status_id] [nvarchar](50) NULL,
	[isLeadSupported] [bit] NOT NULL,
	[MarketingStateId] [nvarchar](50) NULL,
	[SecondaryProduct_id] [nvarchar](50) NULL,
	[ProductFeatures] [nvarchar](max) NULL,
	[ProductCharges] [nvarchar](max) NULL,
	[productDescription] [nvarchar](max) NULL,
	[AdditionalInformation] [nvarchar](max) NULL,
	[termsAndConditions] [nvarchar](max) NULL,
	[accountType] [int] NULL,
	[stateId] [int] NULL,
	[rates] [nvarchar](2000) NULL,
	[productImageURL] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_product_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [product$MarketingStateId_UNIQUE] UNIQUE NONCLUSTERED 
(
	[MarketingStateId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[productdetail]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[productdetail](
	[id] [int] IDENTITY(2,1) NOT NULL,
	[Product_id] [int] NULL,
	[Type_id] [nvarchar](50) NULL,
	[header] [nvarchar](100) NULL,
	[description] [nvarchar](max) NULL,
 CONSTRAINT [PK_productdetail_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[producttype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[producttype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_producttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[propertyinforesponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[propertyinforesponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[PurchasePlan] [nvarchar](50) NULL,
	[PropertyAddressLine1] [nvarchar](50) NULL,
	[PropertyAddressLine2] [nvarchar](50) NULL,
	[PropertyAddressCity] [nvarchar](50) NULL,
	[PropertyAddressState] [nvarchar](50) NULL,
	[PropertyAddressCountry] [nvarchar](50) NULL,
	[PropertyAddressZip] [int] NULL,
	[PropertyType] [nvarchar](50) NULL,
	[PropertyUsage] [nvarchar](50) NULL,
	[NumberOfUnits] [int] NULL,
	[IsMixedUseProperty] [bit] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_propertyinforesponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [propertyinforesponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[querycoborrower]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[querycoborrower](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[CoBorrower_Type] [nvarchar](50) NULL,
	[FirstName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[PhoneNumber] [nvarchar](20) NULL,
	[Email] [nvarchar](50) NULL,
	[OTP] [nvarchar](50) NULL,
	[OTPValidity] [datetime] NULL,
	[Is_CoBorrowerActive] [bit] NULL,
	[Is_Verified] [bit] NULL,
	[InvitationLink] [nvarchar](200) NULL,
	[InvitationLinkValidity] [datetime] NULL,
	[InvitationLinkStatus] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[Borrower_id] [nvarchar](50) NULL,
 CONSTRAINT [PK_querycoborrower_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[querydefinition]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[querydefinition](
	[id] [nvarchar](50) NOT NULL,
	[QueryType_id] [nvarchar](50) NULL,
	[Name] [nvarchar](100) NULL,
	[Code] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[StartDate] [date] NULL,
	[EndDate] [date] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_querydefinition_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[queryresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[queryresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryDefinition_id] [nvarchar](50) NULL,
	[Customer_id] [nvarchar](50) NOT NULL,
	[Application_id] [nvarchar](20) NULL,
	[Is_Applicant] [bit] NULL,
	[CoBorrower_id] [nvarchar](50) NULL,
	[LoanProduct_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[SubmitDate] [date] NULL,
	[ClosingDate] [date] NULL,
	[OverallPercentageCompletion] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_queryresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [queryresponse$Application_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Application_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[queryresponseconsent]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[queryresponseconsent](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Disclaimer_id] [nvarchar](50) NULL,
	[Is_Accepted] [nvarchar](50) NULL,
	[Is_Rejected] [nvarchar](50) NULL,
	[Submitedby] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [nvarchar](50) NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_queryresponseconsent_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[querysection]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[querysection](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[QueryDefinition_id] [nvarchar](50) NULL,
	[Parent_id] [nvarchar](50) NULL,
	[ApplicantAllowedTo] [nvarchar](50) NULL,
	[IndCoApplicantAllowedTo] [nvarchar](50) NULL,
	[JointCoApplicantAllowedTo] [nvarchar](50) NULL,
	[Sequence] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
	[abstractname] [nvarchar](45) NULL,
 CONSTRAINT [PK_querysection_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[querysectionquestion]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[querysectionquestion](
	[id] [nvarchar](100) NOT NULL,
	[QueryDefinition_id] [nvarchar](50) NULL,
	[QuerySection_id] [nvarchar](50) NULL,
	[QuestionDefinition_id] [nvarchar](100) NULL,
	[ParentQuerySectionQuestion_id] [nvarchar](50) NULL,
	[ParentQuestionOptionValue] [nvarchar](100) NULL,
	[Sequence] [int] NULL,
	[IsRequired] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
	[abstractName] [nvarchar](100) NOT NULL,
	[ParentQuerySectionQuestion_AbstractName] [nvarchar](50) NULL,
 CONSTRAINT [PK_querysectionquestion_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[querytype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[querytype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[Code] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_querytype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[questiondefinition]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[questiondefinition](
	[id] [nvarchar](100) NOT NULL,
	[QueryDefinition_id] [nvarchar](50) NULL,
	[DataType_id] [nvarchar](50) NULL,
	[OptionGroup_id] [nvarchar](50) NULL,
	[Unit] [nvarchar](50) NULL,
	[Name] [nvarchar](100) NULL,
	[Label] [nvarchar](max) NULL,
	[OtherLabel] [nvarchar](max) NULL,
	[IsRequired] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_questiondefinition_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[questionresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[questionresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](100) NULL,
	[QueryDefinition_id] [nvarchar](50) NULL,
	[QuestionDefinition_id] [nvarchar](50) NULL,
	[QuerySectionQuestion_id] [nvarchar](100) NULL,
	[QuerySection_id] [nvarchar](50) NULL,
	[ArrayIndex] [int] NULL,
	[ResponseValue] [nvarchar](max) NULL,
	[OptionItem_id] [nvarchar](100) NULL,
	[Unit] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [int] NOT NULL,
 CONSTRAINT [PK_questionresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[realestateagentresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[realestateagentresponse](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NULL,
	[Borrower_id] [nvarchar](50) NULL,
	[FirstName] [nvarchar](50) NULL,
	[LastName] [nvarchar](50) NULL,
	[EmailId] [nvarchar](50) NULL,
	[MobileNumber] [nvarchar](15) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[HasAgent] [bit] NULL,
 CONSTRAINT [PK_realestateagentresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [realestateagentresponse$Borrower_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Borrower_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[realestatesresponse]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[realestatesresponse](
	[id] [nvarchar](50) NOT NULL,
	[AssetsResponse_id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[Status] [nvarchar](50) NULL,
	[MarketValue] [int] NULL,
	[PropertyUsageType] [nvarchar](50) NULL,
	[MonthlyRentalIncome] [int] NULL,
	[MonthlyPayments] [int] NULL,
	[City] [nvarchar](50) NULL,
	[State] [nvarchar](50) NULL,
	[Zip] [nvarchar](50) NULL,
	[Country] [nvarchar](50) NULL,
	[OngoingMortgageFlag] [bit] NULL,
	[CreditorName] [nvarchar](50) NULL,
	[CreditorAccountNumber] [nvarchar](100) NULL,
	[MonthlyMortgagePayments] [int] NULL,
	[MortgageType] [nvarchar](50) NULL,
	[CreditLimit] [int] NULL,
	[UnpaidBalance] [int] NULL,
	[PlanToPayoffFlag] [bit] NULL,
	[Sequence] [int] IDENTITY(1,1) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
	[PropertyAddressLine1] [nvarchar](100) NULL,
	[PropertyAddressLine2] [nvarchar](100) NULL,
 CONSTRAINT [PK_realestatesresponse_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [realestatesresponse$Sequence_UNIQUE] UNIQUE NONCLUSTERED 
(
	[Sequence] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[region]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[region](
	[id] [nvarchar](50) NOT NULL,
	[Country_id] [nvarchar](50) NULL,
	[Code] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](128) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_region_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[requestapprovalmatrix]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[requestapprovalmatrix](
	[id] [bigint] IDENTITY(109,1) NOT NULL,
	[approvalMatrixId] [bigint] NOT NULL,
	[requestId] [bigint] NOT NULL,
	[receivedApprovals] [int] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_requestapprovalmatrix_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[requestcategory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[requestcategory](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_requestcategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[requestmessage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[requestmessage](
	[id] [nvarchar](50) NOT NULL,
	[CustomerRequest_id] [nvarchar](50) NULL,
	[MessageDescription] [nvarchar](max) NULL,
	[RepliedBy] [nvarchar](50) NULL,
	[RepliedBy_id] [nvarchar](50) NULL,
	[RepliedBy_Name] [nvarchar](100) NULL,
	[ReplySequence] [int] NULL,
	[IsRead] [nvarchar](10) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_requestmessage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[role]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[role](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Parent_id] [nvarchar](50) NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_role_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[rolecompositeaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[rolecompositeaction](
	[Role_id] [nvarchar](50) NOT NULL,
	[CompositeAction_id] [nvarchar](50) NOT NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_rolecompositeaction_Role_id] PRIMARY KEY CLUSTERED 
(
	[Role_id] ASC,
	[CompositeAction_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[rolecompositepermission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[rolecompositepermission](
	[Role_id] [nvarchar](50) NOT NULL,
	[CompositePermission_id] [nvarchar](50) NOT NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_rolecompositepermission_Role_id] PRIMARY KEY CLUSTERED 
(
	[Role_id] ASC,
	[CompositePermission_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[rolepermission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[rolepermission](
	[Role_id] [nvarchar](50) NOT NULL,
	[Permission_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_rolepermission_Role_id] PRIMARY KEY CLUSTERED 
(
	[Role_id] ASC,
	[Permission_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[roletype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[roletype](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_roletype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[scheduledtransaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[scheduledtransaction](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Payee_id] [int] NULL,
	[Bill_id] [int] NULL,
	[Type_id] [int] NULL,
	[fromAccountNumber] [bigint] NULL,
	[toAccountNumber] [bigint] NULL,
	[amount] [decimal](20, 2) NULL,
	[statusDesc] [nvarchar](50) NULL,
	[notes] [nvarchar](100) NULL,
	[description] [nvarchar](100) NULL,
	[scheduledDate] [date] NULL,
	[transactionDate] [datetime] NULL,
	[createdDate] [datetime] NOT NULL,
	[toExternalAccountNumber] [bigint] NULL,
	[Person_Id] [int] NULL,
	[frequencyType] [nvarchar](8) NULL,
	[numberOfRecurrences] [int] NULL,
	[frequencyStartDate] [date] NULL,
	[frequencyEndDate] [date] NULL,
	[category] [nvarchar](17) NULL,
	[recurrenceDesc] [nvarchar](50) NULL,
	[p2pContact] [nvarchar](50) NULL,
	[routingNumber] [nvarchar](45) NULL,
	[user_id] [int] NOT NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_scheduledtransaction_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[sectionstatus]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[sectionstatus](
	[id] [nvarchar](50) NOT NULL,
	[QueryResponse_id] [nvarchar](50) NOT NULL,
	[Borrower_id] [nvarchar](50) NOT NULL,
	[LoanType] [nvarchar](50) NULL,
	[ApplicantType] [nvarchar](50) NULL,
	[ApplicantPersonalInfo] [nvarchar](10) NULL,
	[MyMortgage] [nvarchar](10) NULL,
	[LoanAndPropertyInfo] [nvarchar](10) NULL,
	[CoApplicantAndAgentInfo] [nvarchar](10) NULL,
	[LoanSelection] [nvarchar](10) NULL,
	[LoanOfficerSelection] [nvarchar](10) NULL,
	[UserDetails] [nvarchar](10) NULL,
	[Income] [nvarchar](10) NULL,
	[Assets] [nvarchar](10) NULL,
	[LiabilitiesAndExpenses] [nvarchar](10) NULL,
	[LegalDeclarations] [nvarchar](10) NULL,
	[Demographics] [nvarchar](10) NULL,
	[Consent] [nvarchar](10) NULL,
	[ReviewAndSubmit] [nvarchar](10) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_sectionstatus_QueryResponse_id] PRIMARY KEY CLUSTERED 
(
	[QueryResponse_id] ASC,
	[Borrower_id] ASC,
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[securityimage]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[securityimage](
	[id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Image] [nvarchar](max) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_securityimage_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[securityquestion]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[securityquestion](
	[id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Question] [nvarchar](255) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_securityquestion_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[service]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[service](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[Feature_id] [nvarchar](50) NULL,
	[Name] [nvarchar](100) NULL,
	[Description] [nvarchar](300) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Disclaimer] [nvarchar](max) NULL,
	[Notes] [nvarchar](2000) NULL,
	[MaxTransferLimit] [decimal](20, 2) NULL,
	[MinTransferLimit] [decimal](20, 2) NULL,
	[TransferDenominations] [nvarchar](100) NULL,
	[IsFutureTransaction] [bit] NULL,
	[TransactionCharges] [nvarchar](100) NULL,
	[IsAuthorizationRequired] [bit] NULL,
	[IsSMSAlertActivated] [bit] NULL,
	[SMSCharges] [nvarchar](50) NULL,
	[IsBeneficiarySMSAlertActivated] [bit] NULL,
	[BeneficiarySMSCharge] [nvarchar](50) NULL,
	[HasWeekendOperation] [bit] NULL,
	[IsOutageMessageActive] [bit] NULL,
	[IsAlertActive] [bit] NULL,
	[IsTCActive] [bit] NULL,
	[IsAgreementActive] [bit] NULL,
	[IsCampaignActive] [bit] NULL,
	[WorkSchedule_id] [nvarchar](50) NULL,
	[TransactionFee_id] [nvarchar](50) NULL,
	[TransactionLimit_id] [nvarchar](50) NULL,
	[code] [nvarchar](50) NULL,
	[Category_id] [nvarchar](50) NULL,
	[DisplayName] [nvarchar](100) NULL,
	[DisplayDescription] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_service_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[service_channels]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[service_channels](
	[Service_id] [nvarchar](50) NOT NULL,
	[Channel_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_service_channels_Service_id] PRIMARY KEY CLUSTERED 
(
	[Service_id] ASC,
	[Channel_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[service_permission_mapper]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[service_permission_mapper](
	[id] [nvarchar](50) NOT NULL,
	[service_name] [nvarchar](50) NOT NULL,
	[object_name] [nvarchar](50) NULL,
	[operation] [nvarchar](50) NOT NULL,
	[permissions] [nvarchar](max) NULL,
 CONSTRAINT [PK_service_permission_mapper_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [service_permission_mapper$UNIQUE_service_permission_mapper] UNIQUE NONCLUSTERED 
(
	[service_name] ASC,
	[object_name] ASC,
	[operation] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[servicechannel]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[servicechannel](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_servicechannel_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[servicecommunication]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[servicecommunication](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Priority] [int] NULL,
	[Value] [nvarchar](100) NULL,
	[Extension] [nvarchar](50) NULL,
	[Description] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NOT NULL,
 CONSTRAINT [PK_servicecommunication_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[servicetype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[servicetype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_servicetype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[signatorytype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[signatorytype](
	[id] [nvarchar](50) NOT NULL,
	[name] [nvarchar](300) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_signatorytype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[state]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[state](
	[id] [int] NOT NULL,
	[state] [nvarchar](45) NULL,
	[country_id] [int] NULL,
 CONSTRAINT [PK_state_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [state$state_UNIQUE] UNIQUE NONCLUSTERED 
(
	[state] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[status]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[status](
	[id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](50) NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_status_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[Type_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[statuschange]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[statuschange](
	[id] [nvarchar](50) NOT NULL,
	[PreviousStatus_id] [nvarchar](50) NOT NULL,
	[NextStatus_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_statuschange_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[statustype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[statustype](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_statustype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[swiftcode]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[swiftcode](
	[id] [int] IDENTITY(127,1) NOT NULL,
	[bankName] [nvarchar](100) NULL,
	[city] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[bic] [nvarchar](50) NULL,
	[countryCode] [nvarchar](2) NULL,
	[countryRegion] [nvarchar](13) NULL,
 CONSTRAINT [PK_swiftcode_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[systemconfiguration]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[systemconfiguration](
	[id] [int] NULL,
	[PropertyName] [nvarchar](45) NOT NULL,
	[PropertyValue] [nvarchar](45) NOT NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NULL,
 CONSTRAINT [PK_systemconfiguration_PropertyName] PRIMARY KEY CLUSTERED 
(
	[PropertyName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[systemuser]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[systemuser](
	[id] [nvarchar](50) NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[Username] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](100) NOT NULL,
	[Email] [nvarchar](70) NOT NULL,
	[Code] [nvarchar](50) NULL,
	[FirstName] [nvarchar](255) NULL,
	[MiddleName] [nvarchar](255) NULL,
	[LastName] [nvarchar](255) NULL,
	[FailedCount] [int] NULL,
	[LastPasswordChangedts] [datetime] NULL,
	[ResetpasswordLink] [nvarchar](255) NULL,
	[ResetPasswordExpdts] [datetime] NULL,
	[lastLogints] [datetime] NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_systemuser_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[tbladdetails]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[tbladdetails](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[addetails] [nvarchar](2000) NULL,
	[actiondetails] [nvarchar](2000) NULL,
	[user_id] [int] NULL,
 CONSTRAINT [PK_tbladdetails_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[termandcondition]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[termandcondition](
	[id] [nvarchar](50) NOT NULL,
	[Code] [nvarchar](255) NOT NULL,
	[Title] [nvarchar](255) NULL,
	[Description] [nvarchar](255) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_termandcondition_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [termandcondition$Termandcondition_title_unique_index] UNIQUE NONCLUSTERED 
(
	[Title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [termandcondition$Termandcondition_unique_index] UNIQUE NONCLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[termandconditionapp]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[termandconditionapp](
	[id] [nvarchar](50) NOT NULL,
	[TermAndConditionId] [nvarchar](50) NOT NULL,
	[AppId] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_termandconditionapp_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [termandconditionapp$Termandconditionapp_unique_index] UNIQUE NONCLUSTERED 
(
	[TermAndConditionId] ASC,
	[AppId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[termandconditiontext]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[termandconditiontext](
	[id] [nvarchar](50) NOT NULL,
	[TermAndConditionId] [nvarchar](50) NOT NULL,
	[LanguageCode] [nvarchar](10) NOT NULL,
	[Version_Id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](255) NULL,
	[Content] [nvarchar](max) NULL,
	[ContentType_id] [nvarchar](50) NOT NULL,
	[ContentModifiedBy] [nvarchar](50) NOT NULL,
	[ContentModifiedOn] [datetime] NOT NULL,
	[Status_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_termandconditiontext_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [termandconditiontext$Termandconditiontext_unique_index] UNIQUE NONCLUSTERED 
(
	[TermAndConditionId] ASC,
	[LanguageCode] ASC,
	[Version_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[termsandconditions]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[termsandconditions](
	[id] [nvarchar](50) NOT NULL,
	[Service_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[Description] [nvarchar](max) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_termsandconditions_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[timeperiod]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[timeperiod](
	[id] [int] IDENTITY(8,1) NOT NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_timeperiod_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionfee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactionfee](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactionfee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionfeeslab]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactionfeeslab](
	[id] [nvarchar](50) NOT NULL,
	[TransactionFee_id] [nvarchar](50) NOT NULL,
	[MinimumTransactionValue] [nvarchar](50) NOT NULL,
	[MaximumTransactionValue] [nvarchar](50) NOT NULL,
	[Currency] [nvarchar](50) NOT NULL,
	[Fees] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactionfeeslab_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionfrequency]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactionfrequency](
	[id] [varchar](50) NOT NULL,
	[value] [varchar](50) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactionfrequency_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactiongroup]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactiongroup](
	[id] [nvarchar](50) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[Description] [nvarchar](100) NULL,
	[TransactionLimit_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactiongroup_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactiongroupservice]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactiongroupservice](
	[id] [nvarchar](50) NOT NULL,
	[TransactionGroup_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime2](4) NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactiongroupservice_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionlimit]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactionlimit](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](150) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_transactionlimit_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactionlogs]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactionlogs](
	[id] [int] NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[transactionId] [nvarchar](45) NULL,
	[userId] [nvarchar](45) NULL,
	[userName] [nvarchar](45) NULL,
	[fromAccount] [nvarchar](45) NULL,
	[fromAccType] [nvarchar](45) NULL,
	[toAccount] [nvarchar](45) NULL,
	[toAccType] [nvarchar](45) NULL,
	[amount] [nvarchar](45) NULL,
	[currency] [nvarchar](45) NULL,
	[payeeName] [nvarchar](45) NULL,
	[status] [nvarchar](45) NULL,
	[description] [nvarchar](100) NULL,
	[beneficiaryRoutingNum] [nvarchar](45) NULL,
	[batchId] [nvarchar](45) NULL,
	[logId] [nvarchar](45) NULL,
	[type] [nvarchar](45) NULL,
	[createdts] [datetime] NULL,
 CONSTRAINT [PK_transactionlogs_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transactiontype]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transactiontype](
	[Id] [int] IDENTITY(22,1) NOT NULL,
	[description] [nvarchar](100) NULL,
 CONSTRAINT [PK_transactiontype_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[transferseries]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[transferseries](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Type_id] [nvarchar](50) NULL,
	[accountFrom] [bigint] NULL,
	[accountTo] [bigint] NULL,
	[amount] [decimal](10, 2) NULL,
	[notes] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[createdDate] [date] NULL,
	[recurrenceType] [nvarchar](50) NULL,
	[frequency] [int] NULL,
	[occurrences] [int] NULL,
	[startDate] [date] NULL,
	[endDate] [date] NULL,
 CONSTRAINT [PK_transferseries_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[travelnotification]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[travelnotification](
	[id] [nvarchar](50) NOT NULL,
	[Date] [datetime] NOT NULL,
	[Channel_id] [nvarchar](50) NULL,
	[Status_id] [nvarchar](50) NULL,
	[PlannedDepartureDate] [date] NULL,
	[PlannedReturnDate] [date] NULL,
	[Destinations] [nvarchar](200) NULL,
	[AdditionalNotes] [nvarchar](200) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
	[phonenumber] [nvarchar](15) NULL,
 CONSTRAINT [PK_travelnotification_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[user]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[user](
	[Id] [int] IDENTITY(206,1) NOT NULL,
	[Application_id] [nvarchar](50) NULL,
	[Session_id] [nvarchar](50) NULL,
	[Device_id] [nvarchar](50) NULL,
	[userImage] [nvarchar](max) NULL,
	[ssn] [nvarchar](25) NULL,
	[userName] [nvarchar](50) NULL,
	[passWord] [nvarchar](50) NULL,
	[userFirstName] [nvarchar](50) NULL,
	[userLastName] [nvarchar](50) NULL,
	[phone] [nvarchar](20) NULL,
	[countryCode] [nvarchar](45) NULL,
	[email] [nvarchar](80) NULL,
	[default_account_transfers] [nvarchar](50) NULL,
	[dateOfBirth] [date] NULL,
	[default_account_deposit] [nvarchar](50) NULL,
	[defaultModule_id] [int] NULL,
	[default_account_payments] [nvarchar](50) NULL,
	[secondaryphone] [nvarchar](20) NULL,
	[secondaryemail] [nvarchar](30) NULL,
	[lastlogintime] [datetime] NULL,
	[areUserAlertsTurnedOn] [bit] NULL,
	[areDepositTermsAccepted] [bit] NULL,
	[areAccountStatementTermsAccepted] [bit] NULL,
	[unsuccessfulLoginAttempts] [int] NULL,
	[isUserAccountLocked] [bit] NULL,
	[userImageURL] [nvarchar](500) NULL,
	[addressLine1] [nvarchar](50) NULL,
	[addressLine2] [nvarchar](45) NULL,
	[city] [nvarchar](45) NULL,
	[state] [nvarchar](45) NULL,
	[country] [nvarchar](45) NULL,
	[zipcode] [nvarchar](45) NULL,
	[isSuperAdmin] [bit] NULL,
	[userCompany] [nvarchar](45) NULL,
	[validDate] [datetime] NULL,
	[pin] [nvarchar](6) NULL,
	[isPinSet] [bit] NULL,
	[role] [nvarchar](7) NULL,
	[cvv] [nvarchar](45) NULL,
	[otp] [nvarchar](45) NULL,
	[lockCount] [int] NULL,
	[isEnrolled] [bit] NULL,
	[Bank_id] [nvarchar](50) NULL,
	[default_account_cardless] [nvarchar](50) NULL,
	[isBillPaySupported] [bit] NULL,
	[isP2PSupported] [bit] NULL,
	[isBillPayActivated] [bit] NULL,
	[isP2PActivated] [bit] NULL,
	[default_account_billPay] [nvarchar](50) NULL,
	[default_to_account_p2p] [nvarchar](50) NULL,
	[default_from_account_p2p] [nvarchar](50) NULL,
	[isPhoneEnabled] [bit] NULL,
	[isEmailEnabled] [bit] NULL,
	[secondaryemail2] [nvarchar](80) NULL,
	[secondaryphone2] [nvarchar](20) NULL,
	[token] [nvarchar](200) NULL,
	[isWireTransferActivated] [bit] NULL,
	[default_account_wire] [bigint] NULL,
	[isWireTransferEligible] [bit] NULL,
	[currentLoginTime] [datetime] NULL,
	[maritalstatus] [nvarchar](8) NULL,
	[spousefirstname] [nvarchar](50) NULL,
	[spouselastname] [nvarchar](50) NULL,
	[noofdependents] [nvarchar](10) NULL,
	[gender] [nvarchar](6) NULL,
	[showBillPayFromAccPopup] [bit] NULL,
	[drivingLicenseNumber] [nvarchar](50) NULL,
	[phoneExtension] [nvarchar](10) NULL,
	[phoneCountryCode] [nvarchar](10) NULL,
 CONSTRAINT [PK_user_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [user$defaultModule_id_UNIQUE] UNIQUE NONCLUSTERED 
(
	[defaultModule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [user$User_Name] UNIQUE NONCLUSTERED 
(
	[userName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[useraccountalerts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[useraccountalerts](
	[id] [int] IDENTITY(286,1) NOT NULL,
	[User_id] [int] NULL,
	[AccountNumber] [bigint] NULL,
	[minimumBalance] [decimal](20, 2) NULL,
	[debitLimit] [decimal](20, 2) NULL,
	[creditLimit] [decimal](20, 2) NULL,
	[balanceUpdate_PeriodId] [int] NULL,
	[PayementDueReminder_PeriodId] [int] NULL,
	[depositMaturityReminder_PeriodId] [int] NULL,
	[isEnabled] [bit] NULL,
	[successfulTransfer] [bit] NULL,
	[checkClearance] [bit] NULL,
 CONSTRAINT [PK_useraccountalerts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[useraccounts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[useraccounts](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[MemberId] [nvarchar](50) NULL,
	[Account_id] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[AccountName] [nvarchar](50) NULL,
	[IsViewAllowed] [bit] NOT NULL,
	[IsDepositAllowed] [bit] NOT NULL,
	[IsWithdrawAllowed] [bit] NOT NULL,
	[IsOrganizationAccount] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_useraccounts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[useraddress]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[useraddress](
	[User_id] [nvarchar](50) NOT NULL,
	[Address_id] [nvarchar](50) NOT NULL,
	[Type_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_useraddress_User_id] PRIMARY KEY CLUSTERED 
(
	[User_id] ASC,
	[Address_id] ASC,
	[Type_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[useralerts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[useralerts](
	[id] [int] IDENTITY(180,1) NOT NULL,
	[User_id] [nvarchar](50) NULL,
	[bankingIDChange] [bit] NULL,
	[passwordChange] [bit] NULL,
	[passwordExpired] [bit] NULL,
	[communicationChange] [bit] NULL,
	[newPayeeAdded] [bit] NULL,
	[payeeDetailsUpdated] [bit] NULL,
	[newDealsAvailable] [bit] NULL,
	[dealsExpiring] [bit] NULL,
 CONSTRAINT [PK_useralerts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usercashflow]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usercashflow](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[monthCash] [int] NOT NULL,
	[monthCredit] [int] NOT NULL,
	[totalCash] [int] NOT NULL,
	[totalCreditDebit] [int] NOT NULL,
 CONSTRAINT [PK_usercashflow_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usercommunication]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usercommunication](
	[Type_id] [nvarchar](50) NULL,
	[User_id] [int] NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
	[sequence] [int] NULL,
	[value] [nvarchar](100) NULL,
	[extension] [nvarchar](50) NULL,
	[description] [nvarchar](50) NULL,
 CONSTRAINT [PK_usercommunication_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usercompositeaction]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usercompositeaction](
	[User_id] [nvarchar](50) NOT NULL,
	[CompositeAction_id] [nvarchar](50) NOT NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_usercompositeaction_User_id] PRIMARY KEY CLUSTERED 
(
	[User_id] ASC,
	[CompositeAction_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usercompositepermission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usercompositepermission](
	[User_id] [nvarchar](50) NOT NULL,
	[CompositePermission_id] [nvarchar](50) NOT NULL,
	[isEnabled] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_usercompositepermission_User_id] PRIMARY KEY CLUSTERED 
(
	[User_id] ASC,
	[CompositePermission_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usercreditcheck]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usercreditcheck](
	[id] [int] IDENTITY(4,1) NOT NULL,
	[User_id] [int] NULL,
	[isCreditCheck] [bit] NULL,
	[isSingatureUpload] [bit] NULL,
	[ssn] [nvarchar](20) NULL,
	[singatureImage] [nvarchar](max) NULL,
	[newuser_id] [int] NULL,
 CONSTRAINT [PK_usercreditcheck_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usernamerules]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usernamerules](
	[id] [nvarchar](50) NOT NULL,
	[IsCustomer] [bit] NOT NULL,
	[minLength] [int] NOT NULL,
	[maxLength] [int] NOT NULL,
	[symbolsAllowed] [bit] NOT NULL,
	[supportedSymbols] [nvarchar](100) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[lastsynctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_usernamerules_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usernotification]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usernotification](
	[id] [int] IDENTITY(634,1) NOT NULL,
	[notification_id] [int] NULL,
	[user_id] [nvarchar](50) NULL,
	[isRead] [nvarchar](11) NULL,
	[receivedDate] [datetime] NOT NULL,
 CONSTRAINT [PK_usernotification_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userpermission]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userpermission](
	[User_id] [nvarchar](50) NOT NULL,
	[Permission_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](45) NULL,
	[modifiedby] [nvarchar](45) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [tinyint] NULL,
 CONSTRAINT [PK_userpermission_Permission_id] PRIMARY KEY CLUSTERED 
(
	[Permission_id] ASC,
	[User_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userpersonalinfo]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userpersonalinfo](
	[User_id] [int] NULL,
	[id] [int] IDENTITY(18,1) NOT NULL,
	[dateOfBirth] [date] NULL,
	[gender] [nvarchar](6) NULL,
	[userfirstname] [nvarchar](50) NULL,
	[userlastname] [nvarchar](50) NULL,
	[maritalstatus] [nvarchar](8) NULL,
	[spouseFirstName] [nvarchar](50) NULL,
	[spouseLastName] [nvarchar](50) NULL,
	[noOfDependents] [nvarchar](10) NULL,
	[addressLine1] [nvarchar](50) NULL,
	[addressLine2] [nvarchar](50) NULL,
	[city] [nvarchar](20) NULL,
	[state] [nvarchar](20) NULL,
	[country] [nvarchar](20) NULL,
	[zipcode] [nvarchar](10) NULL,
	[employmentInfo] [nvarchar](50) NULL,
	[company] [nvarchar](50) NULL,
	[jobProfile] [nvarchar](50) NULL,
	[experience] [nvarchar](50) NULL,
	[annualIncome] [nvarchar](50) NULL,
	[assets] [nvarchar](50) NULL,
	[montlyExpenditure] [nvarchar](50) NULL,
	[addressDoc] [nvarchar](max) NULL,
	[signatureImage] [nvarchar](max) NULL,
	[employementDoc] [nvarchar](max) NULL,
	[incomeDoc] [nvarchar](max) NULL,
	[ssn] [nvarchar](50) NULL,
	[userPersonalInfo] [bit] NULL,
	[userEmploymentInfo] [bit] NULL,
	[userFinancialInfo] [bit] NULL,
	[userSecurityQuestions] [bit] NULL,
	[spousename] [nvarchar](50) NULL,
	[newuser_id] [int] NULL,
 CONSTRAINT [PK_userpersonalinfo_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userproducts]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userproducts](
	[id] [int] IDENTITY(55,1) NOT NULL,
	[User_id] [int] NULL,
	[Product_id] [int] NULL,
	[newuser_id] [int] NULL,
	[product] [nvarchar](max) NULL,
 CONSTRAINT [PK_userproducts_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userrole]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userrole](
	[User_id] [nvarchar](50) NOT NULL,
	[Role_id] [nvarchar](50) NOT NULL,
	[hasSuperAdminPrivilages] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_userrole_User_id] PRIMARY KEY CLUSTERED 
(
	[User_id] ASC,
	[Role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userrolecustomerrole]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userrolecustomerrole](
	[UserRole_id] [nvarchar](50) NOT NULL,
	[CustomerRole_id] [nvarchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_userrolecustomerrole_UserRole_id] PRIMARY KEY CLUSTERED 
(
	[UserRole_id] ASC,
	[CustomerRole_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usersecurity]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usersecurity](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[User_id] [nvarchar](50) NOT NULL,
	[question] [int] NOT NULL,
	[answer] [nvarchar](100) NULL,
 CONSTRAINT [PK_usersecurity_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[userserviceprefernces]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userserviceprefernces](
	[id] [nvarchar](50) NOT NULL,
	[Customer_id] [nvarchar](50) NULL,
	[Service_id] [nvarchar](50) NULL,
	[isTypeBusiness] [nvarchar](45) NULL,
	[PerDayLimit] [nvarchar](50) NULL,
	[PerMonthLimit] [nvarchar](50) NULL,
	[PerAccountLimit] [nvarchar](50) NULL,
	[HasApprove] [bit] NOT NULL,
	[HasDraftOnly] [bit] NOT NULL,
	[HasCancel] [bit] NOT NULL,
	[IsViewAll] [bit] NOT NULL,
	[IsViewNone] [bit] NOT NULL,
	[IsViewOwnTransfersOnly] [bit] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NULL,
	[lastmodifiedts] [datetime] NULL,
 CONSTRAINT [PK_userserviceprefernces_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[usertransactionhistory]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[usertransactionhistory](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](45) NULL,
	[transactionDate] [datetime] NULL,
	[amount] [int] NULL,
	[depositAmount] [int] NULL,
	[transactionType] [nvarchar](45) NULL,
	[referenceId] [nvarchar](45) NULL,
	[closingBalanceAmount] [int] NULL,
 CONSTRAINT [PK_usertransactionhistory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[variablereference]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[variablereference](
	[vr_key] [nvarchar](50) NOT NULL,
	[vr_value] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_variablereference_vr_key] PRIMARY KEY CLUSTERED 
(
	[vr_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[vehiclemakes]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[vehiclemakes](
	[id] [nvarchar](500) NOT NULL,
	[MakeId] [int] NULL,
	[MakeName] [nvarchar](500) NULL,
	[VehicleTypeId] [int] NULL,
	[VehicleTypeName] [nvarchar](500) NULL,
	[ParentVehicleTypeName] [nvarchar](500) NULL,
 CONSTRAINT [PK_vehiclemakes_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[vehiclemodels]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[vehiclemodels](
	[id] [nvarchar](500) NOT NULL,
	[Model_ID] [int] NULL,
	[Model_Name] [nvarchar](500) NULL,
	[VehicleTypeId] [int] NULL,
	[VehicleTypeName] [nvarchar](500) NULL,
	[ParentVehicleTypeName] [nvarchar](500) NULL,
	[Make_ID] [int] NULL,
	[Make_Name] [nvarchar](500) NULL,
	[Year] [nvarchar](50) NULL,
 CONSTRAINT [PK_vehiclemodels_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[vehicletypes]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[vehicletypes](
	[VehicleTypeId] [int] NOT NULL,
	[ParentVehicleTypeName] [nvarchar](50) NULL,
	[VehicleTypeName] [nvarchar](50) NULL,
 CONSTRAINT [PK_vehicletypes_VehicleTypeId] PRIMARY KEY CLUSTERED 
(
	[VehicleTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[vihicleinfo]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[vihicleinfo](
	[id] [nvarchar](50) NOT NULL,
	[VehicleType] [nvarchar](50) NULL,
	[Year] [bit] NULL,
	[Make] [nvarchar](50) NULL,
	[Model] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[lastmodifiedts] [datetime] NULL,
	[synctimestamp] [datetime] NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_vihicleinfo_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[wallet]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[wallet](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
 CONSTRAINT [PK_wallet_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [wallet$UK_fee6cfa8db4440ba8e768221092] UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[wiretransfers]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[wiretransfers](
	[transactionId] [bigint] IDENTITY(109,1) NOT NULL,
	[featureActionId] [nvarchar](50) NOT NULL,
	[transactionType] [nvarchar](45) NULL,
	[companyId] [nvarchar](50) NULL,
	[roleId] [nvarchar](45) NULL,
	[onetime_id] [bigint] NULL,
	[wireFileExecution_id] [int] NULL,
	[wireTemplateExecution_id] [int] NULL,
	[requestId] [bigint] NULL,
	[fromAccountNumber] [nvarchar](50) NOT NULL,
	[payeeAccountNumber] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NULL,
	[payeeCurrency] [nvarchar](50) NULL,
	[amount] [float] NULL,
	[status] [nvarchar](50) NULL,
	[confirmationNumber] [nvarchar](45) NULL,
	[notes] [nvarchar](255) NULL,
	[transactionts] [datetime] NULL,
	[processingDate] [nvarchar](50) NULL,
	[personId] [nvarchar](50) NULL,
	[fromNickName] [nvarchar](50) NULL,
	[fromAccountType] [nvarchar](50) NULL,
	[day1] [nvarchar](50) NULL,
	[day2] [nvarchar](50) NULL,
	[toAccountType] [nvarchar](50) NULL,
	[payPersonName] [nvarchar](50) NULL,
	[securityQuestion] [nvarchar](50) NULL,
	[SecurityAnswer] [nvarchar](50) NULL,
	[checkImageBack] [nvarchar](50) NULL,
	[payeeName] [nvarchar](50) NULL,
	[profileId] [nvarchar](50) NULL,
	[cardNumber] [nvarchar](50) NULL,
	[cardExpiry] [nvarchar](50) NULL,
	[isScheduled] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_wiretransfers_transactionId] PRIMARY KEY CLUSTERED 
(
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[wiretransferspayee]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[wiretransferspayee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[typeId] [nvarchar](45) NULL,
	[payeeId] [nvarchar](50) NOT NULL,
	[customerId] [nvarchar](50) NOT NULL,
	[companyId] [nvarchar](50) NULL,
	[cif] [nvarchar](50) NULL,
	[isBusinessPayee] [nvarchar](50) NULL,
 CONSTRAINT [PK_wiretransferspayee_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [${dbxschemaname}].[workschedule]    Script Date: 7/28/2020 3:10:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[workschedule](
	[id] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](50) NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_workschedule_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [sequence]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[accountcommunication] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[accountstatement] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[accountstatement] ADD  DEFAULT (NULL) FOR [statementLink]
GO
ALTER TABLE [${dbxschemaname}].[accountstatement] ADD  DEFAULT (getdate()) FOR [month]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [TypeDescription]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [displayName]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [transactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [transferLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [dailyDepositLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [monthlyDepositLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [termsAndConditions]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [features]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [rates]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [info]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT ((0)) FOR [supportChecks]
GO
ALTER TABLE [${dbxschemaname}].[accounttype] ADD  DEFAULT (NULL) FOR [countryCode]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [achFileName]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [featureActionId]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [debitAmount]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [requestType]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [numberOfCredits]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [numberOfDebits]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [numberOfPrenotes]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [creditAmount]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [numberOfRecords]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [achFileFormatType_id]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [actedBy]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [updatedts]
GO
ALTER TABLE [${dbxschemaname}].[achfile] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[achfileformattype] ADD  DEFAULT (NULL) FOR [fileextension]
GO
ALTER TABLE [${dbxschemaname}].[achfileformattype] ADD  DEFAULT (NULL) FOR [mimetype]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [achFileId]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [requestType]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [totalDebitAmount]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [totalCreditAmount]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [effectiveDate]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [offsetAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [offsetTransactionType]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] ADD  DEFAULT (NULL) FOR [offsetAmount]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [achFileRecordId]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [receiverTransactionType]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [receiverAccountType]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [receiverAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [receiverName]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [fromAccount]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [effectiveDate]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [maxAmount]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [transactionType_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [templateType_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [templateRequestType_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (N'No Template Used') FOR [templateName]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [actedBy]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [template_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [updatedts]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [totalAmount]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] ADD  DEFAULT (NULL) FOR [featureActionId]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [abatrcNumber]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [detail_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [additionalInfo]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [eIN]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [isZeroTaxDue]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [taxType_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [transaction_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [templateRequestType_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] ADD  DEFAULT (NULL) FOR [record_Name]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionsubrecord] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[action] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[actionprofile] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[actiontype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [action1]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [action2]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [imageURL]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [adType]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [title]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [actionType]
GO
ALTER TABLE [${dbxschemaname}].[addetails] ADD  DEFAULT (NULL) FOR [imageURL2]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (NULL) FOR [AdditionalField_id]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (NULL) FOR [FieldValue]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmploymentType_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [ProvideEmploymentDetails]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerName]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressCity]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressState]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressCountry]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressZipCode]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployerPhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [TotalGrossIncome]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [EmployeeDesignation]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [BusinessShare_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [BusinessMonthlyLoss]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [ProfessionStartDate]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [LineOfWorkDuration]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [IsOtherParty]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [PrevStartDate]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [PrevEndDate]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (NULL) FOR [DataType]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (NULL) FOR [Length]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (NULL) FOR [FieldLabel]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionalfield] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [AdditionalEmploymentsResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [IncomeDetail_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [PayPeriod_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [Amount]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [WorkingHours]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (NULL) FOR [HasAdditionalMortgage]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [CreditorName]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [LienType]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [MonthlyMortgage]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [AmountToBeDrawn]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [CreditLimit]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] ADD  DEFAULT (NULL) FOR [AdditionalMortgageHeaderResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [Region_id]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [City_id]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [addressLine2]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [addressLine3]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [latitude]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [logitude]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [isPreferredAddress]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [cityName]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[address] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[addresstype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [StartDate]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [ExpirationDate]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[adminnotification] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[advertisements] ADD  DEFAULT (NULL) FOR [actionType]
GO
ALTER TABLE [${dbxschemaname}].[advertisements] ADD  DEFAULT (NULL) FOR [action]
GO
ALTER TABLE [${dbxschemaname}].[advertisements] ADD  DEFAULT (NULL) FOR [adimagesrc]
GO
ALTER TABLE [${dbxschemaname}].[advertisements] ADD  DEFAULT (NULL) FOR [url]
GO
ALTER TABLE [${dbxschemaname}].[advertisements] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [AlertType_id]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT ((0)) FOR [IsSubscriptionNeeded]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT ((0)) FOR [IsSmsActive]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT ((0)) FOR [IsEmailActive]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT ((0)) FOR [IsPushActive]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [AlertContent]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [isActive]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [hasValue]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [currentValue]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [defaultValue]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alert] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (NULL) FOR [NoOfFields]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (NULL) FOR [DefaultValue]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertcontentfields] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [LanguageCode]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [Subject]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [SenderName]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [SenderEmail]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [ReferenceNumber]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (getdate()) FOR [DispatchDate]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (NULL) FOR [IsSubscriptionNeeded]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alerttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeaccounttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alerttypeapp] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[alerttypecustomertype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [LoanType_id]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[app] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[appaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[appchannel] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [OSType]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [OSversion]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [BannerURL]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [VersionLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [currencyCode]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [BusinessDays]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [BankName]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [DistanceUnit]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [ocrApiKey]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [ocrSecretKey]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [facialLicenseString]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [facialLicenseServerUrl]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [appStoreLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [playStoreLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [ipadNativeAppLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [androidTabletNativeAppLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x00) FOR [isLanguageSelectionEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x00) FOR [isBackEndCurencySymbolEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x00) FOR [isCountryCodeEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x00) FOR [isSortCodeVisible]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [currenciesSupported]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [deploymentGeography]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x00) FOR [isUTCDateFormattingEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [language]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [defaultAccountType]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [fundingAmount]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT ((0)) FOR [isBusinessBankingEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (N'false') FOR [isAccountAggregationEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [defaultCountryDialCode]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (0x01) FOR [isFeedbackEnabled]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [noOfDaysForRatingFromProfile]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [noOfDaysForRatingFromTransactions]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [noOfDaysForAnotherAttemptForRating]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [maxtimesFeedbackperversion]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [majorVersionsForFeedback]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [bannerImageURL]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [desktopBannerImageURL]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [mobileBannerImageURL]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [viewMoreDBXLink]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT ((1)) FOR [showAdsPostLogin]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT ((1)) FOR [isAlertAccountIDLevel]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (N'false') FOR [isAccountTypeLevelAlerts]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (N'true') FOR [isprofileImageAvailable]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [cardStatementYears]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [bwFileTransactionsLimit]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT ((1)) FOR [isAccountCentricCore]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (N'UTC+11:00') FOR [timeZoneOffset]
GO
ALTER TABLE [${dbxschemaname}].[application] ADD  DEFAULT (NULL) FOR [stopReasons]
GO
ALTER TABLE [${dbxschemaname}].[appmappingaid] ADD  DEFAULT (NULL) FOR [Appid]
GO
ALTER TABLE [${dbxschemaname}].[appmappingaid] ADD  DEFAULT (NULL) FOR [Channel]
GO
ALTER TABLE [${dbxschemaname}].[appmappingaid] ADD  DEFAULT (NULL) FOR [aid]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [appointmentTime]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [appointmentWith]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [dob]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [uid]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [branch_id]
GO
ALTER TABLE [${dbxschemaname}].[appointment] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (NULL) FOR [approvalruleId]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT ((-1.00)) FOR [lowerlimit]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT ((-1.00)) FOR [upperlimit]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD  DEFAULT ((0)) FOR [invalid]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[approvalrule] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (N'MAJOR') FOR [versionType]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT ((1)) FOR [isSupported]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[appversion] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[appversiontype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [LanguageCode]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [Subject]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [SenderName]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [SenderEmail]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [ReferenceNumber]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (getdate()) FOR [DispatchDate]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedalerthistory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [Priority]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [RequestSubject]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [AssignedTo]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [Accountid]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT ((0)) FOR [lastupdatedbycustomer]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [middleName]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [salutation]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT ((0)) FOR [isCustomer]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [customerId]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [product_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [csr_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [countryCode]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (NULL) FOR [lead_Id]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (NULL) FOR [Url]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (N'0') FOR [Size]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (NULL) FOR [RequestMessage_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (NULL) FOR [AttachmentType_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (NULL) FOR [Media_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [CustomerRequest_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [MessageDescription]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy_id]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy_Name]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [ReplySequence]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (N'false') FOR [IsRead]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [HasBankAssets]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [HasRealEstateAssets]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [HasGiftedAssets]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [HasOtherAssets]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[attachmenttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (NULL) FOR [options]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (NULL) FOR [range]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (NULL) FOR [helptext]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[attribute] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[attributecriteria] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[attributeoption] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[attributetype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (NULL) FOR [sequenceNumber]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (NULL) FOR [BackendId]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (NULL) FOR [BackendType]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (NULL) FOR [identifier_name]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD  DEFAULT (N'0') FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[bank] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[bank] ADD  DEFAULT ((0)) FOR [Oauth2]
GO
ALTER TABLE [${dbxschemaname}].[bank] ADD  DEFAULT (NULL) FOR [IdentityProvider]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [FinancialInstituteName]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [AccountType]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [AccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [CashOrMarketValue]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [InternationalAccountFlag]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [address1]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [address2]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [workingHours]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [services]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [latitude]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [longitude]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[bankcommunication] ADD  DEFAULT (NULL) FOR [Bank_id]
GO
ALTER TABLE [${dbxschemaname}].[bankcommunication] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[bankcommunication] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[bankcommunication] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [Url]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [Address_id]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [BankForTransfer_id]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [RoutingNumber]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [RoutingCode]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bankservice] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[banner] ADD  DEFAULT (NULL) FOR [Category_id]
GO
ALTER TABLE [${dbxschemaname}].[banner] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [columnName]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [condition]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [dueDateChecktype]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [dueDateParamName]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT (getdate()) FOR [updatedts]
GO
ALTER TABLE [${dbxschemaname}].[batchalertdefinition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[batchalertobject] ADD  DEFAULT (NULL) FOR [operationName]
GO
ALTER TABLE [${dbxschemaname}].[batchalertobject] ADD  DEFAULT (getdate()) FOR [lastSyncTimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (NULL) FOR [action]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [transactionId]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [requiredSets]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [receivedSets]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] ADD  DEFAULT (NULL) FOR [accountId]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [templateName]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [templateDescription]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [fromAccount]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [effectiveDate]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [maxAmount]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [updatedBy]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (getdate()) FOR [updatedts]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [transactionType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [templateType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [templateRequestType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [actedBy]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [totalAmount]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] ADD  DEFAULT (NULL) FOR [featureActionId]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [record_Name]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [abatrcNumber]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [detail_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [additionalInfo]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [ein]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [isZeroTaxDue]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [template_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [taxType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [templateRequestType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [Payee_id]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [billDueDate]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [paidDate]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT ((0.00)) FOR [dueAmount]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT ((0.00)) FOR [paidAmount]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT ((0.00)) FOR [balanceAmount]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT ((0.00)) FOR [minimumDue]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [statusDesc]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [billerMaster_id]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [billGeneratedDate]
GO
ALTER TABLE [${dbxschemaname}].[bill] ADD  DEFAULT (NULL) FOR [currencyCode]
GO
ALTER TABLE [${dbxschemaname}].[billercategory] ADD  DEFAULT (NULL) FOR [categoryName]
GO
ALTER TABLE [${dbxschemaname}].[billercompany] ADD  DEFAULT (NULL) FOR [companyName]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [billerName]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [accountNumber]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [mobileNumber]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [address]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [relationshipNumber]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [policyNumber]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT (NULL) FOR [billerCategoryId]
GO
ALTER TABLE [${dbxschemaname}].[billermaster] ADD  DEFAULT ((0)) FOR [ebillSupport]
GO
ALTER TABLE [${dbxschemaname}].[billpaypayee] ADD  DEFAULT (NULL) FOR [typeId]
GO
ALTER TABLE [${dbxschemaname}].[billpaypayee] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[billpaypayee] ADD  DEFAULT (NULL) FOR [cif]
GO
ALTER TABLE [${dbxschemaname}].[billpaypayee] ADD  DEFAULT (NULL) FOR [isBusinessPayee]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [payeeId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [billerId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [frequencystartdate]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [frequencyenddate]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [deliverBy]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [PhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [CreditType]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [BorrowerType]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [InviteChallenge]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [ApplicantType]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [IsAgreementAccepted]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[borrower] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[branchtype] ADD  DEFAULT (NULL) FOR [Type]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [minimumVersionSupported]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [downloadLink]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [supportedOperatingSystems]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [supportType]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (NULL) FOR [displayNameText]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[browsersupporttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[budget] ADD  DEFAULT (NULL) FOR [ExpenseCategory_id]
GO
ALTER TABLE [${dbxschemaname}].[budget] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[budget] ADD  DEFAULT ((0.00)) FOR [totalBudget]
GO
ALTER TABLE [${dbxschemaname}].[budget] ADD  DEFAULT ((0.00)) FOR [usedBudget]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefileformattype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [bulkWireTransferType]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [internationalRoutingNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT ((0.00)) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (N'') FOR [note]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientCity]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientState]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientCountryName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientZipCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientBankName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientBankAddress1]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientBankZipCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientBankcity]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientBankstate]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [accountNickname]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [recipientAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT ((0)) FOR [noOfTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT ((0)) FOR [noOfDomesticTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT ((0)) FOR [noOfInternationalTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT (NULL) FOR [company_id]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] ADD  DEFAULT (NULL) FOR [lastExecutedOn]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (getdate()) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT ((0)) FOR [totalCountOfDomesticTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT ((0)) FOR [totalCountOfInternationalTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT ((0)) FOR [noOfTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT ((0)) FOR [noOfDomesticTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT ((0)) FOR [noOfInternationalTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (NULL) FOR [modifiedBy]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (NULL) FOR [company_id]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (NULL) FOR [lastExecutedOn]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] ADD  DEFAULT (N'NA') FOR [deleteUniqueValue]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [bulkWireTransferType]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [internationalRoutingNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientCity]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientState]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientCountryName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientZipCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankName]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankAddress1]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankAddress2]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankZipCode]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankcity]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientBankstate]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [accountNickname]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [recipientAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] ADD  DEFAULT (NULL) FOR [payeeId]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (getdate()) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT ((0)) FOR [totalCountOfDomesticTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT ((0)) FOR [totalCountOfInternationalTransactions]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[businessconfiguration] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [minAuthSignatory]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [maxAuthSignatory]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[businesstype] ADD  DEFAULT (NULL) FOR [businesstypecol]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (NULL) FOR [start_datetime]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (NULL) FOR [end_datetime]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[campaign] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[campaignplaceholder] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (NULL) FOR [destination_url]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT ((0)) FOR [display_count]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [reason]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [cardType]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [action]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [account_id]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (N'0.00') FOR [creditLimit]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (N'0.00') FOR [availableCredit]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [serviceProvider]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [billingAddress]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [cardProductName]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [secondaryCardHolder]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (N'0.00') FOR [withdrawlLimit]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (0x00) FOR [isInternational]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [cardHolderName]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT ((0)) FOR [cvv]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [currentBalance]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [rewardsPoint]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [paymentDueDate]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [availableBalance]
GO
ALTER TABLE [${dbxschemaname}].[card] ADD  DEFAULT (NULL) FOR [currencyCode]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [CardAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [CardAccountName]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [AccountType]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [RequestType_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [RequestReason]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (getdate()) FOR [Date]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [Address_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [Communication_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [AdditionalNotes]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (NULL) FOR [DisplayName]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequesttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[cardstatements] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[cardstatements] ADD  DEFAULT (NULL) FOR [statementLink]
GO
ALTER TABLE [${dbxschemaname}].[cardstatements] ADD  DEFAULT (NULL) FOR [month]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionDescription]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionBalance]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionMerchantAddressName]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionMerchantCity]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [merchantCategory]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionStatus]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionCategory]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionDetailDescription]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionIndicator]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionTime]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT ((0.00)) FOR [transactionAmount]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionCurrencyCode]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT ((0.0000000)) FOR [transactionExchangeRate]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [exchangeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT ((0.00)) FOR [exchangeAmount]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionTaxIndicator]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT ((0.00000)) FOR [taxPercentage]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT ((0.00)) FOR [transactionTaxAmount]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [transactionTerminalID]
GO
ALTER TABLE [${dbxschemaname}].[cardtransaction] ADD  DEFAULT (NULL) FOR [cardType]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[category] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[channel] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[channeltext] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[check] ADD  DEFAULT (NULL) FOR [frontImage]
GO
ALTER TABLE [${dbxschemaname}].[check] ADD  DEFAULT (NULL) FOR [backImage]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (getdate()) FOR [orderTime]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [accountName]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [accountNickName]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [leafCount]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [postBoxNumber]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[checkorder] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (NULL) FOR [Region_id]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (NULL) FOR [Country_id]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[city] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[closurereason] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [LanguageCode]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [AlertSubTypeId]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [ChannelID]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [Subject]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [SenderName]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [SenderEmail]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[communicationtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [Action_id]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [Feature_id]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (NULL) FOR [Entitlement_id]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (NULL) FOR [app_id]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[configurationbundles] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [app_id]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [channels]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [role]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [device_id]
GO
ALTER TABLE [${dbxschemaname}].[configurationmasters] ADD  DEFAULT (NULL) FOR [app_version]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT ((0)) FOR [isPreLoginConfiguration]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[configurations] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[contenttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [MemberId]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [MemberType]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [IDType_id]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [IDValue]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[coremembership] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[countrycode] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[credentialchecker] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [userRoleId]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [CustomerType]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [userName]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [internalUserName]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT ((0)) FOR [isConsumed]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [appId]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [lastretrievedts]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (NULL) FOR [tokenconsumedts]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[currency] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Classification_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (N'TYPE_ID_RETAIL') FOR [CustomerType_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x00) FOR [isCombinedUser]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [MiddleName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [FullName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (N'SID_CUS_ACTIVE') FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Password]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [unsuccessfulLoginAttempts]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [lockCount]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Organization_Id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [organizationType]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Salutation]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Gender]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [DateOfBirth]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [DrivingLicenseNumber]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Ssn]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Cvv]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Token]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Pin]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [PreferredContactMethod]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [PreferredContactTime]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [MaritalStatus_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [SpouseName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [NoOfDependents]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [EmployementStatus_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [UserCompany]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [SecurityImage_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Location_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsOlbAllowed]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsStaffMember]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [CountryCode]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [UserImageURL]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [OlbEnrolmentStatus_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Otp]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [PreferedOtpMethod]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [OtpGenaratedts]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [ValidDate]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isUserAccountLocked]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsPinSet]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsEnrolledForOlb]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((1)) FOR [IsAssistConsented]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsPhoneEnabled]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [IsEmailEnabled]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isEnrolled]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isSuperAdmin]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [CurrentLoginTime]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Lastlogintime]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDType_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDValue]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDState]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDCountry]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDIssueDate]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IDExpiryDate]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [IsCoreIdentityScope]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Is_MemberEligibile]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [MemberEligibilityData]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Is_BBOA]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [CreditUnionMemberSince]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [AtionProfile_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [RegistrationLink]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [RegLinkResendCount]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [RegLinkValidity]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [areDepositTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [areAccountStatementTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [areUserAlertsTurnedOn]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x01) FOR [isBillPaySupported]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x00) FOR [isBillPayActivated]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x01) FOR [isP2PSupported]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x00) FOR [isP2PActivated]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x01) FOR [isWireTransferEligible]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (0x00) FOR [isWireTransferActivated]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [lockedOn]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isEagreementSigned]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [MothersMaidenName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [AddressValidationStatus]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Product]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [ApplicantChannel]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (N'1') FOR [Bank_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [Session_id]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [MaritalStatus]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [SpouseFirstName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [SpouseLastName]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [EmploymentInfo]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isEngageProvisioned]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [DefaultLanguage]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [isVIPCustomer]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [isdcode]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [taxid]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT ((0)) FOR [isSignatory]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [sigtype]
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD  DEFAULT (NULL) FOR [combinedUserId]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [Membership_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [Organization_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [AccountName]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [FavouriteStatus]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [IsViewAllowed]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [IsDepositAllowed]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [IsWithdrawAllowed]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [IsOrganizationAccount]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT ((0)) FOR [IsOrgAccountUnLinked]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (NULL) FOR [LimitType_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (N'ADR_TYPE_HOME') FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT ((0)) FOR [isPrimary]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (NULL) FOR [DurationOfStay]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (NULL) FOR [HomeOwnership]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (N'*') FOR [AccountId]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (N'*') FOR [AccountType]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT ((0)) FOR [IsSmsActive]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT ((0)) FOR [IsEmailActive]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT ((0)) FOR [IsPushActive]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (NULL) FOR [Value]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (N'*') FOR [AccountID]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (N'*') FOR [AccountType]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customeralertswitch] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [Party_id]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [CoreCustomer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [ProductId]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [ApplicationStatus]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [RequestKey]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [JSESSIONID]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [SaveChallengeAnswer]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [FundingStatus]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerapplication] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT ((0)) FOR [isPrimary]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [Value]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [Extension]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [IsPreferredContactMethod]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [PreferredContactTime]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (N'Domestic') FOR [countryType]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [receivePromotions]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [DeviceName]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [LastLoginTime]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [LastUsedIp]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [OperatingSystem]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [EnrollmentDate]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [appid]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [CreditScore]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [EmploymentType]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [AnnualIncome]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [AccountBalance]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Age]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [DebtToIncomeRatio]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [TimeofEmployment]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [State]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [PrequalifyScore]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [ActionProfile_id]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Input1]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Input2]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Input3]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Input4]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [Input5]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [MaxTransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [MaxDailyLimit]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [TransactionFee_id]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [TransactionLimit_id]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerexpense] ADD  DEFAULT (NULL) FOR [Amount]
GO
ALTER TABLE [${dbxschemaname}].[customerfile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerfile] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerfile] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerfile] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerfile] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customergroup] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerimage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customernote] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customernote] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customernote] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customernote] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customernote] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (N'') FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (N'') FOR [Notification_id]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT ((0)) FOR [IsRead]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customernotification] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountDeposit]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountTransfers]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultModule_id]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountPayments]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountCardless]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountBillPay]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultToAccountP2P]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultFromAccountP2P]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [DefaultAccountWire]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [areUserAlertsTurnedOn]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [areDepositTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [areAccountStatementTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isBillPaySupported]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isP2PSupported]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isBillPayActivated]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isP2PActivated]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isWireTransferActivated]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [isWireTransferEligible]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (0x00) FOR [ShowBillPayFromAccPopup]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (NULL) FOR [PreferedOtpMethod]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (NULL) FOR [PrequalifyPackage_id]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerpreviouspasswords] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerpreviouspasswords] ADD  DEFAULT (NULL) FOR [PwdSequence]
GO
ALTER TABLE [${dbxschemaname}].[customerpreviouspasswords] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerpreviouspasswords] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerpreviouspasswords] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [QuerySection_id]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [PercentageCompletion]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerquerysectionstatus] ADD  DEFAULT (NULL) FOR [LastQuerySectionQuestion_id]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [Priority]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [RequestSubject]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [AssignedTo]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [Accountid]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT ((0)) FOR [lastupdatedbycustomer]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customersegment] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [HasWeekendOperation]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [WorkSchedule_id]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [TransactionFee_id]
GO
ALTER TABLE [${dbxschemaname}].[customerservice] ADD  DEFAULT (NULL) FOR [TransactionLimit_id]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [appId]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [channel]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [platform]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [browser]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customertermsandconditions] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customertype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customertypeconfig] ADD  DEFAULT (NULL) FOR [CustomerType_id]
GO
ALTER TABLE [${dbxschemaname}].[customertypeconfig] ADD  DEFAULT (NULL) FOR [Appid]
GO
ALTER TABLE [${dbxschemaname}].[customertypeconfig] ADD  DEFAULT (0x00) FOR [AccessPermitted]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (NULL) FOR [parent_id]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customrole] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (NULL) FOR [account_id]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (NULL) FOR [limitType_id]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dashboardalerts] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[dashboardalerts] ADD  DEFAULT (NULL) FOR [created]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[datatype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (NULL) FOR [StartTime]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (NULL) FOR [EndTime]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [Module]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [FieldName]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [FieldValue]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbpconfig] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [accountLevel]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [DisplaySequence]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (NULL) FOR [DisplayName]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [AttributeId]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [AlertConditionId]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [Value1]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [Value2]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [IsGlobal]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [DisplaySequence]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (NULL) FOR [DisplayName]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [Value1]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [Value2]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [LastEventPushed]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [Balance]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (NULL) FOR [completedRecordCounter]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT ((0)) FOR [successflag]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[decisionbatchrun] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [job_id]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [decision_id]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [baseAttributeName]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [baseAttributeValue]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [resultAttributeName]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [errmsg]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [exception]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [failureTriggerJob_id]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[decisionfailure] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [job_id]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [decision_id]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [baseAttributeName]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [baseAttributeValue]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [resultAttributeName]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [resultAttributeValue]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [errmsg]
GO
ALTER TABLE [${dbxschemaname}].[decisionresult] ADD  DEFAULT (NULL) FOR [exception]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaign] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (NULL) FOR [image_url]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (NULL) FOR [destination_url]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT ((0)) FOR [display_count]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [EthinicityDontWish]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [Ethinicity]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [EthinicityOptions]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [GenderDontWish]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [Gender]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [RaceDontWish]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [RaceOptions]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[deviceregistration] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[deviceregistration] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [CreditScore]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfInquiries_6M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfInquiries_12M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfInquiries_24M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [TotalRevolvingOpenToBuyBalance]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [UtilizationPercentOfRevolvingTrades]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [SinceRecentDelinquency_M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [TotalNumberOfDerogatory]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [SinceRecentlyFiledCollection_M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [TotalNumberOfTrades]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [TotalNumberOfActiveTrades]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfTradesOpened_24M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfTradeswithUtilization]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [OldestOpenPersonalFinanceTrade_M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [LoanToIncomeRatio]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [NumberOfLoanAapplications_24M]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [DebtToIncomeRatio]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [PrequalifyScore]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [YearsOfMembership]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [AccountsBalance]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [Age]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [City]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [State]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [ZipCode]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [DurationOfStay]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [HomeOwnership]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [GrossMonthlyIncome]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [AnnualIncome]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[digitalprofile] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [App_id]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [ModuleName]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [DisclaimerName]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [DisclaimerUrl]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [ContentType]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[disclaimer] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[dmaddinteractions] ADD  DEFAULT (NULL) FOR [textcolor]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [imageURL]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [adType]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [navigationType]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [navigationURL]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [visible]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [model]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [flowPosition]
GO
ALTER TABLE [${dbxschemaname}].[dmadvertisements] ADD  DEFAULT (NULL) FOR [adTitle]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[eligibilitycriteria] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [TemplateName]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [Subject]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [SenderName]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [SenderEmail]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [AlertChannel]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [AlertLanguageCode]
GO
ALTER TABLE [${dbxschemaname}].[emailtemplates] ADD  DEFAULT (NULL) FOR [Alert_id]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [EmploymentType]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [CurrentEmployer]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [Designation]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [PayPeriod]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [GrossIncome]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [WeekWorkingHours]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [EmploymentStartDate]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [PreviousEmployer]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [PreviousDesignation]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [OtherEmployementType]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [OtherEmployementDescription]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[employementdetails] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmploymentType_id]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [ProvideEmploymentDetails]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerName]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressCity]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressState]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressCountry]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerAddressZipCode]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployerPhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [TotalGrossIncome]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [EmployeeDesignation]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [BusinessShare_id]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [BusinessMonthlyLoss]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [ProfessionStartDate]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [LineOfWorkDuration]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [IsOtherParty]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [PrevStartDate]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [PrevEndDate]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[entitystatus] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [${dbxschemaname}].[errorstatuscode] ADD  DEFAULT (NULL) FOR [SNo]
GO
ALTER TABLE [${dbxschemaname}].[errorstatuscode] ADD  DEFAULT (NULL) FOR [Opstatus]
GO
ALTER TABLE [${dbxschemaname}].[errorstatuscode] ADD  DEFAULT (NULL) FOR [HttpStatusCode]
GO
ALTER TABLE [${dbxschemaname}].[errorstatuscode] ADD  DEFAULT (NULL) FOR [ErrorMsg]
GO
ALTER TABLE [${dbxschemaname}].[errorstatuscode] ADD  DEFAULT (NULL) FOR [Remarks]
GO
ALTER TABLE [${dbxschemaname}].[event] ADD  DEFAULT (NULL) FOR [EventSubType]
GO
ALTER TABLE [${dbxschemaname}].[event] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[event] ADD  DEFAULT (NULL) FOR [PreProcessorResult]
GO
ALTER TABLE [${dbxschemaname}].[event] ADD  DEFAULT (NULL) FOR [PostProcessorResult]
GO
ALTER TABLE [${dbxschemaname}].[event] ADD  DEFAULT (NULL) FOR [Timestamp]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[eventactivitytype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (NULL) FOR [ActivityType]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[eventtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[exchangerates] ADD  DEFAULT (NULL) FOR [currency]
GO
ALTER TABLE [${dbxschemaname}].[exchangerates] ADD  DEFAULT (NULL) FOR [toCurrency]
GO
ALTER TABLE [${dbxschemaname}].[exchangerates] ADD  DEFAULT (NULL) FOR [currencyType]
GO
ALTER TABLE [${dbxschemaname}].[exchangerates] ADD  DEFAULT (NULL) FOR [exchangeRate]
GO
ALTER TABLE [${dbxschemaname}].[expensecategory] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[expensecategory] ADD  DEFAULT ((0)) FOR [isUndefined]
GO
ALTER TABLE [${dbxschemaname}].[expenseperiod] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[expenseperiod] ADD  DEFAULT (NULL) FOR [startDate]
GO
ALTER TABLE [${dbxschemaname}].[expenseperiod] ADD  DEFAULT (NULL) FOR [endDate]
GO
ALTER TABLE [${dbxschemaname}].[expenseperiod] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (NULL) FOR [HasExpenses]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [ExpensesHeaderResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [ExpenseType]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [MonthlyPayment]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [organizationId]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [Bank_id]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [nickName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [accountNumber]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [accountType]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [countryName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [user_Account]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [isInternationalAccount]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (0x01) FOR [isSameBankAccount]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (0x00) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [isVerified]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [createdOn]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [sortCode]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [phoneExtension]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [addressNickName]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [zipcode]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD  DEFAULT (NULL) FOR [externalaccountcol]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [BankId]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [Scheme]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [Address]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [BankName]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [IdentityProvider]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [Oauth2]
GO
ALTER TABLE [${dbxschemaname}].[externalbank] ADD  DEFAULT (NULL) FOR [logo]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [ExternalBank_id]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [Password]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [SessionToken]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[facility] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[faqcategory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [Question]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [Answer]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [FaqCategory_Id]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[faqs] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [Service_Fee]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [DisplaySequence]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT ((0)) FOR [isPrimary]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[feature] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [Feature_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT ((0)) FOR [isAccountLevel]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT ((0)) FOR [isMFAApplicable]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [MFA_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [TermsAndConditions_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT ((0)) FOR [isPrimary]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [DisplaySequence]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [dependency]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[featureaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [id]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [FeatureName]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [IsAddAllowed]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [IsEditAllowed]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [IsDeleteAllowed]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featurepreferences] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[featureservices] ADD  DEFAULT (NULL) FOR [id]
GO
ALTER TABLE [${dbxschemaname}].[featureservices] ADD  DEFAULT (NULL) FOR [FeaturePrefernce_id]
GO
ALTER TABLE [${dbxschemaname}].[featureservices] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[featuretype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [rating]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [featureRequest]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [likeMost]
GO
ALTER TABLE [${dbxschemaname}].[feedback] ADD  DEFAULT (NULL) FOR [improvement]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT (NULL) FOR [UserName]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT (NULL) FOR [feedbackID]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT ((0)) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT (NULL) FOR [deviceID]
GO
ALTER TABLE [${dbxschemaname}].[feedbackstatus] ADD  DEFAULT (NULL) FOR [customerID]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[frequencytype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [AssetType]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [AssetSource]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [CashOrMarketValue]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [DepositedStatus]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [AlertSubTypeId]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [AlertAttributeId]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [AlertConditionId]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [Value1]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [Value2]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [Frequency]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [DaysBeforeReminder]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [isUserCustomizable]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [IsSmsActive]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [IsEmailActive]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [IsPushActive]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[globalalert] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (NULL) FOR [LimitType_id]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[groupattribute] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT ((0)) FOR [isDefaultGroup]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (NULL) FOR [TransactionFee_id]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (NULL) FOR [TransactionLimit_id]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (NULL) FOR [holidayDate]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (getdate()) FOR [createdOn]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (getdate()) FOR [updatedOn]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (NULL) FOR [createdBy]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[holidays] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[iban] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${dbxschemaname}].[iban] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[idmconfiguration] ADD  DEFAULT (NULL) FOR [IDMKey]
GO
ALTER TABLE [${dbxschemaname}].[idmconfiguration] ADD  DEFAULT (NULL) FOR [IDMValue]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (NULL) FOR [IDName]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[idtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [EmploymentDetailsResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [IncomeDetail_id]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [PayPeriod_id]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [Amount]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [WorkingHours]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (NULL) FOR [HasAdditionalEmployment]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (NULL) FOR [HasPreviousEmployment]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (NULL) FOR [HasOtherSourcesOfIncome]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[informationcontent] ADD  DEFAULT (NULL) FOR [informationType]
GO
ALTER TABLE [${dbxschemaname}].[informationcontent] ADD  DEFAULT (NULL) FOR [informationContent]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [fromAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiarycountry]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryZipcode]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryCity]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryAddressNickName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [feeAmount]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [paymentType]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [paidBy]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [feeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [bankId]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [bicCode]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [iban]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[interbankpayee] ADD  DEFAULT (NULL) FOR [typeId]
GO
ALTER TABLE [${dbxschemaname}].[interbankpayee] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[interbankpayee] ADD  DEFAULT (NULL) FOR [cif]
GO
ALTER TABLE [${dbxschemaname}].[interbankpayee] ADD  DEFAULT (NULL) FOR [isBusinessPayee]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (NULL) FOR [LoanProduct_id]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[interestrate] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[interestrates] ADD  DEFAULT (NULL) FOR [cdterm]
GO
ALTER TABLE [${dbxschemaname}].[interestrates] ADD  DEFAULT (NULL) FOR [apy]
GO
ALTER TABLE [${dbxschemaname}].[interestrates] ADD  DEFAULT (NULL) FOR [minimumDeposit]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [fromAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiarycountry]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryZipcode]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryCity]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryAddressNickName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [feeAmount]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [paymentType]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [paidBy]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [feeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [bankId]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [bicCode]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [iban]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[internationalpayee] ADD  DEFAULT (NULL) FOR [typeId]
GO
ALTER TABLE [${dbxschemaname}].[internationalpayee] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[internationalpayee] ADD  DEFAULT (NULL) FOR [cif]
GO
ALTER TABLE [${dbxschemaname}].[internationalpayee] ADD  DEFAULT (NULL) FOR [isBusinessPayee]
GO
ALTER TABLE [${dbxschemaname}].[intrabankpayee] ADD  DEFAULT (NULL) FOR [typeId]
GO
ALTER TABLE [${dbxschemaname}].[intrabankpayee] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[intrabankpayee] ADD  DEFAULT (NULL) FOR [cif]
GO
ALTER TABLE [${dbxschemaname}].[intrabankpayee] ADD  DEFAULT (NULL) FOR [isBusinessPayee]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [onetime_id]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [fromAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [toAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[issuerimage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [middleName]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [salutation]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT ((0)) FOR [isCustomer]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [customerId]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [product_id]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [csr_id]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [countryCode]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[lead] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (NULL) FOR [lead_Id]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[leadnote] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [IsPrimaryResident]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasRelationshipWithSeller]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [TypeOfProperty]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [TitleOfProperty]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [IsBorrowingMoney]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasAnyOtherMortgageLoan]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [OtherMortgageLoanAmount]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasNewCredit]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasPropertyLien]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [IsGuarantorOfAnyLoan]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasOutsandingJudgements]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasFinancialLiability]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [IsDelinquent]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasForeclosureInPast]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasPreForeclosureSale]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasPropertyForeclosedInPast]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [HasDeclaredBankruptcyInPast]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [BankruptcyType]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (NULL) FOR [HasLiabilities]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [LiabilitiesHeaderResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [AccountType]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [CompanyName]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [AccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [UnpaidBalance]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [MonthlyPayment]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [IsPaidOff]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[limittype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[linkconfig] ADD  DEFAULT (NULL) FOR [LinkType]
GO
ALTER TABLE [${dbxschemaname}].[linkconfig] ADD  DEFAULT (NULL) FOR [UserName]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [LoanPurpose]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [HasIdentifiedHomeToPurchase]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [PurchasePrice]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [DownPaymentValue]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [DownPaymentUnit]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [EstimatedLoanAmount]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [RefinanceType]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [MortgageBalance]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [PropertyValue]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [RequiredLoanAmount]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [EmailId]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [PhoneNo]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [AddressLine]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [City]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [State]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [Zipcode]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [EmpNo]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [Country]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[loanofficers] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [HasLoanOfficer]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [LoansOfficers_EmpNo]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [LoanType_id]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [MinLimitAmount]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [MaxLimitAmount]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [APR]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [AnnualFee]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [InterestRate]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [Point]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [APRRef]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [InterestRateRef]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [PointRef]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] ADD  DEFAULT (NULL) FOR [Priority]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [app_id]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [channels]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [role]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [device_id]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurationmasters] ADD  DEFAULT (NULL) FOR [app_version]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurations] ADD  DEFAULT (NULL) FOR [config_type]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurations] ADD  DEFAULT (NULL) FOR [config_key]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurations] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[loansconfigurations] ADD  DEFAULT ('2018-11-16 13:48:43') FOR [lastUpdatedTime]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [Product_id]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [ProductName]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [ProductRate]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [ProductAPR]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [MonthlyPayment]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT ((0)) FOR [SelectProductLater]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loanselectorresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[loantype] ADD  DEFAULT (NULL) FOR [APRValue]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (NULL) FOR [Language]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locale] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [DisplayName]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [PhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [EmailId]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [Address_id]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [WorkingDays]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [WorkSchedule_id]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT ((0)) FOR [IsMainBranch]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [MainBranchCode]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [WebSiteUrl]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT ((0)) FOR [isMobile]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[location] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (NULL) FOR [successcount]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (NULL) FOR [failurecount]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (NULL) FOR [locationfilestatus]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationfile] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationlanguage] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locationservice] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[locationtype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (N'') FOR [ObjectId]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (N'') FOR [User]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (N'') FOR [ExternalId]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (NULL) FOR [ObjectName]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (NULL) FOR [Mode]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (NULL) FOR [Locked]
GO
ALTER TABLE [${dbxschemaname}].[lockobjects] ADD  DEFAULT (getdate()) FOR [currenttimestamp]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [ViewName]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [viewData]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [LogType]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[logview] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (getdate()) FOR [lastupdatedts]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[losapplications] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (NULL) FOR [Url]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (N'0') FOR [Size]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[media] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [ConditionValues]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [ConditionLabel]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [AdditionalConsideration]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[membereligibility] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [isEAgreementActive]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[membergroup] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[membergrouptype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT ((0)) FOR [isCustomerCentric]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [taxId]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT ((0)) FOR [isBusinessType]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [addressId]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membership] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] ADD  DEFAULT (NULL) FOR [accountId]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [ssn]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [taxId]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [memberType]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [salutation]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [maritalStatus]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [employmentStatus]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [memberTypeId]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] ADD  DEFAULT (NULL) FOR [memberTypeName]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [Category_id]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [Subcategory_id]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [subject]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [message]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [sentDate]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT ((0)) FOR [isSoftDeleted]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT ((0)) FOR [isRead]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [createdDate]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [receivedDate]
GO
ALTER TABLE [${dbxschemaname}].[message] ADD  DEFAULT (NULL) FOR [softdeletedDate]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (NULL) FOR [RequestMessage_id]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (NULL) FOR [AttachmentType_id]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (NULL) FOR [Media_id]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (NULL) FOR [AdditionalInfo]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (getdate()) FOR [creadtedts]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[messagetemplate] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[messagetype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (NULL) FOR [FrequencyValue]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[mfa] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[mfakey] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT (NULL) FOR [serviceName]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT (NULL) FOR [Createddts]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT ((0)) FOR [retryCount]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT (NULL) FOR [securityQuestions]
GO
ALTER TABLE [${dbxschemaname}].[mfaservice] ADD  DEFAULT (NULL) FOR [isVerified]
GO
ALTER TABLE [${dbxschemaname}].[mfaserviceconfig] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[mfaserviceconfig] ADD  DEFAULT (NULL) FOR [field]
GO
ALTER TABLE [${dbxschemaname}].[mfaserviceconfig] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[mfaserviceconfig] ADD  DEFAULT (NULL) FOR [appId]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[mfatype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[mfavariablereference] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[model] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[modelattribute] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[module] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [address]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [dateofbirth]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [ssn]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [accountType]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [locationId]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [productId]
GO
ALTER TABLE [${dbxschemaname}].[newaccount] ADD  DEFAULT (NULL) FOR [userId]
GO
ALTER TABLE [${dbxschemaname}].[newuser] ADD  DEFAULT (NULL) FOR [userName]
GO
ALTER TABLE [${dbxschemaname}].[newuser] ADD  DEFAULT (NULL) FOR [passWord]
GO
ALTER TABLE [${dbxschemaname}].[newuser] ADD  DEFAULT (NULL) FOR [role]
GO
ALTER TABLE [${dbxschemaname}].[newuser] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[newuser] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationModule]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationSubModule]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationSubject]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationText]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationActionLink]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [notificationCategory]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [actionButtonLabelName]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [imageURL]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [isRead]
GO
ALTER TABLE [${dbxschemaname}].[notification] ADD  DEFAULT (NULL) FOR [receivedDate]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (NULL) FOR [Notification_id]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (N'') FOR [ObjectId]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [Length]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [BankId]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [ObjectName]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [CurrentValue]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [StartValue]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [EndValue]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[numberrange] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (NULL) FOR [status_id]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[onboardingtermsandconditions] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [payeeType]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [cityName]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [payeeAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [payeeAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [internationalRoutingCode]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankCity]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankState]
GO
ALTER TABLE [${dbxschemaname}].[onetimepayee] ADD  DEFAULT (NULL) FOR [bankZip]
GO
ALTER TABLE [${dbxschemaname}].[operatinghours] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[operatinghours] ADD  DEFAULT (NULL) FOR [operatingDay]
GO
ALTER TABLE [${dbxschemaname}].[operatinghours] ADD  DEFAULT (NULL) FOR [startHour]
GO
ALTER TABLE [${dbxschemaname}].[operatinghours] ADD  DEFAULT (NULL) FOR [endHour]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [Label]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[option] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[optiongroup] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [OptionGroup_id]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [Label]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [DefaultValue]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [Sequence]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[optionitem] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (NULL) FOR [QuestionResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (NULL) FOR [OptionItem_id]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (NULL) FOR [ItemValue]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [LoanType_id]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [OptionGroup_id]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [FieldIdentifier]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [Section_id]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [Type_Id]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [BusinessType_id]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (N'SID_ORG_PENDING') FOR [StatusId]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [FaxId]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [rejectedby]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [rejectedts]
GO
ALTER TABLE [${dbxschemaname}].[organisation] ADD  DEFAULT (NULL) FOR [rejectedReason]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (NULL) FOR [AccountName]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (NULL) FOR [SearchCriteria]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (NULL) FOR [SearchValue]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] ADD  DEFAULT (N'Active') FOR [StatusDesc]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (NULL) FOR [LimitType_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [Organization_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [Address_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [DurationOfStay]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (0x00) FOR [IsPrimary]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] ADD  DEFAULT (0x00) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [Sequence]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [Value]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [Extension]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (0x00) FOR [IsPreferredContactMethod]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [PreferredContactTime]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] ADD  DEFAULT (0x00) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [Organization_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (0x00) FOR [Is_Admin]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (0x00) FOR [Is_Owner]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (0x00) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] ADD  DEFAULT (0x00) FOR [isAuthSignatory]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] ADD  DEFAULT (NULL) FOR [featureStatus]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[organisationmembership] ADD  DEFAULT (NULL) FOR [Organization_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationmembership] ADD  DEFAULT (NULL) FOR [Taxid]
GO
ALTER TABLE [${dbxschemaname}].[organisationmembership] ADD  DEFAULT (NULL) FOR [Membership_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [Organization_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [MidleName]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [DateOfBirth]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [IDType_id]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [IdValue]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [Phone]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] ADD  DEFAULT (NULL) FOR [Ssn]
GO
ALTER TABLE [${dbxschemaname}].[organisationtype] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[organisationtype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (NULL) FOR [AssetType]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (NULL) FOR [CashOrMarketValue]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [IncomeSource_id]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [IncomePayPeriod_id]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [Amount]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [WorkingHours]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[otherproducttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [IncomeInfo_id]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [PayPeriod]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [GrossIncome]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [WeekWorkingHours]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [SourceOfIncomeName]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [SourceofIncomeDescription]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[othersourceofincome] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [Otp]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [OtpType]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT ((0)) FOR [InvalidAttempt]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [Phone]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [serviceKey]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT ((0)) FOR [NumberOfRetries]
GO
ALTER TABLE [${dbxschemaname}].[otp] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[otpcount] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[otpcount] ADD  DEFAULT ((1)) FOR [Count]
GO
ALTER TABLE [${dbxschemaname}].[otpcount] ADD  DEFAULT (NULL) FOR [Phone]
GO
ALTER TABLE [${dbxschemaname}].[otpcount] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [startTime]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [endTime]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [onetime_id]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [fromAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [toAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT (NULL) FOR [displayName]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT (NULL) FOR [account_id]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT ((0)) FOR [isNpp]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT ((0)) FOR [isZell]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[p2pregistration] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [frequencyTypeId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [fromAccountCurrency]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [p2pContact]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (NULL) FOR [PreviousPassword]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT (getdate()) FOR [lastsynctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT ((1)) FOR [passwordExpiryWarningRequired]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT ((1)) FOR [passwordHistoryCount]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[passwordlockoutsettings] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[passwordpolicy] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [IsCustomer]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [atleastOneLowerCase]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [atleastOneUpperCase]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [atleastOneNumber]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [atleastOneSymbol]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((1)) FOR [charRepeatCount]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (NULL) FOR [supportedSymbols]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT (getdate()) FOR [lastsynctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[passwordrules] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [companyName]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((0)) FOR [eBillEnable]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [Region_id]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [City_id]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [cityName]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [addressLine2]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [User_Id]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((0)) FOR [softDelete]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((1)) FOR [billermaster_id]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((0)) FOR [isAutoPayEnabled]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [nameOnBill]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [billerId]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [routingCode]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankCity]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankState]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [bankZip]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((0)) FOR [isWiredRecepient]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [internationalAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [wireAccountType]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [internationalRoutingCode]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT ((0)) FOR [isManuallyAdded]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [phoneExtension]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (N'3') FOR [transitDays]
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD  DEFAULT (NULL) FOR [organizationId]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [Region_id]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [City_id]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [cityName]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [addressLine2]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [zipCode]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [latitude]
GO
ALTER TABLE [${dbxschemaname}].[payeeaddress] ADD  DEFAULT (NULL) FOR [logitude]
GO
ALTER TABLE [${dbxschemaname}].[payeetype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [firstName]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [lastName]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [secondaryEmail]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [secondoryPhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [secondaryEmail2]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [secondaryPhoneNumber2]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [primaryContactForSending]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [nickName]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (0x00) FOR [isSoftDelete]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [phoneExtension]
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [DayCount]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [Order]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT ((0)) FOR [isEditable]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[period] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [Period_id]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [MaximumLimit]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [Currency]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (NULL) FOR [DataType_id]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT ((0)) FOR [isComposite]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (NULL) FOR [PermissionValue]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[permission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[permissiontype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressCity]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressState]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressZip]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressCountry]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [AddressType_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] ADD  DEFAULT (NULL) FOR [PersonalInfoResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [MiddleName]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [Suffix]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [DOB]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [CitizenshipStatus]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [MaritalStatus]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [NoOfDependents]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [ValuesForDependents]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [CurrentAddressType]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [CurrentAddressDuration]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [SSN]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [MobileNumber]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [OfficeNumber]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [HomeNumber]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [PrimaryContact]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsUSArmedForce]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsCurrOnService]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsCurrOnServiceDOS]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [ExpDOS]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsSurvivingSpouse]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsNonActiveMemResorNationalGaurd]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [IsCurrRetiredorDischargedorSeprated]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] ADD  DEFAULT ((0)) FOR [IsMailingAddCheckbox]
GO
ALTER TABLE [${dbxschemaname}].[pfmbargraph] ADD  DEFAULT (NULL) FOR [totalCashFlow]
GO
ALTER TABLE [${dbxschemaname}].[pfmbargraph] ADD  DEFAULT (NULL) FOR [monthId]
GO
ALTER TABLE [${dbxschemaname}].[pfmbargraph] ADD  DEFAULT (NULL) FOR [userId]
GO
ALTER TABLE [${dbxschemaname}].[pfmbargraph] ADD  DEFAULT (NULL) FOR [year]
GO
ALTER TABLE [${dbxschemaname}].[pfmbudgetsnapshot] ADD  DEFAULT (NULL) FOR [Category_Id]
GO
ALTER TABLE [${dbxschemaname}].[pfmbudgetsnapshot] ADD  DEFAULT ((0)) FOR [allocatedAmount]
GO
ALTER TABLE [${dbxschemaname}].[pfmbudgetsnapshot] ADD  DEFAULT ((0)) FOR [amountSpent]
GO
ALTER TABLE [${dbxschemaname}].[pfmcategory] ADD  DEFAULT (NULL) FOR [categoryName]
GO
ALTER TABLE [${dbxschemaname}].[pfmmonth] ADD  DEFAULT (NULL) FOR [monthName]
GO
ALTER TABLE [${dbxschemaname}].[pfmpiechart] ADD  DEFAULT (NULL) FOR [cashSpent]
GO
ALTER TABLE [${dbxschemaname}].[pfmpiechart] ADD  DEFAULT (NULL) FOR [userId]
GO
ALTER TABLE [${dbxschemaname}].[pfmpiechart] ADD  DEFAULT (NULL) FOR [monthId]
GO
ALTER TABLE [${dbxschemaname}].[pfmpiechart] ADD  DEFAULT (NULL) FOR [categoryId]
GO
ALTER TABLE [${dbxschemaname}].[pfmpiechart] ADD  DEFAULT (NULL) FOR [year]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [userId]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [monthId]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [categoryId]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (getdate()) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT ((0.00)) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [fromAccountName]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT ((0)) FOR [isMappedToMerchant]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT ((0)) FOR [isAnalyzed]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [toAccountName]
GO
ALTER TABLE [${dbxschemaname}].[pfmtransactions] ADD  DEFAULT (NULL) FOR [year]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [countryType]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [phoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [isPrimary]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [receivePromotions]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [account_id]
GO
ALTER TABLE [${dbxschemaname}].[phone] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT (getdate()) FOR [lastsynctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[policycontent] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT ((0)) FOR [IsCustomer]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT (getdate()) FOR [lastsynctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[policytype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[preferredaccount] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[preferredaccount] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [LoanType_id]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [LoanProduct_id]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [LoanAmount]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [LoanTerms]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [APR]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [MonthlyPayment]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [AnnualFee]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [TransferInformation]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [PrequalifyCondition]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] ADD  DEFAULT (NULL) FOR [rate]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [OtherProductType_id]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT ((0)) FOR [isLeadSupported]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [MarketingStateId]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [SecondaryProduct_id]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [accountType]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [stateId]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [rates]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [productImageURL]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[product] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[productdetail] ADD  DEFAULT (NULL) FOR [Product_id]
GO
ALTER TABLE [${dbxschemaname}].[productdetail] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[productdetail] ADD  DEFAULT (NULL) FOR [header]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[producttype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PurchasePlan]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressCity]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressState]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressCountry]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyAddressZip]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyType]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [PropertyUsage]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [NumberOfUnits]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [IsMixedUseProperty]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [CoBorrower_Type]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [PhoneNumber]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [Email]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [OTP]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [OTPValidity]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [Is_CoBorrowerActive]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [Is_Verified]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [InvitationLink]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [InvitationLinkValidity]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [InvitationLinkStatus]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[querycoborrower] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [QueryType_id]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [StartDate]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [EndDate]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[querydefinition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [QueryDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [Application_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [Is_Applicant]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [CoBorrower_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [LoanProduct_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [SubmitDate]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [ClosingDate]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [OverallPercentageCompletion]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [Disclaimer_id]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [Is_Accepted]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [Is_Rejected]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [Submitedby]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [QueryDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [Parent_id]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [ApplicantAllowedTo]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [IndCoApplicantAllowedTo]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [JointCoApplicantAllowedTo]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [Sequence]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[querysection] ADD  DEFAULT (NULL) FOR [abstractname]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [QueryDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [QuerySection_id]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [QuestionDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [ParentQuerySectionQuestion_id]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [ParentQuestionOptionValue]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [Sequence]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT ((0)) FOR [IsRequired]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[querysectionquestion] ADD  DEFAULT (NULL) FOR [ParentQuerySectionQuestion_AbstractName]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[querytype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [QueryDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [DataType_id]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [OptionGroup_id]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [Unit]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [Label]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [OtherLabel]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT ((0)) FOR [IsRequired]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [QueryDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [QuestionDefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [QuerySectionQuestion_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [QuerySection_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [ArrayIndex]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [OptionItem_id]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [Unit]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [QueryResponse_id]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [EmailId]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [MobileNumber]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] ADD  DEFAULT (NULL) FOR [HasAgent]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [MarketValue]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [PropertyUsageType]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [MonthlyRentalIncome]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [MonthlyPayments]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [City]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [State]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [Zip]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [Country]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [OngoingMortgageFlag]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [CreditorName]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [CreditorAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [MonthlyMortgagePayments]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [MortgageType]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [CreditLimit]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [UnpaidBalance]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [PlanToPayoffFlag]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [PropertyAddressLine1]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] ADD  DEFAULT (NULL) FOR [PropertyAddressLine2]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (NULL) FOR [Country_id]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[region] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (NULL) FOR [receivedApprovals]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[requestcategory] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [CustomerRequest_id]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy_id]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [RepliedBy_Name]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [ReplySequence]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (N'false') FOR [IsRead]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (NULL) FOR [Parent_id]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[role] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[roletype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [Payee_id]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [Bill_id]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT ((0.00)) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [statusDesc]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (N'') FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (N' ') FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (getdate()) FOR [createdDate]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [toExternalAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [Person_Id]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (N'Once') FOR [frequencyType]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [frequencyStartDate]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (N'Uncategorised') FOR [category]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [recurrenceDesc]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [p2pContact]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (NULL) FOR [routingNumber]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[scheduledtransaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (NULL) FOR [LoanType]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (NULL) FOR [ApplicantType]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [ApplicantPersonalInfo]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [MyMortgage]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [LoanAndPropertyInfo]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [CoApplicantAndAgentInfo]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [LoanSelection]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [LoanOfficerSelection]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [UserDetails]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [Income]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [Assets]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [LiabilitiesAndExpenses]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [LegalDeclarations]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [Demographics]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [Consent]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (N'notDone') FOR [ReviewAndSubmit]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[sectionstatus] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[securityimage] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[securityquestion] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Feature_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Notes]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [MaxTransferLimit]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [MinTransferLimit]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [TransferDenominations]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsFutureTransaction]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [TransactionCharges]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsAuthorizationRequired]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsSMSAlertActivated]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [SMSCharges]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsBeneficiarySMSAlertActivated]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [BeneficiarySMSCharge]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [HasWeekendOperation]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsOutageMessageActive]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsAlertActive]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsTCActive]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsAgreementActive]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [IsCampaignActive]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [WorkSchedule_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [TransactionFee_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [TransactionLimit_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [code]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [Category_id]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [DisplayName]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [DisplayDescription]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[service] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[service_channels] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[service_permission_mapper] ADD  DEFAULT (NULL) FOR [object_name]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[servicechannel] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Priority]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Value]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Extension]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[servicetype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (NULL) FOR [name]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[signatorytype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[state] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[state] ADD  DEFAULT (NULL) FOR [country_id]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[status] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[statuschange] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[statustype] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (NULL) FOR [bic]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (NULL) FOR [countryCode]
GO
ALTER TABLE [${dbxschemaname}].[swiftcode] ADD  DEFAULT (N'INTERNATIONAL') FOR [countryRegion]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (NULL) FOR [id]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[systemconfiguration] ADD  DEFAULT (NULL) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [Code]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [FirstName]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [MiddleName]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [LastName]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [FailedCount]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [LastPasswordChangedts]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [ResetpasswordLink]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [ResetPasswordExpdts]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [lastLogints]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[systemuser] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[tbladdetails] ADD  DEFAULT (NULL) FOR [addetails]
GO
ALTER TABLE [${dbxschemaname}].[tbladdetails] ADD  DEFAULT (NULL) FOR [actiondetails]
GO
ALTER TABLE [${dbxschemaname}].[tbladdetails] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (NULL) FOR [Title]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[termandcondition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (N'1.0') FOR [Version_Id]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (N'Kony User') FOR [ContentModifiedBy]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (getdate()) FOR [ContentModifiedOn]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (N'SID_ACTIVE') FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] ADD  DEFAULT (N'0') FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[timeperiod] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactionfee] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactionfrequency] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [TransactionLimit_id]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [TransactionGroup_id]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[transactionlimit] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [transactionId]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [userId]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [userName]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [fromAccount]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [fromAccType]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [toAccount]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [toAccType]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [currency]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [beneficiaryRoutingNum]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [batchId]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [logId]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (NULL) FOR [type]
GO
ALTER TABLE [${dbxschemaname}].[transactionlogs] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[transactiontype] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [accountFrom]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [accountTo]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [createdDate]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [recurrenceType]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [frequency]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [occurrences]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [startDate]
GO
ALTER TABLE [${dbxschemaname}].[transferseries] ADD  DEFAULT (NULL) FOR [endDate]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (getdate()) FOR [Date]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [PlannedDepartureDate]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [PlannedReturnDate]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [Destinations]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [AdditionalNotes]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] ADD  DEFAULT (NULL) FOR [phonenumber]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [Application_id]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [Session_id]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [Device_id]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [ssn]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [userName]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [passWord]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [userFirstName]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [userLastName]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [countryCode]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_transfers]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [dateOfBirth]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_deposit]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [defaultModule_id]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_payments]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [secondaryphone]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [secondaryemail]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [lastlogintime]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [areUserAlertsTurnedOn]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [areDepositTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [areAccountStatementTermsAccepted]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [unsuccessfulLoginAttempts]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isUserAccountLocked]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [userImageURL]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [addressLine2]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [zipcode]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isSuperAdmin]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [userCompany]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [validDate]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [pin]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [isPinSet]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (N'BASIC') FOR [role]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [cvv]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [otp]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [lockCount]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((1)) FOR [isEnrolled]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [Bank_id]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_cardless]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isBillPaySupported]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isP2PSupported]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isBillPayActivated]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isP2PActivated]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_billPay]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_to_account_p2p]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_from_account_p2p]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isPhoneEnabled]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isEmailEnabled]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [secondaryemail2]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [secondaryphone2]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [token]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isWireTransferActivated]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [default_account_wire]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT ((0)) FOR [isWireTransferEligible]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (getdate()) FOR [currentLoginTime]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [maritalstatus]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [spousefirstname]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [spouselastname]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [noofdependents]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [gender]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (0x01) FOR [showBillPayFromAccPopup]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [drivingLicenseNumber]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [phoneExtension]
GO
ALTER TABLE [${dbxschemaname}].[user] ADD  DEFAULT (NULL) FOR [phoneCountryCode]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [AccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [minimumBalance]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [debitLimit]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [creditLimit]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [balanceUpdate_PeriodId]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [PayementDueReminder_PeriodId]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT (NULL) FOR [depositMaturityReminder_PeriodId]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT ((0)) FOR [successfulTransfer]
GO
ALTER TABLE [${dbxschemaname}].[useraccountalerts] ADD  DEFAULT ((0)) FOR [checkClearance]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [MemberId]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [Account_id]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [AccountName]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT ((0)) FOR [IsViewAllowed]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT ((0)) FOR [IsDepositAllowed]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT ((0)) FOR [IsWithdrawAllowed]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT ((0)) FOR [IsOrganizationAccount]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[useraccounts] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[useraddress] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [bankingIDChange]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [passwordChange]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [passwordExpired]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [communicationChange]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [newPayeeAdded]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [payeeDetailsUpdated]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [newDealsAvailable]
GO
ALTER TABLE [${dbxschemaname}].[useralerts] ADD  DEFAULT ((0)) FOR [dealsExpiring]
GO
ALTER TABLE [${dbxschemaname}].[usercashflow] ADD  DEFAULT ((0)) FOR [monthCash]
GO
ALTER TABLE [${dbxschemaname}].[usercashflow] ADD  DEFAULT ((0)) FOR [monthCredit]
GO
ALTER TABLE [${dbxschemaname}].[usercashflow] ADD  DEFAULT ((0)) FOR [totalCash]
GO
ALTER TABLE [${dbxschemaname}].[usercashflow] ADD  DEFAULT ((0)) FOR [totalCreditDebit]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [sequence]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [value]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [extension]
GO
ALTER TABLE [${dbxschemaname}].[usercommunication] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT ((0)) FOR [isEnabled]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] ADD  DEFAULT ((0)) FOR [isCreditCheck]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] ADD  DEFAULT ((0)) FOR [isSingatureUpload]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] ADD  DEFAULT (NULL) FOR [ssn]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] ADD  DEFAULT (NULL) FOR [newuser_id]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT ((1)) FOR [IsCustomer]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT ((1)) FOR [symbolsAllowed]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (NULL) FOR [supportedSymbols]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT (getdate()) FOR [lastsynctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[usernamerules] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[usernotification] ADD  DEFAULT (NULL) FOR [notification_id]
GO
ALTER TABLE [${dbxschemaname}].[usernotification] ADD  DEFAULT (NULL) FOR [user_id]
GO
ALTER TABLE [${dbxschemaname}].[usernotification] ADD  DEFAULT (NULL) FOR [isRead]
GO
ALTER TABLE [${dbxschemaname}].[usernotification] ADD  DEFAULT (getdate()) FOR [receivedDate]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[userpermission] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [dateOfBirth]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [gender]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [userfirstname]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [userlastname]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [maritalstatus]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [spouseFirstName]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [spouseLastName]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [noOfDependents]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [addressLine1]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [addressLine2]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [city]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [state]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [country]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [zipcode]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [employmentInfo]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [company]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [jobProfile]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [experience]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [annualIncome]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [assets]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [montlyExpenditure]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [ssn]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT ((0)) FOR [userPersonalInfo]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT ((0)) FOR [userEmploymentInfo]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT ((0)) FOR [userFinancialInfo]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT ((0)) FOR [userSecurityQuestions]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [spousename]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] ADD  DEFAULT (NULL) FOR [newuser_id]
GO
ALTER TABLE [${dbxschemaname}].[userproducts] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[userproducts] ADD  DEFAULT (NULL) FOR [Product_id]
GO
ALTER TABLE [${dbxschemaname}].[userproducts] ADD  DEFAULT (NULL) FOR [newuser_id]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT ((0)) FOR [hasSuperAdminPrivilages]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[userrole] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[usersecurity] ADD  DEFAULT (N'0') FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[usersecurity] ADD  DEFAULT (NULL) FOR [answer]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [Service_id]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [isTypeBusiness]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [PerDayLimit]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [PerMonthLimit]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [PerAccountLimit]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [HasApprove]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [HasDraftOnly]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [HasCancel]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [IsViewAll]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [IsViewNone]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT ((0)) FOR [IsViewOwnTransfersOnly]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[userserviceprefernces] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT (NULL) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT ((0)) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT ((0)) FOR [depositAmount]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT (NULL) FOR [referenceId]
GO
ALTER TABLE [${dbxschemaname}].[usertransactionhistory] ADD  DEFAULT ((0)) FOR [closingBalanceAmount]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemakes] ADD  DEFAULT (NULL) FOR [MakeId]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemakes] ADD  DEFAULT (NULL) FOR [MakeName]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemakes] ADD  DEFAULT (NULL) FOR [VehicleTypeId]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemakes] ADD  DEFAULT (NULL) FOR [VehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemakes] ADD  DEFAULT (NULL) FOR [ParentVehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [Model_ID]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [Model_Name]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [VehicleTypeId]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [VehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [ParentVehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [Make_ID]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [Make_Name]
GO
ALTER TABLE [${dbxschemaname}].[vehiclemodels] ADD  DEFAULT (NULL) FOR [Year]
GO
ALTER TABLE [${dbxschemaname}].[vehicletypes] ADD  DEFAULT (NULL) FOR [ParentVehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vehicletypes] ADD  DEFAULT (NULL) FOR [VehicleTypeName]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [VehicleType]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [Year]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [Make]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [Model]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT (NULL) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[vihicleinfo] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [transactionType]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [roleId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [onetime_id]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [wireFileExecution_id]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [wireTemplateExecution_id]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [requestId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [payeeAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [payeeId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [payeeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [status]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [confirmationNumber]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [transactionts]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [processingDate]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [personId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [fromNickName]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [fromAccountType]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [day1]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [day2]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [toAccountType]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [securityQuestion]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [SecurityAnswer]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [checkImageBack]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [profileId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [cardNumber]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [cardExpiry]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] ADD  DEFAULT (NULL) FOR [typeId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] ADD  DEFAULT (NULL) FOR [companyId]
GO
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] ADD  DEFAULT (NULL) FOR [cif]
GO
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] ADD  DEFAULT (NULL) FOR [isBusinessPayee]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (NULL) FOR [Description]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[workschedule] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[achfile]  WITH NOCHECK ADD  CONSTRAINT [achfile$FK_achfile_achfileformattype] FOREIGN KEY([achFileFormatType_id])
REFERENCES [${dbxschemaname}].[achfileformattype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achfile] CHECK CONSTRAINT [achfile$FK_achfile_achfileformattype]
GO
ALTER TABLE [${dbxschemaname}].[achfile]  WITH NOCHECK ADD  CONSTRAINT [achfile$FK_achfile_Company_id] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achfile] CHECK CONSTRAINT [achfile$FK_achfile_Company_id]
GO
ALTER TABLE [${dbxschemaname}].[achfile]  WITH NOCHECK ADD  CONSTRAINT [achfile$FK_achfile_user] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achfile] CHECK CONSTRAINT [achfile$FK_achfile_user]
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord]  WITH NOCHECK ADD  CONSTRAINT [achfilerecord$FK_achfilerecord_achFileId] FOREIGN KEY([achFileId])
REFERENCES [${dbxschemaname}].[achfile] ([achFile_id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[achfilerecord] CHECK CONSTRAINT [achfilerecord$FK_achfilerecord_achFileId]
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord]  WITH NOCHECK ADD  CONSTRAINT [achfilesubrecord$FK_achfilesubrecord_achFileRecordId] FOREIGN KEY([achFileRecordId])
REFERENCES [${dbxschemaname}].[achfilerecord] ([achFileRecordId])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[achfilesubrecord] CHECK CONSTRAINT [achfilesubrecord$FK_achfilesubrecord_achFileRecordId]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction]  WITH NOCHECK ADD  CONSTRAINT [achtransaction$FK_bbtransaction_Organization] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] CHECK CONSTRAINT [achtransaction$FK_bbtransaction_Organization]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction]  WITH NOCHECK ADD  CONSTRAINT [achtransaction$FK_bbtransaction_Template_Type] FOREIGN KEY([templateType_id])
REFERENCES [${dbxschemaname}].[bbtemplatetype] ([templateType_id])
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] CHECK CONSTRAINT [achtransaction$FK_bbtransaction_Template_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction]  WITH NOCHECK ADD  CONSTRAINT [achtransaction$FK_bbtransaction_TemplateRequest_Type] FOREIGN KEY([templateRequestType_id])
REFERENCES [${dbxschemaname}].[bbtemplaterequesttype] ([templateRequestType_id])
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] CHECK CONSTRAINT [achtransaction$FK_bbtransaction_TemplateRequest_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction]  WITH NOCHECK ADD  CONSTRAINT [achtransaction$FK_bbtransaction_Transaction_Type] FOREIGN KEY([transactionType_id])
REFERENCES [${dbxschemaname}].[bbtransactiontype] ([transactionType_id])
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] CHECK CONSTRAINT [achtransaction$FK_bbtransaction_Transaction_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransaction]  WITH NOCHECK ADD  CONSTRAINT [achtransaction$FK_bbtransaction_user] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achtransaction] CHECK CONSTRAINT [achtransaction$FK_bbtransaction_user]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_Achaccount_Type] FOREIGN KEY([toAccountType])
REFERENCES [${dbxschemaname}].[achaccountstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] CHECK CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_Achaccount_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_Tax_Type] FOREIGN KEY([taxType_id])
REFERENCES [${dbxschemaname}].[bbtaxtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] CHECK CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_Tax_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_TemplateRequest_Type] FOREIGN KEY([templateRequestType_id])
REFERENCES [${dbxschemaname}].[bbtemplaterequesttype] ([templateRequestType_id])
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] CHECK CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_TemplateRequest_Type]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_TransactionId] FOREIGN KEY([transaction_id])
REFERENCES [${dbxschemaname}].[achtransaction] ([transaction_id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[achtransactionrecord] CHECK CONSTRAINT [achtransactionrecord$FK_bbtransactionrecord_TransactionId]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionsubrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionsubrecord$FK_bbtransactionsubrecord_Taxsub_Type_id] FOREIGN KEY([taxSubCategory_id])
REFERENCES [${dbxschemaname}].[bbtaxsubtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[achtransactionsubrecord] CHECK CONSTRAINT [achtransactionsubrecord$FK_bbtransactionsubrecord_Taxsub_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[achtransactionsubrecord]  WITH NOCHECK ADD  CONSTRAINT [achtransactionsubrecord$FK_bbtransactionsubrecord_TransactionRecord_id] FOREIGN KEY([transactionRecord_id])
REFERENCES [${dbxschemaname}].[achtransactionrecord] ([transactionRecord_id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[achtransactionsubrecord] CHECK CONSTRAINT [achtransactionsubrecord$FK_bbtransactionsubrecord_TransactionRecord_id]
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]  WITH NOCHECK ADD  CONSTRAINT [actiondisplaynamedescription$FK_actiondisplaynamedescription_Action_id] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] CHECK CONSTRAINT [actiondisplaynamedescription$FK_actiondisplaynamedescription_Action_id]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit]  WITH NOCHECK ADD  CONSTRAINT [actionlimit$FK_actionlimit_featureaction] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] CHECK CONSTRAINT [actionlimit$FK_actionlimit_featureaction]
GO
ALTER TABLE [${dbxschemaname}].[actionlimit]  WITH NOCHECK ADD  CONSTRAINT [actionlimit$FK_actionlimit_limittype] FOREIGN KEY([LimitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] CHECK CONSTRAINT [actionlimit$FK_actionlimit_limittype]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata]  WITH NOCHECK ADD  CONSTRAINT [additionaldata$FK_AdditionalData_AdditionalFieldId] FOREIGN KEY([AdditionalField_id])
REFERENCES [${dbxschemaname}].[additionalfield] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] CHECK CONSTRAINT [additionaldata$FK_AdditionalData_AdditionalFieldId]
GO
ALTER TABLE [${dbxschemaname}].[additionaldata]  WITH NOCHECK ADD  CONSTRAINT [additionaldata$FK_AdditionalData_ObjectId] FOREIGN KEY([Object_id])
REFERENCES [${dbxschemaname}].[product] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionaldata] CHECK CONSTRAINT [additionaldata$FK_AdditionalData_ObjectId]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] CHECK CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_IncomeResponse] FOREIGN KEY([IncomeResponse_id])
REFERENCES [${dbxschemaname}].[incomeresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] CHECK CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_IncomeResponse]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_OptionItem] FOREIGN KEY([BusinessShare_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] CHECK CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_OptionItem2] FOREIGN KEY([EmploymentType_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalemploymentsresponse] CHECK CONSTRAINT [additionalemploymentsresponse$FK_AdditionalEmploymentsResponse_OptionItem2]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_AdditionalEmploymentsResponse] FOREIGN KEY([AdditionalEmploymentsResponse_id])
REFERENCES [${dbxschemaname}].[additionalemploymentsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] CHECK CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_AdditionalEmploymentsResponse]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] CHECK CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_OptionItem] FOREIGN KEY([IncomeDetail_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] CHECK CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_OptionItem2] FOREIGN KEY([PayPeriod_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] CHECK CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_OptionItem2]
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalincomesresponse] CHECK CONSTRAINT [additionalincomesresponse$FK_AdditionalIncomesResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalmortgageheaderresponse$FK_AdditionalMortgageHeaderResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] CHECK CONSTRAINT [additionalmortgageheaderresponse$FK_AdditionalMortgageHeaderResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalmortgageheaderresponse$FK_AdditionalMortgageHeaderResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageheaderresponse] CHECK CONSTRAINT [additionalmortgageheaderresponse$FK_AdditionalMortgageHeaderResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalmortgageresponse$FK_AdditionalMortgageResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] CHECK CONSTRAINT [additionalmortgageresponse$FK_AdditionalMortgageResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse]  WITH NOCHECK ADD  CONSTRAINT [additionalmortgageresponse$FK_AdditionalMortgageResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[additionalmortgageresponse] CHECK CONSTRAINT [additionalmortgageresponse$FK_AdditionalMortgageResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[address]  WITH NOCHECK ADD  CONSTRAINT [address$FK_Address_Region] FOREIGN KEY([Region_id])
REFERENCES [${dbxschemaname}].[region] ([id])
GO
ALTER TABLE [${dbxschemaname}].[address] CHECK CONSTRAINT [address$FK_Address_Region]
GO
ALTER TABLE [${dbxschemaname}].[alert]  WITH NOCHECK ADD  CONSTRAINT [alert$FK_Alert_AlertType] FOREIGN KEY([AlertType_id])
REFERENCES [${dbxschemaname}].[alerttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[alert] CHECK CONSTRAINT [alert$FK_Alert_AlertType]
GO
ALTER TABLE [${dbxschemaname}].[alertattribute]  WITH NOCHECK ADD  CONSTRAINT [alertattribute$FK_alertattribute_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[alertattribute] CHECK CONSTRAINT [alertattribute$FK_alertattribute_locale]
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues]  WITH NOCHECK ADD  CONSTRAINT [alertattributelistvalues$FK_alertattributelistvalues_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] CHECK CONSTRAINT [alertattributelistvalues$FK_alertattributelistvalues_locale]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel]  WITH NOCHECK ADD  CONSTRAINT [alertcategorychannel$FK_alertcategorychannel_channel] FOREIGN KEY([ChannelID])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] CHECK CONSTRAINT [alertcategorychannel$FK_alertcategorychannel_channel]
GO
ALTER TABLE [${dbxschemaname}].[alertcondition]  WITH NOCHECK ADD  CONSTRAINT [alertcondition$FK_alertcondition_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[alertcondition] CHECK CONSTRAINT [alertcondition$FK_alertcondition_locale]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory]  WITH NOCHECK ADD  CONSTRAINT [alerthistory$FK_alerthistory_alertsubtype] FOREIGN KEY([AlertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] CHECK CONSTRAINT [alerthistory$FK_alerthistory_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory]  WITH NOCHECK ADD  CONSTRAINT [alerthistory$FK_alerthistory_customer] FOREIGN KEY([Customer_Id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] CHECK CONSTRAINT [alerthistory$FK_alerthistory_customer]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype]  WITH NOCHECK ADD  CONSTRAINT [alertsubtype$FK_alertsubtype_alerttype] FOREIGN KEY([AlertTypeId])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] CHECK CONSTRAINT [alertsubtype$FK_alertsubtype_alerttype]
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate]  WITH NOCHECK ADD  CONSTRAINT [annualpercentagerate$FK_AnnualPercentageRate_LoanType] FOREIGN KEY([LoanType_id])
REFERENCES [${dbxschemaname}].[loantype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[annualpercentagerate] CHECK CONSTRAINT [annualpercentagerate$FK_AnnualPercentageRate_LoanType]
GO
ALTER TABLE [${dbxschemaname}].[appaction]  WITH NOCHECK ADD  CONSTRAINT [appaction$FK_appaction_action_id] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[appaction] CHECK CONSTRAINT [appaction$FK_appaction_action_id]
GO
ALTER TABLE [${dbxschemaname}].[appaction]  WITH NOCHECK ADD  CONSTRAINT [appaction$FK_appaction_app_id] FOREIGN KEY([App_id])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[appaction] CHECK CONSTRAINT [appaction$FK_appaction_app_id]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix]  WITH NOCHECK ADD  CONSTRAINT [approvalmatrix$FK_approvalmatrix_actionid] FOREIGN KEY([actionId])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] CHECK CONSTRAINT [approvalmatrix$FK_approvalmatrix_actionid]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix]  WITH NOCHECK ADD  CONSTRAINT [approvalmatrix$FK_approvalmatrix_approvalruleid] FOREIGN KEY([approvalruleId])
REFERENCES [${dbxschemaname}].[approvalrule] ([id])
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] CHECK CONSTRAINT [approvalmatrix$FK_approvalmatrix_approvalruleid]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix]  WITH NOCHECK ADD  CONSTRAINT [approvalmatrix$FK_approvalmatrix_Companyid] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] CHECK CONSTRAINT [approvalmatrix$FK_approvalmatrix_Companyid]
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix]  WITH NOCHECK ADD  CONSTRAINT [approvalmatrix$FK_approvalmatrix_limittypeid] FOREIGN KEY([limitTypeId])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] CHECK CONSTRAINT [approvalmatrix$FK_approvalmatrix_limittypeid]
GO
ALTER TABLE [${dbxschemaname}].[appversion]  WITH NOCHECK ADD  CONSTRAINT [appversion$appversion_app] FOREIGN KEY([app])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[appversion] CHECK CONSTRAINT [appversion$appversion_app]
GO
ALTER TABLE [${dbxschemaname}].[appversion]  WITH NOCHECK ADD  CONSTRAINT [appversion$appversion_appchannel] FOREIGN KEY([channel])
REFERENCES [${dbxschemaname}].[appchannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[appversion] CHECK CONSTRAINT [appversion$appversion_appchannel]
GO
ALTER TABLE [${dbxschemaname}].[appversion]  WITH NOCHECK ADD  CONSTRAINT [appversion$appversion_appversiontype] FOREIGN KEY([versionType])
REFERENCES [${dbxschemaname}].[appversiontype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[appversion] CHECK CONSTRAINT [appversion$appversion_appversiontype]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest]  WITH NOCHECK ADD  CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_AssignedTo] FOREIGN KEY([AssignedTo])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] CHECK CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_AssignedTo]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest]  WITH NOCHECK ADD  CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] CHECK CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_Customer]
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest]  WITH NOCHECK ADD  CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_RequestCategory] FOREIGN KEY([RequestCategory_id])
REFERENCES [${dbxschemaname}].[requestcategory] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] CHECK CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_RequestCategory]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead]  WITH NOCHECK ADD  CONSTRAINT [archivedlead$FK_archivedlead_customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] CHECK CONSTRAINT [archivedlead$FK_archivedlead_customer]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead]  WITH NOCHECK ADD  CONSTRAINT [archivedlead$FK_archivedlead_product] FOREIGN KEY([product_id])
REFERENCES [${dbxschemaname}].[product] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] CHECK CONSTRAINT [archivedlead$FK_archivedlead_product]
GO
ALTER TABLE [${dbxschemaname}].[archivedlead]  WITH NOCHECK ADD  CONSTRAINT [archivedlead$FK_archivedlead_systemuser] FOREIGN KEY([csr_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedlead] CHECK CONSTRAINT [archivedlead$FK_archivedlead_systemuser]
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote]  WITH NOCHECK ADD  CONSTRAINT [archivedleadnote$FK_archivedleadnote_archivedlead] FOREIGN KEY([lead_Id])
REFERENCES [${dbxschemaname}].[archivedlead] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedleadnote] CHECK CONSTRAINT [archivedleadnote$FK_archivedleadnote_archivedlead]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment]  WITH NOCHECK ADD  CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_AttachementType] FOREIGN KEY([AttachmentType_id])
REFERENCES [${dbxschemaname}].[attachmenttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] CHECK CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_AttachementType]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment]  WITH NOCHECK ADD  CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_Media] FOREIGN KEY([Media_id])
REFERENCES [${dbxschemaname}].[archivedmedia] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] CHECK CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_Media]
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment]  WITH NOCHECK ADD  CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_RequestMessage] FOREIGN KEY([RequestMessage_id])
REFERENCES [${dbxschemaname}].[archivedrequestmessage] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] CHECK CONSTRAINT [archivedmessageattachment$FK_ArchivedMessageAttachement_RequestMessage]
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage]  WITH NOCHECK ADD  CONSTRAINT [archivedrequestmessage$FK_ArchivedRequestMessage_CustomerRequest] FOREIGN KEY([CustomerRequest_id])
REFERENCES [${dbxschemaname}].[archivedcustomerrequest] ([id])
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] CHECK CONSTRAINT [archivedrequestmessage$FK_ArchivedRequestMessage_CustomerRequest]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse]  WITH NOCHECK ADD  CONSTRAINT [assetsresponse$FK_AssetsResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] CHECK CONSTRAINT [assetsresponse$FK_AssetsResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse]  WITH NOCHECK ADD  CONSTRAINT [assetsresponse$FK_AssetsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[assetsresponse] CHECK CONSTRAINT [assetsresponse$FK_AssetsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse]  WITH NOCHECK ADD  CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_AssetsResponse] FOREIGN KEY([AssetsResponse_id])
REFERENCES [${dbxschemaname}].[assetsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] CHECK CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_AssetsResponse]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse]  WITH NOCHECK ADD  CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] CHECK CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse]  WITH NOCHECK ADD  CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankaccountsresponse] CHECK CONSTRAINT [bankaccountsresponse$FK_BankAccountsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[bankbranch]  WITH NOCHECK ADD  CONSTRAINT [bankbranch$FK_branchtype_id] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[branchtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankbranch] CHECK CONSTRAINT [bankbranch$FK_branchtype_id]
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer]  WITH NOCHECK ADD  CONSTRAINT [bankfortransfer$FK_BankForTransfer_AddressID] FOREIGN KEY([Address_id])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankfortransfer] CHECK CONSTRAINT [bankfortransfer$FK_BankForTransfer_AddressID]
GO
ALTER TABLE [${dbxschemaname}].[bankservice]  WITH NOCHECK ADD  CONSTRAINT [bankservice$FK_BankService_BankForTransferID] FOREIGN KEY([BankForTransfer_id])
REFERENCES [${dbxschemaname}].[bankfortransfer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankservice] CHECK CONSTRAINT [bankservice$FK_BankService_BankForTransferID]
GO
ALTER TABLE [${dbxschemaname}].[bankservice]  WITH NOCHECK ADD  CONSTRAINT [bankservice$FK_BankService_ServiceID] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bankservice] CHECK CONSTRAINT [bankservice$FK_BankService_ServiceID]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest]  WITH NOCHECK ADD  CONSTRAINT [bbactedrequest$FK_bbactedrequest_Organisation_id] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] CHECK CONSTRAINT [bbactedrequest$FK_bbactedrequest_Organisation_id]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest]  WITH NOCHECK ADD  CONSTRAINT [bbactedrequest$FK_bbactedrequest_requestId] FOREIGN KEY([requestId])
REFERENCES [${dbxschemaname}].[bbrequest] ([requestId])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] CHECK CONSTRAINT [bbactedrequest$FK_bbactedrequest_requestId]
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest]  WITH NOCHECK ADD  CONSTRAINT [bbactedrequest$FK_bbactedrequest_user_id] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbactedrequest] CHECK CONSTRAINT [bbactedrequest$FK_bbactedrequest_user_id]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest]  WITH NOCHECK ADD  CONSTRAINT [bbrequest$FK_bbrequest_businessbankingcompany] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] CHECK CONSTRAINT [bbrequest$FK_bbrequest_businessbankingcompany]
GO
ALTER TABLE [${dbxschemaname}].[bbrequest]  WITH NOCHECK ADD  CONSTRAINT [bbrequest$FK_bbrequest_customer] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbrequest] CHECK CONSTRAINT [bbrequest$FK_bbrequest_customer]
GO
ALTER TABLE [${dbxschemaname}].[bbtaxsubtype]  WITH NOCHECK ADD  CONSTRAINT [bbtaxsubtype$FK_bbtaxsubtype_bbtaxtype] FOREIGN KEY([taxType])
REFERENCES [${dbxschemaname}].[bbtaxtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtaxsubtype] CHECK CONSTRAINT [bbtaxsubtype$FK_bbtaxsubtype_bbtaxtype]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_Organization] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_Organization]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_Template_Type] FOREIGN KEY([templateType_id])
REFERENCES [${dbxschemaname}].[bbtemplatetype] ([templateType_id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_Template_Type]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_TemplateRequest_Type] FOREIGN KEY([templateRequestType_id])
REFERENCES [${dbxschemaname}].[bbtemplaterequesttype] ([templateRequestType_id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_TemplateRequest_Type]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_Transaction_Type] FOREIGN KEY([transactionType_id])
REFERENCES [${dbxschemaname}].[bbtransactiontype] ([transactionType_id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_Transaction_Type]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_user] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_user]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate]  WITH NOCHECK ADD  CONSTRAINT [bbtemplate$FK_bbtemplate_user_2] FOREIGN KEY([updatedBy])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplate] CHECK CONSTRAINT [bbtemplate$FK_bbtemplate_user_2]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Account_Type_id] FOREIGN KEY([toAccountType])
REFERENCES [${dbxschemaname}].[achaccountstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] CHECK CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Account_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Tax_Type_id] FOREIGN KEY([taxType_id])
REFERENCES [${dbxschemaname}].[bbtaxtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] CHECK CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Tax_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Template_id] FOREIGN KEY([template_id])
REFERENCES [${dbxschemaname}].[bbtemplate] ([templateId])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] CHECK CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Template_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Template_Request_Type_id] FOREIGN KEY([templateRequestType_id])
REFERENCES [${dbxschemaname}].[bbtemplaterequesttype] ([templateRequestType_id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterecord] CHECK CONSTRAINT [bbtemplaterecord$FK_bbtemplaterecord_Template_Request_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterequesttype]  WITH NOCHECK ADD  CONSTRAINT [bbtemplaterequesttype$FK_bbtemplaterequesttype_TransactionType_id] FOREIGN KEY([transactionType_id])
REFERENCES [${dbxschemaname}].[bbtransactiontype] ([transactionType_id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplaterequesttype] CHECK CONSTRAINT [bbtemplaterequesttype$FK_bbtemplaterequesttype_TransactionType_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplatesubrecord$FK_bbtemplatesubrecord_Taxsub_Type_id] FOREIGN KEY([taxSubCategory_id])
REFERENCES [${dbxschemaname}].[bbtaxsubtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord] CHECK CONSTRAINT [bbtemplatesubrecord$FK_bbtemplatesubrecord_Taxsub_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord]  WITH NOCHECK ADD  CONSTRAINT [bbtemplatesubrecord$FK_bbtemplatesubrecord_TemplateRecord_id] FOREIGN KEY([templateRecord_id])
REFERENCES [${dbxschemaname}].[bbtemplaterecord] ([templateRecord_id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[bbtemplatesubrecord] CHECK CONSTRAINT [bbtemplatesubrecord$FK_bbtemplatesubrecord_TemplateRecord_id]
GO
ALTER TABLE [${dbxschemaname}].[billermaster]  WITH NOCHECK ADD  CONSTRAINT [billermaster$FK_billermaster_billercategory] FOREIGN KEY([billerCategoryId])
REFERENCES [${dbxschemaname}].[billercategory] ([id])
GO
ALTER TABLE [${dbxschemaname}].[billermaster] CHECK CONSTRAINT [billermaster$FK_billermaster_billercategory]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers]  WITH NOCHECK ADD  CONSTRAINT [billpaytransfers$FK_billpaytranfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] CHECK CONSTRAINT [billpaytransfers$FK_billpaytranfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers]  WITH NOCHECK ADD  CONSTRAINT [billpaytransfers$FK_billpaytranfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[billpaytransfers] CHECK CONSTRAINT [billpaytransfers$FK_billpaytranfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[browsersupport]  WITH NOCHECK ADD  CONSTRAINT [browsersupport$browsersupport_supporttype_id] FOREIGN KEY([supportType])
REFERENCES [${dbxschemaname}].[browsersupporttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[browsersupport] CHECK CONSTRAINT [browsersupport$browsersupport_supporttype_id]
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext]  WITH NOCHECK ADD  CONSTRAINT [browsersupportdisplaynametext$browsersupportdisplaynametext_browsersupport_id] FOREIGN KEY([BrowserSupport_id])
REFERENCES [${dbxschemaname}].[browsersupport] ([id])
GO
ALTER TABLE [${dbxschemaname}].[browsersupportdisplaynametext] CHECK CONSTRAINT [browsersupportdisplaynametext$browsersupportdisplaynametext_browsersupport_id]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems]  WITH NOCHECK ADD  CONSTRAINT [bulkwirefilelineitems$FK_bulkwirelineitems_bulkwirefileid] FOREIGN KEY([bulkWireFileID])
REFERENCES [${dbxschemaname}].[bulkwirefiles] ([bulkWireFileID])
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] CHECK CONSTRAINT [bulkwirefilelineitems$FK_bulkwirelineitems_bulkwirefileid]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems]  WITH NOCHECK ADD  CONSTRAINT [bulkwirefilelineitems$FK_bulkwirelineitems_curency] FOREIGN KEY([currency])
REFERENCES [${dbxschemaname}].[currency] ([code])
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefilelineitems] CHECK CONSTRAINT [bulkwirefilelineitems$FK_bulkwirelineitems_curency]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles]  WITH NOCHECK ADD  CONSTRAINT [bulkwirefiles$FK_bulkwirefiles_CompanyID] FOREIGN KEY([company_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] CHECK CONSTRAINT [bulkwirefiles$FK_bulkwirefiles_CompanyID]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles]  WITH NOCHECK ADD  CONSTRAINT [bulkwirefiles$FK_bulkwirefiles_FileFormat] FOREIGN KEY([fileFormatCode])
REFERENCES [${dbxschemaname}].[bulkwirefileformattype] ([bulkWiresFileFormatTypeCode])
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiles] CHECK CONSTRAINT [bulkwirefiles$FK_bulkwirefiles_FileFormat]
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails]  WITH NOCHECK ADD  CONSTRAINT [bulkwirefiletransactdetails$FK_transactionID_bulkwirefileID] FOREIGN KEY([bulkWireFileID])
REFERENCES [${dbxschemaname}].[bulkwirefiles] ([bulkWireFileID])
GO
ALTER TABLE [${dbxschemaname}].[bulkwirefiletransactdetails] CHECK CONSTRAINT [bulkwirefiletransactdetails$FK_transactionID_bulkwirefileID]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile]  WITH NOCHECK ADD  CONSTRAINT [bulkwiresamplefile$FK_bulkwiresample_Fileformat] FOREIGN KEY([bulkWireSampleFileFormatCode])
REFERENCES [${dbxschemaname}].[bulkwirefileformattype] ([bulkWiresFileFormatTypeCode])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiresamplefile] CHECK CONSTRAINT [bulkwiresamplefile$FK_bulkwiresample_Fileformat]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplate$FK1_bulkwiretemplate_CreatedBy] FOREIGN KEY([createdBy])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] CHECK CONSTRAINT [bulkwiretemplate$FK1_bulkwiretemplate_CreatedBy]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplate$FK2_bulkwiretemplate_CompanyID] FOREIGN KEY([company_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] CHECK CONSTRAINT [bulkwiretemplate$FK2_bulkwiretemplate_CompanyID]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplate$FK3_bulkwiretemplate_ModifiedBy] FOREIGN KEY([modifiedBy])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] CHECK CONSTRAINT [bulkwiretemplate$FK3_bulkwiretemplate_ModifiedBy]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplate$FK4_bulkwiretemplate_DefaultCurrency] FOREIGN KEY([defaultCurrency])
REFERENCES [${dbxschemaname}].[currency] ([code])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] CHECK CONSTRAINT [bulkwiretemplate$FK4_bulkwiretemplate_DefaultCurrency]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplatelineitems$FK1_bulkwiretemplatelineitems_bulkwireTemplateID] FOREIGN KEY([bulkWireTemplateID])
REFERENCES [${dbxschemaname}].[bulkwiretemplate] ([bulkWireTemplateID])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] CHECK CONSTRAINT [bulkwiretemplatelineitems$FK1_bulkwiretemplatelineitems_bulkwireTemplateID]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplatelineitems$FK2_bulkwiretemplatelineitems_payeeID] FOREIGN KEY([payeeId])
REFERENCES [${dbxschemaname}].[payee] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] CHECK CONSTRAINT [bulkwiretemplatelineitems$FK2_bulkwiretemplatelineitems_payeeID]
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails]  WITH NOCHECK ADD  CONSTRAINT [bulkwiretemplatetransactdetails$FK1_bulkwiretemplatetransactdetails_bulkwiretemplateID] FOREIGN KEY([bulkWireTemplateID])
REFERENCES [${dbxschemaname}].[bulkwiretemplate] ([bulkWireTemplateID])
GO
ALTER TABLE [${dbxschemaname}].[bulkwiretemplatetransactdetails] CHECK CONSTRAINT [bulkwiretemplatetransactdetails$FK1_bulkwiretemplatetransactdetails_bulkwiretemplateID]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory]  WITH NOCHECK ADD  CONSTRAINT [businesssignatory$FK_BusinessSignatory_BusinessType] FOREIGN KEY([BusinessType_id])
REFERENCES [${dbxschemaname}].[businesstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] CHECK CONSTRAINT [businesssignatory$FK_BusinessSignatory_BusinessType]
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory]  WITH NOCHECK ADD  CONSTRAINT [businesssignatory$FK_BusinessSignatory_Signatory] FOREIGN KEY([Signatory_id])
REFERENCES [${dbxschemaname}].[signatorytype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[businesssignatory] CHECK CONSTRAINT [businesssignatory$FK_BusinessSignatory_Signatory]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup]  WITH NOCHECK ADD  CONSTRAINT [campaigngroup$FK_CampaignGroup_campaign_id] FOREIGN KEY([campaign_id])
REFERENCES [${dbxschemaname}].[campaign] ([id])
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] CHECK CONSTRAINT [campaigngroup$FK_CampaignGroup_campaign_id]
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup]  WITH NOCHECK ADD  CONSTRAINT [campaigngroup$FK_CampaignGroup_group_id] FOREIGN KEY([group_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[campaigngroup] CHECK CONSTRAINT [campaigngroup$FK_CampaignGroup_group_id]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification]  WITH NOCHECK ADD  CONSTRAINT [campaignspecification$FK_CampaignSpecification_campaign_id] FOREIGN KEY([campaign_id])
REFERENCES [${dbxschemaname}].[campaign] ([id])
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] CHECK CONSTRAINT [campaignspecification$FK_CampaignSpecification_campaign_id]
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification]  WITH NOCHECK ADD  CONSTRAINT [campaignspecification$FK_CampaignSpecification_campaignplaceholder_id] FOREIGN KEY([campaignplaceholder_id])
REFERENCES [${dbxschemaname}].[campaignplaceholder] ([id])
GO
ALTER TABLE [${dbxschemaname}].[campaignspecification] CHECK CONSTRAINT [campaignspecification$FK_CampaignSpecification_campaignplaceholder_id]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest]  WITH NOCHECK ADD  CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_Address] FOREIGN KEY([Address_id])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] CHECK CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_Address]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest]  WITH NOCHECK ADD  CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_CardAccountRequestType] FOREIGN KEY([RequestType_id])
REFERENCES [${dbxschemaname}].[cardaccountrequesttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] CHECK CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_CardAccountRequestType]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest]  WITH NOCHECK ADD  CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] CHECK CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_Customer]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest]  WITH NOCHECK ADD  CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_CustomerCommunication] FOREIGN KEY([Communication_id])
REFERENCES [${dbxschemaname}].[customercommunication] ([id])
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] CHECK CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_CustomerCommunication]
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest]  WITH NOCHECK ADD  CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_ServiceChannel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[cardaccountrequest] CHECK CONSTRAINT [cardaccountrequest$FK_CardAccountRequest_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[channeltext]  WITH NOCHECK ADD  CONSTRAINT [channeltext$FK_channeltext_channel] FOREIGN KEY([channelID])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[channeltext] CHECK CONSTRAINT [channeltext$FK_channeltext_channel]
GO
ALTER TABLE [${dbxschemaname}].[city]  WITH NOCHECK ADD  CONSTRAINT [city$FK_City_Country] FOREIGN KEY([Country_id])
REFERENCES [${dbxschemaname}].[country] ([id])
GO
ALTER TABLE [${dbxschemaname}].[city] CHECK CONSTRAINT [city$FK_City_Country]
GO
ALTER TABLE [${dbxschemaname}].[city]  WITH NOCHECK ADD  CONSTRAINT [city$FK_City_Region] FOREIGN KEY([Region_id])
REFERENCES [${dbxschemaname}].[region] ([id])
GO
ALTER TABLE [${dbxschemaname}].[city] CHECK CONSTRAINT [city$FK_City_Region]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate]  WITH NOCHECK ADD  CONSTRAINT [communicationtemplate$FK_communicationtemplate_alertsubtype] FOREIGN KEY([AlertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] CHECK CONSTRAINT [communicationtemplate$FK_communicationtemplate_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate]  WITH NOCHECK ADD  CONSTRAINT [communicationtemplate$FK_communicationtemplate_channel] FOREIGN KEY([ChannelID])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] CHECK CONSTRAINT [communicationtemplate$FK_communicationtemplate_channel]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction]  WITH NOCHECK ADD  CONSTRAINT [compositeaction$FK_CompositeAction_Action] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] CHECK CONSTRAINT [compositeaction$FK_CompositeAction_Action]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction]  WITH NOCHECK ADD  CONSTRAINT [compositeaction$FK_CompositeAction_Feature] FOREIGN KEY([Feature_id])
REFERENCES [${dbxschemaname}].[feature] ([id])
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] CHECK CONSTRAINT [compositeaction$FK_CompositeAction_Feature]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction]  WITH NOCHECK ADD  CONSTRAINT [compositeaction$FK_CompositeAction_PermissionId] FOREIGN KEY([Permission_id])
REFERENCES [${dbxschemaname}].[permission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] CHECK CONSTRAINT [compositeaction$FK_CompositeAction_PermissionId]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission]  WITH NOCHECK ADD  CONSTRAINT [compositepermission$FK_CompositePermission_Entitlement] FOREIGN KEY([Entitlement_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] CHECK CONSTRAINT [compositepermission$FK_CompositePermission_Entitlement]
GO
ALTER TABLE [${dbxschemaname}].[compositepermission]  WITH NOCHECK ADD  CONSTRAINT [compositepermission$FK_CompositePermission_PermissionId] FOREIGN KEY([Permission_id])
REFERENCES [${dbxschemaname}].[permission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[compositepermission] CHECK CONSTRAINT [compositepermission$FK_CompositePermission_PermissionId]
GO
ALTER TABLE [${dbxschemaname}].[configurations]  WITH NOCHECK ADD  CONSTRAINT [configurations$FK_bundle_id] FOREIGN KEY([bundle_id])
REFERENCES [${dbxschemaname}].[configurationbundles] ([bundle_id])
GO
ALTER TABLE [${dbxschemaname}].[configurations] CHECK CONSTRAINT [configurations$FK_bundle_id]
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant]  WITH NOCHECK ADD  CONSTRAINT [csrassistgrant$FK_CSRAssistGrant_Customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] CHECK CONSTRAINT [csrassistgrant$FK_CSRAssistGrant_Customer]
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign]  WITH NOCHECK ADD  CONSTRAINT [custcompletedcampaign$fk_customerid] FOREIGN KEY([customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[custcompletedcampaign] CHECK CONSTRAINT [custcompletedcampaign$fk_customerid]
GO
ALTER TABLE [${dbxschemaname}].[customer]  WITH NOCHECK ADD  CONSTRAINT [customer$FK_Customer_IDType] FOREIGN KEY([IDType_id])
REFERENCES [${dbxschemaname}].[idtype] ([IDType])
GO
ALTER TABLE [${dbxschemaname}].[customer] CHECK CONSTRAINT [customer$FK_Customer_IDType]
GO
ALTER TABLE [${dbxschemaname}].[customer]  WITH NOCHECK ADD  CONSTRAINT [customer$FK_Customer_Location] FOREIGN KEY([Location_id])
REFERENCES [${dbxschemaname}].[location] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customer] CHECK CONSTRAINT [customer$FK_Customer_Location]
GO
ALTER TABLE [${dbxschemaname}].[customer]  WITH NOCHECK ADD  CONSTRAINT [customer$FK_Customer_SecurityImage] FOREIGN KEY([SecurityImage_id])
REFERENCES [${dbxschemaname}].[securityimage] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customer] CHECK CONSTRAINT [customer$FK_Customer_SecurityImage]
GO
ALTER TABLE [${dbxschemaname}].[customer]  WITH NOCHECK ADD  CONSTRAINT [customer$FK_Customer_Type_id] FOREIGN KEY([CustomerType_id])
REFERENCES [${dbxschemaname}].[customertype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customer] CHECK CONSTRAINT [customer$FK_Customer_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[customeraction]  WITH NOCHECK ADD  CONSTRAINT [customeraction$FK_CustomerActionLimit_Action] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraction] CHECK CONSTRAINT [customeraction$FK_CustomerActionLimit_Action]
GO
ALTER TABLE [${dbxschemaname}].[customeraction]  WITH NOCHECK ADD  CONSTRAINT [customeraction$FK_CustomerActionLimit_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraction] CHECK CONSTRAINT [customeraction$FK_CustomerActionLimit_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customeraction]  WITH NOCHECK ADD  CONSTRAINT [customeraction$FK_CustomerActionLimit_customertype] FOREIGN KEY([RoleType_id])
REFERENCES [${dbxschemaname}].[membergrouptype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraction] CHECK CONSTRAINT [customeraction$FK_CustomerActionLimit_customertype]
GO
ALTER TABLE [${dbxschemaname}].[customeraction]  WITH NOCHECK ADD  CONSTRAINT [customeraction$FK_CustomerActionLimit_limittype] FOREIGN KEY([LimitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraction] CHECK CONSTRAINT [customeraction$FK_CustomerActionLimit_limittype]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress]  WITH NOCHECK ADD  CONSTRAINT [customeraddress$FK_CustomerAddress_Address] FOREIGN KEY([Address_id])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] CHECK CONSTRAINT [customeraddress$FK_CustomerAddress_Address]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress]  WITH NOCHECK ADD  CONSTRAINT [customeraddress$FK_CustomerAddress_AddressType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[addresstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] CHECK CONSTRAINT [customeraddress$FK_CustomerAddress_AddressType]
GO
ALTER TABLE [${dbxschemaname}].[customeraddress]  WITH NOCHECK ADD  CONSTRAINT [customeraddress$FK_CustomerAddress_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeraddress] CHECK CONSTRAINT [customeraddress$FK_CustomerAddress_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel]  WITH NOCHECK ADD  CONSTRAINT [customeralertcategorychannel$FK_customeralertcategorychannel_channel] FOREIGN KEY([ChannelId])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeralertcategorychannel] CHECK CONSTRAINT [customeralertcategorychannel$FK_customeralertcategorychannel_channel]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement]  WITH NOCHECK ADD  CONSTRAINT [customeralertentitlement$FK_CustomerAlertEntitlement_Alert] FOREIGN KEY([Alert_id])
REFERENCES [${dbxschemaname}].[alert] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] CHECK CONSTRAINT [customeralertentitlement$FK_CustomerAlertEntitlement_Alert]
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement]  WITH NOCHECK ADD  CONSTRAINT [customeralertentitlement$FK_CustomerAlertEntitlement_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customeralertentitlement] CHECK CONSTRAINT [customeralertentitlement$FK_CustomerAlertEntitlement_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix]  WITH NOCHECK ADD  CONSTRAINT [customerapprovalmatrix$FK_customerapprovalmatrix_approvalmatrix] FOREIGN KEY([approvalMatrixId])
REFERENCES [${dbxschemaname}].[approvalmatrix] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] CHECK CONSTRAINT [customerapprovalmatrix$FK_customerapprovalmatrix_approvalmatrix]
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix]  WITH NOCHECK ADD  CONSTRAINT [customerapprovalmatrix$FK_customerapprovalmatrix_customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerapprovalmatrix] CHECK CONSTRAINT [customerapprovalmatrix$FK_customerapprovalmatrix_customer]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype]  WITH NOCHECK ADD  CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Business_Type] FOREIGN KEY([BusinessType_id])
REFERENCES [${dbxschemaname}].[businesstype] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] CHECK CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Business_Type]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype]  WITH NOCHECK ADD  CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] CHECK CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype]  WITH NOCHECK ADD  CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Signatory_Type] FOREIGN KEY([SignatoryType_id])
REFERENCES [${dbxschemaname}].[signatorytype] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customerbusinesstype] CHECK CONSTRAINT [customerbusinesstype$FK_customerbusinesstype_Signatory_Type]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication]  WITH NOCHECK ADD  CONSTRAINT [customercommunication$FK_CustomerCommunication_CommunicationType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[communicationtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] CHECK CONSTRAINT [customercommunication$FK_CustomerCommunication_CommunicationType]
GO
ALTER TABLE [${dbxschemaname}].[customercommunication]  WITH NOCHECK ADD  CONSTRAINT [customercommunication$FK_CustomerCommunication_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customercommunication] CHECK CONSTRAINT [customercommunication$FK_CustomerCommunication_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice]  WITH NOCHECK ADD  CONSTRAINT [customerdevice$FK_CustomerDevice_Channel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] CHECK CONSTRAINT [customerdevice$FK_CustomerDevice_Channel]
GO
ALTER TABLE [${dbxschemaname}].[customerdevice]  WITH NOCHECK ADD  CONSTRAINT [customerdevice$FK_CustomerDevice_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerdevice] CHECK CONSTRAINT [customerdevice$FK_CustomerDevice_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata]  WITH NOCHECK ADD  CONSTRAINT [customerdpdata$FK_CutomerDpData_ActionProfile] FOREIGN KEY([ActionProfile_id])
REFERENCES [${dbxschemaname}].[actionprofile] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerdpdata] CHECK CONSTRAINT [customerdpdata$FK_CutomerDpData_ActionProfile]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement]  WITH NOCHECK ADD  CONSTRAINT [customerentitlement$FK_CustomerEntitlement_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] CHECK CONSTRAINT [customerentitlement$FK_CustomerEntitlement_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement]  WITH NOCHECK ADD  CONSTRAINT [customerentitlement$FK_CustomerEntitlement_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerentitlement] CHECK CONSTRAINT [customerentitlement$FK_CustomerEntitlement_Service]
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus]  WITH NOCHECK ADD  CONSTRAINT [customerflagstatus$FK_CustomerFlagStatus_CustomerID] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerflagstatus] CHECK CONSTRAINT [customerflagstatus$FK_CustomerFlagStatus_CustomerID]
GO
ALTER TABLE [${dbxschemaname}].[customergroup]  WITH NOCHECK ADD  CONSTRAINT [customergroup$FK_CustomerGroup_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customergroup] CHECK CONSTRAINT [customergroup$FK_CustomerGroup_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customergroup]  WITH NOCHECK ADD  CONSTRAINT [customergroup$FK_CustomerGroup_Group] FOREIGN KEY([Group_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customergroup] CHECK CONSTRAINT [customergroup$FK_CustomerGroup_Group]
GO
ALTER TABLE [${dbxschemaname}].[customerimage]  WITH NOCHECK ADD  CONSTRAINT [customerimage$customerimage_ibfk_1] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customerimage] CHECK CONSTRAINT [customerimage$customerimage_ibfk_1]
GO
ALTER TABLE [${dbxschemaname}].[customernote]  WITH NOCHECK ADD  CONSTRAINT [customernote$FK_CustomerNote_Createdby] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customernote] CHECK CONSTRAINT [customernote$FK_CustomerNote_Createdby]
GO
ALTER TABLE [${dbxschemaname}].[customernote]  WITH NOCHECK ADD  CONSTRAINT [customernote$FK_CustomerNote_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customernote] CHECK CONSTRAINT [customernote$FK_CustomerNote_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customernotification]  WITH NOCHECK ADD  CONSTRAINT [customernotification$FK_CustomerNotification_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customernotification] CHECK CONSTRAINT [customernotification$FK_CustomerNotification_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customernotification]  WITH NOCHECK ADD  CONSTRAINT [customernotification$FK_CustomerNotification_Notification] FOREIGN KEY([Notification_id])
REFERENCES [${dbxschemaname}].[adminnotification] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[customernotification] CHECK CONSTRAINT [customernotification$FK_CustomerNotification_Notification]
GO
ALTER TABLE [${dbxschemaname}].[customerpreference]  WITH NOCHECK ADD  CONSTRAINT [customerpreference$FK_CustomerPreference_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerpreference] CHECK CONSTRAINT [customerpreference$FK_CustomerPreference_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage]  WITH NOCHECK ADD  CONSTRAINT [customerprequalifypackage$FK_CustomerPQP_PreQualifyP] FOREIGN KEY([PrequalifyPackage_id])
REFERENCES [${dbxschemaname}].[prequalifypackage] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customerprequalifypackage] CHECK CONSTRAINT [customerprequalifypackage$FK_CustomerPQP_PreQualifyP]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct]  WITH NOCHECK ADD  CONSTRAINT [customerproduct$FK_CustomerProduct_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] CHECK CONSTRAINT [customerproduct$FK_CustomerProduct_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerproduct]  WITH NOCHECK ADD  CONSTRAINT [customerproduct$FK_CustomerProduct_Product] FOREIGN KEY([Product_id])
REFERENCES [${dbxschemaname}].[product] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerproduct] CHECK CONSTRAINT [customerproduct$FK_CustomerProduct_Product]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest]  WITH NOCHECK ADD  CONSTRAINT [customerrequest$FK_CustomerRequest_AssignedTo] FOREIGN KEY([AssignedTo])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] CHECK CONSTRAINT [customerrequest$FK_CustomerRequest_AssignedTo]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest]  WITH NOCHECK ADD  CONSTRAINT [customerrequest$FK_CustomerRequest_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] CHECK CONSTRAINT [customerrequest$FK_CustomerRequest_Customer]
GO
ALTER TABLE [${dbxschemaname}].[customerrequest]  WITH NOCHECK ADD  CONSTRAINT [customerrequest$FK_CustomerRequest_RequestCategory] FOREIGN KEY([RequestCategory_id])
REFERENCES [${dbxschemaname}].[requestcategory] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerrequest] CHECK CONSTRAINT [customerrequest$FK_CustomerRequest_RequestCategory]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages]  WITH NOCHECK ADD  CONSTRAINT [customersecurityimages$FK_CustomerSecureImages_UserID] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customersecurityimages] CHECK CONSTRAINT [customersecurityimages$FK_CustomerSecureImages_UserID]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions]  WITH NOCHECK ADD  CONSTRAINT [customersecurityquestions$FK_CustomerSecurityQuestion_CustomerID] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] CHECK CONSTRAINT [customersecurityquestions$FK_CustomerSecurityQuestion_CustomerID]
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions]  WITH NOCHECK ADD  CONSTRAINT [customersecurityquestions$FK_CustomerSecurityQuestion_QuestionID] FOREIGN KEY([SecurityQuestion_id])
REFERENCES [${dbxschemaname}].[securityquestion] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customersecurityquestions] CHECK CONSTRAINT [customersecurityquestions$FK_CustomerSecurityQuestion_QuestionID]
GO
ALTER TABLE [${dbxschemaname}].[customerservice]  WITH NOCHECK ADD  CONSTRAINT [customerservice$FK_CustomerService_ServiceType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[servicetype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[customerservice] CHECK CONSTRAINT [customerservice$FK_CustomerService_ServiceType]
GO
ALTER TABLE [${dbxschemaname}].[customrole]  WITH NOCHECK ADD  CONSTRAINT [customrole$customrole_ibfk_1] FOREIGN KEY([parent_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customrole] CHECK CONSTRAINT [customrole$customrole_ibfk_1]
GO
ALTER TABLE [${dbxschemaname}].[customrole]  WITH NOCHECK ADD  CONSTRAINT [customrole$customrole_ibfk_2] FOREIGN KEY([organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customrole] CHECK CONSTRAINT [customrole$customrole_ibfk_2]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits]  WITH NOCHECK ADD  CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_1] FOREIGN KEY([customRole_id])
REFERENCES [${dbxschemaname}].[customrole] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] CHECK CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_1]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits]  WITH NOCHECK ADD  CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_3] FOREIGN KEY([action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] CHECK CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_3]
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits]  WITH NOCHECK ADD  CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_4] FOREIGN KEY([limitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] CHECK CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_4]
GO
ALTER TABLE [${dbxschemaname}].[dayschedule]  WITH NOCHECK ADD  CONSTRAINT [dayschedule$FK_DaySchedule_WorkSchedule] FOREIGN KEY([WorkSchedule_id])
REFERENCES [${dbxschemaname}].[workschedule] ([id])
GO
ALTER TABLE [${dbxschemaname}].[dayschedule] CHECK CONSTRAINT [dayschedule$FK_DaySchedule_WorkSchedule]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext]  WITH NOCHECK ADD  CONSTRAINT [dbxalertcategorytext$FK_alertcategorytext_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] CHECK CONSTRAINT [dbxalertcategorytext$FK_alertcategorytext_locale]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype]  WITH NOCHECK ADD  CONSTRAINT [dbxalerttype$FK_alerttype_alertcategory] FOREIGN KEY([AlertCategoryId])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id])
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] CHECK CONSTRAINT [dbxalerttype$FK_alerttype_alertcategory]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext]  WITH NOCHECK ADD  CONSTRAINT [dbxalerttypetext$FK_alerttypetext_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] CHECK CONSTRAINT [dbxalerttypetext$FK_alerttypetext_locale]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement]  WITH NOCHECK ADD  CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_alerttype] FOREIGN KEY([AlertTypeId])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] CHECK CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_alerttype]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement]  WITH NOCHECK ADD  CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] CHECK CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_customer]
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification]  WITH NOCHECK ADD  CONSTRAINT [defaultcampaignspecification$FK_DefaultCampaignSpecification_campaignplaceholder_id] FOREIGN KEY([campaignplaceholder_id])
REFERENCES [${dbxschemaname}].[campaignplaceholder] ([id])
GO
ALTER TABLE [${dbxschemaname}].[defaultcampaignspecification] CHECK CONSTRAINT [defaultcampaignspecification$FK_DefaultCampaignSpecification_campaignplaceholder_id]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse]  WITH NOCHECK ADD  CONSTRAINT [demographicsresponse$FK_DemographicsResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] CHECK CONSTRAINT [demographicsresponse$FK_DemographicsResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse]  WITH NOCHECK ADD  CONSTRAINT [demographicsresponse$FK_DemographicsResponse_QueryResponseId] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[demographicsresponse] CHECK CONSTRAINT [demographicsresponse$FK_DemographicsResponse_QueryResponseId]
GO
ALTER TABLE [${dbxschemaname}].[dmaddinteractions]  WITH NOCHECK ADD  CONSTRAINT [dmaddinteractions$FK_dmaddinteractions_dmadvertisements] FOREIGN KEY([dm_add_id])
REFERENCES [${dbxschemaname}].[dmadvertisements] ([id])
GO
ALTER TABLE [${dbxschemaname}].[dmaddinteractions] CHECK CONSTRAINT [dmaddinteractions$FK_dmaddinteractions_dmadvertisements]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_IncomeResponse] FOREIGN KEY([IncomeResponse_id])
REFERENCES [${dbxschemaname}].[incomeresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_IncomeResponse]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem] FOREIGN KEY([EmploymentType_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem2] FOREIGN KEY([BusinessShare_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem2]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem3] FOREIGN KEY([EmploymentDetailType_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_OptionItem3]
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse]  WITH NOCHECK ADD  CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[employmentdetailsresponse] CHECK CONSTRAINT [employmentdetailsresponse$FK_EmploymentDetailsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype]  WITH NOCHECK ADD  CONSTRAINT [eventsubtype$fk_eventtypeid] FOREIGN KEY([eventtypeid])
REFERENCES [${dbxschemaname}].[eventtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[eventsubtype] CHECK CONSTRAINT [eventsubtype$fk_eventtypeid]
GO
ALTER TABLE [${dbxschemaname}].[eventtype]  WITH NOCHECK ADD  CONSTRAINT [eventtype$eventtype_activitytype] FOREIGN KEY([ActivityType])
REFERENCES [${dbxschemaname}].[eventactivitytype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[eventtype] CHECK CONSTRAINT [eventtype$eventtype_activitytype]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesheaderresponse$FK_ExpensesHeaderResponse_BorrowerId] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] CHECK CONSTRAINT [expensesheaderresponse$FK_ExpensesHeaderResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesheaderresponse$FK_ExpensesHeaderResponse_QueryResponse] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesheaderresponse] CHECK CONSTRAINT [expensesheaderresponse$FK_ExpensesHeaderResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesresponse$FK_ExpensesResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] CHECK CONSTRAINT [expensesresponse$FK_ExpensesResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesresponse$FK_ExpensesResponse_ExpensesHeaderResponse] FOREIGN KEY([ExpensesHeaderResponse_id])
REFERENCES [${dbxschemaname}].[expensesheaderresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] CHECK CONSTRAINT [expensesresponse$FK_ExpensesResponse_ExpensesHeaderResponse]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesresponse$FK_ExpensesResponse_ExpenseType] FOREIGN KEY([ExpenseType])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] CHECK CONSTRAINT [expensesresponse$FK_ExpensesResponse_ExpenseType]
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse]  WITH NOCHECK ADD  CONSTRAINT [expensesresponse$FK_ExpensesResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[expensesresponse] CHECK CONSTRAINT [expensesresponse$FK_ExpensesResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity]  WITH NOCHECK ADD  CONSTRAINT [externalbankidentity$FK_Bankid] FOREIGN KEY([ExternalBank_id])
REFERENCES [${dbxschemaname}].[externalbank] ([id])
GO
ALTER TABLE [${dbxschemaname}].[externalbankidentity] CHECK CONSTRAINT [externalbankidentity$FK_Bankid]
GO
ALTER TABLE [${dbxschemaname}].[faqs]  WITH NOCHECK ADD  CONSTRAINT [faqs$FK_FAQs_Category] FOREIGN KEY([FaqCategory_Id])
REFERENCES [${dbxschemaname}].[faqcategory] ([id])
GO
ALTER TABLE [${dbxschemaname}].[faqs] CHECK CONSTRAINT [faqs$FK_FAQs_Category]
GO
ALTER TABLE [${dbxschemaname}].[faqs]  WITH NOCHECK ADD  CONSTRAINT [faqs$FK_FAQs_ServiceChannel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[faqs] CHECK CONSTRAINT [faqs$FK_FAQs_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[feature]  WITH NOCHECK ADD  CONSTRAINT [feature$FK_feature_App_id] FOREIGN KEY([App_id])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[feature] CHECK CONSTRAINT [feature$FK_feature_App_id]
GO
ALTER TABLE [${dbxschemaname}].[feature]  WITH NOCHECK ADD  CONSTRAINT [feature$FK_feature_type_id] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[featuretype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[feature] CHECK CONSTRAINT [feature$FK_feature_type_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction]  WITH NOCHECK ADD  CONSTRAINT [featureaction$FK_action_feature_id] FOREIGN KEY([Feature_id])
REFERENCES [${dbxschemaname}].[feature] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureaction] CHECK CONSTRAINT [featureaction$FK_action_feature_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction]  WITH NOCHECK ADD  CONSTRAINT [featureaction$FK_featureaction_actiontype] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[actiontype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureaction] CHECK CONSTRAINT [featureaction$FK_featureaction_actiontype]
GO
ALTER TABLE [${dbxschemaname}].[featureaction]  WITH NOCHECK ADD  CONSTRAINT [featureaction$FK_featureaction_app_id] FOREIGN KEY([App_id])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureaction] CHECK CONSTRAINT [featureaction$FK_featureaction_app_id]
GO
ALTER TABLE [${dbxschemaname}].[featureaction]  WITH NOCHECK ADD  CONSTRAINT [featureaction$FK_featureaction_termandcondition] FOREIGN KEY([TermsAndConditions_id])
REFERENCES [${dbxschemaname}].[termandcondition] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureaction] CHECK CONSTRAINT [featureaction$FK_featureaction_termandcondition]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype]  WITH NOCHECK ADD  CONSTRAINT [featureactionroletype$FK_featureactionroletype_Action_id] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] CHECK CONSTRAINT [featureactionroletype$FK_featureactionroletype_Action_id]
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype]  WITH NOCHECK ADD  CONSTRAINT [featureactionroletype$FK_featureactionroletype_membergrouptype] FOREIGN KEY([RoleType_id])
REFERENCES [${dbxschemaname}].[membergrouptype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureactionroletype] CHECK CONSTRAINT [featureactionroletype$FK_featureactionroletype_membergrouptype]
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription]  WITH NOCHECK ADD  CONSTRAINT [featuredisplaynamedescription$FK_featuredisplaynamedescription_Feature_id] FOREIGN KEY([Feature_id])
REFERENCES [${dbxschemaname}].[feature] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] CHECK CONSTRAINT [featuredisplaynamedescription$FK_featuredisplaynamedescription_Feature_id]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype]  WITH NOCHECK ADD  CONSTRAINT [featureroletype$FK_featureroletype_Feature_id] FOREIGN KEY([Feature_id])
REFERENCES [${dbxschemaname}].[feature] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] CHECK CONSTRAINT [featureroletype$FK_featureroletype_Feature_id]
GO
ALTER TABLE [${dbxschemaname}].[featureroletype]  WITH NOCHECK ADD  CONSTRAINT [featureroletype$FK_featureroletype_membergrouptype] FOREIGN KEY([RoleType_id])
REFERENCES [${dbxschemaname}].[membergrouptype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] CHECK CONSTRAINT [featureroletype$FK_featureroletype_membergrouptype]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse]  WITH NOCHECK ADD  CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_AssetsResponse] FOREIGN KEY([AssetsResponse_id])
REFERENCES [${dbxschemaname}].[assetsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] CHECK CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_AssetsResponse]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse]  WITH NOCHECK ADD  CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] CHECK CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse]  WITH NOCHECK ADD  CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[giftsandgrantsresponse] CHECK CONSTRAINT [giftsandgrantsresponse$FK_GiftsAndGrantsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit]  WITH NOCHECK ADD  CONSTRAINT [groupactionlimit$FK_groupactionlimit_Action] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] CHECK CONSTRAINT [groupactionlimit$FK_groupactionlimit_Action]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit]  WITH NOCHECK ADD  CONSTRAINT [groupactionlimit$FK_groupactionlimit_Group] FOREIGN KEY([Group_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] CHECK CONSTRAINT [groupactionlimit$FK_groupactionlimit_Group]
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit]  WITH NOCHECK ADD  CONSTRAINT [groupactionlimit$FK_groupactionlimit_limitsubtype] FOREIGN KEY([LimitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupactionlimit] CHECK CONSTRAINT [groupactionlimit$FK_groupactionlimit_limitsubtype]
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype]  WITH NOCHECK ADD  CONSTRAINT [groupbusinesstype$FK_groupbusinesstype_Group] FOREIGN KEY([Group_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] CHECK CONSTRAINT [groupbusinesstype$FK_groupbusinesstype_Group]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement]  WITH NOCHECK ADD  CONSTRAINT [groupentitlement$FK_GroupEntitlement_TransactionFee] FOREIGN KEY([TransactionFee_id])
REFERENCES [${dbxschemaname}].[transactionfee] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] CHECK CONSTRAINT [groupentitlement$FK_GroupEntitlement_TransactionFee]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement]  WITH NOCHECK ADD  CONSTRAINT [groupentitlement$FK_GroupEntitlement_TransactionLimit] FOREIGN KEY([TransactionLimit_id])
REFERENCES [${dbxschemaname}].[transactionlimit] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] CHECK CONSTRAINT [groupentitlement$FK_GroupEntitlement_TransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement]  WITH NOCHECK ADD  CONSTRAINT [groupentitlement$FK_GroupEntitlements_Group] FOREIGN KEY([Group_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] CHECK CONSTRAINT [groupentitlement$FK_GroupEntitlements_Group]
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement]  WITH NOCHECK ADD  CONSTRAINT [groupentitlement$FK_GroupEntitlements_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[groupentitlement] CHECK CONSTRAINT [groupentitlement$FK_GroupEntitlements_Service]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse]  WITH NOCHECK ADD  CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] CHECK CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse]  WITH NOCHECK ADD  CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_EmploymentDetailsResponse] FOREIGN KEY([EmploymentDetailsResponse_id])
REFERENCES [${dbxschemaname}].[employmentdetailsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] CHECK CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_EmploymentDetailsResponse]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse]  WITH NOCHECK ADD  CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_OptionItem] FOREIGN KEY([IncomeDetail_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] CHECK CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse]  WITH NOCHECK ADD  CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_OptionItem2] FOREIGN KEY([PayPeriod_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] CHECK CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_OptionItem2]
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse]  WITH NOCHECK ADD  CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomedistributionresponse] CHECK CONSTRAINT [incomedistributionresponse$FK_IncomeDistributionResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse]  WITH NOCHECK ADD  CONSTRAINT [incomeresponse$FK_IncomeResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] CHECK CONSTRAINT [incomeresponse$FK_IncomeResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse]  WITH NOCHECK ADD  CONSTRAINT [incomeresponse$FK_IncomeResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[incomeresponse] CHECK CONSTRAINT [incomeresponse$FK_IncomeResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers]  WITH NOCHECK ADD  CONSTRAINT [interbankfundtransfers$FK_interbankfundtransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] CHECK CONSTRAINT [interbankfundtransfers$FK_interbankfundtransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers]  WITH NOCHECK ADD  CONSTRAINT [interbankfundtransfers$FK_interbankfundtransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] CHECK CONSTRAINT [interbankfundtransfers$FK_interbankfundtransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[interestrate]  WITH NOCHECK ADD  CONSTRAINT [interestrate$FK_InterestRate_LoanProduct] FOREIGN KEY([LoanProduct_id])
REFERENCES [${dbxschemaname}].[loanproduct] ([id])
GO
ALTER TABLE [${dbxschemaname}].[interestrate] CHECK CONSTRAINT [interestrate$FK_InterestRate_LoanProduct]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers]  WITH NOCHECK ADD  CONSTRAINT [internationalfundtransfers$FK_externaltransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] CHECK CONSTRAINT [internationalfundtransfers$FK_externaltransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers]  WITH NOCHECK ADD  CONSTRAINT [internationalfundtransfers$FK_externaltransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] CHECK CONSTRAINT [internationalfundtransfers$FK_externaltransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers]  WITH NOCHECK ADD  CONSTRAINT [intrabanktransfers$FK_intrabanktransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] CHECK CONSTRAINT [intrabanktransfers$FK_intrabanktransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers]  WITH NOCHECK ADD  CONSTRAINT [intrabanktransfers$FK_intrabanktransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] CHECK CONSTRAINT [intrabanktransfers$FK_intrabanktransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[lead]  WITH NOCHECK ADD  CONSTRAINT [lead$FK_lead_customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[lead] CHECK CONSTRAINT [lead$FK_lead_customer]
GO
ALTER TABLE [${dbxschemaname}].[lead]  WITH NOCHECK ADD  CONSTRAINT [lead$FK_lead_product] FOREIGN KEY([product_id])
REFERENCES [${dbxschemaname}].[product] ([id])
GO
ALTER TABLE [${dbxschemaname}].[lead] CHECK CONSTRAINT [lead$FK_lead_product]
GO
ALTER TABLE [${dbxschemaname}].[lead]  WITH NOCHECK ADD  CONSTRAINT [lead$FK_lead_systemuser] FOREIGN KEY([csr_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[lead] CHECK CONSTRAINT [lead$FK_lead_systemuser]
GO
ALTER TABLE [${dbxschemaname}].[leadnote]  WITH NOCHECK ADD  CONSTRAINT [leadnote$FK_leadnote_lead] FOREIGN KEY([lead_Id])
REFERENCES [${dbxschemaname}].[lead] ([id])
GO
ALTER TABLE [${dbxschemaname}].[leadnote] CHECK CONSTRAINT [leadnote$FK_leadnote_lead]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse]  WITH NOCHECK ADD  CONSTRAINT [legaldeclarationsresponse$FK_legaldeclarationsresponse_Borrower_id] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] CHECK CONSTRAINT [legaldeclarationsresponse$FK_legaldeclarationsresponse_Borrower_id]
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse]  WITH NOCHECK ADD  CONSTRAINT [legaldeclarationsresponse$FK_LegalDeclarationsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[legaldeclarationsresponse] CHECK CONSTRAINT [legaldeclarationsresponse$FK_LegalDeclarationsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesheaderresponse$FK_LiabilitiesHeaderResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] CHECK CONSTRAINT [liabilitiesheaderresponse$FK_LiabilitiesHeaderResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesheaderresponse$FK_LiabilitiesHeaderResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesheaderresponse] CHECK CONSTRAINT [liabilitiesheaderresponse$FK_LiabilitiesHeaderResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_AccountType] FOREIGN KEY([AccountType])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] CHECK CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_AccountType]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] CHECK CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_LiabilitiesHeaderResponse] FOREIGN KEY([LiabilitiesHeaderResponse_id])
REFERENCES [${dbxschemaname}].[liabilitiesheaderresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] CHECK CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_LiabilitiesHeaderResponse]
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse]  WITH NOCHECK ADD  CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[liabilitiesresponse] CHECK CONSTRAINT [liabilitiesresponse$FK_LiabilitiesResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse]  WITH NOCHECK ADD  CONSTRAINT [loaninforesponse$FK_LoanInfoResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[loaninforesponse] CHECK CONSTRAINT [loaninforesponse$FK_LoanInfoResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse]  WITH NOCHECK ADD  CONSTRAINT [loanofficersresponse$FK_LoanOfficersResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] CHECK CONSTRAINT [loanofficersresponse$FK_LoanOfficersResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse]  WITH NOCHECK ADD  CONSTRAINT [loanofficersresponse$FK_LoanOfficersResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[loanofficersresponse] CHECK CONSTRAINT [loanofficersresponse$FK_LoanOfficersResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[loanproduct]  WITH NOCHECK ADD  CONSTRAINT [loanproduct$FK_LoanProduct_LoanType] FOREIGN KEY([LoanType_id])
REFERENCES [${dbxschemaname}].[loantype] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[loanproduct] CHECK CONSTRAINT [loanproduct$FK_LoanProduct_LoanType]
GO
ALTER TABLE [${dbxschemaname}].[location]  WITH NOCHECK ADD  CONSTRAINT [location$FK_Location_Address] FOREIGN KEY([Address_id])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[location] CHECK CONSTRAINT [location$FK_Location_Address]
GO
ALTER TABLE [${dbxschemaname}].[location]  WITH NOCHECK ADD  CONSTRAINT [location$FK_Location_LocationType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[locationtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[location] CHECK CONSTRAINT [location$FK_Location_LocationType]
GO
ALTER TABLE [${dbxschemaname}].[location]  WITH NOCHECK ADD  CONSTRAINT [location$FK_Location_WorkSchedule] FOREIGN KEY([WorkSchedule_id])
REFERENCES [${dbxschemaname}].[workschedule] ([id])
GO
ALTER TABLE [${dbxschemaname}].[location] CHECK CONSTRAINT [location$FK_Location_WorkSchedule]
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency]  WITH NOCHECK ADD  CONSTRAINT [locationcurrency$FH_LocationCurrency_Location_id] FOREIGN KEY([Location_id])
REFERENCES [${dbxschemaname}].[location] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationcurrency] CHECK CONSTRAINT [locationcurrency$FH_LocationCurrency_Location_id]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment]  WITH NOCHECK ADD  CONSTRAINT [locationcustomersegment$FH_LocationCustomerSegment_Location_id] FOREIGN KEY([Location_id])
REFERENCES [${dbxschemaname}].[location] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] CHECK CONSTRAINT [locationcustomersegment$FH_LocationCustomerSegment_Location_id]
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment]  WITH NOCHECK ADD  CONSTRAINT [locationcustomersegment$FH_LocationCustomerSegment_segment_id] FOREIGN KEY([segment_id])
REFERENCES [${dbxschemaname}].[customersegment] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationcustomersegment] CHECK CONSTRAINT [locationcustomersegment$FH_LocationCustomerSegment_segment_id]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility]  WITH NOCHECK ADD  CONSTRAINT [locationfacility$FH_LocationCustomerFacility_facility_id] FOREIGN KEY([facility_id])
REFERENCES [${dbxschemaname}].[facility] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] CHECK CONSTRAINT [locationfacility$FH_LocationCustomerFacility_facility_id]
GO
ALTER TABLE [${dbxschemaname}].[locationfacility]  WITH NOCHECK ADD  CONSTRAINT [locationfacility$FH_LocationCustomerFacility_Location_id] FOREIGN KEY([Location_id])
REFERENCES [${dbxschemaname}].[location] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationfacility] CHECK CONSTRAINT [locationfacility$FH_LocationCustomerFacility_Location_id]
GO
ALTER TABLE [${dbxschemaname}].[locationservice]  WITH NOCHECK ADD  CONSTRAINT [locationservice$FH_LocationService_Location_id] FOREIGN KEY([Location_id])
REFERENCES [${dbxschemaname}].[location] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationservice] CHECK CONSTRAINT [locationservice$FH_LocationService_Location_id]
GO
ALTER TABLE [${dbxschemaname}].[locationservice]  WITH NOCHECK ADD  CONSTRAINT [locationservice$FH_LocationService_Service_id] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[locationservice] CHECK CONSTRAINT [locationservice$FH_LocationService_Service_id]
GO
ALTER TABLE [${dbxschemaname}].[membergroup]  WITH NOCHECK ADD  CONSTRAINT [membergroup$FK_MemberGroup_Type_id] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[membergrouptype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[membergroup] CHECK CONSTRAINT [membergroup$FK_MemberGroup_Type_id]
GO
ALTER TABLE [${dbxschemaname}].[membership]  WITH NOCHECK ADD  CONSTRAINT [membership$FK_membership_address_addressId] FOREIGN KEY([addressId])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[membership] CHECK CONSTRAINT [membership$FK_membership_address_addressId]
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts]  WITH NOCHECK ADD  CONSTRAINT [membershipaccounts$FK_membershipaccounts_membership_membershipId] FOREIGN KEY([membershipId])
REFERENCES [${dbxschemaname}].[membership] ([id])
GO
ALTER TABLE [${dbxschemaname}].[membershipaccounts] CHECK CONSTRAINT [membershipaccounts$FK_membershipaccounts_membership_membershipId]
GO
ALTER TABLE [${dbxschemaname}].[membershipowner]  WITH NOCHECK ADD  CONSTRAINT [membershipowner$FK_membershipowner_membership_membershipId] FOREIGN KEY([membershipId])
REFERENCES [${dbxschemaname}].[membership] ([id])
GO
ALTER TABLE [${dbxschemaname}].[membershipowner] CHECK CONSTRAINT [membershipowner$FK_membershipowner_membership_membershipId]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment]  WITH NOCHECK ADD  CONSTRAINT [messageattachment$FK_MessageAttachement_AttachementType] FOREIGN KEY([AttachmentType_id])
REFERENCES [${dbxschemaname}].[attachmenttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] CHECK CONSTRAINT [messageattachment$FK_MessageAttachement_AttachementType]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment]  WITH NOCHECK ADD  CONSTRAINT [messageattachment$FK_MessageAttachement_Media] FOREIGN KEY([Media_id])
REFERENCES [${dbxschemaname}].[media] ([id])
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] CHECK CONSTRAINT [messageattachment$FK_MessageAttachement_Media]
GO
ALTER TABLE [${dbxschemaname}].[messageattachment]  WITH NOCHECK ADD  CONSTRAINT [messageattachment$FK_MessageAttachement_RequestMessage] FOREIGN KEY([RequestMessage_id])
REFERENCES [${dbxschemaname}].[requestmessage] ([id])
GO
ALTER TABLE [${dbxschemaname}].[messageattachment] CHECK CONSTRAINT [messageattachment$FK_MessageAttachement_RequestMessage]
GO
ALTER TABLE [${dbxschemaname}].[messagesubcategory]  WITH NOCHECK ADD  CONSTRAINT [messagesubcategory$FK_Subcategory_Category] FOREIGN KEY([Category_id])
REFERENCES [${dbxschemaname}].[messagecategory] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[messagesubcategory] CHECK CONSTRAINT [messagesubcategory$FK_Subcategory_Category]
GO
ALTER TABLE [${dbxschemaname}].[mfa]  WITH NOCHECK ADD  CONSTRAINT [mfa$FK_appaction_PrimaryMFA_id] FOREIGN KEY([PrimaryMFAType])
REFERENCES [${dbxschemaname}].[mfatype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[mfa] CHECK CONSTRAINT [mfa$FK_appaction_PrimaryMFA_id]
GO
ALTER TABLE [${dbxschemaname}].[mfa]  WITH NOCHECK ADD  CONSTRAINT [mfa$FK_appaction_SecondaryMFA_id] FOREIGN KEY([SecondaryMFAType])
REFERENCES [${dbxschemaname}].[mfatype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[mfa] CHECK CONSTRAINT [mfa$FK_appaction_SecondaryMFA_id]
GO
ALTER TABLE [${dbxschemaname}].[mfa]  WITH NOCHECK ADD  CONSTRAINT [mfa$FK_appactionmfa_Frequency_id] FOREIGN KEY([FrequencyType_id])
REFERENCES [${dbxschemaname}].[frequencytype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[mfa] CHECK CONSTRAINT [mfa$FK_appactionmfa_Frequency_id]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations]  WITH NOCHECK ADD  CONSTRAINT [mfaconfigurations$FK_mfaConfigurations_MFA_id] FOREIGN KEY([MFA_id])
REFERENCES [${dbxschemaname}].[mfatype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] CHECK CONSTRAINT [mfaconfigurations$FK_mfaConfigurations_MFA_id]
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations]  WITH NOCHECK ADD  CONSTRAINT [mfaconfigurations$FK_mfaconfigurations_mfakey_id] FOREIGN KEY([MFAKey_id])
REFERENCES [${dbxschemaname}].[mfakey] ([id])
GO
ALTER TABLE [${dbxschemaname}].[mfaconfigurations] CHECK CONSTRAINT [mfaconfigurations$FK_mfaconfigurations_mfakey_id]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo]  WITH NOCHECK ADD  CONSTRAINT [notificationcardinfo$FK_NotificationCardinfo_Customer] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] CHECK CONSTRAINT [notificationcardinfo$FK_NotificationCardinfo_Customer]
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo]  WITH NOCHECK ADD  CONSTRAINT [notificationcardinfo$FK_NotificationCardinfo_TravelNotification] FOREIGN KEY([Notification_id])
REFERENCES [${dbxschemaname}].[travelnotification] ([id])
GO
ALTER TABLE [${dbxschemaname}].[notificationcardinfo] CHECK CONSTRAINT [notificationcardinfo$FK_NotificationCardinfo_TravelNotification]
GO
ALTER TABLE [${dbxschemaname}].[optionitem]  WITH NOCHECK ADD  CONSTRAINT [optionitem$FK_OptionItem_OptionGroup] FOREIGN KEY([OptionGroup_id])
REFERENCES [${dbxschemaname}].[optiongroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[optionitem] CHECK CONSTRAINT [optionitem$FK_OptionItem_OptionGroup]
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse]  WITH NOCHECK ADD  CONSTRAINT [optionitemresponse$FK_OptionItemResponse_OptionItem] FOREIGN KEY([OptionItem_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[optionitemresponse] CHECK CONSTRAINT [optionitemresponse$FK_OptionItemResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata]  WITH NOCHECK ADD  CONSTRAINT [optionmetadata$FK_OptionMetaData_LoanType] FOREIGN KEY([LoanType_id])
REFERENCES [${dbxschemaname}].[loantype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] CHECK CONSTRAINT [optionmetadata$FK_OptionMetaData_LoanType]
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata]  WITH NOCHECK ADD  CONSTRAINT [optionmetadata$FK_OptionMetaData_OptionGroup] FOREIGN KEY([OptionGroup_id])
REFERENCES [${dbxschemaname}].[optiongroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[optionmetadata] CHECK CONSTRAINT [optionmetadata$FK_OptionMetaData_OptionGroup]
GO
ALTER TABLE [${dbxschemaname}].[organisation]  WITH NOCHECK ADD  CONSTRAINT [organisation$FK_organisation_Business_Type] FOREIGN KEY([BusinessType_id])
REFERENCES [${dbxschemaname}].[businesstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisation] CHECK CONSTRAINT [organisation$FK_organisation_Business_Type]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts]  WITH NOCHECK ADD  CONSTRAINT [organisationaccounts$FK_organisationaccounts_accounttype] FOREIGN KEY([TypeID])
REFERENCES [${dbxschemaname}].[accounttype] ([TypeID])
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] CHECK CONSTRAINT [organisationaccounts$FK_organisationaccounts_accounttype]
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts]  WITH NOCHECK ADD  CONSTRAINT [organisationaccounts$FK_organisationaccounts_organisation] FOREIGN KEY([Organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationaccounts] CHECK CONSTRAINT [organisationaccounts$FK_organisationaccounts_organisation]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit]  WITH NOCHECK ADD  CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_action] FOREIGN KEY([Action_id])
REFERENCES [${dbxschemaname}].[featureaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] CHECK CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_action]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit]  WITH NOCHECK ADD  CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_limitsubtype] FOREIGN KEY([LimitType_id])
REFERENCES [${dbxschemaname}].[limittype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] CHECK CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_limitsubtype]
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit]  WITH NOCHECK ADD  CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_organisation] FOREIGN KEY([Organisation_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] CHECK CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_organisation]
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress]  WITH NOCHECK ADD  CONSTRAINT [organisationaddress$FK_organisationaddress_organizationId] FOREIGN KEY([Organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationaddress] CHECK CONSTRAINT [organisationaddress$FK_organisationaddress_organizationId]
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication]  WITH NOCHECK ADD  CONSTRAINT [organisationcommunication$FK_orgcommunication_organizationId] FOREIGN KEY([Organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationcommunication] CHECK CONSTRAINT [organisationcommunication$FK_orgcommunication_organizationId]
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees]  WITH NOCHECK ADD  CONSTRAINT [organisationemployees$FK_orgemployees_organizationId] FOREIGN KEY([Organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationemployees] CHECK CONSTRAINT [organisationemployees$FK_orgemployees_organizationId]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures]  WITH NOCHECK ADD  CONSTRAINT [organisationfeatures$FK_featureId] FOREIGN KEY([featureId])
REFERENCES [${dbxschemaname}].[feature] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] CHECK CONSTRAINT [organisationfeatures$FK_featureId]
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures]  WITH NOCHECK ADD  CONSTRAINT [organisationfeatures$FK_OrganisationId] FOREIGN KEY([organisationId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] CHECK CONSTRAINT [organisationfeatures$FK_OrganisationId]
GO
ALTER TABLE [${dbxschemaname}].[organisationowner]  WITH NOCHECK ADD  CONSTRAINT [organisationowner$FK_organisationowner_organisationId] FOREIGN KEY([Organization_id])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[organisationowner] CHECK CONSTRAINT [organisationowner$FK_organisationowner_organisationId]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse]  WITH NOCHECK ADD  CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_AssetsResponse] FOREIGN KEY([AssetsResponse_id])
REFERENCES [${dbxschemaname}].[assetsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] CHECK CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_AssetsResponse]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse]  WITH NOCHECK ADD  CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] CHECK CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse]  WITH NOCHECK ADD  CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherassetsresponse] CHECK CONSTRAINT [otherassetsresponse$FK_OtherAssetsResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] CHECK CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_IncomeResponse] FOREIGN KEY([IncomeResponse_id])
REFERENCES [${dbxschemaname}].[incomeresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] CHECK CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_IncomeResponse]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_OptionItem] FOREIGN KEY([IncomeSource_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] CHECK CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_OptionItem]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_OptionItem2] FOREIGN KEY([IncomePayPeriod_id])
REFERENCES [${dbxschemaname}].[optionitem] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] CHECK CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_OptionItem2]
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse]  WITH NOCHECK ADD  CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[otherincomesresponse] CHECK CONSTRAINT [otherincomesresponse$FK_OtherIncomesResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage]  WITH NOCHECK ADD  CONSTRAINT [outagemessage$FK_OutageMessage_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] CHECK CONSTRAINT [outagemessage$FK_OutageMessage_Service]
GO
ALTER TABLE [${dbxschemaname}].[outagemessage]  WITH NOCHECK ADD  CONSTRAINT [outagemessage$FK_OutageMessage_ServiceChannel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[outagemessage] CHECK CONSTRAINT [outagemessage$FK_OutageMessage_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp]  WITH NOCHECK ADD  CONSTRAINT [outagemessageapp$FK_outagemessageapp_app] FOREIGN KEY([App_id])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] CHECK CONSTRAINT [outagemessageapp$FK_outagemessageapp_app]
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp]  WITH NOCHECK ADD  CONSTRAINT [outagemessageapp$FK_outagemessageapp_outagemessage] FOREIGN KEY([Outagemessage_id])
REFERENCES [${dbxschemaname}].[outagemessage] ([id])
GO
ALTER TABLE [${dbxschemaname}].[outagemessageapp] CHECK CONSTRAINT [outagemessageapp$FK_outagemessageapp_outagemessage]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers]  WITH NOCHECK ADD  CONSTRAINT [ownaccounttransfers$FK_internaltransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] CHECK CONSTRAINT [ownaccounttransfers$FK_internaltransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers]  WITH NOCHECK ADD  CONSTRAINT [ownaccounttransfers$FK_internaltransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] CHECK CONSTRAINT [ownaccounttransfers$FK_internaltransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers]  WITH NOCHECK ADD  CONSTRAINT [p2ptransfers$FK_p2ptransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] CHECK CONSTRAINT [p2ptransfers$FK_p2ptransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers]  WITH NOCHECK ADD  CONSTRAINT [p2ptransfers$FK_p2ptransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[p2ptransfers] CHECK CONSTRAINT [p2ptransfers$FK_p2ptransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory]  WITH NOCHECK ADD  CONSTRAINT [passwordhistory$FK_Customer_id] FOREIGN KEY([Customer_id])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[passwordhistory] CHECK CONSTRAINT [passwordhistory$FK_Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit]  WITH NOCHECK ADD  CONSTRAINT [periodiclimit$FK_PeriodicLimit_Period] FOREIGN KEY([Period_id])
REFERENCES [${dbxschemaname}].[period] ([id])
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] CHECK CONSTRAINT [periodiclimit$FK_PeriodicLimit_Period]
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit]  WITH NOCHECK ADD  CONSTRAINT [periodiclimit$FK_PeriodicLimit_TransactionLimit] FOREIGN KEY([TransactionLimit_id])
REFERENCES [${dbxschemaname}].[transactionlimit] ([id])
GO
ALTER TABLE [${dbxschemaname}].[periodiclimit] CHECK CONSTRAINT [periodiclimit$FK_PeriodicLimit_TransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[permission]  WITH NOCHECK ADD  CONSTRAINT [permission$FK_Permission_DataType] FOREIGN KEY([DataType_id])
REFERENCES [${dbxschemaname}].[datatype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[permission] CHECK CONSTRAINT [permission$FK_Permission_DataType]
GO
ALTER TABLE [${dbxschemaname}].[permission]  WITH NOCHECK ADD  CONSTRAINT [permission$FK_Permission_PermissionType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[permissiontype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[permission] CHECK CONSTRAINT [permission$FK_Permission_PermissionType]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse]  WITH NOCHECK ADD  CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_addresstype] FOREIGN KEY([AddressType_id])
REFERENCES [${dbxschemaname}].[addresstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] CHECK CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_addresstype]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse]  WITH NOCHECK ADD  CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] CHECK CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_borrower]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse]  WITH NOCHECK ADD  CONSTRAINT [personalinfoaddressresponse$FK_PersonalInfoAddressResponse_personalinforesponse] FOREIGN KEY([PersonalInfoResponse_id])
REFERENCES [${dbxschemaname}].[personalinforesponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] CHECK CONSTRAINT [personalinfoaddressresponse$FK_PersonalInfoAddressResponse_personalinforesponse]
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse]  WITH NOCHECK ADD  CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_queryresponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinfoaddressresponse] CHECK CONSTRAINT [personalinfoaddressresponse$FK_personalinfoaddressresponse_queryresponse]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse]  WITH NOCHECK ADD  CONSTRAINT [personalinforesponse$FK_personalinforesponse_borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] CHECK CONSTRAINT [personalinforesponse$FK_personalinforesponse_borrower]
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse]  WITH NOCHECK ADD  CONSTRAINT [personalinforesponse$FK_personalinforesponse_queryresponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[personalinforesponse] CHECK CONSTRAINT [personalinforesponse$FK_personalinforesponse_queryresponse]
GO
ALTER TABLE [${dbxschemaname}].[policycontent]  WITH NOCHECK ADD  CONSTRAINT [policycontent$FK_Policy_id] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[policytype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[policycontent] CHECK CONSTRAINT [policycontent$FK_Policy_id]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage]  WITH NOCHECK ADD  CONSTRAINT [prequalifypackage$FK_PreQualifyPackage_LoanProduct] FOREIGN KEY([LoanProduct_id])
REFERENCES [${dbxschemaname}].[loanproduct] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] CHECK CONSTRAINT [prequalifypackage$FK_PreQualifyPackage_LoanProduct]
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage]  WITH NOCHECK ADD  CONSTRAINT [prequalifypackage$FK_PreQualifyPackage_LoanType] FOREIGN KEY([LoanType_id])
REFERENCES [${dbxschemaname}].[loantype] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[prequalifypackage] CHECK CONSTRAINT [prequalifypackage$FK_PreQualifyPackage_LoanType]
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy]  WITH NOCHECK ADD  CONSTRAINT [privacypolicy$FK_ PrivacyPolicy_ServiceChannel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[privacypolicy] CHECK CONSTRAINT [privacypolicy$FK_ PrivacyPolicy_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[product]  WITH NOCHECK ADD  CONSTRAINT [product$FK_OtherProduct_Type] FOREIGN KEY([OtherProductType_id])
REFERENCES [${dbxschemaname}].[otherproducttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[product] CHECK CONSTRAINT [product$FK_OtherProduct_Type]
GO
ALTER TABLE [${dbxschemaname}].[product]  WITH NOCHECK ADD  CONSTRAINT [product$FK_Product_Type] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[producttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[product] CHECK CONSTRAINT [product$FK_Product_Type]
GO
ALTER TABLE [${dbxschemaname}].[product]  WITH NOCHECK ADD  CONSTRAINT [product$FK_SecondaryProductId] FOREIGN KEY([SecondaryProduct_id])
REFERENCES [${dbxschemaname}].[product] ([id])
GO
ALTER TABLE [${dbxschemaname}].[product] CHECK CONSTRAINT [product$FK_SecondaryProductId]
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse]  WITH NOCHECK ADD  CONSTRAINT [propertyinforesponse$FK_PropertyInfoResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[propertyinforesponse] CHECK CONSTRAINT [propertyinforesponse$FK_PropertyInfoResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse]  WITH NOCHECK ADD  CONSTRAINT [queryresponse$FK_QueryResponse_LoanProduct] FOREIGN KEY([LoanProduct_id])
REFERENCES [${dbxschemaname}].[loanproduct] ([id])
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] CHECK CONSTRAINT [queryresponse$FK_QueryResponse_LoanProduct]
GO
ALTER TABLE [${dbxschemaname}].[queryresponse]  WITH NOCHECK ADD  CONSTRAINT [queryresponse$FK_QueryResponse_QueryDefinition] FOREIGN KEY([QueryDefinition_id])
REFERENCES [${dbxschemaname}].[querydefinition] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[queryresponse] CHECK CONSTRAINT [queryresponse$FK_QueryResponse_QueryDefinition]
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent]  WITH NOCHECK ADD  CONSTRAINT [queryresponseconsent$FK_QueryResponseConsent_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[queryresponseconsent] CHECK CONSTRAINT [queryresponseconsent$FK_QueryResponseConsent_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[querysection]  WITH NOCHECK ADD  CONSTRAINT [querysection$FK_QuerySection_QueryDefintion] FOREIGN KEY([QueryDefinition_id])
REFERENCES [${dbxschemaname}].[querydefinition] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[querysection] CHECK CONSTRAINT [querysection$FK_QuerySection_QueryDefintion]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition]  WITH NOCHECK ADD  CONSTRAINT [questiondefinition$FK_QuestionDefintion_OptionGroup] FOREIGN KEY([OptionGroup_id])
REFERENCES [${dbxschemaname}].[optiongroup] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] CHECK CONSTRAINT [questiondefinition$FK_QuestionDefintion_OptionGroup]
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition]  WITH NOCHECK ADD  CONSTRAINT [questiondefinition$FK_QuestionDefintion_QueryDefinition] FOREIGN KEY([QueryDefinition_id])
REFERENCES [${dbxschemaname}].[querydefinition] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[questiondefinition] CHECK CONSTRAINT [questiondefinition$FK_QuestionDefintion_QueryDefinition]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse]  WITH NOCHECK ADD  CONSTRAINT [questionresponse$FK_QuestionResponse_QueryDefintion] FOREIGN KEY([QueryDefinition_id])
REFERENCES [${dbxschemaname}].[querydefinition] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] CHECK CONSTRAINT [questionresponse$FK_QuestionResponse_QueryDefintion]
GO
ALTER TABLE [${dbxschemaname}].[questionresponse]  WITH NOCHECK ADD  CONSTRAINT [questionresponse$FK_QuestionResponse_QuerySection] FOREIGN KEY([QuerySection_id])
REFERENCES [${dbxschemaname}].[querysection] ([id])
GO
ALTER TABLE [${dbxschemaname}].[questionresponse] CHECK CONSTRAINT [questionresponse$FK_QuestionResponse_QuerySection]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse]  WITH NOCHECK ADD  CONSTRAINT [realestateagentresponse$FK_RealEstateagentResponse_Borrower] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] CHECK CONSTRAINT [realestateagentresponse$FK_RealEstateagentResponse_Borrower]
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse]  WITH NOCHECK ADD  CONSTRAINT [realestateagentresponse$FK_RealEstateAgentResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[realestateagentresponse] CHECK CONSTRAINT [realestateagentresponse$FK_RealEstateAgentResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse]  WITH NOCHECK ADD  CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_AssetsResponse] FOREIGN KEY([AssetsResponse_id])
REFERENCES [${dbxschemaname}].[assetsresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] CHECK CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_AssetsResponse]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse]  WITH NOCHECK ADD  CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_BorrowerId] FOREIGN KEY([Borrower_id])
REFERENCES [${dbxschemaname}].[borrower] ([id])
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] CHECK CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_BorrowerId]
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse]  WITH NOCHECK ADD  CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_QueryResponse] FOREIGN KEY([QueryResponse_id])
REFERENCES [${dbxschemaname}].[queryresponse] ([id])
GO
ALTER TABLE [${dbxschemaname}].[realestatesresponse] CHECK CONSTRAINT [realestatesresponse$FK_RealEstatesResponse_QueryResponse]
GO
ALTER TABLE [${dbxschemaname}].[region]  WITH NOCHECK ADD  CONSTRAINT [region$FK_Region_Country] FOREIGN KEY([Country_id])
REFERENCES [${dbxschemaname}].[country] ([id])
GO
ALTER TABLE [${dbxschemaname}].[region] CHECK CONSTRAINT [region$FK_Region_Country]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix]  WITH NOCHECK ADD  CONSTRAINT [requestapprovalmatrix$FK_requestapprovalmatrix_approvalmatrix_id] FOREIGN KEY([approvalMatrixId])
REFERENCES [${dbxschemaname}].[approvalmatrix] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] CHECK CONSTRAINT [requestapprovalmatrix$FK_requestapprovalmatrix_approvalmatrix_id]
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix]  WITH NOCHECK ADD  CONSTRAINT [requestapprovalmatrix$FK_requestapprovalmatrix_bbrequest] FOREIGN KEY([requestId])
REFERENCES [${dbxschemaname}].[bbrequest] ([requestId])
ON DELETE CASCADE
GO
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] CHECK CONSTRAINT [requestapprovalmatrix$FK_requestapprovalmatrix_bbrequest]
GO
ALTER TABLE [${dbxschemaname}].[requestmessage]  WITH NOCHECK ADD  CONSTRAINT [requestmessage$FK_RequestMessage_CustomerRequest] FOREIGN KEY([CustomerRequest_id])
REFERENCES [${dbxschemaname}].[customerrequest] ([id])
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] CHECK CONSTRAINT [requestmessage$FK_RequestMessage_CustomerRequest]
GO
ALTER TABLE [${dbxschemaname}].[role]  WITH NOCHECK ADD  CONSTRAINT [role$FK_Role_Role] FOREIGN KEY([Parent_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[role] CHECK CONSTRAINT [role$FK_Role_Role]
GO
ALTER TABLE [${dbxschemaname}].[role]  WITH NOCHECK ADD  CONSTRAINT [role$FK_Role_RoleType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[roletype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[role] CHECK CONSTRAINT [role$FK_Role_RoleType]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction]  WITH NOCHECK ADD  CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_CompositeAction] FOREIGN KEY([CompositeAction_id])
REFERENCES [${dbxschemaname}].[compositeaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] CHECK CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_CompositeAction]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction]  WITH NOCHECK ADD  CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_Role] FOREIGN KEY([Role_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] CHECK CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_Role]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission]  WITH NOCHECK ADD  CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_CompositePermission] FOREIGN KEY([CompositePermission_id])
REFERENCES [${dbxschemaname}].[compositepermission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] CHECK CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_CompositePermission]
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission]  WITH NOCHECK ADD  CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_Role] FOREIGN KEY([Role_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] CHECK CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_Role]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission]  WITH NOCHECK ADD  CONSTRAINT [rolepermission$FK_RolePermission_Permission] FOREIGN KEY([Permission_id])
REFERENCES [${dbxschemaname}].[permission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] CHECK CONSTRAINT [rolepermission$FK_RolePermission_Permission]
GO
ALTER TABLE [${dbxschemaname}].[rolepermission]  WITH NOCHECK ADD  CONSTRAINT [rolepermission$FK_RolePermission_Role] FOREIGN KEY([Role_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] CHECK CONSTRAINT [rolepermission$FK_RolePermission_Role]
GO
ALTER TABLE [${dbxschemaname}].[service]  WITH NOCHECK ADD  CONSTRAINT [service$FK_Service_CategoryID] FOREIGN KEY([Category_id])
REFERENCES [${dbxschemaname}].[category] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service] CHECK CONSTRAINT [service$FK_Service_CategoryID]
GO
ALTER TABLE [${dbxschemaname}].[service]  WITH NOCHECK ADD  CONSTRAINT [service$FK_Service_ServiceType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[servicetype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service] CHECK CONSTRAINT [service$FK_Service_ServiceType]
GO
ALTER TABLE [${dbxschemaname}].[service]  WITH NOCHECK ADD  CONSTRAINT [service$FK_Service_TransactionFee] FOREIGN KEY([TransactionFee_id])
REFERENCES [${dbxschemaname}].[transactionfee] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service] CHECK CONSTRAINT [service$FK_Service_TransactionFee]
GO
ALTER TABLE [${dbxschemaname}].[service]  WITH NOCHECK ADD  CONSTRAINT [service$FK_Service_TransactionLimit] FOREIGN KEY([TransactionLimit_id])
REFERENCES [${dbxschemaname}].[transactionlimit] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service] CHECK CONSTRAINT [service$FK_Service_TransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[service]  WITH NOCHECK ADD  CONSTRAINT [service$FK_Service_WorkSchedule] FOREIGN KEY([WorkSchedule_id])
REFERENCES [${dbxschemaname}].[workschedule] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service] CHECK CONSTRAINT [service$FK_Service_WorkSchedule]
GO
ALTER TABLE [${dbxschemaname}].[service_channels]  WITH NOCHECK ADD  CONSTRAINT [service_channels$service_channels_Channel_id] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service_channels] CHECK CONSTRAINT [service_channels$service_channels_Channel_id]
GO
ALTER TABLE [${dbxschemaname}].[service_channels]  WITH NOCHECK ADD  CONSTRAINT [service_channels$service_channels_Service_id] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[service_channels] CHECK CONSTRAINT [service_channels$service_channels_Service_id]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication]  WITH NOCHECK ADD  CONSTRAINT [servicecommunication$FK_ServiceCommunication_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[customerservice] ([id])
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] CHECK CONSTRAINT [servicecommunication$FK_ServiceCommunication_Service]
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication]  WITH NOCHECK ADD  CONSTRAINT [servicecommunication$FK_ServiceCommunication_ServiceChannel] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[communicationtype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[servicecommunication] CHECK CONSTRAINT [servicecommunication$FK_ServiceCommunication_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[status]  WITH NOCHECK ADD  CONSTRAINT [status$FK_Status_StatusType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[statustype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[status] CHECK CONSTRAINT [status$FK_Status_StatusType]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp]  WITH NOCHECK ADD  CONSTRAINT [termandconditionapp$FK_termandconditionapp_app] FOREIGN KEY([AppId])
REFERENCES [${dbxschemaname}].[app] ([id])
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] CHECK CONSTRAINT [termandconditionapp$FK_termandconditionapp_app]
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp]  WITH NOCHECK ADD  CONSTRAINT [termandconditionapp$FK_termandconditionapp_termandcondition] FOREIGN KEY([TermAndConditionId])
REFERENCES [${dbxschemaname}].[termandcondition] ([id])
GO
ALTER TABLE [${dbxschemaname}].[termandconditionapp] CHECK CONSTRAINT [termandconditionapp$FK_termandconditionapp_termandcondition]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext]  WITH NOCHECK ADD  CONSTRAINT [termandconditiontext$FK_termandconditiontext_contenttype_id] FOREIGN KEY([ContentType_id])
REFERENCES [${dbxschemaname}].[contenttype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] CHECK CONSTRAINT [termandconditiontext$FK_termandconditiontext_contenttype_id]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext]  WITH NOCHECK ADD  CONSTRAINT [termandconditiontext$FK_termandconditiontext_locale] FOREIGN KEY([LanguageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] CHECK CONSTRAINT [termandconditiontext$FK_termandconditiontext_locale]
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext]  WITH NOCHECK ADD  CONSTRAINT [termandconditiontext$FK_termandconditiontext_termandcondition] FOREIGN KEY([TermAndConditionId])
REFERENCES [${dbxschemaname}].[termandcondition] ([id])
GO
ALTER TABLE [${dbxschemaname}].[termandconditiontext] CHECK CONSTRAINT [termandconditiontext$FK_termandconditiontext_termandcondition]
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions]  WITH NOCHECK ADD  CONSTRAINT [termsandconditions$FK_TermsAndConditions_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[termsandconditions] CHECK CONSTRAINT [termsandconditions$FK_TermsAndConditions_Service]
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab]  WITH NOCHECK ADD  CONSTRAINT [transactionfeeslab$FK_TransactionFeeSlab_TransactionFee] FOREIGN KEY([TransactionFee_id])
REFERENCES [${dbxschemaname}].[transactionfee] ([id])
GO
ALTER TABLE [${dbxschemaname}].[transactionfeeslab] CHECK CONSTRAINT [transactionfeeslab$FK_TransactionFeeSlab_TransactionFee]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup]  WITH NOCHECK ADD  CONSTRAINT [transactiongroup$FK_TransactionGroup_TransactionLimit] FOREIGN KEY([TransactionLimit_id])
REFERENCES [${dbxschemaname}].[transactionlimit] ([id])
GO
ALTER TABLE [${dbxschemaname}].[transactiongroup] CHECK CONSTRAINT [transactiongroup$FK_TransactionGroup_TransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice]  WITH NOCHECK ADD  CONSTRAINT [transactiongroupservice$FK_TransactionGroupService_Service] FOREIGN KEY([Service_id])
REFERENCES [${dbxschemaname}].[service] ([id])
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] CHECK CONSTRAINT [transactiongroupservice$FK_TransactionGroupService_Service]
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice]  WITH NOCHECK ADD  CONSTRAINT [transactiongroupservice$FK_TransactionGroupService_TransactionGroup] FOREIGN KEY([TransactionGroup_id])
REFERENCES [${dbxschemaname}].[transactiongroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[transactiongroupservice] CHECK CONSTRAINT [transactiongroupservice$FK_TransactionGroupService_TransactionGroup]
GO
ALTER TABLE [${dbxschemaname}].[travelnotification]  WITH NOCHECK ADD  CONSTRAINT [travelnotification$FK_TravelNotificationRequest_ServiceChannel] FOREIGN KEY([Channel_id])
REFERENCES [${dbxschemaname}].[servicechannel] ([id])
GO
ALTER TABLE [${dbxschemaname}].[travelnotification] CHECK CONSTRAINT [travelnotification$FK_TravelNotificationRequest_ServiceChannel]
GO
ALTER TABLE [${dbxschemaname}].[useraddress]  WITH NOCHECK ADD  CONSTRAINT [useraddress$FK_UserAddress_Address] FOREIGN KEY([Address_id])
REFERENCES [${dbxschemaname}].[address] ([id])
GO
ALTER TABLE [${dbxschemaname}].[useraddress] CHECK CONSTRAINT [useraddress$FK_UserAddress_Address]
GO
ALTER TABLE [${dbxschemaname}].[useraddress]  WITH NOCHECK ADD  CONSTRAINT [useraddress$FK_UserAddress_AddressType] FOREIGN KEY([Type_id])
REFERENCES [${dbxschemaname}].[addresstype] ([id])
GO
ALTER TABLE [${dbxschemaname}].[useraddress] CHECK CONSTRAINT [useraddress$FK_UserAddress_AddressType]
GO
ALTER TABLE [${dbxschemaname}].[useraddress]  WITH NOCHECK ADD  CONSTRAINT [useraddress$FK_UserAddress_SystemUser] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[useraddress] CHECK CONSTRAINT [useraddress$FK_UserAddress_SystemUser]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction]  WITH NOCHECK ADD  CONSTRAINT [usercompositeaction$FK_UserCompositeAction_CompositeAction] FOREIGN KEY([CompositeAction_id])
REFERENCES [${dbxschemaname}].[compositeaction] ([id])
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] CHECK CONSTRAINT [usercompositeaction$FK_UserCompositeAction_CompositeAction]
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction]  WITH NOCHECK ADD  CONSTRAINT [usercompositeaction$FK_UserCompositeAction_User] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] CHECK CONSTRAINT [usercompositeaction$FK_UserCompositeAction_User]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission]  WITH NOCHECK ADD  CONSTRAINT [usercompositepermission$FK_UserCompositePermission_CompositePermission] FOREIGN KEY([CompositePermission_id])
REFERENCES [${dbxschemaname}].[compositepermission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] CHECK CONSTRAINT [usercompositepermission$FK_UserCompositePermission_CompositePermission]
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission]  WITH NOCHECK ADD  CONSTRAINT [usercompositepermission$FK_UserCompositePermission_User] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[usercompositepermission] CHECK CONSTRAINT [usercompositepermission$FK_UserCompositePermission_User]
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck]  WITH NOCHECK ADD  CONSTRAINT [usercreditcheck$FK_USERCREDITCHECK_USER] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[user] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[usercreditcheck] CHECK CONSTRAINT [usercreditcheck$FK_USERCREDITCHECK_USER]
GO
ALTER TABLE [${dbxschemaname}].[userpermission]  WITH NOCHECK ADD  CONSTRAINT [userpermission$FK_UserPermission_PermissionID] FOREIGN KEY([Permission_id])
REFERENCES [${dbxschemaname}].[permission] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userpermission] CHECK CONSTRAINT [userpermission$FK_UserPermission_PermissionID]
GO
ALTER TABLE [${dbxschemaname}].[userpermission]  WITH NOCHECK ADD  CONSTRAINT [userpermission$FK_UserPermission_UserID] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userpermission] CHECK CONSTRAINT [userpermission$FK_UserPermission_UserID]
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo]  WITH NOCHECK ADD  CONSTRAINT [userpersonalinfo$FK_USERPERSONALINFO_USER] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[user] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[userpersonalinfo] CHECK CONSTRAINT [userpersonalinfo$FK_USERPERSONALINFO_USER]
GO
ALTER TABLE [${dbxschemaname}].[userproducts]  WITH NOCHECK ADD  CONSTRAINT [userproducts$FK_USERPRODUCTS_USER] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[user] ([Id])
GO
ALTER TABLE [${dbxschemaname}].[userproducts] CHECK CONSTRAINT [userproducts$FK_USERPRODUCTS_USER]
GO
ALTER TABLE [${dbxschemaname}].[userrole]  WITH NOCHECK ADD  CONSTRAINT [userrole$FK_UserRole_Role] FOREIGN KEY([Role_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userrole] CHECK CONSTRAINT [userrole$FK_UserRole_Role]
GO
ALTER TABLE [${dbxschemaname}].[userrole]  WITH NOCHECK ADD  CONSTRAINT [userrole$FK_UserRole_SystemUser] FOREIGN KEY([User_id])
REFERENCES [${dbxschemaname}].[systemuser] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userrole] CHECK CONSTRAINT [userrole$FK_UserRole_SystemUser]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole]  WITH NOCHECK ADD  CONSTRAINT [userrolecustomerrole$userrolecustomerrole_customerrole_id] FOREIGN KEY([CustomerRole_id])
REFERENCES [${dbxschemaname}].[membergroup] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] CHECK CONSTRAINT [userrolecustomerrole$userrolecustomerrole_customerrole_id]
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole]  WITH NOCHECK ADD  CONSTRAINT [userrolecustomerrole$userrolecustomerrole_userrole_id] FOREIGN KEY([UserRole_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] CHECK CONSTRAINT [userrolecustomerrole$userrolecustomerrole_userrole_id]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers]  WITH NOCHECK ADD  CONSTRAINT [wiretransfers$FK_wiretransfers_companyIdx] FOREIGN KEY([companyId])
REFERENCES [${dbxschemaname}].[organisation] ([id])
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] CHECK CONSTRAINT [wiretransfers$FK_wiretransfers_companyIdx]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers]  WITH NOCHECK ADD  CONSTRAINT [wiretransfers$FK_wiretransfers_createdby_idx] FOREIGN KEY([createdby])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] CHECK CONSTRAINT [wiretransfers$FK_wiretransfers_createdby_idx]
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers]  WITH NOCHECK ADD  CONSTRAINT [wiretransfers$FK_wiretransfers_onetime_idx] FOREIGN KEY([onetime_id])
REFERENCES [${dbxschemaname}].[onetimepayee] ([onetime_id])
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] CHECK CONSTRAINT [wiretransfers$FK_wiretransfers_onetime_idx]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [AccountName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [UserName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [ExternalBankidentity_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [CurrencyCode]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [AvailableBalance]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [AccountHolder]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Address]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Scheme]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Number]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [error]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Product_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('1') FOR [Bank_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [User_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Name]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ((0)) FOR [isBusinessAccount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [StatusDesc]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [SupportDeposit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [SupportBillPay]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [SupportTransferFrom]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [SupportTransferTo]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [ShowTransactions]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [CurrentBalance]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [InterestRate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [AvailableCredit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [MinimumDue]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [DueDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PrincipalValue]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [FirstPaymentDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [ClosingDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [PaymentTerm]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [OpeningDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [MaturityDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [TransactionLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [TransferLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [NickName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [LastStatementBalance]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [AvailablePoints]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [OutstandingBalance]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [CreditCardNumber]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [IsPFM]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [SupportCardlessCash]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [FavouriteStatus]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [MaturityOption]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [RoutingNumber]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [SwiftCode]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [JointHolders]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [DividendRate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [DividendYTD]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [LastDividendPaidAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [LastDividendPaidDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PreviousYearDividend]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [BondInterest]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [BondInterestLastYear]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [TotalCreditMonths]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [TotalDebitsMonth]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [CurrentAmountDue]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PaymentDue]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [LastPaymentDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [LastPaymentAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [LateFeesDue]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [CreditLimit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [InterestPaidYTD]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [InterestPaidPreviousYTD]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [UnpaidInterest]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [PaymentMethod]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [RegularPaymentAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [DividendPaidYTD]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [DividendLastPaidAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [DividendLastPaidDate]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PreviousYearsDividends]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PendingDeposit]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PendingWithdrawal]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [InterestEarned]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [maturityAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [principalBalance]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [OriginalAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [payoffAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [BsbNum]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [PayOffCharge]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0.00') FOR [InterestPaidLastYear]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [EStatementmentEnable]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [Phone_id]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [LastUpdated]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [BankName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ((0)) FOR [AccountPreference]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [InternalAccount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [email]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [jointAccountHolder1]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [jointAccountHolder2]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [bankAddress]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [intermediaryBankName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [intermediaryBankAddress]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [intermediaryBankSwiftCode]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [phone]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [accountSubType]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [schemeName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [identification]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [secondaryIdentification]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [servicerSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [servicerIdentification]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataCreditDebitIndicator]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataType]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataDateTime]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataCreditLineIncluded]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataCreditLineType]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataCreditLineAmount]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [dataCreditLineCurrency]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [adminProductId]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [UpdatedBy]
GO
ALTER TABLE [${dbxschemaname}].[accounts] ADD  DEFAULT (NULL) FOR [ActualUpdatedBY]
GO
ALTER TABLE [${dbxschemaname}].[accounts]  WITH CHECK ADD  CONSTRAINT [External_id] FOREIGN KEY([ExternalBankidentity_id])
REFERENCES [${dbxschemaname}].[externalbankidentity] ([id])
GO
ALTER TABLE [${dbxschemaname}].[accounts] CHECK CONSTRAINT [External_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [isScheduled]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Customer_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [ExpenseCategory_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Payee_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Bill_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Type_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Reference_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [fromAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [fromAccountBalance]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [toAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [toAccountBalance]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [amount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [convertedAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [baseCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Status_id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [statusDesc]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('') FOR [notes]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [checkNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [hasDepositImage]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (' ') FOR [description]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [scheduledDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (getdate()) FOR [transactionDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (getdate()) FOR [postedDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (getdate()) FOR [createdDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionComments]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [toExternalAccountNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Person_Id]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('Once') FOR [frequencyType]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [numberOfRecurrences]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [frequencyStartDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [frequencyEndDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessOTPValidDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessOTP]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessPhone]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessEmail]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessPersonName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessMode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessSecurityCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashWithdrawalTransactionStatus]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cashlessPin]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [category]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [recurrenceDesc]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [deliverBy]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [p2pContact]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [p2pRequiredDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [requestCreatedDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [penaltyFlag]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [payoffFlag]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('http://pmqa.konylabs.net/KonyWebBanking/view_report.png') FOR [viewReportLink]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [isPaypersonDeleted]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [fee]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [feeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [feePaidByReceipent]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [frontImage1]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [frontImage2]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [backImage1]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [backImage2]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [checkDesc]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [checkNumber1]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [checkNumber2]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [bankName1]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [bankName2]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [withdrawlAmount1]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [withdrawlAmount2]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0.00') FOR [cashAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('INR') FOR [payeeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [billid]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [isDisputed]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [disputeDescription]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [disputeReason]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [disputeStatus]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [disputeDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [payeeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [checkDateOfIssue]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [checkReason]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [isPayeeDeleted]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT ('0') FOR [amountRecieved]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [requestValidity]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [statementReference]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transCreditDebitIndicator]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [bookingDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [valueDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionInformation]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [addressLine]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [chargeAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [chargeCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [sourceCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [targetCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [unitCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [exchangeRate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [contractIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [quotationDate]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [instructedAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [instructedCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [transactionSubCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [proprietaryTransactionCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [proprietaryTransactionIssuer]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [balanceCreditDebitIndicator]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [balanceType]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [balanceAmount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [balanceCurrency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [merchantName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [merchantCategoryCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentaddressType]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentDepartment]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentSubDepartment]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentStreetName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentBuildingNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentPostCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentTownName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentCountrySubDivision]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentCountry]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAgentAddressLine]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAccountSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAccountIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAccountName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [creditorAccountSeconIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentAddressType]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentDepartment]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentSubDepartment]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentStreetName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentBuildingNumber]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [dedtorAgentPostCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentTownName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentCountrySubDivision]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentCountry]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAgentAddressLine]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAccountSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAccountIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAccountName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [debtorAccountSeconIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cardInstrumentSchemeName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cardInstrumentAuthorisationType]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cardInstrumentName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [cardInstrumentIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [IBAN]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [sortCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FirstPaymentDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [NextPaymentDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FinalPaymentDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [StandingOrderStatusCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FP_Amount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FP_Currency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [NP_Amount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [NP_Currency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FPA_Amount]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [FPA_Currency]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [ConsentId]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Initiation_InstructionIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [Initiation_EndToEndIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [RI_Reference]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [RI_Unstructured]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [RiskPaymentContextCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [MerchantCustomerIdentification]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [beneficiaryName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [bankName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [swiftCode]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [DomesticPaymentId]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [linkSelf]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [StatusUpdateDateTime]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [dataStatus]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [serviceName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [payPersonName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [payPersonNickName]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [p2pAlternateContact]
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD  DEFAULT (NULL) FOR [billerId]
GO
ALTER TABLE [${dbxschemaname}].[transaction]  WITH CHECK ADD  CONSTRAINT [billcateg] FOREIGN KEY([billCategory])
REFERENCES [${dbxschemaname}].[billcategory] ([billcategId])
GO
ALTER TABLE [${dbxschemaname}].[transaction] CHECK CONSTRAINT [billcateg]
GO
ALTER TABLE [${dbxschemaname}].[transaction]  WITH CHECK ADD  CONSTRAINT [categ] FOREIGN KEY([category])
REFERENCES [${dbxschemaname}].[tcategories] ([categId])
GO
ALTER TABLE [${dbxschemaname}].[transaction] CHECK CONSTRAINT [categ]
GO
ALTER TABLE [${dbxschemaname}].[transaction]  WITH CHECK ADD  CONSTRAINT [freqType] FOREIGN KEY([frequencyType])
REFERENCES [${dbxschemaname}].[freq1] ([freqId])
GO
ALTER TABLE [${dbxschemaname}].[transaction] CHECK CONSTRAINT [freqType]
GO
alter table [${dbxschemaname}].archivedcustomerrequest add isTypeBusiness nvarchar(max) default NULL
GO
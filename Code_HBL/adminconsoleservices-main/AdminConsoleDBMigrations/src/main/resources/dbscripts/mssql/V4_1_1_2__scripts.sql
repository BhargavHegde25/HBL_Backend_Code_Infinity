USE [${dbxdbname}]
GO
/****** Object:  View [${dbxschemaname}].[achfile_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[accountransactionview]
      AS 
         SELECT 
            
               [${dbxschemaname}].accounts.Account_id AS Account_id , 
               [${dbxschemaname}].accounts.AccountName AS AccountName , 
               [${dbxschemaname}].accounts.AccountHolder AS  AccountHolder , 
               [${dbxschemaname}].accounts.UserName AS  UserName , 
               [${dbxschemaname}].accounts.ExternalBankidentity_id AS  ExternalBankidentity_id , 
               [${dbxschemaname}].accounts.CurrencyCode AS  CurrencyCode , 
               [${dbxschemaname}].accounts.User_id AS  User_id , 
               [${dbxschemaname}].accounts.AvailableBalance AS  AvailableBalance , 
               [${dbxschemaname}].accounts.Bank_id  AS  Bank_id , 
               [${dbxschemaname}].accounts.ShowTransactions  AS  ShowTransactions , 
               [${dbxschemaname}].accounts.CurrentBalance  AS  CurrentBalance , 
               [${dbxschemaname}].accounts.RoutingNumber  AS  RoutingNumber , 
               [${dbxschemaname}].[transaction].Id  AS  transactionId , 
               [${dbxschemaname}].[transaction].Type_id  AS  transactiontype , 
               [${dbxschemaname}].[transaction].Customer_id  AS  Customer_id , 
               [${dbxschemaname}].[transaction].ExpenseCategory_id  AS  ExpenseCategory_id , 
               [${dbxschemaname}].[transaction].billid  AS  Bill_id , 
               [${dbxschemaname}].[transaction].Reference_id  AS  Reference_id , 
               [${dbxschemaname}].[transaction].fromAccountNumber  AS  fromAccountNumber , 
               [${dbxschemaname}].[transaction].fromAccountBalance  AS  fromAccountBalance , 
               [${dbxschemaname}].[transaction].toAccountNumber  AS  toAccountNumber , 
               [${dbxschemaname}].[transaction].toAccountBalance  AS  toAccountBalance , 
               [${dbxschemaname}].[transaction].amount  AS  amount , 
               [${dbxschemaname}].[transaction].convertedAmount  AS  convertedAmount , 
               [${dbxschemaname}].[transaction].transactionCurrency  AS  transactionCurrency , 
               [${dbxschemaname}].[transaction].baseCurrency  AS  baseCurrency , 
               [${dbxschemaname}].[transaction].Status_id  AS  Status_id , 
               [${dbxschemaname}].[transaction].statusDesc  AS  statusDesc , 
               [${dbxschemaname}].[transaction].isScheduled  AS  isScheduled , 
               [${dbxschemaname}].[transaction].category  AS  category , 
               [${dbxschemaname}].[transaction].billCategory  AS  billCategory , 
               [${dbxschemaname}].[transaction].toExternalAccountNumber  AS  ExternalAccountNumber , 
               [${dbxschemaname}].[transaction].Person_Id  AS  Person_Id , 
               [${dbxschemaname}].[transaction].frequencyType  AS  frequencyType , 
               [${dbxschemaname}].[transaction].createdDate  AS  createdDate , 
               [${dbxschemaname}].[transaction].cashlessEmail  AS  cashlessEmail , 
               [${dbxschemaname}].[transaction].cashlessMode  AS  cashlessMode , 
               [${dbxschemaname}].[transaction].cashlessOTP  AS  cashlessOTP , 
               [${dbxschemaname}].[transaction].cashlessOTPValidDate  AS  cashlessOTPValidDate , 
               [${dbxschemaname}].[transaction].cashlessPersonName  AS  cashlessPersonName , 
               [${dbxschemaname}].[transaction].cashlessPhone  AS  cashlessPhone , 
               [${dbxschemaname}].[transaction].cashlessSecurityCode  AS  cashlessSecurityCode , 
               [${dbxschemaname}].[transaction].cashWithdrawalTransactionStatus  AS  cashWithdrawalTransactionStatus , 
               [${dbxschemaname}].[transaction].frequencyEndDate  AS  frequencyEndDate , 
               [${dbxschemaname}].[transaction].frequencyStartDate  AS  frequencyStartDate , 
               [${dbxschemaname}].[transaction].hasDepositImage  AS  hasDepositImage , 
               [${dbxschemaname}].[transaction].Payee_id  AS  payeeId , 
               [${dbxschemaname}].[transaction].payeeName  AS  payeeName , 
               [${dbxschemaname}].[transaction].p2pContact  AS  p2pContact , 
               [${dbxschemaname}].[transaction].Person_Id  AS  personId , 
               [${dbxschemaname}].[transaction].recurrenceDesc  AS  recurrenceDesc , 
               [${dbxschemaname}].[transaction].numberOfRecurrences  AS  numberOfRecurrences , 
               [${dbxschemaname}].[transaction].scheduledDate  AS  scheduledDate , 
               [${dbxschemaname}].[transaction].transactionComments  AS  transactionComments , 
               [${dbxschemaname}].[transaction].notes  AS  transactionsNotes , 
               [${dbxschemaname}].[transaction].description  AS  transDescription , 
               [${dbxschemaname}].[transaction].transactionDate  AS  transactionDate , 
               [${dbxschemaname}].[transaction].postedDate  AS  postedDate , 
               [${dbxschemaname}].[transaction].frontImage1  AS  frontImage1 , 
               [${dbxschemaname}].[transaction].frontImage2  AS  frontImage2 , 
               [${dbxschemaname}].[transaction].backImage1  AS  backImage1 , 
               [${dbxschemaname}].[transaction].backImage2  AS  backImage2 , 
               [${dbxschemaname}].[transaction].checkDesc  AS  checkDesc , 
               [${dbxschemaname}].[transaction].checkNumber1  AS  checkNumber1 , 
               [${dbxschemaname}].[transaction].checkNumber2  AS  checkNumber2 , 
               [${dbxschemaname}].[transaction].checkNumber  AS  checkNumber , 
               [${dbxschemaname}].[transaction].checkReason  AS  checkReason , 
               [${dbxschemaname}].[transaction].requestValidity  AS  requestValidity , 
               [${dbxschemaname}].[transaction].checkDateOfIssue  AS  checkDateOfIssue , 
               [${dbxschemaname}].[transaction].bankName1  AS  bankName1 , 
               [${dbxschemaname}].[transaction].bankName2  AS  bankName2 , 
               [${dbxschemaname}].[transaction].withdrawlAmount1  AS  withdrawlAmount1 , 
               [${dbxschemaname}].[transaction].withdrawlAmount2  AS  withdrawlAmount2 , 
               [${dbxschemaname}].[transaction].cashAmount  AS  cashAmount , 
               [${dbxschemaname}].[transaction].payeeCurrency  AS  payeeCurrency , 
               [${dbxschemaname}].[transaction].fee  AS  fee , 
               [${dbxschemaname}].[transaction].feePaidByReceipent  AS  feePaidByReceipent , 
               [${dbxschemaname}].[transaction].feeCurrency  AS  feeCurrency , 
               [${dbxschemaname}].[transaction].isDisputed  AS  isDisputed , 
               [${dbxschemaname}].[transaction].disputeReason  AS  disputeReason , 
               [${dbxschemaname}].[transaction].disputeDescription  AS  disputeDescription , 
               [${dbxschemaname}].[transaction].disputeDate  AS  disputeDate , 
               [${dbxschemaname}].[transaction].disputeStatus  AS  disputeStatus , 
               [${dbxschemaname}].transactiontype.description  AS  description , 
               [${dbxschemaname}].[transaction].statementReference  AS  statementReference , 
               [${dbxschemaname}].[transaction].transCreditDebitIndicator  AS  transCreditDebitIndicator , 
               [${dbxschemaname}].[transaction].bookingDateTime  AS  bookingDateTime , 
               [${dbxschemaname}].[transaction].valueDateTime  AS  valueDateTime , 
               [${dbxschemaname}].[transaction].transactionInformation  AS  transactionInformation , 
               [${dbxschemaname}].[transaction].addressLine  AS  addressLine , 
               [${dbxschemaname}].[transaction].transactionAmount  AS  transactionAmount , 
               [${dbxschemaname}].[transaction].chargeAmount  AS  chargeAmount , 
               [${dbxschemaname}].[transaction].chargeCurrency  AS  chargeCurrency , 
               [${dbxschemaname}].[transaction].sourceCurrency  AS  sourceCurrency , 
               [${dbxschemaname}].[transaction].targetCurrency  AS  targetCurrency , 
               [${dbxschemaname}].[transaction].unitCurrency  AS  unitCurrency , 
               [${dbxschemaname}].[transaction].exchangeRate  AS  exchangeRate , 
               [${dbxschemaname}].[transaction].contractIdentification  AS  contractIdentification , 
               [${dbxschemaname}].[transaction].quotationDate  AS  quotationDate , 
               [${dbxschemaname}].[transaction].instructedAmount  AS  instructedAmount , 
               [${dbxschemaname}].[transaction].instructedCurrency  AS  instructedCurrency , 
               [${dbxschemaname}].[transaction].transactionCode  AS  transactionCode , 
               [${dbxschemaname}].[transaction].transactionSubCode  AS  transactionSubCode , 
               [${dbxschemaname}].[transaction].proprietaryTransactionCode  AS  proprietaryTransactionCode , 
               [${dbxschemaname}].[transaction].proprietaryTransactionIssuer  AS  proprietaryTransactionIssuer , 
               [${dbxschemaname}].[transaction].balanceCreditDebitIndicator  AS  balanceCreditDebitIndicator , 
               [${dbxschemaname}].[transaction].balanceType  AS  balanceType , 
               [${dbxschemaname}].[transaction].balanceAmount  AS  balanceAmount , 
               [${dbxschemaname}].[transaction].balanceCurrency  AS  balanceCurrency , 
               [${dbxschemaname}].[transaction].merchantName  AS  merchantName , 
               [${dbxschemaname}].[transaction].merchantCategoryCode  AS  merchantCategoryCode , 
               [${dbxschemaname}].[transaction].creditorAgentSchemeName  AS  creditorAgentSchemeName , 
               [${dbxschemaname}].[transaction].creditorAgentIdentification  AS  creditorAgentIdentification , 
               [${dbxschemaname}].[transaction].creditorAgentName  AS  creditorAgentName , 
               [${dbxschemaname}].[transaction].creditorAgentaddressType  AS  creditorAgentaddressType , 
               [${dbxschemaname}].[transaction].creditorAgentDepartment  AS  creditorAgentDepartment , 
               [${dbxschemaname}].[transaction].creditorAgentSubDepartment  AS  creditorAgentSubDepartment , 
               [${dbxschemaname}].[transaction].creditorAgentStreetName  AS  creditorAgentStreetName , 
               [${dbxschemaname}].[transaction].creditorAgentBuildingNumber  AS  creditorAgentBuildingNumber , 
               [${dbxschemaname}].[transaction].creditorAgentPostCode  AS  creditorAgentPostCode , 
               [${dbxschemaname}].[transaction].creditorAgentTownName  AS  creditorAgentTownName , 
               [${dbxschemaname}].[transaction].creditorAgentCountrySubDivision  AS  creditorAgentCountrySubDivision , 
               [${dbxschemaname}].[transaction].creditorAgentCountry  AS  creditorAgentCountry , 
               [${dbxschemaname}].[transaction].creditorAgentAddressLine  AS  creditorAgentAddressLine , 
               [${dbxschemaname}].[transaction].creditorAccountschemeName  AS  creditorAccountschemeName , 
               [${dbxschemaname}].[transaction].creditorAccountIdentification  AS  creditorAccountIdentification , 
               [${dbxschemaname}].[transaction].creditorAccountName  AS  creditorAccountName , 
               [${dbxschemaname}].[transaction].creditorAccountseconIdentification  AS  creditorAccountseconIdentification , 
               [${dbxschemaname}].[transaction].debtorAgentSchemeName  AS  debtorAgentSchemeName , 
               [${dbxschemaname}].[transaction].debtorAgentIdentification  AS  debtorAgentIdentification , 
               [${dbxschemaname}].[transaction].debtorAgentName  AS  debtorAgentName , 
               [${dbxschemaname}].[transaction].debtorAgentAddressType  AS  debtorAgentAddressType , 
               [${dbxschemaname}].[transaction].debtorAgentDepartment  AS  debtorAgentDepartment , 
               [${dbxschemaname}].[transaction].debtorAgentSubDepartment  AS  debtorAgentSubDepartment , 
               [${dbxschemaname}].[transaction].debtorAgentStreetName  AS  debtorAgentStreetName , 
               [${dbxschemaname}].[transaction].debtorAgentBuildingNumber  AS  debtorAgentBuildingNumber , 
               [${dbxschemaname}].[transaction].dedtorAgentPostCode  AS  dedtorAgentPostCode , 
               [${dbxschemaname}].[transaction].debtorAgentTownName  AS  debtorAgentTownName , 
               [${dbxschemaname}].[transaction].debtorAgentCountrySubDivision  AS  debtorAgentCountrySubDivision , 
               [${dbxschemaname}].[transaction].debtorAgentCountry  AS  debtorAgentCountry , 
               [${dbxschemaname}].[transaction].debtorAgentAddressLine  AS  debtorAgentAddressLine , 
               [${dbxschemaname}].[transaction].debtorAccountschemeName  AS  debtorAccountschemeName , 
               [${dbxschemaname}].[transaction].debtorAccountIdentification  AS  debtorAccountIdentification , 
               [${dbxschemaname}].[transaction].debtorAccountName  AS  debtorAccountName , 
               [${dbxschemaname}].[transaction].debtorAccountseconIdentification  AS  debtorAccountseconIdentification , 
               [${dbxschemaname}].[transaction].cardInstrumentSchemeName  AS  cardInstrumentSchemeName , 
               [${dbxschemaname}].[transaction].cardInstrumentAuthorisationType  AS  cardInstrumentAuthorisationType , 
               [${dbxschemaname}].[transaction].cardInstrumentName  AS  cardInstrumentName , 
               [${dbxschemaname}].[transaction].cardInstrumentIdentification  AS  cardInstrumentIdentification , 
               [${dbxschemaname}].[transaction].FirstPaymentDateTime  AS  FirstPaymentDateTime , 
               [${dbxschemaname}].[transaction].NextPaymentDateTime  AS  NextPaymentDateTime , 
               [${dbxschemaname}].[transaction].FinalPaymentDateTime  AS  FinalPaymentDateTime , 
               [${dbxschemaname}].[transaction].StandingOrderStatusCode  AS  StandingOrderStatusCode , 
               [${dbxschemaname}].[transaction].FP_Amount  AS  FP_Amount , 
               [${dbxschemaname}].[transaction].FP_Currency  AS  FP_Currency , 
               [${dbxschemaname}].[transaction].NP_Amount  AS  NP_Amount , 
               [${dbxschemaname}].[transaction].NP_Currency  AS  NP_Currency , 
               [${dbxschemaname}].[transaction].FPA_Amount  AS  FPA_Amount , 
               [${dbxschemaname}].[transaction].FPA_Currency  AS  FPA_Currency , 
               [${dbxschemaname}].[transaction].IBAN  AS  IBAN , 
               [${dbxschemaname}].[transaction].sortCode  AS  sortCode , 
               [${dbxschemaname}].[transaction].beneficiaryName  AS  beneficiaryName , 
               [${dbxschemaname}].[transaction].bankName  AS  bankName , 
               [${dbxschemaname}].[transaction].swiftCode  AS  swiftCode , 
               [${dbxschemaname}].accounts.NickName  AS  nickName 
         FROM [${dbxschemaname}].accounts INNER JOIN [${dbxschemaname}].[transaction] ON
		 (([${dbxschemaname}].accounts . Account_id  =  [${dbxschemaname}].[transaction]. fromAccountNumber ) OR ( [${dbxschemaname}].accounts . Account_id  =  [${dbxschemaname}].[transaction] . toAccountNumber ))
		 INNER JOIN [${dbxschemaname}].transactiontype ON 
         [${dbxschemaname}].[transaction] . Type_id  =  [${dbxschemaname}].transactiontype . Id
         
GO
/****** Object:  View [${dbxschemaname}].[accountstatementview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE   
      VIEW [${dbxschemaname}].[accountstatementview]
      AS 
         SELECT 
               [${dbxschemaname}].accounts.Type_id AS Type_id, 
               [${dbxschemaname}].accountstatement.description AS Description, 
               [${dbxschemaname}].accountstatement.statementLink AS Statementlink, 
               [${dbxschemaname}].accountstatement.Account_id AS Account_id, 
               [${dbxschemaname}].accountstatement.month AS Month
         FROM [${dbxschemaname}].accounts 
            INNER JOIN [${dbxschemaname}].accountstatement ON
         [${dbxschemaname}].accounts.Account_id = [${dbxschemaname}].accountstatement.Account_id
GO
/****** Object:  View [${dbxschemaname}].[accountsview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE   
      VIEW [${dbxschemaname}].[accountsview]
      AS 
         SELECT 
            
               [${dbxschemaname}].accounts.ExternalBankidentity_id AS ExternalBankidentity_id, 
               [${dbxschemaname}].externalbankidentity.MainUser_id AS MainUser_id, 
               [${dbxschemaname}].externalbankidentity.User_id AS User_id, 
               [${dbxschemaname}].externalbankidentity.Password AS Password, 
               [${dbxschemaname}].externalbankidentity.SessionToken AS SessionToken, 
               externalbank.BankName AS BankName, 
               externalbank.logo AS logo, 
               [${dbxschemaname}].externalbankidentity.ExternalBank_id AS ExternalBank_id, 
               [${dbxschemaname}].accounts.Account_id AS Account_id, 
               [${dbxschemaname}].accounts.AccountName AS AccountName, 
               [${dbxschemaname}].accounts.CurrencyCode AS CurrencyCode, 
               [${dbxschemaname}].accounts.AvailableBalance AS AvailableBalance, 
               [${dbxschemaname}].accounts.AccountHolder AS AccountHolder, 
               [${dbxschemaname}].accounts.Address AS Address, 
               [${dbxschemaname}].accounts.Scheme AS Scheme, 
               [${dbxschemaname}].accounts.Number AS Number, 
               [${dbxschemaname}].accounts.error AS error, 
               [${dbxschemaname}].accounts.LastUpdated AS LastUpdated, 
               [${dbxschemaname}].accounts.InternalAccount AS InternalAccount, 
               [${dbxschemaname}].accounts.Type_id AS Type_id, 
               [${dbxschemaname}].accounts.NickName AS NickName, 
               [${dbxschemaname}].accounts.FavouriteStatus AS FavouriteStatus, 
               accounttype.TypeDescription AS TypeDescription
         FROM [${dbxschemaname}].accounts 
            INNER JOIN [${dbxschemaname}].externalbankidentity ON
			[${dbxschemaname}].accounts.ExternalBankidentity_id = [${dbxschemaname}].externalbankidentity.id
            INNER JOIN [${dbxschemaname}].externalbank ON
			[${dbxschemaname}].externalbankidentity.ExternalBank_id = externalbank.id
            INNER JOIN [${dbxschemaname}].accounttype ON
			[${dbxschemaname}].accounts.Type_id = accounttype.TypeID
            
GO
/****** Object:  View [${dbxschemaname}].[alertattribute_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[achfile_view] (
   [featureActionId], 
   [companyId], 
   [createdby], 
   [createdts], 
   [status], 
   [roleId], 
   [scheduledDate], 
   [amount], 
   [fromAccountNumber])
AS 
   SELECT 
      achfile.featureActionId AS featureActionId, 
      achfile.companyId AS companyId, 
      achfile.createdby AS createdby, 
      achfile.createdts AS createdts, 
      achfile.status AS status, 
      achfile.roleId AS roleId, 
      CAST(achfilerecord.effectiveDate AS datetime2(0)) AS scheduledDate, 
      achfilerecord.offsetAmount AS amount, 
      achfilerecord.offsetAccountNumber AS fromAccountNumber
   FROM ([${dbxschemaname}].achfile 
      INNER JOIN [${dbxschemaname}].achfilerecord 
      ON ((achfile.achFile_id = achfilerecord.achFileId)))
GO
/****** Object:  View [${dbxschemaname}].[achtransaction_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[achtransaction_view] (
   [transaction_id], 
   [fromAccount], 
   [effectiveDate], 
   [requestId], 
   [createdby], 
   [roleId], 
   [createdts], 
   [maxAmount], 
   [status], 
   [transactionType_id], 
   [templateType_id], 
   [companyId], 
   [templateRequestType_id], 
   [softDelete], 
   [templateName], 
   [confirmationNumber], 
   [actedBy], 
   [template_id], 
   [updatedts], 
   [totalAmount], 
   [featureActionId], 
   [fromAccountNumber], 
   [amount], 
   [scheduledDate])
AS 
   SELECT 
      achtransaction.transaction_id AS transaction_id, 
      achtransaction.fromAccount AS fromAccount, 
      achtransaction.effectiveDate AS effectiveDate, 
      achtransaction.requestId AS requestId, 
      achtransaction.createdby AS createdby, 
      achtransaction.roleId AS roleId, 
      achtransaction.createdts AS createdts, 
      achtransaction.maxAmount AS maxAmount, 
      achtransaction.status AS status, 
      achtransaction.transactionType_id AS transactionType_id, 
      achtransaction.templateType_id AS templateType_id, 
      achtransaction.companyId AS companyId, 
      achtransaction.templateRequestType_id AS templateRequestType_id, 
      achtransaction.softDelete AS softDelete, 
      achtransaction.templateName AS templateName, 
      achtransaction.confirmationNumber AS confirmationNumber, 
      achtransaction.actedBy AS actedBy, 
      achtransaction.template_id AS template_id, 
      achtransaction.updatedts AS updatedts, 
      achtransaction.totalAmount AS totalAmount, 
      achtransaction.featureActionId AS featureActionId, 
      achtransaction.fromAccount AS fromAccountNumber, 
      achtransaction.totalAmount AS amount, 
      CAST(achtransaction.effectiveDate AS datetime2(0)) AS scheduledDate
   FROM [${dbxschemaname}].achtransaction
GO
/****** Object:  View [${dbxschemaname}].[alertattribute_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alertattribute_view] (
   [alertattribute_id], 
   [alertattribute_LanguageCode], 
   [alertattribute_name], 
   [alertattribute_type], 
   [alertattribute_softdeleteflag], 
   [alertattributelistvalues_id], 
   [alertattributelistvalues_AlertAttributeId], 
   [alertattributelistvalues_LanguageCode], 
   [alertattributelistvalues_name], 
   [alertattributelistvalues_softdeleteflag])
AS 
   SELECT 
      alertattribute.id AS alertattribute_id, 
      alertattribute.LanguageCode AS alertattribute_LanguageCode, 
      alertattribute.name AS alertattribute_name, 
      alertattribute.type AS alertattribute_type, 
      alertattribute.softdeleteflag AS alertattribute_softdeleteflag, 
      alertattributelistvalues.id AS alertattributelistvalues_id, 
      alertattributelistvalues.AlertAttributeId AS alertattributelistvalues_AlertAttributeId, 
      alertattributelistvalues.LanguageCode AS alertattributelistvalues_LanguageCode, 
      alertattributelistvalues.name AS alertattributelistvalues_name, 
      alertattributelistvalues.softdeleteflag AS alertattributelistvalues_softdeleteflag
   FROM ([${dbxschemaname}].alertattribute 
      LEFT JOIN [${dbxschemaname}].alertattributelistvalues 
      ON ((alertattribute.id = alertattributelistvalues.AlertAttributeId)))
GO
/****** Object:  View [${dbxschemaname}].[alertcategory_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alertcategory_view] (
   [alertcategory_id], 
   [alertcategory_status_id], 
   [alertcategory_accountLevel], 
   [alertcategory_DisplaySequence], 
   [alertcategory_softdeleteflag], 
   [alertcategory_Name], 
   [alertcategorytext_LanguageCode], 
   [alertcategorytext_DisplayName], 
   [alertcategorytext_Description], 
   [alertcategorytext_createdby], 
   [alertcategorytext_modifiedby], 
   [alertcategorytext_createdts], 
   [alertcategorytext_lastmodifiedts], 
   [alertcategorytext_synctimestamp], 
   [alertcategorytext_softdeleteflag])
AS 
   SELECT 
      dbxalertcategory.id AS alertcategory_id, 
      dbxalertcategory.status_id AS alertcategory_status_id, 
      dbxalertcategory.accountLevel AS alertcategory_accountLevel, 
      dbxalertcategory.DisplaySequence AS alertcategory_DisplaySequence, 
      dbxalertcategory.softdeleteflag AS alertcategory_softdeleteflag, 
      dbxalertcategory.Name AS alertcategory_Name, 
      dbxalertcategorytext.LanguageCode AS alertcategorytext_LanguageCode, 
      dbxalertcategorytext.DisplayName AS alertcategorytext_DisplayName, 
      dbxalertcategorytext.Description AS alertcategorytext_Description, 
      dbxalertcategorytext.createdby AS alertcategorytext_createdby, 
      dbxalertcategorytext.modifiedby AS alertcategorytext_modifiedby, 
      dbxalertcategorytext.createdts AS alertcategorytext_createdts, 
      dbxalertcategorytext.lastmodifiedts AS alertcategorytext_lastmodifiedts, 
      dbxalertcategorytext.synctimestamp AS alertcategorytext_synctimestamp, 
      dbxalertcategorytext.softdeleteflag AS alertcategorytext_softdeleteflag
   FROM ([${dbxschemaname}].dbxalertcategorytext 
      INNER JOIN [${dbxschemaname}].dbxalertcategory 
      ON ((dbxalertcategorytext.AlertCategoryId = dbxalertcategory.id)))
GO
/****** Object:  View [${dbxschemaname}].[alertcustomerchannels_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view] (
   [AlertTypeId], 
   [AccountType], 
   [Name], 
   [Customer_id], 
   [AlertCategoryId], 
   [AttributeId], 
   [AlertConditionId], 
   [Value1], 
   [Value2], 
   [IsGlobal], 
   [ChannelId], 
   [AccountId], 
   [Status_id], 
   [accountLevel])
AS 
   SELECT 
      dbxcustomeralertentitlement.AlertTypeId AS AlertTypeId, 
      dbxcustomeralertentitlement.AccountType AS AccountType, 
      dbxalerttype.Name AS Name, 
      customeralertcategorychannel.Customer_id AS Customer_id, 
      customeralertcategorychannel.AlertCategoryId AS AlertCategoryId, 
      dbxalerttype.AttributeId AS AttributeId, 
      dbxalerttype.AlertConditionId AS AlertConditionId, 
      dbxcustomeralertentitlement.Value1 AS Value1, 
      dbxcustomeralertentitlement.Value2 AS Value2, 
      dbxalerttype.IsGlobal AS IsGlobal, 
      customeralertcategorychannel.ChannelId AS ChannelId, 
      customeralertcategorychannel.AccountId AS AccountId, 
      status.Type_id AS Status_id, 
      dbxalertcategory.accountLevel AS accountLevel
   FROM (((([${dbxschemaname}].dbxcustomeralertentitlement 
      INNER JOIN [${dbxschemaname}].dbxalerttype 
      ON ((dbxcustomeralertentitlement.AlertTypeId = dbxalerttype.id))) 
      INNER JOIN [${dbxschemaname}].dbxalertcategory 
      ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id))) 
      INNER JOIN [${dbxschemaname}].customeralertcategorychannel 
      ON ((
         (dbxcustomeralertentitlement.Customer_id = customeralertcategorychannel.Customer_id) AND 
         (dbxcustomeralertentitlement.AccountId = customeralertcategorychannel.AccountId) AND 
         (dbxalertcategory.id = customeralertcategorychannel.AlertCategoryId)))) 
      INNER JOIN [${dbxschemaname}].status 
      ON ((dbxalerttype.Status_id = status.id)))
GO
/****** Object:  View [${dbxschemaname}].[alertcustomersaccountchannels_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alertcustomersaccountchannels_view] (
   [AlertTypeId], 
   [AccountType], 
   [Name], 
   [Customer_id], 
   [AlertCategoryId], 
   [AttributeId], 
   [AlertConditionId], 
   [Value1], 
   [Value2], 
   [IsGlobal], 
   [ChannelId], 
   [AccountId], 
   [Status_id], 
   [accountLevel])
AS 
   SELECT 
      dbxcustomeralertentitlement.AlertTypeId AS AlertTypeId, 
      dbxcustomeralertentitlement.AccountType AS AccountType, 
      dbxalerttype.Name AS Name, 
      customeralertcategorychannel.Customer_id AS Customer_id, 
      customeralertcategorychannel.AlertCategoryId AS AlertCategoryId, 
      dbxalerttype.AttributeId AS AttributeId, 
      dbxalerttype.AlertConditionId AS AlertConditionId, 
      dbxcustomeralertentitlement.Value1 AS Value1, 
      dbxcustomeralertentitlement.Value2 AS Value2, 
      dbxalerttype.IsGlobal AS IsGlobal, 
      customeralertcategorychannel.ChannelId AS ChannelId, 
      customeralertcategorychannel.AccountId AS AccountId, 
      status.Type_id AS Status_id, 
      dbxalertcategory.accountLevel AS accountLevel
   FROM (((([${dbxschemaname}].dbxcustomeralertentitlement 
      INNER JOIN [${dbxschemaname}].dbxalerttype 
      ON ((dbxcustomeralertentitlement.AlertTypeId = dbxalerttype.id))) 
      INNER JOIN [${dbxschemaname}].dbxalertcategory 
      ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id))) 
      INNER JOIN [${dbxschemaname}].customeralertcategorychannel 
      ON ((
         (dbxcustomeralertentitlement.Customer_id = customeralertcategorychannel.Customer_id) AND 
         (dbxcustomeralertentitlement.AccountId = customeralertcategorychannel.AccountId) AND 
         (dbxalertcategory.id = customeralertcategorychannel.AlertCategoryId)))) 
      INNER JOIN [${dbxschemaname}].status 
      ON ((dbxalerttype.Status_id = status.id)))
GO
/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view] (
   [AlertTypeId], 
   [AlertCategoryId], 
   [AttributeId], 
   [AlertConditionId], 
   [Value1], 
   [Value2], 
   [alerttype_status_id], 
   [IsGlobal], 
   [alertcategory_status_id], 
   [ChannelId], 
   [AlertSubTypeId], 
   [alertsubtypetype_status_id], 
   [accountLevel])
AS 
   SELECT 
      dbxalerttype.id AS AlertTypeId, 
      dbxalerttype.AlertCategoryId AS AlertCategoryId, 
      dbxalerttype.AttributeId AS AttributeId, 
      dbxalerttype.AlertConditionId AS AlertConditionId, 
      dbxalerttype.Value1 AS Value1, 
      dbxalerttype.Value2 AS Value2, 
      dbxalerttype.Status_id AS alerttype_status_id, 
      dbxalerttype.IsGlobal AS IsGlobal, 
      dbxalertcategory.status_id AS alertcategory_status_id, 
      alertcategorychannel.ChannelID AS ChannelId, 
      alertsubtype.id AS AlertSubTypeId, 
      alertsubtype.Status_id AS alertsubtypetype_status_id, 
      dbxalertcategory.accountLevel AS accountLevel
   FROM ((([${dbxschemaname}].dbxalerttype 
      INNER JOIN [${dbxschemaname}].alertcategorychannel 
      ON ((dbxalerttype.AlertCategoryId = alertcategorychannel.AlertCategoryId))) 
      INNER JOIN [${dbxschemaname}].dbxalertcategory 
      ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id))) 
      INNER JOIN [${dbxschemaname}].alertsubtype 
      ON ((dbxalerttype.id = alertsubtype.AlertTypeId)))
GO
/****** Object:  View [${dbxschemaname}].[alerttype_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[alerttype_view] (
   [alerttype_id], 
   [alerttype_Name], 
   [alerttype_AlertCategoryId], 
   [alerttype_AttributeId], 
   [alerttype_AlertConditionId], 
   [alerttype_Value1], 
   [alerttype_Value2], 
   [alerttype_Status_id], 
   [alerttype_IsGlobal], 
   [alerttype_DisplaySequence], 
   [alerttype_softdeleteflag], 
   [alerttypetext_LanguageCode], 
   [alerttypetext_DisplayName], 
   [alerttypetext_Description], 
   [alerttypetext_createdby], 
   [alerttypetext_modifiedby], 
   [alerttypetext_createdts], 
   [alerttypetext_lastmodifiedts], 
   [alerttypetext_synctimestamp], 
   [alerttypetext_softdeleteflag])
AS 
   SELECT 
      dbxalerttype.id AS alerttype_id, 
      dbxalerttype.Name AS alerttype_Name, 
      dbxalerttype.AlertCategoryId AS alerttype_AlertCategoryId, 
      dbxalerttype.AttributeId AS alerttype_AttributeId, 
      dbxalerttype.AlertConditionId AS alerttype_AlertConditionId, 
      dbxalerttype.Value1 AS alerttype_Value1, 
      dbxalerttype.Value2 AS alerttype_Value2, 
      dbxalerttype.Status_id AS alerttype_Status_id, 
      dbxalerttype.IsGlobal AS alerttype_IsGlobal, 
      dbxalerttype.DisplaySequence AS alerttype_DisplaySequence, 
      dbxalerttype.softdeleteflag AS alerttype_softdeleteflag, 
      dbxalerttypetext.LanguageCode AS alerttypetext_LanguageCode, 
      dbxalerttypetext.DisplayName AS alerttypetext_DisplayName, 
      dbxalerttypetext.Description AS alerttypetext_Description, 
      dbxalerttypetext.createdby AS alerttypetext_createdby, 
      dbxalerttypetext.modifiedby AS alerttypetext_modifiedby, 
      dbxalerttypetext.createdts AS alerttypetext_createdts, 
      dbxalerttypetext.lastmodifiedts AS alerttypetext_lastmodifiedts, 
      dbxalerttypetext.synctimestamp AS alerttypetext_synctimestamp, 
      dbxalerttypetext.softdeleteflag AS alerttypetext_softdeleteflag
   FROM ([${dbxschemaname}].dbxalerttypetext 
      INNER JOIN [${dbxschemaname}].dbxalerttype 
      ON ((dbxalerttypetext.AlertTypeId = dbxalerttype.id)))
GO
/****** Object:  View [${dbxschemaname}].[archivedcustomer_request_detailed_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[alerttypeaccounttype_view]
      AS 
         SELECT 
            
				accounttype.TypeID AS accounttype_TypeID, 
				accounttype.TypeDescription AS accounttype_TypeDescription, 
                accounttype.displayName AS accounttype_displayName, 
				dbxalerttype.id AS alerttype_AlertTypeId, 
				dbxalerttype.Name AS alerttype_Name,
				(SELECT 
					case when (COUNT(0) > 0) then 1 else 0 end
				FROM
					[${dbxschemaname}].alerttypeaccounttype
				WHERE
					((alerttypeaccounttype.AccountTypeId = accounttype.TypeID)
						AND (alerttypeaccounttype.AlertTypeId = dbxalerttype.id))) AS isSupported,
				dbxalerttype.softdeleteflag AS alerttype_softdeleteflag
                     FROM [${dbxschemaname}].alerttypeaccounttype INNER JOIN [${dbxschemaname}].accounttype ON
                     [${dbxschemaname}].alerttypeaccounttype.AccountTypeId = [${dbxschemaname}].accounttype.TypeID INNER JOIN [${dbxschemaname}].dbxalerttype ON
					 [${dbxschemaname}].alerttypeaccounttype.AlertTypeId = [${dbxschemaname}].dbxalerttype.id
GO
/****** Object:  View [${dbxschemaname}].[alerttypeapp_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[alerttypeapp_view]
      AS 
         SELECT 
               app.id AS app_Id, 
               dbxalerttype.id AS alerttype_AlertTypeId, 
               dbxalerttype.Name AS alerttype_Name, 
               app.Name AS app_Name, 
               app.Description AS app_Description, 
			           (SELECT 
                case when(COUNT(0) > 0) then 1 else 0 end
            FROM
                [${dbxschemaname}].alerttypeapp
            WHERE
                (alerttypeapp.AppId = app.id
                    AND alerttypeapp.AlertTypeId = dbxalerttype.id)) AS isSupported,
			   dbxalerttype.softdeleteflag AS alerttype_softdeleteflag, 
               app.softdeleteflag AS app_softdeleteflag
                     FROM [${dbxschemaname}].alerttypeapp INNER JOIN [${dbxschemaname}].app ON
                     [${dbxschemaname}].alerttypeapp.AppId = [${dbxschemaname}].app.id INNER JOIN [${dbxschemaname}].dbxalerttype ON
					 [${dbxschemaname}].alerttypeapp.AlertTypeId = [${dbxschemaname}].dbxalerttype.id

GO
/****** Object:  View [${dbxschemaname}].[alerttypecustomertype_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[alerttypecustomertype_view]
      AS 
         SELECT 
            
               customertype.id AS customertype_TypeID, 
               customertype.Name AS customertype_Name, 
               dbxalerttype.id AS alerttype_AlertTypeId, 
               dbxalerttype.Name AS alerttype_Name, 
			   (SELECT 
               case when (COUNT(0) > 0) then 1 else 0 end
            FROM
                [${dbxschemaname}].alerttypecustomertype
            WHERE
                ((alerttypecustomertype.CustomerTypeId = customertype.id)
                    AND (alerttypecustomertype.AlertTypeId = dbxalerttype.id))) AS isSupported,
               customertype.softdeleteflag AS customertype_softdeleteflag, 
               dbxalerttype.softdeleteflag AS alerttype_softdeleteflag
                     FROM [${dbxschemaname}].alerttypecustomertype INNER JOIN [${dbxschemaname}].customertype ON
                     [${dbxschemaname}].alerttypecustomertype.CustomerTypeId = [${dbxschemaname}].customertype.id INNER JOIN [${dbxschemaname}].dbxalerttype ON
					 [${dbxschemaname}].alerttypecustomertype.AlertTypeId = [${dbxschemaname}].dbxalerttype.id  
GO
/****** Object:  View [${dbxschemaname}].[allaccountsview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[allaccountsview] (
   [AccountHolder], 
   [Account_id], 
   [AccountName], 
   [Type_id],
   [IsOrganizationAccount],
   [accountType], 
   [Membership_id], 
   [Taxid], 
   [phoneNumber],
   [emailId],
   [name])
AS 
   /*
   *   SSMA informational messages:
   *   M2SS0052: string literal was converted to NUMERIC literal
   */

SELECT DISTINCT 
      accounts.AccountHolder AS AccountHolder, 
      accounts.Account_id AS Account_id, 
      accounts.AccountName AS AccountName, 
      accounts.Type_id AS Type_id,
	  accounts.isBusinessAccount AS IsOrganizationAccount,
      accounttype.TypeDescription AS accountType, 
      membershipaccounts.membershipId AS Membership_id, 
      membership.Taxid AS Taxid, 
      membership.phone AS phoneNumber,
	  membership.email AS emailId,
	  membership.name AS name
   FROM (([${dbxschemaname}].accounts 
      LEFT JOIN [${dbxschemaname}].membershipaccounts 
      ON ((accounts.Account_id = membershipaccounts.accountId))) 
      LEFT JOIN [${dbxschemaname}].accounttype 
      ON ((accounts.Type_id = accounttype.TypeID)))
	  LEFT JOIN [${dbxschemaname}].membership ON ((membership.id = membershipaccounts.membershipId))
GO
/****** Object:  View [${dbxschemaname}].[archivedcustomer_request_detailed_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[archivedcustomer_request_detailed_view] (
   [customerrequest_id], 
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
      archivedcustomerrequest.id AS customerrequest_id, 
      archivedcustomerrequest.RequestCategory_id AS customerrequest_RequestCategory_id, 
      archivedcustomerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer, 
      requestcategory.Name AS requestcategory_Name, 
      archivedcustomerrequest.Customer_id AS customerrequest_Customer_id, 
      customer.FirstName AS customer_FirstName, 
      customer.MiddleName AS customer_MiddleName, 
      customer.FirstName + customer.LastName AS customer_Fullname, 
      systemuser.FirstName + systemuser.LastName AS customerrequest_AssignedTo_Name, 
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
      archivedcustomerrequest.Priority AS customerrequest_Priority, 
      archivedcustomerrequest.Status_id AS customerrequest_Status_id, 
      archivedcustomerrequest.AssignedTo AS customerrequest_AssignedTo, 
      archivedcustomerrequest.RequestSubject AS customerrequest_RequestSubject, 
      archivedcustomerrequest.Accountid AS customerrequest_Accountid, 
      archivedcustomerrequest.createdby AS customerrequest_createdby, 
      archivedcustomerrequest.modifiedby AS customerrequest_modifiedby, 
      archivedcustomerrequest.createdts AS customerrequest_createdts, 
      archivedcustomerrequest.lastmodifiedts AS customerrequest_lastmodifiedts, 
      archivedcustomerrequest.synctimestamp AS customerrequest_synctimestamp, 
      archivedcustomerrequest.softdeleteflag AS customerrequest_softdeleteflag, 
      archivedrequestmessage.id AS requestmessage_id, 
      archivedrequestmessage.RepliedBy AS requestmessage_RepliedBy, 
      archivedrequestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name, 
      archivedrequestmessage.MessageDescription AS requestmessage_MessageDescription, 
      archivedrequestmessage.ReplySequence AS requestmessage_ReplySequence, 
      archivedrequestmessage.IsRead AS requestmessage_IsRead, 
      archivedrequestmessage.createdby AS requestmessage_createdby, 
      archivedrequestmessage.modifiedby AS requestmessage_modifiedby, 
      archivedrequestmessage.createdts AS requestmessage_createdts, 
      archivedrequestmessage.lastmodifiedts AS requestmessage_lastmodifiedts, 
      archivedrequestmessage.synctimestamp AS requestmessage_synctimestamp, 
      archivedrequestmessage.softdeleteflag AS requestmessage_softdeleteflag, 
      archivedmessageattachment.id AS messageattachment_id, 
      archivedmessageattachment.AttachmentType_id AS messageattachment_AttachmentType_id, 
      archivedmessageattachment.Media_id AS messageattachment_Media_id, 
      archivedmessageattachment.createdby AS messageattachment_createdby, 
      archivedmessageattachment.modifiedby AS messageattachment_modifiedby, 
      archivedmessageattachment.createdts AS messageattachment_createdts, 
      archivedmessageattachment.lastmodifiedts AS messageattachment_lastmodifiedts, 
      archivedmessageattachment.softdeleteflag AS messageattachment_softdeleteflag, 
      archivedmedia.id AS media_id, 
      archivedmedia.Name AS media_Name, 
      archivedmedia.Size AS media_Size, 
      archivedmedia.Type AS media_Type, 
      archivedmedia.Description AS media_Description, 
      archivedmedia.Url AS media_Url, 
      archivedmedia.createdby AS media_createdby, 
      archivedmedia.modifiedby AS media_modifiedby, 
      archivedmedia.lastmodifiedts AS media_lastmodifiedts, 
      archivedmedia.synctimestamp AS media_synctimestamp, 
      archivedmedia.softdeleteflag AS media_softdeleteflag
   FROM (((((([${dbxschemaname}].archivedcustomerrequest 
      INNER JOIN [${dbxschemaname}].archivedrequestmessage 
      ON ((archivedcustomerrequest.id = archivedrequestmessage.CustomerRequest_id))) 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((archivedcustomerrequest.Customer_id = customer.id))) 
      INNER JOIN [${dbxschemaname}].requestcategory 
      ON ((archivedcustomerrequest.RequestCategory_id = requestcategory.id))) 
      LEFT JOIN [${dbxschemaname}].systemuser 
      ON ((archivedcustomerrequest.AssignedTo = systemuser.id))) 
      LEFT JOIN [${dbxschemaname}].archivedmessageattachment 
      ON ((archivedrequestmessage.id = archivedmessageattachment.RequestMessage_id))) 
      LEFT JOIN [${dbxschemaname}].archivedmedia 
      ON ((archivedmessageattachment.Media_id = archivedmedia.id)))
GO
/****** Object:  View [${dbxschemaname}].[bankbranchview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[bankbranchview] (
   [id], 
   [address1], 
   [address2], 
   [city], 
   [state], 
   [zipCode], 
   [phone], 
   [workingHours], 
   [services], 
   [latitude], 
   [longitude], 
   [status], 
   [email], 
   [Type_id], 
   [Type])
AS 
   SELECT 
      bb.id AS id, 
      bb.address1 AS address1, 
      bb.address2 AS address2, 
      bb.city AS city, 
      bb.state AS state, 
      bb.zipCode AS zipCode, 
      bb.phone AS phone, 
      bb.workingHours AS workingHours, 
      bb.services AS services, 
      bb.latitude AS latitude, 
      bb.longitude AS longitude, 
      bb.status AS status, 
      bb.email AS email, 
      bb.Type_id AS Type_id, 
      bt.Type AS Type
   FROM ([${dbxschemaname}].bankbranch  AS bb 
      CROSS JOIN [${dbxschemaname}].branchtype  AS bt)
   WHERE (bb.Type_id = bt.id)
GO
/****** Object:  View [${dbxschemaname}].[bankfortransfer_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[bankfortransfer_view] (
   [id], 
   [Bank_Code], 
   [Bank_Name], 
   [Logo], 
   [Status_id], 
   [Bank_Url], 
   [Address_id], 
   [AddressLine1], 
   [AddressLine2], 
   [City_id], 
   [City_Name], 
   [Region_id], 
   [Region_Name], 
   [Country_id], 
   [Country_Name], 
   [Routing_Code], 
   [Routing_Number], 
   [Service_id], 
   [Service_Name])
AS 
   SELECT 
      bankfortransfer.id AS id, 
      bankfortransfer.Code AS Bank_Code, 
      bankfortransfer.Name AS Bank_Name, 
      bankfortransfer.Logo AS Logo, 
      bankfortransfer.Status_id AS Status_id, 
      bankfortransfer.Url AS Bank_Url, 
      bankfortransfer.Address_id AS Address_id, 
      address.addressLine1 AS AddressLine1, 
      address.addressLine2 AS AddressLine2, 
      address.City_id AS City_id, 
      city.Name AS City_Name, 
      region.id AS Region_id, 
      region.Name AS Region_Name, 
      region.Country_id AS Country_id, 
      country.Name AS Country_Name, 
      bankservice.RoutingCode AS Routing_Code, 
      bankservice.RoutingNumber AS Routing_Number, 
      bankservice.Service_id AS Service_id, 
      service.Name AS Service_Name
   FROM (((((([${dbxschemaname}].bankfortransfer 
      LEFT JOIN [${dbxschemaname}].bankservice 
      ON ((bankfortransfer.id = bankservice.BankForTransfer_id))) 
      INNER JOIN [${dbxschemaname}].address 
      ON ((bankfortransfer.Address_id = address.id))) 
      INNER JOIN [${dbxschemaname}].city 
      ON ((address.City_id = city.id))) 
      INNER JOIN [${dbxschemaname}].region 
      ON ((address.Region_id = region.id))) 
      INNER JOIN [${dbxschemaname}].country 
      ON ((region.Country_id = country.id))) 
      INNER JOIN [${dbxschemaname}].service 
      ON ((bankservice.Service_id = service.id)))
GO
/****** Object:  View [${dbxschemaname}].[billermasterview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[billermasterview] (
   [accountNumber], 
   [address], 
   [billerCategoryId], 
   [billerName], 
   [city], 
   [ebillSupport], 
   [id], 
   [state], 
   [zipCode], 
   [billerCategoryName])
AS 
   SELECT 
      bb.accountNumber AS accountNumber, 
      bb.address AS address, 
      bb.billerCategoryId AS billerCategoryId, 
      bb.billerName AS billerName, 
      bb.city AS city, 
      bb.ebillSupport AS ebillSupport, 
      bb.id AS id, 
      bb.state AS state, 
      bb.zipCode AS zipCode, 
      bc.categoryName AS billerCategoryName
   FROM ([${dbxschemaname}].billermaster  AS bb 
      CROSS JOIN [${dbxschemaname}].billercategory  AS bc)
   WHERE (bb.billerCategoryId = bc.id)
GO
/****** Object:  View [${dbxschemaname}].[branch_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[billview] (
   [balanceAmount], 
   [billDueDate], 
   [billGeneratedDate], 
   [description], 
   [dueAmount], 
   [ebillURL], 
   [id], 
   [paidAmount], 
   [paidDate], 
   [payeeId], 
   [currencyCode], 
   [fromAccountName], 
   [fromAccountNumber], 
   [User_id], 
   [payeeName], 
   [softDelete], 
   [ebillStatus], 
   [payeeNickName], 
   [payeeAddressLine1], 
   [billerCategory], 
   [billerCategoryId], 
   [ebillSupport])
AS 
   SELECT 
      bb.balanceAmount AS balanceAmount, 
      bb.billDueDate AS billDueDate, 
      bb.billGeneratedDate AS billGeneratedDate, 
      bb.description AS description, 
      bb.dueAmount AS dueAmount, 
      bb.ebillURL AS ebillURL, 
      bb.id AS id, 
      bb.paidAmount AS paidAmount, 
      bb.paidDate AS paidDate, 
      bb.Payee_id AS payeeId, 
      bb.currencyCode AS currencyCode, 
      ac.AccountName AS fromAccountName, 
      ac.Account_id AS fromAccountNumber, 
      ac.User_id AS User_id, 
      py.name AS payeeName, 
      py.softDelete AS softDelete, 
      py.eBillEnable AS ebillStatus, 
      py.nickName AS payeeNickName, 
      py.addressLine1 AS payeeAddressLine1, 
      bc.categoryName AS billerCategory, 
      bm.billerCategoryId AS billerCategoryId, 
      bm.ebillSupport AS ebillSupport
   FROM (((([${dbxschemaname}].bill  AS bb 
      CROSS JOIN [${dbxschemaname}].payee  AS py) 
      CROSS JOIN [${dbxschemaname}].accounts  AS ac) 
      CROSS JOIN [${dbxschemaname}].billercategory  AS bc) 
      CROSS JOIN [${dbxschemaname}].billermaster  AS bm)
   WHERE (
      (bb.billerMaster_id = bm.id) AND 
      (bb.Account_id = ac.Account_id) AND 
      (bm.billerCategoryId = bc.id) AND 
      (bb.Payee_id = py.Id))

GO
/****** Object:  View [${dbxschemaname}].[branch_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[branch_view] (
   [Branch_id], 
   [Branch_Typeid], 
   [Branch_Name], 
   [Branch_DisplayName], 
   [Branch_Description], 
   [Branch_Code], 
   [Branch_PhoneNumber], 
   [Branch_EmailId], 
   [Branch_Status_id], 
   [Branch_IsMainBranch], 
   [Branch_WorkSchedule_id], 
   [Branch_MainBranchCode], 
   [Branch_WebSiteUrl], 
   [City_id], 
   [City_Name], 
   [Address_id], 
   [Branch_Complete_Addr], 
   [Branch_createdby], 
   [Branch_modifiedby], 
   [Branch_createdts], 
   [Branch_lastmodifiedts], 
   [Branch_synctimestamp])
AS 
   /*
   *   SSMA warning messages:
   *   M2SS0250: Parameters used in N'ISNULL' function/operator are not of same data type so they are casted to Character datatype.
   *   M2SS0028: Input value '' can not be converted to DATETIME format
   *   M2SS0028: Input value '' can not be converted to DATETIME format
   *   M2SS0028: Input value '' can not be converted to DATETIME format
   */

   SELECT 
      loc.id AS Branch_id, 
      ISNULL(loc.Type_id, N'') AS Branch_Typeid, 
      ISNULL(loc.Name, N'') AS Branch_Name, 
      ISNULL(loc.DisplayName, N'') AS Branch_DisplayName, 
      ISNULL(loc.Description, N'') AS Branch_Description, 
      ISNULL(loc.Code, N'') AS Branch_Code, 
      ISNULL(loc.PhoneNumber, N'') AS Branch_PhoneNumber, 
      ISNULL(loc.EmailId, N'') AS Branch_EmailId, 
      ISNULL(loc.Status_id, N'') AS Branch_Status_id, 
      ISNULL(CAST(loc.IsMainBranch AS varchar(50)), N'') AS Branch_IsMainBranch, 
      ISNULL(loc.WorkSchedule_id, N'') AS Branch_WorkSchedule_id, 
      ISNULL(loc.MainBranchCode, N'') AS Branch_MainBranchCode, 
      ISNULL(loc.WebSiteUrl, N'') AS Branch_WebSiteUrl, 
      address.City_id AS City_id, 
      city.Name AS City_Name, 
      loc.Address_id AS Address_id, 
      
         address.addressLine1
          + 
         N', '
          + 
         ISNULL(address.addressLine2, N'')
          + 
         N', '
          + 
         city.Name
          + 
         N', '
          + 
         region.Name
          + 
         N', '
          + 
         country.Name
          + 
         N', '
          + 
         ISNULL(address.zipCode, N'') AS Branch_Complete_Addr, 
      ISNULL(loc.createdby, N'') AS Branch_createdby, 
      ISNULL(loc.modifiedby, N'') AS Branch_modifiedby, 
      ISNULL(loc.createdts, NULL) AS Branch_createdts, 
      ISNULL(loc.lastmodifiedts, NULL) AS Branch_lastmodifiedts, 
      ISNULL(loc.synctimestamp, NULL) AS Branch_synctimestamp
   FROM (((([${dbxschemaname}].location  AS loc 
      INNER JOIN [${dbxschemaname}].address 
      ON (((loc.Address_id = address.id) AND (loc.Type_id = 'Branch')))) 
      INNER JOIN [${dbxschemaname}].city 
      ON ((city.id = address.City_id))) 
      INNER JOIN [${dbxschemaname}].region 
      ON ((region.id = address.Region_id))) 
      INNER JOIN [${dbxschemaname}].country 
      ON ((city.Country_id = country.id)))
GO
/****** Object:  View [${dbxschemaname}].[card_request_notification_count_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[businesstypes_view] (
   [BusinessType_id], 
   [BusinessType_name], 
   [minAuthSignatory], 
   [maxAuthSignatory],
   [Groups_count],
   [Default_group],
   [Customers_Count],
   [DefaultGroupCustomers_Count])
AS 
   SELECT 
      businesstype.id AS BusinessType_id, 
      businesstype.name AS BusinessType_name, 
      businesstype.minAuthSignatory AS minAuthSignatory, 
      businesstype.maxAuthSignatory AS maxAuthSignatory,
      (SELECT count_big(groupbusinesstype.Group_id)
            FROM [${dbxschemaname}].groupbusinesstype
            WHERE groupbusinesstype.BusinessType_id = businesstype.id
         ) AS Groups_count, 
	(SELECT groupbusinesstype.Group_id AS Group_id
            FROM [${dbxschemaname}].groupbusinesstype
            WHERE (groupbusinesstype.BusinessType_id = businesstype.id) AND (groupbusinesstype.isDefaultGroup = 1)
         ) AS Default_group, 
      (SELECT 
                count(customerbusinesstype.Customer_id)
            FROM
                [${dbxschemaname}].customerbusinesstype
            WHERE
                (customerbusinesstype.BusinessType_id = businesstype.id)) AS Customers_Count,
         (SELECT 
                count(customergroup.Customer_id)
            FROM
                [${dbxschemaname}].customergroup
            WHERE
                ((customergroup.Group_id = 'Default_group')
                    AND customergroup.Customer_id IN (
			SELECT customerbusinesstype.Customer_id
            FROM [${dbxschemaname}].customerbusinesstype
            WHERE customerbusinesstype.BusinessType_id = businesstype.id) ))
			 AS DefaultGroupCustomers_Count
   FROM [${dbxschemaname}].businesstype 
GO
/****** Object:  View [${dbxschemaname}].[card_request_notification_count_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[card_request_notification_count_view] (
   [customerId], 
   [cardNumber], 
   [reqType], 
   [requestcount])
AS 
   SELECT tbl$1.customerId, tbl$1.cardNumber, tbl$1.reqType, tbl$1.requestcount
   FROM 
      (
         SELECT TOP (9223372036854775807) cardaccountrequest_alias.Customer_id AS customerId, cardaccountrequest_alias.CardAccountNumber AS cardNumber, N'REQUEST' AS reqType, count_big(cardaccountrequest_alias.CardAccountNumber) AS requestcount
         FROM [${dbxschemaname}].cardaccountrequest  AS cardaccountrequest_alias
         GROUP BY cardaccountrequest_alias.Customer_id, cardaccountrequest_alias.CardAccountNumber
            ORDER BY cardaccountrequest_alias.Customer_id, cardaccountrequest_alias.CardAccountNumber
      )  AS tbl$1
    UNION
   SELECT notificationcardinfo_alias.Customer_id AS customerId, notificationcardinfo_alias.CardNumber AS cardNumber, N'NOTIFICATION' AS reqType, 1 AS requestcount
   FROM [${dbxschemaname}].notificationcardinfo  AS notificationcardinfo_alias
GO
/****** Object:  View [${dbxschemaname}].[cardaccountrequest_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[cardaccountrequest_view] (
   [Request_id], 
   [Date], 
   [Type], 
   [CardAccountNumber], 
   [CardAccountName], 
   [Reason], 
   [AccountType], 
   [Status], 
   [CustomerId], 
   [CommunicationValue], 
   [DeliveryMode], 
   [Address])
AS 
   SELECT 
      cardaccountrequest.id AS Request_id, 
      cardaccountrequest.Date AS Date, 
      cardaccountrequesttype.DisplayName AS Type, 
      cardaccountrequest.CardAccountNumber AS CardAccountNumber, 
      cardaccountrequest.CardAccountName AS CardAccountName, 
      cardaccountrequest.RequestReason AS Reason, 
      cardaccountrequest.AccountType AS AccountType, 
      
         (
            SELECT TOP (1) status.Description
            FROM [${dbxschemaname}].status
            WHERE (cardaccountrequest.Status_id = status.id)
         ) AS Status, 
      cardaccountrequest.Customer_id AS CustomerId, 
      customercommunication.Value AS CommunicationValue, 
      communicationtype.Description AS DeliveryMode, 
      
         address.addressLine2
          + 
         address.addressLine1
          + 
         address.addressLine3
          + 
         address.addressLine1
          + 
         city.Name
          + 
         address.addressLine1
          + 
         region.Name
          + 
         address.addressLine1
          + 
         country.Name
          + 
         address.addressLine1
          + 
         address.zipCode AS Address
   FROM ((((((([${dbxschemaname}].cardaccountrequest 
      LEFT JOIN [${dbxschemaname}].cardaccountrequesttype 
      ON ((cardaccountrequest.RequestType_id = cardaccountrequesttype.id))) 
      LEFT JOIN [${dbxschemaname}].address 
      ON ((cardaccountrequest.Address_id = address.id))) 
      LEFT JOIN [${dbxschemaname}].city 
      ON ((address.City_id = city.id))) 
      LEFT JOIN [${dbxschemaname}].region 
      ON ((address.Region_id = region.id))) 
      LEFT JOIN [${dbxschemaname}].country 
      ON ((city.Country_id = country.id))) 
      LEFT JOIN [${dbxschemaname}].customercommunication 
      ON ((cardaccountrequest.Communication_id = customercommunication.id))) 
      LEFT JOIN [${dbxschemaname}].communicationtype 
      ON ((customercommunication.Type_id = communicationtype.id)))
GO
/****** Object:  View [${dbxschemaname}].[channel_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[channel_view] (
   [channel_id], 
   [channel_status_id], 
   [channeltext_LanguageCode], 
   [channeltext_Description], 
   [channeltext_createdby], 
   [channeltext_modifiedby], 
   [channeltext_createdts], 
   [channeltext_lastmodifiedts], 
   [channeltext_synctimestamp], 
   [channeltext_softdeleteflag])
AS 
   SELECT 
      channel.id AS channel_id, 
      channel.status_id AS channel_status_id, 
      channeltext.LanguageCode AS channeltext_LanguageCode, 
      channeltext.Description AS channeltext_Description, 
      channeltext.createdby AS channeltext_createdby, 
      channeltext.modifiedby AS channeltext_modifiedby, 
      channeltext.createdts AS channeltext_createdts, 
      channeltext.lastmodifiedts AS channeltext_lastmodifiedts, 
      channeltext.synctimestamp AS channeltext_synctimestamp, 
      channeltext.softdeleteflag AS channeltext_softdeleteflag
   FROM ([${dbxschemaname}].channeltext 
      INNER JOIN [${dbxschemaname}].channel 
      ON ((channeltext.channelID = channel.id)))
GO
/****** Object:  View [${dbxschemaname}].[communicationtemplate_channel_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[communicationtemplate_channel_view] (
   [communicationtemplate_id], 
   [communicationtemplate_Name], 
   [communicationtemplate_Text], 
   [communicationtemplate_AlertSubTypeId], 
   [communicationtemplate_LanguageCode], 
   [communicationtemplate_ChannelID], 
   [communicationtemplate_Status_id], 
   [communicationtemplate_Subject], 
   [communicationtemplate_SenderName], 
   [communicationtemplate_SenderEmail], 
   [channeltext_Description], 
   [channeltext_softdeleteflag], 
   [communicationtemplate_softdeleteflag])
AS 
   SELECT 
      communicationtemplate.Id AS communicationtemplate_id, 
      communicationtemplate.Name AS communicationtemplate_Name, 
      communicationtemplate.Text AS communicationtemplate_Text, 
      communicationtemplate.AlertSubTypeId AS communicationtemplate_AlertSubTypeId, 
      communicationtemplate.LanguageCode AS communicationtemplate_LanguageCode, 
      communicationtemplate.ChannelID AS communicationtemplate_ChannelID, 
      communicationtemplate.Status_id AS communicationtemplate_Status_id, 
      communicationtemplate.Subject AS communicationtemplate_Subject, 
      communicationtemplate.SenderName AS communicationtemplate_SenderName, 
      communicationtemplate.SenderEmail AS communicationtemplate_SenderEmail, 
      channeltext.Description AS channeltext_Description, 
      channeltext.softdeleteflag AS channeltext_softdeleteflag, 
      communicationtemplate.softdeleteflag AS communicationtemplate_softdeleteflag
   FROM ([${dbxschemaname}].communicationtemplate 
      INNER JOIN [${dbxschemaname}].channeltext 
      ON (((communicationtemplate.ChannelID = channeltext.channelID) AND (communicationtemplate.LanguageCode = channeltext.LanguageCode))))
GO
/****** Object:  View [${dbxschemaname}].[composite_actions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[composite_actions_view] (
   [id], 
   [Name], 
   [Description], 
   [Action_id], 
   [Feature_id], 
   [Permission_id], 
   [isEnabled], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag])
AS 
   SELECT 
      compositeaction.id AS id, 
      CASE 
         WHEN (CASE 
            WHEN compositeaction.Action_id IS NULL THEN 1
            ELSE 0
         END <> 0) THEN compositeaction.Name
         ELSE featureaction.name
      END AS Name, 
      CASE 
         WHEN (CASE 
            WHEN compositeaction.Action_id IS NULL THEN 1
            ELSE 0
         END <> 0) THEN compositeaction.Description
         ELSE featureaction.description
      END AS Description, 
      compositeaction.Action_id AS Action_id, 
      compositeaction.Feature_id AS Feature_id, 
      compositeaction.Permission_id AS Permission_id, 
      compositeaction.isEnabled AS isEnabled, 
      compositeaction.createdby AS createdby, 
      compositeaction.modifiedby AS modifiedby, 
      compositeaction.createdts AS createdts, 
      compositeaction.lastmodifiedts AS lastmodifiedts, 
      compositeaction.synctimestamp AS synctimestamp, 
      compositeaction.softdeleteflag AS softdeleteflag
   FROM ([${dbxschemaname}].compositeaction 
      LEFT JOIN [${dbxschemaname}].featureaction 
      ON ((featureaction.id = compositeaction.Action_id)))
GO
/****** Object:  View [${dbxschemaname}].[composite_permissions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[composite_permissions_view] (
   [id], 
   [Name], 
   [Description], 
   [Service_id], 
   [Permission_id], 
   [isEnabled], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag])
AS 
   SELECT 
      compositepermission.id AS id, 
      CASE 
         WHEN (CASE 
            WHEN compositepermission.Entitlement_id IS NULL THEN 1
            ELSE 0
         END <> 0) THEN compositepermission.Name
         ELSE service.Name
      END AS Name, 
      CASE 
         WHEN (CASE 
            WHEN compositepermission.Entitlement_id IS NULL THEN 1
            ELSE 0
         END <> 0) THEN compositepermission.Description
         ELSE service.Description
      END AS Description, 
      compositepermission.Entitlement_id AS Service_id, 
      compositepermission.Permission_id AS Permission_id, 
      compositepermission.isEnabled AS isEnabled, 
      compositepermission.createdby AS createdby, 
      compositepermission.modifiedby AS modifiedby, 
      compositepermission.createdts AS createdts, 
      compositepermission.lastmodifiedts AS lastmodifiedts, 
      compositepermission.synctimestamp AS synctimestamp, 
      compositepermission.softdeleteflag AS softdeleteflag
   FROM ([${dbxschemaname}].compositepermission 
      LEFT JOIN [${dbxschemaname}].service 
      ON ((service.id = compositepermission.Entitlement_id)))
GO
/****** Object:  View [${dbxschemaname}].[customer_alertcategory_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
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
               configurationbundles.app_id AS app_id
         FROM [${dbxschemaname}].configurations 
            LEFT JOIN [${dbxschemaname}].configurationbundles ON configurationbundles.bundle_id = configurations.bundle_id;
GO
/****** Object:  View [${dbxschemaname}].[customer_alertcategory_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_alertcategory_view] (
   [customer_id], 
   [accountID], 
   [preference_Status_id], 
   [alertcategory_id], 
   [alertcategory_status_id], 
   [alertcategory_accountLevel], 
   [alertcategory_DisplaySequence], 
   [alertcategory_softdeleteflag], 
   [alertcategorytext_LanguageCode], 
   [alertcategory_Name], 
   [alertcategorytext_DisplayName], 
   [alertcategorytext_Description], 
   [alertcategorytext_createdby], 
   [alertcategorytext_modifiedby], 
   [alertcategorytext_createdts], 
   [alertcategorytext_lastmodifiedts], 
   [alertcategorytext_synctimestamp], 
   [alertcategorytext_softdeleteflag])
AS 
   SELECT 
      customeralertswitch.Customer_id AS customer_id, 
      customeralertswitch.AccountID AS accountID, 
      customeralertswitch.Status_id AS preference_Status_id, 
      dbxalertcategory.id AS alertcategory_id, 
      dbxalertcategory.status_id AS alertcategory_status_id, 
      dbxalertcategory.accountLevel AS alertcategory_accountLevel, 
      dbxalertcategory.DisplaySequence AS alertcategory_DisplaySequence, 
      dbxalertcategory.softdeleteflag AS alertcategory_softdeleteflag, 
      dbxalertcategorytext.LanguageCode AS alertcategorytext_LanguageCode, 
      dbxalertcategory.Name AS alertcategory_Name, 
      dbxalertcategorytext.DisplayName AS alertcategorytext_DisplayName, 
      dbxalertcategorytext.Description AS alertcategorytext_Description, 
      dbxalertcategorytext.createdby AS alertcategorytext_createdby, 
      dbxalertcategorytext.modifiedby AS alertcategorytext_modifiedby, 
      dbxalertcategorytext.createdts AS alertcategorytext_createdts, 
      dbxalertcategorytext.lastmodifiedts AS alertcategorytext_lastmodifiedts, 
      dbxalertcategorytext.synctimestamp AS alertcategorytext_synctimestamp, 
      dbxalertcategorytext.softdeleteflag AS alertcategorytext_softdeleteflag
   FROM (([${dbxschemaname}].customeralertswitch 
      LEFT JOIN [${dbxschemaname}].dbxalertcategory 
      ON ((customeralertswitch.AlertCategoryId = dbxalertcategory.id))) 
      LEFT JOIN [${dbxschemaname}].dbxalertcategorytext 
      ON ((customeralertswitch.AlertCategoryId = dbxalertcategorytext.AlertCategoryId)))
GO
/****** Object:  View [${dbxschemaname}].[customer_communication_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_communication_view] (
   [customer_id], 
   [customer_FirstName], 
   [customer_MiddleName], 
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
   [customercommunication_id], 
   [customercommunication_Type_id], 
   [customercommunication], 
   [isTypeBusiness], 
   [customercommunication_Value], 
   [customercommunication_Extension], 
   [customercommunication_Description], 
   [customercommunication_createdby], 
   [customercommunication_modifiedby], 
   [customercommunication_createdts], 
   [customercommunication_lastmodifiedts], 
   [customercommunication_synctimestamp], 
   [customercommunication_softdeleteflag])
AS 
   SELECT 
      customer.id AS customer_id, 
      customer.FirstName AS customer_FirstName, 
      customer.MiddleName AS customer_MiddleName, 
      customer.LastName AS customer_LastName, 
      customer.UserName AS customer_Username, 
      customer.Salutation AS customer_Salutation, 
      customer.Gender AS customer_Gender, 
      customer.DateOfBirth AS customer_DateOfBirth, 
      customer.Status_id AS customer_Status_id, 
      customer.Ssn AS customer_Ssn, 
      customer.MaritalStatus_id AS customer_MaritalStatus_id, 
      customer.SpouseName AS customer_SpouseName, 
      customer.EmployementStatus_id AS customer_EmployementStatus_id, 
      customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb, 
      customer.IsStaffMember AS customer_IsStaffMember, 
      customer.Location_id AS customer_Location_id, 
      customer.PreferredContactMethod AS customer_PreferredContactMethod, 
      customer.PreferredContactTime AS customer_PreferredContactTime, 
      customercommunication.id AS customercommunication_id, 
      customercommunication.Type_id AS customercommunication_Type_id, 
      customercommunication.isPrimary AS customercommunication, 
      customercommunication.isTypeBusiness AS isTypeBusiness, 
      customercommunication.Value AS customercommunication_Value, 
      customercommunication.Extension AS customercommunication_Extension, 
      customercommunication.Description AS customercommunication_Description, 
      customercommunication.createdby AS customercommunication_createdby, 
      customercommunication.modifiedby AS customercommunication_modifiedby, 
      customercommunication.createdts AS customercommunication_createdts, 
      customercommunication.lastmodifiedts AS customercommunication_lastmodifiedts, 
      customercommunication.synctimestamp AS customercommunication_synctimestamp, 
      customercommunication.softdeleteflag AS customercommunication_softdeleteflag
   FROM ([${dbxschemaname}].customer 
      INNER JOIN [${dbxschemaname}].customercommunication 
      ON ((customer.id = customercommunication.Customer_id)))
GO
/****** Object:  View [${dbxschemaname}].[customer_device_information_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_device_information_view] (
   [Device_id], 
   [Customer_id], 
   [Customer_username], 
   [DeviceName], 
   [LastLoginTime], 
   [LastUsedIp], 
   [Status_id], 
   [Status_name], 
   [OperatingSystem], 
   [Channel_id], 
   [Channel_Description], 
   [EnrollmentDate], 
   [createdby], 
   [modifiedby], 
   [Registered_Date], 
   [lastmodifiedts])
AS 
   SELECT 
      customerdevice.id AS Device_id, 
      customerdevice.Customer_id AS Customer_id, 
      customer.UserName AS Customer_username, 
      customerdevice.DeviceName AS DeviceName, 
      customerdevice.LastLoginTime AS LastLoginTime, 
      customerdevice.LastUsedIp AS LastUsedIp, 
      customerdevice.Status_id AS Status_id, 
      status.Description AS Status_name, 
      customerdevice.OperatingSystem AS OperatingSystem, 
      customerdevice.Channel_id AS Channel_id, 
      servicechannel.Description AS Channel_Description, 
      customerdevice.EnrollmentDate AS EnrollmentDate, 
      customerdevice.createdby AS createdby, 
      customerdevice.modifiedby AS modifiedby, 
      customerdevice.createdts AS Registered_Date, 
      customerdevice.lastmodifiedts AS lastmodifiedts
   FROM ((([${dbxschemaname}].customerdevice 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customer.id = customerdevice.Customer_id))) 
      INNER JOIN [${dbxschemaname}].status 
      ON ((customerdevice.Status_id = status.id))) 
      INNER JOIN [${dbxschemaname}].servicechannel 
      ON ((servicechannel.id = customerdevice.Channel_id)))
GO
/****** Object:  View [${dbxschemaname}].[customer_indirect_permissions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_indirect_permissions_view] (
   [Customer_id], 
   [Group_id], 
   [Group_name], 
   [Group_desc], 
   [Service_id], 
   [Service_Name], 
   [Service_Description], 
   [Service_Status_id], 
   [Service_DisplayName], 
   [Service_DisplayDescription])
AS 
   SELECT 
      customergroup.Customer_id AS Customer_id, 
      membergroup.id AS Group_id, 
      membergroup.Name AS Group_name, 
      membergroup.Description AS Group_desc, 
      service.id AS Service_id, 
      service.Name AS Service_Name, 
      service.Description AS Service_Description, 
      service.Status_id AS Service_Status_id, 
      service.DisplayName AS Service_DisplayName, 
      service.DisplayDescription AS Service_DisplayDescription
   FROM ((([${dbxschemaname}].customergroup 
      INNER JOIN [${dbxschemaname}].membergroup 
      ON ((customergroup.Group_id = membergroup.id))) 
      LEFT JOIN [${dbxschemaname}].groupentitlement 
      ON ((customergroup.Group_id = groupentitlement.Group_id))) 
      LEFT JOIN [${dbxschemaname}].service 
      ON ((groupentitlement.Service_id = service.id)))
GO
/****** Object:  View [${dbxschemaname}].[customer_request_category_count_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_request_category_count_view] ([requestcategory_id], [requestcategory_Name], [request_count])
AS 
   /*
   *   SSMA warning messages:
   *   M2SS0104: Non aggregated column NAME is aggregated with Min(..) in Select, Orderby and Having clauses.
   */

   SELECT TOP (9223372036854775807) requestcategory.id AS requestcategory_id, min(requestcategory.Name) AS requestcategory_Name, count_big(customerrequest.id) AS request_count
   FROM ([${dbxschemaname}].requestcategory 
      LEFT JOIN [${dbxschemaname}].customerrequest 
      ON (((customerrequest.RequestCategory_id = requestcategory.id) AND (customerrequest.Status_id = 'SID_OPEN'))))
   GROUP BY requestcategory.id
      ORDER BY requestcategory.id
GO
/****** Object:  View [${dbxschemaname}].[customer_request_detailed_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_request_csr_count_view] (
   [customerrequest_Status_id], 
   [status_Description], 
   [customerrequest_assignedTo], 
   [request_count])
AS 
   SELECT customerrequest.Status_id AS customerrequest_Status_id, 
      (
         /*
         *   SSMA warning messages:
         *   M2SS0104: Non aggregated column DESCRIPTION is aggregated with Min(..) in Select, Orderby and Having clauses.
         *   M2SS0104: Non aggregated column ID is aggregated with Min(..) in Select, Orderby and Having clauses.
         */

         SELECT TOP (1) (status.Description) AS Description
         FROM [${dbxschemaname}].status
         WHERE status.id = customerrequest.Status_id
      ) AS status_Description, customerrequest.AssignedTo AS customerrequest_assignedTo, count_big(customerrequest.id) AS request_count
   FROM [${dbxschemaname}].customerrequest
   GROUP BY customerrequest.AssignedTo, customerrequest.Status_id
      
GO
/****** Object:  View [${dbxschemaname}].[customer_request_detailed_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_request_detailed_view] (
   [customerrequest_id], 
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
      ON ((messageattachment.Media_id = media.id)))
GO
/****** Object:  View [${dbxschemaname}].[customer_request_status_count_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customer_request_status_count_view] ([Count], [Status_id])
AS 
   SELECT tbl$1.Count, tbl$1.Status_id
   FROM 
      (
         /*
         *   SSMA warning messages:
         *   M2SS0219: Converted operator may not work exactly the same as in MySQL
         */

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
      ORDER BY s2.id
GO
/****** Object:  View [${dbxschemaname}].[customeraddress_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customeraccountransactionview] (
   [User_id], 
   [transactionId], 
   [transactiontype], 
   [ExpenseCategory_id], 
   [Bill_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [convertedAmount], 
   [transactionCurrency], 
   [baseCurrency], 
   [Status_id], 
   [statusDesc], 
   [isScheduled], 
   [category], 
   [billCategory], 
   [ExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [createdDate], 
   [cashlessEmail], 
   [cashlessMode], 
   [cashlessOTP], 
   [cashlessOTPValidDate], 
   [cashlessPersonName], 
   [cashlessPhone], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [frequencyEndDate], 
   [frequencyStartDate], 
   [hasDepositImage], 
   [payeeId], 
   [payeeName], 
   [p2pContact], 
   [personId], 
   [recurrenceDesc], 
   [numberOfRecurrences], 
   [scheduledDate], 
   [transactionComments], 
   [transactionsNotes], 
   [transDescription], 
   [transactionDate], 
   [postedDate], 
   [frontImage1], 
   [frontImage2], 
   [backImage1], 
   [backImage2], 
   [checkDesc], 
   [checkNumber1], 
   [checkNumber2], 
   [checkNumber], 
   [checkReason], 
   [requestValidity], 
   [checkDateOfIssue], 
   [bankName1], 
   [bankName2], 
   [withdrawlAmount1], 
   [withdrawlAmount2], 
   [cashAmount], 
   [payeeCurrency], 
   [fee], 
   [feePaidByReceipent], 
   [feeCurrency], 
   [isDisputed], 
   [disputeReason], 
   [disputeDescription], 
   [disputeDate], 
   [disputeStatus], 
   [description], 
   [statementReference], 
   [transCreditDebitIndicator], 
   [bookingDateTime], 
   [valueDateTime], 
   [transactionInformation], 
   [addressLine], 
   [transactionAmount], 
   [chargeAmount], 
   [chargeCurrency], 
   [sourceCurrency], 
   [targetCurrency], 
   [unitCurrency], 
   [exchangeRate], 
   [contractIdentification], 
   [quotationDate], 
   [instructedAmount], 
   [instructedCurrency], 
   [transactionCode], 
   [transactionSubCode], 
   [proprietaryTransactionCode], 
   [proprietaryTransactionIssuer], 
   [balanceCreditDebitIndicator], 
   [balanceType], 
   [balanceAmount], 
   [balanceCurrency], 
   [merchantName], 
   [merchantCategoryCode], 
   [creditorAgentSchemeName], 
   [creditorAgentIdentification], 
   [creditorAgentName], 
   [creditorAgentaddressType], 
   [creditorAgentDepartment], 
   [creditorAgentSubDepartment], 
   [creditorAgentStreetName], 
   [creditorAgentBuildingNumber], 
   [creditorAgentPostCode], 
   [creditorAgentTownName], 
   [creditorAgentCountrySubDivision], 
   [creditorAgentCountry], 
   [creditorAgentAddressLine], 
   [creditorAccountSchemeName], 
   [creditorAccountIdentification], 
   [creditorAccountName], 
   [creditorAccountSeconIdentification], 
   [debtorAgentSchemeName], 
   [debtorAgentIdentification], 
   [debtorAgentName], 
   [debtorAgentAddressType], 
   [debtorAgentDepartment], 
   [debtorAgentSubDepartment], 
   [debtorAgentStreetName], 
   [debtorAgentBuildingNumber], 
   [dedtorAgentPostCode], 
   [debtorAgentTownName], 
   [debtorAgentCountrySubDivision], 
   [debtorAgentCountry], 
   [debtorAgentAddressLine], 
   [debtorAccountSchemeName], 
   [debtorAccountIdentification], 
   [debtorAccountName], 
   [debtorAccountSeconIdentification], 
   [cardInstrumentSchemeName], 
   [cardInstrumentAuthorisationType], 
   [cardInstrumentName], 
   [cardInstrumentIdentification], 
   [FirstPaymentDateTime], 
   [NextPaymentDateTime], 
   [FinalPaymentDateTime], 
   [StandingOrderStatusCode], 
   [FP_Amount], 
   [FP_Currency], 
   [NP_Amount], 
   [NP_Currency], 
   [FPA_Amount], 
   [FPA_Currency], 
   [IBAN], 
   [sortCode], 
   [beneficiaryName], 
   [bankName], 
   [swiftCode])
AS 
   SELECT DISTINCT 
      [${dbxschemaname}].customeraccounts.Customer_id AS User_id, 
      [${dbxschemaname}].[transaction].Id AS transactionId, 
      [${dbxschemaname}].[transaction].Type_id AS transactiontype, 
      [${dbxschemaname}].[transaction].ExpenseCategory_id AS ExpenseCategory_id, 
      [${dbxschemaname}].[transaction].billid AS Bill_id, 
      [${dbxschemaname}].[transaction].Reference_id AS Reference_id, 
      [${dbxschemaname}].[transaction].fromAccountNumber AS fromAccountNumber, 
      [${dbxschemaname}].[transaction].fromAccountBalance AS fromAccountBalance, 
      [${dbxschemaname}].[transaction].toAccountNumber AS toAccountNumber, 
      [${dbxschemaname}].[transaction].toAccountBalance AS toAccountBalance, 
      [${dbxschemaname}].[transaction].amount AS amount, 
      [${dbxschemaname}].[transaction].convertedAmount AS convertedAmount, 
      [${dbxschemaname}].[transaction].transactionCurrency AS transactionCurrency, 
      [${dbxschemaname}].[transaction].baseCurrency AS baseCurrency, 
      [${dbxschemaname}].[transaction].Status_id AS Status_id, 
      [${dbxschemaname}].[transaction].statusDesc AS statusDesc, 
      [${dbxschemaname}].[transaction].isScheduled AS isScheduled, 
      [${dbxschemaname}].[transaction].category AS category, 
      [${dbxschemaname}].[transaction].billCategory AS billCategory, 
      [${dbxschemaname}].[transaction].toExternalAccountNumber AS ExternalAccountNumber, 
      [${dbxschemaname}].[transaction].Person_Id AS Person_Id, 
      [${dbxschemaname}].[transaction].frequencyType AS frequencyType, 
      [${dbxschemaname}].[transaction].createdDate AS createdDate, 
      [${dbxschemaname}].[transaction].cashlessEmail AS cashlessEmail, 
      [${dbxschemaname}].[transaction].cashlessMode AS cashlessMode, 
      [${dbxschemaname}].[transaction].cashlessOTP AS cashlessOTP, 
      [${dbxschemaname}].[transaction].cashlessOTPValidDate AS cashlessOTPValidDate, 
      [${dbxschemaname}].[transaction].cashlessPersonName AS cashlessPersonName, 
      [${dbxschemaname}].[transaction].cashlessPhone AS cashlessPhone, 
      [${dbxschemaname}].[transaction].cashlessSecurityCode AS cashlessSecurityCode, 
      [${dbxschemaname}].[transaction].cashWithdrawalTransactionStatus AS cashWithdrawalTransactionStatus, 
      [${dbxschemaname}].[transaction].frequencyEndDate AS frequencyEndDate, 
      [${dbxschemaname}].[transaction].frequencyStartDate AS frequencyStartDate, 
      [${dbxschemaname}].[transaction].hasDepositImage AS hasDepositImage, 
      [${dbxschemaname}].[transaction].Payee_id AS payeeId, 
      [${dbxschemaname}].[transaction].payeeName AS payeeName, 
      [${dbxschemaname}].[transaction].p2pContact AS p2pContact, 
      [${dbxschemaname}].[transaction].Person_Id AS personId, 
      [${dbxschemaname}].[transaction].recurrenceDesc AS recurrenceDesc, 
      [${dbxschemaname}].[transaction].numberOfRecurrences AS numberOfRecurrences, 
      [${dbxschemaname}].[transaction].scheduledDate AS scheduledDate, 
      [${dbxschemaname}].[transaction].transactionComments AS transactionComments, 
      [${dbxschemaname}].[transaction].notes AS transactionsNotes, 
      [${dbxschemaname}].[transaction].description AS transDescription, 
      [${dbxschemaname}].[transaction].transactionDate AS transactionDate, 
      [${dbxschemaname}].[transaction].postedDate AS postedDate, 
      [${dbxschemaname}].[transaction].frontImage1 AS frontImage1, 
      [${dbxschemaname}].[transaction].frontImage2 AS frontImage2, 
      [${dbxschemaname}].[transaction].backImage1 AS backImage1, 
      [${dbxschemaname}].[transaction].backImage2 AS backImage2, 
      [${dbxschemaname}].[transaction].checkDesc AS checkDesc, 
      [${dbxschemaname}].[transaction].checkNumber1 AS checkNumber1, 
      [${dbxschemaname}].[transaction].checkNumber2 AS checkNumber2, 
      [${dbxschemaname}].[transaction].checkNumber AS checkNumber, 
      [${dbxschemaname}].[transaction].checkReason AS checkReason, 
      [${dbxschemaname}].[transaction].requestValidity AS requestValidity, 
      [${dbxschemaname}].[transaction].checkDateOfIssue AS checkDateOfIssue, 
      [${dbxschemaname}].[transaction].bankName1 AS bankName1, 
      [${dbxschemaname}].[transaction].bankName2 AS bankName2, 
      [${dbxschemaname}].[transaction].withdrawlAmount1 AS withdrawlAmount1, 
      [${dbxschemaname}].[transaction].withdrawlAmount2 AS withdrawlAmount2, 
      [${dbxschemaname}].[transaction].cashAmount AS cashAmount, 
      [${dbxschemaname}].[transaction].payeeCurrency AS payeeCurrency, 
      [${dbxschemaname}].[transaction].fee AS fee, 
      [${dbxschemaname}].[transaction].feePaidByReceipent AS feePaidByReceipent, 
      [${dbxschemaname}].[transaction].feeCurrency AS feeCurrency, 
      [${dbxschemaname}].[transaction].isDisputed AS isDisputed, 
      [${dbxschemaname}].[transaction].disputeReason AS disputeReason, 
      [${dbxschemaname}].[transaction].disputeDescription AS disputeDescription, 
      [${dbxschemaname}].[transaction].disputeDate AS disputeDate, 
      [${dbxschemaname}].[transaction].disputeStatus AS disputeStatus, 
      [${dbxschemaname}].transactiontype.description AS description, 
      [${dbxschemaname}].[transaction].statementReference AS statementReference, 
      [${dbxschemaname}].[transaction].transCreditDebitIndicator AS transCreditDebitIndicator, 
      [${dbxschemaname}].[transaction].bookingDateTime AS bookingDateTime, 
      [${dbxschemaname}].[transaction].valueDateTime AS valueDateTime, 
      [${dbxschemaname}].[transaction].transactionInformation AS transactionInformation, 
      [${dbxschemaname}].[transaction].addressLine AS addressLine, 
      [${dbxschemaname}].[transaction].transactionAmount AS transactionAmount, 
      [${dbxschemaname}].[transaction].chargeAmount AS chargeAmount, 
      [${dbxschemaname}].[transaction].chargeCurrency AS chargeCurrency, 
      [${dbxschemaname}].[transaction].sourceCurrency AS sourceCurrency, 
      [${dbxschemaname}].[transaction].targetCurrency AS targetCurrency, 
      [${dbxschemaname}].[transaction].unitCurrency AS unitCurrency, 
      [${dbxschemaname}].[transaction].exchangeRate AS exchangeRate, 
      [${dbxschemaname}].[transaction].contractIdentification AS contractIdentification, 
      [${dbxschemaname}].[transaction].quotationDate AS quotationDate, 
      [${dbxschemaname}].[transaction].instructedAmount AS instructedAmount, 
      [${dbxschemaname}].[transaction].instructedCurrency AS instructedCurrency, 
      [${dbxschemaname}].[transaction].transactionCode AS transactionCode, 
      [${dbxschemaname}].[transaction].transactionSubCode AS transactionSubCode, 
      [${dbxschemaname}].[transaction].proprietaryTransactionCode AS proprietaryTransactionCode, 
      [${dbxschemaname}].[transaction].proprietaryTransactionIssuer AS proprietaryTransactionIssuer, 
      [${dbxschemaname}].[transaction].balanceCreditDebitIndicator AS balanceCreditDebitIndicator, 
      [${dbxschemaname}].[transaction].balanceType AS balanceType, 
      [${dbxschemaname}].[transaction].balanceAmount AS balanceAmount, 
      [${dbxschemaname}].[transaction].balanceCurrency AS balanceCurrency, 
      [${dbxschemaname}].[transaction].merchantName AS merchantName, 
      [${dbxschemaname}].[transaction].merchantCategoryCode AS merchantCategoryCode, 
      [${dbxschemaname}].[transaction].creditorAgentSchemeName AS creditorAgentSchemeName, 
      [${dbxschemaname}].[transaction].creditorAgentIdentification AS creditorAgentIdentification, 
      [${dbxschemaname}].[transaction].creditorAgentName AS creditorAgentName, 
      [${dbxschemaname}].[transaction].creditorAgentaddressType AS creditorAgentaddressType, 
      [${dbxschemaname}].[transaction].creditorAgentDepartment AS creditorAgentDepartment, 
      [${dbxschemaname}].[transaction].creditorAgentSubDepartment AS creditorAgentSubDepartment, 
      [${dbxschemaname}].[transaction].creditorAgentStreetName AS creditorAgentStreetName, 
      [${dbxschemaname}].[transaction].creditorAgentBuildingNumber AS creditorAgentBuildingNumber, 
      [${dbxschemaname}].[transaction].creditorAgentPostCode AS creditorAgentPostCode, 
      [${dbxschemaname}].[transaction].creditorAgentTownName AS creditorAgentTownName, 
      [${dbxschemaname}].[transaction].creditorAgentCountrySubDivision AS creditorAgentCountrySubDivision, 
      [${dbxschemaname}].[transaction].creditorAgentCountry AS creditorAgentCountry, 
      [${dbxschemaname}].[transaction].creditorAgentAddressLine AS creditorAgentAddressLine, 
      [${dbxschemaname}].[transaction].creditorAccountSchemeName AS creditorAccountSchemeName, 
      [${dbxschemaname}].[transaction].creditorAccountIdentification AS creditorAccountIdentification, 
      [${dbxschemaname}].[transaction].creditorAccountName AS creditorAccountName, 
      [${dbxschemaname}].[transaction].creditorAccountSeconIdentification AS creditorAccountSeconIdentification, 
      [${dbxschemaname}].[transaction].debtorAgentSchemeName AS debtorAgentSchemeName, 
      [${dbxschemaname}].[transaction].debtorAgentIdentification AS debtorAgentIdentification, 
      [${dbxschemaname}].[transaction].debtorAgentName AS debtorAgentName, 
      [${dbxschemaname}].[transaction].debtorAgentAddressType AS debtorAgentAddressType, 
      [${dbxschemaname}].[transaction].debtorAgentDepartment AS debtorAgentDepartment, 
      [${dbxschemaname}].[transaction].debtorAgentSubDepartment AS debtorAgentSubDepartment, 
      [${dbxschemaname}].[transaction].debtorAgentStreetName AS debtorAgentStreetName, 
      [${dbxschemaname}].[transaction].debtorAgentBuildingNumber AS debtorAgentBuildingNumber, 
      [${dbxschemaname}].[transaction].dedtorAgentPostCode AS dedtorAgentPostCode, 
      [${dbxschemaname}].[transaction].debtorAgentTownName AS debtorAgentTownName, 
      [${dbxschemaname}].[transaction].debtorAgentCountrySubDivision AS debtorAgentCountrySubDivision, 
      [${dbxschemaname}].[transaction].debtorAgentCountry AS debtorAgentCountry, 
      [${dbxschemaname}].[transaction].debtorAgentAddressLine AS debtorAgentAddressLine, 
      [${dbxschemaname}].[transaction].debtorAccountSchemeName AS debtorAccountSchemeName, 
      [${dbxschemaname}].[transaction].debtorAccountIdentification AS debtorAccountIdentification, 
      [${dbxschemaname}].[transaction].debtorAccountName AS debtorAccountName, 
      [${dbxschemaname}].[transaction].debtorAccountSeconIdentification AS debtorAccountSeconIdentification, 
      [${dbxschemaname}].[transaction].cardInstrumentSchemeName AS cardInstrumentSchemeName, 
      [${dbxschemaname}].[transaction].cardInstrumentAuthorisationType AS cardInstrumentAuthorisationType, 
      [${dbxschemaname}].[transaction].cardInstrumentName AS cardInstrumentName, 
      [${dbxschemaname}].[transaction].cardInstrumentIdentification AS cardInstrumentIdentification, 
      [${dbxschemaname}].[transaction].FirstPaymentDateTime AS FirstPaymentDateTime, 
      [${dbxschemaname}].[transaction].NextPaymentDateTime AS NextPaymentDateTime, 
      [${dbxschemaname}].[transaction].FinalPaymentDateTime AS FinalPaymentDateTime, 
      [${dbxschemaname}].[transaction].StandingOrderStatusCode AS StandingOrderStatusCode, 
      [${dbxschemaname}].[transaction].FP_Amount AS FP_Amount, 
      [${dbxschemaname}].[transaction].FP_Currency AS FP_Currency, 
      [${dbxschemaname}].[transaction].NP_Amount AS NP_Amount, 
      [${dbxschemaname}].[transaction].NP_Currency AS NP_Currency, 
      [${dbxschemaname}].[transaction].FPA_Amount AS FPA_Amount, 
      [${dbxschemaname}].[transaction].FPA_Currency AS FPA_Currency, 
      [${dbxschemaname}].[transaction].IBAN AS IBAN, 
      [${dbxschemaname}].[transaction].sortCode AS sortCode, 
      [${dbxschemaname}].[transaction].beneficiaryName AS beneficiaryName, 
      [${dbxschemaname}].[transaction].bankName AS bankName, 
      [${dbxschemaname}].[transaction].swiftCode AS swiftCode
   FROM (([${dbxschemaname}].customeraccounts 
      CROSS JOIN [${dbxschemaname}].[transaction]) 
      CROSS JOIN [${dbxschemaname}].transactiontype)
   WHERE (
      ((CAST(customeraccounts.Account_id AS float(53)) = [${dbxschemaname}].[transaction].fromAccountNumber) OR (CAST(customeraccounts.Account_id AS float(53)) = [${dbxschemaname}].[transaction].toAccountNumber)) AND 
      (customeraccounts.Customer_id IS NOT NULL) AND 
      ([${dbxschemaname}].[transaction].Type_id = transactiontype.Id))
      

GO
/****** Object:  View [${dbxschemaname}].[customeraccountsview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[customeraccountsview] (
   [Membership_id],
   [MembershipName],
   [Taxid], 
   [Customer_id], 
   [User_id], 
   [Account_id], 
   [isBusinessAccount], 
   [Type_id], 
   [userName], 
   [currencyCode], 
   [accountHolder], 
   [error], 
   [Address], 
   [Scheme], 
   [number], 
   [availableBalance], 
   [currentBalance], 
   [interestRate], 
   [availableCredit], 
   [minimumDue], 
   [dueDate], 
   [firstPaymentDate], 
   [closingDate], 
   [paymentTerm], 
   [openingDate], 
   [maturityDate], 
   [dividendLastPaidAmount], 
   [dividendLastPaidDate], 
   [dividendPaidYTD], 
   [dividendRate], 
   [dividendYTD], 
   [eStatementEnable], 
   [IsOrganizationAccount],
   [favouriteStatus], 
   [statusDesc], 
   [nickName], 
   [originalAmount], 
   [outstandingBalance], 
   [paymentDue], 
   [paymentMethod], 
   [swiftCode], 
   [totalCreditMonths], 
   [totalDebitsMonth], 
   [routingNumber], 
   [supportBillPay], 
   [supportCardlessCash], 
   [supportTransferFrom], 
   [supportTransferTo], 
   [supportDeposit], 
   [unpaidInterest], 
   [previousYearsDividends], 
   [principalBalance], 
   [principalValue], 
   [regularPaymentAmount], 
   [phoneId], 
   [lastDividendPaidDate], 
   [lastDividendPaidAmount], 
   [lastPaymentAmount], 
   [lastPaymentDate], 
   [lastStatementBalance], 
   [lateFeesDue], 
   [maturityAmount], 
   [maturityOption], 
   [payoffAmount], 
   [payOffCharge], 
   [pendingDeposit], 
   [pendingWithdrawal], 
   [jointHolders], 
   [isPFM], 
   [interestPaidYTD], 
   [interestPaidPreviousYTD], 
   [interestPaidLastYear], 
   [interestEarned], 
   [currentAmountDue], 
   [creditLimit], 
   [creditCardNumber], 
   [bsbNum], 
   [bondInterestLastYear], 
   [bondInterest], 
   [availablePoints], 
   [accountName], 
   [email], 
   [IBAN], 
   [adminProductId], 
   [UpdatedBy], 
   [LastUpdated], 
   [ActualUpdatedBY], 
   [bankname], 
   [accountPreference], 
   [transactionLimit], 
   [transferLimit], 
   [rates], 
   [termsAndConditions], 
   [typeDescription], 
   [supportChecks], 
   [displayName], 
   [accountSubType], 
   [description], 
   [schemeName], 
   [identification], 
   [secondaryIdentification], 
   [servicerSchemeName], 
   [servicerIdentification], 
   [dataCreditDebitIndicator], 
   [dataType], 
   [dataDateTime], 
   [dataCreditLineIncluded], 
   [dataCreditLineType], 
   [dataCreditLineAmount], 
   [dataCreditLineCurrency])
AS 
   SELECT DISTINCT 
      [${dbxschemaname}].accounts.Membership_id AS Membership_id, 
	  [${dbxschemaname}].accounts.MembershipName AS MembershipName,
      [${dbxschemaname}].accounts.Taxid AS Taxid, 
      [${dbxschemaname}].customeraccounts.Customer_id AS Customer_id, 
      [${dbxschemaname}].customeraccounts.Customer_id AS User_id, 
      [${dbxschemaname}].accounts.Account_id AS Account_id, 
      [${dbxschemaname}].accounts.isBusinessAccount AS isBusinessAccount, 
      [${dbxschemaname}].accounts.Type_id AS Type_id, 
      [${dbxschemaname}].accounts.UserName AS userName, 
      [${dbxschemaname}].accounts.CurrencyCode AS currencyCode, 
      [${dbxschemaname}].accounts.AccountHolder AS accountHolder, 
      [${dbxschemaname}].accounts.error AS error, 
      [${dbxschemaname}].accounts.Address AS Address, 
      [${dbxschemaname}].accounts.Scheme AS Scheme, 
      [${dbxschemaname}].accounts.Number AS number, 
      [${dbxschemaname}].accounts.AvailableBalance AS availableBalance, 
      [${dbxschemaname}].accounts.CurrentBalance AS currentBalance, 
      [${dbxschemaname}].accounts.InterestRate AS interestRate, 
      [${dbxschemaname}].accounts.AvailableCredit AS availableCredit, 
      [${dbxschemaname}].accounts.MinimumDue AS minimumDue, 
      [${dbxschemaname}].accounts.DueDate AS dueDate, 
      [${dbxschemaname}].accounts.FirstPaymentDate AS firstPaymentDate, 
      [${dbxschemaname}].accounts.ClosingDate AS closingDate, 
      [${dbxschemaname}].accounts.PaymentTerm AS paymentTerm, 
      [${dbxschemaname}].accounts.OpeningDate AS openingDate, 
      [${dbxschemaname}].accounts.MaturityDate AS maturityDate, 
      [${dbxschemaname}].accounts.DividendLastPaidAmount AS dividendLastPaidAmount, 
      [${dbxschemaname}].accounts.DividendLastPaidDate AS dividendLastPaidDate, 
      [${dbxschemaname}].accounts.DividendPaidYTD AS dividendPaidYTD, 
      [${dbxschemaname}].accounts.DividendRate AS dividendRate, 
      [${dbxschemaname}].accounts.DividendYTD AS dividendYTD, 
      [${dbxschemaname}].accounts.EStatementmentEnable AS eStatementEnable, 
	  [${dbxschemaname}].customeraccounts.IsOrganizationAccount as IsOrganizationAccount,
      [${dbxschemaname}].customeraccounts.FavouriteStatus AS favouriteStatus, 
      [${dbxschemaname}].accounts.StatusDesc AS statusDesc, 
      [${dbxschemaname}].accounts.NickName AS nickName, 
      [${dbxschemaname}].accounts.OriginalAmount AS originalAmount, 
      [${dbxschemaname}].accounts.OutstandingBalance AS outstandingBalance, 
      [${dbxschemaname}].accounts.PaymentDue AS paymentDue, 
      [${dbxschemaname}].accounts.PaymentMethod AS paymentMethod, 
      [${dbxschemaname}].accounts.SwiftCode AS swiftCode, 
      [${dbxschemaname}].accounts.TotalCreditMonths AS totalCreditMonths, 
      [${dbxschemaname}].accounts.TotalDebitsMonth AS totalDebitsMonth, 
      [${dbxschemaname}].accounts.RoutingNumber AS routingNumber, 
      [${dbxschemaname}].accounts.SupportBillPay AS supportBillPay, 
      [${dbxschemaname}].accounts.SupportCardlessCash AS supportCardlessCash, 
      [${dbxschemaname}].accounts.SupportTransferFrom AS supportTransferFrom, 
      [${dbxschemaname}].accounts.SupportTransferTo AS supportTransferTo, 
      [${dbxschemaname}].accounts.SupportDeposit AS supportDeposit, 
      [${dbxschemaname}].accounts.UnpaidInterest AS unpaidInterest, 
      [${dbxschemaname}].accounts.PreviousYearsDividends AS previousYearsDividends, 
      [${dbxschemaname}].accounts.principalBalance AS principalBalance, 
      [${dbxschemaname}].accounts.PrincipalValue AS principalValue, 
      [${dbxschemaname}].accounts.RegularPaymentAmount AS regularPaymentAmount, 
      [${dbxschemaname}].accounts.phone AS phoneId, 
      [${dbxschemaname}].accounts.LastDividendPaidDate AS lastDividendPaidDate, 
      [${dbxschemaname}].accounts.LastDividendPaidAmount AS lastDividendPaidAmount, 
      [${dbxschemaname}].accounts.LastPaymentAmount AS lastPaymentAmount, 
      [${dbxschemaname}].accounts.LastPaymentDate AS lastPaymentDate, 
      [${dbxschemaname}].accounts.LastStatementBalance AS lastStatementBalance, 
      [${dbxschemaname}].accounts.LateFeesDue AS lateFeesDue, 
      [${dbxschemaname}].accounts.maturityAmount AS maturityAmount, 
      [${dbxschemaname}].accounts.MaturityOption AS maturityOption, 
      [${dbxschemaname}].accounts.payoffAmount AS payoffAmount, 
      [${dbxschemaname}].accounts.PayOffCharge AS payOffCharge, 
      [${dbxschemaname}].accounts.PendingDeposit AS pendingDeposit, 
      [${dbxschemaname}].accounts.PendingWithdrawal AS pendingWithdrawal, 
      [${dbxschemaname}].accounts.JointHolders AS jointHolders, 
      [${dbxschemaname}].accounts.IsPFM AS isPFM, 
      [${dbxschemaname}].accounts.InterestPaidYTD AS interestPaidYTD, 
      [${dbxschemaname}].accounts.InterestPaidPreviousYTD AS interestPaidPreviousYTD, 
      [${dbxschemaname}].accounts.InterestPaidLastYear AS interestPaidLastYear, 
      [${dbxschemaname}].accounts.InterestEarned AS interestEarned, 
      [${dbxschemaname}].accounts.CurrentAmountDue AS currentAmountDue, 
      [${dbxschemaname}].accounts.CreditLimit AS creditLimit, 
      [${dbxschemaname}].accounts.CreditCardNumber AS creditCardNumber, 
      [${dbxschemaname}].accounts.BsbNum AS bsbNum, 
      [${dbxschemaname}].accounts.BondInterestLastYear AS bondInterestLastYear, 
      [${dbxschemaname}].accounts.BondInterest AS bondInterest, 
      [${dbxschemaname}].accounts.AvailablePoints AS availablePoints, 
      [${dbxschemaname}].accounts.AccountName AS accountName, 
      [${dbxschemaname}].accounts.email AS email, 
      [${dbxschemaname}].accounts.IBAN AS IBAN, 
      [${dbxschemaname}].accounts.adminProductId AS adminProductId, 
      [${dbxschemaname}].accounts.UpdatedBy AS UpdatedBy, 
      [${dbxschemaname}].accounts.LastUpdated AS LastUpdated, 
      [${dbxschemaname}].accounts.ActualUpdatedBY AS ActualUpdatedBY, 
      [${dbxschemaname}].bank.Description AS bankname, 
      [${dbxschemaname}].accounts.AccountPreference AS accountPreference, 
      [${dbxschemaname}].accounttype.transactionLimit AS transactionLimit, 
      [${dbxschemaname}].accounttype.transferLimit AS transferLimit, 
      [${dbxschemaname}].accounttype.rates AS rates, 
      [${dbxschemaname}].accounttype.termsAndConditions AS termsAndConditions, 
      [${dbxschemaname}].accounttype.TypeDescription AS typeDescription, 
      [${dbxschemaname}].accounttype.supportChecks AS supportChecks, 
      [${dbxschemaname}].accounttype.displayName AS displayName, 
      [${dbxschemaname}].accounts.accountSubType AS accountSubType, 
      [${dbxschemaname}].accounts.description AS description, 
      [${dbxschemaname}].accounts.schemeName AS schemeName, 
      [${dbxschemaname}].accounts.identification AS identification, 
      [${dbxschemaname}].accounts.secondaryIdentification AS secondaryIdentification, 
      [${dbxschemaname}].accounts.servicerSchemeName AS servicerSchemeName, 
      [${dbxschemaname}].accounts.servicerIdentification AS servicerIdentification, 
      [${dbxschemaname}].accounts.dataCreditDebitIndicator AS dataCreditDebitIndicator, 
      [${dbxschemaname}].accounts.dataType AS dataType, 
      [${dbxschemaname}].accounts.dataDateTime AS dataDateTime, 
      [${dbxschemaname}].accounts.dataCreditLineIncluded AS dataCreditLineIncluded, 
      [${dbxschemaname}].accounts.dataCreditLineType AS dataCreditLineType, 
      [${dbxschemaname}].accounts.dataCreditLineAmount AS dataCreditLineAmount, 
      [${dbxschemaname}].accounts.dataCreditLineCurrency AS dataCreditLineCurrency
   FROM ((((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].customeraccounts ON ([${dbxschemaname}].accounts.Account_id = customeraccounts.Account_id)) 
      INNER JOIN [${dbxschemaname}].accounttype ON ([${dbxschemaname}].accounts.Type_id = accounttype.TypeID)) 
      LEFT JOIN [${dbxschemaname}].membershipaccounts 
      ON (([${dbxschemaname}].accounts.Account_id = membershipaccounts.accountId)))
	  LEFT JOIN [${dbxschemaname}].membership on
		(([${dbxschemaname}].membership.id = [${dbxschemaname}].membershipaccounts.membershipId)))
      LEFT JOIN [${dbxschemaname}].bank 
      ON (([${dbxschemaname}].accounts.Bank_id = bank.id)))


GO
/****** Object:  View [${dbxschemaname}].[customeraddress_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customeraddress_view] (
   [CustomerId], 
   [Address_id], 
   [isPrimary], 
   [AddressType], 
   [isTypeBusiness], 
   [AddressId], 
   [AddressLine1], 
   [AddressLine2], 
   [ZipCode], 
   [Region_id], 
   [City_id], 
   [Country_id], 
   [CityName], 
   [RegionName], 
   [RegionCode], 
   [CountryName], 
   [CountryCode])
AS 
   SELECT 
      c.id AS CustomerId, 
      ca.Address_id AS Address_id, 
      ca.isPrimary AS isPrimary, 
      ca.Type_id AS AddressType, 
      ca.isTypeBusiness AS isTypeBusiness, 
      a.id AS AddressId, 
      a.addressLine1 AS AddressLine1, 
      a.addressLine2 AS AddressLine2, 
      a.zipCode AS ZipCode, 
      a.Region_id AS Region_id, 
      a.City_id AS City_id, 
      coun.id AS Country_id, 
      a.cityName AS CityName, 
      reg.Name AS RegionName, 
      reg.Code AS RegionCode, 
      coun.Name AS CountryName, 
      coun.Code AS CountryCode
   FROM (((([${dbxschemaname}].customeraddress  AS ca 
      INNER JOIN [${dbxschemaname}].customer  AS c 
      ON ((ca.Customer_id = c.id))) 
      INNER JOIN [${dbxschemaname}].address  AS a 
      ON ((a.id = ca.Address_id))) 
      INNER JOIN [${dbxschemaname}].region  AS reg 
      ON ((reg.id = a.Region_id))) 
      INNER JOIN [${dbxschemaname}].country  AS coun 
      ON ((coun.id = reg.Country_id)))
GO
/****** Object:  View [${dbxschemaname}].[customeraddressmbview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customeraddressmbview] (
   [CustomerId], 
   [Address_id], 
   [Type_id], 
   [isTypeBusiness], 
   [DurationOfStay], 
   [HomeOwnership], 
   [isPrimary], 
   [AddressType], 
   [AddressLine1], 
   [AddressLine2], 
   [AddressLine3], 
   [ZipCode], 
   [CityName], 
   [CountryName], 
   [State])
AS 
   /*
   *   SSMA informational messages:
   *   M2SS0052: string literal was converted to NUMERIC literal
   */

   SELECT 
      ca.Customer_id AS CustomerId, 
      ca.Address_id AS Address_id, 
      ca.Type_id AS Type_id, 
      ca.isTypeBusiness AS isTypeBusiness, 
      ca.DurationOfStay AS DurationOfStay, 
      ca.HomeOwnership AS HomeOwnership, 
      ca.isPrimary AS isPrimary, 
      at.Description AS AddressType, 
      a.addressLine1 AS AddressLine1, 
      a.addressLine2 AS AddressLine2, 
      a.addressLine3 AS AddressLine3, 
      a.zipCode AS ZipCode, 
      a.cityName AS CityName, 
      a.country AS CountryName, 
      a.state AS State
   FROM (([${dbxschemaname}].customeraddress  AS ca 
      INNER JOIN [${dbxschemaname}].address  AS a 
      ON ((a.id = ca.Address_id))) 
      INNER JOIN [${dbxschemaname}].addresstype  AS at 
      ON ((ca.Type_id = at.id)))
   WHERE (a.softdeleteflag = 0)
GO
/****** Object:  View [${dbxschemaname}].[customercommunicationview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
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
   customer.MaritalStatus_id,maritalstatus.Description,customer.SpouseName,customer.DrivingLicenseNumber,customer.lockedOn
   ,customer.lockCount,customer.EmployementStatus_id,customer.EmployementStatus_id,employementstatus.Description,
   customer.IsEnrolledForOlb,customer.IsStaffMember,customer.Location_id,location.Name,location.Code,customer.IsOlbAllowed
   ,customer.IsAssistConsented,customer.IsAssistConsented,customer.isEagreementSigned,customer.isCombinedUser, customer.CustomerType_id,
   customertype.Name,customertype.Description,customer.Organization_Id,organisation.Name,primaryphone.Value,primaryemail.Value,
   customer.DocumentsSubmitted,customer.ApplicantChannel,customer.Product,customer.Reason
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customercommunicationview] (
   [id], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [UserName], 
   [Gender], 
   [DateOfBirth], 
   [Ssn], 
   [Status_id], 
   [CustomerType], 
   [Phone], 
   [Email])
AS 
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
      customertype.Name AS CustomerType, 
      primaryphone.Value AS Phone, 
      primaryemail.Value AS Email
   FROM ((([${dbxschemaname}].customer 
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
      INNER JOIN [${dbxschemaname}].customertype 
      ON ((customertype.id = customer.CustomerType_id)))
GO
/****** Object:  View [${dbxschemaname}].[customergroupinfo_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customergroupinfo_view] (
   [Customer_id], 
   [Group_id], 
   [Group_name], 
   [Group_Desc], 
   [GroupStatus_id], 
   [GroupStatus_name], 
   [Group_createdby], 
   [Group_modifiedby], 
   [Group_createdts], 
   [Group_lastmodifiedts], 
   [Group_synctimestamp], 
   [Group_Type_id])
AS 
   SELECT 
      customergroup.Customer_id AS Customer_id, 
      customergroup.Group_id AS Group_id, 
      membergroup.Name AS Group_name, 
      membergroup.Description AS Group_Desc, 
      membergroup.Status_id AS GroupStatus_id, 
      
         (
            SELECT status.Description
            FROM [${dbxschemaname}].status
            WHERE (membergroup.Status_id = status.id)
         ) AS GroupStatus_name, 
      membergroup.createdby AS Group_createdby, 
      membergroup.modifiedby AS Group_modifiedby, 
      membergroup.createdts AS Group_createdts, 
      membergroup.lastmodifiedts AS Group_lastmodifiedts, 
      membergroup.synctimestamp AS Group_synctimestamp, 
      membergroup.Type_id AS Group_Type_id
   FROM ([${dbxschemaname}].customergroup 
      INNER JOIN [${dbxschemaname}].membergroup 
      ON ((customergroup.Group_id = membergroup.id)))
GO
/****** Object:  View [${dbxschemaname}].[customernotes_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customernotes_view] (
   [id], 
   [Note], 
   [Customer_id], 
   [Customer_FirstName], 
   [Customer_MiddleName], 
   [Customer_LastName], 
   [Customer_Username], 
   [Customer_Status_id], 
   [InternalUser_id], 
   [InternalUser_Username], 
   [InternalUser_FirstName], 
   [InternalUser_LastName], 
   [InternalUser_MiddleName], 
   [InternalUser_Email], 
   [createdts], 
   [synctimestamp], 
   [softdeleteflag])
AS 
   SELECT 
      customernote.id AS id, 
      customernote.Note AS Note, 
      customernote.Customer_id AS Customer_id, 
      customer.FirstName AS Customer_FirstName, 
      customer.MiddleName AS Customer_MiddleName, 
      customer.LastName AS Customer_LastName, 
      customer.UserName AS Customer_Username, 
      customer.Status_id AS Customer_Status_id, 
      customernote.createdby AS InternalUser_id, 
      systemuser.Username AS InternalUser_Username, 
      systemuser.FirstName AS InternalUser_FirstName, 
      systemuser.LastName AS InternalUser_LastName, 
      systemuser.MiddleName AS InternalUser_MiddleName, 
      systemuser.Email AS InternalUser_Email, 
      customernote.createdts AS createdts, 
      customernote.synctimestamp AS synctimestamp, 
      customernote.softdeleteflag AS softdeleteflag
   FROM (([${dbxschemaname}].customernote 
      INNER JOIN [${dbxschemaname}].systemuser 
      ON ((customernote.createdby = systemuser.id))) 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customernote.Customer_id = customer.id)))
GO
/****** Object:  View [${dbxschemaname}].[customernotifications_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customernotifications_view] (
   [customer_Id], 
   [isread], 
   [Name], 
   [Description], 
   [StartDate], 
   [ExpirationDate], 
   [Status_id], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag])
AS 
    (
      SELECT 
         cn.Customer_id AS customer_Id, 
         cn.IsRead AS isread, 
         n.Name AS Name, 
         n.Description AS Description, 
         n.StartDate AS StartDate, 
         n.ExpirationDate AS ExpirationDate, 
         n.Status_id AS Status_id, 
         n.createdby AS createdby, 
         n.modifiedby AS modifiedby, 
         n.createdts AS createdts, 
         n.lastmodifiedts AS lastmodifiedts, 
         n.synctimestamp AS synctimestamp, 
         n.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].adminnotification  AS n 
         INNER JOIN [${dbxschemaname}].customernotification  AS cn 
         ON ((n.Id = cn.Notification_id)))
    )
GO
/****** Object:  View [${dbxschemaname}].[customerorganisationmembershipview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customerorganisationmembershipview] (
   [LastName], 
   [DateOfBirth], 
   [Ssn], 
   [Phone], 
   [Email], 
   [UserName], 
   [Gender], 
   [id], 
   [FirstName], 
   [CustomerType], 
   [Organization_Id], 
   [Membership_id], 
   [Taxid])
AS 
   SELECT DISTINCT 
      customer.LastName AS LastName, 
      customer.DateOfBirth AS DateOfBirth, 
      customer.Ssn AS Ssn, 
      primaryphone.Value AS Phone, 
      primaryemail.Value AS Email, 
      customer.UserName AS UserName, 
      customer.Gender AS Gender, 
      customer.id AS id, 
      customer.FirstName AS FirstName, 
      customer.CustomerType_id AS CustomerType, 
      customer.Organization_Id AS Organization_Id, 
      organisationmembership.Membership_id AS Membership_id, 
      organisationmembership.Taxid AS Taxid
   FROM (((([${dbxschemaname}].customer 
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
      CROSS JOIN [${dbxschemaname}].organisationemployees) 
      CROSS JOIN [${dbxschemaname}].organisationmembership)
   WHERE (((customer.Organization_Id = organisationemployees.Organization_id) OR (customer.id = organisationemployees.Customer_id)) AND (organisationemployees.Organization_id = organisationmembership.Organization_id))
GO
/****** Object:  View [${dbxschemaname}].[customerpermissions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customerpermissions_view] (
   [Customer_id], 
   [Service_id], 
   [Service_name], 
   [Service_description], 
   [Service_notes], 
   [Status_id], 
   [Status_description], 
   [TransactionFee_id], 
   [TransactionFee_description], 
   [TransactionLimit_id], 
   [TransactionLimit_description], 
   [ServiceType_id], 
   [ServiceType_description], 
   [MinTransferLimit], 
   [MaxTransferLimit], 
   [Display_Name], 
   [Display_Description])
AS 
   SELECT 
      customerentitlement.Customer_id AS Customer_id, 
      customerentitlement.Service_id AS Service_id, 
      service.Name AS Service_name, 
      service.Description AS Service_description, 
      service.Notes AS Service_notes, 
      service.Status_id AS Status_id, 
      status.Description AS Status_description, 
      customerentitlement.TransactionFee_id AS TransactionFee_id, 
      transactionfee.Description AS TransactionFee_description, 
      customerentitlement.TransactionLimit_id AS TransactionLimit_id, 
      transactionlimit.Description AS TransactionLimit_description, 
      service.Type_id AS ServiceType_id, 
      servicetype.Description AS ServiceType_description, 
      service.MinTransferLimit AS MinTransferLimit, 
      service.MaxTransferLimit AS MaxTransferLimit, 
      service.DisplayName AS Display_Name, 
      service.DisplayDescription AS Display_Description
   FROM ((((([${dbxschemaname}].customerentitlement 
      LEFT JOIN [${dbxschemaname}].service 
      ON ((customerentitlement.Service_id = service.id))) 
      LEFT JOIN [${dbxschemaname}].status 
      ON ((service.Status_id = status.id))) 
      LEFT JOIN [${dbxschemaname}].transactionfee 
      ON ((transactionfee.id = customerentitlement.TransactionFee_id))) 
      LEFT JOIN [${dbxschemaname}].transactionlimit 
      ON ((transactionlimit.id = customerentitlement.TransactionLimit_id))) 
      LEFT JOIN [${dbxschemaname}].servicetype 
      ON ((servicetype.id = service.Type_id)))
GO
/****** Object:  View [${dbxschemaname}].[customerpreferencesview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customerpreferencesview] (
   [addressLine1], 
   [addressLine2], 
   [state], 
   [city], 
   [country], 
   [zipcode], 
   [areUserAlertsTurnedOn], 
   [areAccountStatementTermsAccepted], 
   [areDepositTermsAccepted], 
   [default_account_deposit], 
   [default_account_billPay], 
   [default_account_payments], 
   [default_account_cardless], 
   [default_account_transfers], 
   [DefaultModule_id], 
   [default_account_wire], 
   [default_from_account_p2p], 
   [default_to_account_p2p], 
   [isP2PActivated], 
   [isP2PSupported], 
   [isBillPaySupported], 
   [isBillPayActivated], 
   [isWireTransferActivated], 
   [isWireTransferEligible], 
   [showBillPayFromAccPopup], 
   [userFirstName], 
   [userLastName], 
   [gender], 
   [isPinSet], 
   [DateOfBirth], 
   [noofdependents], 
   [spousefirstname], 
   [ssn], 
   [CountryCode], 
   [userImage], 
   [userImageURL], 
   [isEagreementSigned], 
   [id], 
   [UserName], 
   [maritalstatus], 
   [lastlogintime], 
   [Bank_id], 
   [Phone], 
   [Email])
AS 
   SELECT DISTINCT 
      address.addressLine1 AS addressLine1, 
      address.addressLine2 AS addressLine2, 
      address.state AS state, 
      address.cityName AS city, 
      address.country AS country, 
      address.zipCode AS zipcode, 
      customer.areUserAlertsTurnedOn AS areUserAlertsTurnedOn, 
      customer.areAccountStatementTermsAccepted AS areAccountStatementTermsAccepted, 
      customer.areDepositTermsAccepted AS areDepositTermsAccepted, 
      customerpreference.DefaultAccountDeposit AS default_account_deposit, 
      customerpreference.DefaultAccountBillPay AS default_account_billPay, 
      customerpreference.DefaultAccountPayments AS default_account_payments, 
      customerpreference.DefaultAccountCardless AS default_account_cardless, 
      customerpreference.DefaultAccountTransfers AS default_account_transfers, 
      customerpreference.DefaultModule_id AS DefaultModule_id, 
      customerpreference.DefaultAccountWire AS default_account_wire, 
      customerpreference.DefaultFromAccountP2P AS default_from_account_p2p, 
      customerpreference.DefaultToAccountP2P AS default_to_account_p2p, 
      customer.isP2PActivated AS isP2PActivated, 
      customer.isP2PSupported AS isP2PSupported, 
      customer.isBillPaySupported AS isBillPaySupported, 
      customer.isBillPayActivated AS isBillPayActivated, 
      customer.isWireTransferActivated AS isWireTransferActivated, 
      customer.isWireTransferEligible AS isWireTransferEligible, 
      customerpreference.ShowBillPayFromAccPopup AS showBillPayFromAccPopup, 
      customer.FirstName AS userFirstName, 
      customer.LastName AS userLastName, 
      customer.Gender AS gender, 
      customer.IsPinSet AS isPinSet, 
      customer.DateOfBirth AS DateOfBirth, 
      customer.NoOfDependents AS noofdependents, 
      customer.SpouseName AS spousefirstname, 
      customer.Ssn AS ssn, 
      customer.CountryCode AS CountryCode, 
      customer.UserImage AS userImage, 
      customer.UserImageURL AS userImageURL, 
      customer.isEagreementSigned AS isEagreementSigned, 
      customer.id AS id, 
      customer.UserName AS UserName, 
      customer.MaritalStatus_id AS maritalstatus, 
      customer.Lastlogintime AS lastlogintime, 
      customer.Bank_id AS Bank_id, 
      primaryphone.Value AS Phone, 
      primaryemail.Value AS Email
   FROM (((([${dbxschemaname}].customer 
      LEFT JOIN [${dbxschemaname}].customerpreference 
      ON ((customerpreference.Customer_id = customer.id))) 
      LEFT JOIN ([${dbxschemaname}].customeraddress 
      INNER JOIN [${dbxschemaname}].address 
      ON ((address.id = customeraddress.Address_id))) 
      ON (((customer.id = customeraddress.Customer_id) AND (customeraddress.isPrimary = 1)))) 
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
GO
/****** Object:  View [${dbxschemaname}].[customersecurityquestion_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customerrequests_view] (
   [id], 
   [softdeleteflag], 
   [priority], 
   [requestCreatedDate], 
   [recentMsgDate], 
   [requestcategory_id], 
   [customer_id], 
   [username], 
   [status_id], 
   [statusIdentifier], 
   [requestsubject], 
   [accountid], 
   [assignTo], 
   [totalmsgs], 
   [readmsgs], 
   [unreadmsgs],
   [firstMessage],
   [msgids],
   [totalAttachments])
AS 
   SELECT TOP 100 PERCENT
      ([${dbxschemaname}].customerrequest.id ) AS id, 
       [${dbxschemaname}].customerrequest.softdeleteflag AS softdeleteflag, 
      ([${dbxschemaname}].customerrequest.Priority) AS [priority], 
      ([${dbxschemaname}].customerrequest.createdts) AS requestCreatedDate, 
      max([${dbxschemaname}].requestmessage.createdts) AS recentMsgDate, 
      ([${dbxschemaname}].requestcategory.Name) AS requestcategory_id, 
      ([${dbxschemaname}].customerrequest.Customer_id) AS customer_id, 
      ([${dbxschemaname}].customer.UserName) AS username, 
      (SELECT TOP (1) [status].[Description]
            FROM [${dbxschemaname}].[status]
            WHERE ([status].id = min([${dbxschemaname}].customerrequest.Status_id))
         ) AS status_id, 
      ([${dbxschemaname}].customerrequest.id) AS statusIdentifier, 
      ([${dbxschemaname}].customerrequest.RequestSubject) AS requestsubject, 
      ([${dbxschemaname}].customerrequest.Accountid) AS accountid, 
      ([${dbxschemaname}].systemuser.FirstName) + (N' ') + min([${dbxschemaname}].systemuser.LastName) AS assignTo, 
      count([${dbxschemaname}].requestmessage.id) AS totalmsgs, 
      count((
         CASE 
            WHEN ([${dbxschemaname}].requestmessage.IsRead = 'true') THEN 1
         END)) AS readmsgs, 
      count((
         CASE 
            WHEN ([${dbxschemaname}].requestmessage.IsRead = 'false') THEN 1
         END)) AS unreadmsgs,
		 [${dbxschemaname}].substring_index(
   (String_agg(
      CAST(requestmessage.MessageDescription as nvarchar(max)),'||') WITHIN GROUP (order by requestmessage.createdts ASC, requestmessage.id ASC) ),'||', 1) AS firstMessage, 
  String_agg(CAST(requestmessage.id as nvarchar(max)),',') AS msgids, 
  count(messageattachment.id) AS totalAttachments  

  FROM 
  ((((( [${dbxschemaname}].customerrequest 
  left join [${dbxschemaname}].requestmessage on((
  customerrequest.id = requestmessage.CustomerRequest_id))) 
  left join [${dbxschemaname}].requestcategory on((
  requestcategory.id = customerrequest.RequestCategory_id))) 
  left join [${dbxschemaname}].customer on((
  customer.id = customerrequest.Customer_id))) 
  left join [${dbxschemaname}].messageattachment on((
  messageattachment.RequestMessage_id = requestmessage.id))) 
  left join [${dbxschemaname}].systemuser on((
  systemuser.id = customerrequest.AssignedTo))) 
group by 
  customerrequest.id ,customerrequest.softdeleteflag,customerrequest.[Priority],[${dbxschemaname}].customerrequest.createdts,requestcategory.[Name],customerrequest.Customer_id,
  customer.UserName,customerrequest.RequestSubject,customerrequest.Accountid,systemuser.FirstName

order by 
  customerrequest.createdts DESC,
  customerrequest.id
GO
/****** Object:  View [${dbxschemaname}].[customersecurityquestion_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[customersecurityquestion_view] (
   [Customer_id], 
   [SecurityQuestion_id], 
   [CustomerAnswer], 
   [isTypeBusiness], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [Question], 
   [QuestionStatus_id], 
   [CustomerStatus_id])
AS 
   SELECT 
      customersecurityquestions.Customer_id AS Customer_id, 
      customersecurityquestions.SecurityQuestion_id AS SecurityQuestion_id, 
      customersecurityquestions.CustomerAnswer AS CustomerAnswer, 
      customersecurityquestions.isTypeBusiness AS isTypeBusiness, 
      customersecurityquestions.createdby AS createdby, 
      customersecurityquestions.modifiedby AS modifiedby, 
      customersecurityquestions.createdts AS createdts, 
      customersecurityquestions.lastmodifiedts AS lastmodifiedts, 
      securityquestion.Question AS Question, 
      securityquestion.Status_id AS QuestionStatus_id, 
      customer.Status_id AS CustomerStatus_id
   FROM (([${dbxschemaname}].customersecurityquestions 
      INNER JOIN [${dbxschemaname}].securityquestion 
      ON ((securityquestion.id = customersecurityquestions.SecurityQuestion_id))) 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customer.id = customersecurityquestions.Customer_id)))
GO
/****** Object:  View [${dbxschemaname}].[customerview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE

   
      VIEW [${dbxschemaname}].[customerservice_communication_view]
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
               [${dbxschemaname}].servicecommunication.softdeleteflag AS ServiceCommunication_SoftDeleteFlag
         FROM ([${dbxschemaname}].servicecommunication 
            JOIN customerservice ON (([${dbxschemaname}].servicecommunication.Service_id = [${dbxschemaname}].customerservice.id)));
GO
/****** Object:  View [${dbxschemaname}].[customerview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customerview] (
   [id], 
   [FirstName], 
   [LastName], 
   [UserName], 
   [Value], 
   [isTypeBusiness], 
   [description])
AS 
   SELECT 
      c.id AS id, 
      c.FirstName AS FirstName, 
      c.LastName AS LastName, 
      c.UserName AS UserName, 
      cc.Value AS Value, 
      cc.isTypeBusiness AS isTypeBusiness, 
      ct.Description AS description
   FROM (([${dbxschemaname}].customercommunication  AS cc 
      CROSS JOIN [${dbxschemaname}].customer  AS c) 
      CROSS JOIN [${dbxschemaname}].communicationtype  AS ct)
   WHERE ((cc.Type_id = ct.id) AND (cc.Customer_id = c.id))
GO
/****** Object:  View [${dbxschemaname}].[faqcategory_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[faqcategory_view] (
   [id], 
   [Status_id], 
   [QuestionCode], 
   [Question], 
   [Channel_id], 
   [Answer], 
   [CategoryId], 
   [CategoryName])
AS 
   SELECT 
      faqs.id AS id, 
      faqs.Status_id AS Status_id, 
      faqs.QuestionCode AS QuestionCode, 
      faqs.Question AS Question, 
      faqs.Channel_id AS Channel_id, 
      faqs.Answer AS Answer, 
      faqs.FaqCategory_Id AS CategoryId, 
      faqcategory.Name AS CategoryName
   FROM ([${dbxschemaname}].faqs 
      INNER JOIN [${dbxschemaname}].faqcategory 
      ON ((faqcategory.id = faqs.FaqCategory_Id)))
GO
/****** Object:  View [${dbxschemaname}].[feature_actions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

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
   [value])
AS 
   SELECT 
      featureaction.id AS id, 
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
      ON ((actionlimit.Action_id = featureaction.id)))
GO
/****** Object:  View [${dbxschemaname}].[feature_details_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[feature_details_view] (
   [id], 
   [name], 
   [description], 
   [Type_id], 
   [Status_id], 
   [Locale_id], 
   [displayName], 
   [displayDescription])
AS 
   SELECT 
      feature.id AS id, 
      feature.name AS name, 
      feature.description AS description, 
      feature.Type_id AS Type_id, 
      feature.Status_id AS Status_id, 
      featuredisplaynamedescription.Locale_id AS Locale_id, 
      featuredisplaynamedescription.displayName AS displayName, 
      featuredisplaynamedescription.displayDescription AS displayDescription
   FROM ([${dbxschemaname}].feature 
      LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription 
      ON ((featuredisplaynamedescription.Feature_id = feature.id)))
GO
/****** Object:  View [${dbxschemaname}].[group_features_actions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[feature_view] (
   [Code], 
   [Type], 
   [Type_Id], 
   [Name], 
   [Status], 
   [Status_Id])
AS 
   SELECT 
      feature.id AS Code, 
      NULL AS Type, 
      NULL AS Type_Id, 
      min(feature.name) AS Name, 
      min(status.Description) AS Status, 
      min(status.id) AS Status_Id
   FROM ((([${dbxschemaname}].feature 
      LEFT JOIN [${dbxschemaname}].featureroletype  AS fr 
      ON ((fr.Feature_id = feature.id))) 
      LEFT JOIN [${dbxschemaname}].membergrouptype 
      ON ((membergrouptype.id = fr.RoleType_id))) 
      LEFT JOIN [${dbxschemaname}].status 
      ON ((feature.Status_id = status.id)))
   GROUP BY feature.id
GO
/****** Object:  View [${dbxschemaname}].[fromaccountransactionview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[fromaccountransactionview] (
   [Account_id], 
   [AccountName], 
   [NickName], 
   [AccountHolder], 
   [UserName], 
   [ExternalBankidentity_id], 
   [CurrencyCode], 
   [User_id], 
   [AvailableBalance], 
   [Bank_id], 
   [ShowTransactions], 
   [CurrentBalance], 
   [SwiftCode], 
   [RoutingNumber], 
   [BankNmae], 
   [transactionId], 
   [transactiontype], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Bill_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [isScheduled], 
   [category], 
   [billCategory], 
   [ExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [createdDate], 
   [cashlessEmail], 
   [cashlessMode], 
   [cashlessOTP], 
   [cashlessOTPValidDate], 
   [cashlessPersonName], 
   [cashlessPhone], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [frequencyEndDate], 
   [frequencyStartDate], 
   [hasDepositImage], 
   [payeeId], 
   [p2pContact], 
   [personId], 
   [recurrenceDesc], 
   [numberOfRecurrences], 
   [scheduledDate], 
   [transactionComments], 
   [transactionsNotes], 
   [transDescription], 
   [transactionDate], 
   [description], 
   [TypeDescription], 
   [IBAN], 
   [sortCode])
AS 

   SELECT 
      min(accounts.Account_id) AS Account_id, 
      min(accounts.AccountName) AS AccountName, 
      min(accounts.NickName) AS NickName, 
      min(accounts.AccountHolder) AS AccountHolder, 
      min(accounts.UserName) AS UserName, 
      min(accounts.ExternalBankidentity_id) AS ExternalBankidentity_id, 
      min(accounts.CurrencyCode) AS CurrencyCode, 
      min(accounts.[User_id]) AS [User_id], 
      min(accounts.AvailableBalance) AS AvailableBalance, 
      min(accounts.Bank_id) AS Bank_id, 
      accounts.ShowTransactions AS ShowTransactions, 
      min(accounts.CurrentBalance) AS CurrentBalance, 
      min(accounts.SwiftCode) AS SwiftCode, 
      min(accounts.RoutingNumber) AS RoutingNumber, 
      min(bank.[Description]) AS BankNmae, 
      [transaction].Id AS transactionId, 
      min([transaction].[Type_id]) AS transactiontype, 
      min([transaction].Customer_id) AS Customer_id, 
      min([transaction].ExpenseCategory_id) AS ExpenseCategory_id, 
      min([transaction].Bill_id) AS Bill_id, 
      min([transaction].Reference_id) AS Reference_id, 
      min([transaction].fromAccountNumber) AS fromAccountNumber, 
      min([transaction].fromAccountBalance) AS fromAccountBalance, 
      min([transaction].toAccountNumber) AS toAccountNumber, 
      min([transaction].toAccountBalance) AS toAccountBalance, 
      min([transaction].amount) AS amount, 
      min([transaction].Status_id) AS Status_id, 
      min([transaction].statusDesc) AS statusDesc, 
      [transaction].isScheduled AS isScheduled, 
      min([transaction].category) AS category, 
      min([transaction].billCategory) AS billCategory, 
      min([transaction].toExternalAccountNumber) AS ExternalAccountNumber, 
      min([transaction].Person_Id) AS Person_Id, 
      min([transaction].frequencyType) AS frequencyType, 
      min([transaction].createdDate) AS createdDate, 
      min([transaction].cashlessEmail) AS cashlessEmail, 
      min([transaction].cashlessMode) AS cashlessMode, 
      min([transaction].cashlessOTP) AS cashlessOTP, 
      min([transaction].cashlessOTPValidDate) AS cashlessOTPValidDate, 
      min([transaction].cashlessPersonName) AS cashlessPersonName, 
      min([transaction].cashlessPhone) AS cashlessPhone, 
      min([transaction].cashlessSecurityCode) AS cashlessSecurityCode, 
      min([transaction].cashWithdrawalTransactionStatus) AS cashWithdrawalTransactionStatus, 
      min([transaction].frequencyEndDate) AS frequencyEndDate, 
      min([transaction].frequencyStartDate) AS frequencyStartDate, 
      min([transaction].hasDepositImage) AS hasDepositImage, 
      min([transaction].Payee_id) AS payeeId, 
      min([transaction].p2pContact) AS p2pContact, 
      min([transaction].Person_Id) AS personId, 
      min([transaction].recurrenceDesc) AS recurrenceDesc, 
      min([transaction].numberOfRecurrences) AS numberOfRecurrences, 
      min([transaction].scheduledDate) AS scheduledDate, 
      min([transaction].transactionComments) AS transactionComments, 
      min([transaction].notes) AS transactionsNotes, 
      min([transaction].[description]) AS transDescription, 
      min([transaction].transactionDate) AS transactionDate, 
      min(transactiontype.[description]) AS [description], 
      min(accounttype.TypeDescription) AS TypeDescription, 
      min([transaction].IBAN) AS IBAN, 
      min([transaction].sortCode) AS sortCode
   FROM (((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].[transaction] on (accounts.Account_id = [transaction].fromAccountNumber) )
      INNER JOIN [${dbxschemaname}].transactiontype on ([transaction].[Type_id] = transactiontype.Id)) 
      INNER JOIN [${dbxschemaname}].accounttype on (accounttype.TypeID = accounts.[Type_id])) 
      LEFT JOIN [${dbxschemaname}].bank on (bank.id = accounts.Bank_id))

   GROUP BY [transaction].Id,[transaction].isScheduled, ShowTransactions
GO
 
/****** Object:  View [${dbxschemaname}].[getaccountsview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[getaccountsview] (
   [Account_id], 
   [Type_id], 
   [userName], 
   [currencyCode], 
   [accountHolder], 
   [isBusinessAccount], 
   [error], 
   [Address], 
   [Scheme], 
   [number], 
   [availableBalance], 
   [currentBalance], 
   [interestRate], 
   [availableCredit], 
   [minimumDue], 
   [dueDate], 
   [firstPaymentDate], 
   [closingDate], 
   [paymentTerm], 
   [openingDate], 
   [maturityDate], 
   [dividendLastPaidAmount], 
   [dividendLastPaidDate], 
   [dividendPaidYTD], 
   [dividendRate], 
   [dividendYTD], 
   [eStatementEnable], 
   [favouriteStatus], 
   [statusDesc], 
   [nickName], 
   [User_id], 
   [originalAmount], 
   [outstandingBalance], 
   [paymentDue], 
   [paymentMethod], 
   [swiftCode], 
   [totalCreditMonths], 
   [totalDebitsMonth], 
   [routingNumber], 
   [supportBillPay], 
   [supportCardlessCash], 
   [supportTransferFrom], 
   [supportTransferTo], 
   [supportDeposit], 
   [unpaidInterest], 
   [previousYearsDividends], 
   [principalBalance], 
   [principalValue], 
   [regularPaymentAmount], 
   [phoneId], 
   [lastDividendPaidDate], 
   [lastDividendPaidAmount], 
   [lastPaymentAmount], 
   [lastPaymentDate], 
   [lastStatementBalance], 
   [lateFeesDue], 
   [maturityAmount], 
   [maturityOption], 
   [payoffAmount], 
   [payOffCharge], 
   [pendingDeposit], 
   [pendingWithdrawal], 
   [jointHolders], 
   [isPFM], 
   [interestPaidYTD], 
   [interestPaidPreviousYTD], 
   [interestPaidLastYear], 
   [interestEarned], 
   [currentAmountDue], 
   [creditLimit], 
   [creditCardNumber], 
   [bsbNum], 
   [bondInterestLastYear], 
   [bondInterest], 
   [availablePoints], 
   [accountName], 
   [email], 
   [IBAN], 
   [adminProductId], 
   [bankname], 
   [accountPreference], 
   [transactionLimit], 
   [transferLimit], 
   [rates], 
   [termsAndConditions], 
   [typeDescription], 
   [supportChecks], 
   [displayName], 
   [accountsubType], 
   [description], 
   [schemeName], 
   [identification], 
   [secondaryIdentification], 
   [servicerSchemeName], 
   [servicerIdentification], 
   [dataCreditDebitIndicator], 
   [dataType], 
   [dataDateTime], 
   [dataCreditLineIncluded], 
   [dataCreditLineType], 
   [dataCreditLineAmount], 
   [dataCreditLineCurrency], 
   [UpdatedBy], 
   [LastUpdated], 
   [ActualUpdatedBY])
AS 
   SELECT 
      accounts.Account_id AS Account_id, 
      accounts.Type_id AS Type_id, 
      accounts.UserName AS userName, 
      accounts.CurrencyCode AS currencyCode, 
      accounts.AccountHolder AS accountHolder, 
      accounts.isBusinessAccount AS isBusinessAccount, 
      accounts.error AS error, 
      accounts.Address AS Address, 
      accounts.Scheme AS Scheme, 
      accounts.Number AS number, 
      accounts.AvailableBalance AS availableBalance, 
      accounts.CurrentBalance AS currentBalance, 
      accounts.InterestRate AS interestRate, 
      accounts.AvailableCredit AS availableCredit, 
      accounts.MinimumDue AS minimumDue, 
      accounts.DueDate AS dueDate, 
      accounts.FirstPaymentDate AS firstPaymentDate, 
      accounts.ClosingDate AS closingDate, 
      accounts.PaymentTerm AS paymentTerm, 
      accounts.OpeningDate AS openingDate, 
      accounts.MaturityDate AS maturityDate, 
      accounts.DividendLastPaidAmount AS dividendLastPaidAmount, 
      accounts.DividendLastPaidDate AS dividendLastPaidDate, 
      accounts.DividendPaidYTD AS dividendPaidYTD, 
      accounts.DividendRate AS dividendRate, 
      accounts.DividendYTD AS dividendYTD, 
      accounts.EStatementmentEnable AS eStatementEnable, 
      accounts.FavouriteStatus AS favouriteStatus, 
      accounts.StatusDesc AS statusDesc, 
      accounts.NickName AS nickName, 
      accounts.User_id AS User_id, 
      accounts.OriginalAmount AS originalAmount, 
      accounts.OutstandingBalance AS outstandingBalance, 
      accounts.PaymentDue AS paymentDue, 
      accounts.PaymentMethod AS paymentMethod, 
      accounts.SwiftCode AS swiftCode, 
      accounts.TotalCreditMonths AS totalCreditMonths, 
      accounts.TotalDebitsMonth AS totalDebitsMonth, 
      accounts.RoutingNumber AS routingNumber, 
      accounts.SupportBillPay AS supportBillPay, 
      accounts.SupportCardlessCash AS supportCardlessCash, 
      accounts.SupportTransferFrom AS supportTransferFrom, 
      accounts.SupportTransferTo AS supportTransferTo, 
      accounts.SupportDeposit AS supportDeposit, 
      accounts.UnpaidInterest AS unpaidInterest, 
      accounts.PreviousYearsDividends AS previousYearsDividends, 
      accounts.principalBalance AS principalBalance, 
      accounts.PrincipalValue AS principalValue, 
      accounts.RegularPaymentAmount AS regularPaymentAmount, 
      accounts.phone AS phoneId, 
      accounts.LastDividendPaidDate AS lastDividendPaidDate, 
      accounts.LastDividendPaidAmount AS lastDividendPaidAmount, 
      accounts.LastPaymentAmount AS lastPaymentAmount, 
      accounts.LastPaymentDate AS lastPaymentDate, 
      accounts.LastStatementBalance AS lastStatementBalance, 
      accounts.LateFeesDue AS lateFeesDue, 
      accounts.maturityAmount AS maturityAmount, 
      accounts.MaturityOption AS maturityOption, 
      accounts.payoffAmount AS payoffAmount, 
      accounts.PayOffCharge AS payOffCharge, 
      accounts.PendingDeposit AS pendingDeposit, 
      accounts.PendingWithdrawal AS pendingWithdrawal, 
      accounts.JointHolders AS jointHolders, 
      accounts.IsPFM AS isPFM, 
      accounts.InterestPaidYTD AS interestPaidYTD, 
      accounts.InterestPaidPreviousYTD AS interestPaidPreviousYTD, 
      accounts.InterestPaidLastYear AS interestPaidLastYear, 
      accounts.InterestEarned AS interestEarned, 
      accounts.CurrentAmountDue AS currentAmountDue, 
      accounts.CreditLimit AS creditLimit, 
      accounts.CreditCardNumber AS creditCardNumber, 
      accounts.BsbNum AS bsbNum, 
      accounts.BondInterestLastYear AS bondInterestLastYear, 
      accounts.BondInterest AS bondInterest, 
      accounts.AvailablePoints AS availablePoints, 
      accounts.AccountName AS accountName, 
      accounts.email AS email, 
      accounts.IBAN AS IBAN, 
      accounts.adminProductId AS adminProductId, 
      bank.Description AS bankname, 
      accounts.AccountPreference AS accountPreference, 
      accounttype.transactionLimit AS transactionLimit, 
      accounttype.transferLimit AS transferLimit, 
      accounttype.rates AS rates, 
      accounttype.termsAndConditions AS termsAndConditions, 
      accounttype.TypeDescription AS typeDescription, 
      accounttype.supportChecks AS supportChecks, 
      accounttype.displayName AS displayName, 
      accounts.accountsubType AS accountsubType, 
      accounts.description AS description, 
      accounts.schemeName AS schemeName, 
      accounts.identification AS identification, 
      accounts.secondaryIdentification AS secondaryIdentification, 
      accounts.servicerSchemeName AS servicerSchemeName, 
      accounts.servicerIdentification AS servicerIdentification, 
      accounts.dataCreditDebitIndicator AS dataCreditDebitIndicator, 
      accounts.dataType AS dataType, 
      accounts.dataDateTime AS dataDateTime, 
      accounts.dataCreditLineIncluded AS dataCreditLineIncluded, 
      accounts.dataCreditLineType AS dataCreditLineType, 
      accounts.dataCreditLineAmount AS dataCreditLineAmount, 
      accounts.dataCreditLineCurrency AS dataCreditLineCurrency, 
      accounts.UpdatedBy AS UpdatedBy, 
      accounts.LastUpdated AS LastUpdated, 
      accounts.ActualUpdatedBY AS ActualUpdatedBY
   FROM (([${dbxschemaname}].accounts 
      CROSS JOIN [${dbxschemaname}].accounttype) 
      CROSS JOIN [${dbxschemaname}].bank)
   WHERE ((accounts.Type_id = CAST(accounttype.TypeID AS float(53))) AND (accounts.Bank_id = CAST(bank.id AS float(53))))

GO
/****** Object:  View [${dbxschemaname}].[group_features_actions_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[group_features_actions_view] (
   [Group_id], 
   [Action_id], 
   [LimitType_id], 
   [value], 
   [groupactionlimit_id], 
   [Type_id], 
   [Group_name], 
   [Group_description], 
   [Action_name], 
   [Action_description], 
   [Action_Type_id], 
   [Feature_id], 
   [isMFAApplicable], 
   [isAccountLevel], 
   [isPrimary], 
   [Action_displaysequence], 
   [Action_dependency], 
   [Feature_name], 
   [Feature_description], 
   [Feature_Type_id], 
   [Feature_Status_id], 
   [Feature_displaysequence], 
   [Feature_isPrimary])
AS 
   SELECT 
      groupactionlimit.Group_id AS Group_id, 
      groupactionlimit.Action_id AS Action_id, 
      groupactionlimit.LimitType_id AS LimitType_id, 
      groupactionlimit.value AS value, 
      groupactionlimit.id AS groupactionlimit_id, 
      membergroup.Type_id AS Type_id, 
      membergroup.Name AS Group_name, 
      membergroup.Description AS Group_description, 
      featureaction.name AS Action_name, 
      featureaction.description AS Action_description, 
      featureaction.Type_id AS Action_Type_id, 
      featureaction.Feature_id AS Feature_id, 
      featureaction.isMFAApplicable AS isMFAApplicable, 
      featureaction.isAccountLevel AS isAccountLevel, 
      featureaction.isPrimary AS isPrimary, 
      featureaction.DisplaySequence AS Action_displaysequence, 
      featureaction.dependency AS Action_dependency, 
      feature.name AS Feature_name, 
      feature.description AS Feature_description, 
      feature.Type_id AS Feature_Type_id, 
      feature.Status_id AS Feature_Status_id, 
      feature.DisplaySequence AS Feature_displaysequence, 
      feature.isPrimary AS Feature_isPrimary
   FROM ((([${dbxschemaname}].groupactionlimit 
      LEFT JOIN [${dbxschemaname}].membergroup 
      ON ((membergroup.id = groupactionlimit.Group_id))) 
      LEFT JOIN [${dbxschemaname}].featureaction 
      ON ((featureaction.id = groupactionlimit.Action_id))) 
      LEFT JOIN [${dbxschemaname}].feature 
      ON ((feature.id = featureaction.Feature_id)))
GO
/****** Object:  View [${dbxschemaname}].[groupentitlement_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[groupentitlement_view] (
   [Group_id], 
   [Service_id], 
   [Service_name], 
   [Service_description], 
   [Service_type_id], 
   [MaxDailyLimit], 
   [MaxTransferLimit], 
   [MinTransferLimit], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [Code])
AS 
   SELECT 
      groupentitlement.Group_id AS Group_id, 
      service.id AS Service_id, 
      service.Name AS Service_name, 
      service.Description AS Service_description, 
      service.Type_id AS Service_type_id, 
      periodiclimit.MaximumLimit AS MaxDailyLimit, 
      service.MaxTransferLimit AS MaxTransferLimit, 
      service.MinTransferLimit AS MinTransferLimit, 
      groupentitlement.TransactionFee_id AS TransactionFee_id, 
      groupentitlement.TransactionLimit_id AS TransactionLimit_id, 
      periodiclimit.Code AS Code
   FROM (([${dbxschemaname}].groupentitlement 
      LEFT JOIN [${dbxschemaname}].service 
      ON ((groupentitlement.Service_id = service.id))) 
      LEFT JOIN [${dbxschemaname}].periodiclimit 
      ON ((service.TransactionLimit_id = periodiclimit.TransactionLimit_id)))
GO
/****** Object:  View [${dbxschemaname}].[internal_role_to_customer_role_mapping_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[groups_view] (
   [Group_id], 
   [Type_id], 
   [Type_Name], 
   [Group_Desc], 
   [Status_id], 
   [Group_Name], 
   [isEAgreementActive], 
   [Entitlements_Count], 
   [Customers_Count], 
   [Status])
AS 
   SELECT 
      membergroup.id AS Group_id, 
      min(membergroup.[Type_id]) AS [Type_id], 
      min(customertype.[Name]) AS [Type_Name], 
      min(membergroup.[Description]) AS Group_Desc, 
      min(membergroup.Status_id) AS Status_id, 
      min(membergroup.[Name]) AS Group_Name, 
      (membergroup.isEAgreementActive) AS isEAgreementActive, 
      
         (
            SELECT COUNT(groupentitlement.Group_id)
            FROM [${dbxschemaname}].groupentitlement
            WHERE groupentitlement.Group_id = membergroup.id
         ) AS Entitlements_Count, 
      
         (
            SELECT COUNT(customergroup.Customer_id)
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Group_id = membergroup.id
         ) AS Customers_Count, 
      min(CASE (membergroup.Status_id)
         WHEN N'SID_ACTIVE' THEN N'Active'
         ELSE N'Inactive'
      END) AS Status
   FROM ([${dbxschemaname}].membergroup 
      INNER JOIN [${dbxschemaname}].customertype 
      ON ((membergroup.[Type_id] = customertype.id)))
	  GROUP BY  membergroup.id,membergroup.isEAgreementActive
GO
/****** Object:  View [${dbxschemaname}].[groupservices_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[groupservices_view] (
   [Group_id], 
   [Service_id], 
   [Service_name], 
   [Service_description], 
   [Service_notes], 
   [Status_id], 
   [Status_description], 
   [TransactionFee_id], 
   [TransactionFee_description], 
   [TransactionLimit_id], 
   [TransactionLimit_description], 
   [ServiceType_id], 
   [ServiceType_description], 
   [Channel_id], 
   [ChannelType_description], 
   [MinTransferLimit], 
   [MaxTransferLimit], 
   [Display_Name], 
   [Display_Description])
AS 
   SELECT 
      groupentitlement.Group_id AS Group_id, 
      groupentitlement.Service_id AS Service_id, 
      min([service].[Name]) AS [Service_name], 
      min([service].[Description]) AS Service_description, 
      min([service].Notes) AS Service_notes, 
      min([service].Status_id) AS Status_id, 
      
         (
            SELECT [status].[Description]
            FROM [${dbxschemaname}].[status]
            WHERE ([status].id = min([service].Status_id))
         ) AS Status_description, 
      min(groupentitlement.TransactionFee_id) AS TransactionFee_id, 
      
         (
            SELECT transactionfee.[Description]
            FROM [${dbxschemaname}].transactionfee
            WHERE (transactionfee.id = min(groupentitlement.TransactionFee_id))
         ) AS TransactionFee_description, 
      min(groupentitlement.TransactionLimit_id) AS TransactionLimit_id, 
      
         (
            SELECT transactionlimit.[Description]
            FROM [${dbxschemaname}].transactionlimit
            WHERE (transactionlimit.id = min(groupentitlement.TransactionLimit_id))
         ) AS TransactionLimit_description, 
      min([service].[Type_id]) AS ServiceType_id, 
      
         (
            SELECT servicetype.[Description]
            FROM [${dbxschemaname}].servicetype
            WHERE (servicetype.id = min([service].[Type_id]))
         ) AS ServiceType_description, 
      String_agg(CAST(sc.Channel_id as nvarchar(max)), ',') AS Channel_id, 
      String_agg(CAST(servicechannel.[Description] as nvarchar(max)) , ',') AS ChannelType_description, 
      min([service].MinTransferLimit) AS MinTransferLimit, 
      min([service].MaxTransferLimit) AS MaxTransferLimit, 
      min([service].DisplayName) AS Display_Name, 
      min([service].DisplayDescription) AS Display_Description
   FROM ((([${dbxschemaname}].groupentitlement 
      LEFT JOIN [${dbxschemaname}].[service] 
      ON ((groupentitlement.Service_id = [service].id))) 
      LEFT JOIN [${dbxschemaname}].service_channels  AS sc 
      ON ((sc.Service_id = [service].id))) 
      LEFT JOIN [${dbxschemaname}].servicechannel 
      ON ((sc.Channel_id = servicechannel.id)))
   GROUP BY groupentitlement.Group_id, groupentitlement.Service_id
GO
/****** Object:  View [${dbxschemaname}].[internal_role_to_customer_role_mapping_view]    Script Date: 6/9/2020 2:00:26 PM ******/
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
      role.Description AS InternalRole_Description
   FROM (([${dbxschemaname}].userrolecustomerrole 
      LEFT JOIN [${dbxschemaname}].membergroup 
      ON ((membergroup.id = userrolecustomerrole.CustomerRole_id))) 
      LEFT JOIN [${dbxschemaname}].role 
      ON ((role.id = userrolecustomerrole.UserRole_id)))
GO
/****** Object:  View [${dbxschemaname}].[internaluserdetails_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[internaluserdetails_view] (
   [id], 
   [Username], 
   [Email], 
   [Status_id], 
   [Password], 
   [Code], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [FailedCount], 
   [LastPasswordChangedts], 
   [ResetpasswordLink], 
   [ResetPasswordExpdts], 
   [lastLogints], 
   [createdby], 
   [createdts], 
   [modifiedby], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag], 
   [Role_id], 
   [hasSuperAdminPrivilages], 
   [Role_Name], 
   [Role_Status_id])
AS 
   SELECT 
      systemuser.id AS id, 
      systemuser.Username AS Username, 
      systemuser.Email AS Email, 
      systemuser.Status_id AS Status_id, 
      systemuser.Password AS Password, 
      systemuser.Code AS Code, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.FailedCount AS FailedCount, 
      systemuser.LastPasswordChangedts AS LastPasswordChangedts, 
      systemuser.ResetpasswordLink AS ResetpasswordLink, 
      systemuser.ResetPasswordExpdts AS ResetPasswordExpdts, 
      systemuser.lastLogints AS lastLogints, 
      systemuser.createdby AS createdby, 
      systemuser.createdts AS createdts, 
      systemuser.modifiedby AS modifiedby, 
      systemuser.lastmodifiedts AS lastmodifiedts, 
      systemuser.synctimestamp AS synctimestamp, 
      systemuser.softdeleteflag AS softdeleteflag, 
      userrole.Role_id AS Role_id, 
      userrole.hasSuperAdminPrivilages AS hasSuperAdminPrivilages, 
      role.Name AS Role_Name, 
      role.Status_id AS Role_Status_id
   FROM (([${dbxschemaname}].systemuser 
      LEFT JOIN [${dbxschemaname}].userrole 
      ON ((userrole.User_id = systemuser.id))) 
      LEFT JOIN [${dbxschemaname}].role 
      ON ((userrole.Role_id = role.id)))
GO
/****** Object:  View [${dbxschemaname}].[lead_csr_count_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
	CREATE VIEW [${dbxschemaname}].[internalusers_view] (
	   [User_id], 
	   [Status_id], 
	   [Status_Desc], 
	   [FirstName], 
	   [MiddleName], 
	   [LastName], 
	   [lastLogints], 
	   [Name], 
	   [Username], 
	   [Email], 
	   [Role_id], 
	   [Role_Desc], 
	   [Role_Name], 
	   [Permission_Count], 
	   [lastmodifiedts], 
	   [createdts], 
	   [Work_Addr], 
	   [Home_Addr], 
	   [Home_AddressID], 
	   [Home_AddressLine1], 
	   [Home_AddressLine2], 
	   [Home_CityName], 
	   [Home_CityID], 
	   [Home_StateName], 
	   [Home_StateID], 
	   [Home_CountryName], 
	   [Home_CountryID], 
	   [Home_Zipcode], 
	   [Work_AddressID], 
	   [Work_AddressLine1], 
	   [Work_AddressLine2], 
	   [Work_CityName], 
	   [Work_CityID], 
	   [Work_StateName], 
	   [Work_StateID], 
	   [Work_CountryName], 
	   [Work_CountryID], 
	   [Work_Zipcode])
	AS 
	SELECT (systemuser.id) AS [User_id], 
		  (systemuser.Status_id) AS Status_id, 
		  (status.Description) AS Status_Desc, 
		  (systemuser.FirstName) AS FirstName, 
		  (systemuser.MiddleName) AS MiddleName, 
		  (systemuser.LastName) AS LastName, 
		  (systemuser.lastLogints) AS lastLogints, 
		  (systemuser.FirstName) + ' ' + (systemuser.LastName) AS Name, 
		  (systemuser.Username) AS Username, 
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
			 ) AS Work_Zipcode
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
			 )))
GO
/****** Object:  View [${dbxschemaname}].[lead_csr_count_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[lead_csr_count_view] ([csrId], [count])
AS 
   SELECT TOP (9223372036854775807) lead.csr_id AS csrId, count_big(lead.id) AS count
   FROM [${dbxschemaname}].lead
   GROUP BY lead.csr_id
      ORDER BY lead.csr_id
GO
/****** Object:  View [${dbxschemaname}].[lead_status_count_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[lead_status_count_view] ([count], [leadStatus])
AS 
   SELECT tbl$1.count, tbl$1.leadStatus
   FROM 
      (
         SELECT TOP (9223372036854775807) count_big(le.id) AS count, s1.id AS leadStatus
         FROM ([${dbxschemaname}].status  AS s1 
            LEFT JOIN [${dbxschemaname}].lead  AS le 
            ON ((s1.id = le.status_id)))
         WHERE ((s1.Type_id = 'STID_LEADSTATUS') AND (s1.id <> 'SID_ARCHIVED'))
         GROUP BY s1.id
            ORDER BY s1.id
      )  AS tbl$1
    UNION
   SELECT TOP (9223372036854775807) count_big(ale.id) AS count, s2.id AS leadStatus
   FROM ([${dbxschemaname}].status  AS s2 
      LEFT JOIN [${dbxschemaname}].archivedlead  AS ale 
      ON ((s2.id = ale.status_id)))
   WHERE ((s2.Type_id = 'STID_LEADSTATUS') AND (s2.id = 'SID_ARCHIVED'))
   GROUP BY s2.id
      ORDER BY s2.id
GO
/****** Object:  View [${dbxschemaname}].[location_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

/****** Object:  View [${dbxschemaname}].[location_view]    Script Date: 6/9/2020 2:00:26 PM ******/
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
   [Status_id])
AS 
   SELECT DISTINCT 
      location.id AS id, 
      location.Name AS Name, 
      location.Code AS Code, 
      location.Description AS Description, 
      location.PhoneNumber AS PhoneNumber, 
      location.Type_id AS Type_id, 
      CASE location.Status_id
         WHEN N'SID_ACTIVE' THEN N'Active'
         ELSE N'Inactive'
      END AS Status_id
   FROM [${dbxschemaname}].location
GO
/****** Object:  View [${dbxschemaname}].[locationfacility_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[locationdetails_view]
      AS 
         SELECT DISTINCT 
            
               location.id AS locationId, 
               
                  (
                     SELECT DISTINCT
                        string_agg( 
                           upper(left(dayschedule.WeekDayName, 1))+
                           lower(substring(dayschedule.WeekDayName,0, 2)) +
                           ':'+
                           CONVERT(varchar(max),dayschedule.StartTime)+
                           '-'+
                           CONVERT(varchar(max),dayschedule.EndTime),'||' )
                     FROM ([${dbxschemaname}].dayschedule 
                        JOIN [${dbxschemaname}].location l
                     on dayschedule.WorkSchedule_id = l.WorkSchedule_id))
                   AS workingHours, 
               location.Name AS informationTitle,
			   location.Name AS name,
               location.Description AS Description, 
               location.Code AS Code, 
               location.PhoneNumber AS phoneNumber, 
               location.EmailId AS email, 
               (
                  CASE 
                     location.Status_id
                        WHEN 'SID_ACTIVE' THEN 'OPEN'
                        ELSE 'CLOSED'
                  END) AS status, 
				location.Status_id AS isVisible,
               location.Type_id AS type, 
               location.isMobile AS isMobile, 
               location.IsMainBranch AS IsMainBranch, 
               
                  (
                     SELECT 
                        string_agg(service.Name, '||')
                     FROM [${dbxschemaname}].service 
                        JOIN [${dbxschemaname}].locationservice
                     on ((service.id = locationservice.Service_id) AND (locationservice.Location_id = location.id))
                  ) AS services, 
               
                  (
                     SELECT 
                        string_agg(currency.code , ',')
                     FROM [${dbxschemaname}].currency 
                        JOIN [${dbxschemaname}].locationcurrency
                     on ((currency.code = CONVERT(varchar,locationcurrency.currency_code)) AND (CONVERT(varchar,locationcurrency.Location_id)  = location.id))
                  ) AS currencies, 
               
                  (
                     SELECT 
                        string_agg(CAST(customersegment.type as nvarchar(max)) , ',')
                     FROM [${dbxschemaname}].customersegment 
                        JOIN [${dbxschemaname}].locationcustomersegment
                     on ((customersegment.id = CONVERT(varchar,locationcustomersegment.segment_id )) AND (CONVERT(varchar,locationcustomersegment.Location_id) = location.id))
                  ) AS segments, 
               
                  (
                     SELECT 
                       string_agg(CAST(facility.id as nvarchar(max)) , ',')
                     FROM [${dbxschemaname}].facility 
                        JOIN [${dbxschemaname}].locationfacility
                     on ((facility.id = CONVERT(varchar,locationfacility.facility_id)) AND (CONVERT(varchar,locationfacility.Location_id ) = location.id))
                  ) AS facilities_names, 
               
                  (
                     SELECT 
                        string_agg(facility.id ,',')
                     FROM [${dbxschemaname}].facility 
                        JOIN [${dbxschemaname}].locationfacility
                     on ((facility.id = CONVERT(varchar,locationfacility.facility_id )) AND (CONVERT(varchar,locationfacility.Location_id) = location.id))
                  ) AS facilities_codes, 
               
                  (
                     SELECT 
                        city.Name
                     FROM [${dbxschemaname}].city
                     WHERE (city.id = address.City_id)
                  ) AS city, 
               
                  (
                     SELECT 
                        region.Name
                     FROM [${dbxschemaname}].region
                     WHERE (region.id = address.Region_id)
                  ) AS region, 
               
                  (
                     SELECT 
                        country.Name
                     FROM [${dbxschemaname}].country
                     WHERE (country.id = 
                        (
                           SELECT 
                              region.Country_id
             
                           WHERE (region.id = address.Region_id)
                        ))
                  ) AS country, 
               address.addressLine1 AS addressLine1, 
               address.addressLine2 AS addressLine2, 
               address.addressLine3 AS addressLine3, 
               address.zipCode AS zipCode, 
               address.latitude AS latitude, 
               address.logitude AS longitude
         FROM [${dbxschemaname}].address 
            INNER JOIN [${dbxschemaname}].location on
			 address.id = location.Address_id
            INNER JOIN [${dbxschemaname}].region 
         on region.id = address.Region_id
GO
/****** Object:  View [${dbxschemaname}].[locationfacility_view]    Script Date: 6/9/2020 2:00:26 PM ******/
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
   [Facility_facilitytype])
AS 
   SELECT 
      location.id AS Location_id, 
      facility.id AS Facility_id, 
      facility.code AS Facility_code, 
      facility.name AS Facility_Name, 
      facility.description AS Facility_description, 
      facility.facilitytype AS Facility_facilitytype
   FROM (([${dbxschemaname}].location 
      LEFT JOIN [${dbxschemaname}].locationfacility 
      ON ((location.id = locationfacility.Location_id))) 
      INNER JOIN [${dbxschemaname}].facility 
      ON ((locationfacility.facility_id = facility.id)))
GO
/****** Object:  View [${dbxschemaname}].[notificationview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
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
            JOIN [${dbxschemaname}].address ON address.id = location.Address_id
GO
/****** Object:  View [${dbxschemaname}].[memtinaccountsview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[memtinaccountsview] (
   [Account_Type], 
   [Customer_id], 
   [Account_id], 
   [accountName], 
   [Organization_Id], 
   [Membership_id], 
   [Taxid])
AS 
   SELECT DISTINCT 
      accounttype.TypeDescription AS Account_Type, 
      customeraccounts.Customer_id AS Customer_id, 
      customeraccounts.Account_id AS Account_id, 
      customeraccounts.AccountName AS accountName, 
      organisationmembership.Organization_id AS Organization_Id, 
      organisationmembership.Membership_id AS Membership_id, 
      organisationmembership.Taxid AS Taxid
   FROM (([${dbxschemaname}].accounttype 
      CROSS JOIN [${dbxschemaname}].accounts) 
      CROSS JOIN (([${dbxschemaname}].customeraccounts 
      CROSS JOIN [${dbxschemaname}].organisationemployees) 
      CROSS JOIN [${dbxschemaname}].organisationmembership))
   WHERE (
      (CAST(accounttype.TypeID AS float(53)) = accounts.Type_id) AND 
      (accounts.Account_id = CAST(customeraccounts.Account_id AS float(53))) AND 
      (customeraccounts.Membership_id = organisationmembership.Membership_id))

GO
/****** Object:  View [${dbxschemaname}].[messageview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[messageview] (
   [id], 
   [Account_id], 
   [accountName], 
   [nickName], 
   [Category_id], 
   [category], 
   [Subcategory_id], 
   [subcategory], 
   [subject], 
   [message], 
   [sentDate], 
   [status], 
   [isSoftDeleted], 
   [isRead], 
   [createdDate], 
   [receivedDate], 
   [softdeletedDate], 
   [User_id])
AS 
   SELECT 
      msg.id AS id, 
      msg.Account_id AS Account_id, 
      acnts.AccountName AS accountName, 
      acnts.NickName AS nickName, 
      msg.Category_id AS Category_id, 
      msgcategory.category AS category, 
      msg.Subcategory_id AS Subcategory_id, 
      msgsubcategory.subcategory AS subcategory, 
      msg.subject AS subject, 
      msg.message AS message, 
      msg.sentDate AS sentDate, 
      msg.status AS status, 
      msg.isSoftDeleted AS isSoftDeleted, 
      msg.isRead AS isRead, 
      msg.createdDate AS createdDate, 
      msg.receivedDate AS receivedDate, 
      msg.softdeletedDate AS softdeletedDate, 
      acnts.User_id AS User_id
   FROM ((([${dbxschemaname}].message  AS msg 
      CROSS JOIN [${dbxschemaname}].accounts  AS acnts) 
      CROSS JOIN [${dbxschemaname}].messagecategory  AS msgcategory) 
      CROSS JOIN [${dbxschemaname}].messagesubcategory  AS msgsubcategory)
   WHERE (
      (msg.Account_id = acnts.Account_id) AND 
      (msgcategory.Id = msg.Category_id) AND 
      (msgsubcategory.Id = msg.Subcategory_id))

GO
/****** Object:  View [${dbxschemaname}].[notificationview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[notificationview] (
   [notificationId], 
   [imageURL], 
   [isRead], 
   [notificationActionLink], 
   [notificationModule], 
   [notificationSubject], 
   [notificationSubModule], 
   [notificationText], 
   [receivedDate], 
   [userNotificationId], 
   [user_id], 
   [notificationCategory], 
   [actionButtonLabelName])
AS 
   SELECT 
      notification.notificationId AS notificationId, 
      notification.imageURL AS imageURL, 
      usernotification.isRead AS isRead, 
      notification.notificationActionLink AS notificationActionLink, 
      notification.notificationModule AS notificationModule, 
      notification.notificationSubject AS notificationSubject, 
      notification.notificationSubModule AS notificationSubModule, 
      notification.notificationText AS notificationText, 
      usernotification.receivedDate AS receivedDate, 
      usernotification.id AS userNotificationId, 
      usernotification.user_id AS user_id, 
      notification.notificationCategory AS notificationCategory, 
      notification.actionButtonLabelName AS actionButtonLabelName
   FROM ([${dbxschemaname}].usernotification 
      INNER JOIN [${dbxschemaname}].notification 
      ON ((notification.notificationId = usernotification.notification_id)))
GO
/****** Object:  View [${dbxschemaname}].[organisation_action_limits_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[organisation_action_limits_view] (
   [id], 
   [Organisation_id], 
   [Action_id], 
   [LimitType_id], 
   [value], 
   [isAccountLevel])
AS 
   SELECT 
      organisationactionlimit.id AS id, 
      organisationactionlimit.Organisation_id AS Organisation_id, 
      organisationactionlimit.Action_id AS Action_id, 
      organisationactionlimit.LimitType_id AS LimitType_id, 
      organisationactionlimit.value AS value, 
      featureaction.isAccountLevel AS isAccountLevel
   FROM ([${dbxschemaname}].organisationactionlimit 
      LEFT JOIN [${dbxschemaname}].featureaction 
      ON ((featureaction.id = organisationactionlimit.Action_id)))
GO
/****** Object:  View [${dbxschemaname}].[organisationemployeesview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[organisationemployeesview] (
   [orgemp_id], 
   [orgemp_orgid], 
   [orgemp_cusid], 
   [isAuthSignatory], 
   [isOwner], 
   [customer_id], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [UserName], 
   [DrivingLicenseNumber], 
   [DateOfBirth], 
   [Ssn], 
   [custcomm_id], 
   [custcomm_typeid], 
   [custcomm_custid], 
   [custcomm_value], 
   [custcomm_istypebusiness], 
   [Status_id], 
   [createdts], 
   [Lastlogintime], 
   [Group_id], 
   [role_name], 
   [createdby], 
   [signatorytypeId], 
   [signatorytypeName])
AS 
   SELECT 
      organisationemployees.id AS orgemp_id, 
      organisationemployees.Organization_id AS orgemp_orgid, 
      organisationemployees.Customer_id AS orgemp_cusid, 
      organisationemployees.isAuthSignatory AS isAuthSignatory, 
      organisationemployees.Is_Admin AS isOwner, 
      customer.id AS customer_id, 
      customer.FirstName AS FirstName, 
      customer.MiddleName AS MiddleName, 
      customer.LastName AS LastName, 
      customer.UserName AS UserName, 
      customer.DrivingLicenseNumber AS DrivingLicenseNumber, 
      customer.DateOfBirth AS DateOfBirth, 
      customer.Ssn AS Ssn, 
      customercommunication.id AS custcomm_id, 
      customercommunication.Type_id AS custcomm_typeid, 
      customercommunication.Customer_id AS custcomm_custid, 
      customercommunication.Value AS custcomm_value, 
      customercommunication.isTypeBusiness AS custcomm_istypebusiness, 
      customer.Status_id AS Status_id, 
      customer.createdts AS createdts, 
      customer.Lastlogintime AS Lastlogintime, 
      customergroup.Group_id AS Group_id, 
      membergroup.Name AS role_name, 
      customer.createdby AS createdby, 
      signatorytype.id AS signatorytypeId, 
      signatorytype.name AS signatorytypeName
   FROM (((((([${dbxschemaname}].organisationemployees 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((organisationemployees.Customer_id = customer.id))) 
      LEFT JOIN [${dbxschemaname}].customercommunication 
      ON ((customer.id = customercommunication.Customer_id))) 
      LEFT JOIN [${dbxschemaname}].customergroup 
      ON ((customer.id = customergroup.Customer_id))) 
      LEFT JOIN [${dbxschemaname}].customerbusinesstype 
      ON ((customerbusinesstype.Customer_id = customer.id))) 
      LEFT JOIN [${dbxschemaname}].signatorytype 
      ON ((signatorytype.id = customerbusinesstype.SignatoryType_id))) 
      INNER JOIN [${dbxschemaname}].membergroup 
      ON (((membergroup.id = customergroup.Group_id) AND (membergroup.Type_id = 'TYPE_ID_BUSINESS'))))
GO
/****** Object:  View [${dbxschemaname}].[organisationview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[organisationview] (
   [org_id], 
   [org_Name], 
   [org_typeId], 
   [org_status], 
   [org_faxid], 
   [orgcomm_Value], 
   [orgmem_memid], 
   [orgmem_taxid], 
   [cityName], 
   [addressLine1], 
   [addressLine2], 
   [zipCode], 
   [addressId], 
   [orgown_firstName], 
   [orgown_midleName], 
   [orgown_lastName], 
   [orgown_dob], 
   [orgown_ssn], 
   [orgown_email], 
   [orgown_phone], 
   [State], 
   [Country], 
   [TypeName], 
   [IsPrimary], 
   [businessType], 
   [businessTypeId])
AS 
   SELECT 
      organisation.id AS org_id, 
      organisation.Name AS org_Name, 
      organisation.Type_Id AS org_typeId, 
      organisation.StatusId AS org_status, 
      organisation.FaxId AS org_faxid, 
      organisationcommunication.Value AS orgcomm_Value, 
      samplemember.Membership_id AS orgmem_memid, 
      samplemember.Taxid AS orgmem_taxid, 
      address.cityName AS cityName, 
      address.addressLine1 AS addressLine1, 
      address.addressLine2 AS addressLine2, 
      address.zipCode AS zipCode, 
      address.id AS addressId, 
      sampleowner.FirstName AS orgown_firstName, 
      sampleowner.MidleName AS orgown_midleName, 
      sampleowner.LastName AS orgown_lastName, 
      sampleowner.DateOfBirth AS orgown_dob, 
      sampleowner.Ssn AS orgown_ssn, 
      sampleowner.Email AS orgown_email, 
      sampleowner.Phone AS orgown_phone, 
      address.state AS State, 
      address.country AS Country, 
      customertype.Name AS TypeName, 
      organisationaddress.IsPrimary AS IsPrimary, 
      businesstype.name AS businessType, 
      businesstype.id AS businessTypeId
   FROM ((((((([${dbxschemaname}].organisation 
      LEFT JOIN [${dbxschemaname}].organisationcommunication 
      ON ((organisation.id = organisationcommunication.Organization_id))) 
      LEFT JOIN [${dbxschemaname}].organisationaddress 
      ON ((organisation.id = organisationaddress.Organization_id))) 
      LEFT JOIN [${dbxschemaname}].address 
      ON ((organisationaddress.Address_id = address.id))) 
      LEFT JOIN [${dbxschemaname}].customertype 
      ON ((organisation.Type_Id = customertype.id))) 
      LEFT JOIN [${dbxschemaname}].organisationmembership  AS samplemember 
      ON ((organisation.id = samplemember.Organization_id))) 
      LEFT JOIN [${dbxschemaname}].organisationowner  AS sampleowner 
      ON ((sampleowner.Organization_id = organisation.id))) 
      LEFT JOIN [${dbxschemaname}].businesstype 
      ON ((businesstype.id = organisation.BusinessType_id)))
GO
/****** Object:  View [${dbxschemaname}].[organizationownerview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[organizationownerview] (
   [LastName], 
   [DateOfBirth], 
   [Ssn], 
   [Phone], 
   [Email], 
   [FirstName], 
   [IDType_id], 
   [IdValue], 
   [Organization_Id], 
   [Membership_id], 
   [Taxid])
AS 
   SELECT DISTINCT 
      organisationowner.LastName AS LastName, 
      organisationowner.DateOfBirth AS DateOfBirth, 
      organisationowner.Ssn AS Ssn, 
      organisationowner.Phone AS Phone, 
      organisationowner.Email AS Email, 
      organisationowner.FirstName AS FirstName, 
      organisationowner.IDType_id AS IDType_id, 
      organisationowner.IdValue AS IdValue, 
      organisationowner.Organization_id AS Organization_Id, 
      organisationmembership.Membership_id AS Membership_id, 
      organisationmembership.Taxid AS Taxid
   FROM ([${dbxschemaname}].organisationowner 
      CROSS JOIN [${dbxschemaname}].organisationmembership)
   WHERE (organisationowner.Organization_id = organisationmembership.Organization_id)
GO
/****** Object:  View [${dbxschemaname}].[orgemployeedetails]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[orgemployeedetails] (
   [id], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [Username], 
   [Gender], 
   [DateOfBirth], 
   [DrivingLicenseNumber], 
   [Ssn], 
   [UserCompany], 
   [Lastlogintime], 
   [Status], 
   [Account_id], 
   [AccountName], 
   [createdby])
AS 
   /*
   *   SSMA informational messages:
   *   M2SS0052: string literal was converted to NUMERIC literal
   */

   SELECT 
      c.id AS id, 
      c.FirstName AS FirstName, 
      c.MiddleName AS MiddleName, 
      c.LastName AS LastName, 
      c.UserName AS Username, 
      c.Gender AS Gender, 
      c.DateOfBirth AS DateOfBirth, 
      c.DrivingLicenseNumber AS DrivingLicenseNumber, 
      c.Ssn AS Ssn, 
      c.UserCompany AS UserCompany, 
      c.Lastlogintime AS Lastlogintime, 
      c.Status_id AS Status, 
      ca.Account_id AS Account_id, 
      ca.AccountName AS AccountName, 
      c.createdby AS createdby
   FROM ([${dbxschemaname}].customer  AS c 
      CROSS JOIN [${dbxschemaname}].customeraccounts  AS ca)
   WHERE ((c.id = ca.Customer_id) AND (ca.IsOrganizationAccount = 1))
GO
/****** Object:  View [${dbxschemaname}].[overallpaymentlimits_view]    Script Date: 7/28/2020 3:42:08 PM ******/
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
   [lastmodifiedts])
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
      outagemessage.lastmodifiedts AS lastmodifiedts
   FROM [${dbxschemaname}].outagemessage
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[overallpaymentlimits_view] (
   [TransactionGroupId], 
   [TransactionGroupName], 
   [TransactionGroupServiceId], 
   [ServiceId], 
   [ServiceName], 
   [Status_id], 
   [TransactionLimitId], 
   [PeriodicLimitId], 
   [Period_id], 
   [MaximumLimit], 
   [Currency], 
   [PeriodName], 
   [DayCount], 
   [Order])
AS 
   SELECT 
      transactiongroup.id AS TransactionGroupId, 
      transactiongroup.Name AS TransactionGroupName, 
      transactiongroupservice.id AS TransactionGroupServiceId, 
      transactiongroupservice.Service_id AS ServiceId, 
      service.Name AS ServiceName, 
      transactiongroup.Status_id AS Status_id, 
      periodiclimit.TransactionLimit_id AS TransactionLimitId, 
      periodiclimit.id AS PeriodicLimitId, 
      periodiclimit.Period_id AS Period_id, 
      periodiclimit.MaximumLimit AS MaximumLimit, 
      periodiclimit.Currency AS Currency, 
      period.Name AS PeriodName, 
      period.DayCount AS DayCount, 
      period.[Order] AS [Order]
   FROM (((([${dbxschemaname}].transactiongroup 
      INNER JOIN [${dbxschemaname}].transactiongroupservice 
      ON ((transactiongroupservice.TransactionGroup_id = transactiongroup.id))) 
      INNER JOIN [${dbxschemaname}].periodiclimit 
      ON ((periodiclimit.TransactionLimit_id = transactiongroup.TransactionLimit_id))) 
      INNER JOIN [${dbxschemaname}].period 
      ON ((periodiclimit.Period_id = period.id))) 
      INNER JOIN [${dbxschemaname}].service 
      ON ((service.id = transactiongroupservice.Service_id)))
GO
/****** Object:  View [${dbxschemaname}].[periodiclimitenduser_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[payeetransactionview] (
   [Id], 
   [isScheduled], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Payee_id], 
   [Bill_id], 
   [Type_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [notes], 
   [checkNumber], 
   [imageURL1], 
   [imageURL2], 
   [hasDepositImage], 
   [description], 
   [scheduledDate], 
   [transactionDate], 
   [createdDate], 
   [transactionComments], 
   [toExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [numberOfRecurrences], 
   [frequencyStartDate], 
   [frequencyEndDate], 
   [checkImage], 
   [checkImageBack], 
   [cashlessOTPValidDate], 
   [cashlessOTP], 
   [cashlessPhone], 
   [cashlessEmail], 
   [cashlessPersonName], 
   [cashlessMode], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [cashlessPin], 
   [category], 
   [billCategory], 
   [recurrenceDesc], 
   [deliverBy], 
   [p2pContact], 
   [p2pRequiredDate], 
   [requestCreatedDate], 
   [penaltyFlag], 
   [payoffFlag], 
   [viewReportLink], 
   [isPaypersonDeleted], 
   [fee], 
   [payeeId], 
   [payeeType], 
   [name], 
   [nickName], 
   [phone], 
   [email], 
   [accountNumber], 
   [billerId], 
   [billermaster_id], 
   [softDelete], 
   [User_Id], 
   [addressLine1], 
   [addressLine2], 
   [eBillEnable], 
   [phoneExtension], 
   [phoneCountryCode], 
   [ebillSupport], 
   [transactionType])
AS 
   SELECT 
      t.Id AS Id, 
      t.isScheduled AS isScheduled, 
      t.Customer_id AS Customer_id, 
      t.ExpenseCategory_id AS ExpenseCategory_id, 
      t.Payee_id AS Payee_id, 
      t.Bill_id AS Bill_id, 
      t.Type_id AS Type_id, 
      t.Reference_id AS Reference_id, 
      t.fromAccountNumber AS fromAccountNumber, 
      t.fromAccountBalance AS fromAccountBalance, 
      t.toAccountNumber AS toAccountNumber, 
      t.toAccountBalance AS toAccountBalance, 
      t.amount AS amount, 
      t.Status_id AS Status_id, 
      t.statusDesc AS statusDesc, 
      t.notes AS notes, 
      t.checkNumber AS checkNumber, 
      t.imageURL1 AS imageURL1, 
      t.imageURL2 AS imageURL2, 
      t.hasDepositImage AS hasDepositImage, 
      t.description AS description, 
      t.scheduledDate AS scheduledDate, 
      t.transactionDate AS transactionDate, 
      t.createdDate AS createdDate, 
      t.transactionComments AS transactionComments, 
      t.toExternalAccountNumber AS toExternalAccountNumber, 
      t.Person_Id AS Person_Id, 
      t.frequencyType AS frequencyType, 
      t.numberOfRecurrences AS numberOfRecurrences, 
      t.frequencyStartDate AS frequencyStartDate, 
      t.frequencyEndDate AS frequencyEndDate, 
      t.checkImage AS checkImage, 
      t.checkImageBack AS checkImageBack, 
      t.cashlessOTPValidDate AS cashlessOTPValidDate, 
      t.cashlessOTP AS cashlessOTP, 
      t.cashlessPhone AS cashlessPhone, 
      t.cashlessEmail AS cashlessEmail, 
      t.cashlessPersonName AS cashlessPersonName, 
      t.cashlessMode AS cashlessMode, 
      t.cashlessSecurityCode AS cashlessSecurityCode, 
      t.cashWithdrawalTransactionStatus AS cashWithdrawalTransactionStatus, 
      t.cashlessPin AS cashlessPin, 
      t.category AS category, 
      t.billCategory AS billCategory, 
      t.recurrenceDesc AS recurrenceDesc, 
      t.deliverBy AS deliverBy, 
      t.p2pContact AS p2pContact, 
      t.p2pRequiredDate AS p2pRequiredDate, 
      t.requestCreatedDate AS requestCreatedDate, 
      t.penaltyFlag AS penaltyFlag, 
      t.payoffFlag AS payoffFlag, 
      t.viewReportLink AS viewReportLink, 
      t.isPaypersonDeleted AS isPaypersonDeleted, 
      t.fee AS fee, 
      p.Id AS payeeId, 
      p.Type_id AS payeeType, 
      p.name AS name, 
      p.nickName AS nickName, 
      p.phone AS phone, 
      p.email AS email, 
      p.accountNumber AS accountNumber, 
      p.billerId AS billerId, 
      p.billermaster_id AS billermaster_id, 
      p.softDelete AS softDelete, 
      p.User_Id AS User_Id, 
      p.addressLine1 AS addressLine1, 
      p.addressLine2 AS addressLine2, 
      p.eBillEnable AS eBillEnable, 
      p.phoneExtension AS phoneExtension, 
      p.phoneCountryCode AS phoneCountryCode, 
      bm.ebillSupport AS ebillSupport, 
      tt.description AS transactionType
   FROM ((([${dbxschemaname}].[transaction]  AS t 
      LEFT JOIN [${dbxschemaname}].payee  AS p 
      ON ((t.Payee_id = p.Id))) 
      INNER JOIN [${dbxschemaname}].transactiontype  AS tt 
      ON ((t.Type_id = tt.Id))) 
      LEFT JOIN [${dbxschemaname}].billermaster  AS bm 
      ON ((bm.id = p.billermaster_id)))

GO
/****** Object:  View [${dbxschemaname}].[paypersontransactionview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[paypersontransactionview] (
   [Id], 
   [isScheduled], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Payee_id], 
   [Bill_id], 
   [Type_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [notes], 
   [checkNumber], 
   [imageURL1], 
   [imageURL2], 
   [hasDepositImage], 
   [description], 
   [scheduledDate], 
   [transactionDate], 
   [createdDate], 
   [transactionComments], 
   [toExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [numberOfRecurrences], 
   [frequencyStartDate], 
   [frequencyEndDate], 
   [checkImage], 
   [checkImageBack], 
   [cashlessOTPValidDate], 
   [cashlessOTP], 
   [cashlessPhone], 
   [cashlessEmail], 
   [cashlessPersonName], 
   [cashlessMode], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [cashlessPin], 
   [category], 
   [billCategory], 
   [recurrenceDesc], 
   [deliverBy], 
   [p2pContact], 
   [p2pRequiredDate], 
   [requestCreatedDate], 
   [penaltyFlag], 
   [payoffFlag], 
   [viewReportLink], 
   [isPaypersonDeleted], 
   [fee], 
   [paypersonID], 
   [firstName], 
   [lastName], 
   [phone], 
   [email], 
   [User_id], 
   [secondaryEmail], 
   [secondoryPhoneNumber], 
   [primaryContactForSending], 
   [nickName], 
   [isSoftDelete], 
   [transactionType])
AS 
   SELECT 
      t.Id AS Id, 
      t.isScheduled AS isScheduled, 
      t.Customer_id AS Customer_id, 
      t.ExpenseCategory_id AS ExpenseCategory_id, 
      t.Payee_id AS Payee_id, 
      t.Bill_id AS Bill_id, 
      t.Type_id AS Type_id, 
      t.Reference_id AS Reference_id, 
      t.fromAccountNumber AS fromAccountNumber, 
      t.fromAccountBalance AS fromAccountBalance, 
      t.toAccountNumber AS toAccountNumber, 
      t.toAccountBalance AS toAccountBalance, 
      t.amount AS amount, 
      t.Status_id AS Status_id, 
      t.statusDesc AS statusDesc, 
      t.notes AS notes, 
      t.checkNumber AS checkNumber, 
      t.imageURL1 AS imageURL1, 
      t.imageURL2 AS imageURL2, 
      t.hasDepositImage AS hasDepositImage, 
      t.description AS description, 
      t.scheduledDate AS scheduledDate, 
      t.transactionDate AS transactionDate, 
      t.createdDate AS createdDate, 
      t.transactionComments AS transactionComments, 
      t.toExternalAccountNumber AS toExternalAccountNumber, 
      t.Person_Id AS Person_Id, 
      t.frequencyType AS frequencyType, 
      t.numberOfRecurrences AS numberOfRecurrences, 
      t.frequencyStartDate AS frequencyStartDate, 
      t.frequencyEndDate AS frequencyEndDate, 
      t.checkImage AS checkImage, 
      t.checkImageBack AS checkImageBack, 
      t.cashlessOTPValidDate AS cashlessOTPValidDate, 
      t.cashlessOTP AS cashlessOTP, 
      t.cashlessPhone AS cashlessPhone, 
      t.cashlessEmail AS cashlessEmail, 
      t.cashlessPersonName AS cashlessPersonName, 
      t.cashlessMode AS cashlessMode, 
      t.cashlessSecurityCode AS cashlessSecurityCode, 
      t.cashWithdrawalTransactionStatus AS cashWithdrawalTransactionStatus, 
      t.cashlessPin AS cashlessPin, 
      t.category AS category, 
      t.billCategory AS billCategory, 
      t.recurrenceDesc AS recurrenceDesc, 
      t.deliverBy AS deliverBy, 
      t.p2pContact AS p2pContact, 
      t.p2pRequiredDate AS p2pRequiredDate, 
      t.requestCreatedDate AS requestCreatedDate, 
      t.penaltyFlag AS penaltyFlag, 
      t.payoffFlag AS payoffFlag, 
      t.viewReportLink AS viewReportLink, 
      t.isPaypersonDeleted AS isPaypersonDeleted, 
      t.fee AS fee, 
      p.id AS paypersonID, 
      p.firstName AS firstName, 
      p.lastName AS lastName, 
      p.phone AS phone, 
      p.email AS email, 
      p.User_id AS User_id, 
      p.secondaryEmail AS secondaryEmail, 
      p.secondoryPhoneNumber AS secondoryPhoneNumber, 
      p.primaryContactForSending AS primaryContactForSending, 
      p.nickName AS nickName, 
      p.isSoftDelete AS isSoftDelete, 
      tt.description AS transactionType
   FROM (([${dbxschemaname}].[transaction]  AS t 
      LEFT JOIN [${dbxschemaname}].payperson  AS p 
      ON ((t.Person_Id = p.id))) 
      INNER JOIN [${dbxschemaname}].transactiontype  AS tt 
      ON ((t.Type_id = tt.Id)))

GO
/****** Object:  View [${dbxschemaname}].[periodiclimitenduser_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[periodiclimitenduser_view] (
   [Customer_id], 
   [Service_id], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [PeriodLimit_id], 
   [Period_id], 
   [Period_Name], 
   [MaximumLimit], 
   [Code], 
   [Currency])
AS 
   SELECT 
      customerentitlement.Customer_id AS Customer_id, 
      customerentitlement.Service_id AS Service_id, 
      customerentitlement.TransactionFee_id AS TransactionFee_id, 
      customerentitlement.TransactionLimit_id AS TransactionLimit_id, 
      periodiclimit.id AS PeriodLimit_id, 
      periodiclimit.Period_id AS Period_id, 
      period.Name AS Period_Name, 
      periodiclimit.MaximumLimit AS MaximumLimit, 
      periodiclimit.Code AS Code, 
      periodiclimit.Currency AS Currency
   FROM (([${dbxschemaname}].periodiclimit 
      INNER JOIN [${dbxschemaname}].customerentitlement 
      ON ((customerentitlement.TransactionLimit_id = periodiclimit.TransactionLimit_id))) 
      INNER JOIN [${dbxschemaname}].period 
      ON ((periodiclimit.Period_id = period.id)))
GO
/****** Object:  View [${dbxschemaname}].[periodiclimitservice_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[periodiclimitservice_view] (
   [Service_id], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [PeriodLimit_id], 
   [Period_id], 
   [Period_Name], 
   [MaximumLimit], 
   [Code], 
   [Currency])
AS 
   SELECT 
      service.id AS Service_id, 
      service.TransactionFee_id AS TransactionFee_id, 
      service.TransactionLimit_id AS TransactionLimit_id, 
      periodiclimit.id AS PeriodLimit_id, 
      periodiclimit.Period_id AS Period_id, 
      period.Name AS Period_Name, 
      periodiclimit.MaximumLimit AS MaximumLimit, 
      periodiclimit.Code AS Code, 
      periodiclimit.Currency AS Currency
   FROM (([${dbxschemaname}].periodiclimit 
      INNER JOIN [${dbxschemaname}].service 
      ON ((service.TransactionLimit_id = periodiclimit.TransactionLimit_id))) 
      INNER JOIN [${dbxschemaname}].period 
      ON ((periodiclimit.Period_id = period.id)))
GO
/****** Object:  View [${dbxschemaname}].[periodiclimitusergroup_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[periodiclimitusergroup_view] (
   [Group_id], 
   [Service_id], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [PeriodLimit_id], 
   [Period_id], 
   [Period_Name], 
   [MaximumLimit], 
   [Code], 
   [Currency])
AS 
   SELECT 
      groupentitlement.Group_id AS Group_id, 
      groupentitlement.Service_id AS Service_id, 
      groupentitlement.TransactionFee_id AS TransactionFee_id, 
      groupentitlement.TransactionLimit_id AS TransactionLimit_id, 
      periodiclimit.id AS PeriodLimit_id, 
      periodiclimit.Period_id AS Period_id, 
      period.Name AS Period_Name, 
      periodiclimit.MaximumLimit AS MaximumLimit, 
      periodiclimit.Code AS Code, 
      periodiclimit.Currency AS Currency
   FROM (([${dbxschemaname}].periodiclimit 
      INNER JOIN [${dbxschemaname}].groupentitlement 
      ON ((groupentitlement.TransactionLimit_id = periodiclimit.TransactionLimit_id))) 
      INNER JOIN [${dbxschemaname}].period 
      ON ((periodiclimit.Period_id = period.id)))
GO
/****** Object:  View [${dbxschemaname}].[permissionsallusers_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[permissions_view] (
   [Permission_id], 
   [PermissionType_id], 
   [Permission_Name], 
   [Permission_Desc], 
   [Status_id], 
   [Status_Desc], 
   [Role_Count], 
   [Users_Count], 
   [Status])
AS 

   SELECT 
      (permission.id) AS Permission_id, 
      (permission.Type_id) AS PermissionType_id, 
      (permission.Name) AS Permission_Name, 
      (permission.Description) AS Permission_Desc, 
      (permission.Status_id) AS Status_id, 
      (status.Description) AS Status_Desc, 
      
         (
            SELECT count(rolepermission.Role_id)
            FROM [${dbxschemaname}].rolepermission
            WHERE rolepermission.Permission_id = (permission.id)
         ) AS Role_Count, 
      (
         (
            SELECT count(userpermission.Permission_id)
            FROM [${dbxschemaname}].userpermission
            WHERE userpermission.Permission_id = (permission.id)
         ) + 
         (
            SELECT count(userrole.User_id)
            FROM [${dbxschemaname}].userrole
            WHERE userrole.Role_id IN 
               (
                  SELECT rolepermission.Role_id AS Role_id
                  FROM [${dbxschemaname}].rolepermission
                  WHERE rolepermission.Permission_id = (permission.id)
               )
         )) AS Users_Count, 
      CASE (permission.Status_id)
         WHEN N'SID_ACTIVE' THEN N'Active'
         ELSE N'Inactive'
      END AS Status
   FROM ([${dbxschemaname}].permission 
      INNER JOIN [${dbxschemaname}].status 
      ON ((permission.Status_id = status.id)))
GO
/****** Object:  View [${dbxschemaname}].[permissionsallusers_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[permissionsallusers_view] (
   [User_id], 
   [Permission_id], 
   [Permission_Name], 
   [Permission_Status_id], 
   [Permission_Description], 
   [User_Status_id], 
   [UserName], 
   [Email], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [createdby], 
   [updatedby], 
   [createdts], 
   [updatedts], 
   [isDirect], 
   [softdeleteflag])
AS 
   SELECT 
      userpermission.User_id AS User_id, 
      userpermission.Permission_id AS Permission_id, 
      permission.Name AS Permission_Name, 
      permission.Status_id AS Permission_Status_id, 
      permission.Description AS Permission_Description, 
      systemuser.Status_id AS User_Status_id, 
      systemuser.Username AS UserName, 
      systemuser.Email AS Email, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.createdby AS createdby, 
      systemuser.modifiedby AS updatedby, 
      systemuser.createdts AS createdts, 
      systemuser.lastmodifiedts AS updatedts, 
      N'true' AS isDirect, 
      systemuser.softdeleteflag AS softdeleteflag
   FROM (([${dbxschemaname}].userpermission 
      INNER JOIN [${dbxschemaname}].systemuser 
      ON ((userpermission.User_id = systemuser.id))) 
      INNER JOIN [${dbxschemaname}].permission 
      ON ((userpermission.Permission_id = permission.id)))
    UNION
   SELECT 
      userrole.User_id AS User_id, 
      rolepermission.Permission_id AS Permission_id, 
      permission.Name AS Permission_Name, 
      permission.Status_id AS Permission_Status_id, 
      permission.Description AS Permission_Description, 
      systemuser.Status_id AS User_Status_id, 
      systemuser.Username AS UserName, 
      systemuser.Email AS Email, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.createdby AS createdby, 
      systemuser.modifiedby AS updatedby, 
      systemuser.createdts AS createdts, 
      systemuser.lastmodifiedts AS updatedts, 
      N'false' AS isDirect, 
      systemuser.softdeleteflag AS softdeleteflag
   FROM ((([${dbxschemaname}].rolepermission 
      INNER JOIN [${dbxschemaname}].userrole 
      ON ((userrole.Role_id = rolepermission.Role_id))) 
      INNER JOIN [${dbxschemaname}].systemuser 
      ON ((userrole.User_id = systemuser.id))) 
      INNER JOIN [${dbxschemaname}].permission 
      ON ((rolepermission.Permission_id = permission.id)))
GO
/****** Object:  View [${dbxschemaname}].[pfmbudgetsnapshotview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[pfmbudgetsnapshotview] (
   [allocatedAmount], 
   [amountSpent], 
   [budgetId], 
   [categoryId], 
   [categoryName])
AS 
   SELECT 
      pfmbudgetsnapshot.allocatedAmount AS allocatedAmount, 
      pfmbudgetsnapshot.amountSpent AS amountSpent, 
      pfmbudgetsnapshot.id AS budgetId, 
      pfmbudgetsnapshot.Category_Id AS categoryId, 
      pfmcategory.categoryName AS categoryName
   FROM ([${dbxschemaname}].pfmbudgetsnapshot 
      CROSS JOIN [${dbxschemaname}].pfmcategory)
   WHERE (pfmbudgetsnapshot.Category_Id = pfmcategory.id)
GO
/****** Object:  View [${dbxschemaname}].[pfmpiechartview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[pfmpiechartview] (
   [cashSpent], 
   [monthId], 
   [year], 
   [categoryId], 
   [monthName], 
   [categoryName])
AS 
   SELECT 
      pfmpiechart.cashSpent AS cashSpent, 
      pfmpiechart.monthId AS monthId, 
      pfmpiechart.year AS year, 
      pfmpiechart.categoryId AS categoryId, 
      pfmmonth.monthName AS monthName, 
      pfmcategory.categoryName AS categoryName
   FROM (([${dbxschemaname}].pfmpiechart 
      CROSS JOIN [${dbxschemaname}].pfmmonth) 
      CROSS JOIN [${dbxschemaname}].pfmcategory)
   WHERE ((pfmpiechart.monthId = pfmmonth.id) AND (pfmpiechart.categoryId = pfmcategory.id))
GO
/****** Object:  View [${dbxschemaname}].[policy_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[policy_view] (
   [id], 
   [Type_id], 
   [Locale], 
   [PolicyContent])
AS 
   SELECT policycontent.id AS id, policytype.id AS Type_id, policycontent.Locale_Code AS Locale, policycontent.Content AS PolicyContent
   FROM ([${dbxschemaname}].policycontent 
      INNER JOIN [${dbxschemaname}].policytype 
      ON ((policycontent.Type_id = policytype.id)))
GO
/****** Object:  View [${dbxschemaname}].[productdetail_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[productdetail_view] (
   [productId], 
   [Type_id], 
   [isLeadSupported], 
   [productTypeId], 
   [productName], 
   [Status_id], 
   [features], 
   [rates], 
   [info], 
   [productDescription], 
   [termsAndConditions], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag], 
   [productType], 
   [MarketingStateId], 
   [SecondaryProduct_id], 
   [otherproducttype_Name], 
   [otherproducttype_Description], 
   [otherproducttype_id])
AS 
   SELECT 
      product.id AS productId, 
      product.Type_id AS Type_id, 
      product.isLeadSupported AS isLeadSupported, 
      product.ProductCode AS productTypeId, 
      product.Name AS productName, 
      product.Status_id AS Status_id, 
      product.ProductFeatures AS features, 
      product.ProductCharges AS rates, 
      product.AdditionalInformation AS info, 
      product.productDescription AS productDescription, 
      product.termsAndConditions AS termsAndConditions, 
      product.createdby AS createdby, 
      product.modifiedby AS modifiedby, 
      product.createdts AS createdts, 
      product.lastmodifiedts AS lastmodifiedts, 
      product.synctimestamp AS synctimestamp, 
      product.softdeleteflag AS softdeleteflag, 
      producttype.Name AS productType, 
      product.MarketingStateId AS MarketingStateId, 
      product.SecondaryProduct_id AS SecondaryProduct_id, 
      otherproducttype.Name AS otherproducttype_Name, 
      otherproducttype.Description AS otherproducttype_Description, 
      otherproducttype.id AS otherproducttype_id
   FROM (([${dbxschemaname}].product 
      INNER JOIN [${dbxschemaname}].producttype 
      ON ((product.Type_id = producttype.id))) 
      INNER JOIN [${dbxschemaname}].otherproducttype 
      ON ((product.OtherProductType_id = otherproducttype.id)))
GO
/****** Object:  View [${dbxschemaname}].[region_details_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[region_details_view] (
   [region_Id], 
   [region_Code], 
   [region_Name], 
   [country_Id], 
   [country_Code], 
   [country_Name], 
   [region_createdby], 
   [region_modifiedby], 
   [region_createdts], 
   [region_lastmodifiedts])
AS 
   SELECT 
      region.id AS region_Id, 
      region.Code AS region_Code, 
      region.Name AS region_Name, 
      region.Country_id AS country_Id, 
      country.Code AS country_Code, 
      country.Name AS country_Name, 
      region.createdby AS region_createdby, 
      region.modifiedby AS region_modifiedby, 
      region.createdts AS region_createdts, 
      region.lastmodifiedts AS region_lastmodifiedts
   FROM ([${dbxschemaname}].region 
      INNER JOIN [${dbxschemaname}].country 
      ON ((country.id = region.Country_id)))
GO
/****** Object:  View [${dbxschemaname}].[rolepermission_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[rolepermission_view] (
   [Role_Name], 
   [Role_Description], 
   [Role_Status_id], 
   [Role_id], 
   [Permission_id], 
   [Permission_Type_id], 
   [Permission_Status_id], 
   [DataType_id], 
   [Permission_Name], 
   [Permission_Description], 
   [Permission_isComposite], 
   [PermissionValue], 
   [Permission_createdby], 
   [Permission_modifiedby], 
   [Permission_createdts], 
   [Permission_lastmodifiedts], 
   [Permission_synctimestamp], 
   [Permission_softdeleteflag])
AS 
   SELECT 
      role.Name AS Role_Name, 
      role.Description AS Role_Description, 
      role.Status_id AS Role_Status_id, 
      rolepermission.Role_id AS Role_id, 
      permission.id AS Permission_id, 
      permission.Type_id AS Permission_Type_id, 
      permission.Status_id AS Permission_Status_id, 
      permission.DataType_id AS DataType_id, 
      permission.Name AS Permission_Name, 
      permission.Description AS Permission_Description, 
      permission.isComposite AS Permission_isComposite, 
      permission.PermissionValue AS PermissionValue, 
      permission.createdby AS Permission_createdby, 
      permission.modifiedby AS Permission_modifiedby, 
      permission.createdts AS Permission_createdts, 
      permission.lastmodifiedts AS Permission_lastmodifiedts, 
      permission.synctimestamp AS Permission_synctimestamp, 
      permission.softdeleteflag AS Permission_softdeleteflag
   FROM (([${dbxschemaname}].rolepermission 
      INNER JOIN [${dbxschemaname}].permission 
      ON ((rolepermission.Permission_id = permission.id))) 
      INNER JOIN [${dbxschemaname}].role 
      ON ((role.id = rolepermission.Role_id)))
GO
/****** Object:  View [${dbxschemaname}].[roleuser_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[roles_view] (
   [role_id], 
   [roleType_id], 
   [role_Name], 
   [role_Desc], 
   [Status_id], 
   [Status_Desc], 
   [permission_Count], 
   [Users_Count], 
   [Status])
AS 

   SELECT 
      (role.id) AS role_id, 
      (role.Type_id) AS roleType_id, 
      (role.Name) AS role_Name, 
      (role.Description) AS role_Desc, 
      (role.Status_id) AS Status_id, 
      (status.Description) AS Status_Desc, 
      
         (
            SELECT count(rolepermission.Role_id)
            FROM [${dbxschemaname}].rolepermission
            WHERE rolepermission.Role_id = (role.id)
         ) AS permission_Count, 
      
         (
            SELECT count(userrole.User_id)
            FROM [${dbxschemaname}].userrole
            WHERE userrole.Role_id IN 
               (

                  SELECT (rolepermission.Role_id) AS Role_id
                  FROM [${dbxschemaname}].rolepermission
                  WHERE rolepermission.Role_id = (role.id)
               )
         ) AS Users_Count, 
      CASE (role.Status_id)
         WHEN N'SID_ACTIVE' THEN N'Active'
         ELSE N'Inactive'
      END AS Status
   FROM ([${dbxschemaname}].role 
      INNER JOIN [${dbxschemaname}].status 
      ON ((role.Status_id = status.id)))
GO
/****** Object:  View [${dbxschemaname}].[roleuser_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[roleuser_view] (
   [User_id], 
   [Role_id], 
   [Status_id], 
   [Username], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [Email], 
   [UpdatedBy], 
   [LastModifiedTimeStamp])
AS 
   SELECT 
      userrole.User_id AS User_id, 
      userrole.Role_id AS Role_id, 
      systemuser.Status_id AS Status_id, 
      systemuser.Username AS Username, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.Email AS Email, 
      systemuser.modifiedby AS UpdatedBy, 
      systemuser.lastmodifiedts AS LastModifiedTimeStamp
   FROM ([${dbxschemaname}].userrole 
      INNER JOIN [${dbxschemaname}].systemuser 
      ON ((userrole.User_id = systemuser.id)))
GO
/****** Object:  View [${dbxschemaname}].[systemuser_permissions_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[security_images_view] (
   [SecurityImage_id], 
   [SecurityImageBase64String], 
   [SecurityImage_Status], 
   [UserCount], 
   [softdeleteflag])
AS 
   SELECT 
      (securityimage.id) AS SecurityImage_id, 
      (securityimage.[Image]) AS SecurityImageBase64String, 
      (securityimage.Status_id) AS SecurityImage_Status, 
      
         (
            SELECT COUNT(customersecurityimages.Customer_id)
            FROM [${dbxschemaname}].customersecurityimages
            WHERE customersecurityimages.Image_id = (securityimage.id)
         ) AS UserCount, 
      (securityimage.softdeleteflag) AS softdeleteflag
   FROM [${dbxschemaname}].securityimage
GO
/****** Object:  View [${dbxschemaname}].[security_questions_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[security_questions_view] (
   [SecurityQuestion_id], 
   [SecurityQuestion], 
   [SecurityQuestion_Status], 
   [lastmodifiedts], 
   [UserCount], 
   [softdeleteflag])
AS 
SELECT 
      (securityquestion.id) AS SecurityQuestion_id, 
      (securityquestion.Question) AS SecurityQuestion, 
      (securityquestion.Status_id) AS SecurityQuestion_Status, 
      (securityquestion.lastmodifiedts) AS lastmodifiedts, 
      
         (
			SELECT COUNT(customersecurityquestions.Customer_id)
            FROM [${dbxschemaname}].customersecurityquestions
            WHERE customersecurityquestions.SecurityQuestion_id = (securityquestion.id)
         ) AS UserCount, 
      (securityquestion.softdeleteflag) AS softdeleteflag
   FROM [${dbxschemaname}].securityquestion
GO
/****** Object:  View [${dbxschemaname}].[service_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[service_view] (
   [id], 
   [Type_id], 
   [Channel_id], 
   [Channel], 
   [Name], 
   [Description], 
   [DisplayName], 
   [DisplayDescription], 
   [Category_Id], 
   [Code], 
   [Status_id], 
   [Notes], 
   [MaxTransferLimit], 
   [MinTransferLimit], 
   [TransferDenominations], 
   [IsFutureTransaction], 
   [TransactionCharges], 
   [IsAuthorizationRequired], 
   [IsSMSAlertActivated], 
   [SMSCharges], 
   [IsBeneficiarySMSAlertActivated], 
   [BeneficiarySMSCharge], 
   [HasWeekendOperation], 
   [IsOutageMessageActive], 
   [IsAlertActive], 
   [IsTCActive], 
   [IsAgreementActive], 
   [IsCampaignActive], 
   [WorkSchedule_id], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [createdby], 
   [modifiedby], 
   [createdts], 
   [lastmodifiedts], 
   [synctimestamp], 
   [softdeleteflag], 
   [Status], 
   [Category_Name], 
   [Type_Name], 
   [WorkSchedule_Desc])
AS 
   SELECT 
      service.id AS id, 
      min(service.Type_id) AS Type_id, 
      String_agg(CAST(sc.Channel_id as nvarchar(max)) , ',') AS Channel_id, 
      String_agg(CAST(servicechannel.Description as nvarchar(max)) , ',') AS Channel, 
      min(service.Name) AS Name, 
      min(service.Description) AS Description, 
      min(service.DisplayName) AS DisplayName, 
      min(service.DisplayDescription) AS DisplayDescription, 
      min(service.Category_id) AS Category_Id, 
      min(service.code) AS Code, 
      min(service.Status_id) AS Status_id, 
      min(service.Notes) AS Notes, 
      min(service.MaxTransferLimit) AS MaxTransferLimit, 
      min(service.MinTransferLimit) AS MinTransferLimit, 
      min(service.TransferDenominations) AS TransferDenominations, 
      (service.IsFutureTransaction) AS IsFutureTransaction, 
      min(service.TransactionCharges) AS TransactionCharges, 
      (service.IsAuthorizationRequired) AS IsAuthorizationRequired, 
      (service.IsSMSAlertActivated) AS IsSMSAlertActivated, 
      min(service.SMSCharges) AS SMSCharges, 
      (service.IsBeneficiarySMSAlertActivated) AS IsBeneficiarySMSAlertActivated, 
      min(service.BeneficiarySMSCharge) AS BeneficiarySMSCharge, 
      (service.HasWeekendOperation) AS HasWeekendOperation, 
      (service.IsOutageMessageActive) AS IsOutageMessageActive, 
      (service.IsAlertActive) AS IsAlertActive, 
      (service.IsTCActive) AS IsTCActive, 
      (service.IsAgreementActive) AS IsAgreementActive, 
      (service.IsCampaignActive) AS IsCampaignActive, 
      min(service.WorkSchedule_id) AS WorkSchedule_id, 
      min(service.TransactionFee_id) AS TransactionFee_id, 
      min(service.TransactionLimit_id) AS TransactionLimit_id, 
      min(service.createdby) AS createdby, 
      min(service.modifiedby) AS modifiedby, 
      min(service.createdts) AS createdts, 
      min(service.lastmodifiedts) AS lastmodifiedts, 
      min(service.synctimestamp) AS synctimestamp, 
      (service.softdeleteflag) AS softdeleteflag, 
      min(status.Description) AS Status, 
      min(category.Name) AS Category_Name, 
      min(servicetype.Description) AS Type_Name, 
      min(workschedule.Description) AS WorkSchedule_Desc
   FROM (((((([${dbxschemaname}].service 
      LEFT JOIN [${dbxschemaname}].service_channels  AS sc 
      ON ((sc.Service_id = service.id))) 
      LEFT JOIN [${dbxschemaname}].servicechannel 
      ON ((sc.Channel_id = servicechannel.id))) 
      LEFT JOIN [${dbxschemaname}].category 
      ON ((service.Category_id = category.id))) 
      LEFT JOIN [${dbxschemaname}].status 
      ON ((service.Status_id = status.id))) 
      LEFT JOIN [${dbxschemaname}].servicetype 
      ON ((service.Type_id = servicetype.id))) 
      LEFT JOIN [${dbxschemaname}].workschedule 
      ON ((service.WorkSchedule_id = workschedule.id)))
   GROUP BY service.id,service.IsFutureTransaction,service.IsAuthorizationRequired,service.IsSMSAlertActivated,service.IsBeneficiarySMSAlertActivated,
   service.HasWeekendOperation,service.IsOutageMessageActive,service.IsAlertActive,service.IsTCActive,service.IsAgreementActive,
   service.IsCampaignActive,service.softdeleteflag

GO
/****** Object:  View [${dbxschemaname}].[systemuser_permissions_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[systemuser_permissions_view] (
   [User_id], 
   [Permission_id], 
   [Permission_name], 
   [Permission_status])
AS 
   SELECT up.User_id AS User_id, up.Permission_id AS Permission_id, p.Name AS Permission_name, p.Status_id AS Permission_status
   FROM ([${dbxschemaname}].userpermission  AS up 
      INNER JOIN [${dbxschemaname}].permission  AS p 
      ON ((up.Permission_id = p.id)))
    UNION
   SELECT ur.User_id AS User_id, p.id AS Permission_id, p.Name AS Permission_name, p.Status_id AS Permission_status
   FROM ((([${dbxschemaname}].role  AS r 
      INNER JOIN [${dbxschemaname}].userrole  AS ur 
      ON ((r.id = ur.Role_id))) 
      INNER JOIN [${dbxschemaname}].rolepermission  AS rp 
      ON ((ur.Role_id = rp.Role_id))) 
      INNER JOIN [${dbxschemaname}].permission  AS p 
      ON ((rp.Permission_id = p.id)))
GO
/****** Object:  View [${dbxschemaname}].[systemuser_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[systemuser_view] (
   [UserID], 
   [Username], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [Email], 
   [Status_id], 
   [UpdatedBy], 
   [LastModifiedTimeStamp])
AS 
   SELECT 
      systemuser.id AS UserID, 
      systemuser.Username AS Username, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.Email AS Email, 
      systemuser.Status_id AS Status_id, 
      systemuser.modifiedby AS UpdatedBy, 
      systemuser.lastmodifiedts AS LastModifiedTimeStamp
   FROM [${dbxschemaname}].systemuser
GO
/****** Object:  View [${dbxschemaname}].[transactionfeegroup_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[toaccountransactionview] (
   [Account_id], 
   [AccountName], 
   [NickName], 
   [AccountHolder], 
   [UserName], 
   [ExternalBankidentity_id], 
   [CurrencyCode], 
   [User_id], 
   [AvailableBalance], 
   [Bank_id], 
   [ShowTransactions], 
   [CurrentBalance], 
   [SwiftCode], 
   [RoutingNumber], 
   [transactionId], 
   [transactiontype], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Bill_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [isScheduled], 
   [category], 
   [billCategory], 
   [ExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [createdDate], 
   [cashlessEmail], 
   [cashlessMode], 
   [cashlessOTP], 
   [cashlessOTPValidDate], 
   [cashlessPersonName], 
   [cashlessPhone], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [frequencyEndDate], 
   [frequencyStartDate], 
   [hasDepositImage], 
   [payeeId], 
   [p2pContact], 
   [personId], 
   [recurrenceDesc], 
   [numberOfRecurrences], 
   [scheduledDate], 
   [transactionComments], 
   [transactionsNotes], 
   [transDescription], 
   [transactionDate], 
   [description], 
   [TypeDescription], 
   [IBAN], 
   [sortCode])
AS 
   /*
   *   SSMA warning messages:
   *   M2SS0088: Unable to determine if GROUP BY clause contains constant expression or alias, because it contains unresolved identifiers.
   */

   SELECT 
      min(accounts.Account_id) AS Account_id, 
      min(accounts.AccountName) AS AccountName, 
      min(accounts.NickName) AS NickName, 
      min(accounts.AccountHolder) AS AccountHolder, 
      min(accounts.UserName) AS UserName, 
      min(accounts.ExternalBankidentity_id) AS ExternalBankidentity_id, 
      min(accounts.CurrencyCode) AS CurrencyCode, 
      min(accounts.User_id) AS User_id, 
      min(accounts.AvailableBalance) AS AvailableBalance, 
      min(accounts.Bank_id) AS Bank_id, 
      accounts.ShowTransactions AS ShowTransactions, 
      min(accounts.CurrentBalance) AS CurrentBalance, 
      min(accounts.SwiftCode) AS SwiftCode, 
      min(accounts.RoutingNumber) AS RoutingNumber, 
      [transaction].Id AS transactionId, 
      min([transaction].Type_id) AS transactiontype, 
      min([transaction].Customer_id) AS Customer_id, 
      min([transaction].ExpenseCategory_id) AS ExpenseCategory_id, 
      min([transaction].Bill_id) AS Bill_id, 
      min([transaction].Reference_id) AS Reference_id, 
      min([transaction].fromAccountNumber) AS fromAccountNumber, 
      min([transaction].fromAccountBalance) AS fromAccountBalance, 
      min([transaction].toAccountNumber) AS toAccountNumber, 
      min([transaction].toAccountBalance) AS toAccountBalance, 
      min([transaction].amount) AS amount, 
      min([transaction].Status_id) AS Status_id, 
      min([transaction].statusDesc) AS statusDesc, 
      [transaction].isScheduled AS isScheduled, 
      min([transaction].category) AS category, 
      min([transaction].billCategory) AS billCategory, 
      min([transaction].toExternalAccountNumber) AS ExternalAccountNumber, 
      min([transaction].Person_Id) AS Person_Id, 
      min([transaction].frequencyType) AS frequencyType, 
      min([transaction].createdDate) AS createdDate, 
      min([transaction].cashlessEmail) AS cashlessEmail, 
      min([transaction].cashlessMode) AS cashlessMode, 
      min([transaction].cashlessOTP) AS cashlessOTP, 
      min([transaction].cashlessOTPValidDate) AS cashlessOTPValidDate, 
      min([transaction].cashlessPersonName) AS cashlessPersonName, 
      min([transaction].cashlessPhone) AS cashlessPhone, 
      min([transaction].cashlessSecurityCode) AS cashlessSecurityCode, 
      min([transaction].cashWithdrawalTransactionStatus) AS cashWithdrawalTransactionStatus, 
      min([transaction].frequencyEndDate) AS frequencyEndDate, 
      min([transaction].frequencyStartDate) AS frequencyStartDate, 
      min([transaction].hasDepositImage) AS hasDepositImage, 
      min([transaction].Payee_id) AS payeeId, 
      min([transaction].p2pContact) AS p2pContact, 
      min([transaction].Person_Id) AS personId, 
      min([transaction].recurrenceDesc) AS recurrenceDesc, 
      min([transaction].numberOfRecurrences) AS numberOfRecurrences, 
      min([transaction].scheduledDate) AS scheduledDate, 
      min([transaction].transactionComments) AS transactionComments, 
      min([transaction].notes) AS transactionsNotes, 
      min([transaction].description) AS transDescription, 
      min([transaction].transactionDate) AS transactionDate, 
      min(transactiontype.description) AS description, 
      min(accounttype.TypeDescription) AS TypeDescription, 
      min([transaction].IBAN) AS IBAN, 
      min([transaction].sortCode) AS sortCode
   FROM ((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].[transaction] on (accounts.Account_id = [transaction].toAccountNumber)) 
      INNER JOIN [${dbxschemaname}].transactiontype on ([transaction].Type_id = transactiontype.Id)) 
      INNER JOIN [${dbxschemaname}].accounttype on (accounttype.TypeID = accounts.Type_id))
   GROUP BY [transaction].Id,isScheduled,ShowTransactions

GO
/****** Object:  View [${dbxschemaname}].[transactionfeegroup_view]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[transactionfeegroup_view] (
   [Group_id], 
   [Service_id], 
   [TransactionFee_id], 
   [MinimumTransactionValue], 
   [MaximumTransactionValue], 
   [Fees], 
   [transactionFeeSlab_id])
AS 
   SELECT 
      groupentitlement.Group_id AS Group_id, 
      groupentitlement.Service_id AS Service_id, 
      groupentitlement.TransactionFee_id AS TransactionFee_id, 
      transactionfeeslab.MinimumTransactionValue AS MinimumTransactionValue, 
      transactionfeeslab.MaximumTransactionValue AS MaximumTransactionValue, 
      transactionfeeslab.Fees AS Fees, 
      transactionfeeslab.id AS transactionFeeSlab_id
   FROM ([${dbxschemaname}].transactionfeeslab 
      INNER JOIN [${dbxschemaname}].groupentitlement 
      ON ((groupentitlement.TransactionFee_id = transactionfeeslab.TransactionFee_id)))
GO
/****** Object:  View [${dbxschemaname}].[transactionfeesenduser_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[transactionfeesenduser_view] (
   [customer_id], 
   [Service_id], 
   [isTypeBusiness], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [transactionfee_Description], 
   [MinimumTransactionValue], 
   [MaximumTransactionValue], 
   [Currency], 
   [Fees], 
   [transactionFeeSlab_id])
AS 
   SELECT 
      customerentitlement.Customer_id AS customer_id, 
      customerentitlement.Service_id AS Service_id, 
      customerentitlement.isTypeBusiness AS isTypeBusiness, 
      customerentitlement.TransactionFee_id AS TransactionFee_id, 
      customerentitlement.TransactionLimit_id AS TransactionLimit_id, 
      transactionfee.Description AS transactionfee_Description, 
      transactionfeeslab.MinimumTransactionValue AS MinimumTransactionValue, 
      transactionfeeslab.MaximumTransactionValue AS MaximumTransactionValue, 
      transactionfeeslab.Currency AS Currency, 
      transactionfeeslab.Fees AS Fees, 
      transactionfeeslab.id AS transactionFeeSlab_id
   FROM (([${dbxschemaname}].transactionfee 
      INNER JOIN [${dbxschemaname}].customerentitlement 
      ON ((customerentitlement.TransactionFee_id = transactionfee.id))) 
      INNER JOIN [${dbxschemaname}].transactionfeeslab 
      ON ((transactionfeeslab.TransactionFee_id = transactionfee.id)))
GO
/****** Object:  View [${dbxschemaname}].[transactionfeeservice_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[transactionfeeservice_view] (
   [Service_id], 
   [TransactionFee_id], 
   [TransactionLimit_id], 
   [MinimumTransactionValue], 
   [MaximumTransactionValue], 
   [Currency], 
   [Fees], 
   [transactionFeeSlab_id])
AS 
   SELECT 
      service.id AS Service_id, 
      service.TransactionFee_id AS TransactionFee_id, 
      service.TransactionLimit_id AS TransactionLimit_id, 
      transactionfeeslab.MinimumTransactionValue AS MinimumTransactionValue, 
      transactionfeeslab.MaximumTransactionValue AS MaximumTransactionValue, 
      transactionfeeslab.Currency AS Currency, 
      transactionfeeslab.Fees AS Fees, 
      transactionfeeslab.id AS transactionFeeSlab_id
   FROM ([${dbxschemaname}].transactionfeeslab 
      INNER JOIN [${dbxschemaname}].service 
      ON ((service.TransactionFee_id = transactionfeeslab.TransactionFee_id)))
GO
/****** Object:  View [${dbxschemaname}].[userbanksview]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[travelnotifications_view] (
   [notificationId], 
   [startDate], 
   [endDate], 
   [destinations], 
   [additionalNotes], 
   [Status_id], 
   [contactNumber], 
   [customerId], 
   [date], 
   [cardNumber], 
   [cardCount], 
   [status])
AS 
   SELECT 
      travelnotification.id AS notificationId, 
      travelnotification.PlannedDepartureDate AS startDate, 
      travelnotification.PlannedReturnDate AS endDate, 
      travelnotification.Destinations AS destinations, 
      travelnotification.AdditionalNotes AS additionalNotes, 
      travelnotification.Status_id AS Status_id, 
      travelnotification.phonenumber AS contactNumber, 
      notificationcardinfo.Customer_id AS customerId, 
      travelnotification.createdts AS date, 
      STRING_AGG(CAST((notificationcardinfo.CardName +' ' +notificationcardinfo.CardNumber) as nvarchar(max))
            , ',') AS cardNumber,
      count_big(notificationcardinfo.CardNumber) AS cardCount, 
      status.Description AS status
   FROM (([${dbxschemaname}].travelnotification 
      INNER JOIN [${dbxschemaname}].notificationcardinfo 
      ON ((travelnotification.id = notificationcardinfo.Notification_id))) 
      INNER JOIN [${dbxschemaname}].status 
      ON ((travelnotification.Status_id = status.id)))
   GROUP BY travelnotification.id, travelnotification.PlannedDepartureDate, travelnotification.PlannedReturnDate,
   travelnotification.Destinations, travelnotification.AdditionalNotes, travelnotification.Status_id,travelnotification.phonenumber,
   notificationcardinfo.Customer_id, travelnotification.createdts, status.Description


GO
/****** Object:  View [${dbxschemaname}].[userbanksview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[userbanksview] (
   [User_id], 
   [MainUser_id], 
   [id], 
   [BankName], 
   [BankId])
AS 
   SELECT 
      externalbankidentity.User_id AS User_id, 
      externalbankidentity.MainUser_id AS MainUser_id, 
      externalbank.id AS id, 
      externalbank.BankName AS BankName, 
      externalbank.BankId AS BankId
   FROM ([${dbxschemaname}].externalbank 
      CROSS JOIN [${dbxschemaname}].externalbankidentity)
   WHERE (externalbank.id = externalbankidentity.ExternalBank_id)
GO
/****** Object:  View [${dbxschemaname}].[userdirectpermission_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[userdirectpermission_view] (
   [User_id], 
   [Permission_id], 
   [Permission_Name], 
   [Permission_Status_id], 
   [Permission_Description], 
   [Permission_isComposite], 
   [User_Status_id], 
   [UserName], 
   [Email], 
   [FirstName], 
   [MiddleName], 
   [LastName], 
   [createdby], 
   [updatedby], 
   [createdts], 
   [updatedts], 
   [softdeleteflag])
AS 
   SELECT 
      userpermission.User_id AS User_id, 
      userpermission.Permission_id AS Permission_id, 
      permission.Name AS Permission_Name, 
      permission.Status_id AS Permission_Status_id, 
      permission.Description AS Permission_Description, 
      permission.isComposite AS Permission_isComposite, 
      systemuser.Status_id AS User_Status_id, 
      systemuser.Username AS UserName, 
      systemuser.Email AS Email, 
      systemuser.FirstName AS FirstName, 
      systemuser.MiddleName AS MiddleName, 
      systemuser.LastName AS LastName, 
      systemuser.createdby AS createdby, 
      systemuser.modifiedby AS updatedby, 
      systemuser.createdts AS createdts, 
      systemuser.lastmodifiedts AS updatedts, 
      systemuser.softdeleteflag AS softdeleteflag
   FROM (([${dbxschemaname}].userpermission 
      INNER JOIN [${dbxschemaname}].systemuser 
      ON ((userpermission.User_id = systemuser.id))) 
      INNER JOIN [${dbxschemaname}].permission 
      ON ((userpermission.Permission_id = permission.id)))
GO
/****** Object:  View [${dbxschemaname}].[userpermission_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/

CREATE VIEW [${dbxschemaname}].[userpermission_view] (
   [User_id], 
   [Role_id], 
   [Permission_id], 
   [Permission_Name], 
   [Permission_Desc], 
   [Permission_isComposite])
AS 
   SELECT 
      userrole.User_id AS User_id, 
      userrole.Role_id AS Role_id, 
      rolepermission.Permission_id AS Permission_id, 
      permission.Name AS Permission_Name, 
      permission.Description AS Permission_Desc, 
      permission.isComposite AS Permission_isComposite
   FROM (([${dbxschemaname}].userrole 
      INNER JOIN [${dbxschemaname}].rolepermission 
      ON ((userrole.Role_id = rolepermission.Role_id))) 
      INNER JOIN [${dbxschemaname}].permission 
      ON ((permission.id = rolepermission.Permission_id)))
GO
/****** Object:  View [${dbxschemaname}].[wiretransfers_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   ALGORITHM =  UNDEFINED.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*   M2SS0003: The following SQL clause was ignored during conversion:
*   SQL SECURITY DEFINER.
*/
CREATE VIEW [${dbxschemaname}].[wireaccounttransactionview] (
   [Account_id], 
   [AccountName], 
   [AccountHolder], 
   [UserName], 
   [ExternalBankidentity_id], 
   [CurrencyCode], 
   [User_id], 
   [AvailableBalance], 
   [Bank_id], 
   [ShowTransactions], 
   [CurrentBalance], 
   [SwiftCode], 
   [RoutingNumber], 
   [transactionId], 
   [transactiontype], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Bill_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [isScheduled], 
   [category], 
   [billCategory], 
   [ExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [createdDate], 
   [cashlessEmail], 
   [cashlessMode], 
   [cashlessOTP], 
   [cashlessOTPValidDate], 
   [cashlessPersonName], 
   [cashlessPhone], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [frequencyEndDate], 
   [frequencyStartDate], 
   [hasDepositImage], 
   [payeeId], 
   [payeeName], 
   [p2pContact], 
   [personId], 
   [recurrenceDesc], 
   [numberOfRecurrences], 
   [scheduledDate], 
   [transactionComments], 
   [transactionsNotes], 
   [transDescription], 
   [transactionDate], 
   [frontImage1], 
   [frontImage2], 
   [backImage1], 
   [backImage2], 
   [checkDesc], 
   [checkNumber1], 
   [checkNumber2], 
   [checkNumber], 
   [checkReason], 
   [requestValidity], 
   [checkDateOfIssue], 
   [bankName1], 
   [bankName2], 
   [withdrawlAmount1], 
   [withdrawlAmount2], 
   [cashAmount], 
   [amountRecieved], 
   [payeeCurrency], 
   [fee], 
   [isDisputed], 
   [disputeReason], 
   [disputeDescription], 
   [disputeDate], 
   [disputeStatus], 
   [description], 
   [nickName], 
   [payeeAccountNumber], 
   [payeeType], 
   [payeeAddressLine2], 
   [payeeAddressLine1], 
   [iban])
AS 
   /*
   *   SSMA warning messages:
   *   M2SS0088: Unable to determine if GROUP BY clause contains constant expression or alias, because it contains unresolved identifiers.
   */

   SELECT 
      min(accounts.Account_id) AS Account_id, 
      min(accounts.AccountName) AS AccountName, 
      min(accounts.AccountHolder) AS AccountHolder, 
      min(accounts.UserName) AS UserName, 
      min(accounts.ExternalBankidentity_id) AS ExternalBankidentity_id, 
      min(accounts.CurrencyCode) AS CurrencyCode, 
      min(accounts.User_id) AS User_id, 
      min(accounts.AvailableBalance) AS AvailableBalance, 
      min(accounts.Bank_id) AS Bank_id, 
      accounts.ShowTransactions AS ShowTransactions, 
      min(accounts.CurrentBalance) AS CurrentBalance, 
      min(accounts.SwiftCode) AS SwiftCode, 
      min(accounts.RoutingNumber) AS RoutingNumber, 
      [transaction].Id AS transactionId, 
      min([transaction].Type_id) AS transactiontype, 
      min([transaction].Customer_id) AS Customer_id, 
      min([transaction].ExpenseCategory_id) AS ExpenseCategory_id, 
      min([transaction].billid) AS Bill_id, 
      min([transaction].Reference_id) AS Reference_id, 
      min([transaction].fromAccountNumber) AS fromAccountNumber, 
      min([transaction].fromAccountBalance) AS fromAccountBalance, 
      min([transaction].toAccountNumber) AS toAccountNumber, 
      min([transaction].toAccountBalance) AS toAccountBalance, 
      min([transaction].amount) AS amount, 
      min([transaction].Status_id) AS Status_id, 
      min([transaction].statusDesc) AS statusDesc, 
      [transaction].isScheduled AS isScheduled, 
      min([transaction].category) AS category, 
      min([transaction].billCategory) AS billCategory, 
      min([transaction].toExternalAccountNumber) AS ExternalAccountNumber, 
      min([transaction].Person_Id) AS Person_Id, 
      min([transaction].frequencyType) AS frequencyType, 
      min([transaction].createdDate) AS createdDate, 
      min([transaction].cashlessEmail) AS cashlessEmail, 
      min([transaction].cashlessMode) AS cashlessMode, 
      min([transaction].cashlessOTP) AS cashlessOTP, 
      min([transaction].cashlessOTPValidDate) AS cashlessOTPValidDate, 
      min([transaction].cashlessPersonName) AS cashlessPersonName, 
      min([transaction].cashlessPhone) AS cashlessPhone, 
      min([transaction].cashlessSecurityCode) AS cashlessSecurityCode, 
      min([transaction].cashWithdrawalTransactionStatus) AS cashWithdrawalTransactionStatus, 
      min([transaction].frequencyEndDate) AS frequencyEndDate, 
      min([transaction].frequencyStartDate) AS frequencyStartDate, 
      min([transaction].hasDepositImage) AS hasDepositImage, 
      min([transaction].Payee_id) AS payeeId, 
      min([transaction].payeeName) AS payeeName, 
      min([transaction].p2pContact) AS p2pContact, 
      min([transaction].Person_Id) AS personId, 
      min([transaction].recurrenceDesc) AS recurrenceDesc, 
      min([transaction].numberOfRecurrences) AS numberOfRecurrences, 
      min([transaction].scheduledDate) AS scheduledDate, 
      min([transaction].transactionComments) AS transactionComments, 
      min([transaction].notes) AS transactionsNotes, 
      min([transaction].description) AS transDescription, 
      min([transaction].transactionDate) AS transactionDate, 
      min([transaction].frontImage1) AS frontImage1, 
      min([transaction].frontImage2) AS frontImage2, 
      min([transaction].backImage1) AS backImage1, 
      min([transaction].backImage2) AS backImage2, 
      min([transaction].checkDesc) AS checkDesc, 
      min([transaction].checkNumber1) AS checkNumber1, 
      min([transaction].checkNumber2) AS checkNumber2, 
      min([transaction].checkNumber) AS checkNumber, 
      min([transaction].checkReason) AS checkReason, 
      min([transaction].requestValidity) AS requestValidity, 
      min([transaction].checkDateOfIssue) AS checkDateOfIssue, 
      min([transaction].bankName1) AS bankName1, 
      min([transaction].bankName2) AS bankName2, 
      min([transaction].withdrawlAmount1) AS withdrawlAmount1, 
      min([transaction].withdrawlAmount2) AS withdrawlAmount2, 
      min([transaction].cashAmount) AS cashAmount, 
      min([transaction].amountRecieved) AS amountRecieved, 
      min([transaction].payeeCurrency) AS payeeCurrency, 
      min([transaction].fee) AS fee, 
      [transaction].isDisputed AS isDisputed, 
      min([transaction].disputeReason) AS disputeReason, 
      min([transaction].disputeDescription) AS disputeDescription, 
      min([transaction].disputeDate) AS disputeDate, 
      min([transaction].disputeStatus) AS disputeStatus, 
      min(transactiontype.description) AS description, 
      min(payee.nickName) AS nickName, 
      min(payee.accountNumber) AS payeeAccountNumber, 
      min(payee.Type_id) AS payeeType, 
      min(payee.addressLine1) AS payeeAddressLine2, 
      min(payee.addressLine2) AS payeeAddressLine1, 
      min(accounts.IBAN) AS iban
   FROM ((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].[transaction] on ((accounts.Account_id = [transaction].fromAccountNumber) OR (accounts.Account_id = [transaction].toAccountNumber))) 
      INNER JOIN [${dbxschemaname}].transactiontype on ([transaction].Type_id = transactiontype.Id) ) 
      INNER JOIN [${dbxschemaname}].payee on ([transaction].Payee_id = payee.Id) )
   GROUP BY [transaction].Id,[transaction].isScheduled,[transaction].isDisputed, ShowTransactions

GO
/****** Object:  View [${dbxschemaname}].[wirecustaccounttransactionview]    Script Date: 6/9/2020 2:00:26 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[wirecustaccounttransactionview] (
   [User_id], 
   [transactionId], 
   [transactiontype], 
   [Customer_id], 
   [ExpenseCategory_id], 
   [Bill_id], 
   [Reference_id], 
   [fromAccountNumber], 
   [fromAccountBalance], 
   [toAccountNumber], 
   [toAccountBalance], 
   [amount], 
   [Status_id], 
   [statusDesc], 
   [isScheduled], 
   [category], 
   [billCategory], 
   [ExternalAccountNumber], 
   [Person_Id], 
   [frequencyType], 
   [createdDate], 
   [cashlessEmail], 
   [cashlessMode], 
   [cashlessOTP], 
   [cashlessOTPValidDate], 
   [cashlessPersonName], 
   [cashlessPhone], 
   [cashlessSecurityCode], 
   [cashWithdrawalTransactionStatus], 
   [frequencyEndDate], 
   [frequencyStartDate], 
   [hasDepositImage], 
   [payeeId], 
   [payeeName], 
   [p2pContact], 
   [personId], 
   [recurrenceDesc], 
   [numberOfRecurrences], 
   [scheduledDate], 
   [transactionComments], 
   [transactionsNotes], 
   [transDescription], 
   [transactionDate], 
   [frontImage1], 
   [frontImage2], 
   [backImage1], 
   [backImage2], 
   [checkDesc], 
   [checkNumber1], 
   [checkNumber2], 
   [checkNumber], 
   [checkReason], 
   [requestValidity], 
   [checkDateOfIssue], 
   [bankName1], 
   [bankName2], 
   [withdrawlAmount1], 
   [withdrawlAmount2], 
   [cashAmount], 
   [amountRecieved], 
   [payeeCurrency], 
   [fee], 
   [isDisputed], 
   [disputeReason], 
   [disputeDescription], 
   [disputeDate], 
   [disputeStatus], 
   [description], 
   [nickName], 
   [payeeAccountNumber], 
   [payeeType], 
   [payeeAddressLine2], 
   [payeeAddressLine1])
AS 
   SELECT DISTINCT 
      customeraccounts.Customer_id AS User_id, 
      [transaction].Id AS transactionId, 
      [transaction].Type_id AS transactiontype, 
      [transaction].Customer_id AS Customer_id, 
      [transaction].ExpenseCategory_id AS ExpenseCategory_id, 
      [transaction].billid AS Bill_id, 
      [transaction].Reference_id AS Reference_id, 
      [transaction].fromAccountNumber AS fromAccountNumber, 
      [transaction].fromAccountBalance AS fromAccountBalance, 
      [transaction].toAccountNumber AS toAccountNumber, 
      [transaction].toAccountBalance AS toAccountBalance, 
      [transaction].amount AS amount, 
      [transaction].Status_id AS Status_id, 
      [transaction].statusDesc AS statusDesc, 
      [transaction].isScheduled AS isScheduled, 
      [transaction].category AS category, 
      [transaction].billCategory AS billCategory, 
      [transaction].toExternalAccountNumber AS ExternalAccountNumber, 
      [transaction].Person_Id AS Person_Id, 
      [transaction].frequencyType AS frequencyType, 
      [transaction].createdDate AS createdDate, 
      [transaction].cashlessEmail AS cashlessEmail, 
      [transaction].cashlessMode AS cashlessMode, 
      [transaction].cashlessOTP AS cashlessOTP, 
      [transaction].cashlessOTPValidDate AS cashlessOTPValidDate, 
      [transaction].cashlessPersonName AS cashlessPersonName, 
      [transaction].cashlessPhone AS cashlessPhone, 
      [transaction].cashlessSecurityCode AS cashlessSecurityCode, 
      [transaction].cashWithdrawalTransactionStatus AS cashWithdrawalTransactionStatus, 
      [transaction].frequencyEndDate AS frequencyEndDate, 
      [transaction].frequencyStartDate AS frequencyStartDate, 
      [transaction].hasDepositImage AS hasDepositImage, 
      [transaction].Payee_id AS payeeId, 
      [transaction].payeeName AS payeeName, 
      [transaction].p2pContact AS p2pContact, 
      [transaction].Person_Id AS personId, 
      [transaction].recurrenceDesc AS recurrenceDesc, 
      [transaction].numberOfRecurrences AS numberOfRecurrences, 
      [transaction].scheduledDate AS scheduledDate, 
      [transaction].transactionComments AS transactionComments, 
      [transaction].notes AS transactionsNotes, 
      [transaction].description AS transDescription, 
      [transaction].transactionDate AS transactionDate, 
      [transaction].frontImage1 AS frontImage1, 
      [transaction].frontImage2 AS frontImage2, 
      [transaction].backImage1 AS backImage1, 
      [transaction].backImage2 AS backImage2, 
      [transaction].checkDesc AS checkDesc, 
      [transaction].checkNumber1 AS checkNumber1, 
      [transaction].checkNumber2 AS checkNumber2, 
      [transaction].checkNumber AS checkNumber, 
      [transaction].checkReason AS checkReason, 
      [transaction].requestValidity AS requestValidity, 
      [transaction].checkDateOfIssue AS checkDateOfIssue, 
      [transaction].bankName1 AS bankName1, 
      [transaction].bankName2 AS bankName2, 
      [transaction].withdrawlAmount1 AS withdrawlAmount1, 
      [transaction].withdrawlAmount2 AS withdrawlAmount2, 
      [transaction].cashAmount AS cashAmount, 
      [transaction].amountRecieved AS amountRecieved, 
      [transaction].payeeCurrency AS payeeCurrency, 
      [transaction].fee AS fee, 
      [transaction].isDisputed AS isDisputed, 
      [transaction].disputeReason AS disputeReason, 
      [transaction].disputeDescription AS disputeDescription, 
      [transaction].disputeDate AS disputeDate, 
      [transaction].disputeStatus AS disputeStatus, 
      transactiontype.description AS description, 
      payee.nickName AS nickName, 
      payee.accountNumber AS payeeAccountNumber, 
      payee.Type_id AS payeeType, 
      payee.addressLine1 AS payeeAddressLine2, 
      payee.addressLine2 AS payeeAddressLine1
   FROM ((([${dbxschemaname}].customeraccounts 
      CROSS JOIN [${dbxschemaname}].[transaction]) 
      CROSS JOIN [${dbxschemaname}].transactiontype) 
      CROSS JOIN [${dbxschemaname}].payee)
   WHERE (
      ((CAST(customeraccounts.Account_id AS float(53)) = [transaction].fromAccountNumber) OR (CAST(customeraccounts.Account_id AS float(53)) = [transaction].toAccountNumber)) AND 
      ([transaction].Type_id = transactiontype.Id) AND 
      ([transaction].Payee_id = payee.Id))
      

GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[wiretransfers_view] (
   [featureActionId], 
   [amount], 
   [roleId], 
   [companyId], 
   [createdby], 
   [fromAccountNumber], 
   [createdts], 
   [status], 
   [scheduledDate])
AS 
   SELECT 
      wiretransfers.featureActionId AS featureActionId, 
      wiretransfers.amount AS amount, 
      wiretransfers.roleId AS roleId, 
      wiretransfers.companyId AS companyId, 
      wiretransfers.createdby AS createdby, 
      wiretransfers.fromAccountNumber AS fromAccountNumber, 
      wiretransfers.createdts AS createdts, 
      wiretransfers.status AS status, 
      wiretransfers.createdts AS scheduledDate
   FROM [${dbxschemaname}].wiretransfers
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].groupbusinesstypecustomercount_view AS
select 
  customergroup.Group_id AS Group_id, 
  customerbusinesstype.BusinessType_id AS BusinessType_id, 
  (
    select 
      count(customergroup.Customer_id) 
    from 
      [${dbxschemaname}].customergroup
    where 
      (
        customergroup.Customer_id = customerbusinesstype.Customer_id
      )
  ) AS Customers_Count 
from 
  (
    (
      [${dbxschemaname}].customergroup 
      join [${dbxschemaname}].customerbusinesstype on (
        (
          customerbusinesstype.Customer_id = customergroup.Customer_id
        )
      )
    ) 
    join [${dbxschemaname}].businesstype on(
      (
        customerbusinesstype.BusinessType_id = businesstype.id
      )
    )
  )
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

  CREATE VIEW [${dbxschemaname}].[organisationaccountsview] AS 
SELECT 
  DISTINCT accounttype.TypeDescription AS Account_Type,
  customeraccounts.Customer_id AS Customer_id,
  customeraccounts.Account_id AS Account_id,
  customeraccounts.Account_id AS accountID,
  customeraccounts.Organization_id AS Organization_Id,
  customeraccounts.createdts AS createdts,
  customeraccounts.lastmodifiedts AS lastmodifiedts,
  accounts.StatusDesc AS StatusDesc,
  accounts.AccountName AS AccountName,
  accounts.AvailableBalance AS availableBalance,
  accounts.CurrentBalance AS currentBalance,
  accounts.DividendRate AS dividendRate,
  accounts.EStatementmentEnable AS eStatementEnable,
  accounts.SwiftCode AS swiftCode,
  accounts.RoutingNumber AS routingNumber,
  accounts.AccountHolder AS accountHolder, 
  accounts.LastDividendPaidDate AS lastDividendPaidDate,
  accounts.LastDividendPaidAmount AS lastDividendPaidAmount,
  accounts.DividendLastPaidAmount AS dividendLastPaidAmount,
  accounts.DividendLastPaidDate AS dividendLastPaidDate,
  'joint' AS Ownership
FROM
      [${dbxschemaname}].accounts
      LEFT JOIN [${dbxschemaname}].accounttype ON
      accounttype.TypeID = accounts.Type_id
      JOIN [${dbxschemaname}].customeraccounts ON
	  accounts.Account_id = customeraccounts.Account_id
WHERE accounts.Account_id = customeraccounts.Account_id
GO



INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID422', N'0');

INSERT INTO [${dbxschemaname}].[alertmessagetypeconfig] ([messagetype], [alerttype], [alertsubtype], [subject]) VALUES ('8010' ,'ACCOUNT' ,'ACCOUNT_LOAD','AlertRouter' );

UPDATE [${dbxschemaname}].[alertrecipienttype] SET inputparamsmapping = '{"accountId":"accountnumber","legalEntityId":"companyLegalUnit"}' WHERE (id = '3');
UPDATE [${dbxschemaname}].[alertrecipienttype] SET inputparamsmapping = '{"coreCustomerId": "corecustomerid",  "id": "customerId","legalEntityId":"companyLegalUnit"}' WHERE (id = '2');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'ApprovalMatrix','ApprovalMatrix','getApprovalMatrixByContractId','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'SignatoryObject','ApprovalMode','deleteApprovalMode','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'SignatoryObject','ApprovalMode','fetchApprovalMode','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'SignatoryObject','ApprovalMode','updateApprovalMode','ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','editAccountSweep','ACCOUNT_SWEEP_EDIT');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','getAccountSweeps','ACCOUNT_SWEEP_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','deleteAccountSweep','ACCOUNT_SWEEP_DELETE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','initiateDownloadAccountSweeps','ACCOUNT_SWEEP_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','createAccountSweep','ACCOUNT_SWEEP_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'AccountSweepsObjects','AccountSweeps','getAccountSweepById','ACCOUNT_SWEEP_VIEW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'BulkWireObjects','BulkWireFile','downloadFile','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'BulkWireObjects','BulkWireFile','downloadSampleFile','ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'LoanPayoff','Packaging','create','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'LoanPayoff','Packaging','get','ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','BankDetails','isValidIBAN','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','BankDetails','getBankDetailsFromBicCode','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','BankDetails','GetBankNameByRoutingNumber','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','BankDetails','getSwiftCode','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','BankDetails','getBICFromBankDetails','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','Payee_Name','getPayeeName','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','Payees','getIntraInterBankPayee','PAYEE_MANAGEMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeManagement','Payees','getPayeesList','PAYEE_MANAGEMENT_VIEW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeObjects','Recipients','createP2PPayee','PAYEE_MANAGEMENT_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeObjects','Recipients','deleteP2PPayee','PAYEE_MANAGEMENT_DELETE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeObjects','Recipients','editP2PPayee','PAYEE_MANAGEMENT_EDIT');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'PayeeObjects','Recipients','getP2PPayee','PAYEE_MANAGEMENT_VIEW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'QRPayments','QRPay','CreateQRPayment','QR_PAYMENTS_CREATE');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','Activity','getToExternalAccountTransactions','INTRA_BANK_FUND_TRANSFER_VIEW,INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','Activity','getUserWiredTransactions','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','Activity','getRecipientWireTransaction','DOMESTIC_WIRE_TRANSFER_VIEW,INTERNATIONAL_WIRE_TRANSFER_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','BankDate','getBankDate','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','CreditCard','getCreditCardAccounts','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','CreditCard','createCreditCardTransfer','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'TransactionObjects','Transaction','P2PTransfer','P2P_CREATE');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','DirectDebits','getDirectDebits','DIRECT_DEBIT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','DirectDebits','stopNextPayment','DIRECT_DEBIT_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','DirectDebits','cancelDirectDebit','DIRECT_DEBIT_CANCEL');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','OnboardingTransactions','createOnboardingTransfer','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','UpcomingTransactions','getScheduledTransactions','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ( NEWID(), 'Transfers','WireTransfer','getUserWiredTransactions','ALLOW');
GO
UPDATE [${dbxschemaname}].[alertsubtype] SET alertConditionId = 'LESS_THAN' WHERE (id = 'MINIMUM_BALANCE');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'resume',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'updateTermsAndConditions',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'getApplicationTypeById',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'submit',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'updateLastEditedSection',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'updateConsents',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationManagement',N'ApplicationJourney',N'getData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'updateInfo',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'deleteInfoAddress',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'deleteInfo',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'updateRelatedInfo',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'getInfo',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'updateInfoAddress',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsCompanyManagement',N'Company',N'getAddress',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'Address',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'Address',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'getRecentApplications',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'updateApplication',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'getCoApplicantStatus',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'deleteCoApplicants',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'updateCoApplicantStatus',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'ApplicationData',N'getAllCoApplicantStatus',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'FinancialInformation',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'FinancialInformation',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'Identity',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'Identity',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'IncomeAndEmployment',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'IncomeAndEmployment',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'updateProspectProfile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'sendInvite',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'createCoApplicants',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'getCoApplicantsBasicInfo',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsManagement',N'PersonalInfo',N'createProspectProfile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsUsersManagement',N'Party',N'getPartyAndDueDiligenceDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyDetailsUsersManagement',N'Prospect',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Messaging',N'OTP',N'verifyMFAOTP',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Messaging',N'OTP',N'isMFAEnabled',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Messaging',N'OTP',N'requestMFAOTP',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'AuthID',N'initiateDoc',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'AuthID',N'getToken',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'AuthID',N'getDocumentDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'AuthID',N'getDocumentStatus',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'IDV',N'executeIDV',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'IDV',N'verifyResponse',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Thirdparty',N'Selfie',N'updateSelfieScoreAndResult',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OrigIntegrationsManagement',N'OrigIntegrations',N'withdrawApplication',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OrigIntegrationsManagement',N'OrigIntegrations',N'getCollateral',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OrigIntegrationsManagement',N'OrigIntegrations',N'getRequestId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DashboardProductsManagement',N'Product',N'getProductSelectionDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'addApplicationEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'useApplicationEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'submitApplicationEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'downloadAssistDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'getApplicationFulfilment',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'createApplicationFulfilment',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DMS',N'DocumentStorage',N'deleteApplicationEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EmailManagement',N'SendEmail',N'sendResumeMail',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PartyMSObjects',N'PartyRelations',N'getPartyRelations',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'UsersManagement',N'User',N'getExternalUsers',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'UsersManagement',N'User',N'getDetailsFromPartyAndT24',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'UsersManagement',N'User',N'getCollateral',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'UsersManagement',N'User',N'signout',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'UsersManagement',N'User',N'getMortgageApplications',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'Collateral',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'Collateral',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'Collateral',N'getOPMSDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'FundingPosition',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'FundingPosition',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'MortgageComposition',N'generateDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'MortgageComposition',N'updatePaymentSchedule',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'MortgageComposition',N'getMortgageXAI',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'ProductSelection',N'updateProductSelection',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'ProductSelection',N'getProductsForPurpose',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'ProductSelection',N'getProductSelection',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ProductsSelectionManagement',N'ProductSelection',N'getAllCDPlans',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Account',N'Account',N'getHoldings',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Funding',N'Funding',N'getSelectedProducts',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Funding',N'Funding',N'getType',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Funding',N'Funding',N'updateData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Funding',N'Funding',N'getProducts',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingThirdPartyMngmnt',N'ExternalAccount',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EligibilityApplication',N'ApplicationEligibility',N'updateApplication',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EligibilityApplication',N'ApplicationEligibility',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'updateUserActionBulk',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'getUserActions',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'createPreRequirements',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'getDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'updateUserAction',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'signalProcess',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'notifyRM',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsManagement',N'UserAction',N'uploadDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'getRequiredDocuments',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'uploadMultipleDocumentForChecklist',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'getDocumentChecklist',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'downloadDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'deleteEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'deleteDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'getDocumentsData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'submitEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'uploadDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'useEvidence',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'Document',N'DocumentChecklist',N'uploadMultipleDocuments',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ApplicationReviewManagement',N'DocumentList',N'downloadDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AdditionInstructionsManagement',N'AdditionalInstruction',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AdditionInstructionsManagement',N'AdditionalInstruction',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AdditionInstructionsManagement',N'AdditionalInstruction',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'adminconsoleService',N'BundleConfigurations',N'fetchConfigurations',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'adminconsoleService',N'Location',N'getCountries',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'approveRejectServiceRequest',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'getServiceRequestTask',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'getServiceRequest',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'claimServiceRequest',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'updateServiceRequestTask',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'getReferenceData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'updateRepaymentDate',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'getServiceRequestOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'getRequestByPagination',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'AssistServiceRequestManagement',N'ServiceRequest',N'updateDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'BridgeLoanManagement',N'BridgeLoan',N'createBridgeLoan',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'BridgeLoanManagement',N'BridgeLoan',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'BridgeLoanManagement',N'BridgeLoan',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CashflowPredictionManagement',N'cashflowPrediction',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralManagement',N'Collateral',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralManagement',N'Collateral',N'getAllDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralManagement',N'Collateral',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralManagement',N'Collateral',N'createCollateral',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralRelationshipManagement',N'CollateralRelationship',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralRelationshipManagement',N'CollateralRelationship',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralRelationshipManagement',N'CollateralRelationship',N'updateDetailsForFacility',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CollateralRelationshipManagement',N'CollateralRelationship',N'updateMultipleDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ComplianceManagement',N'Compliance',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ComplianceManagement',N'Compliance',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ComplianceManagement',N'Compliance',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ConsentsManagement',N'Consents',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'AccountRequest',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'AccountRequest',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'AccountRequest',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'AccountRequest',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'EntityOverview',N'getDiscrepentData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'RiskScoreCard',N'getRiskScoreCard',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'ScoreCard',N'updateScoreCard',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOS',N'ScoreCard',N'getScoreCard',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOSConfigurations',N'GenericConfigurations',N'get',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOSConfigurations',N'GenericConfigurations',N'uploadi18nConfigurations',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOSReferenceData',N'ReferenceData',N'getDocumentCategories',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CorporateLOSReferenceData',N'ReferenceData',N'getReferenceData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CovenantManagement',N'Covenant',N'updateFacility',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CovenantManagement',N'Covenant',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CovenantManagement',N'Covenant',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CovenantManagement',N'Covenant',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CovenantManagement',N'Covenant',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'getAllCustomerActionMetaData',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'reinitiateCustomerAction',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'createCustomerAction',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'getAllEntities',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'updateCustomerActionStatus',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'CustomerActionsMngmntObjSrvc',N'CustomerActions',N'getCustomerActionsByRequest',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DocumentStorageManagement',N'DocumentStorage',N'deleteFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DocumentStorageManagement',N'DocumentStorage',N'downloadFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DocumentStorageManagement',N'DocumentStorage',N'uploadFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawingManagement',N'Drawing',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawingManagement',N'Drawing',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawingManagement',N'Drawing',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawingManagement',N'Drawing',N'getOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawingManagement',N'Drawing',N'updateMultipleDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawRestrictionManagement',N'DrawRestriction',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawRestrictionManagement',N'DrawRestriction',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawRestrictionManagement',N'DrawRestriction',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'DrawRestrictionManagement',N'DrawRestriction',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'getById',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'getList',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'getByName',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'getOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Entity',N'getByLastName',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityAddress',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityAddress',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityAddress',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityAddress',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'updateForIndividualParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'deleteForBusinessParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'createForIndividualParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'deleteForIndividualParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'updateForBusinessParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityContact',N'createForBusinessParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityRelation',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntityRelation',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'EntitySearch',N'getResults',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'FinancialInformation',N'UpdateForBusinessParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'FinancialInformation',N'UpdateForIndividualParty',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Group',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'Group',N'getOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'GroupAddress',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'GroupContact',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'GroupContact',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'GroupContact',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'RiskRating',N'updateByPartyId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'RiskRating',N'getByPartyId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'EntityManagement',N'RiskRating',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExposureManagement',N'Exposure',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExposureManagement',N'Exposure',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalServicesHistoryManagement',N'ExternalServicesHistory',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalServicesHistoryManagement',N'ExternalServicesHistory',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalServicesHistoryManagement',N'ExternalServicesHistory',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'DocumentChecklist',N'getDocumentChecklist',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'updateMortgageSimulation',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'getOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'getInflightProductDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FacilityManagement',N'Facility',N'startSimulationProcess',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'factFindManagement',N'factFind',N'updateFactFind',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FeeManagement',N'Borrower',N'deleteFee',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FeeManagement',N'Borrower',N'getFee',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FeeManagement',N'Borrower',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FeeManagement',N'Borrower',N'updateFee',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FinancialResultManagement',N'FinancialResult',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FinancialResultManagement',N'FinancialResult',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FinancialResultManagement',N'FinancialResult',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingManagement',N'Funding',N'updateFacility',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingManagement',N'Funding',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingManagement',N'Funding',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingManagement',N'Funding',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingManagement',N'Funding',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingPositionManagement',N'FundingPosition',N'updateFundingPosition',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'FundingPositionManagement',N'FundingPosition',N'deleteFundingPosition',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'MortgageSimulationManagement',N'MortgageSimulation',N'generateDocument',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'MortgageSimulationManagement',N'MortgageSimulation',N'getMaximumRepaymentTerm',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'MortgageSimulationManagement',N'MortgageSimulation',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NarrativeManagement',N'Narrative',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NarrativeManagement',N'Narrative',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NarrativeManagement',N'Narrative',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NarrativeManagement',N'Narrative',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NotaryManagement',N'Notary',N'deleteNotary',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NotaryManagement',N'Notary',N'getNotary',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NotaryManagement',N'Notary',N'updateNotary',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'NotaryManagement',N'Notary',N'createNotary',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'BusinessPartyInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'BusinessPartyInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'BusinessPartyInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CollateralInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CollateralInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CollateralInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CollateralInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CustomerInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CustomerInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'CustomerInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'submitDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'getbypk',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'saveDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'get',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DealRequest',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DocumentInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DocumentInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DocumentInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'DocumentInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'FacilityInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'FacilityInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'FacilityInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'FacilityInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'GroupInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'GroupInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'IndividualPartyInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'IndividualPartyInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'IndividualPartyInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'IndividualPartyInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'ModifiedFacilityInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'ModifiedFacilityInfo',N'getActiveFacility',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'ModifiedFacilityInfo',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'RelatedPartyInfo',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'RelatedPartyInfo',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'RelatedPartyInfo',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OnboardingManagement',N'RelatedPartyInfo',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'updateCondition',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'deleteCondition',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDecisionManagement',N'OriginationDecision',N'createCondition',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDocumentManagement',N'OriginationDocument',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDocumentManagement',N'OriginationDocument',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDocumentManagement',N'OriginationDocument',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationDocumentManagement',N'OriginationDocument',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Application',N'createApplication',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Application',N'getInitiatedApplicationsForUser',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Email',N'SendEmailToCustomer',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Prospect',N'updateProspectProfile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Prospect',N'createOnboardingProspect',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationManagement',N'Prospect',N'createProspectProfile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationUserManagement',N'OriginationUser',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationUserManagement',N'OriginationUser',N'getKeyCloakDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'OriginationUserManagement',N'OriginationUser',N'getPermissionsForlegalEntities',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PolicyExceptionManagement',N'PolicyException',N'create',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PolicyExceptionManagement',N'PolicyException',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PolicyExceptionManagement',N'PolicyException',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PricingManagement',N'Pricing',N'deleteDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PricingManagement',N'Pricing',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PricingManagement',N'Pricing',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'PricingManagement',N'Pricing',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RelatedPartyManagement',N'RelatedParty',N'deleteGroupDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RelatedPartyManagement',N'RelatedParty',N'createDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RelatedPartyManagement',N'RelatedParty',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RelatedPartyManagement',N'RelatedParty',N'deletePartyDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RelatedPartyManagement',N'RelatedParty',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RemortgageManagement',N'Remortgage',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RemortgageManagement',N'Remortgage',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getAll',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getRecentlyViewed',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getAllApplications',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getByPaginationAndFilters',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'RequestManagement',N'Request',N'getOverview',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SearchManagement',N'GlobalSearch',N'getFacilityResults',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SearchManagement',N'GlobalSearch',N'getGlobalResults',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SearchManagement',N'GlobalSearch',N'getRequestResults',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SRMSDocumentStorageManagement',N'SRMSDocumentStorage',N'downloadFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SRMSDocumentStorageManagement',N'SRMSDocumentStorage',N'deleteFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'SRMSDocumentStorageManagement',N'SRMSDocumentStorage',N'uploadFile',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getTaskActivityByProcessId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getbypk',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getTaskByTaskId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'release',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getByTeam',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'reassign',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getRecentDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getByQueue',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getNotes',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'updateDetails',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'claim',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getByRequestId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'createAdhoc',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getByUser',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'getByUserId',N'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'TaskManagement',N'Task',N'claimMultipleTasks',N'ALLOW');
GO

/* SQL Scripts for InfinityWealth - Security Attributes */
DELETE FROM [${dbxschemaname}].[service_permission_mapper] WHERE service_name IN ('PortfolioServicing', 'WealthOrder');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
(NEWID(), 'PortfolioServicing', 'AccountActivity', 'getAccountActivityOperations', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'DailyMarket', 'getDailyMarket', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Dashboard', 'getAssetList', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Dashboard', 'getWealthDashboard', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Dashboard', 'getDashboardRecentActivity', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Dashboard', 'getPortfolioList', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Dashboard', 'getDashboardGraphData', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'DownloadPDF', 'generatePDF', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'DownloadPDF', 'generatePastPropPDF', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getInvestmentProposal', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getPastProposal', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'rejectProposal', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getRiskAnalysisIP', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'confirmOrdersIP', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getOrderProposal', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getHealthIfOrderAccepted', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getConstraintsIP', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'InvestmentProposals', 'getRecommendedInstrIP', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'MarketNews', 'getTopMarketNews', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getPortfolioHoldings', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getFieldsOrder', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getPortfolioDetails', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'updateFieldsOrder', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getAllocation', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getAssetAllocation', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getInstrumentTotal', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getTransactionDetails', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getOrdersDetails', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioDetails', 'getCashAccounts', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioHealth', 'getRiskAnalysisHC', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioHealth', 'getAllocationHC', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioHealth', 'getRecommendedInstrumentsHC', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioHealth', 'getInvestmentConstraintsHC', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioHealth', 'getPortfolioHealth', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'PortfolioPerformance', 'getPortfolioPerformance', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Reports', 'getReportAndDownloadTypes', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getAllStrategies', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getStrategyAllocation', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getStrategyQuestions', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'revertStrategy', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getRecomStrat', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getSuitabilityProfile', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'submitStrategyQues', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'confirmStrategyFromQuestion', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'confirmRecomStrat', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'confirmChangeStrat', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'computeStrategy', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getRecommendedStrategy', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getMyStrategy', 'ALLOW'),
(NEWID(), 'PortfolioServicing', 'Strategies', 'getPersonalizedStrategy', 'ALLOW'),
(NEWID(), 'WealthOrder', 'CurrencyDetails', 'GetMarketRates', 'ALLOW'),
(NEWID(), 'WealthOrder', 'CurrencyDetails', 'createOrder', 'ALLOW'),
(NEWID(), 'WealthOrder', 'CurrencyDetails', 'getHistoricalData', 'ALLOW'),
(NEWID(), 'WealthOrder', 'CurrencyDetails', 'getAddCurrency', 'ALLOW'),
(NEWID(), 'WealthOrder', 'CurrencyDetails', 'createCurrencyConvertion', 'ALLOW'),
(NEWID(), 'WealthOrder', 'DailyMarket', 'getDailyMarket', 'ALLOW'),
(NEWID(), 'WealthOrder', 'Documents', 'getDocuments', 'ALLOW'),
(NEWID(), 'WealthOrder', 'DownloadOrderPDF', 'generatePDF', 'ALLOW'),
(NEWID(), 'WealthOrder', 'FavouriteInstruments', 'getUserFavouriteInstruments', 'ALLOW'),
(NEWID(), 'WealthOrder', 'FavouriteInstruments', 'getFavoriteInstruments', 'ALLOW'),
(NEWID(), 'WealthOrder', 'FavouriteInstruments', 'getSearchFavoriteInstruments', 'ALLOW'),
(NEWID(), 'WealthOrder', 'FavouriteInstruments', 'updateUserFavouriteInstruments', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'viewInstrumentTransactions', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'GetInstrumentDetails', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getstockNewsDetails', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getSearchInstrumentList', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getPricingData', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getInstrumentMinimal', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getStockNews', 'ALLOW'),
(NEWID(), 'WealthOrder', 'InstrumentDetails', 'getStockNewsStory', 'ALLOW'),
(NEWID(), 'WealthOrder', 'Order', 'cancelOrder', 'ALLOW'),
(NEWID(), 'WealthOrder', 'Order', 'createMarketOrder', 'ALLOW'),
(NEWID(), 'WealthOrder', 'Order', 'modifyOrder', 'ALLOW'),
(NEWID(), 'WealthOrder', 'PortfolioDetails', 'getPortfolioHoldings', 'ALLOW'),
(NEWID(), 'WealthOrder', 'PortfolioDetails', 'getPortfolioDetails', 'ALLOW'),
(NEWID(), 'WealthOrder', 'PortfolioDetails', 'getCashAccounts', 'ALLOW'),
(NEWID(), 'WealthOrder', 'ProductDetails', 'getProductDetailsFromId', 'ALLOW'),
(NEWID(), 'WealthOrder', 'ProductDetails', 'getProductDetails', 'ALLOW');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardlessCash','CardlessTransaction','createCardlessTransaction','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardlessCash','CardlessTransaction','getPostedCardlessTransactions','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardlessCash','CardlessTransaction','getPendingCardlessTransactions','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardlessCash','CardlessTransaction','deleteCardlessTransaction','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CheckDeposit','CheckDepositTransaction','getPostedDeposits','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CheckDeposit','CheckDepositTransaction','getPendingDeposits','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CheckDeposit','CheckDepositTransaction','createRDC','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'ChequeManagement','ChequeDetails','getDetails','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','DownloadTransactionPDF','get','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','DownloadTransactionPDF','create','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','DownloadTransactionReport','get','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','DownloadTransactionReport','downloadTransactionReport','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeAddressReq','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadSimulationResults','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeRepaymentDayReqAck','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadAccountClosureReqAckPDF','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeRepaymentAccountReqAck','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'DocumentManagement','MortgageDocumentsDownload','downloadPartialRepaymentAckPDF','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','DigitalArrangements','closeAccount','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','DigitalArrangements','submitAccountClosureServiceRequest','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','CreatePartialRepayment','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','submitChangeRepaymentAccountServiceRequest','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','submitPartialRepaymentServiceRequestOperation','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','getMortgageSimulatedResults','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','getMockSimulatedResults','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','getMortgageDrawings','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','submitChangeRepaymentDayServiceRequest','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','Mortgage','getMortgageFacilityDetails','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','mortgageDocument','downloadDocument','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','mortgageDocument','get','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','mortgageDocument','getDocuments','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','TermsAndConditions','createCustomerTNCForAccountClosure','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Holdings','TermsAndConditions','getCustomerTNCForAccountClosure','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'ServiceRequestManagement','ServiceRequest','getDetails','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardManagementServices','ApplePay','AddCard','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardManagementServices','CardIssuer','EnrollWithIssuer','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardManagementServices','GooglePay','AddCard','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardManagementServices','RequestCreditCard','applyForCreditCard','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'CardManagementServices','SamsungPay','AddCard','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'BatchProcessingObjects','AlertSubscribers','getAlertSubscribers','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'PushExternalEvents','AlertRouter','create','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'PushExternalEvents','PushEvent','create','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'PushExternalEvents','pushExternalEvent','create','ALLOW');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('4a5f3ce1-d63b-404a-9ae2-f86abb155735','AppConfigurationsObjService','LoginType','getLoginTypeConfiguration','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('24a824d8-66dc-495a-90b9-1a82e1c8a110','BrowserManagementObjService','Browser','getSupportedBrowsers','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f95f1818-fafa-4dd9-8d98-e763d4d2b242','BusinessBankingObjService','businesstype','getBusinessTypeCustomers','ViewBusinessTypeCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('dd0dd8e4-9829-4b58-acf0-5034e8cde6a7','BusinessBankingObjService','company','get','ViewCompanies');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('8aa31e50-335b-4df0-9a21-c61f2de3c49c','BusinessBankingObjService','servicedefinition','getServiceDefinitionProductIdPermissions','ViewServiceDefinition,API_ACCESS,CreateContract,UpdateContract');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('fde630ae-9528-40e4-a320-fb8f21dd6225','ContractManagementObjService','Contract','getCoreCustomerDetails','ViewContract');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('956ea4a0-ab2a-4f57-b38d-a30e8b615f27','ContractManagementObjService','Contract','updateContractStatus','UpdateContract');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('4ad6ea3a-ae59-4ac1-ac1c-f47ad213bdf8','CustomerManagementObjService','Applicant','createApplicant','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('beb39bb4-e81b-417a-88b2-96d0ad638efb','CustomerManagementObjService','Customer','checkProspectStatus','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('51cd49bd-0f3b-43d6-a5b3-35199474e940','CustomerManagementObjService','Customer','createDMSUser','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('5c54196c-7342-4110-9a00-a8b659be84fd','CustomerManagementObjService','Customer','createMember','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('5d6928c8-050c-4ecb-9e0e-fa2b70c094aa','CustomerManagementObjService','Customer','createPartyUser','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f08ed53f-9c34-410b-ba88-c18e3b168bc5','CustomerManagementObjService','Customer','CSRAssistAuthorizationForOrig','CSRAssist');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('86c7e39d-d174-4308-8a0c-8204ab6864bd','CustomerManagementObjService','Customer','CSRAssistAuthorizationNativeApplication','CSRAssist');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('293f455c-1a69-491e-8d62-dd798ab7e528','CustomerManagementObjService','Customer','CSRAssistCustomerOnboardingNewApp','CSRAssist');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c17bc8fd-e60f-44a9-a466-abad597c1fe8','CustomerManagementObjService','Customer','CustomerLegalEntitiesGetOperation','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e524b908-308f-4732-a359-295247ab312a','CustomerManagementObjService','Customer','customerSearchByUserName','ViewCustomerActivityLogs,ViewCustomer,CreateGroup,UpdateCustomerGroup,AssignCustomerGroup,UpdateGroup');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('cf358f98-7348-4027-98b3-d9827d12ff2d','CustomerManagementObjService','Customer','enrolledCustomerSearch','ViewCustomerActivityLogs,ViewCustomer,CreateGroup,UpdateCustomerGroup,AssignCustomerGroup,UpdateGroup');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('62949b2a-c7a8-44b6-affa-1102862c8c08','CustomerManagementObjService','Customer','generateCDPReport','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f9a74106-e731-4c30-af08-fffa378e1860','CustomerManagementObjService','Customer','GetCustomerAccountDetails','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e2dc7729-a890-4036-b1ad-54b2439b1ddf','CustomerManagementObjService','Customer','getCustomerIdentifiers','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('900dc4ad-583c-45cc-a564-0f7b0304ddf8','CustomerManagementObjService','Customer','getFilteredCompanies','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('74360f2e-5e2b-458a-83dd-fd23cde6d0cf','CustomerManagementObjService','Customer','searchPartyUser','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('971c5d84-333d-41c9-bc36-9d62a8c0769b','CustomerManagementObjService','Customer','updatePartyUser','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('177503a3-51d3-499d-9188-34007dfc5496','CustomerManagementObjService','Customer','UserIdSearchOperationDetailedData','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('3cb499f8-fa2a-40bb-95a4-bb142cbe2e74','CustomerManagementObjService','CustomerActions','getCoreCustomerProductRolesFeatureActionLimits','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('1136eb78-102a-4c8b-8069-dced180709d4','CustomerManagementObjService','CustomerDevice','DeleteDevice','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('77068cc4-f285-4664-8548-4755f50c0492','CustomerManagementObjService','CustomerDevice','getUserDevices','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('20efc1f7-329e-4920-ae65-45ff1d0b0a49','CustomerManagementObjService','CustomerDevice','UpdateDeviceStatus','UpdateCustomerDevice');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f492b846-e49b-4e2f-a420-718c34d97ad3','CustomerManagementObjService','DueDiligence','CreateAccountUsage','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('81b465b7-b2e0-41a8-be8d-af36816fd19c','CustomerManagementObjService','DueDiligence','CreateEmploymentDetails','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('05ea8b4b-c0e0-48d2-9f20-9602be3032d0','CustomerManagementObjService','DueDiligence','CreateTaxDetails','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('49543c93-f95b-40f8-954a-27e5841c9bcd','CustomerManagementObjService','DueDiligence','GetAccountUsage','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('2c07b454-62f7-483c-95fc-5c4b6bcba4a7','CustomerManagementObjService','DueDiligence','GetDueDiligenceDetails','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('a01f2ed3-ec76-459d-9d92-65d6d32bd526','CustomerManagementObjService','DueDiligence','GetEmploymentDetails','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('3a238aa0-e543-4a6e-94df-9b7340ca1291','CustomerManagementObjService','DueDiligence','GetTaxDetails','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('1c70650b-472b-49c0-aa4d-e4df36cd9485','CustomerManagementObjService','DueDiligence','UpdateAccountUsage','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e3b539e2-4b59-429b-9cb5-6e842610877c','CustomerManagementObjService','DueDiligence','UpdateCitizenshipDetails','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('290b5911-a6e4-4b59-b877-750525e2ddcd','CustomerManagementObjService','DueDiligence','UpdateEmploymentDetails','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e0bc1864-3ef1-4633-9920-56ac3d60005d','CustomerManagementObjService','DueDiligence','UpdateTaxDetails','UpdateCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('04106296-c5e9-4943-a57e-20a9bfd2f791','CustomerManagementObjService','InfinityUser','getInfinityUserAccountsForCorecustomer','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('bf498dfd-a72c-4c60-91f7-e9cf748d5a61','CustomerManagementObjService','InfinityUser','getInfinityUserServiceDefsRoles','ViewCustomer');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('0ee98e9a-c280-450b-a247-b8adef5367d3','DashboardObjService','Analytics','GetJWTToken','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('cc43245b-f5f4-4f19-b0a2-1f20cbab775c','DbxdbObjects','archivedmedia','createBinary','CreateNewMessage,UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('a3a5cc7d-20c4-4259-bf86-41f49210fbf0','DbxdbObjects','archivedmedia','create','CreateNewMessage,UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('985822cb-6668-4ed4-ab9a-a14fe74ae44d','DbxdbObjects','archivedmedia','deleteBinary','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('4527e2a6-5381-4b28-8a49-e156d3b85c6b','DbxdbObjects','archivedmedia','delete','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('183409bb-c5c9-4121-994f-bbda83fb54fd','DbxdbObjects','archivedmedia','getBinary','ViewMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7f22a9c1-e056-42a4-ab12-6a666b3f36f8','DbxdbObjects','archivedmedia','get','ViewMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('b4f1d153-57e1-43bc-9e36-e99b6180e7cf','DbxdbObjects','archivedmedia','updateBinary','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('dc1f5ffd-6d22-4d57-97d5-969ed9506f88','DbxdbObjects','archivedmedia','update','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('322d6a3f-d5ed-4d81-9f6b-24b196aa8fd9','DbxdbObjects','media','deleteBinary','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c01348b2-e39e-4b5d-9dbc-abf4f940b87a','DbxdbObjects','media','delete','UpdateMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('634e7d7a-2a7a-4136-b3ec-6493f4b26953','DbxdbObjects','media','get','ViewMessages,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('5fda76a0-c3c8-4e49-b775-f8f2dad431ec','DMSUserCreationObjService','DMSUser','createUser','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('2ee8a4a8-b359-4907-97f9-6d4b49814c73','FeatureObjService','feature','getServiceFee','ViewFeatureConfig');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('037c1ed3-0874-4619-ba6b-b66c9f011d1c','InternalUserApprovals','Requests','ApprovalHistory','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('59c2bdf6-7809-43d8-b07b-a3339a3842ef','InternalUserApprovals','Requests','getCounts','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('30cfb54d-c9e7-4e0b-8367-ffc6e430ce5f','InternalUserApprovals','Requests','PendingApprovals','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7168e4cc-d971-4361-9679-d6727edbcafe','InternalusersObjService','employeeFeatures','downloadEmployeeFeaturesList','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('cf773fe5-1a60-4b59-8862-609909e69250','InternalusersObjService','employeeFeatures','getAllInternalFeatureActions','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('8e05b315-648a-4bf8-8727-ef585ff550b2','InternalusersObjService','employeeFeatures','getInternalUserFeatureActions','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c2460e0f-61bb-47c4-82a5-937f0a79ff10','InternalusersObjService','employeeFeatures','getInternalUserFeatures','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('43e28aa2-6df2-4ece-8dd5-21420ae8308f','InternalusersObjService','employeeFeatures','updateInternalUserActionStatus','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('519ac53d-6e97-49ef-bf58-ac2de8abbb1a','InternalusersObjService','employeeFeatures','updateInternalUserFeatureActions','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('3580833d-fb08-4c1f-a9c1-71f1b72ebc68','InternalusersObjService','employeePermission','createEmployeePermission','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('411c673b-b788-45d8-9ca3-f2147803593f','InternalusersObjService','employeePermission','fetchLegalEntityList','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c4dda325-5447-49ad-8082-b0121885bd1c','InternalusersObjService','employeePermission','getEmployeePermissionDetails','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9f4cb79a-a434-4f10-97ea-63ecb1248177','InternalusersObjService','employeePermission','getEmployeePermissions','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e32c9cce-d87c-4ca0-9537-f602e2636e59','InternalusersObjService','employeePermission','getPermissionsByLegalEntities','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('dd6deb26-5ede-44e1-b2fb-33d72dbc2452','InternalusersObjService','employeePermission','updateEmployeePermissionDetails','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('4aa91f65-f3fa-4965-9246-9b1f6d4a9e39','InternalusersObjService','employeePermission','updateEmployeePermissionStatus','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('668b472a-b75a-4ad8-a51f-a8336805a984','InternalusersObjService','employeeRole','createEmployeeRole','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('822b4fc4-9ccd-4f77-bc80-5f5693b93bc9','InternalusersObjService','employeeRole','getEmployeeRoleDetails','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('5f222eae-f06a-4154-a3d7-69fa5b0546b4','InternalusersObjService','employeeRole','getEmployeeRoles','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('566e4ab6-5e26-4dfb-99ce-9dd8a2fb2013','InternalusersObjService','employeeRole','updateEmployeeRoleDetails','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('635cd463-5d60-4330-89a7-233570c00e07','InternalusersObjService','employeeRole','updateEmployeeRoleStatus','UpdateUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('17d345e7-820f-4251-9c3b-667609bf5b92','InternalusersObjService','internalUsers_view','downloadUserList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('8fb0dd90-150c-4b62-9c5c-6e7e80c4ec26','InternalusersObjService','internalUsers_view','generateUsersList','ViewUser');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('e1dabb5a-e1f4-4a5a-a728-b94c4add3332','KeyCloakObjService','KeycloakUsers','getKeycloakUserRole','ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('b83d6664-e909-499f-b5eb-5314d6a58a93','LicensingUnitObjService','getAndPushUsersCount','getAndPushUsersCountToMeteringStore','ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('31724506-f459-486a-b7ec-791394bb6cf8','LocationObjService','LocationObject','downloadLocationList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('476166db-d415-4ec6-b4d9-dc9611196f27','LocationObjService','LocationObject','generateLocationList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('d5389af1-8e99-457b-a960-0265328bd753','LocationObjService','LocationObject','getFilteredLocations','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f2239d12-c3e9-4e9e-b182-d8b81350b9d6','LocationObjService','LocationsUsingCSV','downloadLocationCSV','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('29606bbf-baa1-4d70-87c6-21b4cd27119d','LocationObjService','LocationsUsingCSV','generateLocationsCSV','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('1a81b750-ffaf-4d09-a661-93faedcd984f','ReportsObjService','report','displayAllColumns','ViewReports');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('cd8c05c4-bdee-4ca7-8797-97085447e2ea','ReportsObjService','report','fetchRecords','ViewReports');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('11c99c24-ed35-4fb7-9a59-34a804602fce','ReportsObjService','report','saveReports','CreateReport');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c6c88c8c-a516-4452-bdee-cab572faa9de','ReportsObjService','report','showReports','ViewReports');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('d56a580f-f06f-4312-90e6-40f3fc2efe57','RolesAndPermissionsObjService','permissions_view','downloadPermissionList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f09700b7-d183-4210-aebf-2821569fa55b','RolesAndPermissionsObjService','permissions_view','generatePermissionsList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('92204202-208b-44e4-a919-f421a81b3d05','RolesAndPermissionsObjService','role','downloadRoleList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('96b76274-f8d3-4cb9-b464-fdf5ad965be6','RolesAndPermissionsObjService','role','generateRolesList','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('3a7ee4c5-6790-4935-8c41-d41a805c2435','SCAObjectServices','Authenticator','getUserAuthenticators','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('a59e0ab4-3a0f-4b32-933a-d28f275dbcd5','SCAObjectServices','MasterReset','reset','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('ea322863-e86b-423e-b0c2-a4649f50876a','SCAObjectServices','User','updateStatus','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('8b26d03e-daf6-465b-ae4f-02854bce27b8','SCAObjectServices','UserDevice','getUserDevices','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('428f583c-18fc-4a55-815b-8d64a51ed8d2','SCAObjectServices','UserDevice','updateDeviceStatus','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f28a2d55-f30d-4b72-b9b9-76f4686d6b3c','SCAObjService','scaconfigandscenarios','createSCAScenario','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('a593ce76-f6c8-4b6a-b5f5-d8f954721441','SCAObjService','scaconfigandscenarios','deleteSCAScenario','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('b199e967-c416-4d01-a842-45ccbf8bdc46','SCAObjService','scaconfigandscenarios','editSCAScenario','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f76e86ca-1204-495b-95e2-f2072e01c92e','SCAObjService','scaconfigandscenarios','getSCAAction','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('19c5fa8d-67fe-4656-841f-1dae1e346487','SCAObjService','scaconfigandscenarios','getSCAFeature','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('d44c36b4-5759-4aca-8f75-f643c967214a','SCAObjService','scaconfigandscenarios','getSCAMode','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('6b32fac9-3c08-481d-a1d7-82569f14c6d3','SCAObjService','scaconfigandscenarios','getSCAScenario','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('1ffcc367-0fdd-4a47-99d1-0d400c30fa3a','SCAObjService','scaconfigandscenarios','testSCAService','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('371f4fcc-81e4-40ab-8086-6f0f8ed4519b','TermsAndConditionsObjService','termsandconditions','getRequiredTermsAndConditions','ViewAppContent');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('2196c294-ec18-4a29-8f2e-ef33f0b567f9','DeleteUserSessionObjService','session','deleteUserSession','API_ACCESS');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_2',N'updateSourceOfFunds',N'ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_2',N'updateT24SourceOfFunds',N'ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_2',N'updateT24AssestLiabilities',N'ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_2',N'updateAssetLiabilities',N'ALLOW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_2',N'updateEmployment',N'ALLOW,API_ACCESS');
UPDATE [${dbxschemaname}].service_permission_mapper SET [${dbxschemaname}].service_permission_mapper.permissions='API_ACCESS,ALLOW' WHERE operation = 'createCustomerInPartyandT24' and service_name='ExternalUserManagement';
UPDATE [${dbxschemaname}].service_permission_mapper SET [${dbxschemaname}].service_permission_mapper.permissions='API_ACCESS,ALLOW' WHERE operation = 'updateCustomerInPartyAndT24' and service_name='ExternalUserManagement';
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Utility','LegalEntity','UpdateCurrentEntityInCache','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Utility','LegalEntity','getLegalEntities','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Utility','LegalEntity','GetAllEntitiesOfCustomer','ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] VALUES (NEWID(),'Utility','LegalEntity','UpdateDefaultEntity','ALLOW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (NEWID(),N'ExternalUserManagement',N'ExternalUsers_1',N'updateInfinityProspect',N'ALLOW,API_ACCESS');
GO
UPDATE [${dbxschemaname}].service_permission_mapper SET [${dbxschemaname}].service_permission_mapper.permissions='UpdateMessages,CreateNewMessage,ViewMessages' WHERE service_name ='CustomerManagementObjService' and object_name ='CustomerRequest' and operation ='getCustomerRequests';
GO

/* Updating NON-MONETARY to MONETARY including action limits for INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE action */
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES 
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'MY_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL');
GO

UPDATE [${dbxschemaname}].[feature] SET [Type_id] = 'MONETARY' WHERE [id] = 'INVESTMENT_PROPOSAL';

UPDATE [${dbxschemaname}].[featureaction] SET [isAccountLevel] = '1', [accesspolicyId] = 'CREATE', [actionlevelId] = 'ACCOUNT_LEVEL' WHERE [id] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE [serviceDefinitionId] = '90356097-7fdf-4b8c-89bd-8a1065338a97' AND [actionId] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES 
(NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL');

DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE [serviceDefinitionId] = 'f85d8392-9afe-4128-b23e-a370f138784f' AND [actionId] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES 
(NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL');

DELETE FROM [${dbxschemaname}].[groupactionlimit] WHERE [Group_id] = '4dd6183f-61d2-410f-8a4f-871af67ac933' AND [Action_id] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES 
(NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL');

DELETE FROM [${dbxschemaname}].[groupactionlimit] WHERE [Group_id] = 'a759860a-683a-4d41-81f8-fbd97d53b608' AND [Action_id] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES 
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'ALL');

DELETE FROM [${dbxschemaname}].[dependentactions] WHERE [actionId] = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName], [createdby], [createdts], [lastmodifiedts]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL','Investment Proposal New Proposal Create','Investment Proposal','InfinityWealth', GETDATE(), GETDATE());
GO

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'ALLOW,API_ACCESS' WHERE [service_name] = 'MultiEntityObjService' AND [object_name] = 'MultiEntity' AND [operation] = 'getLegalEntities';
GO

update [${dbxschemaname}].[service_permission_mapper] set [permissions]='ViewUser,AssignUserPermission,ModifyUserStatus,UpdateMessages,CreateNewMessage,ViewMessages,ViewCustomer,API_ACCESS' where [service_name] = 'InternalusersObjService' and [object_name]='internalUsers_view' and [operation]='GetUsers';
GO

DELETE FROM [${dbxschemaname}].[configurations] where [configuration_id] = '172' and [config_key] = 'ACCOUNT_TYPES';

INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdby]) VALUES ('172', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACCOUNT_TYPES', 'Account Types', '{"SAVINGS.PLAN":"Deposit","NOTICE.ACCOUNT":"Savings","FIXED.RATE.DRAWINGS": "Mortgage","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"Mortgages","BB.SMALL.BUSINESS.LOAN":"Loan","BB.PREMIUM.ACCOUNT":"Checking","BB.STANDARD.ACCOUNT":"Checking","BB.START.UP.ACCOUNT":"Checking","RES.SAVINGS.ACCOUNT-20200101":"Savings","RES.NOTICE.ACCOUNT-20200101":"Savings","RES.FCY.SAVINGS.ACCOUNT-20200101":"Savings","RES.SAVINGS.PARENT-20200101":"Savings","RES.CURRENT.ACCOUNT-20200101":"Checking","RES.PREMIUM.ACCOUNT-20200101":"Checking","RES.MULTI.CURRENCY.ACCOUNT-20200101":"Checking","RES.CURRENT.PARENT-20200101":"Checking","RES.MCY.SUB.ACCOUNT-20200101":"Checking","RES.SHORT.TERM.AUTO.ROLL-20200101":"Deposit","RES.SHORT.TERM.MANUAL.ROLL-20200101":"Deposit","RES.LONG.TERM.FLEXI.PAY-20200101":"Deposit","RES.LONG.TERM.HIGH.EARN-20200101":"Deposit","RES.SAVINGS.PLAN-20200101":"Deposit","RES.CONSUMER.LOAN-20200101":"Loan","RES.CREDIT.LINE.PARENT-20200101":"Loan","RES.MORTGAGE.FACILITIES-20210101":"Mortgages","RES.10Y.FIXED.LINEAR-20210101":"Mortgages","RES.2Y.FIXED.CONST-20210101":"Mortgages","RES.5Y.TRACKER.CONST-20210101":"Mortgages","RES.BNPL.FACILITY-20210101":"Loan","RES.PAYIN.3-20210101":"Loan","RES.PAYIN.3M-20210101":"Loan","RES.PAST.PURCHASE-20210101":"Loan","RES.CONSUMER.LOAN.PARENT-20200101":"Loan","RES.CREDIT.LINE-20200101":"Loan"}', 'SERVER', '0', 'UID10');
GO


update [${dbxschemaname}].servicecommunication set value ='InfinitySupportEscalation@temenos.com', Description = 'InfinitySupportEscalation@temenos.com Desc' where Type_id ='COMM_TYPE_EMAIL';

update [${dbxschemaname}].servicecommunication set value ='1877-777-7684', Description = '1877-777-7684' where Type_id ='COMM_TYPE_PHONE';

GO

DELETE FROM [${dbxschemaname}].[configurations] where [configuration_id] = '172' and [config_key] = 'ACCOUNT_TYPES';

INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdby]) VALUES ('172', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACCOUNT_TYPES', 'Account Types', '{"SAVINGS.PLAN":"Deposit","NOTICE.ACCOUNT":"Savings","FIXED.RATE.DRAWINGS": "Mortgage","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"Mortgages","BB.SMALL.BUSINESS.LOAN":"Loan","BB.PREMIUM.ACCOUNT":"Checking","BB.STANDARD.ACCOUNT":"Checking","BB.START.UP.ACCOUNT":"Checking","RES.SAVINGS.ACCOUNT-20200101":"Savings","RES.NOTICE.ACCOUNT-20200101":"Savings","RES.FCY.SAVINGS.ACCOUNT-20200101":"Savings","RES.SAVINGS.PARENT-20200101":"Savings","RES.CURRENT.ACCOUNT-20200101":"Checking","RES.PREMIUM.ACCOUNT-20200101":"Checking","RES.MULTI.CURRENCY.ACCOUNT-20200101":"Checking","RES.CURRENT.PARENT-20200101":"Checking","RES.MCY.SUB.ACCOUNT-20200101":"Checking","RES.SHORT.TERM.AUTO.ROLL-20200101":"Deposit","RES.SHORT.TERM.MANUAL.ROLL-20200101":"Deposit","RES.LONG.TERM.FLEXI.PAY-20200101":"Deposit","RES.LONG.TERM.HIGH.EARN-20200101":"Deposit","RES.SAVINGS.PLAN-20200101":"Deposit","RES.CONSUMER.LOAN-20200101":"Loan","RES.CREDIT.LINE.PARENT-20200101":"Loan","RES.MORTGAGE.FACILITIES-20210101":"Mortgages","RES.10Y.FIXED.LINEAR-20210101":"Mortgages","RES.2Y.FIXED.CONST-20210101":"Mortgages","RES.5Y.TRACKER.CONST-20210101":"Mortgages","RES.BNPL.FACILITY-20210101":"Loan","RES.PAYIN.3-20210101":"Loan","RES.PAYIN.3M-20210101":"Loan","RES.PAST.PURCHASE-20210101":"Loan","RES.CONSUMER.LOAN.PARENT-20200101":"Loan","RES.CREDIT.LINE-20200101":"Loan"}', 'SERVER', '0', 'UID10');
GO


update [${dbxschemaname}].servicecommunication set value ='InfinitySupportEscalation@temenos.com', Description = 'InfinitySupportEscalation@temenos.com Desc' where Type_id ='COMM_TYPE_EMAIL';

update [${dbxschemaname}].servicecommunication set value ='1877-777-7684', Description = '1877-777-7684' where Type_id ='COMM_TYPE_PHONE';

GO
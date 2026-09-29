INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID422', '0');

INSERT INTO `alertmessagetypeconfig` (`messagetype`, `alerttype`, `alertsubtype`, `subject`) VALUES ('8010', 'ACCOUNT', 'ACCOUNT_LOAD', 'AlertRouter');

UPDATE `alertrecipienttype` SET `inputparamsmapping` = '{\"accountId\":\"accountnumber\",\"legalEntityId\":\"companyLegalUnit\"}' WHERE (`id` = '3');
UPDATE `alertrecipienttype` SET `inputparamsmapping` = '{  \"coreCustomerId\": \"corecustomerid\",  \"id\": \"customerId\",\"legalEntityId\":\"companyLegalUnit\"}' WHERE (`id` = '2');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'ApprovalMatrix','ApprovalMatrix','getApprovalMatrixByContractId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'SignatoryObject','ApprovalMode','deleteApprovalMode','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'SignatoryObject','ApprovalMode','fetchApprovalMode','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'SignatoryObject','ApprovalMode','updateApprovalMode','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','editAccountSweep','ACCOUNT_SWEEP_EDIT');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','getAccountSweeps','ACCOUNT_SWEEP_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','deleteAccountSweep','ACCOUNT_SWEEP_DELETE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','initiateDownloadAccountSweeps','ACCOUNT_SWEEP_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','createAccountSweep','ACCOUNT_SWEEP_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'AccountSweepsObjects','AccountSweeps','getAccountSweepById','ACCOUNT_SWEEP_VIEW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'BulkWireObjects','BulkWireFile','downloadFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'BulkWireObjects','BulkWireFile','downloadSampleFile','ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'LoanPayoff','Packaging','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'LoanPayoff','Packaging','get','ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','BankDetails','isValidIBAN','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','BankDetails','getBankDetailsFromBicCode','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','BankDetails','GetBankNameByRoutingNumber','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','BankDetails','getSwiftCode','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','BankDetails','getBICFromBankDetails','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','Payee_Name','getPayeeName','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','Payees','getIntraInterBankPayee','PAYEE_MANAGEMENT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeManagement','Payees','getPayeesList','PAYEE_MANAGEMENT_VIEW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeObjects','Recipients','createP2PPayee','PAYEE_MANAGEMENT_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeObjects','Recipients','deleteP2PPayee','PAYEE_MANAGEMENT_DELETE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeObjects','Recipients','editP2PPayee','PAYEE_MANAGEMENT_EDIT');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'PayeeObjects','Recipients','getP2PPayee','PAYEE_MANAGEMENT_VIEW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'QRPayments','QRPay','CreateQRPayment','QR_PAYMENTS_CREATE');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','Activity','getToExternalAccountTransactions','INTRA_BANK_FUND_TRANSFER_VIEW,INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','Activity','getUserWiredTransactions','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','Activity','getRecipientWireTransaction','DOMESTIC_WIRE_TRANSFER_VIEW,INTERNATIONAL_WIRE_TRANSFER_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','BankDate','getBankDate','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','CreditCard','getCreditCardAccounts','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','CreditCard','createCreditCardTransfer','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'TransactionObjects','Transaction','P2PTransfer','P2P_CREATE');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','DirectDebits','getDirectDebits','DIRECT_DEBIT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','DirectDebits','stopNextPayment','DIRECT_DEBIT_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','DirectDebits','cancelDirectDebit','DIRECT_DEBIT_CANCEL');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','OnboardingTransactions','createOnboardingTransfer','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','UpcomingTransactions','getScheduledTransactions','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'Transfers','WireTransfer','getUserWiredTransactions','ALLOW');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SET_DEFAULT_ENTITY_PREFERENCE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

UPDATE `dbxdb`.`alertsubtype` SET `alertConditionId` = 'LESS_THAN' WHERE (`id` = 'MINIMUM_BALANCE');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','resume','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','updateTermsAndConditions','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','getApplicationTypeById','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','submit','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','updateLastEditedSection','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','updateConsents','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationManagement','ApplicationJourney','getData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','updateInfo','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','deleteInfoAddress','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','deleteInfo','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','updateRelatedInfo','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','getInfo','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','updateInfoAddress','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsCompanyManagement','Company','getAddress','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','Address','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','Address','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','getRecentApplications','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','updateApplication','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','getCoApplicantStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','deleteCoApplicants','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','updateCoApplicantStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','ApplicationData','getAllCoApplicantStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','FinancialInformation','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','FinancialInformation','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','Identity','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','Identity','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','IncomeAndEmployment','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','IncomeAndEmployment','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','updateProspectProfile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','sendInvite','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','createCoApplicants','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','getCoApplicantsBasicInfo','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsManagement','PersonalInfo','createProspectProfile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsUsersManagement','Party','getPartyAndDueDiligenceDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyDetailsUsersManagement','Prospect','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Messaging','OTP','verifyMFAOTP','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Messaging','OTP','isMFAEnabled','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Messaging','OTP','requestMFAOTP','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','AuthID','initiateDoc','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','AuthID','getToken','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','AuthID','getDocumentDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','AuthID','getDocumentStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','IDV','executeIDV','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','IDV','verifyResponse','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Thirdparty','Selfie','updateSelfieScoreAndResult','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OrigIntegrationsManagement','OrigIntegrations','withdrawApplication','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OrigIntegrationsManagement','OrigIntegrations','getCollateral','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OrigIntegrationsManagement','OrigIntegrations','getRequestId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DashboardProductsManagement','Product','getProductSelectionDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','addApplicationEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','useApplicationEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','submitApplicationEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','downloadAssistDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','getApplicationFulfilment','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','createApplicationFulfilment','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DMS','DocumentStorage','deleteApplicationEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EmailManagement','SendEmail','sendResumeMail','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PartyMSObjects','PartyRelations','getPartyRelations','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'UsersManagement','User','getExternalUsers','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'UsersManagement','User','getDetailsFromPartyAndT24','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'UsersManagement','User','getCollateral','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'UsersManagement','User','signout','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'UsersManagement','User','getMortgageApplications','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','Collateral','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','Collateral','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','Collateral','getOPMSDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','FundingPosition','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','FundingPosition','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','MortgageComposition','generateDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','MortgageComposition','updatePaymentSchedule','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','MortgageComposition','getMortgageXAI','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','ProductSelection','updateProductSelection','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','ProductSelection','getProductsForPurpose','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','ProductSelection','getProductSelection','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ProductsSelectionManagement','ProductSelection','getAllCDPlans','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Account','Account','getHoldings','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Funding','Funding','getSelectedProducts','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Funding','Funding','getType','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Funding','Funding','updateData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Funding','Funding','getProducts','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingThirdPartyMngmnt','ExternalAccount','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EligibilityApplication','ApplicationEligibility','updateApplication','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EligibilityApplication','ApplicationEligibility','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','updateUserActionBulk','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','getUserActions','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','createPreRequirements','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','getDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','updateUserAction','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','signalProcess','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','notifyRM','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsManagement','UserAction','uploadDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','getRequiredDocuments','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','uploadMultipleDocumentForChecklist','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','getDocumentChecklist','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','downloadDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','deleteEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','deleteDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','getDocumentsData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','submitEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','uploadDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','useEvidence','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'Document','DocumentChecklist','uploadMultipleDocuments','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ApplicationReviewManagement','DocumentList','downloadDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AdditionInstructionsManagement','AdditionalInstruction','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AdditionInstructionsManagement','AdditionalInstruction','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AdditionInstructionsManagement','AdditionalInstruction','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'adminconsoleService','BundleConfigurations','fetchConfigurations','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'adminconsoleService','Location','getCountries','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','approveRejectServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','getServiceRequestTask','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','getServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','claimServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','updateServiceRequestTask','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','getReferenceData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','updateRepaymentDate','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','getServiceRequestOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','getRequestByPagination','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'AssistServiceRequestManagement','ServiceRequest','updateDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'BridgeLoanManagement','BridgeLoan','createBridgeLoan','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'BridgeLoanManagement','BridgeLoan','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'BridgeLoanManagement','BridgeLoan','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CashflowPredictionManagement','cashflowPrediction','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralManagement','Collateral','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralManagement','Collateral','getAllDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralManagement','Collateral','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralManagement','Collateral','createCollateral','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralRelationshipManagement','CollateralRelationship','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralRelationshipManagement','CollateralRelationship','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralRelationshipManagement','CollateralRelationship','updateDetailsForFacility','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CollateralRelationshipManagement','CollateralRelationship','updateMultipleDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ComplianceManagement','Compliance','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ComplianceManagement','Compliance','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ComplianceManagement','Compliance','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ConsentsManagement','Consents','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','AccountRequest','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','AccountRequest','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','AccountRequest','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','AccountRequest','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','EntityOverview','getDiscrepentData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','RiskScoreCard','getRiskScoreCard','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','ScoreCard','updateScoreCard','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOS','ScoreCard','getScoreCard','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOSConfigurations','GenericConfigurations','get','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOSConfigurations','GenericConfigurations','uploadi18nConfigurations','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOSReferenceData','ReferenceData','getDocumentCategories','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CorporateLOSReferenceData','ReferenceData','getReferenceData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CovenantManagement','Covenant','updateFacility','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CovenantManagement','Covenant','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CovenantManagement','Covenant','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CovenantManagement','Covenant','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CovenantManagement','Covenant','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','getAllCustomerActionMetaData','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','reinitiateCustomerAction','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','createCustomerAction','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','getAllEntities','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','updateCustomerActionStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'CustomerActionsMngmntObjSrvc','CustomerActions','getCustomerActionsByRequest','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DocumentStorageManagement','DocumentStorage','deleteFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DocumentStorageManagement','DocumentStorage','downloadFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DocumentStorageManagement','DocumentStorage','uploadFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawingManagement','Drawing','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawingManagement','Drawing','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawingManagement','Drawing','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawingManagement','Drawing','getOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawingManagement','Drawing','updateMultipleDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawRestrictionManagement','DrawRestriction','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawRestrictionManagement','DrawRestriction','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawRestrictionManagement','DrawRestriction','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'DrawRestrictionManagement','DrawRestriction','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','getById','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','getList','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','getByName','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','getOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Entity','getByLastName','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityAddress','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityAddress','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityAddress','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityAddress','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','updateForIndividualParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','deleteForBusinessParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','createForIndividualParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','deleteForIndividualParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','updateForBusinessParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityContact','createForBusinessParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityRelation','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntityRelation','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','EntitySearch','getResults','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','FinancialInformation','UpdateForBusinessParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','FinancialInformation','UpdateForIndividualParty','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Group','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','Group','getOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','GroupAddress','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','GroupContact','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','GroupContact','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','GroupContact','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','RiskRating','updateByPartyId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','RiskRating','getByPartyId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'EntityManagement','RiskRating','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExposureManagement','Exposure','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExposureManagement','Exposure','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalServicesHistoryManagement','ExternalServicesHistory','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalServicesHistoryManagement','ExternalServicesHistory','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalServicesHistoryManagement','ExternalServicesHistory','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','DocumentChecklist','getDocumentChecklist','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','updateMortgageSimulation','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','getOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','getInflightProductDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FacilityManagement','Facility','startSimulationProcess','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'factFindManagement','factFind','updateFactFind','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FeeManagement','Borrower','deleteFee','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FeeManagement','Borrower','getFee','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FeeManagement','Borrower','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FeeManagement','Borrower','updateFee','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FinancialResultManagement','FinancialResult','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FinancialResultManagement','FinancialResult','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FinancialResultManagement','FinancialResult','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingManagement','Funding','updateFacility','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingManagement','Funding','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingManagement','Funding','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingManagement','Funding','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingManagement','Funding','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingPositionManagement','FundingPosition','updateFundingPosition','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'FundingPositionManagement','FundingPosition','deleteFundingPosition','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'MortgageSimulationManagement','MortgageSimulation','generateDocument','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'MortgageSimulationManagement','MortgageSimulation','getMaximumRepaymentTerm','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'MortgageSimulationManagement','MortgageSimulation','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NarrativeManagement','Narrative','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NarrativeManagement','Narrative','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NarrativeManagement','Narrative','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NarrativeManagement','Narrative','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NotaryManagement','Notary','deleteNotary','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NotaryManagement','Notary','getNotary','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NotaryManagement','Notary','updateNotary','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'NotaryManagement','Notary','createNotary','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','BusinessPartyInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','BusinessPartyInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','BusinessPartyInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CollateralInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CollateralInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CollateralInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CollateralInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CustomerInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CustomerInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','CustomerInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','submitDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','getbypk','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','saveDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','get','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DealRequest','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DocumentInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DocumentInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DocumentInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','DocumentInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','FacilityInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','FacilityInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','FacilityInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','FacilityInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','GroupInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','GroupInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','IndividualPartyInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','IndividualPartyInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','IndividualPartyInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','IndividualPartyInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','ModifiedFacilityInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','ModifiedFacilityInfo','getActiveFacility','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','ModifiedFacilityInfo','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','RelatedPartyInfo','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','RelatedPartyInfo','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','RelatedPartyInfo','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OnboardingManagement','RelatedPartyInfo','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','updateCondition','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','deleteCondition','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDecisionManagement','OriginationDecision','createCondition','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDocumentManagement','OriginationDocument','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDocumentManagement','OriginationDocument','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDocumentManagement','OriginationDocument','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationDocumentManagement','OriginationDocument','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Application','createApplication','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Application','getInitiatedApplicationsForUser','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Email','SendEmailToCustomer','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Prospect','updateProspectProfile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Prospect','createOnboardingProspect','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationManagement','Prospect','createProspectProfile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationUserManagement','OriginationUser','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationUserManagement','OriginationUser','getKeyCloakDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'OriginationUserManagement','OriginationUser','getPermissionsForlegalEntities','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PolicyExceptionManagement','PolicyException','create','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PolicyExceptionManagement','PolicyException','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PolicyExceptionManagement','PolicyException','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PricingManagement','Pricing','deleteDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PricingManagement','Pricing','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PricingManagement','Pricing','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'PricingManagement','Pricing','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RelatedPartyManagement','RelatedParty','deleteGroupDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RelatedPartyManagement','RelatedParty','createDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RelatedPartyManagement','RelatedParty','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RelatedPartyManagement','RelatedParty','deletePartyDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RelatedPartyManagement','RelatedParty','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RemortgageManagement','Remortgage','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RemortgageManagement','Remortgage','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getAll','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getRecentlyViewed','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getAllApplications','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getByPaginationAndFilters','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'RequestManagement','Request','getOverview','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SearchManagement','GlobalSearch','getFacilityResults','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SearchManagement','GlobalSearch','getGlobalResults','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SearchManagement','GlobalSearch','getRequestResults','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SRMSDocumentStorageManagement','SRMSDocumentStorage','downloadFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SRMSDocumentStorageManagement','SRMSDocumentStorage','deleteFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'SRMSDocumentStorageManagement','SRMSDocumentStorage','uploadFile','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getTaskActivityByProcessId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getbypk','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getTaskByTaskId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','release','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getByTeam','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','reassign','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getRecentDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getByQueue','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getNotes','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','updateDetails','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','claim','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getByRequestId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','createAdhoc','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getByUser','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','getByUserId','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'TaskManagement','Task','claimMultipleTasks','ALLOW');

/* SQL Scripts for InfinityWealth - Security Attributes */
DELETE FROM dbxdb.service_permission_mapper WHERE service_name IN ('PortfolioServicing', 'WealthOrder');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
(UUID(), 'PortfolioServicing', 'AccountActivity', 'getAccountActivityOperations', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'DailyMarket', 'getDailyMarket', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Dashboard', 'getAssetList', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Dashboard', 'getWealthDashboard', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Dashboard', 'getDashboardRecentActivity', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Dashboard', 'getPortfolioList', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Dashboard', 'getDashboardGraphData', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'DownloadPDF', 'generatePDF', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'DownloadPDF', 'generatePastPropPDF', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getInvestmentProposal', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getPastProposal', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'rejectProposal', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getRiskAnalysisIP', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'confirmOrdersIP', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getOrderProposal', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getHealthIfOrderAccepted', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getConstraintsIP', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'InvestmentProposals', 'getRecommendedInstrIP', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'MarketNews', 'getTopMarketNews', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getPortfolioHoldings', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getFieldsOrder', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getPortfolioDetails', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'updateFieldsOrder', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getAllocation', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getAssetAllocation', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getInstrumentTotal', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getTransactionDetails', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getOrdersDetails', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioDetails', 'getCashAccounts', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioHealth', 'getRiskAnalysisHC', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioHealth', 'getAllocationHC', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioHealth', 'getRecommendedInstrumentsHC', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioHealth', 'getInvestmentConstraintsHC', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioHealth', 'getPortfolioHealth', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'PortfolioPerformance', 'getPortfolioPerformance', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Reports', 'getReportAndDownloadTypes', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getAllStrategies', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getStrategyAllocation', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getStrategyQuestions', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'revertStrategy', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getRecomStrat', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getSuitabilityProfile', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'submitStrategyQues', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'confirmStrategyFromQuestion', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'confirmRecomStrat', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'confirmChangeStrat', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'computeStrategy', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getRecommendedStrategy', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getMyStrategy', 'ALLOW'),
(UUID(), 'PortfolioServicing', 'Strategies', 'getPersonalizedStrategy', 'ALLOW'),
(UUID(), 'WealthOrder', 'CurrencyDetails', 'GetMarketRates', 'ALLOW'),
(UUID(), 'WealthOrder', 'CurrencyDetails', 'createOrder', 'ALLOW'),
(UUID(), 'WealthOrder', 'CurrencyDetails', 'getHistoricalData', 'ALLOW'),
(UUID(), 'WealthOrder', 'CurrencyDetails', 'getAddCurrency', 'ALLOW'),
(UUID(), 'WealthOrder', 'CurrencyDetails', 'createCurrencyConvertion', 'ALLOW'),
(UUID(), 'WealthOrder', 'DailyMarket', 'getDailyMarket', 'ALLOW'),
(UUID(), 'WealthOrder', 'Documents', 'getDocuments', 'ALLOW'),
(UUID(), 'WealthOrder', 'DownloadOrderPDF', 'generatePDF', 'ALLOW'),
(UUID(), 'WealthOrder', 'FavouriteInstruments', 'getUserFavouriteInstruments', 'ALLOW'),
(UUID(), 'WealthOrder', 'FavouriteInstruments', 'getFavoriteInstruments', 'ALLOW'),
(UUID(), 'WealthOrder', 'FavouriteInstruments', 'getSearchFavoriteInstruments', 'ALLOW'),
(UUID(), 'WealthOrder', 'FavouriteInstruments', 'updateUserFavouriteInstruments', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'viewInstrumentTransactions', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'GetInstrumentDetails', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getstockNewsDetails', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getSearchInstrumentList', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getPricingData', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getInstrumentMinimal', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getStockNews', 'ALLOW'),
(UUID(), 'WealthOrder', 'InstrumentDetails', 'getStockNewsStory', 'ALLOW'),
(UUID(), 'WealthOrder', 'Order', 'cancelOrder', 'ALLOW'),
(UUID(), 'WealthOrder', 'Order', 'createMarketOrder', 'ALLOW'),
(UUID(), 'WealthOrder', 'Order', 'modifyOrder', 'ALLOW'),
(UUID(), 'WealthOrder', 'PortfolioDetails', 'getPortfolioHoldings', 'ALLOW'),
(UUID(), 'WealthOrder', 'PortfolioDetails', 'getPortfolioDetails', 'ALLOW'),
(UUID(), 'WealthOrder', 'PortfolioDetails', 'getCashAccounts', 'ALLOW'),
(UUID(), 'WealthOrder', 'ProductDetails', 'getProductDetailsFromId', 'ALLOW'),
(UUID(), 'WealthOrder', 'ProductDetails', 'getProductDetails', 'ALLOW');


INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardlessCash','CardlessTransaction','createCardlessTransaction','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardlessCash','CardlessTransaction','getPostedCardlessTransactions','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardlessCash','CardlessTransaction','getPendingCardlessTransactions','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardlessCash','CardlessTransaction','deleteCardlessTransaction','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CheckDeposit','CheckDepositTransaction','getPostedDeposits','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CheckDeposit','CheckDepositTransaction','getPendingDeposits','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CheckDeposit','CheckDepositTransaction','createRDC','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'ChequeManagement','ChequeDetails','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','DownloadTransactionPDF','get','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','DownloadTransactionPDF','create','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','DownloadTransactionReport','get','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','DownloadTransactionReport','downloadTransactionReport','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeAddressReq','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadSimulationResults','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeRepaymentDayReqAck','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadAccountClosureReqAckPDF','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadChangeRepaymentAccountReqAck','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'DocumentManagement','MortgageDocumentsDownload','downloadPartialRepaymentAckPDF','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','DigitalArrangements','closeAccount','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','DigitalArrangements','submitAccountClosureServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','CreatePartialRepayment','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','submitChangeRepaymentAccountServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','submitPartialRepaymentServiceRequestOperation','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','getMortgageSimulatedResults','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','getMockSimulatedResults','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','getMortgageDrawings','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','submitChangeRepaymentDayServiceRequest','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','Mortgage','getMortgageFacilityDetails','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','mortgageDocument','downloadDocument','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','mortgageDocument','get','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','mortgageDocument','getDocuments','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','TermsAndConditions','createCustomerTNCForAccountClosure','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Holdings','TermsAndConditions','getCustomerTNCForAccountClosure','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'ServiceRequestManagement','ServiceRequest','getDetails','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardManagementServices','ApplePay','AddCard','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardManagementServices','CardIssuer','EnrollWithIssuer','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardManagementServices','GooglePay','AddCard','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardManagementServices','RequestCreditCard','applyForCreditCard','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'CardManagementServices','SamsungPay','AddCard','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'BatchProcessingObjects','AlertSubscribers','getAlertSubscribers','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'PushExternalEvents','AlertRouter','create','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'PushExternalEvents','PushEvent','create','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'PushExternalEvents','pushExternalEvent','create','ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4a5f3ce1-d63b-404a-9ae2-f86abb155735','AppConfigurationsObjService','LoginType','getLoginTypeConfiguration','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('24a824d8-66dc-495a-90b9-1a82e1c8a110','BrowserManagementObjService','Browser','getSupportedBrowsers','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f95f1818-fafa-4dd9-8d98-e763d4d2b242','BusinessBankingObjService','businesstype','getBusinessTypeCustomers','ViewBusinessTypeCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('dd0dd8e4-9829-4b58-acf0-5034e8cde6a7','BusinessBankingObjService','company','get','ViewCompanies');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('8aa31e50-335b-4df0-9a21-c61f2de3c49c','BusinessBankingObjService','servicedefinition','getServiceDefinitionProductIdPermissions','ViewServiceDefinition,API_ACCESS,CreateContract,UpdateContract');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('fde630ae-9528-40e4-a320-fb8f21dd6225','ContractManagementObjService','Contract','getCoreCustomerDetails','ViewContract');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('956ea4a0-ab2a-4f57-b38d-a30e8b615f27','ContractManagementObjService','Contract','updateContractStatus','UpdateContract');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4ad6ea3a-ae59-4ac1-ac1c-f47ad213bdf8','CustomerManagementObjService','Applicant','createApplicant','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('beb39bb4-e81b-417a-88b2-96d0ad638efb','CustomerManagementObjService','Customer','checkProspectStatus','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('51cd49bd-0f3b-43d6-a5b3-35199474e940','CustomerManagementObjService','Customer','createDMSUser','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('5c54196c-7342-4110-9a00-a8b659be84fd','CustomerManagementObjService','Customer','createMember','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('5d6928c8-050c-4ecb-9e0e-fa2b70c094aa','CustomerManagementObjService','Customer','createPartyUser','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f08ed53f-9c34-410b-ba88-c18e3b168bc5','CustomerManagementObjService','Customer','CSRAssistAuthorizationForOrig','CSRAssist');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('86c7e39d-d174-4308-8a0c-8204ab6864bd','CustomerManagementObjService','Customer','CSRAssistAuthorizationNativeApplication','CSRAssist');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('293f455c-1a69-491e-8d62-dd798ab7e528','CustomerManagementObjService','Customer','CSRAssistCustomerOnboardingNewApp','CSRAssist');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c17bc8fd-e60f-44a9-a466-abad597c1fe8','CustomerManagementObjService','Customer','CustomerLegalEntitiesGetOperation','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e524b908-308f-4732-a359-295247ab312a','CustomerManagementObjService','Customer','customerSearchByUserName','ViewCustomerActivityLogs,ViewCustomer,CreateGroup,UpdateCustomerGroup,AssignCustomerGroup,UpdateGroup');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cf358f98-7348-4027-98b3-d9827d12ff2d','CustomerManagementObjService','Customer','enrolledCustomerSearch','ViewCustomerActivityLogs,ViewCustomer,CreateGroup,UpdateCustomerGroup,AssignCustomerGroup,UpdateGroup');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('62949b2a-c7a8-44b6-affa-1102862c8c08','CustomerManagementObjService','Customer','generateCDPReport','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f9a74106-e731-4c30-af08-fffa378e1860','CustomerManagementObjService','Customer','GetCustomerAccountDetails','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e2dc7729-a890-4036-b1ad-54b2439b1ddf','CustomerManagementObjService','Customer','getCustomerIdentifiers','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('900dc4ad-583c-45cc-a564-0f7b0304ddf8','CustomerManagementObjService','Customer','getFilteredCompanies','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('74360f2e-5e2b-458a-83dd-fd23cde6d0cf','CustomerManagementObjService','Customer','searchPartyUser','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('971c5d84-333d-41c9-bc36-9d62a8c0769b','CustomerManagementObjService','Customer','updatePartyUser','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('177503a3-51d3-499d-9188-34007dfc5496','CustomerManagementObjService','Customer','UserIdSearchOperationDetailedData','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('3cb499f8-fa2a-40bb-95a4-bb142cbe2e74','CustomerManagementObjService','CustomerActions','getCoreCustomerProductRolesFeatureActionLimits','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1136eb78-102a-4c8b-8069-dced180709d4','CustomerManagementObjService','CustomerDevice','DeleteDevice','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('77068cc4-f285-4664-8548-4755f50c0492','CustomerManagementObjService','CustomerDevice','getUserDevices','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('20efc1f7-329e-4920-ae65-45ff1d0b0a49','CustomerManagementObjService','CustomerDevice','UpdateDeviceStatus','UpdateCustomerDevice');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f492b846-e49b-4e2f-a420-718c34d97ad3','CustomerManagementObjService','DueDiligence','CreateAccountUsage','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('81b465b7-b2e0-41a8-be8d-af36816fd19c','CustomerManagementObjService','DueDiligence','CreateEmploymentDetails','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('05ea8b4b-c0e0-48d2-9f20-9602be3032d0','CustomerManagementObjService','DueDiligence','CreateTaxDetails','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('49543c93-f95b-40f8-954a-27e5841c9bcd','CustomerManagementObjService','DueDiligence','GetAccountUsage','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('2c07b454-62f7-483c-95fc-5c4b6bcba4a7','CustomerManagementObjService','DueDiligence','GetDueDiligenceDetails','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('a01f2ed3-ec76-459d-9d92-65d6d32bd526','CustomerManagementObjService','DueDiligence','GetEmploymentDetails','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('3a238aa0-e543-4a6e-94df-9b7340ca1291','CustomerManagementObjService','DueDiligence','GetTaxDetails','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1c70650b-472b-49c0-aa4d-e4df36cd9485','CustomerManagementObjService','DueDiligence','UpdateAccountUsage','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e3b539e2-4b59-429b-9cb5-6e842610877c','CustomerManagementObjService','DueDiligence','UpdateCitizenshipDetails','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('290b5911-a6e4-4b59-b877-750525e2ddcd','CustomerManagementObjService','DueDiligence','UpdateEmploymentDetails','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e0bc1864-3ef1-4633-9920-56ac3d60005d','CustomerManagementObjService','DueDiligence','UpdateTaxDetails','UpdateCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('04106296-c5e9-4943-a57e-20a9bfd2f791','CustomerManagementObjService','InfinityUser','getInfinityUserAccountsForCorecustomer','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('bf498dfd-a72c-4c60-91f7-e9cf748d5a61','CustomerManagementObjService','InfinityUser','getInfinityUserServiceDefsRoles','ViewCustomer');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('0ee98e9a-c280-450b-a247-b8adef5367d3','DashboardObjService','Analytics','GetJWTToken','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cc43245b-f5f4-4f19-b0a2-1f20cbab775c','DbxdbObjects','archivedmedia','createBinary','CreateNewMessage,UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('a3a5cc7d-20c4-4259-bf86-41f49210fbf0','DbxdbObjects','archivedmedia','create','CreateNewMessage,UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('985822cb-6668-4ed4-ab9a-a14fe74ae44d','DbxdbObjects','archivedmedia','deleteBinary','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4527e2a6-5381-4b28-8a49-e156d3b85c6b','DbxdbObjects','archivedmedia','delete','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('183409bb-c5c9-4121-994f-bbda83fb54fd','DbxdbObjects','archivedmedia','getBinary','ViewMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7f22a9c1-e056-42a4-ab12-6a666b3f36f8','DbxdbObjects','archivedmedia','get','ViewMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b4f1d153-57e1-43bc-9e36-e99b6180e7cf','DbxdbObjects','archivedmedia','updateBinary','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('dc1f5ffd-6d22-4d57-97d5-969ed9506f88','DbxdbObjects','archivedmedia','update','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('322d6a3f-d5ed-4d81-9f6b-24b196aa8fd9','DbxdbObjects','media','deleteBinary','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c01348b2-e39e-4b5d-9dbc-abf4f940b87a','DbxdbObjects','media','delete','UpdateMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('634e7d7a-2a7a-4136-b3ec-6493f4b26953','DbxdbObjects','media','get','ViewMessages,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('5fda76a0-c3c8-4e49-b775-f8f2dad431ec','DMSUserCreationObjService','DMSUser','createUser','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('2ee8a4a8-b359-4907-97f9-6d4b49814c73','FeatureObjService','feature','getServiceFee','ViewFeatureConfig');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('037c1ed3-0874-4619-ba6b-b66c9f011d1c','InternalUserApprovals','Requests','ApprovalHistory','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('59c2bdf6-7809-43d8-b07b-a3339a3842ef','InternalUserApprovals','Requests','getCounts','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('30cfb54d-c9e7-4e0b-8367-ffc6e430ce5f','InternalUserApprovals','Requests','PendingApprovals','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7168e4cc-d971-4361-9679-d6727edbcafe','InternalusersObjService','employeeFeatures','downloadEmployeeFeaturesList','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cf773fe5-1a60-4b59-8862-609909e69250','InternalusersObjService','employeeFeatures','getAllInternalFeatureActions','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('8e05b315-648a-4bf8-8727-ef585ff550b2','InternalusersObjService','employeeFeatures','getInternalUserFeatureActions','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c2460e0f-61bb-47c4-82a5-937f0a79ff10','InternalusersObjService','employeeFeatures','getInternalUserFeatures','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('43e28aa2-6df2-4ece-8dd5-21420ae8308f','InternalusersObjService','employeeFeatures','updateInternalUserActionStatus','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('519ac53d-6e97-49ef-bf58-ac2de8abbb1a','InternalusersObjService','employeeFeatures','updateInternalUserFeatureActions','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('3580833d-fb08-4c1f-a9c1-71f1b72ebc68','InternalusersObjService','employeePermission','createEmployeePermission','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('411c673b-b788-45d8-9ca3-f2147803593f','InternalusersObjService','employeePermission','fetchLegalEntityList','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c4dda325-5447-49ad-8082-b0121885bd1c','InternalusersObjService','employeePermission','getEmployeePermissionDetails','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9f4cb79a-a434-4f10-97ea-63ecb1248177','InternalusersObjService','employeePermission','getEmployeePermissions','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e32c9cce-d87c-4ca0-9537-f602e2636e59','InternalusersObjService','employeePermission','getPermissionsByLegalEntities','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('dd6deb26-5ede-44e1-b2fb-33d72dbc2452','InternalusersObjService','employeePermission','updateEmployeePermissionDetails','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4aa91f65-f3fa-4965-9246-9b1f6d4a9e39','InternalusersObjService','employeePermission','updateEmployeePermissionStatus','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('668b472a-b75a-4ad8-a51f-a8336805a984','InternalusersObjService','employeeRole','createEmployeeRole','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('822b4fc4-9ccd-4f77-bc80-5f5693b93bc9','InternalusersObjService','employeeRole','getEmployeeRoleDetails','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('5f222eae-f06a-4154-a3d7-69fa5b0546b4','InternalusersObjService','employeeRole','getEmployeeRoles','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('566e4ab6-5e26-4dfb-99ce-9dd8a2fb2013','InternalusersObjService','employeeRole','updateEmployeeRoleDetails','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('635cd463-5d60-4330-89a7-233570c00e07','InternalusersObjService','employeeRole','updateEmployeeRoleStatus','UpdateUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('17d345e7-820f-4251-9c3b-667609bf5b92','InternalusersObjService','internalUsers_view','downloadUserList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('8fb0dd90-150c-4b62-9c5c-6e7e80c4ec26','InternalusersObjService','internalUsers_view','generateUsersList','ViewUser');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e1dabb5a-e1f4-4a5a-a728-b94c4add3332','KeyCloakObjService','KeycloakUsers','getKeycloakUserRole','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b83d6664-e909-499f-b5eb-5314d6a58a93','LicensingUnitObjService','getAndPushUsersCount','getAndPushUsersCountToMeteringStore','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('31724506-f459-486a-b7ec-791394bb6cf8','LocationObjService','LocationObject','downloadLocationList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('476166db-d415-4ec6-b4d9-dc9611196f27','LocationObjService','LocationObject','generateLocationList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d5389af1-8e99-457b-a960-0265328bd753','LocationObjService','LocationObject','getFilteredLocations','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f2239d12-c3e9-4e9e-b182-d8b81350b9d6','LocationObjService','LocationsUsingCSV','downloadLocationCSV','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('29606bbf-baa1-4d70-87c6-21b4cd27119d','LocationObjService','LocationsUsingCSV','generateLocationsCSV','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1a81b750-ffaf-4d09-a661-93faedcd984f','ReportsObjService','report','displayAllColumns','ViewReports');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cd8c05c4-bdee-4ca7-8797-97085447e2ea','ReportsObjService','report','fetchRecords','ViewReports');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('11c99c24-ed35-4fb7-9a59-34a804602fce','ReportsObjService','report','saveReports','CreateReport');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c6c88c8c-a516-4452-bdee-cab572faa9de','ReportsObjService','report','showReports','ViewReports');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d56a580f-f06f-4312-90e6-40f3fc2efe57','RolesAndPermissionsObjService','permissions_view','downloadPermissionList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f09700b7-d183-4210-aebf-2821569fa55b','RolesAndPermissionsObjService','permissions_view','generatePermissionsList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('92204202-208b-44e4-a919-f421a81b3d05','RolesAndPermissionsObjService','role','downloadRoleList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('96b76274-f8d3-4cb9-b464-fdf5ad965be6','RolesAndPermissionsObjService','role','generateRolesList','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('3a7ee4c5-6790-4935-8c41-d41a805c2435','SCAObjectServices','Authenticator','getUserAuthenticators','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('a59e0ab4-3a0f-4b32-933a-d28f275dbcd5','SCAObjectServices','MasterReset','reset','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ea322863-e86b-423e-b0c2-a4649f50876a','SCAObjectServices','User','updateStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('8b26d03e-daf6-465b-ae4f-02854bce27b8','SCAObjectServices','UserDevice','getUserDevices','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('428f583c-18fc-4a55-815b-8d64a51ed8d2','SCAObjectServices','UserDevice','updateDeviceStatus','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f28a2d55-f30d-4b72-b9b9-76f4686d6b3c','SCAObjService','scaconfigandscenarios','createSCAScenario','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('a593ce76-f6c8-4b6a-b5f5-d8f954721441','SCAObjService','scaconfigandscenarios','deleteSCAScenario','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b199e967-c416-4d01-a842-45ccbf8bdc46','SCAObjService','scaconfigandscenarios','editSCAScenario','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f76e86ca-1204-495b-95e2-f2072e01c92e','SCAObjService','scaconfigandscenarios','getSCAAction','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('19c5fa8d-67fe-4656-841f-1dae1e346487','SCAObjService','scaconfigandscenarios','getSCAFeature','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d44c36b4-5759-4aca-8f75-f643c967214a','SCAObjService','scaconfigandscenarios','getSCAMode','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('6b32fac9-3c08-481d-a1d7-82569f14c6d3','SCAObjService','scaconfigandscenarios','getSCAScenario','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1ffcc367-0fdd-4a47-99d1-0d400c30fa3a','SCAObjService','scaconfigandscenarios','testSCAService','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('371f4fcc-81e4-40ab-8086-6f0f8ed4519b','TermsAndConditionsObjService','termsandconditions','getRequiredTermsAndConditions','ViewAppContent');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ( UUID(), 'DeleteUserSessionObjService','session','deleteUserSession','API_ACCESS');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_2','updateSourceOfFunds','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_2','updateT24SourceOfFunds','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_2','updateT24AssestLiabilities','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_2','updateAssetLiabilities','ALLOW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_2','updateEmployment','ALLOW,API_ACCESS');

UPDATE `service_permission_mapper` set permissions='API_ACCESS,ALLOW' WHERE operation = 'createCustomerInPartyandT24' and service_name='ExternalUserManagement';
UPDATE `service_permission_mapper` set permissions='API_ACCESS,ALLOW' WHERE operation = 'updateCustomerInPartyAndT24' and service_name='ExternalUserManagement';

INSERT INTO `service_permission_mapper` VALUES (UUID(),'Utility','LegalEntity','UpdateCurrentEntityInCache','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Utility','LegalEntity','getLegalEntities','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Utility','LegalEntity','GetAllEntitiesOfCustomer','ALLOW');
INSERT INTO `service_permission_mapper` VALUES (UUID(),'Utility','LegalEntity','UpdateDefaultEntity','ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (UUID(),'ExternalUserManagement','ExternalUsers_1','updateInfinityProspect','ALLOW,API_ACCESS');

UPDATE `service_permission_mapper` set permissions='UpdateMessages,CreateNewMessage,ViewMessages' where service_name ='CustomerManagementObjService' and object_name ='CustomerRequest' and operation ='getCustomerRequests';

/* Updating companyLegalUnit to ALL from GB0010001 for the required InfinityWealth actions */
UPDATE `feature` SET `companyLegalUnit` = 'ALL' WHERE `id` IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');
UPDATE `featuredisplaynamedescription` SET `companyLegalUnit` = 'ALL' WHERE `Feature_id` IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');
UPDATE `featureroletype` SET `companyLegalUnit` = 'ALL' WHERE `Feature_id` IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');
UPDATE `featureaction` SET `companyLegalUnit` = 'ALL' WHERE `Feature_id` IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE `actiondisplaynamedescription` SET `companyLegalUnit` = 'ALL' WHERE `Action_id` IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE `featureactionroletype` SET `companyLegalUnit` = 'ALL' WHERE `Action_id` IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE `compositeaction` SET `companyLegalUnit` = 'ALL' WHERE `Feature_id` IN ('PORTFOLIO_HEALTH', 'REVIEW_STRATEGY', 'INVESTMENT_PROPOSAL');

UPDATE `servicedefinitionactionlimit` SET `companyLegalUnit` = 'ALL' WHERE `actionId` IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE `groupactionlimit` SET `companyLegalUnit` = 'ALL' WHERE `Action_id` IN ('CHANGE_STRATEGY_CONFIRMATION', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL_VIEW', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'MY_STRATEGY_VIEW', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'RECOMMENDED_STRATEGY_USE_VIEW', 'RECOMMENDED_STRATEGY_VIEW', 'STRATEGY_ALLOCATION_PERSONALIZE', 'STRATEGY_ALLOCATION_VIEW', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'SUITABILITY_PROFILE_VIEW');

UPDATE `actionlimit` SET `companyLegalUnit` = 'ALL' WHERE `Action_id` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

/* Updating NON-MONETARY to MONETARY including action limits for INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE action */
UPDATE `feature` SET `Type_id` = 'MONETARY' WHERE `id` = 'INVESTMENT_PROPOSAL';

UPDATE `featureaction` SET `isAccountLevel` = '1', `accesspolicyId` = 'CREATE', `actionlevelId` = 'ACCOUNT_LEVEL' WHERE `id` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

DELETE FROM `servicedefinitionactionlimit` WHERE `serviceDefinitionId` = '90356097-7fdf-4b8c-89bd-8a1065338a97' AND `actionId` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES 
(uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL');

DELETE FROM `servicedefinitionactionlimit` WHERE `serviceDefinitionId` = 'f85d8392-9afe-4128-b23e-a370f138784f' AND `actionId` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES 
(uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL');

DELETE FROM `groupactionlimit` WHERE `Group_id` = '4dd6183f-61d2-410f-8a4f-871af67ac933' AND `Action_id` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES 
(uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL');

DELETE FROM `groupactionlimit` WHERE `Group_id` = 'a759860a-683a-4d41-81f8-fbd97d53b608' AND `Action_id` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES 
(uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL'),
(uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','ALL');

DELETE FROM `dependentactions` WHERE `actionId` = 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE';

INSERT INTO `dependentactions` (`actionId`,`dependentactionId`, `featureId`, `actionName`, `featureName`, `createdby`, `createdts`, `lastmodifiedts`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', 'Investment Proposal New Proposal Create','Investment Proposal', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


UPDATE `service_permission_mapper` SET `permissions` = 'ALLOW,API_ACCESS' WHERE `service_name` = 'MultiEntityObjService' AND `object_name` = 'MultiEntity' AND `operation` = 'getLegalEntities';

update `service_permission_mapper` set permissions='ViewUser,AssignUserPermission,ModifyUserStatus,UpdateMessages,CreateNewMessage,ViewMessages,ViewCustomer,API_ACCESS' where `service_name` = 'InternalusersObjService' and `object_name`='internalUsers_view' and `operation`='GetUsers';

update servicecommunication set value ='InfinitySupportEscalation@temenos.com', Description = 'InfinitySupportEscalation@temenos.com Desc' where Type_id ='COMM_TYPE_EMAIL';

update servicecommunication set value ='1877-777-7684', Description = '1877-777-7684' where Type_id ='COMM_TYPE_PHONE';

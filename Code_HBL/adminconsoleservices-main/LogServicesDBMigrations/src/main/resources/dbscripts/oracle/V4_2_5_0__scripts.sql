-- DROP TABLE auditactivity CASCADE CONSTRAINTS;

CREATE TABLE "auditactivity" (
  "Id" VARCHAR2(255 CHAR) NOT NULL,
  "EventId" VARCHAR2(45 CHAR),
"EventType" VARCHAR2(255 CHAR),
"EventSubType" VARCHAR2(255 CHAR),
"Status_Id" VARCHAR2(255 CHAR),
"sessionId" VARCHAR2(255 CHAR),
"AppId" VARCHAR2(255 CHAR),
"UserName" VARCHAR2(255 CHAR),
"Customer_Id" VARCHAR2(255 CHAR),
"partyid" VARCHAR2(255 CHAR),
"corecustomerid" VARCHAR2(255 CHAR),
"isCSRAssist" NUMBER(3,0) NOT NULL,
"appSessionId" VARCHAR2(255 CHAR),
"payeeNickName" VARCHAR2(255 CHAR),
"relationshipNumber" VARCHAR2(255 CHAR),
"AdminUserName" VARCHAR2(255 CHAR),
"AdminUserRole" VARCHAR2(255 CHAR),
"Producer" VARCHAR2(255 CHAR),
"MoneyMovementRefId" VARCHAR2(255 CHAR),
"EventData" clob CONSTRAINT "EventData" CHECK ("EventData" IS JSON),
"creditcardnumber" VARCHAR2(255 CHAR),
"mfa_State" VARCHAR2(255 CHAR),
"mfa_ServiceKey" VARCHAR2(255 CHAR),
"mfa_Type" VARCHAR2(255 CHAR),
"nonSearchable" clob CONSTRAINT "nonSearchable" CHECK ("nonSearchable" IS JSON),
"phoneNumber" VARCHAR2(255 CHAR),
"email" VARCHAR2(255 CHAR),
"deviceModel" VARCHAR2(255 CHAR),
"operatingSystem" VARCHAR2(255 CHAR),
"browser" VARCHAR2(255 CHAR),
"deviceId" VARCHAR2(255 CHAR),
"channel" VARCHAR2(255 CHAR),
"appVersion" VARCHAR2(255 CHAR),
"platform" VARCHAR2(255 CHAR),
"ipAddress" VARCHAR2(255 CHAR),
"eventts" TIMESTAMP,
"createdby" VARCHAR2(255 CHAR),
"createdts" TIMESTAMP,
"softdeleteflag" NUMBER(3,0)
);


ALTER TABLE "auditactivity" MODIFY ("isCSRAssist" DEFAULT '0');
ALTER TABLE "auditactivity"
ADD CONSTRAINT PRIMARY_2 PRIMARY KEY
(
  "Id"
)
ENABLE
;

CREATE INDEX IDX_auditactivity_eventsubtype ON "auditactivity"
(
  "EventSubType"
) 
;
CREATE INDEX IDX_auditactivity_eventtype ON "auditactivity"
(
  "EventType"
) 
;
CREATE INDEX IDX_auditactivity_UserName ON "auditactivity"
(
  "UserName"
) 
;
CREATE INDEX IDX_auditactivity_CustomerId ON "auditactivity"
(
  "Customer_Id"
) 
;
CREATE INDEX IDX_auditactivity_moneymovementid ON "auditactivity"
(
  "MoneyMovementRefId"
) 
;
--GRANT ALL ON auditactivity TO ROLE_mysqldbxdblogs;

-- DROP TABLE moneymovementlog CASCADE CONSTRAINTS;
CREATE TABLE "moneymovementlog" (
"Id" VARCHAR2(50 CHAR) NOT NULL,
"isScheduled" CHAR(1 CHAR) NOT NULL,
"Customer_id" VARCHAR2(50 CHAR),
"ExpenseCategory_id" NUMBER(10,0),
"Payee_id" NUMBER(10,0),
"Bill_id" NUMBER(10,0),
"Type_id" NUMBER(10,0),
"Reference_id" VARCHAR2(50 CHAR),
"fromAccountNumber" VARCHAR2(50 CHAR),
"fromAccountBalance" FLOAT,
"toAccountNumber" VARCHAR2(50 CHAR),
"toAccountBalance" FLOAT,
"amount" FLOAT,
"convertedAmount" FLOAT,
"transactionCurrency" VARCHAR2(45 CHAR),
"baseCurrency" VARCHAR2(45 CHAR),
"Status_id" VARCHAR2(50 CHAR),
"statusDesc" VARCHAR2(50 CHAR),
"notes" VARCHAR2(150 CHAR),
"checkNumber" NUMBER(10,0),
"imageURL1" CLOB,
"imageURL2" CLOB,
"hasDepositImage" NUMBER(3,0),
"description" VARCHAR2(100 CHAR),
"scheduledDate" DATE,
"transactionDate" DATE,
"createdDate" DATE,
"transactionComments" VARCHAR2(100 CHAR),
"toExternalAccountNumber" VARCHAR2(45 CHAR),
"Person_Id" NUMBER(10,0),
"frequencyType" VARCHAR2(4000 CHAR) CHECK ("frequencyType" IN ('Once','Daily','Weekly','BiWeekly','Monthly','Yearly','Half Yearly','Quarterly','Every Two Weeks')),
"numberOfRecurrences" NUMBER(10,0),
"frequencyStartDate" DATE,
"frequencyEndDate" DATE,
"checkImage" CLOB,
"checkImageBack" CLOB,
"cashlessOTPValidDate" DATE,
"cashlessOTP" VARCHAR2(50 CHAR),
"cashlessPhone" VARCHAR2(50 CHAR),
"cashlessEmail" VARCHAR2(50 CHAR),
"cashlessPersonName" VARCHAR2(50 CHAR),
"cashlessMode" VARCHAR2(50 CHAR),
"cashlessSecurityCode" VARCHAR2(50 CHAR),
"cashWithdrawalTransactionStatus" VARCHAR2(50 CHAR),
"cashlessPin" VARCHAR2(50 CHAR),
"category" VARCHAR2(4000 CHAR) CHECK("category" IN ('Auto & Transport','Bills & Utilities','Business Services','Education','Entertainment','Fees & Charges','Financial','Food & Dining','Gifts & Donations','Health & Fitness','Home','Income','Investments','Kids','Personal Care','Pets','Shopping','Taxes','Transfer','Travel','Uncategorised')),
"billCategory" VARCHAR2(4000 CHAR) CHECK("billCategory" IN ('Credit Card','Phone','Utilities','Insurance')),
"recurrenceDesc" VARCHAR2(50 CHAR),
"deliverBy" VARCHAR2(50 CHAR),
"p2pContact" VARCHAR2(50 CHAR),
"p2pRequiredDate" DATE,
"requestCreatedDate" VARCHAR2(50 CHAR),
"penaltyFlag" CHAR(1 CHAR),
"payoffFlag" CHAR(1 CHAR),
"viewReportLink" VARCHAR2(150 CHAR),
"isPaypersonDeleted" CHAR(1 CHAR),
"fee" VARCHAR2(50 CHAR),
"feeCurrency" VARCHAR2(45 CHAR),
"feePaidByReceipent" CHAR(1 CHAR),
"frontImage1" VARCHAR2(100 CHAR),
"frontImage2" VARCHAR2(100 CHAR),
"backImage1" VARCHAR2(100 CHAR),
"backImage2" VARCHAR2(100 CHAR),
"checkDesc" VARCHAR2(50 CHAR),
"checkNumber1" VARCHAR2(50 CHAR),
"checkNumber2" VARCHAR2(50 CHAR),
"bankName1" VARCHAR2(50 CHAR),
"bankName2" VARCHAR2(50 CHAR),
"withdrawlAmount1" VARCHAR2(50 CHAR),
"withdrawlAmount2" VARCHAR2(50 CHAR),
"cashAmount" VARCHAR2(50 CHAR),
"payeeCurrency" VARCHAR2(50 CHAR),
"billid" NUMBER(24,0),
"isDisputed" CHAR(1 CHAR),
"disputeDescription" VARCHAR2(50 CHAR),
"disputeReason" VARCHAR2(50 CHAR),
"disputeStatus" VARCHAR2(50 CHAR),
"disputeDate" DATE,
"payeeName" VARCHAR2(50 CHAR),
"checkDateOfIssue" DATE,
"checkReason" VARCHAR2(50 CHAR),
"isPayeeDeleted" CHAR(1 CHAR),
"amountRecieved" VARCHAR2(50 CHAR),
"requestValidity" DATE,
"statementReference" VARCHAR2(35 CHAR),
"transCreditDebitIndicator" VARCHAR2(45 CHAR),
"bookingDateTime" DATE,
"valueDateTime" DATE,
"transactionInformation" VARCHAR2(500 CHAR),
"addressLine" VARCHAR2(70 CHAR),
"transactionAmount" VARCHAR2(50 CHAR),
"chargeAmount" VARCHAR2(50 CHAR),
"chargeCurrency" VARCHAR2(50 CHAR),
"sourceCurrency" VARCHAR2(45 CHAR),
"targetCurrency" VARCHAR2(50 CHAR),
"unitCurrency" VARCHAR2(50 CHAR),
"exchangeRate" VARCHAR2(45 CHAR),
"contractIdentification" VARCHAR2(35 CHAR),
"quotationDate" DATE,
"instructedAmount" VARCHAR2(45 CHAR),
"instructedCurrency" VARCHAR2(45 CHAR),
"transactionCode" VARCHAR2(35 CHAR),
"transactionSubCode" VARCHAR2(45 CHAR),
"proprietaryTransactionCode" VARCHAR2(35 CHAR),
"proprietaryTransactionIssuer" VARCHAR2(35 CHAR),
"balanceCreditDebitIndicator" VARCHAR2(45 CHAR),
"balanceType" VARCHAR2(45 CHAR),
"balanceAmount" VARCHAR2(45 CHAR),
"balanceCurrency" VARCHAR2(45 CHAR),
"merchantName" VARCHAR2(45 CHAR),
"merchantCategoryCode" VARCHAR2(45 CHAR),
"creditorAgentSchemeName" VARCHAR2(45 CHAR),
"creditorAgentIdentification" VARCHAR2(45 CHAR),
"creditorAgentName" VARCHAR2(140 CHAR),
"creditorAgentaddressType" VARCHAR2(45 CHAR),
"creditorAgentDepartment" VARCHAR2(45 CHAR),
"creditorAgentSubDepartment" VARCHAR2(45 CHAR),
"creditorAgentStreetName" VARCHAR2(45 CHAR),
"creditorAgentBuildingNumber" VARCHAR2(45 CHAR),
"creditorAgentPostCode" VARCHAR2(45 CHAR),
"creditorAgentTownName" VARCHAR2(45 CHAR),
"creditorAgentCountrySubDivision" VARCHAR2(45 CHAR),
"creditorAgentCountry" VARCHAR2(45 CHAR),
"creditorAgentAddressLine" VARCHAR2(45 CHAR),
"creditorAccountSchemeName" VARCHAR2(45 CHAR),
"creditorAccountIdentification" VARCHAR2(45 CHAR),
"creditorAccountName" VARCHAR2(45 CHAR),
"creditorAccountSeconIdentification" VARCHAR2(45 CHAR),
"debtorAgentSchemeName" VARCHAR2(45 CHAR),
"debtorAgentIdentification" VARCHAR2(45 CHAR),
"debtorAgentName" VARCHAR2(45 CHAR),
"debtorAgentAddressType" VARCHAR2(45 CHAR),
"debtorAgentDepartment" VARCHAR2(45 CHAR),
"debtorAgentSubDepartment" VARCHAR2(45 CHAR),
"debtorAgentStreetName" VARCHAR2(45 CHAR),
"debtorAgentBuildingNumber" VARCHAR2(45 CHAR),
"dedtorAgentPostCode" VARCHAR2(45 CHAR),
"debtorAgentTownName" VARCHAR2(45 CHAR),
"debtorAgentCountrySubDivision" VARCHAR2(45 CHAR),
"debtorAgentCountry" VARCHAR2(45 CHAR),
"debtorAgentAddressLine" VARCHAR2(45 CHAR),
"debtorAccountSchemeName" VARCHAR2(45 CHAR),
"debtorAccountIdentification" VARCHAR2(45 CHAR),
"debtorAccountName" VARCHAR2(45 CHAR),
"debtorAccountSeconIdentification" VARCHAR2(45 CHAR),
"cardInstrumentSchemeName" VARCHAR2(45 CHAR),
"cardInstrumentAuthorisationType" VARCHAR2(45 CHAR),
"cardInstrumentName" VARCHAR2(45 CHAR),
"cardInstrumentIdentification" VARCHAR2(45 CHAR),
"IBAN" VARCHAR2(45 CHAR),
"sortCode" VARCHAR2(45 CHAR),
"FirstPaymentDateTime" DATE,
"NextPaymentDateTime" DATE,
"FinalPaymentDateTime" DATE,
"StandingOrderStatusCode" VARCHAR2(6 CHAR),
"FP_Amount" FLOAT,
"FP_Currency" VARCHAR2(45 CHAR),
"NP_Amount" FLOAT,
"NP_Currency" VARCHAR2(45 CHAR),
"FPA_Amount" FLOAT,
"FPA_Currency" VARCHAR2(45 CHAR),
"ConsentId" VARCHAR2(45 CHAR),
"Initiation_InstructionIdentification" VARCHAR2(45 CHAR),
"Initiation_EndToEndIdentification" VARCHAR2(45 CHAR),
"RI_Reference" VARCHAR2(45 CHAR),
"RI_Unstructured" VARCHAR2(45 CHAR),
"RiskPaymentContextCode" VARCHAR2(45 CHAR),
"MerchantCustomerIdentification" VARCHAR2(200 CHAR),
"beneficiaryName" VARCHAR2(50 CHAR),
"bankName" VARCHAR2(50 CHAR),
"swiftCode" VARCHAR2(45 CHAR),
"DomesticPaymentId" VARCHAR2(45 CHAR),
"linkSelf" VARCHAR2(45 CHAR),
"StatusUpdateDateTime" DATE,
"dataStatus" VARCHAR2(45 CHAR),
"serviceName" VARCHAR2(50 CHAR),
"payPersonName" VARCHAR2(45 CHAR)
);


ALTER TABLE "moneymovementlog" MODIFY ("isScheduled" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("fromAccountBalance" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("toAccountBalance" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("amount" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("checkNumber" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("hasDepositImage" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("description" DEFAULT ' ');
ALTER TABLE "moneymovementlog" MODIFY ("frequencyType" DEFAULT 'Once');
ALTER TABLE "moneymovementlog" MODIFY ("numberOfRecurrences" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("category" DEFAULT 'Uncategorised');
ALTER TABLE "moneymovementlog" MODIFY ("penaltyFlag" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("payoffFlag" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("viewReportLink" DEFAULT 'http://pmqa.konylabs.net/KonyWebBanking/view_report.png');
ALTER TABLE "moneymovementlog" MODIFY ("isPaypersonDeleted" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("fee" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("withdrawlAmount1" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("withdrawlAmount2" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("cashAmount" DEFAULT '0.00');
ALTER TABLE "moneymovementlog" MODIFY ("payeeCurrency" DEFAULT 'INR');
ALTER TABLE "moneymovementlog" MODIFY ("isDisputed" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("isPayeeDeleted" DEFAULT '0');
ALTER TABLE "moneymovementlog" MODIFY ("amountRecieved" DEFAULT '0');
ALTER TABLE "moneymovementlog"
ADD CONSTRAINT PRIMARY_6 PRIMARY KEY
(
  "Id"
)
ENABLE
;

CREATE INDEX fromAccountNumber_1 ON "moneymovementlog"
(
  "fromAccountNumber"
) 
;
CREATE INDEX toAccountNumber_1 ON "moneymovementlog"
(
  "toAccountNumber"
) 
;
CREATE INDEX fromtoaccountnumber_1 ON "moneymovementlog"
(
  "fromAccountNumber",
  "toAccountNumber"
) 
;
CREATE INDEX type_id_1 ON "moneymovementlog"
(
  "Type_id"
) 
;
CREATE INDEX payee_id_1 ON "moneymovementlog"
(
  "Payee_id"
) 
;

--GRANT ALL ON moneymovementlog TO ROLE_mysqldbxdblogs;
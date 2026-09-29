--DROP TABLE adminactivity CASCADE CONSTRAINTS;
CREATE TABLE "adminactivity" (
  "id" VARCHAR2(50 CHAR) NOT NULL,
  "event" VARCHAR2(50 CHAR),
  "description" VARCHAR2(1000 CHAR),
  "username" VARCHAR2(50 CHAR),
  "userRole" VARCHAR2(500 CHAR),
  "moduleName" VARCHAR2(50 CHAR),
  "eventData" CLOB,
  "eventts" timestamp NOT NULL,
  "status" VARCHAR2(50 CHAR),
  "createdBy" VARCHAR2(50 CHAR),
  "createdOn" timestamp NOT NULL
);


ALTER TABLE "adminactivity" MODIFY ("eventts" DEFAULT CURRENT_TIMESTAMP);
ALTER TABLE "adminactivity" MODIFY ("createdOn" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "adminactivity"
ADD CONSTRAINT PRIMARY PRIMARY KEY
(
  "id"
)
ENABLE
;
CREATE INDEX idx_systemuseractivity_eventts ON "adminactivity"
(
  "eventts"
) 
;

CREATE INDEX idx_systemuseractivity_modulename ON "adminactivity"
(
  "moduleName"
) 
;

CREATE INDEX idx_systemuseractivity_username ON "adminactivity"
(
  "username"
) 
;

CREATE INDEX idx_systemuseractivity_userrole ON "adminactivity"
(
  "userRole"
) 
;
CREATE INDEX idx_systemuseractivity_event ON "adminactivity"
(
  "event"
) 
;

CREATE INDEX idx_systemuseractivity_status ON "adminactivity"
(
  "status"
) 
;
--GRANT ALL ON adminactivity TO ROLE_mysqldbxdblogs;

-- DROP TABLE admincustomeractivity CASCADE CONSTRAINTS;
CREATE TABLE "admincustomeractivity" (
  "id" VARCHAR2(50 CHAR) NOT NULL,
  "customerId" VARCHAR2(50 CHAR),
  "adminName" VARCHAR2(255 CHAR),
  "adminRole" VARCHAR2(50 CHAR),
  "activityType" VARCHAR2(50 CHAR),
  "description" VARCHAR2(1000 CHAR),
  "eventts" timestamp NOT NULL,
  "status" VARCHAR2(50 CHAR),
  "createdBy" VARCHAR2(50 CHAR),
  "createdOn" timestamp NOT NULL
);
ALTER TABLE "admincustomeractivity" MODIFY ("eventts" DEFAULT CURRENT_TIMESTAMP);
ALTER TABLE "admincustomeractivity" MODIFY ("createdOn" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "admincustomeractivity"
ADD CONSTRAINT PRIMARY_1 PRIMARY KEY
(
  "id"
)
ENABLE
;
CREATE INDEX idx_admincustomeractivity_adminname ON "admincustomeractivity"
(
  "adminName"
) 
;
CREATE INDEX idx_admincustomeractivity_activitytype ON "admincustomeractivity"
(
  "activityType"
) 
;
CREATE INDEX idx_admincustomeractivity_eventts ON "admincustomeractivity"
(
  "eventts"
) 
;
CREATE INDEX idx_admincustomeractivity_adminrole ON "admincustomeractivity"
(
  "adminRole"
) 
;
CREATE INDEX idx_admincustomeractivity_customerusername ON "admincustomeractivity"
(
  "customerId"
) 
;
--GRANT ALL ON admincustomeractivity TO ROLE_mysqldbxdblogs;

-- DROP TABLE customeractivity CASCADE CONSTRAINTS;

CREATE TABLE "customeractivity" (
  "id" VARCHAR2(50 CHAR) NOT NULL,
  "sessionId" VARCHAR2(100 CHAR),
  "username" VARCHAR2(50 CHAR),
  "moduleName" VARCHAR2(100 CHAR),
  "activityType" VARCHAR2(50 CHAR),
  "description" VARCHAR2(1000 CHAR),
  "eventts" timestamp NOT NULL,
  "status" VARCHAR2(50 CHAR),
  "channel" VARCHAR2(50 CHAR),
  "ipAddress" VARCHAR2(50 CHAR),
  "device" VARCHAR2(50 CHAR),
  "deviceId" VARCHAR2(100 CHAR),
  "operatingSystem" VARCHAR2(50 CHAR),
  "browser" VARCHAR2(50 CHAR),
  "referenceId" VARCHAR2(50 CHAR),
  "errorCode" VARCHAR2(50 CHAR),
  "createdBy" VARCHAR2(50 CHAR),
  "createdOn" timestamp NOT NULL,
  "customerId" VARCHAR2(50 CHAR),
  "typeOfMFA" VARCHAR2(50 CHAR),
  "payeeName" VARCHAR2(50 CHAR),
  "accountNumber" VARCHAR2(50 CHAR),
  "relationshipNumber" VARCHAR2(50 CHAR),
  "phoneNumber" VARCHAR2(50 CHAR),
  "email" VARCHAR2(50 CHAR),
  "bankName" VARCHAR2(50 CHAR),
  "maskedAccountNumber" VARCHAR2(50 CHAR)
);


ALTER TABLE "customeractivity" MODIFY ("eventts" DEFAULT CURRENT_TIMESTAMP);
ALTER TABLE "customeractivity" MODIFY ("createdOn" DEFAULT CURRENT_TIMESTAMP); 
ALTER TABLE "customeractivity"
ADD CONSTRAINT PRIMARY_5 PRIMARY KEY
(
  "id"
)
ENABLE
;
CREATE INDEX idx_customeractivity_username ON "customeractivity"
(
  "username"
) 
;
CREATE INDEX idx_customeractivity_modulename ON "customeractivity"
(
  "moduleName"
) 
;
CREATE INDEX idx_customeractivity_eventts ON "customeractivity"
(
  "eventts"
) 
;
CREATE INDEX idx_customeractivity_activitytype ON "customeractivity"
(
  "activityType"
) 
;
CREATE INDEX idx_customeractivity_status ON "customeractivity"
(
  "status"
) 
;
CREATE INDEX idx_customeractivity_channel ON "customeractivity"
(
  "channel"
) 
;
CREATE INDEX idx_customeractivity_operatingsystem ON "customeractivity"
(
  "operatingSystem"
) 
;

--GRANT ALL ON customeractivity TO ROLE_mysqldbxdblogs;

-- DROP TABLE transactionlog CASCADE CONSTRAINTS;
CREATE TABLE "transactionlog" (
  "id" VARCHAR2(50 CHAR) NOT NULL,
  "transactionId" VARCHAR2(50 CHAR),
  "username" VARCHAR2(50 CHAR),
  "payeeName" VARCHAR2(50 CHAR),
  "serviceName" VARCHAR2(50 CHAR),
  "type" VARCHAR2(50 CHAR),
  "fromAccount" VARCHAR2(50 CHAR),
  "fromAccountType" VARCHAR2(50 CHAR),
  "toAccount" VARCHAR2(50 CHAR),
  "toAccountType" VARCHAR2(50 CHAR),
  "amount" FLOAT,
  "currencyCode" VARCHAR2(50 CHAR),
  "channel" VARCHAR2(50 CHAR),
  "status" VARCHAR2(50 CHAR),
  "description" VARCHAR2(1000 CHAR),
  "routingNumber" VARCHAR2(50 CHAR),
  "batchId" VARCHAR2(45 CHAR),
  "transactionDate" timestamp NOT NULL,
  "fromMobileOrEmail" VARCHAR2(50 CHAR),
  "toMobileOrEmail" VARCHAR2(50 CHAR),
  "swiftCode" VARCHAR2(50 CHAR),
  "internationalRoutingCode" VARCHAR2(50 CHAR),
  "ibanNumber" VARCHAR2(50 CHAR),
  "createdBy" VARCHAR2(50 CHAR),
  "createdOn" timestamp NOT NULL,
  "module" VARCHAR2(255 CHAR),
  "customerId" VARCHAR2(50 CHAR),
  "device" VARCHAR2(50 CHAR),
  "operatingSystem" VARCHAR2(50 CHAR),
  "deviceId" VARCHAR2(50 CHAR),
  "ipAddress" VARCHAR2(50 CHAR),
  "referenceNumber" VARCHAR2(50 CHAR),
  "transactionDescription" VARCHAR2(1000 CHAR),
  "errorCode" VARCHAR2(50 CHAR),
  "recipientType" VARCHAR2(50 CHAR),
  "recipientBankName" VARCHAR2(50 CHAR),
  "recipientAddress" VARCHAR2(1000 CHAR),
  "recipientBankAddress" VARCHAR2(1000 CHAR),
  "checkNumber" VARCHAR2(50 CHAR),
  "cashWithdrawalFor" VARCHAR2(50 CHAR)
);


ALTER TABLE "transactionlog" MODIFY ("transactionDate" DEFAULT CURRENT_TIMESTAMP);
ALTER TABLE "transactionlog" MODIFY ("createdOn" DEFAULT CURRENT_TIMESTAMP);
ALTER TABLE "transactionlog"
ADD CONSTRAINT PRIMARY_7 PRIMARY KEY
(
  "id"
)
ENABLE
;
CREATE INDEX idx_transactionlog_username ON "transactionlog"
(
  "username"
) 
;
CREATE INDEX idx_transactionlog_status ON "transactionlog"
(
  "status"
) 
;
CREATE INDEX idx_transactionlog_type ON "transactionlog"
(
  "type"
) 
;
CREATE INDEX idx_transactionlog_transactiondate ON "transactionlog"
(
  "transactionDate"
) 
;
CREATE INDEX idx_transactionlog_amount ON "transactionlog"
(
  "amount"
) 
;
CREATE INDEX idx_transactionlog_transactionid ON "transactionlog"
(
  "transactionId"
) 
;
CREATE INDEX idx_transactionlog_servicename ON "transactionlog"
(
  "serviceName"
) 
;
CREATE INDEX idx_transactionlog_fromaccounttype ON "transactionlog"
(
  "fromAccountType"
) 
;
CREATE INDEX idx_transactionlog_toaccounttype ON "transactionlog"
(
  "toAccountType"
) 
;
CREATE INDEX idx_transactionlog_currency ON "transactionlog"
(
  "currencyCode"
) 
;
CREATE INDEX idx_transactionlog_frommobileemail ON "transactionlog"
(
  "fromMobileOrEmail"
) 
;
CREATE INDEX idx_transactionlog_tomobileemail ON "transactionlog"
(
  "toMobileOrEmail"
) 
;
CREATE INDEX idx_transactionlog_payeename ON "transactionlog"
(
  "payeeName"
) 
;
--GRANT ALL ON transactionlog TO ROLE_mysqldbxdblogs;
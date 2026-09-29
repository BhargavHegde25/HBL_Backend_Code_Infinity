UPDATE "makercheckerconfig" SET "auditModule" = 'Contract', "auditEvent" = 'Create' WHERE ("id" = '1');
UPDATE "makercheckerconfig" SET "auditModule" = 'Contract', "auditEvent" = 'Edit' WHERE ("id" = '2');
UPDATE "makercheckerconfig" SET "auditModule" = 'Customer Management', "auditEvent" = 'Enroll' WHERE ("id" = '3');
UPDATE "makercheckerconfig" SET "auditModule" = 'Customer Management', "auditEvent" = 'Edit' WHERE ("id" = '4');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CommonRequestDetails' WHERE ("id" = '1');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CommonRequestDetails' WHERE ("id" = '2');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CommonRequestDetails' WHERE ("id" = '3');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CommonRequestDetails' WHERE ("id" = '4');

INSERT INTO "permission" ("id", "Type_id", "Status_id", "Name", "Description", "isComposite", "PermissionValue") VALUES
('PID705', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ViewMakerCheckerConfigurations', 'Permission to view maker checker configurations', '0', 'TRUE');
INSERT INTO "permission" ("id", "Type_id", "Status_id", "Name", "Description", "isComposite", "PermissionValue") VALUES
('PID706', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'UpdateMakerCheckerConfigurations', 'Permission to update maker checker configurations', '0', 'TRUE');
INSERT INTO "rolepermission" ("Role_id", "Permission_id") VALUES ('RID_SUPERADMIN', 'PID705');
INSERT INTO "rolepermission" ("Role_id", "Permission_id") VALUES ('RID_SUPERADMIN', 'PID706');

INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('Guarantees', '["amount", "status", "bankName", "currency", "createdOn", "issueDate", "expiryDate", "expiryType", "productType", "governingLaw", "applicantParty", "applicableRules", "beneficiaryName", "clauseConditions", "demandAcceptance", "guaranteesSRMSId", "instructingParty", "limitInstructions", "modeOfTransaction", "beneficiaryDetails", "isSingleSettlement", "serviceRequestTime", "guaranteeAndSBLCType", "guaranteesReferenceNo", "instructionCurrencies", "amendmentNo", "returnHistory"]');
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('GuaranteeLCAmendments', '["amount", "billType", "currency", "issueDate", "expiryDate", "expiryType", "amendStatus", "amendmentNo", "productType", "amendCharges", "applicantParty", "amendExpiryType", "benificiaryName", "guaranteesSRMSId", "instructingParty", "amendRequestedDate", "amendmentReference", "amendmentEffectiveDate", "amendmentSRMSRequestId", "rejectedDate", "rejectedReason", "approvedDate", "historyCount", "reasonForReturned"]');
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('IssuedGuaranteeClaims', '["amount", "product", "claimType", "countDown", "documents", "issueDate", "demandType", "expiryDate", "expiryType", "receivedOn", "claimAmount", "claimStatus", "productType", "settledDate", "advisingBank", "claimsSRMSId", "debitAccount", "claimCurrency", "messageToBank", "paymentStatus", "tradeCurrency", "amountFormatted", "beneficiaryName", "claimAcceptance", "messageFromBank", "guaranteesSRMSId", "newExtensionDate", "unUtilizedAmount", "otherDemandDetails", "reasonForRejection", "requestedOverdraft", "receivedOnFormatted", "guaranteeAndSBLCType", "serviceRequestSrmsId", "discrepancyAcceptance", "expectedSettlementDate", "rejectedDate", "returnedTime"]');
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('OutwardCollections', '["amount", "status", "currency", "createdOn", "incoTerms", "tenorType", "updatedOn", "documentNo", "draweeName", "usanceDays", "debitAccount", "maturityDate", "creditAccount", "draweeAddress", "messageToBank", "paymentStatus", "usanceDetails", "collectingBank", "swiftOrBicCode", "messageFromBank", "uploadDocuments", "draweeAcceptance", "physicalDocuments", "collectionReference", "deliveryInstructions", "instructionsForBills", "isBillExchangeSigned", "allowUsanceAcceptance", "collectingBankAddress", "draweeAcknowledgement", "courierTrackingDetails", "otherCollectionDetails", "lastAmendmentDetails", "reasonForCancellation", "billOfExchangeStatus", "reasonForRejection", "reasonForReturn", "returnedHistory", "settledAmount"]');
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('OutwardCollectionModify', '["amount", "status", "currency", "tenorType", "updatedOn", "documentNo", "usanceDays", "amendmentNo", "requestedOn", "maturityDate", "creditAccount", "messageToBank", "usanceDetails", "amendTenorType", "collectingBank", "uploadDocuments", "corporateUserName", "physicalDocuments", "amendmentReference", "chargesDebitAccount", "collectionReference", "allowUsanceAcceptance", "otherCollectionDetails", "reasonForReturn", "returnedHistory", "reasonForRejection"]');

INSERT INTO "permission" ("id", "Type_id", "Status_id", "Name", "Description", "isComposite", "PermissionValue") VALUES
('PID707', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateCustomerBasicInfo', 'Permission to approve updated customer info', '0', 'TRUE'),
('PID708', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateCustomerContact', 'Permission to approve updated customer contact information', '0', 'TRUE'),
('PID709', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateSignatoryGroup', 'Permission to approve updated  signatory group', '0', 'TRUE'),
('PID710', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveDeleteSignatoryGroup', 'Permission to approve deleted signatory group', '0', 'TRUE'),
('PID711', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateSignatoryGroup', 'Permission to approve a newly created signatory group', '0', 'TRUE'),
('PID712', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateCustomerRole', 'Permission to approve a newly created customer role', '0', 'TRUE'),
('PID713', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateCustomerRole', 'Permission to approve  updated customer role', '0', 'TRUE'),
('PID714', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateOutageMessage', 'Permission to approve a newly created outage message', '0', 'TRUE'),
('PID715', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateOutageMessage', 'Permission to approve updated outage message', '0', 'TRUE'),
('PID716', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveBulkUpdateOutageMessage', 'Permission to approve  bulk updated outage messages', '0', 'TRUE'),
('PID717', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateApprovalRuleSGLevel', 'Permission to approve a newly created approval rule', '0', 'TRUE'),
('PID718', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateApprovalRuleSGLevel', 'Permission to approve updated approval rule', '0', 'TRUE'),
('PID719', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateApprovalRuleUserLevel', 'Permission to approve a newly created approval rule', '0', 'TRUE'),
('PID720', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateApprovalRuleUserLevel', 'Permission to approve updated approval rule', '0', 'TRUE');
INSERT INTO "rolepermission" ("Role_id", "Permission_id") VALUES 
('RID_SUPERADMIN', 'PID707'),
('RID_SUPERADMIN', 'PID708'),
('RID_SUPERADMIN', 'PID709'),
('RID_SUPERADMIN', 'PID710'),
('RID_SUPERADMIN', 'PID711'),
('RID_SUPERADMIN', 'PID712'),
('RID_SUPERADMIN', 'PID713'),
('RID_SUPERADMIN', 'PID714'),
('RID_SUPERADMIN', 'PID715'),
('RID_SUPERADMIN', 'PID716'),
('RID_SUPERADMIN', 'PID717'),
('RID_SUPERADMIN', 'PID718'),
('RID_SUPERADMIN', 'PID719'),
('RID_SUPERADMIN', 'PID720');

INSERT INTO "makercheckerconfig" ("id", "expAPIOperationName", "module", "action", "approvalPermissionId", "approvalPermissionName", "keyColumnNames", "isApprovalRequired", "viewDetailsAPI", "auditModule", "auditEvent")
VALUES
(5, 'CustomerManagementObjService_Customer_EditCustomerBasicInfo', 'CUSTOMER_MANAGEMENT', 'EDIT_CUSTOMER_BASIC_INFO', 'PID707', 'ApproveUpdateCustomerBasicInfo', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Customer Management', 'Edit'),
(6, 'CustomerManagementObjService_CustomerContact_EditCustomerContact', 'CUSTOMER_MANAGEMENT', 'EDIT_CUSTOMER_CONTACT', 'PID708', 'ApproveUpdateCustomerContact', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Customer Management', 'Edit'),
(7, 'SignatoryGroupManageObjService_SignatoryGroup_updateSignatoryGroups', 'CONTRACT_MANAGEMENT', 'EDIT_SIGNATORY_GROUP', 'PID709', 'ApproveUpdateSignatoryGroup', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Signatory Group', 'Edit'),
(8, 'SignatoryGroupManageObjService_SignatoryGroup_deleteSignatoryGroup', 'CONTRACT_MANAGEMENT', 'DELETE_SIGNATORY_GROUP', 'PID710', 'ApproveDeleteSignatoryGroup', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Signatory Group', 'Delete'),
(9, 'SignatoryGroupManageObjService_SignatoryGroup_createSignatoryGroup', 'CONTRACT_MANAGEMENT', 'CREATE_SIGNATORY_GROUP', 'PID711', 'ApproveCreateSignatoryGroup', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Signatory Group', 'Create'),
(10, 'CustomerGroupsAndEntitlObjSvc_Group_createGroup', 'CUSTOMER_ROLES', 'CREATE_CUSTOMER_ROLE', 'PID712', 'ApproveCreateCustomerRole', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Customer Role', 'Create'),
(11, 'CustomerGroupsAndEntitlObjSvc_Group_editGroup', 'CUSTOMER_ROLES', 'EDIT_CUSTOMER_ROLE', 'PID713', 'ApproveUpdateCustomerRole', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Customer Role', 'Edit'),
(12, 'StaticContentObjService_outageMessage_createOutageMessage', 'APPLICATION_CONTENT_MANAGEMENT', 'CREATE_OUTAGE_MESSAGE', 'PID714', 'ApproveCreateOutageMessage', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Outage Message', 'Create'),
(13, 'StaticContentObjService_outageMessage_updateOutageMessage', 'APPLICATION_CONTENT_MANAGEMENT', 'EDIT_OUTAGE_MESSAGE', 'PID715', 'ApproveUpdateOutageMessage', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Outage Message', 'Edit'),
(14, 'StaticContentObjService_outageMessage_bulkUpdateOutageMessage', 'APPLICATION_CONTENT_MANAGEMENT', 'BULK_EDIT_OUTAGE_MESSAGE', 'PID716', 'ApproveBulkUpdateOutageMessage', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Outage Message', 'Edit'),
(15, 'ApprovalMatrixManageObjService_ApprovalMatrix_createApprovalRuleSGLevel', 'CONTRACT_MANAGEMENT', 'CREATE_APPROVAL_RULE_SG_LEVEL', 'PID717', 'ApproveCreateApprovalRuleSGLevel', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Approval Rule', 'Create'),
(16, 'ApprovalMatrixManageObjService_ApprovalMatrix_createApprovalRuleUserLevel', 'CONTRACT_MANAGEMENT', 'CREATE_APPROVAL_RULE_USER_LEVEL', 'PID719', 'ApproveCreateApprovalRuleUserLevel', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Approval Rule', 'Create'),
(17, 'ApprovalMatrixManageObjService_ApprovalMatrix_updateApprovalRuleSGLevel', 'CONTRACT_MANAGEMENT', 'EDIT_APPROVAL_RULE_SG_LEVEL', 'PID718', 'ApproveUpdateApprovalRuleSGLevel', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Approval Rule', 'Edit'),
(18, 'ApprovalMatrixManageObjService_ApprovalMatrix_updateApprovalRuleUserLevel', 'CONTRACT_MANAGEMENT', 'EDIT_APPROVAL_RULE_USER_LEVEL', 'PID720', 'ApproveUpdateApprovalRuleUserLevel', 'id', '1', 'MakerCheckerService:CommonRequestDetails', 'Approval Rule', 'Edit');

INSERT INTO "mcactiontext" ("id", "name", "language_code") VALUES
('CREATE_CUSTOMER_ROLE', 'Create Customer Role', 'en-GB'),
('CREATE_CUSTOMER_ROLE', 'Create Customer Role', 'en-US'),
('CREATE_CUSTOMER_ROLE', 'Create Customer Role', 'es-ES'),
('CREATE_CUSTOMER_ROLE', 'Create Customer Role', 'fr-FR'),
('CREATE_CUSTOMER_ROLE', 'Create Customer Role', 'it-IT'),
('EDIT_CUSTOMER_ROLE', 'Edit Customer Role', 'en-GB'),
('EDIT_CUSTOMER_ROLE', 'Edit Customer Role', 'en-US'),
('EDIT_CUSTOMER_ROLE', 'Edit Customer Role', 'es-ES'),
('EDIT_CUSTOMER_ROLE', 'Edit Customer Role', 'fr-FR'),
('EDIT_CUSTOMER_ROLE', 'Edit Customer Role', 'it-IT'),
('EDIT_SIGNATORY_GROUP', 'Edit Signatory Group', 'en-GB'),
('EDIT_SIGNATORY_GROUP', 'Edit Signatory Group', 'en-US'),
('EDIT_SIGNATORY_GROUP', 'Edit Signatory Group', 'es-ES'),
('EDIT_SIGNATORY_GROUP', 'Edit Signatory Group', 'fr-FR'),
('EDIT_SIGNATORY_GROUP', 'Edit Signatory Group', 'it-IT'),
('DELETE_SIGNATORY_GROUP', 'Delete Signatory Group', 'en-GB'),
('DELETE_SIGNATORY_GROUP', 'Delete Signatory Group', 'en-US'),
('DELETE_SIGNATORY_GROUP', 'Delete Signatory Group', 'es-ES'),
('DELETE_SIGNATORY_GROUP', 'Delete Signatory Group', 'fr-FR'),
('DELETE_SIGNATORY_GROUP', 'Delete Signatory Group', 'it-IT'),
('CREATE_SIGNATORY_GROUP', 'Create Signatory Group', 'en-GB'),
('CREATE_SIGNATORY_GROUP', 'Create Signatory Group', 'en-US'),
('CREATE_SIGNATORY_GROUP', 'Create Signatory Group', 'es-ES'),
('CREATE_SIGNATORY_GROUP', 'Create Signatory Group', 'fr-FR'),
('CREATE_SIGNATORY_GROUP', 'Create Signatory Group', 'it-IT'),
('EDIT_CUSTOMER_BASIC_INFO', 'Edit Customer Basic Info', 'en-GB'),
('EDIT_CUSTOMER_BASIC_INFO', 'Edit Customer Basic Info', 'en-US'),
('EDIT_CUSTOMER_BASIC_INFO', 'Edit Customer Basic Info', 'es-ES'),
('EDIT_CUSTOMER_BASIC_INFO', 'Edit Customer Basic Info', 'fr-FR'),
('EDIT_CUSTOMER_BASIC_INFO', 'Edit Customer Basic Info', 'it-IT'),
('EDIT_CUSTOMER_CONTACT', 'Edit Customer Contact', 'en-GB'),
('EDIT_CUSTOMER_CONTACT', 'Edit Customer Contact', 'en-US'),
('EDIT_CUSTOMER_CONTACT', 'Edit Customer Contact', 'es-ES'),
('EDIT_CUSTOMER_CONTACT', 'Edit Customer Contact', 'fr-FR'),
('EDIT_CUSTOMER_CONTACT', 'Edit Customer Contact', 'it-IT'),
('CREATE_OUTAGE_MESSAGE', 'Create Outage Message', 'en-GB'),
('CREATE_OUTAGE_MESSAGE', 'Create Outage Message', 'en-US'),
('CREATE_OUTAGE_MESSAGE', 'Create Outage Message', 'es-ES'),
('CREATE_OUTAGE_MESSAGE', 'Create Outage Message', 'fr-FR'),
('CREATE_OUTAGE_MESSAGE', 'Create Outage Message', 'it-IT'),
('EDIT_OUTAGE_MESSAGE', 'Edit Outage Message', 'en-GB'),
('EDIT_OUTAGE_MESSAGE', 'Edit Outage Message', 'en-US'),
('EDIT_OUTAGE_MESSAGE', 'Edit Outage Message', 'es-ES'),
('EDIT_OUTAGE_MESSAGE', 'Edit Outage Message', 'fr-FR'),
('EDIT_OUTAGE_MESSAGE', 'Edit Outage Message', 'it-IT'),
('BULK_EDIT_OUTAGE_MESSAGE', 'Bulk Edit Outage Message', 'en-GB'),
('BULK_EDIT_OUTAGE_MESSAGE', 'Bulk Edit Outage Message', 'en-US'),
('BULK_EDIT_OUTAGE_MESSAGE', 'Bulk Edit Outage Message', 'es-ES'),
('BULK_EDIT_OUTAGE_MESSAGE', 'Bulk Edit Outage Message', 'fr-FR'),
('BULK_EDIT_OUTAGE_MESSAGE', 'Bulk Edit Outage Message', 'it-IT'),
('CREATE_APPROVAL_RULE_SG_LEVEL', 'Create Approval Rule SG Level', 'en-GB'),
('CREATE_APPROVAL_RULE_SG_LEVEL', 'Create Approval Rule SG Level', 'en-US'),
('CREATE_APPROVAL_RULE_SG_LEVEL', 'Create Approval Rule SG Level', 'es-ES'),
('CREATE_APPROVAL_RULE_SG_LEVEL', 'Create Approval Rule SG Level', 'fr-FR'),
('CREATE_APPROVAL_RULE_SG_LEVEL', 'Create Approval Rule SG Level', 'it-IT'),
('EDIT_APPROVAL_RULE_SG_LEVEL', 'Edit Approval Rule SG Level', 'en-GB'),
('EDIT_APPROVAL_RULE_SG_LEVEL', 'Edit Approval Rule SG Level', 'en-US'),
('EDIT_APPROVAL_RULE_SG_LEVEL', 'Edit Approval Rule SG Level', 'es-ES'),
('EDIT_APPROVAL_RULE_SG_LEVEL', 'Edit Approval Rule SG Level', 'fr-FR'),
('EDIT_APPROVAL_RULE_SG_LEVEL', 'Edit Approval Rule SG Level', 'it-IT'),
('CREATE_APPROVAL_RULE_USER_LEVEL', 'Create Approval Rule User Level', 'en-GB'),
('CREATE_APPROVAL_RULE_USER_LEVEL', 'Create Approval Rule User Level', 'en-US'),
('CREATE_APPROVAL_RULE_USER_LEVEL', 'Create Approval Rule User Level', 'es-ES'),
('CREATE_APPROVAL_RULE_USER_LEVEL', 'Create Approval Rule User Level', 'fr-FR'),
('CREATE_APPROVAL_RULE_USER_LEVEL', 'Create Approval Rule User Level', 'it-IT'),
('EDIT_APPROVAL_RULE_USER_LEVEL', 'Edit Approval Rule User Level', 'en-GB'),
('EDIT_APPROVAL_RULE_USER_LEVEL', 'Edit Approval Rule User Level', 'en-US'),
('EDIT_APPROVAL_RULE_USER_LEVEL', 'Edit Approval Rule User Level', 'es-ES'),
('EDIT_APPROVAL_RULE_USER_LEVEL', 'Edit Approval Rule User Level', 'fr-FR'),
('EDIT_APPROVAL_RULE_USER_LEVEL', 'Edit Approval Rule User Level', 'it-IT');



INSERT INTO "mcmoduletext" ("id", "name", "language_code") VALUES
('APPLICATION_CONTENT_MANAGEMENT', 'Application Content Management', 'en-GB'),
('APPLICATION_CONTENT_MANAGEMENT', 'Application Content Management', 'en-US'),
('APPLICATION_CONTENT_MANAGEMENT', 'Application Content Management', 'es-ES'),
('APPLICATION_CONTENT_MANAGEMENT', 'Application Content Management', 'fr-FR'),
('APPLICATION_CONTENT_MANAGEMENT', 'Application Content Management', 'it-IT'),
('CUSTOMER_ROLES', 'Customer Roles', 'en-GB'),
('CUSTOMER_ROLES', 'Customer Roles', 'en-US'),
('CUSTOMER_ROLES', 'Customer Roles', 'es-ES'),
('CUSTOMER_ROLES', 'Customer Roles', 'fr-FR'),
('CUSTOMER_ROLES', 'Customer Roles', 'it-IT');

UPDATE "makercheckerconfig" set "action" = 'ENROLL_CUSTOMER', "approvalPermissionName" = 'ApproveEnrollCustomer' where "id" = '3';
UPDATE "permission" set "Name" = 'ApproveEnrollCustomer', "Description" = 'Permission to approve a newly enrolled customer' where "id" = 'PID703';

DELETE FROM "mcactiontext" WHERE ("id" = 'CREATE_CUSTOMER');

INSERT INTO "mcactiontext"
("id", "name", "language_code") VALUES 
('ENROLL_CUSTOMER', 'Enroll Customer', 'en-GB'),
('ENROLL_CUSTOMER', 'Enroll Customer', 'en-US'),
('ENROLL_CUSTOMER', 'Enroll Customer', 'es-ES'),
('ENROLL_CUSTOMER', 'Enroll Customer', 'fr-FR'),
('ENROLL_CUSTOMER', 'Enroll Customer', 'it-IT');

INSERT INTO "ld_records_module_configurations" ("module_id", "allowed_fields") VALUES 
('RolloverRequest', '["facilityId", "loanId", "rolloverDays", "rolloverMonths", "rolloverYears", "rolloverRequestId"]'),
('DrawdownRequest', '["facilityId","loanProduct","currency","loanAmount","drawdownTermDays","drawdownTermMonths","drawdownTermYears","customerDetails","payInDetails","payOutDetails","drawdownRequestId"]'),
('PaymentRequest', '["repaymentFor", "fromAccountNumber", "toAccountNumber", "paymentType", "paymentAmount", "paymentCurrency","paymentReferenceMsg","paymentRequestDate","paymentRequestId"]');

UPDATE "configurations" SET "config_value" = '{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"mortgageFacility","BB.SMALL.BUSINESS.LOAN":"Loan","BB.PREMIUM.ACCOUNT":"Checking","BB.STANDARD.ACCOUNT":"Checking","BB.START.UP.ACCOUNT":"Checking","RES.SAVINGS.ACCOUNT":"Savings","RES.NOTICE.ACCOUNT":"Savings","RES.FCY.SAVINGS.ACCOUNT":"Savings","RES.SAVINGS.PARENT":"Savings","RES.CURRENT.ACCOUNT":"Checking","RES.PREMIUM.ACCOUNT":"Checking","RES.MULTI.CURRENCY.ACCOUNT":"Checking","RES.CURRENT.PARENT":"Checking","RES.MCY.SUB.ACCOUNT":"Checking","RES.SHORT.TERM.AUTO.ROLL":"Deposit","RES.SHORT.TERM.MANUAL.ROLL":"Deposit","RES.LONG.TERM.FLEXI.PAY":"Deposit","RES.LONG.TERM.HIGH.EARN":"Deposit","RES.SAVINGS.PLAN":"Deposit","RES.CONSUMER.LOAN":"Loan","RES.CREDIT.LINE.PARENT":"Loan","RES.MORTGAGE.FACILITIES":"mortgageFacility","RES.10Y.FIXED.LINEAR":"Loan","RES.2Y.FIXED.CONST":"Loan","RES.5Y.TRACKER.CONST":"Loan","RES.BNPL.FACILITY":"Loan","RES.PAYIN.3":"Loan","RES.PAYIN.3M":"Loan","RES.PAST.PURCHASE":"Loan","RES.CONSUMER.LOAN.PARENT":"Loan","RES.CREDIT.LINE":"Loan","CONS.CHECKING":"Checking","CONS.MM":"Checking","FOREIGN.CURR.CHECKING":"Checking","CONDITIONAL.CHECKING":"Checking","BUSINESS.CHECKING":"Checking","US.BASIC.BANKING.ACCT.MA":"Checking","US.BASIC.BANKING.ACCT.NY":"Checking","CONS.SAVINGS":"Savings","BUSINESS.SAVINGS":"Savings","US.BASIC.SAVINGS.ACCT.MA":"Savings","US.CNS.LOAN":"Loan","US.LOC":"Loan","UK.ISA.VR.A.EASY":"Savings","UK.ISA.VR.A.H2B":"Savings","UK.ISA.VR.APS.A.APSACC":"Savings","UK.ISA.VR.APS.F.FLEXAPSACC":"Savings","UK.ISA.VR.F.FLEXISA":"Savings","UK.ISA.VR.F.H2B":"Savings","UK.ISA.VR.C.JUNIOR.EASY":"Savings","UK.ISA.FR.A.3Y":"Deposit","UK.ISA.FR.F.3Y.FLEX":"Deposit","AU.CASH.MANAGEMENT.ACCOUNT":"Savings","AU.ONLINE.SAVER.ACCOUNT":"Savings","AU.KIDS.SAVER.ACCOUNT":"Checking","AU.STUDENT.SAVER.ACCOUNT":"Checking","AU.TRANSACTION.ACCOUNT":"Checking","AU.RET.TD.3M":"Deposit","AU.RET.TD.6M":"Deposit","AU.RET.TD.9M":"Deposit","AU.RET.TD.12M":"Deposit","AU.RET.TD.24M":"Deposit","AU.RET.TD.60M":"Deposit","AU.PERSONALLOAN":"Loan","AU.HOME.EQUITY.LOAN":"Loan","AU.MORTGAGE.LOAN.FIXED":"Loan","AU.MORTGAGE.LOAN.VARIABLE":"Loan","AU.RATE.LOCK.LOAN":"Loan"}' WHERE ("configuration_id" = '172');

INSERT INTO "role" ("id", "Type_id", "Status_id", "Parent_id", "Name", "Description", "createdby", "modifiedby", "softdeleteflag", "companyLegalUnit") VALUES
('RID_CUSTOMER_APPROVER','ROLE_TYPE_1','SID_ACTIVE','RID_SUPERADMIN','Customer Approver','This role is used for Customer Approver','Kony User','Kony Dev','0', 'ALL'),
('RID_CONTRACT_APPROVER','ROLE_TYPE_1','SID_ACTIVE','RID_SUPERADMIN','Contract Approver','This role is used for Contract Approver','Kony User','Kony Dev','0', 'ALL'),
('RID_CUSTOMER_ROLE_APPROVER','ROLE_TYPE_1','SID_ACTIVE','RID_SUPERADMIN','Customer Role Approver','This role is used for Customer Role Approver','Kony User','Kony Dev','0','ALL'),
('RID_SERVICE_OUTAGE_MESSAGE_APPROVER','ROLE_TYPE_1','SID_ACTIVE','RID_SUPERADMIN','Service Outage Message Approver','This role is used for Service Outage Message Approver','Kony User','Kony Dev','0','ALL'),
('RID_VIEW_ALL','ROLE_TYPE_1','SID_ACTIVE','RID_SUPERADMIN','View All','This role is used for View All','Kony User','Kony Dev','0','ALL');

INSERT INTO "rolepermission" ("Role_id", "Permission_id", "createdby", "modifiedby") VALUES 
('RID_CUSTOMER_APPROVER','PID703','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID704','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID707','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID708','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID41','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID03','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID10','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID11','Kony User','Kony Dev'),
('RID_CUSTOMER_APPROVER','PID09','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID717','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID701','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID711','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID710','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID718','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID702','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID709','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID113','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID104','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID110','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID105','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID114','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID111','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID115','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID103','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID112','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID719','Kony User','Kony Dev'),
('RID_CONTRACT_APPROVER','PID720','Kony User','Kony Dev'),
('RID_CUSTOMER_ROLE_APPROVER','PID712','Kony User','Kony Dev'),
('RID_CUSTOMER_ROLE_APPROVER','PID713','Kony User','Kony Dev'),
('RID_CUSTOMER_ROLE_APPROVER','PID04','Kony User','Kony Dev'),
('RID_CUSTOMER_ROLE_APPROVER','PID43','Kony User','Kony Dev'),
('RID_CUSTOMER_ROLE_APPROVER','PID05','Kony User','Kony Dev'),
('RID_SERVICE_OUTAGE_MESSAGE_APPROVER','PID714','Kony User','Kony Dev'),
('RID_SERVICE_OUTAGE_MESSAGE_APPROVER','PID715','Kony User','Kony Dev'),
('RID_SERVICE_OUTAGE_MESSAGE_APPROVER','PID716','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID01','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID02','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID03','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID05','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID09','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID10','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID100','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID103','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID104','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID105','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID106','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID11','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID111','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID112','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID113','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID114','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID115','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID14','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID18','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID27','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID31','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID32','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID33','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID34','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID35','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID36','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID37','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID38','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID41','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID44','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID45','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID46','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID47','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID48','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID49','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID50','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID51','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID52','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID53','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID54','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID55','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID56','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID57','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID58','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID59','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID60','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID69','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID70','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID77','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID79','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID80','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID81','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID82','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID83','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID88','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID89','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID90','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID91','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID92','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID93','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID94','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID95','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID96','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID97','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID98','Kony User','Kony Dev'),
('RID_VIEW_ALL','PID99','Kony User','Kony Dev');

INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('LetterOfCreditsImport', '["lcReferenceNo","lcAmount","lcCurrency","tolerancePercentage","maximumCreditAmount","additionalAmountPayable","additionalPayableCurrency","paymentTerms","availableWith1","availableWith2","availableWith3","availableWith4","issueDate","expiryDate","expiryPlace","chargesAccount","commisionAccount","marginAccount","messageToBank","beneficiaryName","beneficiaryAddressLine1","beneficiaryAddressLine2","beneficiaryPostCode","beneficiaryCountry","beneficiaryCity","beneficiaryState","beneficiaryBank","beneficiaryBankAdressLine1","beneficiaryBankAdressLine2","beneficiaryBankPostCode","beneficiaryBankCountry","beneficiaryBankCity","beneficiaryBankState","placeOfTakingIncharge","portOfLoading","portOfDischarge","placeOfFinalDelivery","latestShippingDate","presentationPeriod","transshipment","partialShipments","incoTerms","modeOfShipment","descriptionOfGoods","documentsRequired","additionalConditionsCode","otherAdditionalConditions","documentCharges","supportDocuments","status","fileToUpload","confirmationInstruction","transferable","standByLC","isDraft","screenNumber","utilizedAmount","drawingDetails","lastAmendmentDetails"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ReviewImportDrawing', '["lcReferenceNo","lcType","drawingReferenceNo","beneficiaryName","documentStatus","drawingCreationDate","drawingCurrency","drawingAmount","lcAmount","lcCurrency","lcIssueDate","lcExpiryDate","lcPaymentTerms","drawingDetailsCurrency","presentorReference","presentorName","documentsReceived","forwardContact","shippingGuaranteeReference","approvalDate","totalDocuments","documentName","discrepancyDescription","documentStatus","discrepancies","acceptance","totalAmountToBePaid","accountToBeDebited","messageFromBank","messageToBank","totalPaidAmount","paymentDate","reasonForRejection","rejectedDate","paymentStatus","lcSrmsReqOrderID","status"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('GenerateImportDrawingAdvices', '["beneficiaryName","message","messageType","messageDate","messageCategory","drawingsSrmsReqOrderID","status"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ExportLCDrawing', '["lcType","advisingBankReference","applicant","approvedDate","chargesDebitAccount","creditAccount","currency","discrepanciesHistory1","discrepanciesHistory2","discrepanciesHistory3","discrepanciesHistory4","discrepanciesHistory5","discrepencies","discrepenciesAcceptance","documentReference","drawingAmount","drawingCreatedDate","expiryDate","exportLCId","externalAccount","financeBill","forwardDocuments","issuingBank","lcAmount","lcCurrency","lcIssueDate","lcReferenceNo","messageToBank","paymentDate","paymentStatus","physicalDocuments","reasonForReturn","returnMessageToBank","returnedDate","returnedDocuments","status","totalAmount","totalDocuments","uploadedDocuments"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ImportLCAmendment', '["paymentTerms","amendCharges","amendStatus","amendmentApprovedDate","amendmentExpiryDate","amountType","beneficiaryName","chargesAccount","chargesPaid","creditAmount","expiryDate","importLCId","issueDate","latestShippingDate","lcAmount","lcCurrency","lcReferenceNo","lcSRMSId","otherAmendments","presentationPeriod"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('GenerateExportDrawingAdvices', '["adviceName","advisingBank","beneficiary","charges","creditedAccount","creditedAmount","currency","drawingAmount","drawingReferenceNo","message","paymentDate","orderId","issuingBank","debitedAccount","debitedAmount"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('LetterOfCreditsExport', '["additionalConditions","advisingBankReference","amount","applicant","applicantaddress","beneficiaryAddress","beneficiaryName","confirmInstructions","currency","documentName","expiryDate","exportLCId","forwardContract","goodsDescription","issueDate","issuingBank","issuingBankReference","issuingbankaddress","latestShipmentDate","lcReferenceNo","lcType","paymentTerms","status","uploadedFiles","utilizedLCAmount","amendmentNo","beneficiaryConsent","drawingDetails","lastAmendmentDetails","lcUpdatedOn","messageToBank","reasonForRejection"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('GenerateExportLCAdvices', '["beneficiaryName","message","messageType","messageDate","messageCategory","drawingsSrmsReqOrderID","status"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ExportLCAmendments', '["applicantName","exportlcReferenceNo","exportlcSRMSRequestId","amendmentStatus","amendmentNo","lcType","lcIssueDate","lcExpiryDate","lcCurrency","lcAmountStatus","oldLcAmount","newLcAmount","latestShipmentDate","periodOfPresentation","otherAmendments","amendmentChargesPayer","chargesDebitAccount","amendmentReceivedDate","selfAcceptance","selfAcceptanceDate","selfRejectedDate","reasonForSelfRejection","amendmentReferenceNo","amendmentSRMSRequestId","serviceRequestTime"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('GenerateGuaranteesAmendmentAdvices', '["sender","message","messageType","messageDate","receiver","guaranteesAmendId","adviceName"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ReceivedGuaranteeAmendments', '["amendAmount","amendExpiryConditions","amendExpiryDate","amendExpiryType","amendmentCharges","amendmentNo","amendmentSrmsId","amount","applicant","beneficiaryDetails","currency","dateOfAmountChange","guaranteeSrmsId","lcType","messageFromBank","messageToBank","otherAmendments","otherInstructions","productType","reasonForSelfRejection","receivedOn","selfAcceptance","status","supportingDocuments"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ReceivedGuaranteeClaims', '["amount","applicant","beneficiaryName","chargesDebitAccount","claimAmount","claimCreditAccount","claimCurrency","createdOn","demandType","discrepancies","documentInformation","documentStatus","expiryDate","expiryType","forwardDocuments","guaranteeAndSBLCType","guaranteesReferenceNo","guaranteesSRMSId","issueDate","issuingBank","messageFromBank","messageToBank","newExtensionDate","otherDemandDetails","paymentSettledDate","paymentStatus","physicalDocuments","productType","reasonForReturn","returnMessageToBank","returnedCount","returnedDocuments","returnedHistory","returnedTime","serviceRequestTime","status","totalDocuments","utilizedAmount"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ReceivedGuarantees', '["amount","applicableRules","applicantAddress","applicantName","applicantParty","autoExtensionExpiry","beneficiaryDetails","claimInformation","clausesAndConditions","currency","deliveryInstructions","expectedIssueDate","expiryConditions","expiryDate","expiryType","extensionCapPeriod","extensionDetails","extensionPeriod","governingLaw","guaranteeSrmsId","instructingParty","issuingBankAddress","issuingBankIban","issuingBankLocalCode","issuingBankName","issuingBankSwiftBicCode","lcType","messageFromBank","messageToBank","modeOfTransaction","notificationPeriod","otherInstructions","productType","reasonForSelfRejection","receivedOn","relatedTransactionReference","selfAcceptance","selfAcceptanceDate","selfRejectionHistory","status","transactionReference","uploadedDocuments","utilizedAmount","releasedAmount","liabilityDetails","demandAcceptance"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('InwardCollectionAmendments', '["amendAmount","amendDocuments","amendMaturityDate","amendRemittingBank","amendTenorType","amendUsanceDetails","amendmentNo","amendmentSrmsId","amount","cancellationStatus","collectionSrmsId","createdDate","currency","draweeAcknowledgement","draweeAcknowledgementDate","drawer","maturityDate","messageFromBank","messageToBank","reasonForCancellation","reasonForRejection","receivedOn","remittingBank","status","tenorType","transactionReference"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('InwardCollections', '["amendmentDetails","amount","billExchangeStatus","charges","chargesDebitFrom","collectionSrmsId","createdDate","currency","debitAmountFrom","documentNo","documentsUploaded","draweeAcknowledgement","draweeAcknowledgementDate","drawerName","incoTerms","lastUpdatedDate","maturityDate","messageFromBank","messageToBank","paymentStatus","reasonForRejection","reasonForReturn","receivedOn","remittingBank","settledDate","status","tenorType","transactionReference","usanceAcceptance","usanceAcceptanceDate","usanceAcceptanceEligibility","usanceDetails"]'); 
INSERT INTO "tf_records_module_configurations" ("module_id", "allowed_fields")VALUES ('ReceivedGuaranteeSwiftMessages', '["additionalAmountInformation","applicableRules","applicant","automaticExtensionFinalExpiryDate","automaticExtensionNonExtensionPeriod","automaticExtensionNotificationPeriod","automaticExtensionPeriod","availableWith","bCode","beneficiary","bic","charges","createdDate","dateOfExpiry","deliveryOfLocalUndertaking","deliveryToOrCollectionBy","demandIndicator","documentAndPresentationInstructions","expiryConditionOrEvent","expiryType","formOfUndertaking","governingLawAndOrPlaceOfJurisdiction","issuer","newSequence","obligorOrInstructingParty","orderId","requestedDateOfIssue","requestedLocalUndertakingTermsAndConditions","standardWordingRequestedLanguage","standardWordingRequired","swiftMessagesReference","transferConditions","transferDateOrTime","transferIndicator","type","typeOfUndertaking","underlyingTransactionDetails","undertakingAmount"]'); 
UPDATE "configurations" SET "config_value" = 'false' WHERE ("configuration_id" = '3e411011-cb7a-4a4e-a785-ea196c7634cf');

DELETE FROM "rolepermission" WHERE ("Permission_id" = 'PID45');
TRUNCATE TABLE "rolecompositepermission";
DELETE FROM "compositepermission" WHERE ("Permission_id" = 'PID45');
TRUNCATE TABLE "rolecompositeaction";
DELETE FROM "compositeaction" WHERE ("Permission_id" = 'PID45');
DELETE FROM "permission" WHERE ("id" = 'PID45');

INSERT INTO "permission"
("id", "Type_id", "Status_id", "DataType_id", "Name", "Description", "isComposite", "PermissionValue")
VALUES('PID721', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', NULL, 'ApproveSuspendOrActivateCustomer', 'Permission to approve suspend or activate customer', 0, 'TRUE');

INSERT INTO "rolepermission"
("Role_id", "Permission_id")
VALUES('RID_SUPERADMIN', 'PID721'), ('RID_CUSTOMER_APPROVER', 'PID721');

INSERT INTO "makercheckerconfig"
("id", "expAPIOperationName", "module", "action", "approvalPermissionId", "approvalPermissionName", "keyColumnNames", "isApprovalRequired", "viewDetailsAPI", "auditModule", "auditEvent", "excludedParams")
VALUES(19, 'CustomerManagementObjService_Customer_updateDBPUserStatus', 'CUSTOMER_MANAGEMENT', 'SUSPEND_ACTIVATE_CUSTOMER', 'PID721', 'ApproveSuspendOrActivateCustomer', 'id', 1, 'MakerCheckerService:CommonRequestDetails', 'Customer Management', 'Edit', NULL);

INSERT INTO "mcactiontext" ("id", "name", "language_code")
VALUES('SUSPEND_ACTIVATE_CUSTOMER', 'Suspend or Activate Customer', 'en-GB'),
 ('SUSPEND_ACTIVATE_CUSTOMER', 'Suspend or Activate Customer', 'en-US'),
 ('SUSPEND_ACTIVATE_CUSTOMER', 'Suspend or Activate Customer', 'es-ES'),
 ('SUSPEND_ACTIVATE_CUSTOMER', 'Suspend or Activate Customer', 'fr-FR'),
 ('SUSPEND_ACTIVATE_CUSTOMER', 'Suspend or Activate Customer', 'it-IT');
 
 
INSERT INTO "role" ("id",  "Type_id",  "Status_id",  "Parent_id",  "Name",  "Description") VALUES('RID_MAKER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Maker', 'This role is used for Maker');

INSERT INTO "rolepermission" ("Role_id", "Permission_id") VALUES('RID_MAKER', 'PID01'),('RID_MAKER', 'PID02'),('RID_MAKER', 'PID03'),('RID_MAKER', 'PID04'),('RID_MAKER', 'PID05'),('RID_MAKER', 'PID06'),('RID_MAKER', 'PID07'),('RID_MAKER', 'PID08'),('RID_MAKER', 'PID09'),('RID_MAKER', 'PID10'),('RID_MAKER', 'PID100'),('RID_MAKER', 'PID101'),('RID_MAKER', 'PID102'),('RID_MAKER', 'PID103'),('RID_MAKER', 'PID104'),('RID_MAKER', 'PID105'),('RID_MAKER', 'PID106'),('RID_MAKER', 'PID107'),('RID_MAKER', 'PID108'),('RID_MAKER', 'PID109'),('RID_MAKER', 'PID11'),('RID_MAKER', 'PID110'),('RID_MAKER', 'PID111'),('RID_MAKER', 'PID112'),('RID_MAKER', 'PID113'),('RID_MAKER', 'PID114'),('RID_MAKER', 'PID115'),('RID_MAKER', 'PID12'),('RID_MAKER', 'PID13'),('RID_MAKER', 'PID14'),('RID_MAKER', 'PID15'),('RID_MAKER', 'PID16'),('RID_MAKER', 'PID17'),('RID_MAKER', 'PID18'),('RID_MAKER', 'PID19'),('RID_MAKER', 'PID20'),('RID_MAKER', 'PID21'),('RID_MAKER', 'PID22'),('RID_MAKER', 'PID23'),('RID_MAKER', 'PID24'),('RID_MAKER', 'PID25'),('RID_MAKER', 'PID26'),('RID_MAKER', 'PID27'),('RID_MAKER', 'PID28'),('RID_MAKER', 'PID29'),('RID_MAKER', 'PID30'),('RID_MAKER', 'PID31'),('RID_MAKER', 'PID32'),('RID_MAKER', 'PID33'),('RID_MAKER', 'PID34'),('RID_MAKER', 'PID35'),('RID_MAKER', 'PID36'),('RID_MAKER', 'PID37'),('RID_MAKER', 'PID38'),('RID_MAKER', 'PID39'),('RID_MAKER', 'PID40'),('RID_MAKER', 'PID41'),('RID_MAKER', 'PID42'),('RID_MAKER', 'PID43'),('RID_MAKER', 'PID44'),('RID_MAKER', 'PID46'),('RID_MAKER', 'PID47'),('RID_MAKER', 'PID48'),('RID_MAKER', 'PID49'),('RID_MAKER', 'PID50'),('RID_MAKER', 'PID51'),('RID_MAKER', 'PID52'),('RID_MAKER', 'PID53'),('RID_MAKER', 'PID54'),('RID_MAKER', 'PID55'),('RID_MAKER', 'PID56'),('RID_MAKER', 'PID57'),('RID_MAKER', 'PID58'),('RID_MAKER', 'PID59'),('RID_MAKER', 'PID60'),('RID_MAKER', 'PID61'),('RID_MAKER', 'PID62'),('RID_MAKER', 'PID63'),('RID_MAKER', 'PID64'),('RID_MAKER', 'PID65'),('RID_MAKER', 'PID66'),('RID_MAKER', 'PID67'),('RID_MAKER', 'PID68'),('RID_MAKER', 'PID69'),('RID_MAKER', 'PID70'),('RID_MAKER', 'PID700'),('RID_MAKER', 'PID73'),('RID_MAKER', 'PID74'),('RID_MAKER', 'PID75'),('RID_MAKER', 'PID76'),('RID_MAKER', 'PID77'),('RID_MAKER', 'PID78'),('RID_MAKER', 'PID79'),('RID_MAKER', 'PID80'),('RID_MAKER', 'PID800'),('RID_MAKER', 'PID801'),('RID_MAKER', 'PID806'),('RID_MAKER', 'PID81'),('RID_MAKER', 'PID82'),('RID_MAKER', 'PID83'),('RID_MAKER', 'PID84'),('RID_MAKER', 'PID85'),('RID_MAKER', 'PID86'),('RID_MAKER', 'PID87'),('RID_MAKER', 'PID88'),('RID_MAKER', 'PID89'),('RID_MAKER', 'PID90'),('RID_MAKER', 'PID91'),('RID_MAKER', 'PID92'),('RID_MAKER', 'PID93'),('RID_MAKER', 'PID94'),('RID_MAKER', 'PID95'),('RID_MAKER', 'PID96'),('RID_MAKER', 'PID97'),('RID_MAKER', 'PID98'),('RID_MAKER', 'PID99');


INSERT INTO "userroleservicedefinition"
("UserRole_id", "servicedefinitionId") VALUES 
('RID_MAKER', '5801fa32-a416-45b6-af01-b22e2de93777'),
('RID_MAKER', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a'),
('RID_MAKER', '83c9b8d7-3715-480e-8c7d-3d6e61c00035'),
('RID_MAKER', '90356097-7fdf-4b8c-89bd-8a1065338a97'),
('RID_MAKER', 'bef2fe82-9c21-4ccb-b599-3308de18de44'),
('RID_MAKER', 'f85d8392-9afe-4128-b23e-a370f138784f');

UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CreateContractViewDetails' WHERE ("id" = '1');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:EditContractViewDetails' WHERE ("id" = '2');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:EnrollCustomerViewDetails' WHERE ("id" = '3');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:EditCustomerViewDetails' WHERE ("id" = '4');
UPDATE "makercheckerconfig" SET "viewDetailsAPI" = 'MakerCheckerService:CreateApprovalRuleBySignatoryGroupViewDetails' WHERE ("id" = '15');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES
('137aa776-a206-4914-a8ae-2b31b0544f88', 'MakerCheckerObjService', 'Request', 'getHistory', 'ALLOW'),
('08882713-f360-425d-a12f-4c73331717f5', 'MakerCheckerObjService', 'makerchecker', 'ApproveRejectRequest', 'ApproveCreateContract,ApproveUpdateContract,ApproveEnrollCustomer,ApproveUpdateCustomer,ApproveUpdateCustomerBasicInfo,ApproveUpdateCustomerContact,ApproveUpdateSignatoryGroup,ApproveDeleteSignatoryGroup,ApproveCreateSignatoryGroup,ApproveCreateCustomerRole,ApproveUpdateCustomerRole,ApproveCreateOutageMessage,ApproveUpdateOutageMessage,ApproveBulkUpdateOutageMessage,ApproveCreateApprovalRuleSGLevel,ApproveUpdateApprovalRuleSGLevel,ApproveCreateApprovalRuleUserLevel,ApproveUpdateApprovalRuleUserLevel,ApproveSuspendOrActivateCustomer'),
('0b4b6f84-9a8a-4c46-8af9-72e993bb79b4', 'MakerCheckerObjService', 'MakerCheckerConfigurations', 'editMakerCheckerConfigurations', 'UpdateMakerCheckerConfigurations'),
('3a1dfc63-cc33-4dd0-92c8-75e9706645cb', 'MakerCheckerObjService', 'GetMCModuleActionMasterData', 'FetchMakerCheckerModules', 'ALLOW'),
('95af4d9e-c2bd-4a20-b144-5d4ac2e3553a', 'MakerCheckerObjService', 'FetchMakerPendingRequests', 'FetchMakerRequests', 'ALLOW'),
('298a869b-ba56-49e5-b3f6-c04eeece3ce8', 'MakerCheckerObjService', 'MakerCheckerWidgetCounts', 'get', 'ALLOW'),
('9bbc97b3-9737-4630-b065-482cb1e236fa', 'MakerCheckerObjService', 'GetCheckerApprovalRequests', 'getApprovalRequests', 'ApproveCreateContract,ApproveUpdateContract,ApproveEnrollCustomer,ApproveUpdateCustomer,ApproveUpdateCustomerBasicInfo,ApproveUpdateCustomerContact,ApproveUpdateSignatoryGroup,ApproveDeleteSignatoryGroup,ApproveCreateSignatoryGroup,ApproveCreateCustomerRole,ApproveUpdateCustomerRole,ApproveCreateOutageMessage,ApproveUpdateOutageMessage,ApproveBulkUpdateOutageMessage,ApproveCreateApprovalRuleSGLevel,ApproveUpdateApprovalRuleSGLevel,ApproveCreateApprovalRuleUserLevel,ApproveUpdateApprovalRuleUserLevel,ApproveSuspendOrActivateCustomer'),
('953aa62d-f2a9-475b-a027-b61eb364257b', 'MakerCheckerObjService', 'MakerCheckerConfigurations', 'getMakerCheckerConfigurations', 'ViewMakerCheckerConfigurations'),
('01c2e002-605b-40e3-9196-487fa1fca2d3', 'MakerCheckerObjService', 'ApprovalRequestViewDetails', 'viewDetails', 'ALLOW'),
('f4be4ad4-0873-46b8-affa-6f1299bd0b16', 'MakerCheckerObjService', 'makerchecker', 'WithdrawRequest', 'ALLOW');
package com.temenos.infinity.tradefinanceservices.utils;

import com.temenos.infinity.tradefinanceservices.businessdelegate.impl.*;

/**
 * @author naveen.yerra
 */
public enum ExcelEnum {

    GENERATEIMPORTLETTEROFCREDITSLIST(
            new String[]{"Beneficiary", "LC Type", "Created on", "Issue Date", "Expiry Date", "Status", "Reference", "Amount", "Payment Terms", "Inco Terms", "Charges", "Expiry Place", "Transfarable", "Standby LC"},
            new String[]{"beneficiaryName", "paymentTerms", "createdOnFormatted", "issuedOnFormatted", "expiredOnFormatted", "status", "lcReferenceNo", "amountFormatted", "paymentTerms", "incoTerms", "documentCharges", "expiryPlace", "transferable", "standByLC"},
            GetLetterOfCreditsBusinessDelegateImpl.class, false),
    GENERATEIMPORTDRAWINGSLIST(
            new String[]{"Beneficiary", "Drawing Reference", "Amount", "Date", "Status", "LC Reference", "LC Type", "Doc Status", "Currency", "Message from Bank"},
            new String[]{"beneficiaryName", "drawingReferenceNo", "amountFormatted", "createdOnFormatted", "status", "lcReferenceNo", "lcType", "documentStatus", "drawingCurrency", "messageFromBank"},
            LetterOfCreditDrawingsBusinessDelegateImpl.class, false),
    GENERATEIMPORTAMENDMENTSLIST(
            new String[]{"Beneficiary", "Amendment Reference", "Amended On", "LC Ref No", "Status", "LC Type", "Approved/Rejected on", "Charges Paid By", "Charges Paid"},
            new String[]{"beneficiaryName", "amendmentReference", "amendDateFormatted", "lcReferenceNo", "amendStatus", "paymentTerms", "amendApprovedDateFormatted", "amendCharges", "chargesPaid"},
            GetAmendmentsLetterOfCreditsBusinessDelegateImpl.class, false),
    GENERATEEXPORTLETTEROFCREDITLIST(
            new String[]{"Applicant", "LC Reference", "LC Type", "Expiry Date", "Amount", "Beneficiary", "Issue Date", "Latest Shipment Date", "Goods Description"},
            new String[]{"applicant", "lcReferenceNo", "lcType", "expiredOnFormatted", "amountFormatted", "beneficiaryName", "issueDateFormatted", "latestShipmentDateFormatted", "goodsDescription"},
            GetExportLetterOfCreditsBusinessDelegateImpl.class, false),
    GENERATEEXPORTDRAWINGSLIST(
            new String[]{"Applicant", "Drawing Reference", "Amount", "Date", "Status", "Advising LC Reference Number", "LC Type", "Doc Status", "Currency", "Message To Bank"},
            new String[]{"applicant", "drawingReferenceNo", "amountFormatted", "createdOnFormatted", "status", "lcReferenceNo", "lcType", "documentStatus", "currency", "messageToBank"},
            ExportLetterOfCreditsDrawingsBusinessDelegateImpl.class, false),
    GENERATEEXPORTAMENDMENTSLIST(
            new String[]{"Applicant", "LC Reference", "Status", "Received On", "Amendment No.", "LC Type", "Issue Date", "Expiry Date", "Amendment Reference"},
            new String[]{"applicantName", "exportlcReferenceNo", "amendmentStatus", "amendReceivedDateFormatted", "amendmentNo", "lcType", "issuedDateFormatted", "expiredDateFormatted", "amendmentSRMSRequestId"},
            ExportLCAmendmentBusinessDelegateImpl.class, false),
    GENERATEGUARANTEESLIST(
            new String[]{"Beneficiary", "Product Type", "GT & SBLC Type", "Transaction Ref", "Created On ", "Status", "Amount with Currency", "Issue Date", "Expiry Type", "Total Beneficiaries", "Mode of Transaction", "Advising Bank", "Instructing Party"},
            new String[]{"beneficiaryName", "productType", "guaranteeAndSBLCType", "guaranteesSRMSId", "createdOnFormatted", "status", "amountWithCurrency", "issueDate", "expiryType", "beneficiaryCount", "modeOfTransaction", "bankName", "instructingParty"},
            GuaranteesBusinessDelegateImpl.class, false),
    GENERATEGUARANTEEAMENDMENTLIST(
            new String[]{"Beneficiary", "Amendment No.", "Transaction Ref", "Created On", "Status", "Product Type", "GT & SBLC Type", "Amount with Currency", "Amd. Effective Date", "Expiry Type", "Instructing Party"},
            new String[]{"benificiaryName", "amendmentNo", "amendmentReference", "createdOnFormatted", "amendStatus", "productType", "billType", "amountWithCurrency", "amendEffectiveDateFormatted", "expiryType", "instructingParty"},
            GuaranteeLCAmendmentsBusinessDelegateImpl.class, false),
    GENERATERECEIVEDGUARANTEESLIST(
            new String[]{"Applicant", "Product Type", "GT & SBLC Type", "Transaction Reference", "Received On", "Status", "Currency", "Amount", "Issue Date", "Expiry Type", "modeOfTransaction", "Issuing Bank"},
            new String[]{"applicantName", "productType", "lcType", "transactionReference", "receivedOnFormatted", "status", "currency", "amountFormatted", "expectedIssuedOnFormatted", "expiryType", "modeOfTransaction", "issuingBankName"},
            ReceivedGuaranteesBusinessDelegateImpl.class, false),
    GENERATERECEIVEDAMENDMENTSLIST(
            new String[]{"Applicant", "Transaction Reference", "Amendment Reference", "Received On", "Amendment Number", "Status", "Product Type", "GT & SBLC Type", "Currency", "Amount", "Expiry Type"},
            new String[]{"applicant", "guaranteeSrmsId", "amendmentSrmsId", "receivedOnFormatted", "amendmentNo", "status", "productType", "lcType", "currency", "amountFormatted", "amendExpiryType"},
            ReceivedGuaranteeAmendmentsBusinessDelegateImpl.class, false),
    GENERATERECEIVEDCLAIMSLIST(
            new String[]{"Applicant", "Product Type", "Claim Amount", "Status", "Transaction Ref", "GT & SBLC Type", "Expiry Date", "Demand Type", "New Extension Date", "Issuing Bank"},
            new String[]{"beneficiaryName", "productType", "amountFormatted", "status", "guaranteesSRMSId", "guaranteeAndSBLCType", "expiredOnFormatted", "demandType", "newExtensionDate", "issuingBank"},
            ReceivedGuaranteeClaimsBusinessDelegateImpl.class, false),
    GENERATEISSUEDCLAIMSLIST(
            new String[]{"Guarantee/ Standby LC Reference", "Product Type", "Guarantee/ Standby LC Type", "Beneficiary", "Claim Date", "Expected Settlement Date", "Countdown", "Currency", "Claim Amount", "Demand Type", "Status"},
            new String[]{"guaranteesSRMSId", "productType", "guaranteeAndSBLCType", "beneficiaryName", "receivedOnFormatted", "expectedSettlementDate", "countDown", "claimCurrency", "amountFormatted", "demandType", "claimStatus"},
            IssuedGuaranteeClaimsBusinessDelegateImpl.class, false),
    GENERATEINWARDCOLLECTIONSLIST(
            new String[]{"Drawer", "Tenor Type", "Document No.", "Received On", "Amount", "Status", "Transaction Ref", "Maturity Date", "Amount debit from", "INCO Terms", "Remitting Bank"},
            new String[]{"drawerName", "tenorType", "documentNo", "receivedOnFormatted", "amountFormatted", "status", "collectionSrmsId", "maturityDateFormatted", "debitAmountFrom", "incoTerms", "remittingBank"},
            InwardCollectionsBusinessDelegateImpl.class, false),
    GENERATEINWARDAMENDMENTSLIST(
            new String[]{"Drawer", "Amendment No.", "Tenor Type", "Received On", "Amount", "Status", "Transaction Reference", "Maturity Date", "Remitting Bank"},
            new String[]{"drawer", "amendmentNo", "tenorType", "receivedOnFormatted", "amountFormatted", "status", "transactionReference", "maturityDateFormatted", "remittingBank"},
            InwardCollectionAmendmentsBusinessDelegateImpl.class, false),
    GENERATEOUTWARDCOLLECTIONSLIST(
            new String[]{"Drawee", "Tenor Type", "Document No.", "Updated On", "Currency", "Amount", "Status", "Transaction Ref", "Created On", "Usance Days", "Usance Details", "Collecting  Bank", "Maturity Date", "Bill of Exchange"},
            new String[]{"draweeName", "tenorType", "documentNo", "updatedOnFormatted", "currency", "amountFormatted", "status", "collectionReference", "createdOnFormatted", "usanceDays", "usanceDetails", "collectingBank", "maturityDateFormatted", "billOfExchangeStatus"},
            OutwardCollectionsBusinessDelegateImpl.class, false),
    GENERATEOUTWARDAMENDMENTSLIST(
            new String[]{"Drawee", "Amendment No.", "Transaction Ref", "Updated On", "Currency", "Amount", "Status", "Tenor Type", "Created On", "Maturity Date", "Collecting  Bank"},
            new String[]{"corporateUserName", "amendmentNo", "amendmentReference", "updatedOnFormatted", "currency", "amountFormatted", "status", "tenorType", "requestedOnFormatted", "maturityDateFormatted", "collectingBank"},
            OutwardCollectionAmendmentsBusinessDelegateImpl.class, false),
    GENERATERECEIVABLESLIST(
            new String[]{"Product", "Trans. Ref", "Amount", "Status", "Credit Account", "Avail Bal"},
            new String[]{"product", "transactionReference", "amountFormatted", "paymentStatus", "maskedCreditAccount", "balanceWithCurrency"},
            ReceivablesExcelBusinessDelegateImpl.class, true),
    GENERATEPAYABLESLIST(
            new String[]{"Product", "Trans. Ref", "Amount", "Status", "Debit Account", "Avail Bal"},
            new String[]{"product", "transactionReference", "amountFormatted", "paymentStatus", "maskedDebitAccount", "balanceWithCurrency"},
            PayablesExcelBusinessDelegateImpl.class, true),
    GENERATEALLTRADESLIST(
            new String[]{"Product", "Trans. Ref", "Update On", "Amount"},
            new String[]{"product", "transactionReference", "formattedDate", "amountFormatted"},
            AllTadeDetailsBusinessDelegateImpl.class, false);

    private final Class<? extends ExcelBusinessDelegate> businessDelegateImplName;
    private final String[] headersList;
    private final String[] fieldsList;
    private final boolean isDashboard;

    ExcelEnum(String[] headersList, String[] fieldsList, Class<? extends ExcelBusinessDelegate> businessDelegateImplName, boolean isDashboard) {
        this.businessDelegateImplName = businessDelegateImplName;
        this.fieldsList = fieldsList;
        this.headersList = headersList;
        this.isDashboard = isDashboard;
    }

    public Class<? extends ExcelBusinessDelegate> getBusinessDelegateImplName() {
        return businessDelegateImplName;
    }

    public String[] getHeadersList() {
        return headersList;
    }

    public String[] getFieldsList() {
        return fieldsList;
    }

    public boolean isDashboard() {
        return isDashboard;
    }
}

package com.kony.memorymgmt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.CorporateHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import org.json.JSONObject;

import java.util.Objects;

public class CorporateManager {

    public static final String CLAUSES = "CLAUSES";
    public static final String LIMITS = "LIMITS";
    public static final String BENEFICIARY = "CORPORATE_BENEFICIARY";
    public static final String BULKPAYMENTTEMPLATE = "BULKPAYMENTTEMPLATE";
    public static final String BULKPAYMENTHISTORY = "BULKPAYMENTHISTORY";
    public static final String DOCUMENTS = "DOCUMENTS";
    public static final String MESSAGEELIGIBLERECORDS = "MESSAGEELIGIBLERECORDS";
    public static final String COUNTRTESLIST = "COUNTRTESLIST";
    private SessionMap countriesMap = new SessionMap();
    private FabricRequestManager fabricRequestManager = null;
    private String customerId;
    private String sessionId;
    ObjectMapper mapper = new ObjectMapper();

    public CorporateManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public CorporateManager(DataControllerRequest request) {
        this.customerId = HelperMethods.getCustomerIdFromSession(request);
        this.sessionId = HelperMethods.getSessionTokenFromIdentityService(request);
    }

    public void saveClausesIntoSession(SessionMap clausesMap) {
        if (null != clausesMap) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.CLAUSES + this.customerId, clausesMap);
        }
    }

    public SessionMap getClausesFromSession() {
        SessionMap clausesMap =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.CLAUSES + this.customerId);
        if (null == clausesMap || clausesMap.isEmpty()) {
            clausesMap = CorporateHelper.reloadClausesIntoSession(fabricRequestManager);
        }
        return clausesMap;
    }

    public boolean validateClauseTitle(String title) {
        SessionMap clausesMap = getClausesFromSession();
        if (null == clausesMap) {
            return false;
        }
        if (clausesMap.hasKey(title))
            return true;
        else {
            clausesMap.addKey(title);
            saveClausesIntoSession(clausesMap);
            return false;
        }
    }

    public void saveLimitsIntoSession(SessionMap clausesMap) {
        if (null != clausesMap) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.LIMITS + this.customerId, clausesMap);
        }
    }

    public SessionMap getLimitsFromSession() {
        SessionMap limitsMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                CorporateManager.LIMITS + this.customerId);
        if (null == limitsMap || limitsMap.isEmpty()) {
            limitsMap = CorporateHelper.reloadClausesIntoSession(fabricRequestManager);
        }
        return limitsMap;
    }

    public boolean validateLimitInstructions(JSONObject limitObjectExternal) {
        SessionMap limitsMap = getLimitsFromSession();
        try {
            return limitsMap.hasKey(limitObjectExternal.getString("Limit iD"))
                    && Objects.equals(mapper.readTree(String.valueOf(limitObjectExternal)),
                    mapper.readTree(
                            (new JSONObject(limitsMap.getValue(limitObjectExternal.getString("Limit iD"))))
                                    .toString()));
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public void saveBeneficiaryDetailsIntoSession(SessionMap beneficiaryMap) {
        if (null != beneficiaryMap) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.BENEFICIARY + this.customerId, beneficiaryMap);
        }
    }

    public SessionMap getBeneficiaryDetailsFromSession() {
        SessionMap beneficiaryMap =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.BENEFICIARY + this.customerId);
        if (null == beneficiaryMap || beneficiaryMap.isEmpty()) {
            beneficiaryMap = CorporateHelper.reloadBeneficiaryIntoSession(fabricRequestManager);
        }
        return beneficiaryMap;
    }

    public boolean validateEditBeneficiary(String name) {
        SessionMap beneficiaryMap = getBeneficiaryDetailsFromSession();
        if (null == beneficiaryMap) {
            return false;
        }
        if (beneficiaryMap.hasKey(name)) {
            if (Integer.parseInt(beneficiaryMap.getAttributeValueForKey(name, "count")) > 1)
                return true;
            else {
                beneficiaryMap.addAttributeForKey(name, "count", "1");
                saveBeneficiaryDetailsIntoSession(beneficiaryMap);
                return false;
            }
        } else {
            return false;
        }
    }

    public void saveBulkPaymentTemplateIntoSession(SessionMap bulkPaymentTemplates) {
        if (null != bulkPaymentTemplates) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.BULKPAYMENTTEMPLATE + this.customerId, bulkPaymentTemplates);
        }
    }

    public SessionMap getBulkPaymentTemplateFromSession() {
        SessionMap bulkPaymentTemplates =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.BULKPAYMENTTEMPLATE + this.customerId);
        if (null == bulkPaymentTemplates || bulkPaymentTemplates.isEmpty()) {
            bulkPaymentTemplates = CorporateHelper.reloadBulkPaymentTemplatesIntoSession(fabricRequestManager);
        }
        return bulkPaymentTemplates;
    }

    public boolean validateBulkPaymentTemplateTitle(String title) {
        SessionMap bulkPaymentTemplates = getBulkPaymentTemplateFromSession();
        if (null == bulkPaymentTemplates) {
            return false;
        }
        return bulkPaymentTemplates.hasKey(title);

    }

    public void savePaymentHistoryIntoSession(SessionMap paymentHistory) {
        if (null != paymentHistory) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.BULKPAYMENTHISTORY + this.customerId, paymentHistory);
        }
    }

    public SessionMap getPaymentHistoryFromSession() {
        SessionMap paymentHistory =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.BULKPAYMENTHISTORY + this.customerId);
        if (null == paymentHistory || paymentHistory.isEmpty()) {
            paymentHistory = CorporateHelper.reloadPaymentHistoryIntoSession(fabricRequestManager);
        }
        return paymentHistory;
    }

    public boolean validateBulkPaymentOrderId(String recordId) {
        SessionMap paymentHistory = getPaymentHistoryFromSession();
        if (paymentHistory != null && !paymentHistory.isEmpty() && paymentHistory.hasKey(recordId)) {
            return true;
        }
        paymentHistory = CorporateHelper.reloadOnGoingPaymentsIntoSession(fabricRequestManager);
        if (paymentHistory.isEmpty())
            return false;
        savePaymentHistoryIntoSession(paymentHistory);
        return paymentHistory.hasKey(recordId);
    }

    public void saveDocumentsIntoSession(SessionMap documentsMap) {
        if (null != documentsMap) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.DOCUMENTS + this.customerId, documentsMap);
        }
    }

    public SessionMap getDocumentsFromSession() {
        return (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.DOCUMENTS + this.customerId);
    }

    public boolean validateDocument(String documentId, String documentName) {
        SessionMap documentsMap = getDocumentsFromSession();
        if (null == documentsMap) {
            return false;
        }
        return documentsMap.hasKey(documentId) &&
                documentsMap.getAttributeValueForKey(documentId, "documentName").equals(documentName);
    }

    public void saveMessageEligibleRecordsIntoSession(SessionMap records) {
        if (null != records) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.MESSAGEELIGIBLERECORDS + this.customerId, records);
        }
    }

    public SessionMap getApprovedRecordsFromSession() {
        return (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.MESSAGEELIGIBLERECORDS + this.customerId);
    }

    public boolean isRecordMessageEligible(String recordId) {
        SessionMap messageEligibleRecords = getApprovedRecordsFromSession();
        return messageEligibleRecords != null && !messageEligibleRecords.isEmpty() && messageEligibleRecords.hasKey(recordId);
    }

    public void saveCountriesListToSession(SessionMap countriesMap) {
        if (!countriesMap.isEmpty()) {
            MemoryManager.save(this.fabricRequestManager, CorporateManager.COUNTRTESLIST, countriesMap);
        }
    }

    public CorporateManager getCountriesFromSession() {
        countriesMap = countriesMap.isEmpty() ? (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, CorporateManager.COUNTRTESLIST) : countriesMap;
        return this;
    }

    public boolean isValidCountry(String countryName) {
        return countriesMap != null && !countriesMap.isEmpty() && countriesMap.hasKey(countryName);
    }

    public String getSessionId() {
        return sessionId;
    }
}

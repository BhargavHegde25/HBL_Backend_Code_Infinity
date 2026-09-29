package com.kony.model;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.memorymgmt.CorporateManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CorporateHelper {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private CorporateHelper() {
    }

    public static SessionMap reloadClausesIntoSession(FabricRequestManager fabricRequestManager) {
        try {
             String json = DBPServiceExecutorBuilder.builder().
                     withServiceId("TradeFinanceServices").
                     withObjectId(null).
                     withOperationId("getClauses").
                     withRequestParameters(new HashMap<>()).
                     build().getResponse();
            JsonParser parser = new JsonParser();
            JsonObject response = parser.parse(json).getAsJsonObject();
            if (null != response && !response.isJsonNull()) {
                JsonArray clauses = response.getAsJsonArray("clauses");
                SessionMap clausesMap = getClausesMap(clauses);
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, null);
                corporateManager.saveClausesIntoSession(clausesMap);
                return clausesMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading clauses:", e).log();
        }
        return new SessionMap();
    }

    private static SessionMap getClausesMap(JsonArray clauses) {
        SessionMap clausesMap = new SessionMap();
        if (null != clauses && !clauses.isJsonNull() && clauses.size() > 0) {
            for (JsonElement jsonElement : clauses) {
                JsonObject clause = jsonElement.getAsJsonObject();
                clausesMap.addKey(clause.get("clauseTitle").getAsString());
            }
        }
        return clausesMap;
    }

    public static SessionMap reloadBeneficiaryIntoSession(FabricRequestManager fabricRequestManager) {
        Map<String, Object> inputMap = new HashMap<>();
        try {
            String json = DBPServiceExecutorBuilder.builder().
                    withServiceId("TradeFinanceServices").
                    withOperationId("GetGuarantees").
                    withRequestParameters(inputMap).withFabricRequestManager(fabricRequestManager).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            JsonObject response = parser.parse(json).getAsJsonObject();
            if (null != response && !response.isJsonNull()) {
                JsonArray guarantees = response.getAsJsonArray("GuranteesLC");
                SessionMap beneficiaryMap = getBeneficiaryMap(guarantees);
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, null);
                corporateManager.saveBeneficiaryDetailsIntoSession(beneficiaryMap);
                return beneficiaryMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading guarantees:", e).log();
        }
        return new SessionMap();
    }

	private static SessionMap getBeneficiaryMap(JsonArray guarantees) {
		SessionMap beneficiaryMap = new SessionMap();
		if (null != guarantees && !guarantees.isJsonNull() && guarantees.size() > 0) {
			for (JsonElement jsonElement : guarantees) {
				JsonObject guarantee = jsonElement.getAsJsonObject();
				if (guarantee.has("beneficiaryDetails")) {
					JSONArray beneficiaryDetails = new JSONArray(guarantee.get("beneficiaryDetails"));
					// max length is 5. (length of guarantees * 5) -> tc
					for (int i = 0; i < beneficiaryDetails.length(); i++) {
						JSONObject beneficiary = beneficiaryDetails.getJSONObject(i);
						String beneficiaryName = beneficiary.get("beneficiaryName").toString();
						if (beneficiaryMap.hasKey(beneficiaryName)) {
							beneficiaryMap.addAttributeForKey(beneficiaryName, "count", String.valueOf(
									Integer.parseInt(beneficiaryMap.getAttributeValueForKey(beneficiaryName, "count"))
											+ 1));
						} else {
							beneficiaryMap.addAttributeForKey(beneficiaryName, "count", "1");
						}

					}
				}
			}
		}
		return beneficiaryMap;
	}
    public static SessionMap reloadBulkPaymentTemplatesIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            String json = DBPServiceExecutorBuilder.builder().
                    withServiceId("dbpBulkPaymentServices").
                    withObjectId(null).
                    withOperationId("fetchBulkPaymentTemplates").
                    withRequestParameters(new HashMap<>()).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            JsonObject response = parser.parse(json).getAsJsonObject();
            if (null != response && !response.isJsonNull()) {
                JsonArray bulkPaymentTemplates = response.getAsJsonArray("bulkPaymentTemplates");
                SessionMap bulkPaymentTemplatesMap = getBulkPaymentTemplatesMap(bulkPaymentTemplates);
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, null);
                corporateManager.saveBulkPaymentTemplateIntoSession(bulkPaymentTemplatesMap);
                return bulkPaymentTemplatesMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading fetchBulkPaymentTemplates:", e).log();
        }
        return new SessionMap();
    }

    public static SessionMap reloadPaymentHistoryIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("timeValue","");
            String json = DBPServiceExecutorBuilder.builder().
                    withServiceId("BulkPaymentObjects").
                    withObjectId("BulkPaymentRecord").
                    withOperationId("fetchHistory").
                    withFabricRequestManager(fabricRequestManager).
                    withRequestParameters(inputParams).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            JsonObject response = parser.parse(json).getAsJsonObject();
            if (null != response && !response.isJsonNull()) {
                JsonArray paymentHistory = response.getAsJsonArray("history");
                SessionMap paymentHistoryMap = getPaymentHistoryMap(paymentHistory);
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, null);
                corporateManager.savePaymentHistoryIntoSession(paymentHistoryMap);
                return paymentHistoryMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading fetchBulkPaymentTemplates:", e).log();
        }
        return new SessionMap();
    }

    public static SessionMap reloadOnGoingPaymentsIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("timeValue","");
            String json = DBPServiceExecutorBuilder.builder().
                    withServiceId("BulkPaymentObjects").
                    withObjectId("BulkPaymentRecord").
                    withOperationId("fetchOnGoingPayments").
                    withFabricRequestManager(fabricRequestManager).
                    withRequestParameters(inputParams).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            JsonObject response = parser.parse(json).getAsJsonObject();
            if (null != response && !response.isJsonNull()) {
                JsonArray paymentHistory = response.getAsJsonArray("onGoingPayments");
                SessionMap paymentHistoryMap = getPaymentHistoryMap(paymentHistory);
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, null);
                corporateManager.savePaymentHistoryIntoSession(paymentHistoryMap);
                return paymentHistoryMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading fetchBulkPaymentTemplates:", e).log();
        }
        return new SessionMap();
    }

    private static SessionMap getPaymentHistoryMap(JsonArray paymentHistory) {
        SessionMap paymentHistoryMap = new SessionMap();
        if (null != paymentHistory && !paymentHistory.isJsonNull() && paymentHistory.size() > 0) {
            for (JsonElement jsonElement : paymentHistory) {
                JsonObject template = jsonElement.getAsJsonObject();
                paymentHistoryMap.addKey(template.get("recordId").getAsString());
            }
        }
        return paymentHistoryMap;
    }

    private static SessionMap getBulkPaymentTemplatesMap(JsonArray bulkPaymentTemplates) {
        SessionMap bulkPaymentTemplatesMap = new SessionMap();
        if (null != bulkPaymentTemplates && !bulkPaymentTemplates.isJsonNull() && bulkPaymentTemplates.size() > 0) {
            for (JsonElement jsonElement : bulkPaymentTemplates) {
                JsonObject template = jsonElement.getAsJsonObject();
                bulkPaymentTemplatesMap.addKey(template.get("templateId").getAsString());
            }
        }
        return bulkPaymentTemplatesMap;
    }
}

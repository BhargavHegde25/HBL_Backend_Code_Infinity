/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.sessionmanagement;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.CorporateManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaveGuaranteesPayeesIntoSession implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            SessionMap beneficiaryMap = new SessionMap();
            SessionMap approvedGuranteeRecordsMap = new SessionMap();

            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            if (null != response && !response.isJsonNull()) {
                if (response.has("GuaranteesLC")) {
                    JsonArray guarantees = response.getAsJsonArray("GuaranteesLC");
                    ArrayList<SessionMap> list = getBeneficiaryMap(guarantees);
                    beneficiaryMap = list.get(0);
                    approvedGuranteeRecordsMap = list.get(1);
                }
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                corporateManager.saveBeneficiaryDetailsIntoSession(beneficiaryMap);
               corporateManager.saveMessageEligibleRecordsIntoSession(approvedGuranteeRecordsMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while caching payees in session", e).log();
        }
        return true;

    }

    private ArrayList<SessionMap> getBeneficiaryMap(JsonArray guarantees) {
        ArrayList<SessionMap> list = new ArrayList<SessionMap>();
        SessionMap beneficiaryMap = new SessionMap();
        SessionMap approvedGuranteeRecordsMap = new SessionMap();
        if (null != guarantees && !guarantees.isJsonNull() && guarantees.size() > 0) {
            for (JsonElement jsonElement : guarantees) {
                JsonObject guarantee = jsonElement.getAsJsonObject();
                if (guarantee.has("beneficiaryDetails")) {
                    String beneDetails = guarantee.get("beneficiaryDetails").toString();
                    try {
                        JSONArray beneficiaryDetails = new JSONArray(beneDetails.substring(1, beneDetails.length() - 1));
                        // max length is 5. (length of guarantees * 5) -> tc
                        for (int i = 0; i < beneficiaryDetails.length(); i++) {
                            JSONObject beneficiary = beneficiaryDetails.getJSONObject(i);
                            if (beneficiary.has("payeeId") || beneficiary.has("isCorporate")) {
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
                    } catch (Exception e) {
                        alert.prepareError("Unable to parse beneficiary details, it is not in the correct format").log();
                    }
                }
                /**
                 *  Used to store the Approved record in to the session for validating the proper message sending or not
                 */
                if(guarantee.get("status").getAsString().equalsIgnoreCase(TradeFinanceConstants.PARAM_STATUS_APPROVED))
                {
                    approvedGuranteeRecordsMap.addKey(guarantee.get("guaranteesSRMSId").getAsString());
                }
            }
        }

        list.add(beneficiaryMap);
        list.add(approvedGuranteeRecordsMap);
        return list;
    }
}
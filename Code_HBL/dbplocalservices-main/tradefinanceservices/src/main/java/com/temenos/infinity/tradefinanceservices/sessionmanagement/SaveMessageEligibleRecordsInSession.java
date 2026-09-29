/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
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

import java.util.Arrays;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
public class SaveMessageEligibleRecordsInSession implements ObjectProcessorTask {

    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
        if (null != response && !response.isJsonNull()) {
            String collectionName = response.keySet().toArray()[0].toString();
            if (response.has(collectionName)) {
                SessionMap eligibleRecordsMap = getRecords(collectionName, response.getAsJsonArray(collectionName));
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                corporateManager.saveMessageEligibleRecordsIntoSession(eligibleRecordsMap);
            }
        }
        return true;
    }

    private SessionMap getRecords(String collectionName, JsonArray records) {
        String statusField;
        switch (collectionName) {
            case "Amendments":
            case "GuaranteeLCAmendments":
                statusField = "amendStatus";
                break;
            case "ExportLcAmendments":
                statusField = "amendmentStatus";
                break;
            case "ClaimsReceived":
                statusField = "claimStatus";
                break;
            default:
                statusField = "status";
        }
        SessionMap eligibleRecordsMap = new SessionMap();
        if (null != records && !records.isJsonNull() && records.size() > 0) {
            for (JsonElement jsonElement : records) {
                JsonObject record = jsonElement.getAsJsonObject();
                if (Arrays.asList(PARAM_STATUS_APPROVED, PARAM_STATUS_PAID, PARAM_STATUS_SETTLED, PARAM_STATUS_CLAIMHONOURED, PARAM_STATUS_OVERDUE).contains(record.get(statusField).getAsString())) {
                    eligibleRecordsMap.addKey(record.get("serviceRequestSrmsId").getAsString());
                }
            }
        }
        return eligibleRecordsMap;
    }
}

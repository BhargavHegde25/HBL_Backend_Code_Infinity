/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.sessionmanagement;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.CorporateManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class SaveLimitInstructionsIntoSession implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            SessionMap limitsMap = new SessionMap();
            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            if (null != response && !response.isJsonNull()) {
                if (response.has("Limits")) {
                    JsonObject limitsData = new JsonObject();
                    JsonArray limitsArray = response.getAsJsonArray("Limits");
                    limitsArray.forEach(jsonElement -> {
                        limitsData.add(jsonElement.getAsJsonObject().get("Limit iD").getAsString(), jsonElement);
                    });
                    limitsMap.setData(limitsData.toString());
                }
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                corporateManager.saveLimitsIntoSession(limitsMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while caching payees in session", e).log();
        }
        return true;
    }
}
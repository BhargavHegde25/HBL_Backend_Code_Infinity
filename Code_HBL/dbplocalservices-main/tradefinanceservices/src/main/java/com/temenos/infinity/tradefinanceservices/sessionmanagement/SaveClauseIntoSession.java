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
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class SaveClauseIntoSession implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            SessionMap clausesMap = new SessionMap();
            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            if (null != response && !response.isJsonNull()) {
                if (response.has("clauses")) {
                    JsonArray clauses = response.getAsJsonArray("clauses");
                    clausesMap = getClausesMap(clauses);
                }
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                corporateManager.saveClausesIntoSession(clausesMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while caching payees in session", e).log();
        }
        return true;
    }

    private SessionMap getClausesMap(JsonArray clauses) {
        SessionMap clausesMap = new SessionMap();
        if (null != clauses && !clauses.isJsonNull() && clauses.size() > 0) {
            for (JsonElement jsonElement : clauses) {
                JsonObject clause = jsonElement.getAsJsonObject();
                clausesMap.addKey(clause.get("clauseTitle").getAsString());
            }
        }
        return clausesMap;
    }
}

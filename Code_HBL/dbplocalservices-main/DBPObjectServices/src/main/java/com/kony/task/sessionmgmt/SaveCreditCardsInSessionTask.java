package com.kony.task.sessionmgmt;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.SessionMap;
import com.kony.memorymgmt.TransactionManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class SaveCreditCardsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            saveCreditCards(response, fabricRequestManager, fabricResponseManager);
        } catch (Exception e) {
            alert.prepareError("Exception while caching accounts in session", e).log();
        }
        return true;
    }

    public static void saveCreditCards(JsonObject response, FabricRequestManager fabricRequestManager,
            FabricResponseManager fabricResponseManager) {
        String AccountsObject = "Accounts";
        diagnostic.prepareDebug("Account Helper: Accounts Response: " + response.toString()).log();
        if (null != response && !response.isJsonNull()) {
            if (response.has(AccountsObject)) {
                JsonArray accounts = response.getAsJsonArray(AccountsObject);
                SessionMap creditCards = new SessionMap();
                if (null != accounts && !accounts.isJsonNull() && accounts.size() > 0) {
                    Iterator<JsonElement> itr = accounts.iterator();
                    while (itr.hasNext()) {
                        creditCards.addKey(itr.next().getAsJsonObject().get("accountID").getAsString());
                    }
                }
                alert.prepareError("SessionMap of Credit Cards: " + creditCards.toString()).log();
                TransactionManager transManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
                transManager.saveCreditCardsIntoSession(creditCards);
                alert.prepareError("Credit Cards Saved Successfully").log();
            }
        }
    }

}

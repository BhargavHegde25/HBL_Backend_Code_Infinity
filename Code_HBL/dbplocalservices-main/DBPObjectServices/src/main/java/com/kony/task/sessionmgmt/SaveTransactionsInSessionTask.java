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

public class SaveTransactionsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if (null != response && !response.isJsonNull()) {
			    JsonArray accounts = new JsonArray();
			    if(response.has("OneTimeTransfer"))
			        accounts = response.getAsJsonArray("OneTimeTransfer");
			    else
			        accounts = response.getAsJsonArray("Transactions");
				SessionMap transactionsMap = getAccountsSet(accounts);
				TransactionManager transManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
				transManager.saveTransactionsIntoSession(transactionsMap);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while caching accounts in session", e).log();
		}
		return true;
	}

	private SessionMap getAccountsSet(JsonArray accounts) {
		SessionMap accountsMap = new SessionMap();
		if (null != accounts && !accounts.isJsonNull() && accounts.size() > 0) {
			Iterator<JsonElement> itr = accounts.iterator();
			while (itr.hasNext()) {
				accountsMap.addKey(itr.next().getAsJsonObject().get("transactionId").getAsString());
			}
		}
		return accountsMap;
	}

}

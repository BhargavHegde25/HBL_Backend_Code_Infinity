package com.kony.task.sessionmgmt;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.PayeeManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SaveWireTransferPayeesInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if (null != response && !response.isJsonNull()) {
				JsonArray payees = response.getAsJsonArray("Payee");
				SessionMap wirePayeesMap = getPayeesMap(payees);
				PayeeManager payeemanager = new PayeeManager(fabricRequestManager, fabricResponseManager);
				payeemanager.saveWireTransferPayeesIntoSession(wirePayeesMap);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while caching payees in session", e).log();
		}
		return true;
	}

	private SessionMap getPayeesMap(JsonArray payees) {
		SessionMap payeesMap = new SessionMap();
		if (null != payees && !payees.isJsonNull() && payees.size() > 0) {
			Iterator<JsonElement> itr = payees.iterator();
			while (itr.hasNext()) {
				JsonObject payee = itr.next().getAsJsonObject();
				payeesMap.addAttributeForKey(payee.get("payeeId").getAsString(), "wireAccountType",
						payee.get("wireAccountType").getAsString());
				payeesMap.addAttributeForKey(payee.get("payeeId").getAsString(), "payeeAccountNumber",
                        payee.get("accountNumber").getAsString());
			}
		}
		return payeesMap;
	}

}

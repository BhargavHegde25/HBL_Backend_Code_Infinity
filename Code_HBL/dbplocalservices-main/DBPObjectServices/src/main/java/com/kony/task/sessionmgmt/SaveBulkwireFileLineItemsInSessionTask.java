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

public class SaveBulkwireFileLineItemsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if (null != response && !response.isJsonNull()) {
				JsonArray BulkWires = response.getAsJsonArray("BulkWireTemplateLineItems");
				SessionMap BulkWiresMap = getPayeesMap(BulkWires);
				PayeeManager payeemanager = new PayeeManager(fabricRequestManager, fabricResponseManager);
				payeemanager.saveBulkwireFileLineItemsIntoSession(BulkWiresMap);
			}
//			if (null != response && !response.isJsonNull()) {
//				JsonArray payees =  response.getAsJsonArray("BulkWireFileLineItems");
//				
//				SessionMap payeesMap = new SessionMap();
//				if (null != payees && !payees.isJsonNull() && payees.size() > 0)
//				{
//					JsonObject allPayees = (JsonObject) payees.get(0);
//					if(allPayees.has("Domestic")) {
//					domesticPayees =  allPayees.getAsJsonArray("Domestic");
//					}
//					if(allPayees.has("International")) {
//						internationalPayees =  allPayees.getAsJsonArray("International");
//						}
//					domesticPayees.addAll(internationalPayees);
//					
//				}				   
//				}
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
				payeesMap.addKey(payee.get("recipientAccountNumber").getAsString());
			}
		}
		return payeesMap;
	}

}


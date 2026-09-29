package com.kony.consent.task.sessionmgmt;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.ConsentsManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SavePSDConsentArrangementID implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if (null != response && !response.isJsonNull()) {
				JsonArray consents = response.getAsJsonArray("consentTypes");
				SessionMap consentsMap = getConsentsMap(consents);
				ConsentsManager consentsManager = new ConsentsManager(fabricRequestManager, fabricResponseManager);
				consentsManager.savePSDConsentIntoSession(consentsMap);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while caching PSD consents in session", e).log();
		}
		return true;
	}
	
	private SessionMap getConsentsMap(JsonArray consents) {
		SessionMap consentsMap = new SessionMap();
		if (null != consents && !consents.isJsonNull() && consents.size() > 0) {
			Iterator<JsonElement> itr = consents.iterator();
			do{
				JsonElement element = itr.next();
				consentsMap.addAttributeForKey("PSD_CONSENTS", element.getAsJsonObject().get("arrangementId").getAsString(), element.getAsJsonObject().get("arrangementId").getAsString());
			}while (itr.hasNext());
		}
		//consentsMap.addKey(consents.getAsJsonObject().get("arrangementId").getAsString());
		return consentsMap;
	}

}

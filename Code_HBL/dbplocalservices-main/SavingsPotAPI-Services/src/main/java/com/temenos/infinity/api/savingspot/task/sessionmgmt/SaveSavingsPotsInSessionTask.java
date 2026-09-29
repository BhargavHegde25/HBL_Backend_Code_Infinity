package com.temenos.infinity.api.savingspot.task.sessionmgmt;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.api.savingspot.model.SavingsPotHelper;

public class SaveSavingsPotsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			SavingsPotHelper.saveSavingsPotsofUserIntoSession(response, fabricRequestManager);
		} catch (Exception e) {
			alert.prepareError("Exception while caching SavingsPot in session", e).log();
		}
		return true;
	}

}

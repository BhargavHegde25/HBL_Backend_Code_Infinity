package com.kony.task.sessionmgmt;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.model.UserDetailsHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SaveUserDetailsInSessionTask implements ObjectProcessorTask{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			UserDetailsHelper.saveUserDetailsIntoSession(response, fabricRequestManager);
		} catch (Exception e) {
			alert.prepareError("Exception while caching userdetails in session", e).log();
		}
		return true;
	}

}

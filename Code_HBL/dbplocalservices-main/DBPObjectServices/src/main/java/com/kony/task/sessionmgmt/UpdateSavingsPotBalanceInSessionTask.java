package com.kony.task.sessionmgmt;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.SavingsPotManager;
import com.kony.model.SavingsPotHelper;
import com.kony.utilities.Constants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UpdateSavingsPotBalanceInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement savingsPotId = null;
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if(!response.has("dbpErrMsg") && !response.has("dbpErrCode")) {
			JsonObject request = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
			String customerId = HelperMethods.getAPIUserIdFromSession(fabricRequestManager).toString();
		    String updatedAvailableBalance = request.get(Constants.AMOUNT).toString();
		    savingsPotId =  request.get(Constants.SAVINGSPOTID);
			SavingsPotManager savingsPotManager = new SavingsPotManager(fabricRequestManager, fabricResponseManager);
			savingsPotManager.setAvailableBalanceInPot(customerId, savingsPotId, updatedAvailableBalance);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while updating SavingsPot with id"+savingsPotId.toString()+" in session", e).log();
		}
		return true;
	}

}

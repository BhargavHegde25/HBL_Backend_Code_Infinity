package com.kony.task.datavalidation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UpdateFeatureActionIdTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
			if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
				JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
				PostLoginMFAUtil mfaUtil = new PostLoginMFAUtil(fabricRequestManager, "");
				ServicesManager sm = fabricRequestManager.getServicesManager();
				String appendedString = HelperMethods.getOperationString(sm);
				String featureActionId = mfaUtil.getValidServiceName(requestPayload, appendedString.toLowerCase());
				requestPayload.addProperty(MFAConstants.SERVICE_NAME, featureActionId);
				fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}
		} catch (Exception e) {
			alert.prepareError("Error while loading payees into session", e).log();
		}
		return true;
	}

}
package com.temenos.infinity.api.holdings.datavalidation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class DisputeTransactionValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
//		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
//		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
//			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
//			//String type = URLFinder.getPropertyValue("disputeTransactionType", "objectservice.properties");
//			//String subtype = URLFinder.getPropertyValue("disputeTransactionSubType", "objectservice.properties");
////			requestPayload.addProperty("type", "DisputeTransaction");
////			requestPayload.addProperty("subType", "CancelDisputeTransaction");
////			fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
//		}
//"subtype" param is case different in create and get order services.So adding a check to confirm if it is create/get call and adding param "subtype" accordingly.
		fabricRequestManager.getQueryParamsHandler().addParameter("type", "DisputeTransaction");
		
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			fabricRequestManager.getQueryParamsHandler().addParameter("subType", "CancelDisputeTransaction");
		} else {
			fabricRequestManager.getQueryParamsHandler().addParameter("subtype", "CancelDisputeTransaction");
		}
		return true;
	}
}

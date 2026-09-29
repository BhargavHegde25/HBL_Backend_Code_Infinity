package com.kony.consent.task.datavalidation;


import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UpdatePSDConsentOrderManagementTask implements ObjectProcessorTask {

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			//String type = URLFinder.getPropertyValue("disputeTransactionType", "objectservice.properties");
			//String subtype = URLFinder.getPropertyValue("disputeTransactionSubType", "objectservice.properties");
			requestPayload.addProperty("type", "PSD2Consent");
			requestPayload.addProperty("subtype", "UpdatePSD2Consent");
			fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
		}
		return true;
	}
}

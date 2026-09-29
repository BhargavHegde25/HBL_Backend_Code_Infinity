package com.kony.task.datavalidation;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class AddUserIdTask implements ObjectProcessorTask{

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
		requestPayload.addProperty("userId", (String) fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes().get("customer_id"));
		fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
		return true;
	}
}

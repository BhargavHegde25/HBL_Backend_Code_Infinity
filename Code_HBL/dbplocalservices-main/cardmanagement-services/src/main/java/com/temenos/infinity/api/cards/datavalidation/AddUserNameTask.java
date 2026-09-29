package com.temenos.infinity.api.cards.datavalidation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class AddUserNameTask implements ObjectProcessorTask{

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
		requestPayload.addProperty("userName", (String) fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes().get("UserName"));
		fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
		return true;
	}
}

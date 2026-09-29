package com.kony.task.datavalidation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SkipValidationTask implements ObjectProcessorTask {

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
				IdentityHandler identityHandler = fabricRequestManager.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				String isSchedulingEngine = (String) userAttributes.get("isSchedulingEngine");
				if (isSchedulingEngine != null && !"".equalsIgnoreCase(isSchedulingEngine)
						&& "true".equalsIgnoreCase(isSchedulingEngine)) {
					requestPayload.addProperty("skipDAC", "true");
					fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
				}

				String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "fromAccountNumber");
				String toAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "toAccountNumber");
				String amount = HelperMethods.getStringFromJsonObject(requestPayload, "amount");
				if (!HelperMethods.isMFAVerify(requestPayload) && StringUtils.isBlank(fromAccountNumber)
						&& StringUtils.isBlank(toAccountNumber) && StringUtils.isBlank(amount)) {
					requestPayload.addProperty("skipDAC", "true");
					fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
				}
			}
		} catch (Exception e) {

		}
		return true;
	}

}

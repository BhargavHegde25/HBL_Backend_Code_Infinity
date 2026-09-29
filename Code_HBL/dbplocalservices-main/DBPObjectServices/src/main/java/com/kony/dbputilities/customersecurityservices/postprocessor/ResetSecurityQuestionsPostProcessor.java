package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class ResetSecurityQuestionsPostProcessor implements ObjectServicePostProcessor {

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject response = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		JsonObject request = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		if (response.has(ErrorCodeEnum.ERROR_MESSAGE_KEY) || response.has(ErrorCodeEnum.ERROR_CODE_KEY)) {
			responseManager.getPayloadHandler().updatePayloadAsJson(response);
			return;
		}
		String status = response.get("status").getAsString();

		if (StringUtils.isNotBlank(status) && "success".equalsIgnoreCase(status)) {

			removeServiceKey(requestManager, response, request);

		}

		responseManager.getPayloadHandler().updatePayloadAsJson(response);
		return;
	}

	private void removeServiceKey(FabricRequestManager requestManager, JsonObject response, JsonObject request) {

		String serviceKey = new String();
		JsonObject mfaAttributes = request.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

		if (mfaAttributes.has(MFAConstants.SERVICE_KEY) && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
			serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
		}

		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("serviceKey", serviceKey);

		HelperMethods.callApi(requestManager, hashMap, HelperMethods.getHeaders(requestManager),
				URLConstants.MFA_SERVICE_DELETE);

		return;

	}
}

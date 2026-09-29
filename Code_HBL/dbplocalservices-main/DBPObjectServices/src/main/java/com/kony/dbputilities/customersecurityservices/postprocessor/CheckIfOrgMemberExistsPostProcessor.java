package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;

public class CheckIfOrgMemberExistsPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject resultJson = new JsonObject();
		JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		if (jsonObject.has(DBPConstants.DBP_ERROR_CODE_KEY) || jsonObject.has(DBPConstants.DBP_ERROR_MESSAGE_KEY)) {
			responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
			return;
		}

		String status = jsonObject.get("Status").getAsString();
		JsonObject jsoninput = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		if ("true".equalsIgnoreCase(status)) {

			String serviceKey = updateDataInMFAService(requestManager, jsonObject, jsoninput);
			if (StringUtils.isNotBlank(serviceKey)) {
				jsonObject.addProperty("serviceKey", serviceKey);
			} else {
				ErrorCodeEnum.ERR_10540.setErrorCode(resultJson);
				responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
				return;
			}
		}

		responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
	}

	private String updateDataInMFAService(FabricRequestManager requestManager, JsonObject jsonObject,
			JsonObject jsoninput) {
		String serviceKey = HelperMethods.getNewId();
		String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
		String retryCount = "0";
		String Createddts = HelperMethods.getCurrentTimeStamp();
		String payload = jsoninput.toString();

		Map<String, String> mfaservice = new HashMap<>();
		mfaservice.put("serviceKey", serviceKey);
		mfaservice.put("serviceName", serviceName);
		mfaservice.put("Createddts", Createddts);
		mfaservice.put("retryCount", retryCount);
		try {
			payload = CryptoText.encrypt(payload);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
			return null;
		}

		mfaservice.put("payload", payload);

		Result mfaserviceResult = new Result();
		mfaserviceResult = HelperMethods.callApi(requestManager, mfaservice, HelperMethods.getHeaders(requestManager),
				URLConstants.MFA_SERVICE_CREATE);
		if (HelperMethods.hasRecords(mfaserviceResult)) {
			return HelperMethods.getFieldValue(mfaserviceResult, "serviceKey");
		}
		return null;
	}

}

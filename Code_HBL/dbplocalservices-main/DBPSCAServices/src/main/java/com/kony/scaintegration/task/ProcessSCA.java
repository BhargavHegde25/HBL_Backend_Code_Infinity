package com.kony.scaintegration.task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.scaintegration.helper.Constants;
import com.kony.scaintegration.helper.ErrorCodeEnum;
import com.kony.scaintegration.helper.GetConfigParams;
import com.kony.scaintegration.helper.Helper;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;

public class ProcessSCA implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonObject resultObj = new JsonObject();
		PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();
		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();

		fabricResponseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
				ContentType.APPLICATION_JSON.getMimeType());

		if (GetConfigParams.getIsScaEnabled() == null)
			GetConfigParams.setIsScaEnabled(Helper.getConfigProperty(Constants.ISSCAENABLED));
		if (GetConfigParams.getIsScaEnabled() == null) {
			alert.prepareError("IS_SCA_ENABLED runtime param must be set").log();
		}
		if (!Boolean.parseBoolean(GetConfigParams.getIsScaEnabled()))
			return true;
		if (checkSkipSca(fabricRequestManager))
			return true;
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson() == null
				|| requestPayloadHandler.getPayloadAsJson().isJsonNull() ? new JsonObject()
						: requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		diagnostic.prepareDebug("requestpayload " + requestPayload).log();

		if (requestPayload != null) {
			JsonObject mfaAttributes = getMfaAttributes(requestPayload);
			String serviceKey = mfaAttributes.get(Constants.SERVICEKEY) == null ? null
					: mfaAttributes.get(Constants.SERVICEKEY).getAsString();
			Map<String, Object> inputmap = new HashMap<>();
			String userId = com.kony.dbputilities.util.HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			diagnostic.prepareDebug("userId " + userId).log();
			if (StringUtils.isBlank(serviceKey)) {
				return processGetServicekey(requestPayload, responsePayloadHandler, resultObj, inputmap, userId);
			} else if (StringUtils.isNotBlank(serviceKey)) {
				return processGetRequestPayload(requestPayload, requestPayloadHandler, responsePayloadHandler, serviceKey, resultObj,
						inputmap, userId);
			}

		}

		return true;

	}

	private boolean checkSkipSca(FabricRequestManager fabricRequestManager) {
		String skipSca = null;
		try {
			skipSca = Helper.getConfigProperty(Constants.SKIPSCA);
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		if (StringUtils.isNotBlank(skipSca)) {
			String userLoginAgentType = getUserLoginAgentType(fabricRequestManager);
			Set<String> skipScaSet = Arrays.stream(skipSca.split(",")).collect(Collectors.toSet());
			if (StringUtils.isNotBlank(userLoginAgentType) && skipScaSet.contains(userLoginAgentType)) {
				return true;
			}
		}
		return false;
	}

	private String getUserLoginAgentType(FabricRequestManager fabricRequestManager) {
		String userAgentLoginType = null;
		try {
			userAgentLoginType = fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(Constants.USERAGENTLOGINTYPE).toString();
			if (StringUtils.isBlank(userAgentLoginType)) {
				Result userAttributesResponse = new Result();
				try {
					userAttributesResponse = ServiceCallHelper.invokeServiceAndGetResult(null,
							HelperMethods.getHeaders(fabricRequestManager), URLConstants.USER_ID_GET_IDNETITY,
							fabricRequestManager.getQueryParamsHandler().getParameter("Auth_Token"));
				} catch (HttpCallException e) {
					alert.prepareError(e.getMessage()).log();
				}

				if (userAttributesResponse.getNameOfAllParams().contains(Constants.USERAGENTLOGINTYPE)) {
					userAgentLoginType = userAttributesResponse.getParamValueByName(Constants.USERAGENTLOGINTYPE);
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		return userAgentLoginType;
	}

	private JsonObject getMfaAttributes(JsonObject requestPayload) {
		JsonObject mfaAttributes = null;
		try {
			JsonElement mfaElement = requestPayload.get(MFAConstants.MFA_ATTRIBUTES);
			if (mfaElement != null && mfaElement.isJsonObject()) {
				mfaAttributes = requestPayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
			} else if (mfaElement != null) {
				JsonParser parser = new JsonParser();
				mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
			} else {
				mfaAttributes = new JsonObject();
			}
		} catch (Exception e) {
			mfaAttributes = new JsonObject();
			alert.prepareError(e.getMessage()).log();
		}
		return mfaAttributes;
	}

	private boolean processGetRequestPayload(JsonObject requestPayload, PayloadHandler requestPayloadHandler,
			PayloadHandler responsePayloadHandler, String serviceKey, JsonObject resultObj,
			Map<String, Object> inputmap, String userId) {
		String encryptedUserId = "";
		try {
			if (StringUtils.isNotBlank(userId)) {
				encryptedUserId = CryptoText.encrypt(userId);
				inputmap.put(Constants.USERID, encryptedUserId);
			}
			inputmap.put(Constants.SERVICEKEY, serviceKey);

			String responseString = DBPServiceExecutorBuilder.builder().withOperationId("getRequestPayload")
					.withRequestParameters(inputmap).withServiceId(Constants.SCAINTEGRATIONSERVICE).build()
					.getResponse();
			JsonObject responseJsonObject = new JsonParser().parse(responseString).getAsJsonObject();
			diagnostic.prepareDebug("responseJsonObject " + responseJsonObject).log();
			if (responseJsonObject.get(Constants.PAYLOAD) == null) {
				ErrorCodeEnum.ERR_39002.setErrorCode(resultObj);
				responsePayloadHandler.updatePayloadAsJson(resultObj);
				return false;
			}
			JsonObject payload = new JsonParser()
					.parse(CryptoText.decrypt(responseJsonObject.get(Constants.PAYLOAD).getAsString()))
					.getAsJsonObject();
			for (Map.Entry<String, JsonElement> entry : requestPayload.entrySet()) {
				payload.add(entry.getKey(), entry.getValue());
			}
			diagnostic.prepareDebug("payload "+payload).log();
			requestPayloadHandler.updatePayloadAsJson(payload);
			//responsePayloadHandler.updatePayloadAsJson(payload);
			return true;
		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}
		responsePayloadHandler.updatePayloadAsJson(ErrorCodeEnum.ERR_39002.setErrorCode(resultObj));
		return false;
	}

	private boolean processGetServicekey(JsonObject requestPayload, PayloadHandler responsePayloadHandler,
			JsonObject resultObj, Map<String, Object> inputmap, String userId) {
		String encryptedPayload = "";
		String encryptedUserId = "";
		try {
			encryptedPayload = CryptoText.encrypt(requestPayload.toString());
			if (StringUtils.isNotBlank(userId)) {
				encryptedUserId = CryptoText.encrypt(userId);
				inputmap.put(Constants.USERID, encryptedUserId);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured during encryption ", e).log();
		}
		inputmap.put(Constants.PAYLOAD, encryptedPayload);
		try {
			String responseString = DBPServiceExecutorBuilder.builder().withOperationId("getServiceKey")
					.withRequestParameters(inputmap).withServiceId(Constants.SCAINTEGRATIONSERVICE).build()
					.getResponse();
			JsonObject responseJsonObject = new JsonParser().parse(responseString).getAsJsonObject();
			if (responseJsonObject.get(Constants.SERVICEKEY) == null) {
				ErrorCodeEnum.ERR_39001.setErrorCode(resultObj);
				responsePayloadHandler.updatePayloadAsJson(resultObj);
				return false;
			}
			JsonObject mfaAttributes = new JsonObject();
			mfaAttributes.addProperty("serviceKey", responseJsonObject.get(Constants.SERVICEKEY).getAsString());
			mfaAttributes.addProperty("isMFARequired", "true");
			resultObj.add("MFAAttributes", mfaAttributes);
			responsePayloadHandler.updatePayloadAsJson(resultObj);
			return false;
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		responsePayloadHandler.updatePayloadAsJson(ErrorCodeEnum.ERR_39001.setErrorCode(resultObj));
		return false;
	}

}

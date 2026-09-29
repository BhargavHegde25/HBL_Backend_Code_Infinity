package com.kony.kmsinvoke.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.kmsinvoke.businessdelegate.api.SendPushNotificationBusinessDelegate;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.KmsInvokeEnum;

public class SendPushNotificationBusinessDelegateImpl implements SendPushNotificationBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	@Override
	public JsonObject sendPushNotification(JsonObject inputparamsObj) {
		JsonObject resultObj = new JsonObject();
		if (!HelperMethods.isValidInputParams(inputparamsObj,KmsInvokeConstants.MESSAGEREQUEST))
			return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_PUSHEXCEPTION);
		String apiKey = null;
		try {
			apiKey = HelperMethods.getConfigProperty(KmsInvokeConstants.KMSAPIKEY);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		if (StringUtils.isBlank(apiKey))
			return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_APIKEY);
		Map<String, String> headerparams = new HashMap<>();
		headerparams.put("X-Kony-App-API-Key", apiKey);
		headerparams.put("Content-Type", "application/json");

		try {
			String url = HelperMethods.getConfigProperty(KmsInvokeConstants.DBX_KMS_PUSH);
			if (url == null) {
				return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_PUSHAPIURL);

			}
			url = url + "?checkUserExistence=false";
			Map<String, Object> inputparams = new HashMap<>();
			inputparams.put(KmsInvokeConstants.MESSAGEREQUEST,
					inputparamsObj.get(KmsInvokeConstants.MESSAGEREQUEST).getAsJsonObject());
			resultObj = HelperMethods.callhttpApi(inputparams, headerparams, url);
			if (!resultObj.has(KmsInvokeConstants.ID) || resultObj.get(KmsInvokeConstants.ID) == null
					|| StringUtils.isBlank(resultObj.get(KmsInvokeConstants.ID).getAsString())) {
				return HelperMethods.returnResultJSonObject(false, null,
						resultObj.get(KmsInvokeConstants.MESSAGE).getAsString(), KmsInvokeConstants.PUSHKMSERRORCODE);
			}
			return HelperMethods.returnResultJSonObject(true, resultObj.get(KmsInvokeConstants.ID).getAsString(), null, null);

		} catch (Exception e) {
			alert.prepareError("Exception occured in submitting push notification", e).log();
		}
		return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_PUSHEXCEPTION);
	}

}

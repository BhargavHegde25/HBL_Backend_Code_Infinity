package com.kony.kmsinvoke.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.kmsinvoke.businessdelegate.api.SendSmsBusinessDelegate;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.KmsInvokeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class SendSmsBusinessDelegateImpl implements SendSmsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	//@Override
	public JsonObject sendSms_old(JsonObject inputparamsObj) {
		JsonObject resultObj = new JsonObject();
		if (!HelperMethods.isValidInputParams(inputparamsObj,KmsInvokeConstants.SMSSERVICEREQUEST))
			return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_SMSEXCEPTION);
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
			String url = HelperMethods.getConfigProperty(KmsInvokeConstants.DBX_KMS_SMS);
			if (url == null) {
				return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_SMSAPIURL);

			}
			url = url + "?checkUserExistence=false";
			Map<String, Object> inputparams = new HashMap<>();
			inputparams.put(KmsInvokeConstants.SMSSERVICEREQUEST,
					inputparamsObj.get(KmsInvokeConstants.SMSSERVICEREQUEST).getAsJsonObject());
			resultObj = HelperMethods.callhttpApi(inputparams, headerparams, url);
			if (!resultObj.has(KmsInvokeConstants.ID) || resultObj.get(KmsInvokeConstants.ID) == null
					|| StringUtils.isBlank(resultObj.get(KmsInvokeConstants.ID).getAsString())) {
				return HelperMethods.returnResultJSonObject(false, null,
						resultObj.get(KmsInvokeConstants.MESSAGE).getAsString(), KmsInvokeConstants.SMSKMSERRORCODE);
			}
			return HelperMethods.returnResultJSonObject(true, resultObj.get(KmsInvokeConstants.ID).getAsString(), null, null);

		} catch (Exception e) {
			alert.prepareError("Exception occured in submitting sms", e).log();
		}
		return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_SMSEXCEPTION);
	}
	@Override
	public JsonObject sendSms(JsonObject inputparamsObj) {
		Result result = new Result();
		try {
			JsonObject smsServiceRequest = inputparamsObj.get(KmsInvokeConstants.SMSSERVICEREQUEST).getAsJsonObject();
			String phonenumber = null;
			JsonObject messagesObj = smsServiceRequest.has("messages")?smsServiceRequest.getAsJsonObject("messages"):new JsonObject();
			JsonObject messageObj = messagesObj.has("message")?messagesObj.getAsJsonObject("message"):new JsonObject();
			JsonObject recipients = messageObj.has("recipients")?messageObj.getAsJsonObject("recipients"):new JsonObject();
			JsonArray recipient = recipients.has("recipient")?recipients.getAsJsonArray("recipient"):new JsonArray();
			if(recipient.size()>0) {
				JsonObject mobileRecord = recipient.get(0).getAsJsonObject();
				if(mobileRecord.has("mobile")) {
					phonenumber=mobileRecord.get("mobile").getAsString();
					alert.prepareError("before sendSms.phonenumber:"+ phonenumber).log();
					 String[] array = phonenumber.split("-");
				        phonenumber = array.length > 1 && StringUtils.isNotBlank(array[1]) ? array[1] : phonenumber;
				        phonenumber=phonenumber.replaceAll("[^a-zA-Z0-9]", "");
				        if(phonenumber.length()>10) {
				        	phonenumber=phonenumber.substring(phonenumber.length() -10);
				        }
				}
			}
			String body = messageObj.has("content")?messageObj.get("content").toString():"";
			alert.prepareError("sendSms.phonenumber:"+ phonenumber).log();
			alert.prepareError("sendSms.body:"+ body).log();
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("contact", phonenumber);
			inputParams.put("body", body);
			String smsResponse = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams)
					.withRequestHeaders(headerParams).withDataControllerRequest(null).build().getResponse();
			result = JSONToResult.convert(smsResponse);
			return HelperMethods.returnResultJSonObject(true, "200", null, null);
		} catch (Exception e) {
			alert.prepareError("Error occured while sending SMS."+ e).log();
		}
		return HelperMethods.returnResultJSonObject(false, KmsInvokeEnum.ERROR_SMSEXCEPTION);
	}

}

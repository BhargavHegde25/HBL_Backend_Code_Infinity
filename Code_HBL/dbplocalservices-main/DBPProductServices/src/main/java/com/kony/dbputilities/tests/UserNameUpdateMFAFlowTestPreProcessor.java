package com.kony.dbputilities.tests;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.preprocessors.UserNameUpdateMFAPreProcessor;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class UserNameUpdateMFAFlowTestPreProcessor implements ObjectServicePreProcessor {

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
			FabricRequestChain requestChain) throws Exception {

		new UserNameUpdateMFAPreProcessor().execute(requestManager, responseManager, requestChain);

		JsonObject responseFromTransfer = responseManager.getPayloadHandler().getPayloadAsJson()
				.getAsJsonObject();

		if (responseFromTransfer.has(ErrorCodeEnum.ERROR_MESSAGE_KEY)
				|| responseFromTransfer.has(ErrorCodeEnum.ERROR_CODE_KEY)
				|| !responseFromTransfer.has(MFAConstants.MFA_ATTRIBUTES)) {
			return;
		}

		JsonObject mfaAttributes = responseFromTransfer.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

		if (mfaAttributes.get("MFAType").getAsString().equals(MFAConstants.SECURE_ACCESS_CODE)) {
			handleOTP(mfaAttributes, requestManager, responseManager, requestChain, responseFromTransfer);
			return;
		}

		JsonArray array = mfaAttributes.get("securityQuestions").getAsJsonArray();

		for ( JsonElement element : array) {

			JsonObject question = element.getAsJsonObject();

			Map<String, String> map = new HashMap<>();

			map.put("Customer_id", HelperMethods.getCustomerIdFromSession(requestManager));
			map.put("SecurityQuestion_id", question.get("SecurityQuestion_id").getAsString());
			map.put("CustomerAnswer",
					BCrypt.hashpw("test", BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS)));

			HelperMethods.callApi(requestManager, map, HelperMethods.getHeaders(requestManager),
					URLConstants.CUSTOMER_SECURITYQUESTIONS_UPDATE);

		}

		JsonObject payloadforverifySecurityQuestions = getSecurityQuestionsRequestPayload(responseFromTransfer,
				requestManager);

		requestManager.getPayloadHandler().updatePayloadAsJson(payloadforverifySecurityQuestions);

		new UserNameUpdateMFAPreProcessor().execute(requestManager, responseManager, requestChain);

	}

	private JsonObject getSecurityQuestionsRequestPayload(JsonObject responseFromTransfer,
			FabricRequestManager requestManager) {
		// TODO Auto-generated method stub

		JsonObject mfaAttributes = responseFromTransfer.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

		JsonObject mfaAttr = new JsonObject();

		if (mfaAttributes.has(MFAConstants.SERVICE_KEY)) {
			mfaAttr.addProperty(MFAConstants.SERVICE_KEY, mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString());
		}

		JsonArray jsonArray = new JsonArray();
		JsonArray array = mfaAttributes.get("securityQuestions").getAsJsonArray();

		for ( JsonElement element : array) {
			JsonObject question = element.getAsJsonObject();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("customerAnswer", "test");
			jsonObject.addProperty("questionId", question.get("SecurityQuestion_id").getAsString());
			jsonArray.add(jsonObject);
		}
		mfaAttr.add(MFAConstants.SECURITY_QUESTION, jsonArray);

		JsonObject requestForVerifySecurityQuestions = new JsonObject();
		requestForVerifySecurityQuestions.add(MFAConstants.MFA_ATTRIBUTES, mfaAttr);
		return requestForVerifySecurityQuestions;
	}

	private void handleOTP(JsonObject mfaAttributes, FabricRequestManager requestManager,
			FabricResponseManager responseManager, FabricRequestChain requestChain, JsonObject responseFromTransfer) throws Exception {
		// TODO Auto-generated method stub

		JsonObject requestPayload = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		JsonObject payloadforRequestOTP = null;
		JsonObject payloadforverifyOTP = null;

		if (mfaAttributes.has(MFAConstants.SECURITY_KEY)) {

			payloadforverifyOTP = getverifyOTPPayload(responseFromTransfer, requestPayload, payloadforverifyOTP,
					requestManager);

		} else {

			payloadforRequestOTP = getRequestOTPPayload(responseFromTransfer, requestPayload, payloadforRequestOTP);

			requestManager.getPayloadHandler().updatePayloadAsJson(payloadforRequestOTP);

			new UserNameUpdateMFAPreProcessor().execute(requestManager, responseManager, requestChain);

			responseFromTransfer = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

			if (responseFromTransfer.has(ErrorCodeEnum.ERROR_MESSAGE_KEY)
					|| responseFromTransfer.has(ErrorCodeEnum.ERROR_CODE_KEY)
					|| !responseFromTransfer.has(MFAConstants.MFA_ATTRIBUTES)) {
				return;
			}

			payloadforverifyOTP = getverifyOTPPayload(responseFromTransfer, payloadforRequestOTP, payloadforverifyOTP,
					requestManager);

		}

		requestManager.getPayloadHandler().updatePayloadAsJson(payloadforverifyOTP);

		new UserNameUpdateMFAPreProcessor().execute(requestManager, responseManager, requestChain);
	}

	private JsonObject getverifyOTPPayload(JsonObject responseFromTransfer, JsonObject requestPayload,
			JsonObject payloadforverifyOTP, FabricRequestManager requestManager) {
		// TODO Auto-generated method stub
		String securityKey = null;

		if (payloadforverifyOTP == null) {
			payloadforverifyOTP = new JsonObject();

			JsonObject mfaAttributes = responseFromTransfer.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

			JsonObject mfaAttr = new JsonObject();

			if (mfaAttributes.has(MFAConstants.SERVICE_KEY)) {
				mfaAttr.addProperty(MFAConstants.SERVICE_KEY,
						mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString());
			} else {
				mfaAttr.addProperty(MFAConstants.SERVICE_KEY, requestPayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject().get(MFAConstants.SERVICE_KEY).getAsString());
			}

			JsonObject otp = new JsonObject();

			securityKey = mfaAttributes.get(MFAConstants.SECURITY_KEY).getAsString();

			otp.addProperty(MFAConstants.SECURITY_KEY, securityKey);

			mfaAttr.add(MFAConstants.OTP, otp);

			payloadforverifyOTP.add(MFAConstants.MFA_ATTRIBUTES, mfaAttr);
		}

		Result result = HelperMethods.callGetApi(requestManager,
				MFAConstants.SECURITY_KEY + DBPUtilitiesConstants.EQUAL + securityKey,
				HelperMethods.getHeaders(requestManager), URLConstants.OTP_GET);

		if (HelperMethods.hasRecords(result)) {
			payloadforverifyOTP.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject().get(MFAConstants.OTP).getAsJsonObject().addProperty("otp", HelperMethods.getFieldValue(result, "Otp"));

		}

		return payloadforverifyOTP;
	}

	private JsonObject getRequestOTPPayload(JsonObject reponseFromTransfer, JsonObject requestPayload,
			JsonObject payloadforRequestOTP) {

		if (payloadforRequestOTP == null) {

			payloadforRequestOTP = new JsonObject();

			JsonObject mfaAttributes = reponseFromTransfer.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

			JsonObject mfaAttr = new JsonObject();

			mfaAttr.addProperty(MFAConstants.SERVICE_KEY, mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString());

			JsonObject phone = mfaAttributes.get(MFAConstants.CUSTOMER_COMMUNICATION).getAsJsonObject()
					.get(MFAConstants.PHONE).getAsJsonArray().get(0).getAsJsonObject();

			JsonObject otp = new JsonObject();
			otp.addProperty(MFAConstants.PHONE, phone.get(MFAConstants.UNMASKED).getAsString());
			JsonObject email = mfaAttributes.get(MFAConstants.CUSTOMER_COMMUNICATION).getAsJsonObject().get(MFAConstants.EMAIL).getAsJsonArray().get(0).getAsJsonObject();

			otp.addProperty(MFAConstants.EMAIL, email.get(MFAConstants.UNMASKED).getAsString());

			mfaAttr.add(MFAConstants.OTP, otp);

			payloadforRequestOTP.add(MFAConstants.MFA_ATTRIBUTES, mfaAttr);
		}

		if (!payloadforRequestOTP.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject().has(MFAConstants.SECURITY_KEY)) {
			JsonObject mfaAttributes = reponseFromTransfer.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

			if (mfaAttributes.has(MFAConstants.SECURITY_KEY)) {
				String securityKey = mfaAttributes.get(MFAConstants.SECURITY_KEY).getAsString();

				payloadforRequestOTP.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject().get(MFAConstants.OTP).getAsJsonObject().addProperty(MFAConstants.SECURITY_KEY, securityKey);
			}
		}

		return payloadforRequestOTP;
	}

}

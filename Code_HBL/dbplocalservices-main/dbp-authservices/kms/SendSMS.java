package com.kony.dbputilities.kms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendSMS implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		try {
			if (!KMSUtil.isSMSRequestAllowed(dcRequest)) {
				return ErrorCodeEnum.ERR_10147.setErrorCode(result);
			}

			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

			JSONObject payload = KMSUtil.constructSMSPayload(inputParams, dcRequest);

			Map<String, Object> input = new HashMap<>();
			input.put("inputparams", payload);

			JSONObject logparams = new JSONObject();
			logparams.put("eventType", "OTP");
			logparams.put("eventSubtype", "OTP");
			logparams.put("isAlertsEngine", "OTP");
			input.put("logparams", logparams);

			JsonObject response = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.SEND_SMS_SERVICE);

			String response_id = JSONUtil.hasKey(response, "referenceId") ? JSONUtil.getString(response, "referenceId")
					: "";
			String response_message = JSONUtil.hasKey(response, "dbperrcode")
					? JSONUtil.getString(response, "dbperrcode")
					: "";

			result.addParam(new Param("requestId", response_id));
			result.addParam(new Param("SMS_Message", response_message));

			if (StringUtils.isBlank(response_id)) {
				result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, "Sms not sent.", MWConstants.STRING));
			} else {
				HelperMethods.setSuccessMsgwithCode("Sms sent successfully.", ErrorCodes.RECORD_FOUND, result);
				KMSUtil.insertAuditRecord(dcRequest);
			}
		} catch (Exception e) {
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, "Sms not sent.", MWConstants.STRING));
			alert.prepareError("Error while sending sms:", e).log();
		}
		return result;
	}
}
package com.kony.dbputilities.kms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendEmail implements JavaService2 {

	public boolean sendKMSAdHocEmail(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		JsonObject payload = KMSUtil.constructKMSEmailJSONObject(dcRequest);

		Map<String, Object> input = new HashMap<>();
		input.put("inputparams", payload);

		JSONObject logparams = new JSONObject();
		logparams.put("eventType", "OTP");
		logparams.put("eventSubtype", "OTP");
		logparams.put("isAlertsEngine", "OTP");
		input.put("logparams", logparams);

		JsonObject response = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.SEND_EMAIL_SERVICE);

		boolean status = ((JSONUtil.hasKey(response, "dbperrcode")
				&& StringUtils.isNotBlank(JSONUtil.getString(response, "dbperrcode")))
				|| (JSONUtil.hasKey(response, "dbperrmsg")
						&& StringUtils.isNotBlank(JSONUtil.getString(response, "dbperrmsg")))) ? false : true;
		if (status) {
			result.addParam(new Param("requestId", JSONUtil.getString(response, "referenceId")));
		} else {
			ErrorCodeEnum.ERR_10063.setErrorCode(result, JSONUtil.getString(response, "dbperrmsg"));
		}
		result.addParam(new Param("KMSemailMsg", JSONUtil.getString(response, "dbperrmsg"), MWConstants.STRING));
		result.addParam(new Param("KMSemailStatus", Boolean.toString(status), MWConstants.STRING));
		return status;
	}

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		if (!KMSUtil.isEmailRequestAllowed(dcRequest)) {
			return ErrorCodeEnum.ERR_10148.setErrorCode(new Result());
		}

		Result result = new Result();

		if (sendKMSAdHocEmail(dcRequest, result)) {
			KMSUtil.insertAuditRecord(dcRequest);
		}

		return result;
	}
}
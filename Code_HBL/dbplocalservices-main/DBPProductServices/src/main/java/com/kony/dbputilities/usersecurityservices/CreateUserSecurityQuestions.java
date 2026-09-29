package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateUserSecurityQuestions implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		processHelper(dcRequest, inputParams);
		Param success = new Param("success", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(success);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void processHelper(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
		String userid = (String) inputParams.get("userId");
		if (!StringUtils.isNotBlank(userid)) {
			userid = HelperMethods.getUserIdFromSession(dcRequest);
		}

		String productLi = (String) inputParams.get(DBPUtilitiesConstants.USR_SECURITY_LIST);
		JsonArray jArray = getJsonArray(productLi);
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				String questionId = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_QUES_ID).getAsString();
				String ans = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_ANS).getAsString();
				insertUserSecurityQuestions(dcRequest, userid, questionId, ans);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertUserSecurityQuestions(DataControllerRequest dcRequest, String userid, String questionId,
			String ans) throws HttpCallException {
		Map inputParams = new HashMap();
		inputParams.put(DBPUtilitiesConstants.UA_USR_ID, userid);
		inputParams.put(DBPUtilitiesConstants.QUESTION, questionId);
		inputParams.put(DBPUtilitiesConstants.ANSWER, ans);
		/*
		 * httpConn.invokeHttpPost(
		 * URLFinder.getCompleteUrl(URLConstants.USER_SECURITY_CREATE), inputParams,
		 * HelperMethods.getHeaders(dcRequest));
		 */
		HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_SECURITY_CREATE);
	}

	private JsonArray getJsonArray(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		JsonArray jObject = (JsonArray) jsonParser.parse(jsonString);
		return jObject;
	}
}
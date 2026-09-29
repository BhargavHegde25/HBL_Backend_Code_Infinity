package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUserSecurityQuestions implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		processHelper(dcRequest, inputParams);
		Param success = new Param("success", "Security questions successfully updated",
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(success);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void processHelper(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
		String userid = HelperMethods.getUserIdFromSession(dcRequest);
		String productLi = (String) inputParams.get(DBPUtilitiesConstants.USR_SECURITY_LIST);
		deleteExistingRecords(dcRequest, userid);
		insertNewQuestions(dcRequest, userid, productLi);
	}

	private void insertNewQuestions(DataControllerRequest dcRequest, String userid, String productLi)
			throws HttpCallException {
		JsonArray jArray = getJsonArray(productLi);
		if (null != jArray) {
			for (int i = 0; i < jArray.size(); i++) {
				String questionId = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_QUES_ID).getAsString();
				String ans = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_ANS).getAsString();
				insertUserSecurityQuestions(dcRequest, userid, questionId, ans);
			}
		}
	}

	private void deleteExistingRecords(DataControllerRequest dcRequest, String userid) throws HttpCallException {
		String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userid;
		Result userQuestions = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_SECURITY_GET);
		if (HelperMethods.hasRecords(userQuestions)) {
			List<Record> questionsList = userQuestions.getAllDatasets().get(0).getAllRecords();
			Map<String, String> input = new HashMap<>();
			for (Record question : questionsList) {
				input.put("id", HelperMethods.getFieldValue(question, "id"));
				HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.USER_SECURITY_DELETE);
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
		HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_SECURITY_CREATE);
	}

	private JsonArray getJsonArray(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jObject = jsonParser.parse(jsonString);
		return jObject.isJsonArray() ? (JsonArray) jObject : null;
	}
}
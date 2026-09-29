package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyUserSecurityQuestions implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_SECURITY_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			result = postProcess(inputParams, result);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private Result postProcess(Map inputParams, Result result) {
		Map<String, String> quesAnsmap = getQuesAnsMap(result);
		String quesAnsLi = (String) inputParams.get(DBPUtilitiesConstants.USR_SECURITY_LIST);
		JsonArray jArray = getJsonArray(quesAnsLi);
		if (verifyQuestionAnswer(jArray, quesAnsmap)) {
			HelperMethods.setValidationMsg("Invalid Answers", null, result);
		}
		return result;
	}

	private Map<String, String> getQuesAnsMap(Result result) {
		Map<String, String> map = new HashMap<>();
		List<Record> quesAnss = result.getAllDatasets().get(0).getAllRecords();
		for (Record ans : quesAnss) {
			map.put(HelperMethods.getFieldValue(ans, "question"), HelperMethods.getFieldValue(ans, "answer"));
		}
		return map;
	}

	private boolean verifyQuestionAnswer(JsonArray jArray, Map<String, String> quesAnsmap) {
		if (null != jArray) {
			for (int i = 0; i < jArray.size(); i++) {
				String questionId = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_QUES_ID).getAsString();
				String ans = ((JsonObject) jArray.get(i)).get(DBPUtilitiesConstants.JSON_ANS).getAsString();
				String correctAns = quesAnsmap.get(questionId);
				if (!ans.equals(correctAns)) {
					return false;
				}
			}
		}
		return true;
	}

	private JsonArray getJsonArray(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jObject = jsonParser.parse(jsonString);
		return jObject.isJsonArray() ? (JsonArray) jObject : null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String userId = getUserId(inputParams, dcRequest);
		String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}

	@SuppressWarnings("rawtypes")
	private String getUserId(Map inputParams, DataControllerRequest dcRequest) throws HttpCallException {
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String userName = (String) inputParams.get("userName");
		if (StringUtils.isBlank(userId)) {
			userId = getUserId(dcRequest, userName);
		}
		return userId;
	}

	private String getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
		return HelperMethods.getFieldValue(user, "id");
	}
}
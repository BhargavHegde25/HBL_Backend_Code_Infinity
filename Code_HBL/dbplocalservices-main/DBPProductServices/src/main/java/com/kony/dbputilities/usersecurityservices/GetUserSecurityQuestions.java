package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserSecurityQuestions implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.SECURITY_QUS_GET);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String userId = getUserId(inputParams, dcRequest);
		if (StringUtils.isNotBlank(userId)) {
			inputParams.put(DBPUtilitiesConstants.FILTER, getQuestionFilter(dcRequest, userId));
		} else {
			status = false;
			HelperMethods.setValidationMsg("Invalid username", dcRequest, result);
		}
		return status;
	}

	private Object getQuestionFilter(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		Result questionIds = getQuestionIds(dcRequest, userId);
		StringBuilder filter = new StringBuilder();
		if (HelperMethods.hasRecords(questionIds)) {
			List<Record> questions = questionIds.getAllDatasets().get(0).getAllRecords();
			for (Record question : questions) {
				filter.append("id").append(DBPUtilitiesConstants.EQUAL)
						.append(HelperMethods.getFieldValue(question, "question"));
				filter.append(DBPUtilitiesConstants.OR);
			}
			filter.append("id").append(DBPUtilitiesConstants.AND).append("-1");
		}
		return filter.toString();
	}

	private Result getQuestionIds(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
		input.put(DBPUtilitiesConstants.FILTER, filter);
		input.put(DBPUtilitiesConstants.TOP, "2");
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_SECURITY_GET);
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
package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EditCustomerName implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String userName = inputParams.get("userName");
		String newUserName = inputParams.get("newUserName");
		return updateUserName(dcRequest, userName, newUserName);
	}

	private Result updateUserName(DataControllerRequest dcRequest, String userName, String newUserName)
			throws HttpCallException {
		String userId = getUserId(dcRequest, userName);
		Map<String, String> input = new HashMap<>();
		input.put("id", userId);
		input.put("UserName", newUserName);
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);
	}

	private String getUserId(DataControllerRequest dcRequest, String username) throws HttpCallException {
		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + username;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
		if (HelperMethods.hasRecords(result)) {
			return HelperMethods.getFieldValue(result, "id");
		}
		return null;
	}

	public boolean updateUserNameAtAdminConsole(String username, String newUserName, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("userName", username);
		input.put("newUserName", newUserName);
		JsonObject response = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CUSTOMER_EDIT_BASIC_INO,
				dcRequest);
		return "0".equals(response.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString());
	}

}
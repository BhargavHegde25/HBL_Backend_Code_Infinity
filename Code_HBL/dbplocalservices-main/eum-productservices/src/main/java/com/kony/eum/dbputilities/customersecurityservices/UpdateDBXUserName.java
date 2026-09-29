package com.kony.eum.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.UpdateDBXUserName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateDBXUserName implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String olduserName = inputParams.get("oldUserName");
		String newUserName = inputParams.get("newUserName");
		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (StringUtils.isBlank(newUserName) || StringUtils.isBlank(olduserName) || StringUtils.isBlank(userId)) {
			return postProcess(dcRequest, result);
		}
		Map<String, String> map = new HashMap<>();
		map = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String username = map.get("UserName");

		if (!username.equalsIgnoreCase(olduserName)) {
			ErrorCodeEnum.ERR_10149.setErrorCode(result);
			return result;
		}

		boolean check = checkUserName(olduserName, dcRequest, userId);
		if (check) {
			result = updateUserName(dcRequest, userId, newUserName);
			updateUserNameAtAdminConsole(olduserName, newUserName, dcRequest);
		}

		return postProcess(dcRequest, result);
	}

	private Object postProcess(DataControllerRequest dcRequest, Result result) {

		Result retResult = new Result();

		if (HelperMethods.hasRecords(result)) {
			retResult.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
			return retResult;
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_10143.setErrorCode(retResult, HelperMethods.getError(result));
			return retResult;
		} else {
			ErrorCodeEnum.ERR_10142.setErrorCode(retResult);
			return retResult;
		}

	}

	private boolean checkUserName(String olduserName, DataControllerRequest dcRequest, String userId) {

		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + olduserName;
		Result result = new Result();

		try {
			result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		if (HelperMethods.hasRecords(result)) {
			return true;
		}

		return false;

	}

	private Result updateUserName(DataControllerRequest dcRequest, String userName, String newUserName)
			throws HttpCallException {

		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		Map<String, String> input = new HashMap<>();
		input.put("id", userId);
		input.put("UserName", newUserName);
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);

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
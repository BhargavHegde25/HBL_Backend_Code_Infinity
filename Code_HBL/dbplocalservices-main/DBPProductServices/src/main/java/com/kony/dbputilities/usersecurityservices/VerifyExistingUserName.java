package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyExistingUserName implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = null;
		Result newuser = new Result();
		Result user = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			String username = inputParams.get(DBPUtilitiesConstants.USER_NAME);
			newuser = HelperMethods.callGetApi(dcRequest, "userName" + DBPUtilitiesConstants.EQUAL + username,
					HelperMethods.getHeaders(dcRequest), URLConstants.NEW_USER_GET);
			user = HelperMethods.callGetApi(dcRequest, "UserName" + DBPUtilitiesConstants.EQUAL + username,
					HelperMethods.getHeaders(dcRequest), URLConstants.USER_GET);
		}
		result = postProcess(newuser, user);

		return result;
	}

	private Result postProcess(Result newuser, Result user) {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(newuser) || HelperMethods.hasRecords(user)) {
			HelperMethods.setValidationMsg("User Name already exists", null, retVal);
		} else {
			Param p = new Param("success", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		if (StringUtils.isBlank(inputParams.get(DBPUtilitiesConstants.USER_NAME))) {
			HelperMethods.setValidationMsg("Please provide username.", dcRequest, result);
			return false;
		}
		return true;
	}
}

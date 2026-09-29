package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class VerifyNewUserSecurityQuestions implements JavaService2 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		CreateUserPersonalInfo insertPersonalInfo = new CreateUserPersonalInfo();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		inputParams.put(DBPInputConstants.INFO_TYPE, "SecurityQuestions");
		Object result = insertPersonalInfo.invoke(methodID, inputArray, dcRequest, dcResponse);
		return result;
	}
}
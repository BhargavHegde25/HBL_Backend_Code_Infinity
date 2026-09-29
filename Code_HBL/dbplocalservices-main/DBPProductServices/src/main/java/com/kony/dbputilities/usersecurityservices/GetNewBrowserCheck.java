package com.kony.dbputilities.usersecurityservices;

import java.security.SecureRandom;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetNewBrowserCheck implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		process(result);
		return result;
	}

	private void process(Result result) {
		SecureRandom rand = new SecureRandom();
		Param response = new Param(DBPUtilitiesConstants.RESULT_MSG, "No New Browser detected",
				DBPUtilitiesConstants.STRING_TYPE);
		if (0 == rand.nextInt(5) % 2) {
			response.setValue("New Browser detected");
		}
		result.addParam(response);
	}
}

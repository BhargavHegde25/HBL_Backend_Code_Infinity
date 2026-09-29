package com.kony.dbputilities.usersecurityservices;

import java.security.SecureRandom;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RequestOTP implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		SecureRandom rand = new SecureRandom();
		int n = (int) (100000 + (rand.nextFloat() * 900000));
		Param success = new Param(DBPUtilitiesConstants.OTP, String.valueOf(n),
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(success);
		return result;
	}
}
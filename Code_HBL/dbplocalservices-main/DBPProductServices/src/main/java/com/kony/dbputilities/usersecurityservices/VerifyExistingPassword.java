package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyExistingPassword implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		result = postProcess(inputParams, result);
		return result;
	}

	private Result postProcess(Map<String, String> inputParams, Result result) {
		Result retVal = new Result();
		Boolean status = false;
		if (HelperMethods.hasRecords(result)) {
			String hashedPassword = HelperMethods.getFieldValue(result, "Password");
			status = BCrypt.checkpw(inputParams.get(DBPUtilitiesConstants.PWD_FIELD), hashedPassword);
		}

		if (status) {
			Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "The user is verified",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		} else {
			Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "Invalid Credentials",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String id = HelperMethods.getUserIdFromSession(dcRequest);

		if (StringUtils.isBlank((String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD))) {
			HelperMethods.setValidationMsg("Please provide password.", dcRequest, result);
			status = false;
		}

		if (status) {
			StringBuilder sb = new StringBuilder();
			sb.append("id").append(DBPUtilitiesConstants.EQUAL).append(id);

			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		return status;
	}
}

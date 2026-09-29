package com.kony.dbputilities.customersecurityservices;

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

public class VerifyDbxExistingPassword implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		result = postProcess(result);
		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
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
		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_NEW_FIELD);
		if (!StringUtils.isNotBlank(password)) {
			HelperMethods.setValidationMsg("Please provide password.", dcRequest, result);
			status = false;
		}
		if (status) {
			StringBuilder sb = new StringBuilder();
			sb.append(DBPUtilitiesConstants.C_ID).append(DBPUtilitiesConstants.EQUAL).append(id)
					.append(DBPUtilitiesConstants.AND).append("Password").append(DBPUtilitiesConstants.EQUAL)
					.append(password);

			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		return status;
	}
}
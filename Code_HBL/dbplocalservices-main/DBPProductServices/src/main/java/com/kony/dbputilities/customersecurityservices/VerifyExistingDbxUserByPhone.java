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

public class VerifyExistingDbxUserByPhone implements JavaService2 {

	@Override
	@SuppressWarnings("rawtypes")
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.NEW_USER_GET);
		}
		result = postProcess(result);

		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setValidationMsg("User already exists", null, retVal);
		} else {
			Param p = new Param("success", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String phoneNumber = (String) inputParams.get(DBPUtilitiesConstants.PHONE_NUMBER);

		if (!StringUtils.isNotBlank(phoneNumber)) {
			HelperMethods.setValidationMsg("Please provide " + DBPUtilitiesConstants.PHONE_NUMBER, dcRequest, result);
			status = false;
		}

		if (status) {
			String filterQuery = DBPUtilitiesConstants.PHONE_NUMBER + DBPUtilitiesConstants.EQUAL + phoneNumber;
			inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
		}
		return status;
	}
}
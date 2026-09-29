package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CheckP2PEligibilityForCustomer implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
		}
		result = postProcess(result);
		return result;
	}

	private Result postProcess(Result result) {
		Result response = new Result();
		String message = "Not Eligible";
		if (HelperMethods.hasRecords(result)) {
			String isP2PSupported = HelperMethods.getFieldValue(result, "isP2PSupported");
			String isP2PActivated = HelperMethods.getFieldValue(result, "isP2PActivated");
			if ("true".equalsIgnoreCase(isP2PSupported)) {
				if ("true".equalsIgnoreCase(isP2PActivated)) {
					message = "Activated";
				} else {
					message = "Not Activated";
				}
			}
		}
		response.addParam(new Param("result", message, DBPUtilitiesConstants.STRING_TYPE));
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		String filter = "id" + DBPUtilitiesConstants.EQUAL + userId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}
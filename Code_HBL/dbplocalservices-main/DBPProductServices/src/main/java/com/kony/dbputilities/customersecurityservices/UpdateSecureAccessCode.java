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

public class UpdateSecureAccessCode implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Result retResult = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_UPDATE);
			if (HelperMethods.hasRecords(result)) {
				retResult.addParam(new Param("result", "Updated Successfully", DBPUtilitiesConstants.STRING_TYPE));
			}
		}
		return retResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		inputParams.put("id", userId);
		inputParams.put("IsPhoneEnabled", inputParams.get("isPhoneEnabled"));
		inputParams.put("IsEmailEnabled", inputParams.get("isEmailEnabled"));
		HelperMethods.removeNullValues(inputParams);
		return true;
	}
}
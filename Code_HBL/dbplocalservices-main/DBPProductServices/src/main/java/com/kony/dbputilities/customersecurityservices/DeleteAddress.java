package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DeleteAddress implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			deleteCustomerAddress(dcRequest, inputParams);
			result = deleteAddress(dcRequest, inputParams);
		}
		return result;
	}

	private Result deleteAddress(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		inputParams.put("id", inputParams.get("addressId"));
		return HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.ADDRESS_DELETE);
	}

	private void deleteCustomerAddress(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		Map<String, String> input = new HashMap<>();
		input.put("Customer_id", customerId);
		input.put("Address_id", inputParams.get("addressId"));
		HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_DELETE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		if (StringUtils.isBlank(inputParams.get("addressId"))) {
			HelperMethods.setSuccessMsgwithCode("addressid is blank.", "7003", result);
			return false;
		}
		return true;
	}
}

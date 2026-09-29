package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdatePreferredBillPayAccount implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		
		if (preProcess(inputParams, customerId, dcRequest, result)) {
			if(inputParams.containsKey("id") && !inputParams.get("id").isEmpty()) {		
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCE_UPDATE);
			} else {
				inputParams.put("id", String.valueOf(HelperMethods.getNumericId()));
				inputParams.put("Customer_id",customerId);
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMERPREFERENCE_CREATE);
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, String customerId, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String preferenceId = getCustomerPrefId(customerId, dcRequest);
		inputParams.put("DefaultAccountBillPay",dcRequest.getParameter("default_account_billPay")	);
		inputParams.put("UserName", null);
		inputParams.put("id", preferenceId);
		HelperMethods.removeNullValues(inputParams);
		return true;
	}

	private String getCustomerPrefId(String customerId, DataControllerRequest dcRequest) throws HttpCallException {
		Result result = HelperMethods.callGetApi(dcRequest, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId,
				HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERPREFERENCE_GET);
		return HelperMethods.getFieldValue(result, "id");
	}
}
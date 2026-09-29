package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetBackendidentifierDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.BACKENDIDENTIFIER_GET);
		}
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (StringUtils.isNotBlank(id)) {
			id = inputParams.get(DBPUtilitiesConstants.IDENTIFIER_TYPE);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(DBPUtilitiesConstants.IDENTIFIER_TYPE).append(DBPUtilitiesConstants.EQUAL).append(id);

		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return true;
	}
}
package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyPin implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = null;
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		result = postProcess(result);
		return result;
	}

	private Result postProcess(Result result) {
		Result response = new Result();
		boolean isValidated = HelperMethods.hasRecords(result);
		response.addParam(new Param("result", String.valueOf(isValidated), DBPUtilitiesConstants.STRING_TYPE));
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String pin = (String) inputParams.get("pin");
		if (StringUtils.isNotBlank(userId)) {
			String filter = "id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "pin"
					+ DBPUtilitiesConstants.EQUAL + pin;
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			return true;
		}

		return false;
	}
}

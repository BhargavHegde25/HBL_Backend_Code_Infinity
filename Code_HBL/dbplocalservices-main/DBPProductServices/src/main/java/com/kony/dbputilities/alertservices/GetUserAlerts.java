package com.kony.dbputilities.alertservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserAlerts implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ALERTS_GET);
		}
		if (!HelperMethods.hasError(result)) {
			postProcess(result);
		}

		return result;
	}

	private void postProcess(Result result) {
		Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, DBPUtilitiesConstants.SUCCESS_MSG,
				DBPUtilitiesConstants.STRING_TYPE);
		result.addParam(p);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL
				+ HelperMethods.getUserIdFromSession(dcRequest);
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}

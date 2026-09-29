package com.kony.dbputilities.organisation;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganisationType implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, null, null, URLConstants.ORGANISATION_TYPE_GET);
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {

		if (HelperMethods.hasRecords(result)) {
			result.getAllDatasets().get(0).setId("OgranizationTypes");
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.RECORD_FOUND_IN_DBX, result);
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_11027.setErrorCode(result, HelperMethods.getError(result));
		} else {
			ErrorCodeEnum.ERR_11027.setErrorCode(result);
		}

		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {

		return true;
	}
}

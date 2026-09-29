package com.kony.eum.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateMembershipDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MEMBERSHIP_UPDATE);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();

		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.SUCCESS_MSG, ErrorCodes.RECORD_FOUND, retVal);
		} else {
			HelperMethods.setSuccessMsgwithCode("Record not Present", ErrorCodes.RECORD_NOT_FOUND, retVal);
		}
		return retVal;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		HelperMethods.removeOnlyNullValues(inputParams);

		return status;
	}

}
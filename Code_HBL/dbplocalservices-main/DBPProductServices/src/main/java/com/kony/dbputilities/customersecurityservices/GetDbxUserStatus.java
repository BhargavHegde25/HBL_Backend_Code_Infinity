package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetDbxUserStatus implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		return postProcess(result);
	}

	private Result postProcess(Result result) throws HttpCallException {
		String lockCount = HelperMethods.getFieldValue(result, "lockCount");
		String lockedOn = HelperMethods.getFieldValue(result, "lockedOn");
		String statusId = HelperMethods.getFieldValue(result, "Status_id");
		Result retResult = new Result();
		retResult = errorCodeAssignment(result);
		retResult.addParam(new Param("Status", getStatus(statusId), DBPUtilitiesConstants.STRING_TYPE));
		if (!lockCount.isEmpty() && Integer.parseInt(lockCount) >= 4) {
			retResult.addParam(new Param("lockedOn", lockedOn, DBPUtilitiesConstants.STRING_TYPE));
			retResult.addParam(new Param("Status", "LOCKED", DBPUtilitiesConstants.STRING_TYPE));
		}
		return retResult;
	}

	private String getStatus(String statusId) throws HttpCallException {
		Map<String, String> map = HelperMethods.getCustomerStatus();
		for (String status : map.keySet()) {
			String value = map.get(status);
			if (value.equals(statusId)) {
				return status;
			}
		}
		return null;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		String userName = inputParams.get("UserName");
		StringBuilder sb = new StringBuilder();
		sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(userName);
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return true;
	}

	private Result errorCodeAssignment(Result result) {
		Result retResult = new Result();
		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.USER_EXISTS_IN_DBX, retResult);
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_10026.setErrorCode(retResult, HelperMethods.getError(result));
		} else {
			ErrorCodeEnum.ERR_10027.setErrorCode(retResult);
		}

		return retResult;
	}
}

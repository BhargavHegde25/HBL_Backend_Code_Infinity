package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class LockUnlockDbxUser implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
			result = postProcess(inputParams, dcRequest, result);
		}

		return result;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String res = "Customer not found";
		if (HelperMethods.hasRecords(result)) {
			int lockCount = Integer.parseInt(HelperMethods.getFieldValue(result, "lockCount"));
			if (lockCount > 3) {
				res = "Customer Unlocked";
				updateLockCount(dcRequest, result, 0);
			} else {
				res = "Customer Locked";
				updateLockCount(dcRequest, result, 6);
			}
		}
		Result ret = new Result();
		ret.addParam(new Param("result", res, "String"));
		return ret;
	}

	private void updateLockCount(DataControllerRequest dcRequest, Result user, int lockcount) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("id", HelperMethods.getFieldValue(user, "id"));
		input.put("lockCount", String.valueOf(lockcount));
		input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		StringBuilder filter = new StringBuilder();
		if (StringUtils.isNotBlank(inputParams.get("UserName"))) {
			filter.append("UserName").append(DBPUtilitiesConstants.EQUAL).append("'").append(inputParams.get("UserName")).append("'");
		} else {
			filter.append("id").append(DBPUtilitiesConstants.EQUAL)
					.append(HelperMethods.getCustomerIdFromSession(dcRequest));
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}

}
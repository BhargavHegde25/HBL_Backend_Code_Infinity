package com.kony.dbputilities.usersecurityservices;

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

public class LockUnlockUser implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_GET);
			result = postProcess(inputParams, dcRequest, result);
		}

		return result;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String res = "User not found";
		if (HelperMethods.hasRecords(result)) {
			int lockCount = Integer.parseInt(HelperMethods.getFieldValue(result, "lockCount"));
			if (lockCount > 3) {
				res = "User Unlocked";
				updateLockCount(dcRequest, result, 0);
			} else {
				res = "User Locked";
				updateLockCount(dcRequest, result, 6);
			}
		}
		Result ret = new Result();
		ret.addParam(new Param("result", res, "String"));
		return ret;
	}

	private void updateLockCount(DataControllerRequest dcRequest, Result user, int lockcount) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("Id", HelperMethods.getFieldValue(user, "Id"));
		input.put("lockCount", String.valueOf(lockcount));
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		StringBuilder filter = new StringBuilder();
		if (StringUtils.isNotBlank(inputParams.get("userName"))) {
			filter.append("userName").append(DBPUtilitiesConstants.EQUAL).append(inputParams.get("userName"));
		} else {
			filter.append("Id").append(DBPUtilitiesConstants.EQUAL)
					.append(HelperMethods.getUserIdFromSession(dcRequest));
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}
}

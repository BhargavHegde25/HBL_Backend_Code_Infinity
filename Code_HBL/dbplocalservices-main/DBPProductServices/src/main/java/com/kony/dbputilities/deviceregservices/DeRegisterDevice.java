package com.kony.dbputilities.deviceregservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeRegisterDevice implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.DEVICEREGISTRATION_DELETE);
		}
		if (!HelperMethods.hasError(result)) {
			result.addParam(new Param("status", (String) inputParams.get("Status_id"), "String"));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String deviceId = (String) inputParams.get("deviceId");
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		Result device = getDevice(dcRequest, deviceId, userId);
		inputParams.put("id", HelperMethods.getFieldValue(device, "id"));
		inputParams.put("Status_id", HelperMethods.getFieldValue(device, "Status_id"));
		return true;
	}

	private Result getDevice(DataControllerRequest dcRequest, String deviceId, String userId) throws HttpCallException {
		String filter = "DeviceId" + DBPUtilitiesConstants.EQUAL + deviceId + DBPUtilitiesConstants.AND + "User_id"
				+ DBPUtilitiesConstants.EQUAL + userId;
		return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.DEVICEREGISTRATION_GET);
	}
}
package com.kony.dbputilities.deviceregservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeviceRegistrationStatus implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.DEVICEREGISTRATION_GET);
		}
		result = postProcess(inputParams, result);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private Result postProcess(Map inputParams, Result result) {
		boolean isRegistered = HelperMethods.hasRecords(result);
		String deviceId = (String) inputParams.get("deviceId");
		Result response = new Result();
		response.addParam(new Param("result", String.valueOf(isRegistered), DBPUtilitiesConstants.STRING_TYPE));
		response.addParam(new Param("id", deviceId, DBPUtilitiesConstants.STRING_TYPE));
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String deviceId = (String) inputParams.get("deviceId");
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		StringBuilder filter = new StringBuilder();
		filter.append("DeviceId").append(DBPUtilitiesConstants.EQUAL).append(deviceId);
		filter.append(DBPUtilitiesConstants.AND);
		filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}
}

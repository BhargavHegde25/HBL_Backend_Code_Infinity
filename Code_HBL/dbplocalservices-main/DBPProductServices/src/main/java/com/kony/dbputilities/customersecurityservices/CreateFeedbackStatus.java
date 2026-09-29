package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateFeedbackStatus implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String deviceId = inputParams.get("deviceId");
		String customerId = inputParams.get("Customer_id");
		createFeedbackStatus(dcRequest, customerId, deviceId, result);

		return result;
	}

	private void createFeedbackStatus(DataControllerRequest dcRequest, String customerId, String deviceId,
			Result result) {

		Map<String, String> inputParams = new HashMap<>();

		String filterQuery = "customerID" + DBPUtilitiesConstants.EQUAL + customerId;
		if (StringUtils.isNotBlank(deviceId)) {
			filterQuery = filterQuery + DBPUtilitiesConstants.AND + "deviceID" + DBPUtilitiesConstants.EQUAL + deviceId;
		}
		String url = URLConstants.FEEDBACKSTATUS_CREATE;
		Result feedbackStatus = null;
		try {
			feedbackStatus = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
					URLConstants.FEEDBACKSTATUS_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		String uuid = UUID.randomUUID().toString();
		String createdts = HelperMethods.getCurrentTimeStamp();
		String status = "false";

		inputParams.put("customerID", customerId);
		inputParams.put("feedbackID", uuid);
		inputParams.put("createdts", createdts);
		inputParams.put("status", status);
		inputParams.put("deviceID", deviceId);

		HelperMethods.removeOnlyNullValues(inputParams);
		if (feedbackStatus != null && HelperMethods.hasRecords(feedbackStatus)) {
			url = URLConstants.FEEDBACKSTATUS_UPDATE;
			inputParams.put("id", HelperMethods.getFieldValue(feedbackStatus, "id"));
			try {
				HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
		} else {
			url = URLConstants.FEEDBACKSTATUS_CREATE;
			String id = UUID.randomUUID().toString();
			inputParams.put("id", id);
			try {
				HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
		}

		result.addParam(new Param("feedbackUserId", uuid, "String"));
	}
}
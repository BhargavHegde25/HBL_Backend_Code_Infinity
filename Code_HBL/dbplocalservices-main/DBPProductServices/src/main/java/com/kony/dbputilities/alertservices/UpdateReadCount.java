package com.kony.dbputilities.alertservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateReadCount implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_NOTIFICATION_UPDATE);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String id = (String) inputParams.get("userNotificationId");
		String userid = HelperMethods.getCustomerIdFromSession(dcRequest);
		String filterQuery = "user_id" + DBPUtilitiesConstants.EQUAL + userid + DBPUtilitiesConstants.AND + "id"
				+ DBPUtilitiesConstants.EQUAL + id;
		Result result1 = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_NOTIFICATION_GET);

		if (!HelperMethods.hasRecords(result1)) {
			return false;
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
		inputParams.put("isRead", "1");
		inputParams.put("id", id);
		return true;
	}
}

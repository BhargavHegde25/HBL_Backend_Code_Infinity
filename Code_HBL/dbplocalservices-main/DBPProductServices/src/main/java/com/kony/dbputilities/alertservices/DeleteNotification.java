package com.kony.dbputilities.alertservices;

import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeleteNotification implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_NOTIFICATION_DELETE);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();
		if (!HelperMethods.hasError(retVal)) {
			Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "Notification is deleted",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = false;
		String notificationId = (String) inputParams.get("notificationId");
		if (canBeDeleted(dcRequest, notificationId, result)) {
			inputParams.put(DBPUtilitiesConstants.UN_ID, notificationId);
			status = true;
		}
		return status;
	}

	private boolean canBeDeleted(DataControllerRequest dcRequest, String notificationId, Result result)
			throws HttpCallException {
		boolean status = false;
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String filter = "id" + DBPUtilitiesConstants.EQUAL + notificationId + DBPUtilitiesConstants.AND + "user_id"
				+ DBPUtilitiesConstants.EQUAL + userId;
		Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_NOTIFICATION_GET);

		if (HelperMethods.hasRecords(rs)) {
			if ("0".equalsIgnoreCase(HelperMethods.getFieldValue(rs, "isRead"))) {
				HelperMethods.setValidationMsg("Unread notifications can't be deleted", dcRequest, result);
			}
			if (!userId.equalsIgnoreCase(HelperMethods.getFieldValue(rs, "user_id"))) {
				HelperMethods.setValidationMsg("Notification can't be deleted", dcRequest, result);
			}
			status = true;
		} else {
			HelperMethods.setValidationMsg("Notification doesn't exist", dcRequest, result);
		}
		return status;
	}
}
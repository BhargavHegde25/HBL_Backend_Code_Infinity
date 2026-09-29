package com.kony.dbputilities.messagesservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetMessageCount implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MESSAGE_GET);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Result ret = result;
		if (HelperMethods.hasRecords(result)) {
			Dataset ds = result.getAllDatasets().get(0);
			int size = ds.getAllRecords().size();
			ret = new Result();
			ret.addParam(
					new Param(DBPUtilitiesConstants.COUNT, Integer.toString(size), DBPUtilitiesConstants.STRING_TYPE));
		}
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String messageType = "Inbox";
		String filter = DBPUtilitiesConstants.T_USR_ID + DBPUtilitiesConstants.EQUAL + userId
				+ DBPUtilitiesConstants.AND + DBPUtilitiesConstants.MSG_STATUS + DBPUtilitiesConstants.EQUAL
				+ messageType + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.MSG_READ_STATUS
				+ DBPUtilitiesConstants.EQUAL + "0";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}

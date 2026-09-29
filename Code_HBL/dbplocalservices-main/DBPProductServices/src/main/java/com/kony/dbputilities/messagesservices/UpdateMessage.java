package com.kony.dbputilities.messagesservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateMessage implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MESSAGE_UPDATE);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		Object messageId = inputParams.get(DBPInputConstants.MESSAGE_ID);
		inputParams.put(DBPUtilitiesConstants.UN_ID, messageId);
		inputParams.put(DBPUtilitiesConstants.ACCOUNT_ID, inputParams.get(DBPInputConstants.M_ACCOUNT_ID));
		inputParams.put(DBPUtilitiesConstants.CATEGORY_ID, inputParams.get(DBPInputConstants.CATEGORY_ID));
		inputParams.put(DBPUtilitiesConstants.SUB_CATEGORY_ID, inputParams.get(DBPInputConstants.SUB_CATEGORY_ID));
		inputParams.put(DBPUtilitiesConstants.MSG_STATUS, inputParams.get(DBPInputConstants.MESSAGE_TYPE));
		HelperMethods.removeNullValues(inputParams);
		return true;
	}
}

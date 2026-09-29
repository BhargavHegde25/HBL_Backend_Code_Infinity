package com.kony.dbputilities.messagesservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMessages implements JavaService2 {
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

		return result;
	}

	@SuppressWarnings("rawtypes")
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String messageId = (String) inputParams.get(DBPInputConstants.MESSAGE_ID);
		if (!StringUtils.isNotBlank(messageId) || messageId.contains("$")) {
			getMessageByType(inputParams, dcRequest);
		} else {
			getMessageById(inputParams);
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getMessageById(Map inputParams) {
		String id = (String) inputParams.get(DBPInputConstants.MESSAGE_ID);
		inputParams.put(DBPUtilitiesConstants.UN_ID, id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getMessageByType(Map inputParams, DataControllerRequest dcRequest) {
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String recordNum = (String) inputParams.get(DBPInputConstants.RECORD_NUM);
		String pageSize = (String) inputParams.get(DBPInputConstants.PAGE_SIZE);
		String messageType = (String) inputParams.get(DBPInputConstants.MESSAGE_TYPE);
		String dateType = HelperMethods.getDateType(messageType);
		String isDeleted = "Deleted".equalsIgnoreCase(messageType) ? "1" : "0";
		String filter = DBPUtilitiesConstants.T_USR_ID + DBPUtilitiesConstants.EQUAL + userId
				+ DBPUtilitiesConstants.AND + DBPUtilitiesConstants.MSG_STATUS + DBPUtilitiesConstants.EQUAL
				+ messageType + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.IS_SOFT_DELETED
				+ DBPUtilitiesConstants.EQUAL + isDeleted;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		inputParams.put(DBPUtilitiesConstants.ORDERBY, dateType + " desc");
		if (StringUtils.isNotBlank(pageSize)) {
			inputParams.put(DBPUtilitiesConstants.TOP, pageSize);
		}
		if (StringUtils.isNotBlank(recordNum)) {
			inputParams.put(DBPUtilitiesConstants.SKIP, recordNum);
		}
	}
}

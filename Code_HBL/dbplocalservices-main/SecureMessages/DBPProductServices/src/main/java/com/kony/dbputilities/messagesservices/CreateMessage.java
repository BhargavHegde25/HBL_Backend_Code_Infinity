package com.kony.dbputilities.messagesservices;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class CreateMessage implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MESSAGE_CREATE);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String accountId = (String) inputParams.get(DBPInputConstants.M_ACCOUNT_ID);
		String categoryId = (String) inputParams.get(DBPInputConstants.CATEGORY_ID);
		String subCategoryId = (String) inputParams.get(DBPInputConstants.SUB_CATEGORY_ID);

		if (!StringUtils.isNotBlank(accountId) || !StringUtils.isNotBlank(categoryId)
				|| !StringUtils.isNotBlank(subCategoryId)) {
			HelperMethods.setValidationMsg("Please pass all the mandatory fields", dcRequest, result);
			status = false;
		}

		if (status) {
			updateDate(inputParams);
			inputParams.put(DBPUtilitiesConstants.ACCOUNT_ID, accountId);
			inputParams.put(DBPUtilitiesConstants.CATEGORY_ID, categoryId);
			inputParams.put(DBPUtilitiesConstants.SUB_CATEGORY_ID, subCategoryId);
			inputParams.put(DBPUtilitiesConstants.MSG_STATUS, inputParams.get(DBPInputConstants.MESSAGE_TYPE));
		}

		return status;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateDate(Map inputparams) {
		String messageType = "Inbox";
		String temp = (String) inputparams.get(DBPInputConstants.MESSAGE_TYPE);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String dateString = formatter.format(new Date());

		if (StringUtils.isNotBlank(temp)) {
			messageType = temp;
		}
		inputparams.put(HelperMethods.getDateType(messageType), dateString);
	}
}

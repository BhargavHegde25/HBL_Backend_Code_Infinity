package com.kony.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsByTINandMembership implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MEM_TIN_ACCOUNTS_VIEW_GET);
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {

		if (HelperMethods.hasRecords(result)) {
			result.getAllDatasets().get(0).setId("CustomerAccounts");

			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.ACCOUNTS_EXISTS_IN_DBX, ErrorCodes.RECORD_FOUND,
					result);
		} else if (HelperMethods.hasError(result)) {
			HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result), ErrorCodes.ERROR_SEARCHING_RECORD,
					result);
		} else {
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.ACCOUNTS_NOT_EXISTS_IN_DBX,
					ErrorCodes.RECORD_NOT_FOUND, result);
		}

		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {

		String[] list = { "Membership_id", "Taxid" };

		String filter = "";

		for (int i = 0; i < list.length; i++) {
			String filterKey = list[i];
			String filtervalue = inputParams.get(filterKey);
			if (StringUtils.isNotBlank(filtervalue)) {
				if (!filter.isEmpty()) {
					filter += DBPUtilitiesConstants.AND;
				}

				filter += filterKey + DBPUtilitiesConstants.EQUAL + filtervalue;
			}
		}

		if (StringUtils.isBlank(filter)) {
			HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.INVALID_DETAILS,
					ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, result);
			return false;
		}

		return true;
	}
}

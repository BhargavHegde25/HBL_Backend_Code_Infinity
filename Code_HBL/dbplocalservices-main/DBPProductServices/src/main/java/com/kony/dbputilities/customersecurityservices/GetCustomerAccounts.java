package com.kony.dbputilities.customersecurityservices;

import java.util.List;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerAccounts implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERACCOUNTSVIEW_GET);
			result = postProcess(result, dcRequest);
		}

		return result;
	}

	private Result postProcess(Result result, DataControllerRequest dcRequest) {

		if (HelperMethods.hasRecords(result)) {
			String userName = HelperMethods.getUserFromIdentityService(dcRequest).get("userName");
			Param p = new Param("userName", userName, DBPUtilitiesConstants.STRING_TYPE);
			Dataset ds = result.getAllDatasets().get(0);
			List<Record> recordList = ds.getAllRecords();
			for (Record record : recordList) {
				record.addParam(p);
				String typeId = record.getParam("Type_id").getValue();
				String accountType = HelperMethods.getAccountsNames().get(typeId);
				record.addParam(new Param("Account_Type", accountType, DBPUtilitiesConstants.STRING_TYPE));
			}
			result.getAllDatasets().get(0).setId("Accounts");
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.RECORD_FOUND_IN_DBX, ErrorCodes.RECORD_FOUND,
					result);
		} else if (HelperMethods.hasError(result)) {
			HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result), ErrorCodes.ERROR_SEARCHING_RECORD,
					result);
		} else {
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.RECORD_NOT_FOUND_IN_DBX,
					ErrorCodes.RECORD_NOT_FOUND, result);
		}

		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {

		String customerID = inputParams.get("Customer_id");

		String filter = "";

		filter += "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID;

		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		return true;
	}
}
package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserDetails implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			result = postProcess(dcRequest, result);
		}

		return result;
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		addCurrencyCode(result);
		updateBankName(dcRequest, result);
		return result;
	}

	private void updateBankName(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		String bankName = getBankName(dcRequest, HelperMethods.getFieldValue(result, "Bank_id"));
		Dataset ds = result.getAllDatasets().get(0);
		ds.getRecord(0).addParam(new Param("bankName", bankName, "String"));
	}

	private String getBankName(DataControllerRequest dcRequest, String bankId) throws HttpCallException {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + bankId;
		Result bank = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BANK_GET);
		return HelperMethods.getFieldValue(bank, "Description");
	}

	private void addCurrencyCode(Result result) {
		Dataset ds = result.getAllDatasets().get(0);
		String country = HelperMethods.getParamValue(ds.getRecord(0).getParam(DBPUtilitiesConstants.COUNTRY));
		Param currrencyCode = new Param(DBPUtilitiesConstants.CURRENCYCODE, HelperMethods.getCurrencyCode(country),
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		ds.getRecord(0).addParam(currrencyCode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String id = HelperMethods.getUserIdFromSession(dcRequest);
		String filter = DBPUtilitiesConstants.ID + DBPUtilitiesConstants.EQUAL + id;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}
package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

public class GetCustomerDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCES_GET);
			postProcess(dcRequest, result);
		}

		return result;
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		if (HelperMethods.hasRecords(result)) {
			result.getAllDatasets().get(0).getRecord(0).setId("customer");
			addCurrencyCode(result);
			updateBankName(dcRequest, result);
		}
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
		String country = HelperMethods.getFieldValue(result, "CountryCode");
		Param currrencyCode = new Param(DBPUtilitiesConstants.CURRENCYCODE, HelperMethods.getCurrencyCode(country),
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		ds.getRecord(0).addParam(currrencyCode);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (StringUtils.isBlank(id)) {
			id = inputParams.get("id");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("id").append(DBPUtilitiesConstants.EQUAL).append(id);

		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return true;
	}
}
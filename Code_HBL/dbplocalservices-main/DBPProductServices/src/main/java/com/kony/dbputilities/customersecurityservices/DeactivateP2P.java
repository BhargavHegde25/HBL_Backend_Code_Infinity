package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeactivateP2P implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			deleteTransactions(dcRequest, inputParams);
			updateUser(dcRequest, inputParams);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void deleteTransactions(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
		Result payPersons = getPayPersons(dcRequest, (String) inputParams.get("id"));
		List<Record> payPerson = payPersons.getAllDatasets().get(0).getAllRecords();
		for (Record person : payPerson) {
			deletePendingTransaction(dcRequest, person);
			updatePayPerson(dcRequest, person);
			updateUnsuccessfulTransaction(dcRequest, person);
		}
	}

	private void updateUnsuccessfulTransaction(DataControllerRequest dcRequest, Record person)
			throws HttpCallException {
		Result transactions = getUnsuccessfulTransactions(dcRequest, HelperMethods.getFieldValue(person, "id"));
		List<Record> transaction = transactions.getAllDatasets().get(0).getAllRecords();
		for (Record t : transaction) {
			Map<String, String> input = new HashMap<>();
			input.put("id", HelperMethods.getFieldValue(t, "id"));
			input.put("isPaypersonDeleted", "true");
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNT_TRANSACTION_UPDATE);
		}
	}

	private Result getUnsuccessfulTransactions(DataControllerRequest dcRequest, String personId)
			throws HttpCallException {
		String filter = "Person_id" + DBPUtilitiesConstants.EQUAL + personId + DBPUtilitiesConstants.AND + "statusDesc"
				+ DBPUtilitiesConstants.EQUAL + "Unsuccessful";
		return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.TRANSACTION_GET);
	}

	private void updatePayPerson(DataControllerRequest dcRequest, Record person) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("id", HelperMethods.getFieldValue(person, "id"));
		input.put("isSoftDelete", "true");
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.PAYPERSON_UPDATE);
	}

	private void deletePendingTransaction(DataControllerRequest dcRequest, Record person) throws HttpCallException {
		Result transactions = getPendingTransactions(dcRequest, HelperMethods.getFieldValue(person, "id"));
		List<Record> transaction = transactions.getAllDatasets().get(0).getAllRecords();
		for (Record t : transaction) {
			Map<String, String> input = new HashMap<>();
			input.put("id", HelperMethods.getFieldValue(t, "id"));
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.TRANSACTION_DELETE);
		}
	}

	private Result getPendingTransactions(DataControllerRequest dcRequest, String personId) throws HttpCallException {
		String filter = "Person_id" + DBPUtilitiesConstants.EQUAL + personId + DBPUtilitiesConstants.AND + "statusDesc"
				+ DBPUtilitiesConstants.EQUAL + "Pending";
		return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.TRANSACTION_GET);
	}

	private Result getPayPersons(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
		return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.PAYPERSON_GET);
	}

	@SuppressWarnings("rawtypes")
	private void updateUser(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("id", (String) inputParams.get("id"));
		input.put("isP2PActivated", "false");
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		inputParams.put("id", HelperMethods.getCustomerIdFromSession(dcRequest));
		return true;
	}
}
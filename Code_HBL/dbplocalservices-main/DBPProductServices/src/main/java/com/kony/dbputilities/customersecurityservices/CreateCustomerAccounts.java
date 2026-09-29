package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPDatasetConstants;
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

public class CreateCustomerAccounts implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			createEmployeeAccounts(inputParams, dcRequest, result);
		}
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {

		boolean status = true;

		Record record = new Record();

		if (StringUtils.isBlank(inputParams.get("id"))) {
			HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.USER_ID_MISSED_IN_REQUEST,
					ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
			result.addRecord(record);
			return false;
		}

		result.addParam(new Param("Customer_id", inputParams.get("id"), "String"));

		if (!inputParams.containsKey("accounts")) {
			HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.ACCOUNTS_MISSING_IN_REQUEST,
					ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
			result.addRecord(record);
			return false;
		}

		return status;
	}

	/*
	 * private Result postProcess(Result result, DataControllerRequest dcRequest,
	 * String memberId) { Result retResult = new Result(); Record record = new
	 * Record(); record.setId(DBPUtilitiesConstants.USR_ATTR); if
	 * (!HelperMethods.hasError(result)) { String id = getUserId(result);
	 * HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.SUCCESS_MSG,
	 * ErrorCodes.RECORD_CREATED, record); record.addParam(new Param("id", id,
	 * "String" ));
	 * 
	 * } else {
	 * HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result),
	 * ErrorCodes.ERROR_CREATING_RECORD, record); }
	 * 
	 * retResult.addRecord(record); return retResult; }
	 * 
	 * private String getUserId(Result result) { String id = ""; Dataset ds =
	 * result.getAllDatasets().get(0); if (null != ds &&
	 * !ds.getAllRecords().isEmpty()) { id = ds.getRecord(0).getParam("id")
	 * .getValue(); } return id; }
	 */

	private void createEmployeeAccounts(Map<String, String> inputParams, DataControllerRequest dcRequest,
			Result result) {

		String customer_id = inputParams.get("id");
		String customer_accounts = inputParams.get("accounts");
		JsonArray customerAccounts = null;
		Record record = new Record();

		try {
			customerAccounts = (JsonArray) new JsonParser().parse(customer_accounts);
		} catch (Exception e) {
			record.setId(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);
			HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.ACCOUNTS_MISSING_IN_REQUEST,
					ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
			result.addRecord(record);
			return;
		}

		if (customerAccounts.size() == 0) {
			record.setId(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);
			HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.ACCOUNTS_MISSING_IN_REQUEST,
					ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
			result.addRecord(record);
			return;
		}

		Dataset customerAccountDataset = new Dataset();

		// log.error("CreateCustomerAccounts2:: "+ customerAccounts.toString());
		Result createAccount = new Result();

		for (int i = 0; i < customerAccounts.size(); i++) {
			JsonObject eachAccount = customerAccounts.get(i).getAsJsonObject();
			Map<String, String> inputMaps = processAccount(customer_id, eachAccount);
			// log.error("\nCreateCustomerAccounts3:: "+ eachAccount.get("id") + ": " +
			// eachAccount.get("MemberId")+ " : "+ eachAccount.get("Account_Name"));
			try {
				createAccount = HelperMethods.callApi(dcRequest, inputMaps, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMERACCOUNTS_CREATE);
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
			// log.error("\nCreateCustomerAccounts4:: "+ createAccount.toString());
			// log.error("\nCreateCustomerAccounts11:: "+
			// createAccount.getAllParams().toString());
			customerAccountDataset.addRecord(createAccount.getAllDatasets().get(0).getRecord(0));
		}
		customerAccountDataset.setId(DBPDatasetConstants.CUSTOMER_ACCOUNTS_DATASET);
		// log.error("CreateCustomerAccounts12:: "+result.toString());
		result.addDataset(customerAccountDataset);
		result.addAllParams(createAccount.getAllParams());
	}

	private Map<String, String> processAccount(String customer_id, JsonObject eachAccount) {
		Map<String, String> inputMaps = new HashMap<>();
		inputMaps.put("Customer_id", customer_id);
		inputMaps.put("Account_id", eachAccount.get("id").getAsString());
		inputMaps.put("MemberId", eachAccount.get("MemberId").getAsString());
		inputMaps.put("AccountName", eachAccount.get("Account_Name").getAsString());
		inputMaps.put("IsViewAllowed", "1");
		inputMaps.put("IsDepositAllowed", "1");
		inputMaps.put("IsWithdrawAllowed", "1");
		inputMaps.put("IsOrganizationAccount", "1");

		return inputMaps;
	}

}

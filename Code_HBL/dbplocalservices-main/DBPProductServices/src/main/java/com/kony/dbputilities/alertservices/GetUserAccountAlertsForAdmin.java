package com.kony.dbputilities.alertservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserAccountAlertsForAdmin implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_ACC_ALERT_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			postProcess(dcRequest, result);
		}
		return result;
	}

	private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		List<Record> alerts = result.getAllDatasets().get(0).getAllRecords();
		for (Record alert : alerts) {
			updateToAccountDetails(dcRequest, alert);
			updateAccountAlertsDetails(alert);
		}
	}

	private void updateAccountAlertsDetails(Record alert) {
		Dataset alertDetails = new Dataset("alerts");
		alertDetails.addRecord(getAccountAlertsCreditLimit(alert));
		alertDetails.addRecord(getAccountAlertsDebitLimit(alert));
		alertDetails.addRecord(getAccountAlertsCheckClearance(alert));
		alertDetails.addRecord(getAccountAlertsMinimumBalance(alert));
		alert.addDataset(alertDetails);
	}

	private Record getAccountAlertsCreditLimit(Record alert) {
		Record creditLimit = new Record();
		creditLimit.addParam(new Param("name", "Credit Limit", "String"));
		if (StringUtils.isBlank(HelperMethods.getFieldValue(alert, "debitLimit"))) {
			creditLimit.addParam(new Param("value", "0.00", "String"));
			creditLimit.addParam(new Param("status", "Inactive", "String"));
		} else {
			creditLimit.addParam(new Param("value", HelperMethods.getFieldValue(alert, "creditLimit"), "String"));
			creditLimit.addParam(new Param("status", "Active", "String"));
		}
		creditLimit.addParam(new Param("push", "true", "String"));
		creditLimit.addParam(new Param("sms", "false", "String"));
		creditLimit.addParam(new Param("email", "true", "String"));
		return creditLimit;
	}

	private Record getAccountAlertsDebitLimit(Record alert) {
		Record debitLimit = new Record();
		debitLimit.addParam(new Param("name", "Debit Limit", "String"));
		if (StringUtils.isBlank(HelperMethods.getFieldValue(alert, "debitLimit"))) {
			debitLimit.addParam(new Param("value", "0.00", "String"));
			debitLimit.addParam(new Param("status", "Inactive", "String"));
		} else {
			debitLimit.addParam(new Param("value", HelperMethods.getFieldValue(alert, "debitLimit"), "String"));
			debitLimit.addParam(new Param("status", "Active", "String"));
		}
		debitLimit.addParam(new Param("push", "true", "String"));
		debitLimit.addParam(new Param("sms", "false", "String"));
		debitLimit.addParam(new Param("email", "false", "String"));
		return debitLimit;
	}

	private Record getAccountAlertsCheckClearance(Record alert) {
		Record chkClearance = new Record();
		chkClearance.addParam(new Param("name", "Check Clearance", "String"));
		if (StringUtils.isBlank(HelperMethods.getFieldValue(alert, "debitLimit"))) {
			chkClearance.addParam(new Param("value", "0.00", "String"));
			chkClearance.addParam(new Param("status", "Inactive", "String"));
		} else {
			chkClearance.addParam(new Param("value", HelperMethods.getFieldValue(alert, "checkClearance"), "String"));
			chkClearance.addParam(new Param("status", "Active", "String"));
		}
		chkClearance.addParam(new Param("push", "true", "String"));
		chkClearance.addParam(new Param("sms", "false", "String"));
		chkClearance.addParam(new Param("email", "false", "String"));
		return chkClearance;
	}

	private Record getAccountAlertsMinimumBalance(Record alert) {
		Record minimumBal = new Record();
		minimumBal.addParam(new Param("name", "Minimum Balance", "String"));
		if (StringUtils.isBlank(HelperMethods.getFieldValue(alert, "debitLimit"))) {
			minimumBal.addParam(new Param("value", "0.00", "String"));
			minimumBal.addParam(new Param("status", "Inactive", "String"));
		} else {
			minimumBal.addParam(new Param("value", HelperMethods.getFieldValue(alert, "minimumBalance"), "String"));
			minimumBal.addParam(new Param("status", "Active", "String"));
		}
		minimumBal.addParam(new Param("push", "true", "String"));
		minimumBal.addParam(new Param("sms", "false", "String"));
		minimumBal.addParam(new Param("email", "false", "String"));
		return minimumBal;
	}

	private void updateToAccountDetails(DataControllerRequest dcRequest, Record alert) throws HttpCallException {
		String accountNum = HelperMethods.getFieldValue(alert, "AccountNumber");
		if (StringUtils.isNotBlank(accountNum)) {
			String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
			Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNTS_GET);
			alert.addParam(new Param("accountType", HelperMethods.getFieldValue(account, "typeDescription"),
					DBPUtilitiesConstants.STRING_TYPE));
			alert.addParam(new Param("accountName", HelperMethods.getFieldValue(account, "accountName"),
					DBPUtilitiesConstants.STRING_TYPE));
		}
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
		if (HelperMethods.isAdmin(dcRequest, adminId)) {
			String filter = "User_id" + DBPUtilitiesConstants.EQUAL + getUserId(dcRequest, inputParams.get("username"));
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			return true;
		} else {
			HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
			return false;
		}
	}

	private String getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
		return HelperMethods.getFieldValue(user, "id");
	}

}

package com.kony.dbputilities.alertservices;

import java.util.HashMap;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserAccountAlerts implements JavaService2 {

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
			postProcess(dcRequest, inputParams, result);
		}
		return result;
	}

	private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
			throws HttpCallException {
		Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
		Map<String, String> timePeriods = getTimePeriods(dcRequest);
		List<Record> alters = result.getAllDatasets().get(0).getAllRecords();
		for (Record alert : alters) {
			updateAccountDetails(dcRequest, alert, accountTypes);
			updateTimePeriodDetails(dcRequest, alert, timePeriods);
		}
	}

	private void updateAccountDetails(DataControllerRequest dcRequest, Record alert, Map<String, String> accountTypes)
			throws HttpCallException {
		String accountNum = HelperMethods.getFieldValue(alert, "AccountNumber");
		if (StringUtils.isNotBlank(accountNum)) {
			String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
			Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNTS_GET);
			String typeId = HelperMethods.getFieldValue(frmAccount, "Type_id");
			alert.addParam(new Param("accountType", accountTypes.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
			String accountName = HelperMethods.getFieldValue(frmAccount, "nickName");
			alert.addParam(new Param("accountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
		}
	}

	private void updateTimePeriodDetails(DataControllerRequest dcRequest, Record alert,
			Map<String, String> timePeriods) {
		String timeperiod1 = HelperMethods.getFieldValue(alert, "timeperiod1");
		String timeperiod2 = HelperMethods.getFieldValue(alert, "timeperiod2");
		String timeperiod3 = HelperMethods.getFieldValue(alert, "timeperiod3");
		alert.addParam(new Param("balanceUpdate", timePeriods.get(timeperiod1), DBPUtilitiesConstants.STRING_TYPE));
		alert.addParam(
				new Param("paymentDueReminder", timePeriods.get(timeperiod2), DBPUtilitiesConstants.STRING_TYPE));
		alert.addParam(
				new Param("depositDueReminder", timePeriods.get(timeperiod3), DBPUtilitiesConstants.STRING_TYPE));
	}

	private Map<String, String> getTimePeriods(DataControllerRequest dcRequest) throws HttpCallException {
		Result timePeriods = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
				URLConstants.TIMEPERIOD_GET);
		Map<String, String> timePeriodMap = new HashMap<>();
		List<Record> types = timePeriods.getAllDatasets().get(0).getAllRecords();
		for (Record type : types) {
			timePeriodMap.put(HelperMethods.getFieldValue(type, "id"),
					HelperMethods.getFieldValue(type, "description"));
		}
		return timePeriodMap;
	}

	private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
			throws HttpCallException {
		String filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
		Result accountTypes = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.ACCOUNTTYPE_GET);
		Map<String, String> accountTypeMap = new HashMap<>();
		List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
		for (Record type : types) {
			accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeID"),
					HelperMethods.getFieldValue(type, "TypeDescription"));
		}
		return accountTypeMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String alertId = (String) inputParams.get("alertId");
		Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
		inputParams.put("countryCode", user.get("countryCode"));
		StringBuilder filter = new StringBuilder();
		filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(user.get("user_id"));
		if (StringUtils.isNotBlank(alertId) && !alertId.contains("$")) {
			filter.append(DBPUtilitiesConstants.AND);
			filter.append("id").append(DBPUtilitiesConstants.AND).append(alertId);
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}
}

package com.hbl.javaservices;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import com.konylabs.middleware.dataobject.ResultToJSON;

public class TransactionPINHistoryEntry implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(TransactionPINHistoryEntry.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		String UserName = request.getParameter("userName");
		String pin = request.getParameter("pin");
		String customerId = getCustomerId(request, UserName);
		try {

			if (checkPinLastFiveEntries(request, UserName, pin)) {
				if (pinEntryinDB(request, customerId, pin)) {
					result.setParam(new Param("pinHistoryEntrySuccess", "true"));
				} else {
					result.setParam(new Param("pinHistoryEntrySuccess", "false"));
				}
			} else {
				result.setParam(new Param("pinHistoryEntrySuccess", "false"));
				result.setParam(new Param("ErrorMsg", "Transaction PIN should not match with last 5 Pin's "));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

		} catch (Exception e) {
			result.setParam(new Param("pinHistoryEntrySuccess", "false"));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}

		return result;
	}

	public static boolean pinEntryinDB(DataControllerRequest dcRequest, String customerId, String pin)
			throws HttpCallException {
		boolean isPinEntryMade = false;
		HashMap<String, String> hashMap = new HashMap<>();
		String id = UUID.randomUUID().toString();
		logger.debug("ID Value : " + id);
		hashMap.put("id", id);
		if (StringUtils.isBlank(customerId)) {
			customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		}
		hashMap.put("Customer_id", customerId);
		hashMap.put("PreviousPin", pin);
		hashMap.put("createdby", HelperMethods.getUserFromIdentityService(dcRequest).get("userName"));
		Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
				URLConstants.PIN_HISTORY_CREATE);

		if (HelperMethods.hasRecords(result)) {
			isPinEntryMade = true;
		} else {
			isPinEntryMade = false;
		}
		logger.debug("isPinEntryMade at the end of makePINEntry : " + isPinEntryMade);

		return isPinEntryMade;
	}

	public boolean checkPinLastFiveEntries(DataControllerRequest dcRequest, String userName, String pin)
			throws HttpCallException {

		HashMap<String, String> hashMap = new HashMap<>();
		String customerId = getCustomerId(dcRequest, userName);

		hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + customerId);
		hashMap.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
		hashMap.put(DBPUtilitiesConstants.TOP, "5");
		hashMap.put(DBPUtilitiesConstants.SKIP, "0");
		logger.debug("InputParams for" + URLConstants.PIN_HISTORY_GET + " : " + hashMap);

		Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
				URLConstants.PIN_HISTORY_GET);

		logger.debug("Result for" + URLConstants.PIN_HISTORY_GET + " : " + ResultToJSON.convert(result));

		if (!HelperMethods.hasError(result) && null != result.getAllDatasets()
				&& result.getAllDatasets().get(0).getAllRecords().isEmpty()) {
			return true;
		}
		if (HelperMethods.hasRecords(result)) {
			List<Record> records = result.getAllDatasets().get(0).getAllRecords();
			for (Record record : records) {
				if (record.getNameOfAllParams().contains("PreviousPin")
						&& StringUtils.isNotBlank(record.getParam("PreviousPin").getValue())
						&& pin.equalsIgnoreCase(record.getParam("PreviousPin").getValue())) {
					return false;
				}
			}
		}

		return true;
	}

	private String getCustomerId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
		Result result = new Result();
		String filter = "";
		filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERVERIFY_GET);
		if (HelperMethods.hasRecords(result)) {
			return HelperMethods.getFieldValue(result, "id");
		}
		return null;
	}

}

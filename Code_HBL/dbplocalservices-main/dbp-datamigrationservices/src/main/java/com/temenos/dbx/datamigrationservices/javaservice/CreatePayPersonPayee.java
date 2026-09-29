package com.temenos.dbx.datamigrationservices.javaservice;

import java.util.HashMap;
import java.util.Map;

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
import com.konylabs.middleware.dataobject.Result;

public class CreatePayPersonPayee implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreatePayPersonPayee.class);

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			inputParams.put("id", HelperMethods.getRandomNumericString(8));
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.PAYPERSON_CREATE);
			if (HelperMethods.hasRecords(result) && StringUtils.isNotBlank(inputParams.get("transactionId"))) {
				updateOldTransaction(dcRequest, HelperMethods.getFieldValue(result, "id"), inputParams.get("transactionId"));
			}
		}

		return result;
	}

	private void updateOldTransaction(DataControllerRequest dcRequest, String personId, String transactionId)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("Person_Id", personId);
		input.put("Id", transactionId);
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.TRANSACTION_UPDATE);
	}

	/**
	 * validates mandatory fields : NickName and AccountNumber
	 * 
	 * @param inputParams - map of input params defined for service api
	 * @param dcRequest
	 * @param result
	 * @return - returns boolean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String userId = (String) inputParams.get("userId");

		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + userId);

		String phone = (String) inputParams.get(DBPUtilitiesConstants.PP_PHONE);
		String email = (String) inputParams.get(DBPUtilitiesConstants.PP_EMAIL);
		inputParams.put(DBPUtilitiesConstants.PP_USR_ID, userId);

		if (StringUtils.isBlank(phone) && StringUtils.isBlank(email)) {
			HelperMethods.setValidationMsg("Please provide Phone " + "or Email", dcRequest, result);
			status = false;
		}

		return status;
	}
}

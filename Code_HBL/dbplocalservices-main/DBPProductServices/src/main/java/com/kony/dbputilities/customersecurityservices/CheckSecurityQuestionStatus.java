package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CheckSecurityQuestionStatus implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_SECURITYQUESTIONS_GET);
		}
		result = postProcess(result, dcRequest);
		return result;
	}

	private Result postProcess(Result result, DataControllerRequest dcRequest) {
		String questionId = HelperMethods.getFieldValue(result, "Question_id");
		Result securityQuestion = new Result();
		Map<String, String> inputParams = new HashMap<>();
		String filter = "id" + DBPUtilitiesConstants.EQUAL + questionId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			securityQuestion = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.SECURITY_QUESTION_GET);
		} catch (HttpCallException e) {
			securityQuestion = new Result();
			alert.prepareError(e.getMessage()).log();
		}
		String msg = "Questions does not exist";
		if (HelperMethods.hasRecords(securityQuestion)) {
			msg = "Questions Exist";
		}
		Result response = new Result();
		response.addParam(new Param("result", msg, DBPUtilitiesConstants.STRING_TYPE));
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}
package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerSecurityQuestionsIDsFromDB implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result returnResult = new Result();
		if (preProcess(inputParams, dcRequest)) {
			try {
				return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
						HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_SECURITYQUESTIONS_GET);

			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
		}

		return returnResult;

	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest) {
		String customer_id = dcRequest.getParameter("id");

		if (StringUtils.isBlank(customer_id)) {
			return false;
		}

		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_id;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		return true;
	}
}
package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CreateCustomerBusinessType {

	public static void invoke(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Map<String, String> input = new HashMap<>();
		input.put("Customer_id", inputParams.get("id"));
		input.put("BusinessType_id", inputParams.get("businessTypeId"));
		input.put("SignatoryType_id",
				StringUtils.isNotBlank(inputParams.get("authSignatoryType")) ? inputParams.get("authSignatoryType")
						: dcRequest.getParameter("authSignatoryType"));
		try {
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERBUSINESSTYPE_CREATE);
		} catch (Exception e) {
		}
	}
}

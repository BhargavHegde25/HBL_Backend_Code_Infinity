package com.temenos.auth.usermanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CreateCustomerPreference {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public static void invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) {

		Map<String, String> input = new HashMap<>();
		String id = inputParams.get("id");
		SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		input.put("id", idformatter.format(new Date()));
		input.put("Customer_id", id);
		try {
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCE_CREATE);
		} catch (HttpCallException e) {

			alert.prepareError(e.getMessage()).log();
		}
	}
}

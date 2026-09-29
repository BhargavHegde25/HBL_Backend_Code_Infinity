package com.kony.dbputilities.organisation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CreateEmployee {

	public static void invoke(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Map<String, String> input = null;
		String id = inputParams.get("id");
		input = new HashMap<>();
		SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		input.put("id", idformatter.format(new Date()));
		input.put("Customer_id", id);
		input.put("Organization_id", inputParams.get("Organization_Id"));
		input.put("isAuthSignatory",
				StringUtils.isNotBlank(inputParams.get("isAuthSignatory")) ? inputParams.get("isAuthSignatory")
						: dcRequest.getParameter("isAuthSignatory"));
		if (StringUtils.isNotBlank(inputParams.get("Is_Owner"))
				&& inputParams.get("Is_Owner").equalsIgnoreCase("true")) {
			input.put("Is_Owner", "1");
			input.put("Is_Admin", "1");
		}

		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.ORGANISATIONEMPLOYEE_CREATE);
	}

}

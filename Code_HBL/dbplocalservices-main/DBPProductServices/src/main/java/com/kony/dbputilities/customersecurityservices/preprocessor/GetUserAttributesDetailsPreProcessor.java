package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserAttributesDetailsPreProcessor implements DataPreProcessor2 {

	@Override
	public boolean execute(HashMap inputArray, DataControllerRequest request, DataControllerResponse arg2,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();

		/**
		 * This class needs refactoring.. By default, all the elements in input array
		 * should be added to result by default.
		 * 
		 * Additional attributes (if needed with different name) should be added via
		 * code.
		 */

		String userName = (String) inputArray.get("UserName");
		String customer_id = (String) inputArray.get("customer_id");
		String countryCode = (String) inputArray.get("countryCode");
		String CustomerType_id = (String) inputArray.get("CustomerType_id");
		String Organization_Id = (String) inputArray.get("Organization_Id");
		String backendIdentifiers = (String) inputArray.get("backendIdentifiers");
		String session_token = (String) inputArray.get("session_token");
		 

		result.addParam(new Param("session_token", session_token, MWConstants.STRING));
		result.addParam(new Param("BackendId", backendIdentifiers, MWConstants.STRING));
		result.addParam(new Param("user_id", customer_id, MWConstants.STRING));
		result.addParam(new Param("customer_id", customer_id, MWConstants.STRING));
		result.addParam(new Param("Customer_id", customer_id, MWConstants.STRING));
		result.addParam(new Param("Customer_Id", customer_id, MWConstants.STRING));
		result.addParam(new Param("customerId", customer_id, MWConstants.STRING));
		result.addParam(new Param("userName", userName, MWConstants.STRING));
		result.addParam(new Param("UserName", userName, MWConstants.STRING));
		result.addParam(new Param("username", userName, MWConstants.STRING));
		result.addParam(new Param("countryCode", countryCode, MWConstants.STRING));
		result.addParam(new Param("customerType", CustomerType_id, MWConstants.STRING));
		result.addParam(new Param("CustomerType_id", CustomerType_id, MWConstants.STRING));
		result.addParam(new Param("Organization_Id", Organization_Id, MWConstants.STRING));

		if (userName != null && userName.equals("admin")) {
			result.addParam(new Param("isC360Admin", "true", MWConstants.STRING));
		} else {
			result.addParam(new Param("isC360Admin", "false", MWConstants.STRING));
		}

		return false;
	}
}

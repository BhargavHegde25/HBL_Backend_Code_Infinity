package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerDetailsPreProcessor implements DataPreProcessor2 {

	@Override
	public boolean execute(HashMap inputArray, DataControllerRequest request, DataControllerResponse arg2,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();

		String userId = (String) inputArray.get("customer_id");
		String legalEntityId = (String) inputArray.get("legalEntityId");
		result.addParam(new Param("userId", userId, MWConstants.STRING));
		result.addParam(new Param("customer_id", userId, MWConstants.STRING));
		result.addParam(new Param("legalEntityId", legalEntityId, MWConstants.STRING));
		return false;
	}

}

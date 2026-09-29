package com.temenos.infinity.api.cards.preprocessors;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AddUserIdIntegrationPreprocessor implements DataPreProcessor2{
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		String userId = (String)request.getServicesManager().getIdentityHandler().getUserAttributes().get("customer_id");
		params.put("userId", userId);
		return true;
	}
}

package com.infinity.dbx.temenos.transfers;

import java.util.HashMap;
import java.util.Map;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetPSDConsentsPreProcessor extends TemenosBasePreProcessor {

	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		super.execute(params, request, response, result);
		IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
		String userID = identityHandler.getUserId();
		request.addRequestParam_("userID", userID);
		params.put("userID", userID);
		Map<String, Object> userAttributes = identityHandler.getUserAttributes();
		if (userAttributes != null && userAttributes.size() > 0) {
			String userName = (String) userAttributes.get("UserName");			
			request.addRequestParam_("userName", userName);
			params.put("userName", userName);
		}
		return Boolean.TRUE;

	}
}

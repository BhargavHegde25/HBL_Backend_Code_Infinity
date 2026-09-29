package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateFullUserDetailsPreProcessor extends TemenosBasePreProcessor {

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		super.execute(params, request);
		return Boolean.TRUE;
	}

}

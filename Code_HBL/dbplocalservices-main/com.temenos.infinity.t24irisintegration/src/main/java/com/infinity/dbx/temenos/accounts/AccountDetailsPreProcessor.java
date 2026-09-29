package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AccountDetailsPreProcessor extends TemenosBasePreProcessor{

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);

		return Boolean.TRUE;
	}

}

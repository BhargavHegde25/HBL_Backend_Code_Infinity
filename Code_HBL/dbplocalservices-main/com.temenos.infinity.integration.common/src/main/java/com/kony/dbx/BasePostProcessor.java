package com.kony.dbx;

import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public abstract class BasePostProcessor implements DataPostProcessor2, Constants {
	
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		
		return result;
	}

}

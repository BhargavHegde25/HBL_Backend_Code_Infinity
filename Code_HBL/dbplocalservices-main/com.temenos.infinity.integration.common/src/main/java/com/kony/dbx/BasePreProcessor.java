package com.kony.dbx;

import java.util.HashMap;

import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public abstract class BasePreProcessor implements DataPreProcessor2, Constants {

	@Override
	public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request, DataControllerResponse response, Result result) throws Exception {
				
		return true;
	}
}

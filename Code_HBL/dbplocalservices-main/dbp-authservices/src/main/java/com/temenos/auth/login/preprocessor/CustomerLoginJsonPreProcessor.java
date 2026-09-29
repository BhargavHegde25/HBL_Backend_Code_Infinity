package com.temenos.auth.login.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.utilities.EncodeUtils;

public class CustomerLoginJsonPreProcessor implements DataPreProcessor2 {


	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputParams, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();

		if (inputParams.get("UserName") != null && StringUtils.isNotBlank(inputParams.get("UserName").toString())) {
			inputParams.put("UserName", EncodeUtils.encode(inputParams.get("UserName").toString()));
		}
		if (inputParams.get("Password") != null && StringUtils.isNotBlank(inputParams.get("Password").toString())) {
			inputParams.put("Password", EncodeUtils.encode(inputParams.get("Password").toString()));
		}
		return true;
	}

}

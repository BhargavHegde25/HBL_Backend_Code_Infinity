package com.kony.dbputilities.customersecurityservices.preprocessor;

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
		String ipAddress = request.getParameter("CLIENT_IP");
		if(StringUtils.isNotBlank(ipAddress)) {
			request.getHeaderMap().put("CLIENT_IP", ipAddress);
		}
		if (inputParams.get("userid") != null && StringUtils.isNotBlank(inputParams.get("userid").toString())) {
			inputParams.put("userid", EncodeUtils.encode(inputParams.get("userid").toString()));
		}
		if (inputParams.get("Password") != null && StringUtils.isNotBlank(inputParams.get("Password").toString())) {
			inputParams.put("Password", EncodeUtils.encode(inputParams.get("Password").toString()));
		}
		return true;
	}

}

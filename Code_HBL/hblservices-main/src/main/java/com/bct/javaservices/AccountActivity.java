package com.bct.javaservices;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AccountActivity implements JavaService2 {

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

		

		Result result = new Result();
		result.setParam(new Param("opstatus", "200"));

		return result;
	}

}

package com.kony.eventdispatcher.service;

import com.kony.eventdispatcher.wrapper.EventsConfigurationHolder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateEventConfiguration implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		EventsConfigurationHolder.updateEventsConfiguration();
		Result res = new Result();
		res.addParam(new Param("Success", "true", "String"));
		return res;
	}

}

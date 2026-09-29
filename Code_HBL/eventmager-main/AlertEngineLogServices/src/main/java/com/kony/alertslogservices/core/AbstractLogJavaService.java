package com.kony.alertslogservices.core;

import com.kony.alertslogservices.dbutils.AlertsLogDataSourceHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public abstract class AbstractLogJavaService implements JavaService2 {

	@Override
	public Object invoke(String methodid, Object[] inputarray, DataControllerRequest requestinstance,
			DataControllerResponse responseinstance) throws Exception {
		try {
			// keeping DCR in current thread context
			AlertsLogDataSourceHandler.setRequest(requestinstance);

			return execute(methodid, inputarray, requestinstance, responseinstance);
		} finally {
			// removing DCR from current thread context. This is important.
			AlertsLogDataSourceHandler.removeRequest();

		}
	}

	public abstract Object execute(String methodid, Object[] inputarray, DataControllerRequest requestinstance,
			DataControllerResponse responseinstance);

}

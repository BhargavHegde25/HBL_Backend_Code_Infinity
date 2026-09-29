package com.kony.dbp.batchprocessingalerts;

import java.util.Map;

import com.kony.dbp.batchprocessingengine.helper.HelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class DispatchEvents {
	private DispatchEvents() {
		
	}
	public static Result callQueueMaster(ServicesManager servicesManager, Map<String, Object> inputMap,
			Map<String, Object> headerMap) {
		Result result = new Result();
		try {
			OperationData operationData = servicesManager.getOperationDataBuilder().withServiceId("QueueMaster")
					.withOperationId("PushEventQueue").build();
			ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
					.withHeaders(headerMap).build();
			result = serviceRequest.invokeServiceAndGetResult();
		} catch (AppRegistryException arex) {
			result = HelperMethods.getErrorResult(1012, "Could not access QueueManager service via app registry");
		} catch (Exception ex) {
			result = HelperMethods.getErrorResult(ex);
		}
		return result;
	}

}

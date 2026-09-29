package com.kony.scaintegration.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface SCAIntegrationResource extends Resource {
	Result processPayload(String methodId, DataControllerRequest request);
}

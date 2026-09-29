package com.dbp.reminderengine.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface ProcessAlertsResource extends Resource {

	Result getCustomers(DataControllerRequest request);

}

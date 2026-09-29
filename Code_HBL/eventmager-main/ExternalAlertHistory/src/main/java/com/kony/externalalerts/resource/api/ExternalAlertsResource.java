package com.kony.externalalerts.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface ExternalAlertsResource extends Resource {
	Result triggerExternalAlerts(DataControllerRequest request);

}

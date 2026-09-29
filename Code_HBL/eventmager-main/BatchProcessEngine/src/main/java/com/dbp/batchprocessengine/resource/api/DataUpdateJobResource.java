package com.dbp.batchprocessengine.resource.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface DataUpdateJobResource {

	Result generateEventsFromCoreData(DataControllerRequest request);

}

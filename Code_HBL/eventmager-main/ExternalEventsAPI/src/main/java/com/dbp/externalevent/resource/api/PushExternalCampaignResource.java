package com.dbp.externalevent.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PushExternalCampaignResource extends Resource {

	Result pushExternalCampaign(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}

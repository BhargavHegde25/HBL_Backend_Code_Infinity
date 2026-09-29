package com.dbp.campaigndeliveryengine.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface DeliverCampaignResource extends Resource {

	Result deliverCampaign(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	

}

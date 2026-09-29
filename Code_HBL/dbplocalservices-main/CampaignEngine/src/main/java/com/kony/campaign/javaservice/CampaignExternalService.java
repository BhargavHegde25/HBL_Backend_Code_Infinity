package com.kony.campaign.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaign.resource.api.CampaignResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CampaignExternalService implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
	
		CampaignResource campaignResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(CampaignResource.class);

		return campaignResource.getCampaignsForExternalEvent(
				methodID, inputArray, request, response);
	}
	

}

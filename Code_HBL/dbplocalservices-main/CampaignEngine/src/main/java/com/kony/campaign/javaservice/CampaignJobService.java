package com.kony.campaign.javaservice;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CampaignJobService implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);

		return campaignBusinessDelegate.getCampaignsForJob();
	}

}

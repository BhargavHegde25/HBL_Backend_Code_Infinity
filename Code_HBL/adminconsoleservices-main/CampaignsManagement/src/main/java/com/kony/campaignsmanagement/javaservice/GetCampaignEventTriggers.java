package com.kony.campaignsmanagement.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.campaignsmanagement.resource.api.CampaignsManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetCampaignEventTriggers implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("INFINITY", "SPOTLIGHT");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			Result result = new Result();
			CampaignsManagementResource resource = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(ResourceFactory.class).getResource(CampaignsManagementResource.class);
			
			result = resource.getEventTriggers(methodId, inputArray, request, response);
			
			return result;
		} catch(Exception e) {
			alert.prepareError("Exception occurred in invoking GetCampaignsPlaceholders: " + e).log();
            return ErrorCodeEnum.ERR_22240.setErrorCode(new Result());
		}
	}

}

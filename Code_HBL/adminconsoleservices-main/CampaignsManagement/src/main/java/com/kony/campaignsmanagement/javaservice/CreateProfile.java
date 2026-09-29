package com.kony.campaignsmanagement.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaignsmanagement.resource.api.CampaignsManagementResource;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.campaignsmanagement.utils.ErrorCodesEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class CreateProfile implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			CampaignsManagementResource campaignManagementResource = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(ResourceFactory.class).getResource(CampaignsManagementResource.class);
			
			result = campaignManagementResource.createProfile(methodId, inputArray, request, response);
			
			return result;
			} catch(Exception e) {
				alert.prepareError("Exception occurred in invoking createProfile service " + e).log();
	            return ErrorCodesEnum.ERR_10003.setErrorCode(new Result());
			}
	}

}

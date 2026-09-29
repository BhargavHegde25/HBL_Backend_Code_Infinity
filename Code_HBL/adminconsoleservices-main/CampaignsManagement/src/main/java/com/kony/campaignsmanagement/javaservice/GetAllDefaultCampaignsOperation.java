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

public class GetAllDefaultCampaignsOperation implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("INFINITY", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
        try {
            CampaignsManagementResource camPaignResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(CampaignsManagementResource.class);
            result = camPaignResource.getAllDefaultCampaigns(methodId, inputArray, request, response);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of create Campiagn: ", e).log();
            return ErrorCodeEnum.ERR_22246.setErrorCode(new Result());
        }
        return result;
	}

}

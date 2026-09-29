package com.temenos.auth.usermanagement.operation;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.auth.usermanagement.resource.api.AuthUserManagementResource;

public class UpdateTNCTimeStampOperation implements JavaService2 {
	@Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
    	
		AuthUserManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(AuthUserManagementResource.class);
		return resource.updateTNCTimeStamp(methodID, inputArray, dcRequest, dcResponse);
    }
}

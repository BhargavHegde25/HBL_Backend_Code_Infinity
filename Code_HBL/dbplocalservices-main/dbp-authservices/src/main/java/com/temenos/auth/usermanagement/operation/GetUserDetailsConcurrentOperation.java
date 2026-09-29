package com.temenos.auth.usermanagement.operation;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;

public class GetUserDetailsConcurrentOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
    	
    	ProfileManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(ProfileManagementResource.class);
    	
		return resource.getUserDetailsConcurrent(methodID, inputArray, dcRequest, dcResponse);
    }

}
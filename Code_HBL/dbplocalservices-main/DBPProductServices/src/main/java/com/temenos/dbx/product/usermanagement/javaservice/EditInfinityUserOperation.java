package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 *
 */
public class EditInfinityUserOperation implements JavaService2{


    /**
     *
     */
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        return infinityUserManagementResource.editInfinityUser(methodId, inputArray, request, response);
    }

}

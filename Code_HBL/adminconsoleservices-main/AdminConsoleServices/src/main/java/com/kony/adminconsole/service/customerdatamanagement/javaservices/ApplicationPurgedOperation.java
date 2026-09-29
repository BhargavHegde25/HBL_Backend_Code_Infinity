package com.kony.adminconsole.service.customerdatamanagement.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customerdatamanagement.resource.api.CustomerDataManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class ApplicationPurgedOperation implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {

        CustomerDataManagementResource customerDataResource = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class).getResource(CustomerDataManagementResource.class);
        return customerDataResource.applicationPurge(s, objects, dataControllerRequest, dataControllerResponse);
    }
}

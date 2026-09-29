package com.kony.adminconsole.service.customerdatamanagement.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customerdatamanagement.resource.api.CustomerDataManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CustomerDataErasureOperation implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

        CustomerDataManagementResource customerDataResource = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class).getResource(CustomerDataManagementResource.class);
        return customerDataResource.triggerErasure(dataControllerRequest);
    }
}

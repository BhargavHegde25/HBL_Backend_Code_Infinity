package com.kony.adminconsole.service.customer.javaservices;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.customer.resource.api.InfinityUserManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * @author
 * @since 2021.01
 * @version 1.0 Fetches the customers with which the user is associated
 */
public class AssociatedCustomersGetOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.getAssociatedCustomers(methodID, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
        }

        return result;
    }
}

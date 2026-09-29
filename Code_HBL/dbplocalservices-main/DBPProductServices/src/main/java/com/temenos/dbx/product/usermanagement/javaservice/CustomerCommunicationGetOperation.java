package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerCommunicationResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CustomerCommunicationGetOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            CustomerCommunicationResource customerCommunicationResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(CustomerCommunicationResource.class);
            result = customerCommunicationResource.getCustomerCommunication(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            e.setError(result);
            alert.prepareError("Caught exception while getting customer communication info: ", e).log();
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting customer communication info: ", e).log();
        }

        return result;
    }

}

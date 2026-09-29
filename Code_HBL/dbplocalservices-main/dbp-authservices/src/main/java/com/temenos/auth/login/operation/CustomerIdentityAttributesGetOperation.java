package com.temenos.auth.login.operation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.auth.login.resource.api.CustomerIdentityAttributesResource;

public class CustomerIdentityAttributesGetOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {

            CustomerIdentityAttributesResource resource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(CustomerIdentityAttributesResource.class);

            result = resource.getCustomerIdentityAttributes(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Caught exception while getting identity attributes: ", e).log();
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting identity attributes: ", e).log();
        }

        return result;

    }

}

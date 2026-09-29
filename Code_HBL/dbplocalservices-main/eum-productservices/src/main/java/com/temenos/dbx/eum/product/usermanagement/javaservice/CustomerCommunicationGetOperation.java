package com.temenos.dbx.eum.product.usermanagement.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomerCommunicationGetOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerCommunicationResource;

public class CustomerCommunicationGetOperation implements JavaService2 {
    LoggerUtil logger = new LoggerUtil(CustomerCommunicationGetOperation.class);
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
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

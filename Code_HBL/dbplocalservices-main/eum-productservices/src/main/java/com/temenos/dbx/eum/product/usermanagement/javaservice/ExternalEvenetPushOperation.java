package com.temenos.dbx.eum.product.usermanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.javaservice.ExternalEvenetPushOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.PushExternalEventResource;

public class ExternalEvenetPushOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            PushExternalEventResource resource = DBPAPIAbstractFactoryImpl.getResource(PushExternalEventResource.class);
            result = resource.pushUserIdAndActivationCode(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            e.setError(result);
            alert.prepareError("Error occured while pushing the event" + e.getMessage()).log();
        } catch (Exception e) {
            alert.prepareError("Error occured while pushing the event" + e.getMessage()).log();
        }
        return result;
    }
}

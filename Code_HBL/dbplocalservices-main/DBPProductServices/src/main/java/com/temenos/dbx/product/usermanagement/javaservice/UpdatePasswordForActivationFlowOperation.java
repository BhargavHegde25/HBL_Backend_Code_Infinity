package com.temenos.dbx.product.usermanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.UserManagementResource;
import com.kony.dbp.exception.ApplicationException;

/**
 * @author sowmya.vandanapu
 * Activation code and User Name send operation for Enrollment
 *
 */
public class UpdatePasswordForActivationFlowOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            UserManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(UserManagementResource.class);
            result = resource.updatePasswordForActivationFlow(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while sending activation code and username" + e.getMessage()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending activation code and username" + e.getMessage()).log();
        }
        return result;
    }

}

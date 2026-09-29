package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.usermanagement.resource.api.InternalUserManagementResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

public class UpdateUserStatus implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	Result result = new Result();
		try {
			InternalUserManagementResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(InternalUserManagementResource.class);
			result = roleResource.updateUserStatus(methodID, inputArray, requestInstance, responseInstance);
		}
		catch (ApplicationException e) {
			alert.prepareError(" ApplicationException while updating user status", e).log();
			e.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Exception while updating user status", e).log();
			ErrorCodeEnum.ERR_20687.setErrorCode(result);			
		}
		return result;
    }
}
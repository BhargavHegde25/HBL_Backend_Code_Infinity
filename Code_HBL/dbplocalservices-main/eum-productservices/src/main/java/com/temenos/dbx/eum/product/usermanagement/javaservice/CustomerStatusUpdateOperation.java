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
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomerStatusUpdateOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.UserManagementResource;

public class CustomerStatusUpdateOperation implements JavaService2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	LoggerUtil logger = new LoggerUtil(CustomerStatusUpdateOperation.class);

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		logger = new LoggerUtil(CustomerStatusUpdateOperation.class);

		Result result = new Result();
		try {
			UserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
			result = userManagementResource.updateDBXUserStatus(methodID, inputArray, request, response);
		} catch (ApplicationException e) {
			alert.prepareError("Exception occured while updating customer user status", e).log();
			e.setError(result);
		} catch (Exception e) {
			alert.prepareError("Exception occured while updating customer user status", e).log();
		}

		return result;
	}

}

package com.temenos.dbx.eum.product.usermanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomRoleUpdateOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomRoleResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;

public class CustomRoleUpdateOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = null;
		try {
		    InfinityUserManagementResource customRoleResource = DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
			result = customRoleResource.editCustomRole(methodID, inputArray, request, response);
		}
		catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resource in CustomRoleUpdateOperation", exp).log();
		}

		return result;
	}

}

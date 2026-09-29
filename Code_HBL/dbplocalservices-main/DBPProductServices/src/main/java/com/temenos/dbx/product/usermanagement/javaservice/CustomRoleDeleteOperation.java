package com.temenos.dbx.product.usermanagement.javaservice;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.CustomRoleResource;

public class CustomRoleDeleteOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			return invokeOperation(methodID, inputArray, request, response);
		} finally {
			// getList cache: this operation changes data getList returns. Runs even after a part-way
			// failure, because some rows may already be written.
			GetListCacheInvalidator.permissionsChanged();
		}
	}

	private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = null;
		try {
			CustomRoleResource customRoleResource = DBPAPIAbstractFactoryImpl.getResource(CustomRoleResource.class);
			result = customRoleResource.deleteCustomRole(methodID, inputArray, request, response);
		}
		catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resource in CustomRoleDeleteOperation", exp).log();
		}
		return result;
	}
}
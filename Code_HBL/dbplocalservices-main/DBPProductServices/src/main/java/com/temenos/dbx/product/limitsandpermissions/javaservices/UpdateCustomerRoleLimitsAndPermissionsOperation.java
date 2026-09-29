package com.temenos.dbx.product.limitsandpermissions.javaservices;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.limitsandpermissions.resource.api.LimitsAndPermissionsResource;

public class UpdateCustomerRoleLimitsAndPermissionsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			return invokeOperation(methodId, inputArray, request, response);
		} finally {
			// getList cache: this operation changes data getList returns. Runs even after a part-way
			// failure, because some rows may already be written.
			GetListCacheInvalidator.permissionsChanged();
		}
	}

	private Object invokeOperation(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			LimitsAndPermissionsResource limitsAndPermissionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(LimitsAndPermissionsResource.class);
			result = limitsAndPermissionsResource.UpdateCustomerRoleLimitsAndPermissions(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of UpdateCustomerRoleLimitsAndPermissionsOperation: ", e).log();
			return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
		}
		return result;
	}

}
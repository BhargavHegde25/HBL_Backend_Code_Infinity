package com.temenos.dbx.product.usermanagement.javaservice;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CreateRetailContractOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

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
		try {
			InfinityUserManagementResource profileManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(InfinityUserManagementResource.class);
			return profileManagementResource.createRetailContract(methodID, inputArray, request, response);
		} catch (ApplicationException e) {
			Result result = new Result();
			e.setError(result);
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			return result;
		}
	}
}

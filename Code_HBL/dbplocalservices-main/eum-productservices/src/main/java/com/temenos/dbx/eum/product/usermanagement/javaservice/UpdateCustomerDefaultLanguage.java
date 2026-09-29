package com.temenos.dbx.eum.product.usermanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;

/**
 * 
 * @version
 * 
 */

public class UpdateCustomerDefaultLanguage implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(InfinityUserManagementResource.class);
			return infinityUserManagementResource.updateCustomerLanguage(methodID, inputArray, request, response);

		} catch (ApplicationException e2) {
			e2.setError(result);
		} catch (Exception e) {
			alert.prepareError("Error occured updating language", e).log();
			ErrorCodeEnum.ERR_10821.setErrorCode(result);
		}
		return result;
	}

}

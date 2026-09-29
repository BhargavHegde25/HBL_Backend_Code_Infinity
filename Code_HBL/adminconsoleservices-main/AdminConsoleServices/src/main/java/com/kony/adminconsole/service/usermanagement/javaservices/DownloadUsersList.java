package com.kony.adminconsole.service.usermanagement.javaservices;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.usermanagement.resource.api.InternalUserManagementResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DownloadUsersList implements JavaService2 {

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
			result = roleResource.downloadUsersList(methodID, inputArray, requestInstance, responseInstance);
		}
		catch (ApplicationException e) {
			alert.prepareError(" ApplicationException while downloading users list", e).log();
			e.getErrorCodeEnum().setErrorCode(result);
			CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());
		} catch (Exception e) {
			alert.prepareError("Exception while downloading users list", e).log();
			ErrorCodeEnum.ERR_20687.setErrorCode(result);

			String errorMessage = "Failed to download users list. Please contact administrator.";
			CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
		}
		return result;
	}

}
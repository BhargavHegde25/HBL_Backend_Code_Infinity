package com.kony.adminconsole.service.authmodule;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * Service to get configuration parameter for login type
 *
 * @author Sri Kavya Pitchika
 *
 */
public class LoginTypeGetService implements JavaService2 {


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result operationResult = new Result();
		try {
			String isKeyCloakEnabled=ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance);
			operationResult.addParam(new Param("isKeyCloakEnabled",	isKeyCloakEnabled, FabricConstants.BOOLEAN));
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.SEARCH,
					ActivityStatusEnum.SUCCESSFUL, "Get login type information passed");
		} catch (Exception e) {
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			alert.prepareError("Exception in fetching login type information passed.", e).log();
			ErrorCodeEnum.ERR_20890.setErrorCode(operationResult);
		}
		return operationResult;
	}
}
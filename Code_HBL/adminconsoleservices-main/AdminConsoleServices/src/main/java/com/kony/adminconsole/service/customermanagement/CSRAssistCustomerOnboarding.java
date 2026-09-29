package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.core.config.EnvironmentConfiguration;
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

public class CSRAssistCustomerOnboarding implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			Result processedResult = new Result();
			String alternatehostURL = EnvironmentConfiguration.AC_CREATE_CUSTOMER_HOST_URL.getValue(requestInstance);
			if(!StringUtils.isBlank(alternatehostURL)){
				processedResult.addParam(new Param("BankingURL", alternatehostURL));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.COMMUNICATION,
						ActivityStatusEnum.SUCCESSFUL, "CSR Assist to Customer Onboarding");
			}
			else {	
				String hostURL = EnvironmentConfiguration.AC_CSR_ASSIST_CO_HOST_URL.getValue(requestInstance);
				hostURL += "/apps/" + EnvironmentConfiguration.AC_APP_ID_ONBOARDING.getValue(requestInstance) + "/#_frmLanding" + "?Identifier=c360";
				processedResult.addParam(new Param("BankingURL", hostURL));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.COMMUNICATION,
						ActivityStatusEnum.SUCCESSFUL, "CSR Assist to Customer Onboarding");
			}
			return processedResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

}

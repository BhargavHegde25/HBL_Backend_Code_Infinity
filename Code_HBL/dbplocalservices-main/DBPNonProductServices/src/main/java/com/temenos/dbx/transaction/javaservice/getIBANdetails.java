package com.temenos.dbx.transaction.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class getIBANdetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		String serviceName;
		String operationName = "getIBANdetails";
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		
		try {
            String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND");
            serviceName = ("MOCK".equalsIgnoreCase(PAYMENT_BACKEND) || "SRMS_MOCK".equals(PAYMENT_BACKEND)|| "STUB".equals(PAYMENT_BACKEND)) ? "TransfersMockData" : "PaymentOrchestrationServices";
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			if(result.getErrMsgParamValue()!=null){
				return ErrorCodeEnum.ERR_12064.setErrorCode(new Result());
			}
			return result;
			
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

	}
}

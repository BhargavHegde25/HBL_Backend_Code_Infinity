package com.temenos.dbx.consents.javaservice;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrderForConsent implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String SRMS = "SRMS";

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			if (SRMS.equalsIgnoreCase(EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND"))) {
				HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
				HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
				String serviceName = "ServiceRequestJavaService";
				String operationName = "createOrder";
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			} else {
				GetConsentResponse getConsentResponse = new GetConsentResponse();
				Result res = (Result) getConsentResponse.invoke("methodID", inputArray, request, response);

				diagnostic.prepareDebug("GetConsentResponse ---> " + ResultToJSON.convert(res)).log();
			}
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception while invoking ServiceRequestJavaService:" + e).log();
			return errorResult;
		}
		return result;
	}
}

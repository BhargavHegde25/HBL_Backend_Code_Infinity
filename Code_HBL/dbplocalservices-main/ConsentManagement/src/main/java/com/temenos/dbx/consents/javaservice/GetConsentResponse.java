package com.temenos.dbx.consents.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetConsentResponse implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String STUB = "STUB";
	private static final String T24_BACKEND = "T24";

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String serviceName = new String();
		String operationName = new String();
		request.getHeaderMap().put("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
		LegalEntityUtil.addCompanyIDToHeaders(request);
		request.addRequestParam_("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
		params.put("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
		diagnostic.prepareDebug("input params in consent" + params).log();
		diagnostic.prepareDebug("request in consent" + request).log();
		try {
			Result result = new Result();
			String command;
			command = GetUrl.getURL(request.getParameter("type"));
			if (null != command) {
				String[] api = null;
				api = command.split("#");
				if (api != null && api.length > 1) {
					serviceName = api[0];
					operationName = api[1];
				}
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			} else {
				if ("CDPConsent".equalsIgnoreCase(request.getParameter("type"))) {
					
					if (T24_BACKEND.equalsIgnoreCase(
							EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND"))) {
						result = CommonUtils.callIntegrationService(request, params, serviceHeaders, "T24ISConsents",
								"updateCDPConsent", true);
					} else if (STUB.equalsIgnoreCase(
							EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND"))) {
						result = CommonUtils.callIntegrationService(request, params, serviceHeaders, "ConsentMock",
								"UpdateCDPMock", true);
					}
					
				} else if ("PSD2Consent".equalsIgnoreCase(request.getParameter("type"))) {

					if (T24_BACKEND.equalsIgnoreCase(
							EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND"))) {
						result = CommonUtils.callIntegrationService(request, params, serviceHeaders, "T24ISConsents",
								"updatePSDConsent", true);
					} else if (STUB.equalsIgnoreCase(
							EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND"))) {
						result = CommonUtils.callIntegrationService(request, params, serviceHeaders, "ConsentMock",
								"UpdatePSDMock", true);
					}
					
				}

			}
			return result;
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
		}
	}
}

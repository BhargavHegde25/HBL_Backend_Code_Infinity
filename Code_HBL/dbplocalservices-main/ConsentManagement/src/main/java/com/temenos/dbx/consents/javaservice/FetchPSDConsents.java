package com.temenos.dbx.consents.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FetchPSDConsents implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String serviceName = new String();
			String operationName = new String();
			String type="PSD";
			request.getHeaderMap().put("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
			LegalEntityUtil.addCompanyIDToHeaders(request);
			request.addRequestParam_("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
			params.put("legalEntityId", LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
			diagnostic.prepareDebug("input params in psd2"+params).log();
			diagnostic.prepareDebug("request in psd2"+request).log();
//			String serviceName = "T24ISConsents";
//			String operationName = "getPSDConsent";
			String command;
			command=GetUrl.getURL(type);
	   		  String[] api = null;
				api = command.split("#");
					if (api != null && api.length > 1) {
						serviceName = api[0];
						operationName = api[1];
					}
			
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while fetching PSD Consents:" + e).log();
			return errorResult;
		}
		return result;
	}
}

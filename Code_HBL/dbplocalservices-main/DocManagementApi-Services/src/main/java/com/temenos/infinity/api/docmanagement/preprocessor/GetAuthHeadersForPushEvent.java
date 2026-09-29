package com.temenos.infinity.api.docmanagement.preprocessor;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAuthHeadersForPushEvent implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap requestParams, DataControllerRequest request, DataControllerResponse response,
			Result resultObj) throws Exception {
		Log4j2Configurator.getInstance();
		diagnostic.prepareInfo("PUsh event preprocessor").log();
		boolean result = true;
		String claimsToken = null;
		Map<String, Object> headerMap = request.getHeaderMap();
		String eventMangerAppkey = EnvironmentConfigurationsHandler.getValue("EVENT_MANAGER_APP_KEY", request);
		String eventMangerAppSecret = EnvironmentConfigurationsHandler.getValue("EVENT_MANAGER_APP_SECRET", request);

		if (StringUtils.isBlank(eventMangerAppkey) || StringUtils.isBlank(eventMangerAppSecret)) {
			alert.prepareError("Error while fetching EVENT_MANAGER_APP_KEY or EVENT_MANAGER_APP_SECRET").log();
		}
		Map<String, Object> data = request.getHeaderMap();
		data.put("X-Kony-App-Key", eventMangerAppkey);
		data.put("X-Kony-App-Secret", eventMangerAppSecret);
		try {
			Result loginResult = HelperMethods.callApi(request, data, HelperMethods.getHeaders(request),
					URLConstants.PUSH_EVENT_LOGIN);
			claimsToken = loginResult.getParamValueByName("claimsToken");
			if (StringUtils.isBlank(claimsToken)) {
				alert.prepareError("Error in generating claims token.Push event Identity service failed").log();
			}
		} catch (HttpCallException e) {
			alert.prepareError("Error while generating anonymous claims token").log();
		}
		headerMap.put("X-Kony-Authorization", claimsToken);
		headerMap.remove("x-kony-authorization");
		return true;
	}
}

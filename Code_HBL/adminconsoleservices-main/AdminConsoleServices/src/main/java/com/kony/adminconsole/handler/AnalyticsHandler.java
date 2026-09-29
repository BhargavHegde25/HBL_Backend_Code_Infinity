package com.kony.adminconsole.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.jwt.auth.AnalyticsSpotlight;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AnalyticsHandler implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {
			AnalyticsSpotlight authentication = AnalyticsSpotlight.getInstance();
			Map<String, String> configurationsTableMap = new HashMap<>();
			String bundleId = "C360_CONFIG_BUNDLE";

			if (bundleId != null && !bundleId.equals("")) {
				configurationsTableMap.put(ODataQueryConstants.FILTER, "bundle_id eq '" + bundleId + "'");
			}
			String readConfigurationsResponse = Executor.invokeService(ServiceURLEnum.CONFIGURATIONS_READ,
					configurationsTableMap, null, requestInstance);

			JSONObject valueResponseJSON = new JSONObject(readConfigurationsResponse);
			JSONObject params = getRequiredParams(valueResponseJSON);
			String authToken = authentication.getAuthToken(requestInstance, params);
			Param param = new Param();
			param.setName("JWTToken");
			param.setValue(authToken);
			processedResult.addParam(param);
			if (StringUtils.isBlank(authToken)) {
				alert.prepareError("Error - JWT authToken generated for AnalyticsPostProcessor is empty").log();
			}
			diagnostic.prepareInfo("Auth Token generated from AnalyticsPostProcessor" + authToken).log();

		} catch (Exception e) {
			alert.prepareError("Error occured in AnalyticsPostProcessor" + e).log();
		}
		return processedResult;
	}

	public JSONObject getRequiredParams(JSONObject configurations) {
		JSONObject requiredParams = new JSONObject();
		JSONArray configJsonArray = null;
		if (configurations.has("configurations")) {
			configJsonArray = configurations.getJSONArray("configurations");
			for (int i = 0; i < configJsonArray.length(); i++) {
				String key = configJsonArray.getJSONObject(i).getString("config_key");
				if (key.equalsIgnoreCase("LICENSEGUID"))
					requiredParams.put(configJsonArray.getJSONObject(i).getString("config_key"),
							configJsonArray.getJSONObject(i).getString("config_value"));
				if (key.equalsIgnoreCase("USER_ORG_IDS"))
					requiredParams.put(configJsonArray.getJSONObject(i).getString("config_key"),
							configJsonArray.getJSONObject(i).getString("config_value"));
				if (key.equalsIgnoreCase("ADMIN"))
					requiredParams.put(configJsonArray.getJSONObject(i).getString("config_key"),
							configJsonArray.getJSONObject(i).getString("config_value"));
			}
		}
		return requiredParams;
	}
}

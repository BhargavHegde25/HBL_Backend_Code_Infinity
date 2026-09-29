package com.temenos.infinity.api.wealthservices.preandpostprocessors;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Generates Refinitiv Token for the services. Needs to be included for
 * all refinitiv services.
 * 
 * @author himaja.sridhar
 *
 */
public class RefinitivTokenGenPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String mkt_applicationID = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID, request)
								.toString().trim()
						: "");
		String mkt_username = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER, request)
								.toString().trim()
						: "");
		String mkt_password = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD, request)
								.toString().trim()
						: "");
		if (mkt_applicationID.equals("") || mkt_username.equals("") || mkt_password.equals("")) {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			return false;
			
		} else {
			Map<String, Object> tokenRequest = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();
			tokenRequest.put("ApplicationID", mkt_applicationID);
			tokenRequest.put("Username", mkt_username);
			tokenRequest.put("Password", mkt_password);
			try {
				String refinitivToken = Executor.invokeService(PortfolioWealthAPIServices.WEALTH_GETREFINITIVTOKEN, tokenRequest,
						headerMap);
				JSONObject refinitivJSON = Utilities.convertStringToJSON(refinitivToken);
				request.addRequestParam_("X-Trkd-Auth-Token", (String) refinitivJSON.get("Token"));
				request.addRequestParam_("X-Trkd-Auth-ApplicationID", mkt_applicationID);
				return true;
			} catch (Exception e) {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
			}
			return false;
		}
	}
}
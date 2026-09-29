package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public class GetConfigurations implements JavaService2 {


	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		JSONObject getResponse = getSpotLightSystemConfigurations(request);

		if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
			String authToken = AdminConsoleOperations.login(request);
			ServiceConfig.setValue("Auth_Token", authToken);
			getResponse = getSpotLightSystemConfigurations(request);
		}
		Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
		return processedResult;
	}

	private JSONObject getSpotLightSystemConfigurations(DataControllerRequest request) {
		// TODO Auto-generated method stub

		Map<String, Object> postParametersMap = new HashMap<>();

		postParametersMap.put("bundle_name", request.getParameter("bundle_name"));

		String getResponseString = HelperMethods.invokeC360ServiceAndGetString(request, postParametersMap,
				new HashMap<>(), "getConfigurations");

		return CommonUtilities.getStringAsJSONObject(getResponseString);
	}

}

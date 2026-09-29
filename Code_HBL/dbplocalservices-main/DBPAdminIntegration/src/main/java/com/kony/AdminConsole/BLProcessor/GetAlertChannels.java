package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetAlertChannels implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {			
			JSONObject getResponse = getAlertChannels(requestInstance);
			if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
				String authToken = AdminConsoleOperations.login(requestInstance);
				ServiceConfig.setValue("Auth_Token", authToken);
				getResponse = getAlertChannels(requestInstance);
			}
			return HelperMethods.constructResultFromJSONObject(getResponse);
		} catch(Exception e) {
			return getErrorResult(e);
		}		
	}

	private static Result getErrorResult(Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		return getErrorResult(9001, errorMsg);
	}

	private static Result getErrorResult(int errorNumber, String errorMsg) {
		Result result = new Result();
		result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
		result.addParam(new Param("errormsg", errorMsg));
		result.addParam(new Param("success", "false"));
		return result;
	}

	public JSONObject getAlertChannels(DataControllerRequest dcRequest) {

		HashMap<String, Object> customHeaderParameters = new HashMap<>();
		customHeaderParameters.put("Accept-Language", dcRequest.getHeader("Accept-Language"));
		customHeaderParameters.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

		String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, null,
				customHeaderParameters, "getAlertChannels");
		return CommonUtilities.getStringAsJSONObject(getResponseString);
	}

}

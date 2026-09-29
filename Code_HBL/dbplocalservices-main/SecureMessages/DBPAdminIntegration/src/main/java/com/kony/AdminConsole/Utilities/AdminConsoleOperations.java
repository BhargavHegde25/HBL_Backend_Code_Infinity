package com.kony.AdminConsole.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class AdminConsoleOperations {

    private static final String AC_ACCESS_BY_IDENTIFIER = "OLB";
    private static final String AC_ACCESS_BY_IDENTIFIER_KEY = "X-Kony-AC-API-Access-By";

    private static final String BACKEND_TOKEN_KEY = "backendToken";

    private static final String CLAIMS_TOKEN_KEY = "claims_token";
    private static final String AC_APP_KEY_KEY = "AC-X-Kony-App-Key";
    private static final String AC_APP_SECRET_KEY = "AC-X-Kony-App-Secret";
    private static final String API_ACCESS_TOKEN_KEY = "X-Kony-AC-API-Access-Token";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static String login(DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, Object> headersMap = new HashMap<>();
        headersMap.put(API_ACCESS_TOKEN_KEY, ServiceConfig.getValueFromRunTime(URLConstants.ACCESS_TOKEN, dcRequest));
        headersMap.put(AC_APP_KEY_KEY, ServiceConfig.getValueFromRunTime(URLConstants.APP_KEY, dcRequest));
        headersMap.put(AC_APP_SECRET_KEY, ServiceConfig.getValueFromRunTime(URLConstants.APP_SECRET, dcRequest));
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(dcRequest, null, headersMap,
                URLConstants.ADMINCONSOLE_LOGINURL);
        if (response == null || !response.has(CLAIMS_TOKEN_KEY)
                || StringUtils.isBlank(response.get(CLAIMS_TOKEN_KEY).getAsString())) {
            String serviceResponse = response == null ? StringUtils.EMPTY : response.getAsString();
            alert.prepareError("Failed to fetch API Auth Token from Customer360. Service Response:" + serviceResponse).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10000);
        }
        return response.get(CLAIMS_TOKEN_KEY).getAsString();
    }

    public static JSONObject invokeAPI(Map<String, Object> inputParams, String URL, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, Object> headerMap = new HashMap<>();

        login(dcRequest);
        headerMap.put(AC_ACCESS_BY_IDENTIFIER_KEY, AC_ACCESS_BY_IDENTIFIER);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, inputParams, headerMap, URL);
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

    public static Result invokeApi(HashMap<String, Object> inputParams, String URL, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, Object> headerMap = new HashMap<>();

        String authToken = login(dcRequest);
        headerMap.put(BACKEND_TOKEN_KEY, authToken);
        headerMap.put(AC_ACCESS_BY_IDENTIFIER_KEY, AC_ACCESS_BY_IDENTIFIER);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, inputParams, headerMap, URL);
        return ConvertJsonToResult.convert(getResponseString);
    }
}

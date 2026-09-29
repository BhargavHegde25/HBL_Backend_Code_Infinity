package com.kony.AdminConsole.BLProcessor;

import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.HTTPOperations;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class CreateNewCustomerRequest implements JavaService2 {

    @SuppressWarnings("deprecation")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        String userName = null;
        HttpServletRequest request = (HttpServletRequest) requestInstance.getOriginalRequest();
        request.getSession(false);
        InputStream inputStream = request.getInputStream();
        String contentType = request.getContentType();
        String olbClaimsToken = request.getHeader("X-Kony-Authorization");
        String host = requestInstance.getHeader("host");
        if (StringUtils.isBlank(host)) {
            host = requestInstance.getHeader("Host");
        }
        HTTPOperations httpOperationInstance = new HTTPOperations();
        HashMap<String, String> postParamMap = new HashMap<>();
        String userAttributesResponse = httpOperationInstance.hitPOSTServiceAndGetResponse(
                "https://" + host + "/services/UserAttributes/getUserAttributes", postParamMap,
                "application/x-www-form-urlencoded", olbClaimsToken, null);
        JSONObject userJSONObject = CommonUtilities.getStringAsJSONObject(userAttributesResponse);
        userName = userJSONObject.getString("username");
        return HelperMethods.constructResultFromJSONObject(
                createNewRequestMessage(inputStream, contentType, userName, requestInstance));

    }

    public JSONObject createNewRequestMessage(InputStream inputStream, String contentType, String username,
            DataControllerRequest dcRequest) {
        String getRequestsURL = ServiceConfig.getValueFromRunTime(URLConstants.HOST_URL, dcRequest)
                + ServiceConfig.getValue("createCustomerRequest");
        String authToken = ServiceConfig.getValue("Auth_Token");
        HTTPOperations httpOperationInstance = new HTTPOperations();
        String getResponseString = httpOperationInstance.hitPOSTStreamServiceAndGetResponse(getRequestsURL,
                inputStream, contentType, authToken, null, username);
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
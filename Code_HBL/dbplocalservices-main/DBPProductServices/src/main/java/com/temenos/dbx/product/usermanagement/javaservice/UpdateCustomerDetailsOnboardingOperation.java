package com.temenos.dbx.product.usermanagement.javaservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.dbx.product.utils.InfinityConstants;

public class UpdateCustomerDetailsOnboardingOperation implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);

        JsonObject resultObject = new JsonObject();
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                Map<String, Object> inputMap = HelperMethods.getInputParamObjectMap(inputArray);
                inputMap.putAll(getRequestPayloadInMap(request));
                JsonObject updatedFullUserDetailsResult = updateFullUserDetails(inputMap, request.getHeaderMap());
                JsonObject updatedCustomerDetailsResult = updateCustomerDetails(inputMap, request);
                JsonObject updateUserDetailsResult = updateUserDetails(inputMap, request.getHeaderMap());

                updateinResultJson(resultObject, updatedFullUserDetailsResult);
                updateinResultJson(resultObject, updatedCustomerDetailsResult);
                updateinResultJson(resultObject, updateUserDetailsResult);

            }
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10415.setErrorCode(resultObject);
        }
        return JSONToResult.convert(resultObject.toString());
    }

    private Map<String, Object> getRequestPayloadInMap(DataControllerRequest dcRequest) {

        Map<String, Object> inputmap = new HashMap<>();
        Iterator<String> params = dcRequest.getParameterNames();
        while (params.hasNext()) {
            String key = params.next();
            inputmap.put(key, dcRequest.getParameter(key));
        }
        return inputmap;
    }

    private void updateinResultJson(JsonObject resultJson, JsonObject jsonToBeAdded) {
        if (null != resultJson && null != jsonToBeAdded) {

            for (Entry<String, JsonElement> entry : jsonToBeAdded.entrySet()) {
                resultJson.add(entry.getKey(), entry.getValue());
            }

        }

    }

    private JsonObject updateUserDetails(Map<String, Object> inputMap, Map<String, Object> headerMap) {
        String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
        String operationName = OperationName.UPDATE_USER_DETAILS;
        return ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                inputMap, headerMap, headerMap.get(InfinityConstants.x_kony_authorization).toString());
    }

    private JsonObject updateCustomerDetails(Map<String, Object> inputMap, DataControllerRequest dcRequest)
            throws HttpCallException {
        return ServiceCallHelper.invokeServiceAndGetJson(dcRequest, inputMap, dcRequest.getHeaderMap(),
                URLConstants.UPDATE_CUSTOMERDETAILS_SERVICE);
    }

    private JsonObject updateFullUserDetails(Map<String, Object> inputMap, Map<String, Object> headerMap) {
        String serviceId = ServiceId.T24ISEXTRA_INTEGRATION_SERVICE;
        String operationName = OperationName.UPDATE_FULL_USER_DETAILS;
        return ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                inputMap, headerMap, headerMap.get(InfinityConstants.x_kony_authorization).toString());
    }

}

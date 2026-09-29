package com.temenos.dbx.product.javaservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCustomerDetailsIntegrated implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            inputParams.putAll(getRequestPayloadInMap(dcRequest));

            Result res = updateUserDetails(inputParams, dcRequest.getHeaderMap());
            result.addAllDatasets(res.getAllDatasets());
        }
        return result;

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

    private Result updateUserDetails(Map<String, Object> inputParams, Map<String, Object> headerMap) {

        String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
        String operationName = OperationName.UPDATE_USER_DETAILS;
        String authToken = headerMap.containsKey(DBPUtilitiesConstants.X_KONY_AUTHORIZATION)
                ? headerMap.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION).toString()
                : headerMap.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE).toString();
        JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                inputParams, headerMap, authToken);

        return JSONToResult.convert(jsonobject.toString());

    }
}

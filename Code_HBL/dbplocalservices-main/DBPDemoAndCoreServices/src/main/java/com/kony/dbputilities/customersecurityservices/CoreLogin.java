package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CoreLogin implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("x-kony-app-key", URLFinder.getPathUrl(URLConstants.CORE_APPKEY, dcRequest));
        headerMap.put("x-kony-app-secret", URLFinder.getPathUrl(URLConstants.CORE_SECRET, dcRequest));
        headerMap.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        String url = URLFinder.getPathUrl(URLConstants.CORE_LOGIN, dcRequest);

        if (StringUtils.isNotBlank(url)) {
            result = HelperMethods.callExternalApi(inputParams, headerMap, url);
        }

        return result;
    }

}
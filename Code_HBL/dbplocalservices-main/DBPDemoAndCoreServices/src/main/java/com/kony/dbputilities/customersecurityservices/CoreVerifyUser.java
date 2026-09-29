package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CoreVerifyUser implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.CORE_ATTR);
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> inputParams1 = new HashMap<>();

        for (String key : inputParams.keySet()) {
            inputParams1.put(key.toLowerCase(), inputParams.get(key));
        }

        String url = URLFinder.getPathUrl(URLConstants.CORE_VERIFY_USER, dcRequest);
        if (StringUtils.isNotBlank(url)) {
            result = HelperMethods.callExternalApi(inputParams1, headerMap, url);
        } else {
            HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.BACKEND_IDENTIFIER_NOT_FOUND,
                    ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, record);
            result.addRecord(record);
        }

        return result;
    }

}
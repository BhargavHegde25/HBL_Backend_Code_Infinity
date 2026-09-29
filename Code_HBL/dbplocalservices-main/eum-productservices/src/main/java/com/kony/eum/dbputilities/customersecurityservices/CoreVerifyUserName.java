package com.kony.eum.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CoreVerifyUserName implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.CORE_ATTR);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> inputParams1 = new HashMap<>();

        inputParams1.put("userName", inputParams.get("UserName"));
        inputParams1.put("email", inputParams.get("Email"));
        inputParams1.put("phone", inputParams.get("Phone"));

        String url = URLFinder.getPathUrl(URLConstants.CORE_VERIFY_USERNAME, dcRequest);

        if (StringUtils.isNotBlank(url)) {
            result = HelperMethods.callExternalApi(inputParams1, headerMap, url);
        } else {
            ErrorCodeEnum.ERR_10004.setErrorCode(record);
            result.addRecord(record);
        }
        return result;
    }

}
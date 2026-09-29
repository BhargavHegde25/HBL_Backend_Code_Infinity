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

public class CreateCoreCustomer implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, dcResponse, result)) {
            String url = inputParams.get("url");
            inputParams.remove("url");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

            result = HelperMethods.callExternalApi(inputParams, headerMap, url);

            result = postProcess(result);
        }

        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, Result result) {

        boolean status = true;
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.CORE_ATTR);

        if (StringUtils.isBlank(inputParams.get("userName"))) {
            HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.PROVIDE_USERNAME,
                    ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
            result.addRecord(record);
            return false;
        }

        checkParams(inputParams);

        String url = URLFinder.getPathUrl(URLConstants.CORE_CUSTOMER_CREATE, dcRequest);

        if (StringUtils.isBlank(url)) {
            HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.BACKEND_IDENTIFIER_NOT_FOUND,
                    ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
            result.addRecord(record);
            return false;
        }

        inputParams.put("url", url);

        String dob = inputParams.get(DBPUtilitiesConstants.DOB);
        if (!StringUtils.isNotBlank(dob)) {
            inputParams.remove(DBPUtilitiesConstants.DOB);
        }
        return status;
    }

    private Result postProcess(Result result) {
        Result retResult = new Result();
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.CORE_ATTR);
        if (!HelperMethods.hasError(result)) {
            HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.SUCCESS_MSG, ErrorCodes.RECORD_CREATED, record);
            record.addParam(result.getParamByName("Id"));
        } else {
            HelperMethods.setValidationMsgwithCode(result.getParamValueByName(DBPUtilitiesConstants.VALIDATION_ERROR),
                    ErrorCodes.ERROR_CREATING_RECORD, record);
        }

        retResult.addRecord(record);
        return retResult;
    }

    private void checkParams(Map<String, String> inputParams) {
        String date = "";

        date = inputParams.get("dateOfBirth");
        if (StringUtils.isBlank(date)) {
            inputParams.remove("dateOfBirth");
        }
    }
}
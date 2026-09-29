package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CoreUpdateUser implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> headerMap = HelperMethods.getHeaders(dcRequest);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        String identifierType = inputParams.get(DBPUtilitiesConstants.IDENTIFIER_TYPE);
        if (StringUtils.isBlank(identifierType)) {
            identifierType = DBPUtilitiesConstants.COREBANKING_USER_UPDATE;
        }
        String filter = DBPUtilitiesConstants.IDENTIFIER_TYPE + DBPUtilitiesConstants.EQUAL + identifierType;
        result = HelperMethods.callGetApi(dcRequest, filter, headerMap, URLConstants.BACKENDIDENTIFIER_GET);
        String urlupdate = HelperMethods.getFieldValue(result, "URL");

        String id = getCoreUserId(dcRequest);
        if (!StringUtils.isNotBlank(id)) {
            Result retResult = new Result();
            ErrorCodeEnum.ERR_10028.setErrorCode(retResult);
            return retResult;
        }
        inputParams.put("Id", id);
        inputParams.put("identifierType", null);
        updateField("Password", inputParams, "password");
        updateField("Email", inputParams, "email");
        updateField("Phone", inputParams, "phone");
        updateField("Ssn", inputParams, "ssn");
        updateField("UserImage", inputParams, "userImage");
        updateField("Pin", inputParams, "pin");
        HelperMethods.removeNullValues(inputParams);
        result = HelperMethods.callExternalApi(inputParams, headerMap, urlupdate);
        result = postProcess(result);
        return result;
    }

    private Result postProcess(Result result) {

        Result retResult = new Result();

        Record coreAttr = new Record();
        coreAttr.setId(DBPUtilitiesConstants.CORE_ATTR);

        if (HelperMethods.hasRecords(result)) {
            HelperMethods.setSuccessMsg("Update success. ", coreAttr);
        } else if (HelperMethods.hasError(result)) {
            ErrorCodeEnum.ERR_10028.setErrorCode(result);
        }
        retResult.addRecord(coreAttr);
        return retResult;
    }

    private void updateField(String in, Map<String, String> parms, String out) {
        String userImage = parms.get(in);
        if (StringUtils.isNotBlank(userImage)) {
            parms.put(out, userImage);
            parms.put(in, null);
        }
    }

    private String getCoreUserId(DataControllerRequest dcRequest) {
        Map<String, String> headerMap = HelperMethods.getHeaders(dcRequest);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        Map<String, String> customerInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String userName = customerInfo.get("UserName");
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        try {
            Result result = HelperMethods.callGetApi(dcRequest, filter, headerMap, URLConstants.USER_GET);
            return HelperMethods.getFieldValue(result, "id");
        } catch (HttpCallException e) {
            return new String("");
        }
    }
}
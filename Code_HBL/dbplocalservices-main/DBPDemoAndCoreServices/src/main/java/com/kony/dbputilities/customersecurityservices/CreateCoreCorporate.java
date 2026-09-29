package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateCoreCorporate implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

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

        StringBuilder sb = new StringBuilder();
        sb.append("IDMKey").append(DBPUtilitiesConstants.EQUAL).append("Corporate");

        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        Result idmConfigResult;
        try {
            idmConfigResult = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.IDMCONFIGURATION_GET);
        } catch (HttpCallException e) {
            idmConfigResult = new Result();
            alert.prepareError(e.getMessage()).log();
            ErrorCodeEnum.ERR_10155.setErrorCode(record, ErrorConstants.ERR_IN_CREATING_RECORD);
            result.addRecord(record);
            return false;
        }

        String value = HelperMethods.getFieldValue(idmConfigResult, "IDMValue");

        if (!value.equals(DBPUtilitiesConstants.EXTERNALBANK) && !value.isEmpty()) {
            record.addParam(new Param("message", "externalBankingservice Call is skipped ", "String"));
            result.addRecord(record);
            return false;
        }

        if (StringUtils.isBlank(inputParams.get("userName"))) {
            ErrorCodeEnum.ERR_10069.setErrorCode(record);
            result.addRecord(record);
            return false;
        }

        checkParams(inputParams);
        String url = URLFinder.getPathUrl(URLConstants.CORE_CORPORATE_CREATE, dcRequest);

        if (StringUtils.isBlank(url)) {
            ErrorCodeEnum.ERR_10070.setErrorCode(record);
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
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, record);
            record.addParam(new Param("id", result.getParamValueByName("Id"), "String"));
        } else {
            ErrorCodeEnum.ERR_10071.setErrorCode(record, HelperMethods.getError(result));
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
package com.kony.dbputilities.customersecurityservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserDetailsConcurrent implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        SessionScope.clear(dcRequest);

        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);
        String Username = inputmap.get("Username");
        String Customer_id = inputmap.get("Customer_id");
        String Bank_id = inputmap.get("Bank_id");
        String deviceId = null;

        if (StringUtils.isBlank(Username) || StringUtils.isBlank(Customer_id)) {
            Map<String, String> userDetails = HelperMethods.getUserFromIdentityService(dcRequest);
            Username = userDetails.get("UserName");
            Customer_id = userDetails.get("Customer_id");
        }

        if (StringUtils.isBlank(Username)) {
            ErrorCodeEnum.ERR_10003.setErrorCode(result);
        }

        deviceId = getDeviceId(dcRequest);

        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("Username", Username);
        inputParams.put("Customer_id", Customer_id);
        inputParams.put("Bank_id", Bank_id);
        inputParams.put("deviceId", deviceId);
        try {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USEROBJECT_ORCHESTRATION_GET);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        Result finalResult = orchestrationResultProcess(result);
        return finalResult;
    }

    private Result orchestrationResultProcess(Result result) {
        Result finalResult = new Result();
        Dataset customerDataset = new Dataset();
        customerDataset.setId("customer");
        customerDataset = result.getDatasetById("customer");

        finalResult.addDataset(customerDataset);
        Record user = HelperMethods.getRecord(finalResult);

        if (user.getNameOfAllParams().contains("LastName")) {
            user.addParam("userlastname", user.getParamValueByName("LastName"));
        }

        if (user.getNameOfAllParams().contains("Ssn")) {
            user.addParam("ssn", user.getParamValueByName("Ssn"));
        }

        if (user.getNameOfAllParams().contains("DateOfBirth")) {
            user.addParam("dateOfBirth", user.getParamValueByName("DateOfBirth"));
        }

        addParamsFromRecord(result.getRecordById("CustomerPreferences"), finalResult);
        addParamToResult(result.getParamByName("bankName"), finalResult);
        addParamToResult(result.getParamByName("feedbackUserId"), finalResult);
        addParamToResult(result.getParamByName("isSecurityQuestionConfigured"), finalResult);
        addCurrencyCode(finalResult);
        addDatasetToResult(result.getDatasetById("ContactNumbers"), finalResult);
        addDatasetToResult(result.getDatasetById("EmailIds"), finalResult);
        addDatasetToResult(result.getDatasetById("Addresses"), finalResult);

        return finalResult;
    }

    private void addDatasetToResult(Dataset dataset, Result finalResult) {
        if (HelperMethods.hasRecords(finalResult) && dataset != null) {
            finalResult.getAllDatasets().get(0).getRecord(0).addDataset(dataset);
        }

    }

    private void addParamsFromRecord(Record rec, Result finalResult) {
        if (rec == null) {
            return;
        }
        try {
            Iterator<Param> iterator = rec.getAllParams().iterator();
            Dataset ds = finalResult.getAllDatasets().get(0);
            while (iterator.hasNext()) {
                ds.getRecord(0).addParam(iterator.next());
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

    }

    private void addParamToResult(Param p, Result finalResult) {
        if (p != null) {
            finalResult.getAllDatasets().get(0).getRecord(0).addParam(p);
        }
    }

    private String getDeviceId(DataControllerRequest dcRequest) {
        String deviceId = null;
        String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
        if (StringUtils.isNotBlank(reportingParams)) {
            JSONObject reportingParamsJson = null;
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
            } catch (JSONException | UnsupportedEncodingException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (null != reportingParamsJson) {
                deviceId = reportingParamsJson.optString("did");
            }
        }
        return deviceId;
    }

    private void addCurrencyCode(Result finalResult) {
        Dataset ds = finalResult.getAllDatasets().get(0);
        String country = HelperMethods.getParamValue(ds.getRecord(0).getParam("CountryCode"));
        Param currrencyCode = new Param(DBPUtilitiesConstants.CURRENCYCODE, HelperMethods.getCurrencyCode(country),
                "String");
        ds.getRecord(0).addParam(currrencyCode);
        try {
            ds.getRecord(0).addParam(new Param("lastlogintime", HelperMethods.convertDateFormat(
                    HelperMethods.getFieldValue(finalResult, "lastlogintime"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            ds.getRecord(0).addParam(new Param("Lastlogintime", HelperMethods.convertDateFormat(
                    HelperMethods.getFieldValue(finalResult, "lastlogintime"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
        } catch (ParseException e) {
            alert.prepareError(e.getMessage()).log();
        }

    }
}
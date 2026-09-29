package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MetricsException;

public class CreateCoreUser implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_CREATE);
        }
        if (!HelperMethods.hasError(result)) {
            result = postProcess(dcRequest, inputParams, result);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private Result postProcess(DataControllerRequest dcRequest, Map inputParams, Result result)
            throws HttpCallException, MetricsException, ParseException {
        Param userId = getUserId(result);
        if (!HelperMethods.hasError(result)) {
            result = getSuccessResult();
            if (userId != null) {
                result.addParam(userId);
            }
        }
        return result;
    }

    private Param getUserId(Result result) {
        Param id = null;
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            id = ds.getRecord(0).getParam(DBPUtilitiesConstants.U_ID);
        }
        return id;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String username = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME);
        String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
        if (!StringUtils.isNotBlank(username) || !StringUtils.isNotBlank(password)) {
            HelperMethods.setValidationMsg(DBPUtilitiesConstants.PROVIDE_USERNAME_AND_PASSWORD, dcRequest, result);
            status = false;
        }
        if (status) {
            String role = (String) inputParams.get(DBPUtilitiesConstants.ROLE);
            if (!StringUtils.isNotBlank(role)) {
                inputParams.put(DBPUtilitiesConstants.ROLE, "BASIC");
            }
            String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);
            if (!StringUtils.isNotBlank(dob)) {
                inputParams.remove(dob);
            }
            inputParams.put(DBPUtilitiesConstants.IS_ENROLLED, "true");
            inputParams.put("passWord", password);

            String date = "";

            date = (String) inputParams.get("dateOfBirth");
            if (StringUtils.isBlank(date)) {
                inputParams.remove("dateOfBirth");
            }
        }
        return status;
    }

    private Result getSuccessResult() {
        Result result = new Result();
        Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, DBPUtilitiesConstants.SUCCESS_MSG, "String");
        result.addParam(p);
        return result;
    }

}
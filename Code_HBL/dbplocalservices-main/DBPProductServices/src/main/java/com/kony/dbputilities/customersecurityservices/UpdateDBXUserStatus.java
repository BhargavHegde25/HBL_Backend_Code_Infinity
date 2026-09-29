package com.kony.dbputilities.customersecurityservices;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateDBXUserStatus implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            checkStatusIfActive(dcRequest, inputParams);
            putStatusId(inputParams);
            HelperMethods.removeNullValues(inputParams);
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_UPDATE);
        }
        return postProcess(result, inputParams);

    }

    private void checkStatusIfActive(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {

        String status = inputParams.get("Status");
        if ("ACTIVE".equalsIgnoreCase(status)) {

            String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + inputParams.get("UserName") + "'";

            Result result = new Result();

            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);

            if (StringUtils.isBlank(HelperMethods.getFieldValue(result, "Password"))) {

                inputParams.put("Status", "NEW");
            }
            return;

        }
        return;

    }

    private void putStatusId(Map<String, String> inputParams) throws HttpCallException {
        String status = inputParams.get("Status");
        Map<String, String> map = HelperMethods.getCustomerStatus();
        if (map.containsKey(status)) {
            inputParams.put("Status_id", map.get(status));
        }

    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws Exception {
        String userName = inputParams.get("UserName");
        boolean check = true;
        String id = null;
        String orgIdOfUserUnderUpdate = null;

        Result userRec = new Result();
        if (StringUtils.isNotBlank(userName)) {
            userRec = HelperMethods.getUserRecordByName(userName, dcRequest);
            id = HelperMethods.getFieldValue(userRec, "id");
            orgIdOfUserUnderUpdate = HelperMethods.getFieldValue(userRec, "Organization_Id");
        }

        if (!StringUtils.isBlank(id)) {
            inputParams.put("id", id);
            check = true;
        }

        if (("ACTIVE".equalsIgnoreCase(inputParams.get("Status")))
                || ("SUSPENDED".equalsIgnoreCase(inputParams.get("Status")))) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    private Result postProcess(Result result, Map<String, String> inputParams) {

        Result retResult = new Result();

        if (HelperMethods.hasRecords(result)) {
            // Param p = new Param("errorCode", "3400", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);

            Param p = new Param("Status", inputParams.get("Status"), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retResult.addParam(p);

            p = new Param("success", "User Status updated.", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retResult.addParam(p);
        } else if (HelperMethods.hasError(result)) {
            // Param p = new Param("errorCode", "3401", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);
            // p = new Param("errorMessage",HelperMethods.getError(result),DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            // retResult.addParam(p);
            ErrorCodeEnum.ERR_10017.setErrorCode(retResult, HelperMethods.getError(result));
        } else {
            // Param p = new Param("errorCode", "3402", DBPUtilitiesConstants.STRING_TYPE);
            // retResult.addParam(p);
            // p = new Param("success","Records doesn't exists",DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            // retResult.addParam(p);
            //
            // HelperMethods.setValidationMsg(HelperMethods.getError(result),retResult);
            ErrorCodeEnum.ERR_10018.setErrorCode(retResult);
        }

        return retResult;
    }

}
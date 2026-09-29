package com.kony.dbputilities.customersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.organisation.CreateEmployee;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateCorporate implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_CREATE);
            result = postProcess(inputParams, dcRequest, result);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;

        Record record = new Record();
        record.setId(DBPUtilitiesConstants.USR_ATTR);

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {

            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            if (userPermissions.isEmpty() || !userPermissions.contains("USER_MANAGEMENT")) {
                HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.SECURITY_ERROR, ErrorCodes.SECURITY_ERROR,
                        record);
                ErrorCodeEnum.ERR_10064.setErrorCode(record);
                result.addRecord(record);
                return false;
            }
        }

        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        inputParams.put("id", id);

        Map<String, String> LoggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
        String createdBy = LoggedInUserInfo.get("UserName");
        if (StringUtils.isNotBlank(createdBy)) {
            inputParams.put("createdby", createdBy);
        }

        String orgId = "";
        try {
            orgId = HelperMethods.getOrganizationIDForUser(LoggedInUserInfo.get("customer_id"), dcRequest);
        } catch (HttpCallException e) {
            orgId = "";
            alert.prepareError(e.getMessage()).log();
        }
        if (StringUtils.isBlank(orgId)) {
            orgId = dcRequest.getParameter("Organization_id");
        }

        if (StringUtils.isBlank(orgId)) {
            orgId = dcRequest.getParameter("Organization_Id");
        }

        if (StringUtils.isBlank(orgId)) {
            orgId = inputParams.get("Organization_id");
        }

        inputParams.put("Organization_id", orgId);

        inputParams.put("Organization_Id", orgId);

        if (StringUtils.isBlank(orgId)) {
            // HelperMethods.setValidationMsgwithCode("Invalid Org ID",
            // ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, result);
            ErrorCodeEnum.ERR_10065.setErrorCode(result);
            return false;
        }

        String username = inputParams.get("UserName");
        String password = inputParams.get("Password");
        inputParams.put("inputPassword", password);

        if (StringUtils.isBlank(username)) {
            // HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.PROVIDE_USERNAME,
            // ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, record);
            ErrorCodeEnum.ERR_10066.setErrorCode(record);
            result.addRecord(record);
            return false;
        } else {
            if (StringUtils.isNotBlank(password)) {
                inputParams.put("Status_id", "SID_CUS_ACTIVE");
                String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
                String hashedPassword = BCrypt.hashpw(password, salt);
                inputParams.put("Password", hashedPassword);
            } else {
                inputParams.put("Status_id", "SID_CUS_NEW");
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("IDMKey").append(DBPUtilitiesConstants.EQUAL).append("Corporate");

        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        try {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.IDMCONFIGURATION_GET);
        } catch (HttpCallException e) {
            result = new Result();
            ErrorCodeEnum.ERR_10157.setErrorCode(record);
            result.addRecord(record);
            alert.prepareError(e.getMessage()).log();
            return false;
        }

        String value = HelperMethods.getFieldValue(result, "IDMValue");

        if (!value.equals(DBPUtilitiesConstants.DBX)) {
            return false;
        }

        inputParams.remove(DBPUtilitiesConstants.FILTER);

        Map<String, String> customerTypes = HelperMethods.getCustomerTypes();
        inputParams.put("CustomerType_id", customerTypes.get("Small Business"));

        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");

        inputParams.put("id", idformatter.format(new Date()));

        String role = inputParams.get(DBPUtilitiesConstants.ROLE);
        if (!StringUtils.isNotBlank(role)) {
            inputParams.put("Role", "BASIC");
        }

        HelperMethods.removeNullValues(inputParams);

        return status;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        Result retResult = new Result();
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.USR_ATTR);
        if (HelperMethods.hasRecords(result)) {
            String id = getUserId(result);
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, record);
            record.addParam(new Param("id", id, "String"));
            retResult.addParam(new Param("id", id, "String"));
            inputParams.put("id", id);
            try {
                CreateCustomerCommunication.invoke(inputParams, dcRequest);
            } catch (Exception e) {
                ErrorCodeEnum.ERR_10068.setErrorCode(record);
                alert.prepareError(e.getMessage()).log();
            }
            try {
                CreateEmployee.invoke(inputParams, dcRequest);
            } catch (HttpCallException e) {
                ErrorCodeEnum.ERR_10068.setErrorCode(record);
                alert.prepareError(e.getMessage()).log();
            }
            CreateCustomerPreference.invoke(inputParams, dcRequest);
        } else {
            // HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result),
            // ErrorCodes.ERROR_CREATING_RECORD, record);
            ErrorCodeEnum.ERR_10068.setErrorCode(record);
        }

        retResult.addRecord(record);
        return retResult;
    }

    private String getUserId(Result result) {
        String id = "";
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            if (null != ds && !ds.getAllRecords().isEmpty()) {
                id = ds.getRecord(0).getParam("id").getValue();
            }
        }
        return id;
    }

}
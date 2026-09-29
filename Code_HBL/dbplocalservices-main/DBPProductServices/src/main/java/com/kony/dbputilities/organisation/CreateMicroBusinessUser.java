package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.CreateCustomerPreference;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.customersecurityservices.VerifyDBXUserName;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.BCrypt;
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

public class CreateMicroBusinessUser implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result1 = (Result) new VerifyDBXUserName().invoke(methodID, inputArray, dcRequest, dcResponse);
        Record record = result1.getRecordById(DBPUtilitiesConstants.USR_ATTR);

        if ((record != null) && (HelperMethods.getFieldValue(record, DBPUtilitiesConstants.IS_USERNAME_EXISTS)
                .equalsIgnoreCase("true"))) {
            record = new Record();
            record.setId(DBPUtilitiesConstants.USR_ATTR);
            ErrorCodeEnum.ERR_10049.setErrorCode(record);
            result.addRecord(record);
            return result;
        }

        if (preProcess(methodID, inputArray, inputParams, dcRequest, dcResponse, result)) {
            inputParams.put("ESignStatus", "false");
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_CREATE);
            PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
            if (StringUtils.isNotBlank(getUserId(result))
                    && pm.makePasswordEntry(dcRequest, getUserId(result), inputParams.get("Password"))) {
                result = postProcess(inputParams, dcRequest, result);
            } else if (StringUtils.isNotBlank(getUserId(result))) {
                Map<String, String> input = new HashMap<>();
                String id = getUserId(result);
                input.put("id", id);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_DELETE);
                result = addErrorTOResult(inputParams);
            } else {
                result = addErrorTOResult(inputParams);
            }

        }

        return result;
    }

    private Result addErrorTOResult(Map<String, String> inputParams) {
        Result retResult = new Result();
        Record record = new Record();
        ErrorCodeEnum.ERR_10055.setErrorCode(record);
        retResult.addParam(new Param("isEAgreementRequired", inputParams.get("isEAgreementRequired"), "String"));
        retResult.addParam(new Param("isEagreementSigned", inputParams.get("ESignStatus"), "String"));
        record.addParam(new Param("isEAgreementRequired", inputParams.get("isEAgreementRequired"), "String"));
        record.addParam(new Param("isEagreementSigned", inputParams.get("ESignStatus"), "String"));
        record.setId(DBPUtilitiesConstants.USR_ATTR);
        retResult.addRecord(record);
        return retResult;
    }

    private boolean preProcess(String methodID, Object[] inputArray, Map<String, String> inputParams,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse, Result result) throws Exception {
        boolean status = true;
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.USR_ATTR);

        HelperMethods.removeNullValues(inputParams);

        String orgId = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);
        if (StringUtils.isBlank(orgId)) {
            orgId = dcRequest.getParameter("Organization_id");
        }

        if (StringUtils.isBlank(orgId)) {
            orgId = dcRequest.getParameter("Organization_Id");
        }

        if (StringUtils.isBlank(orgId)) {
            orgId = inputParams.get("Organization_id");
        }

        inputParams.put("Organization_Id", orgId);

        if (StringUtils.isBlank(orgId)) {
            ErrorCodeEnum.ERR_10050.setErrorCode(record);
            result.addRecord(record);
            return false;
        }

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            if (!userPermissions.contains("USER_MANAGEMENT")) {
                record.setId(DBPUtilitiesConstants.USR_ATTR);
                ErrorCodeEnum.ERR_10051.setErrorCode(record);
                result.addRecord(record);
                return false;
            }
        }

        VerifyOrganizationUser dbxUser = new VerifyOrganizationUser();

        result = (Result) dbxUser.invoke(methodID, inputArray, dcRequest, dcResponse);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Ssn", inputParams.get("Ssn"));
        hashMap.put("Organization_Id", orgId);

        Result result1 = (Result) dbxUser.invoke(dcRequest, hashMap);

        if (HelperMethods.hasRecords(result) || HelperMethods.hasRecords(result1)) {
            ErrorCodeEnum.ERR_10052.setErrorCode(record);
            result = new Result();
            result.addRecord(record);
            return false;
        }

        String username = inputParams.get("UserName");
        String password = inputParams.get("Password");
        inputParams.put("inputPassword", password);

        if (StringUtils.isBlank(username)) {
            ErrorCodeEnum.ERR_10053.setErrorCode(record);
            result.addRecord(record);
            return false;
        } else {

            if (isUserEsignAgreementReq(HelperMethods.getRecord(result), dcRequest)) {
                inputParams.put("isEAgreementRequired", "true");
            } else {
                inputParams.put("isEAgreementRequired", "false");
            }

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
        sb.append("IDMKey").append(DBPUtilitiesConstants.EQUAL).append("MicroBusinessUser");

        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        Result idmConfig = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.IDMCONFIGURATION_GET);

        String value = HelperMethods.getFieldValue(idmConfig, "IDMValue");

        if (!value.equals("DBX") && !value.isEmpty()) {
            return false;
        }

        inputParams.remove(DBPUtilitiesConstants.FILTER);

        Map<String, String> customerTypes = HelperMethods.getCustomerTypes();
        inputParams.put("CustomerType_id", customerTypes.get("Micro Business"));

        inputParams.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));

        String role = inputParams.get(DBPUtilitiesConstants.ROLE);
        if (!StringUtils.isNotBlank(role)) {
            inputParams.put("Role", "BASIC");
        }

        return status;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws Exception {
        Result retResult = new Result();
        Record record = new Record();
        if (!HelperMethods.hasError(result)) {
            String id = getUserId(result);
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, record);
            record.addParam(new Param("id", id, "String"));
            result.addParam(new Param("id", id, "String"));
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, retResult);
            retResult.addParam(new Param("id", id, "String"));
            inputParams.put("id", id);
            CreateCustomerCommunication.invoke(inputParams, dcRequest);
            CreateEmployee.invoke(inputParams, dcRequest);
            CreateCustomerPreference.invoke(inputParams, dcRequest);
        } else {
            ErrorCodeEnum.ERR_10055.setErrorCode(record);
        }
        retResult.addParam(new Param("isEAgreementRequired", inputParams.get("isEAgreementRequired"), "String"));
        retResult.addParam(new Param("isEagreementSigned", inputParams.get("ESignStatus"), "String"));
        record.addParam(new Param("isEAgreementRequired", inputParams.get("isEAgreementRequired"), "String"));
        record.addParam(new Param("isEagreementSigned", inputParams.get("ESignStatus"), "String"));
        record.setId(DBPUtilitiesConstants.USR_ATTR);
        retResult.addRecord(record);
        return retResult;
    }

    private String getUserId(Result result) {
        String id = "";
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            id = ds.getRecord(0).getParam("id").getValue();
        }
        return id;
    }

    private boolean isUserEsignAgreementReq(Record user, DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> map = new HashMap<>();
        map.put("Username", HelperMethods.getFieldValue(user, "UserName"));
        Result result = AdminUtil.invokeAPI(map, URLConstants.E_AGREEMENT_AVAILABLE, dcRequest);
        return "true".equalsIgnoreCase(HelperMethods.getParamValue(result.getParamByName("isEAgreementAvailable")));
    }
}

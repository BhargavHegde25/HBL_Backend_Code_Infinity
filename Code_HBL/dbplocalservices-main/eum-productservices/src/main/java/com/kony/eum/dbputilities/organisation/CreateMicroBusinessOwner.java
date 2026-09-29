package com.kony.eum.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.organisation.CreateEmployee;
import com.kony.dbputilities.organisation.VerifyOrganizationUser;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.eum.dbputilities.customersecurityservices.VerifyDBXUserName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateMicroBusinessOwner implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Result result1 = (Result) new VerifyDBXUserName().invoke(methodID, inputArray, dcRequest, dcResponse);
        Record record = result1.getRecordById(DBPUtilitiesConstants.USR_ATTR);
        if ((record != null)
                && (HelperMethods.getFieldValue(record, DBPUtilitiesConstants.IS_USERNAME_EXISTS).equals("true"))) {
            record = new Record();
            record.setId(DBPUtilitiesConstants.USR_ATTR);
            ErrorCodeEnum.ERR_10076.setErrorCode(record);
            result.addRecord(record);
            return result;
        }

        if (preProcess(methodID, inputArray, inputParams, dcRequest, dcResponse, result)) {
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
                result = new Result();
                ErrorCodeEnum.ERR_10081.setErrorCode(result);
            } else {
                result = addErrorTOResult(result);
            }
        }

        return result;
    }

    private Result addErrorTOResult(Result result) {
        Result retResult = new Result();
        ErrorCodeEnum.ERR_10081.setErrorCode(retResult, HelperMethods.getError(result));
        return retResult;
    }

    private boolean preProcess(String methodID, Object[] inputArray, Map<String, String> inputParams,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse, Result result) throws Exception {
        boolean status = true;

        VerifyOrganizationUser dbxUser = new VerifyOrganizationUser();

        Result dbxUsrResult = (Result) dbxUser.invoke(methodID, inputArray, dcRequest, dcResponse);

        if (HelperMethods.hasRecords(dbxUsrResult)) {
            ErrorCodeEnum.ERR_10077.setErrorCode(result);
            return false;
        } else {
            inputParams = getAllParamsfromOrganisationOwner(inputParams, dcRequest);
        }

        String username = inputParams.get("UserName");
        String password = inputParams.get("Password");
        inputParams.put("inputPassword", password);
        if (StringUtils.isBlank(inputParams.get("Organization_Id"))) {
            ErrorCodeEnum.ERR_10078.setErrorCode(result);
            return false;
        }
        if (StringUtils.isBlank(username)) {
            ErrorCodeEnum.ERR_10079.setErrorCode(result);
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

        inputParams.put("ESignStatus", "false");
        StringBuilder sb = new StringBuilder();
        sb.append("IDMKey").append(DBPUtilitiesConstants.EQUAL).append("MicroBusinessUser");
        Result idmResult = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.IDMCONFIGURATION_GET);

        String value = HelperMethods.getFieldValue(idmResult, "IDMValue");

        if (!value.equals("DBX") && !value.isEmpty()) {
            return false;
        }

        inputParams.put("CustomerType_id", "TYPE_ID_MICRO_BUSINESS");

        inputParams.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));

        String role = inputParams.get(DBPUtilitiesConstants.ROLE);
        if (!StringUtils.isNotBlank(role)) {
            inputParams.put("Role", "BASIC");
        }

        HelperMethods.removeNullValues(inputParams);

        return status;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws Exception {
        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            String id = HelperMethods.getFieldValue(result, "id");

            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, retResult);
            retResult.addParam(new Param("id", id, "String"));
            inputParams.put("id", id);
            CreateCustomerCommunication.invoke(inputParams, dcRequest);
            inputParams.put("Is_Admin", "true");
            inputParams.put("Is_Owner", "true");
            createGroupForOwner(id, dcRequest);
            CreateEmployee.invoke(inputParams, dcRequest);
            // CreateCustomerPreference.invoke(inputParams, dcRequest);
        } else {
            ErrorCodeEnum.ERR_10081.setErrorCode(retResult, HelperMethods.getError(result));
        }
        return retResult;
    }

    private void createGroupForOwner(String id, DataControllerRequest dcRequest) {
        Map<String, String> postParamMapGroup = new HashMap<>();
        postParamMapGroup.put("Customer_id", id);
        postParamMapGroup.put("Group_id", "GROUP_MICRO_ADMINISTRATOR");
        try {
            HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GROUP_CREATE);
        } catch (HttpCallException e) {
        }
    }

    private String getUserId(Result result) {
        String id = "";
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            id = ds.getRecord(0).getParam("id").getValue();
        }
        return id;
    }

    private Map<String, String> getAllParamsfromOrganisationOwner(Map<String, String> inputParams,
            DataControllerRequest dcRequest) throws HttpCallException {

        String[] list = { "Organization_Id" };

        String filter = "";

        for (int i = 0; i < list.length; i++) {
            String filterKey = list[i];
            String filtervalue = inputParams.get(filterKey);
            if (StringUtils.isBlank(filtervalue)) {
                filtervalue = dcRequest.getParameter(filterKey);
            }

            if (StringUtils.isNotBlank(filtervalue)) {
                if (!filter.isEmpty()) {
                    filter += DBPUtilitiesConstants.AND;
                }

                filter += filterKey + DBPUtilitiesConstants.EQUAL + filtervalue;
            }
        }

        Result result = null;
        if (!filter.isEmpty()) {
            result = HelperMethods.callGetApi(dcRequest, filter, null, URLConstants.ORGANISATION_OWNER_VIEW_GET);
        }

        if (HelperMethods.hasRecords(result)) {
            String[] list1 = { "LastName", "DateOfBirth", "MidleName", "Phone", "Email", "FirstName", "IDType_id",
                    "IdValue", "Organization_Id" };

            Record record = HelperMethods.getRecord(result);

            for (int i = 0; i < list1.length; i++) {
                String key = list1[i];
                if (StringUtils.isBlank(inputParams.get(key))) {
                    String value = HelperMethods.getParamValue(record.getParam(key));
                    inputParams.put(key, value);
                }
            }
        }

        return inputParams;
    }

}

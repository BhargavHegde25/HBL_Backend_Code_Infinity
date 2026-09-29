package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCustomerDetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_UPDATE);
            if (StringUtils.isNotBlank(inputParams.get("riskStatus"))) {
                updateCustomerFlag(inputParams, dcRequest);
            }

            result = postProcess(result);
            EditCustomerCommunication.invoke(inputParams.get("id"), inputParams, dcRequest);
        }

        return result;
    }

    private void updateCustomerFlag(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String riskStatus = inputParams.get("riskStatus");
        String id = inputParams.get("id");
        String userName = inputParams.get("userName");
        JsonParser parser = new JsonParser();
        JsonObject json = new JsonObject();
        try {
            json = parser.parse(riskStatus).getAsJsonObject();
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }
        if (json.has("ListRemovedRisk") && json.get("ListRemovedRisk").isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray("ListRemovedRisk");
            if (jsonArray != null && !jsonArray.isJsonNull()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    String riskValue = jsonArray.get(i).getAsString();
                    Set<String> list = HelperMethods.getRiskAcceptedValues();
                    try {
                        if (list.contains(riskValue)) {
                            Map<String, String> updateFields = new HashMap<>();
                            updateFields.put("Customer_id", id);
                            updateFields.put("Status_id", riskValue);

                            Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
                            headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

                            HelperMethods.callApi(dcRequest, updateFields, headers, URLConstants.CUSTOMERFLAG_DELETE);
                        }
                    } catch (HttpCallException ex) {
                        ex.getMessage();
                    }
                }
            }

        }

        if (json.has("ListAddedRisk") && json.get("ListAddedRisk").isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray("ListAddedRisk");
            if (jsonArray != null && !jsonArray.isJsonNull()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    String riskValue = jsonArray.get(i).getAsString();
                    Set<String> list = HelperMethods.getRiskAcceptedValues();
                    try {
                        if (list.contains(riskValue)) {
                            Map<String, String> updateFields = new HashMap<>();
                            updateFields.put("Customer_id", id);
                            updateFields.put("Status_id", riskValue);
                            updateFields.put("createdby", userName);
                            updateFields.put("modifiedby", userName);
                            HelperMethods.callApi(dcRequest, updateFields, HelperMethods.getHeaders(dcRequest),
                                    URLConstants.CUSTOMERFLAG_CREATE);
                        }
                    } catch (HttpCallException ex) {
                        ex.getMessage();
                    }
                }
            }

        }

    }

    private Result addErrorTOResult() {
        Result retResult = new Result();
        ErrorCodeEnum.ERR_10068.setErrorCode(retResult);
        return retResult;
    }

    private Result postProcess(Result result) {
        Result retResult = new Result();
        Record record = new Record();
        record.setId(DBPUtilitiesConstants.USR_ATTR);
        if (HelperMethods.hasRecords(result)) {
            HelperMethods.setSuccessMsg("Update success.", record);
        } else if (HelperMethods.hasError(result)) {
            ErrorCodeEnum.ERR_10035.setErrorCode(record, HelperMethods.getError(result));
        }
        retResult.addRecord(record);
        return retResult;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        if (StringUtils.isNotBlank(dcRequest.getParameter("isEngageProvisioned"))) {
            String userId = inputParams.get("id");
            String isEngageProvisioned = inputParams.get("isEngageProvisioned");
            inputParams.clear();
            inputParams.put("id", userId);
            inputParams.put("isEngageProvisioned", isEngageProvisioned);
            return true;
        }

        String dlno = dcRequest.getParameter("DrivingLicenseNumber");
        String middleName = dcRequest.getParameter("MiddleName");
        String isEagreementSigned = dcRequest.getParameter("isEagreementSigned");
        String riskStatus = dcRequest.getParameter("RiskStatus");
        String pin = dcRequest.getParameter("pin");
        if (StringUtils.isNotBlank(riskStatus)) {
            inputParams.put("riskStatus", riskStatus);
        }
        if (StringUtils.isNotBlank(isEagreementSigned)) {
            inputParams.put("isEagreementSigned", isEagreementSigned);
        } else {
            inputParams.put("isEagreementSigned", "");
        }

        HelperMethods.removeNullValues(inputParams);

        if (dlno != null && dlno.isEmpty()) {
            inputParams.put("DrivingLicenseNumber", "");
        }

        if (middleName != null && middleName.isEmpty()) {
            inputParams.put("MiddleName", "");
        }

        if (pin != null && pin.isEmpty()) {
            inputParams.put("Pin", pin);
            inputParams.put(DBPUtilitiesConstants.IS_PIN_SET, "true");
        }

        Record usrAttr = new Record();
        usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);

        String id = inputParams.get("id");

        Result userResult = HelperMethods.getUserRecordById(id, dcRequest);

        String userName = HelperMethods.getFieldValue(userResult, "UserName");

        String userType = HelperMethods.getFieldValue(userResult, "CustomerType_id");

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);

        String loggedInUserId =  HelperMethods.getCustomerIdFromSession(dcRequest);

        if(StringUtils.isBlank(loggedInUserId)) {
            loggedInUserId = loggedInUserInfo.get("customer_id");
        }


        if (StringUtils.isBlank(id)  || (loggedInUserId.equals(id) && !HelperMethods.getCustomerTypes().get("Customer").equals(userType))) {
            ErrorCodeEnum.ERR_12405.setErrorCode(usrAttr);
            result.addRecord(usrAttr);
            return false;
        }

        String loggedInUserOrgId = loggedInUserInfo.get("Organization_Id");
        String orgIdOfUserUnderUpdate = HelperMethods.getOrganizationIDForUser(id, dcRequest);

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            if (userPermissions.contains("USER_MANAGEMENT")) {
                if (StringUtils.isNotBlank(orgIdOfUserUnderUpdate)
                        && !loggedInUserOrgId.equals(orgIdOfUserUnderUpdate)) {
                    ErrorCodeEnum.ERR_10035.setErrorCode(usrAttr);
                    result.addRecord(usrAttr);
                    return false;
                }
            } else {
                ErrorCodeEnum.ERR_10035.setErrorCode(usrAttr);
                result.addRecord(usrAttr);
                return false;
            }
        }
        else{
            inputParams.remove("Ssn");
        }

        inputParams.put("Organization_Id", orgIdOfUserUnderUpdate);
        inputParams.put("Organization_id", orgIdOfUserUnderUpdate);
        inputParams.put("UserName", userName);
        inputParams.put("id", id);
        inputParams.remove("Ssn");

        return true;
    }

    private String getUserId(Result result) {
        String id = "";
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            id = ds.getRecord(0).getParam("id").getValue();
        }
        return id;
    }

}
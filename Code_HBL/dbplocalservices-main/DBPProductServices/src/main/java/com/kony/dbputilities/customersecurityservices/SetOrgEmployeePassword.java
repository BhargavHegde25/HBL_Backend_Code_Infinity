package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SetOrgEmployeePassword implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        String UserName = dcRequest.getParameter("UserName").trim();
        String Password = dcRequest.getParameter("Password").trim();
        String link = dcRequest.getParameter("identifier");
        boolean isUserNameSetUpEnabled = false;
        Result result = new Result();
        try {
            link = new String(java.util.Base64.getDecoder().decode(link));
            String[] strings = link.split("_");
            link = strings[0];
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (StringUtils.isBlank(link)) {
            ErrorCodeEnum.ERR_12429.setErrorCode(result);
            result.addParam(new Param("status", Boolean.toString(false), MWConstants.STRING));
            return result;
        }
        if (StringUtils.isBlank(UserName)) {
            ErrorCodeEnum.ERR_12427.setErrorCode(result);
            return result;
        }
        if (StringUtils.isBlank(Password)) {
            ErrorCodeEnum.ERR_12427.setErrorCode(result);
            return result;
        } else {
            String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
            Password = BCrypt.hashpw(Password, salt);
        }
        if (StringUtils.isBlank(link)) {
            ErrorCodeEnum.ERR_12427.setErrorCode(result);
            return result;
        }
        Result existingRecord = HelperMethods.getActivationRecordByActivationId(link, dcRequest);
        String createdDate;
        String createdUser;
        String credentialType;
        Result userRecord;
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        if (HelperMethods.hasRecords(existingRecord)) {
            createdDate = HelperMethods.getFieldValue(existingRecord, "createdts");
            createdUser = HelperMethods.getFieldValue(existingRecord, "UserName");
            credentialType = HelperMethods.getFieldValue(existingRecord, "linktype");
            if (createdDate.trim().isEmpty()) {
                ErrorCodeEnum.ERR_12429.setErrorCode(result);
                return result;
            }
            if (!(HelperMethods.isDateInRange(createdDate, pm.getRecoveryEmailLinkValidity())
                    && credentialType.equals(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString()))) {
                ErrorCodeEnum.ERR_12429.setErrorCode(result);
                return result;
            }
            userRecord = HelperMethods.getUserRecordByName(createdUser, dcRequest);
            if (!HelperMethods.hasRecords(userRecord)) {
                ErrorCodeEnum.ERR_12431.setErrorCode(result);
                return result;
            }
            if (HelperMethods.getFieldValue(userRecord, "UserName")
                    .equalsIgnoreCase(HelperMethods.getFieldValue(userRecord, "id"))) {
                isUserNameSetUpEnabled = true;
            }
            if (!isUserNameSetUpEnabled && !createdUser.equalsIgnoreCase(UserName)) {
                ErrorCodeEnum.ERR_12430.setErrorCode(result);
                return result;
            }
        } else {
            ErrorCodeEnum.ERR_12429.setErrorCode(result);
            return result;
        }

        String status = HelperMethods.getFieldValue(userRecord, "Status_id");
        String userID = HelperMethods.getFieldValue(userRecord, "id");
        String isEAgreementSigned = HelperMethods.getFieldValue(userRecord, "isEagreementSigned");
        String isEAgreementRequired = String.valueOf(isEAgreementRequired(userRecord, dcRequest, result));
        Param p1 = new Param("isEAgreementRequired", isEAgreementRequired, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param p2 = new Param("isEagreementSigned", isEAgreementSigned, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        if (!status.equals("SID_CUS_NEW")) {
            ErrorCodeEnum.ERR_12432.setErrorCode(result);
            return result;
        }
        Map<String, String> updateUserMap = new HashMap<>();
        if (isUserNameSetUpEnabled) {
            updateUserMap.put("UserName", UserName);
        }
        updateUserMap.put("id", userID);
        updateUserMap.put("Status_id", "SID_CUS_ACTIVE");
        updateUserMap.put("Password", Password);
        Result updateUser = HelperMethods.callApi(dcRequest, updateUserMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_UPDATE);

        pm.makePasswordEntry(dcRequest, userID, Password);

        if (HelperMethods.hasError(updateUser)) {
            ErrorCodeEnum.ERR_12427.setErrorCode(result);

        } else {
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, result);
            result.addStringParam("isUserNameSetUpEnabled", String.valueOf(isUserNameSetUpEnabled));
            result.addParam(p1);
            result.addParam(p2);

            Map<String, String> deleteRecord = new HashMap<>();
            deleteRecord.put("id", link);
            try {
                HelperMethods.callApiAsync(dcRequest, deleteRecord, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CREDENTIAL_CHECKER_DELETE);
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
        return result;
    }

    private boolean isEAgreementRequired(Result user, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input.put("_customerId", HelperMethods.getFieldValue(user, "id"));

        JsonObject response = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_EAGREEMENT_GET);
        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
            return false;
        }
        if (response.has("records") && response.get("records").getAsJsonArray().size() != 0
                && response.get("records").getAsJsonArray().get(0) != null && response.get("records").getAsJsonArray()
                        .get(0).getAsJsonObject().get("isEAgreementActive") != null) {
            return response.get("records").getAsJsonArray().get(0).getAsJsonObject().get("isEAgreementActive")
                    .getAsBoolean();
        }
        result.addStringParam("roleId", JSONUtil.getString(response, "id"));
        result.addStringParam("roleName", JSONUtil.getString(response, "name"));
        return false;
    }

}

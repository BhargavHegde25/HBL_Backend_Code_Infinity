package com.kony.dbputilities.customersecurityservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateMBOwnerAccounts implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String id = dcRequest.getParameter("id");
        if (StringUtils.isBlank(id)) {
            id = inputParams.get("id");
        }

        String orgId = dcRequest.getParameter("Organization_Id");

        Record record = new Record();
        if (StringUtils.isBlank(id) || StringUtils.isBlank(orgId)) {
            ErrorCodeEnum.ERR_12434.setErrorCode(record);
            result.addRecord(record);
            return result;
        }
        String customer_id = id;
        String organization_id = orgId;
        List<String> existingAccounts = getExistingAccounts(organization_id, dcRequest);

        if (existingAccounts.size() == 0) {
            ErrorCodeEnum.ERR_12435.setErrorCode(record);
            result.addRecord(record);
            return result;
        }

        createEmployeeAccounts(customer_id, organization_id, existingAccounts, dcRequest, result);

        JsonArray accounts =  new JsonArray();
        for(String account : existingAccounts) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("accountId", account);
            accounts.add(jsonObject);
        }

        String group_id =  "GROUP_MICRO_ADMINISTRATOR";
        AddCustomerGroup(dcRequest, inputParams, group_id);

        LimitsHandler.modifyCustomerActionLimitsForOwner(orgId, group_id, accounts, customer_id, dcRequest, result);
        return result;
    }


    private void createEmployeeAccounts(String customer_id, String organization_id, List<String> existingAccounts,
            DataControllerRequest dcRequest, Result result) {
        Record record = new Record();
        Result createAccount = new Result();

        for (int i = 0; i < existingAccounts.size(); i++) {
            Map<String, String> inputMaps = processAccount(customer_id, organization_id, existingAccounts.get(i));
            try {
                createAccount = HelperMethods.callApi(dcRequest, inputMaps, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERACCOUNTS_CREATE);
            } catch (HttpCallException e) {
                createAccount = new Result();
                alert.prepareError(e.getMessage()).log();
            }
            if (!HelperMethods.hasError(createAccount)) {
                HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.SUCCESS_MSG, ErrorCodes.RECORD_CREATED,
                        result);
            } else {
                // HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result),
                // ErrorCodes.ERROR_CREATING_RECORD, result);
                ErrorCodeEnum.ERR_12436.setErrorCode(record);
                result.addRecord(record);
                break;
            }
        }

    }

    private Map<String, String> processAccount(String customerId, String orgId, String accountId) {
        Map<String, String> inputMaps = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        inputMaps.put("id", uuid.toString());
        inputMaps.put("Customer_id", customerId);
        inputMaps.put("Account_id", accountId);
        inputMaps.put("Organization_id", orgId);
        inputMaps.put("IsOrganizationAccount", "1");

        return inputMaps;
    }

    private List<String> getExistingAccounts(String orgId, DataControllerRequest dcRequest) {

        String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
        Result getExisting = new Result();
        try {
            getExisting = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_GET);
        } catch (HttpCallException e) {
            getExisting = new Result();
            alert.prepareError(e.getMessage()).log();
        }

        List<String> existingOrgAccountIds = new ArrayList<>();
        if (HelperMethods.hasRecords(getExisting)) {
            List<Record> accounts = getExisting.getAllDatasets().get(0).getAllRecords();
            for (Record accountRecord : accounts) {
                existingOrgAccountIds.add(HelperMethods.getFieldValue(accountRecord, "Account_id"));
            }
        }

        return existingOrgAccountIds;
    }

    private void AddCustomerGroup(DataControllerRequest dcRequest, Map<String, String> inputParams, String group_id) {
        // TODO Auto-generated method stub

        String customerId = dcRequest.getParameter("Customer_id")!=null ? dcRequest.getParameter("Customer_id") : dcRequest.getParameter("id");

        if(customerId!= null) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;

            Result result = new Result();

            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_GROUP_GET);
            } catch (HttpCallException e1) {

            	alert.prepareError(e1.toString()).log();
            }

            Map<String, String> postParamMapGroup = new HashMap<>();

            if(HelperMethods.hasRecords(result)) {
                postParamMapGroup.put("Customer_id", customerId);
                postParamMapGroup.put("Group_id", HelperMethods.getFieldValue(result, "Group_id"));
                HelperMethods.callApiAsync(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_DELETE);
            }

            Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
            String userName = loggedInUserInfo.get("UserName");
            if(StringUtils.isNotBlank(group_id)) {
                postParamMapGroup.put("Customer_id", customerId);
                postParamMapGroup.put("Group_id", group_id);
                postParamMapGroup.put("createdby", userName);
                postParamMapGroup.put("modifiedby", userName);
                HelperMethods.callApiAsync(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_CREATE);
            }
        }

    }
}

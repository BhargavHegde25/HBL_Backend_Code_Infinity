package com.kony.dbputilities.organisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UnlinkOrganizationAccounts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            String accountsList = dcRequest.getParameter("AccountsList");
            String orgId = inputParams.get("id");
            List<HashMap<String, String>> removedList = new ArrayList<>();
            verifyAccountsAndUpdateList(accountsList, orgId, removedList, dcRequest);
            if (removedList.isEmpty()) {
                ErrorCodeEnum.ERR_11005.setErrorCode(result, HelperMethods.getError(result));
                return result;
            }
            EditOrganisationAccounts.invoke(inputParams, removedList, dcRequest);
            HelperMethods.setSuccessMsg("Accounts Unlink Successful", result);
        }

        return result;
    }

    private void verifyAccountsAndUpdateList(String accountsList, String organisationId,
            List<HashMap<String, String>> removedList, DataControllerRequest dcRequest) {
        JsonParser jsonparser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonparser.parse(accountsList);
        } catch (JsonParseException e) {

        }
        if (JSONUtil.isJsonNotNull(element) && element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            List<String> orgAccounts = getOrganisationAccounts(organisationId, dcRequest);
            for (JsonElement account : array) {
                JsonObject jsonObject = account.isJsonObject() ? account.getAsJsonObject() : new JsonObject();
                String accountId =
                        JSONUtil.hasKey(jsonObject, "Account_id") ? jsonObject.get("Account_id").getAsString() : "";
                if (orgAccounts.contains(accountId)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Account_id", accountId);
                    removedList.add(map);

                }
            }

        }

    }

    private List<String> getOrganisationAccounts(String orgId, DataControllerRequest dcRequest) {
        Result result = null;
        String filter = "";
        List<String> organisationAccounts = new ArrayList<>();

        if (StringUtils.isNotBlank(orgId)) {
            filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
        }

        if (!filter.isEmpty()) {
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_GET);
            } catch (HttpCallException e) {
            }
        }
        if (HelperMethods.hasRecords(result)) {
            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            String accountId = null;
            for (Record record : records) {
                accountId = HelperMethods.getFieldValue(record, "Account_id");
                organisationAccounts.add(accountId);
            }

        }
        return organisationAccounts;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {

        String orgId = dcRequest.getParameter("Organization_id");
        if (StringUtils.isNotBlank(orgId)) {
            inputParams.put("id", orgId);
            return true;
        } else {
            ErrorCodeEnum.ERR_11004.setErrorCode(result);
            return false;
        }
    }
}

package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationAccounts {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private CreateOrganisationAccounts() {

    }

    public static Result invoke(Map<String, String> inputParams, List<HashMap<String, String>> addedList,
            DataControllerRequest dcRequest, Set<String> hashSet) {
        Result result = new Result();
        String id = inputParams.get("id");
        if (StringUtils.isBlank(id)) {
            id = dcRequest.getParameter("id");
        }

        if (StringUtils.isNotBlank(id)) {
            List<HashMap<String, String>> accounts = addedList;

            for (int i = 0; i < accounts.size(); i++) {
                HashMap<String, String> account = accounts.get(i);
                boolean isValid = false;

                String accountId = account.get("Account_id");
                String filterString = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
                Result accountResult = null;
                try {
                    accountResult =
                            HelperMethods.callGetApi(dcRequest, filterString, HelperMethods.getHeaders(dcRequest),
                                    URLConstants.ACCOUNT_GET);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                }
                if (!HelperMethods.hasRecords(accountResult)) {
                    isValid = false;
                } else if (StringUtils.isBlank(HelperMethods.getFieldValue(accountResult, "Organization_id"))) {
                    isValid = true;
                }

                if (isValid) {
                    Map<String, String> input = new HashMap<>();
                    input.put("Account_id", accountId);
                    input.put("isBusinessAccount", "1");
                    input.put("Organization_id", id);
                    input.put("AccountHolder", account.get("AccountHolder"));
                    input.put("AccountName", account.get("AccountName"));
                    input.put("Type_id", account.get("Type_id"));
                    input.put("StatusDesc", "Active");
                    input.put("Membership_id", account.get("Membership_id"));
                    input.put("TaxId", account.get("TaxId"));
                    input.put("MembershipName", account.get("MembershipName"));

                    HelperMethods.removeNullValues(input);

                    try {
                        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.ACCOUNTS_UPDATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    if (HelperMethods.hasError(result)) {
                        return result;
                    }
                } else {
                    Map<String, String> input = new HashMap<>();
                    input.put("Account_id", accountId);
                    input.put("isBusinessAccount", "1");
                    input.put("Organization_id", id);

                    JsonObject accountJson = GetDataFromCache(accountId);
                    HashMap<String, String> inputMap = HelperMethods.getRecordMap(accountJson.toString());

                    String partyAccountType = inputMap.get("AccountType");
                    String productAccountTypeId = getProductAccountTypeID(partyAccountType, dcRequest);

                    input.put("AccountName",
                            StringUtils.isNotBlank(account.get("AccountName")) ? account.get("AccountName")
                                    : inputMap.get("AccountName"));
                    input.put("Type_id", StringUtils.isNotBlank(account.get("Type_id")) ? account.get("Type_id")
                            : productAccountTypeId);
                    input.put("StatusDesc", "Active");
                    input.put("AccountHolder",
                            StringUtils.isNotBlank(account.get("AccountHolder")) ? account.get("AccountHolder")
                                    : inputMap.get("AccountHolder"));
                    input.put("arrangementId",
                            StringUtils.isNotBlank(account.get("arrangementId")) ? account.get("arrangementId")
                                    : inputMap.get("arrangementId"));

                    input.put("Membership_id",
                            StringUtils.isNotBlank(account.get("Membership_id")) ? account.get("Membership_id")
                                    : inputMap.get("Membership_id"));

                    input.put("TaxId",
                            StringUtils.isNotBlank(account.get("TaxId")) ? account.get("TaxId")
                                    : inputMap.get("TaxId"));

                    input.put("MembershipName",
                            StringUtils.isNotBlank(account.get("MembershipName")) ? account.get("MembershipName")
                                    : inputMap.get("MembershipName"));

                    HelperMethods.removeNullValues(input);

                    try {
                        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.ACCOUNTS_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    if (HelperMethods.hasError(result)) {
                        return result;
                    }
                }
                String[] actionIds = checkForMonetaryActionsAndconvertHashsetToStringArray(hashSet, dcRequest);
                try {
                    // ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    // .getFactoryInstance(BusinessDelegateFactory.class)
                    // .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
                    //
                    // approvalmatrixDelegate.createDefaultApprovalMatrixEntry(id, accountId, actionIds);
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                    alert.prepareError("Error occured while creating default approval matrix").log();

                }
            }
        }

        return result;
    }

    private static String[] checkForMonetaryActionsAndconvertHashsetToStringArray(Set<String> hashSet,
            DataControllerRequest dcRequest) {
        String[] actionsList = new String[0];
        Map<String, String> input = new HashMap<>();
        Result result = new Result();
        StringBuilder actionsString = new StringBuilder();
        for (String action : hashSet) {
            actionsString.append(action);
            actionsString.append(",");
        }
        if (actionsString.length() > 0)
            actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

        input.put("_featureActions", actionsString.toString());

        try {
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.GET_MONETARY_ACTIONS);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        String monetaryActionsString = HelperMethods.getFieldValue(result, "monetaryActions");
        if (StringUtils.isNotBlank(monetaryActionsString)) {
            actionsList = monetaryActionsString.split(",");
        }

        return actionsList;

    }

    private static JsonObject GetDataFromCache(String accountId) {
        String account = null;
        JsonObject object = new JsonObject();

        account = (String) MemoryManager.getFromCache(DBPUtilitiesConstants.CACHE_KEY_FOR_ACCOUNTS_SEARCH + accountId);

        if (StringUtils.isNotBlank(account)) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(account);
            object = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
        }

        return object;
    }

    private static String getProductAccountTypeID(String partyAccountType, DataControllerRequest dcRequest) {
        if (StringUtils.isBlank(partyAccountType)) {
            return "";
        }
        Result accontType = null;
        String productAccountType = partyAccountType;
        String filter = "TypeDescription" + DBPUtilitiesConstants.EQUAL + productAccountType;
        try {
            accontType = HelperMethods.callGetApi(dcRequest, filter,
                    HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNT_TYPE_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasRecords(accontType)) {
            return HelperMethods.getFieldValue(accontType, "TypeID");
        }

        productAccountType = URLFinder.getPropertyValue(partyAccountType, "accountType.properties");
        filter = "TypeDescription" + DBPUtilitiesConstants.EQUAL + productAccountType;
        accontType = null;
        try {
            accontType = HelperMethods.callGetApi(dcRequest, filter,
                    HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNT_TYPE_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return HelperMethods.getFieldValue(accontType, "TypeID");
    }

}

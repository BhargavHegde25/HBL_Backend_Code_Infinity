package com.kony.dbputilities.accountservices;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UpdateFavouriteStatus implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        try {
            return invokeOperation(methodID, inputArray, dcRequest, dcResponse);
        } finally {
            // getList cache: this operation changes data getList returns. Runs even after a part-way
            // failure, because some rows may already be written.
            GetListCacheInvalidator.sessionCustomerChanged(dcRequest);
        }
    }

    private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        alert.prepareError("UpdateFavouriteStatus start request::"+dcRequest + "::response::"+dcResponse).log();
        Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String loggedInUser = userProfile.get("UserName");

        if (StringUtils.isBlank(loggedInUser)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.SECURITY_ERROR, ErrorCodes.SECURITY_ERROR, result);
            return result;
        }

        // if (HelperMethods.isBusinessUserType(userProfile.get("customerType"))) {
        if (preProcessForBusiness(userProfile, inputParams, dcRequest, result)) {
            inputParams.put("modifiedby", loggedInUser);
            inputParams.put("lastmodifiedts", HelperMethods.getCurrentTimeStamp());
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERACCOUNTS_UPDATE);
        }
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_UPDATE);
        }
        alert.prepareError("UpdateFavouriteStatus start request::"+dcRequest + "::response::"+dcResponse).log();
        return result;
    }

    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String id = (String) inputParams.get("accountID");
        String favouriteStatus = getFavouriteStatus(dcRequest, id);
        if ("0".equals(favouriteStatus) || "false".equalsIgnoreCase(favouriteStatus)) {
            favouriteStatus = "1";
        } else {
            favouriteStatus = "0";
        }
        inputParams.put("Account_id", id);
        inputParams.put("FavouriteStatus", favouriteStatus);
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_UPDATE);
        return true;
    }

    private boolean preProcessForBusiness(Map<String, String> userProfile, Map inputParams,
            DataControllerRequest dcRequest, Result result) throws HttpCallException {

        String userId = userProfile.get("customer_id");
        if (StringUtils.isBlank(userId)) {
            HelperMethods.setValidationMsgwithCode("Unable to retrieve the user information",
                    ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, result);
            return false;
        }

        String accountIdToUpdate = (String) inputParams.get("accountID");
        if (StringUtils.isBlank(accountIdToUpdate)) {
            HelperMethods.setValidationMsgwithCode("Please provide the account information to update",
                    ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, result);
            return false;
        }

        String filter = DBPUtilitiesConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + userId
                + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL
                + accountIdToUpdate;
        createOrgEmployeeAccounts accountsHelper = new createOrgEmployeeAccounts();
        Result existingAccounts = accountsHelper.getExistingAccounts(filter, userId, dcRequest);

        if (!HelperMethods.hasRecords(existingAccounts)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.INVALID_ACCOUNT_ACCESS, ErrorCodes.SECURITY_ERROR,
                    result);
            return false;
        }

        List<Record> accounts = existingAccounts.getAllDatasets().get(0).getAllRecords();
        for (Record accountRecord : accounts) {
            String customer_acct_mapping_id = HelperMethods.getFieldValue(accountRecord, DBPUtilitiesConstants.UN_ID);
            String account_id_retrieved = HelperMethods.getFieldValue(accountRecord, DBPUtilitiesConstants.ACCOUNT_ID);
            if (accountIdToUpdate.equals(account_id_retrieved)) {
                inputParams.put("id", customer_acct_mapping_id);
                String isFavourite = HelperMethods.getFieldValue(accountRecord, "FavouriteStatus");
                if ("1".equals(isFavourite) || "true".equalsIgnoreCase(isFavourite)) {
                    inputParams.put("FavouriteStatus", "0");
                } else {
                    inputParams.put("FavouriteStatus", "1");
                }
                return true;
            } else {
                HelperMethods.setValidationMsgwithCode("Unable to update the favorite staus of account",
                        ErrorCodes.ERROR_UPDATING_RECORD, result);
                return false;
            }

        }

        return false;
    }

    private String getFavouriteStatus(DataControllerRequest dcRequest, String id) throws HttpCallException {
        String filter = DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL + id;
        Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        return HelperMethods.getFieldValue(account, "favouriteStatus");
    }

}

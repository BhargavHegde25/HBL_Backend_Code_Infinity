package com.kony.dbputilities.accountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsForAdmin implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            StringBuilder filter = new StringBuilder();
            String nullVal = null;
            String URL = URLConstants.ACCOUNTS_GET;
            String accountId = inputParams.get("accountID");
            String userId = inputParams.get("userId");
            String userName = inputParams.get("username");
            if (StringUtils.isBlank(accountId) && StringUtils.isBlank(userName)) {
                filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
            } else if (StringUtils.isNotBlank(userName)) {
                Result user = getUser(dcRequest, userName);
                userId = HelperMethods.getFieldValue(user, "id");
                if (!HelperMethods.getCustomerTypes().get("Customer")
                        .equals(HelperMethods.getFieldValue(user, "CustomerType_id"))) {
                    filter.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
                    URL = URLConstants.CUSTOMERACCOUNTSVIEW_GET;
                } else {
                    filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
                }
                if ("charles.smith".equalsIgnoreCase(userName) || "jane.doe".equalsIgnoreCase(userName)) {
                    filter.append(DBPUtilitiesConstants.OR);
                    filter.append("jointHolders").append(DBPUtilitiesConstants.NOT_EQ).append(nullVal);
                }
            } else {
                filter.append("Account_id").append(DBPUtilitiesConstants.EQUAL).append(accountId);
            }
            inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), URL);
        }
        if (HelperMethods.hasRecords(result)) {
            result.getAllDatasets().get(0).setId("accounts");
        } else {
            Dataset dataset = new Dataset();
            dataset.setId("accounts");
            result.addDataset(dataset);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
        if (!HelperMethods.isAdmin(dcRequest, adminId)) {
            HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
            return false;
        }
        return true;
    }

    private Result getUser(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + userName + "'";
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.USER_GET);
    }
}

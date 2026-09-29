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
import com.konylabs.middleware.dataobject.Result;

public class GetAccouts2 implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String accountId = inputParams.get("accountID");
        String username = inputParams.get("userName");
        String deviceId = inputParams.get("deviceID");
        String phone = inputParams.get("phone");
        String userId = "";
        String filter = "";
        if (StringUtils.isNotBlank(deviceId)) {
            userId = getUserId(dcRequest, username);
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isBlank(accountId)) {
            userId = HelperMethods.getUserIdFromSession(dcRequest);
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
            if (StringUtils.isNotBlank(phone)) {
                filter = filter + DBPUtilitiesConstants.AND + "phone" + DBPUtilitiesConstants.EQUAL + phone;
            }
        } else {
            filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "accountPreference asc");

        return status;
    }

    private String getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        return HelperMethods.getFieldValue(user, "id");
    }
}

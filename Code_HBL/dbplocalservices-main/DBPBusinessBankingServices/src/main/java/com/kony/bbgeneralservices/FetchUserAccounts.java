package com.kony.bbgeneralservices;

import java.util.HashMap;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class FetchUserAccounts implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {

        HelperMethods.getUserFromIdentityService(dcRequest);
        HashMap<String, String> dataMap = new HashMap<>();

        String select = "Account_id, AccountName";

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + CommonUtils.getUserId(dcRequest);

        dataMap.put(DBPUtilitiesConstants.SELECT, select);
        dataMap.put(DBPUtilitiesConstants.FILTER, filter);

        return HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_ACCOUNTS_GET);

    }
}

package com.kony.dbputilities.accountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountFeatures implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTTYPE_GET);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String id = inputParams.get("accountTypeId");
        String countryCode = user.get("countryCode");

        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(countryCode)) {
            String filter = "TypeID" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "countryCode"
                    + DBPUtilitiesConstants.EQUAL + countryCode;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        }
        return true;
    }
}

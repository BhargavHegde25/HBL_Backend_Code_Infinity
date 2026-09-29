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

public class GetAccountType implements JavaService2 {

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
        String filter = "";
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String id = inputParams.get(DBPUtilitiesConstants.ID);
        String countryCode = user.get("countryCode");

        if (StringUtils.isNotBlank(id)) {
            filter = DBPUtilitiesConstants.ID + DBPUtilitiesConstants.EQUAL + id;
        }
        if (StringUtils.isNotBlank(countryCode)) {
            if ("".equals(filter)) {
                filter = filter + DBPUtilitiesConstants.AND;
            }
            filter = filter + "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}

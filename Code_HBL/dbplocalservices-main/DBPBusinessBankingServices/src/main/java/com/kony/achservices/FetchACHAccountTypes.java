package com.kony.achservices;

import java.util.HashMap;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchACHAccountTypes implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        try {
            HelperMethods.getUserFromIdentityService(dcRequest);
            HashMap<String, String> dataMap = new HashMap<>();

            String select = "id, accountType";

            dataMap.put(DBPUtilitiesConstants.SELECT, select);

            return HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACH_ACCOUNTTYPES_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}

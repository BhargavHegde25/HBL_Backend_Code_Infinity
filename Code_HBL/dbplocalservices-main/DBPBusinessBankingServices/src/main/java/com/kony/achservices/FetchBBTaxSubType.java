package com.kony.achservices;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchBBTaxSubType implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
            Map<String, String> dataMap = new HashMap<>();
            String taxType = inputParams.get("taxType");
            String filter = "taxType" + DBPUtilitiesConstants.EQUAL + taxType;
            dataMap.put(DBPUtilitiesConstants.FILTER, filter);
            return HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TAX_SUBTYPE_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
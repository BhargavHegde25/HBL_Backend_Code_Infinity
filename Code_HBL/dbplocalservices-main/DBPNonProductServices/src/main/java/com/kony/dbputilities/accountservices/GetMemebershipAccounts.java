package com.kony.dbputilities.accountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMemebershipAccounts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.ALLACCOUNTSVIEW_GET);
        }
        return PostProcess(result);
    }

    private Object PostProcess(Result result) {

        if (HelperMethods.hasRecords(result)) {
            result.getAllDatasets().get(0).setId("Accounts");
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.RECORD_FOUND_IN_DBX, result);
        } else if (HelperMethods.hasError(result)) {
            ErrorCodeEnum.ERR_11025.setErrorCode(result);
        } else {
            result.getAllDatasets().get(0).setId("Accounts");
            ErrorCodeEnum.ERR_11026.setErrorCode(result);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String[] list = { "Account_id", "Membership_id", "Taxid" };
        boolean status = true;
        String filter = "";
        for (int i = 0; i < list.length; i++) {
            String filterKey = list[i];
            String filtervalue = inputParams.get(filterKey);
            if (StringUtils.isNotBlank(filtervalue)) {
                if (!filter.isEmpty()) {
                    filter += DBPUtilitiesConstants.AND;
                }

                filter += filterKey + DBPUtilitiesConstants.EQUAL + filtervalue;
            }
        }
        if (filter.isEmpty()) {
            ErrorCodeEnum.ERR_11024.setErrorCode(result);
            status = false;
        }
        if (status && (StringUtils.isNotBlank(inputParams.get("Membership_id")) || 
        		StringUtils.isNotBlank(inputParams.get("Taxid")))) {
            if (!filter.isEmpty()) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += "(IsOrganizationAccount" + DBPUtilitiesConstants.EQUAL + "true" + DBPUtilitiesConstants.OR
                    + "IsOrganizationAccount" + DBPUtilitiesConstants.EQUAL + "1)";
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return status;
    }

}

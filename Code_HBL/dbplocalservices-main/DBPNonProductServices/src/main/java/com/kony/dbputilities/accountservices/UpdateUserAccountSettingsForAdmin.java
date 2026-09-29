package com.kony.dbputilities.accountservices;

import java.util.Calendar;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUserAccountSettingsForAdmin implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_UPDATE);
        }
        if (!HelperMethods.hasError(result)) {
            result.addParam(new Param("result", "success", "String"));
            result.addParam(new Param("UpdatedBy", HelperMethods.getFieldValue(result, "UpdatedBy"), "String"));
            result.addParam(new Param("LastUpdated", HelperMethods.getFieldValue(result, "LastUpdated"), "String"));
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
        Calendar cal = Calendar.getInstance();
        if (HelperMethods.isAdmin(dcRequest, adminId)) {
            String favouriteStatus = inputParams.get("favouriteStatus");
            String lastUpdated = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
            if ("0".equals(favouriteStatus) || "1".equalsIgnoreCase(favouriteStatus)) {
                inputParams.put("FavouriteStatus", favouriteStatus);
            }
            inputParams.put("Account_id", inputParams.get("id"));
            inputParams.put("EStatementmentEnable", inputParams.get("eStatementEnable"));
            inputParams.put("NickName", inputParams.get("nickName"));
            inputParams.put("UpdatedBy", inputParams.get("UpdatedBy"));
            inputParams.put("ActualUpdatedBY", HelperMethods.getCustomerFromIdentityService(dcRequest).get("UserName"));
            inputParams.put("LastUpdated", lastUpdated);

            HelperMethods.removeNullValues(inputParams);
            return true;
        } else {
            result.addParam(new Param("result", "Invalid user details", "String"));
        }
        return false;
    }

}

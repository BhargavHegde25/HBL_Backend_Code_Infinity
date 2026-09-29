package com.kony.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateBusinessUserPayloadGetService implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String response = null;
        if (preprocess(inputParams, result)) {
            response = HelperMethods.callApiAndGetString(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        }
        postProcess(result, response, dcRequest);
        return result;
    }

    private boolean preprocess(Map<String, String> inputParams, Result result) {
        String userId = inputParams.get("userId");

        if (StringUtils.isBlank(userId)) {
            ErrorCodeEnum.ERR_10049.setErrorCode(result);
            return false;
        }
        inputParams.clear();
        String filterQuery = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        return true;
    }

    private void postProcess(Result result, String response, DataControllerRequest dcRequest) {

        if (response == null) {
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return;
        }

        JsonObject customerActionResponse = new JsonParser().parse(response).getAsJsonObject();

        if (!customerActionResponse.has("opstatus")
                || customerActionResponse.get("opstatus").getAsInt() != 0
                || !customerActionResponse.has("records")) {
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return;
        }
        JsonArray customerActionsArray =
                customerActionResponse.getAsJsonArray("records").getAsJsonArray();
    }
}

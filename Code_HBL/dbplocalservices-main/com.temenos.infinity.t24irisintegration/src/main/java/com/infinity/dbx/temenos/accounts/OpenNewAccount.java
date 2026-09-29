package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;

public class OpenNewAccount implements JavaService2, AccountsConstants, TemenosConstants {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];

        Result result = new Result();
        TemenosUtils temenosUtils = TemenosUtils.getInstance();

        String userId = CommonUtils.getBackendIdFromCache(request, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID,
                "1");
        /*
         * if("".equalsIgnoreCase(userId)) { userId = (String) temenosUtils.retreiveFromSession(MEMBERSHIPID, request);
         * }
         */
        String productLi = params.get(PRODUCT_LI).toString();
        JSONArray products = new JSONArray(productLi);
        JsonArray customerAccounts = new JsonArray();
        Result finalResult = new Result();
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        for (int i = 0; i < products.length(); i++) {

            HashMap<String, Object> inputParams = new HashMap<String, Object>();
            HashMap<String, Object> headerParams = new HashMap<String, Object>();
            request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.POST_LOGIN_FLOW);
            headerParams.put(TemenosConstants.PARAM_AUTHORIZATION, TokenUtils.getT24AuthToken(request));

            JSONObject product = new JSONObject(products.getJSONObject(i).get(PRODUCT).toString());
            String productId = product.get(PRODUCT_ID).toString();
            inputParams.put(PRODUCT_ID, productId);
            if (StringUtils.isBlank(temenosUtils.accountTypesMap.get(productId)))
                temenosUtils.loadAccountTypeProperties(request);
            String accountType = temenosUtils.accountTypesMap.get(productId);
            inputParams.put(USER_ID, userId);

            switch (accountType) {
                case Constants.ACCOUNT_TYPE_SAVINGS:
                    result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                            TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_OPEN_NEW_SAVINGS_ACCOUNT,
                            false);
                    break;
                case Constants.ACCOUNT_TYPE_CHECKING:
                    result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                            TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_OPEN_NEW_CHECKING_ACCOUNT,
                            false);
                    break;
                case Constants.ACCOUNT_TYPE_DEPOSIT:
                    result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                            TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_OPEN_NEW_DEPOSIT_ACCOUNT,
                            false);
                    break;
                case Constants.ACCOUNT_TYPE_LOAN:
                    result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                            TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_OPEN_NEW_LOAN_ACCOUNT, false);
                    break;

            }
            String errmsg = CommonUtils.getParamValue(result, "errmsg");
            if (!"".equalsIgnoreCase(errmsg)) {
                finalResult.addStringParam("dbpErrMsg",
                        "One of the account creation is failed at backend due to technical limitation- Please contact Bank Branch for more information");
                finalResult.addErrMsgParam(
                        "One of the account creation is failed at backend due to technical limitation- Please contact Bank Branch for more information");
                finalResult.addStringParam("dbpErrCode", CommonUtils.getParamValue(result, "errcode"));
                finalResult.addStringParam("errcode", CommonUtils.getParamValue(result, "errcode"));
                finalResult.addOpstatusParam(CommonUtils.getParamValue(result, "opstatus"));
                finalResult.addHttpStatusCodeParam(CommonUtils.getParamValue(result, "httpStatusCode"));
            } else {
                product.put("accountId", CommonUtils.getParamValue(result, "accountId"));
                Gson gson = new Gson();
                customerAccounts.add(gson.fromJson(product.toString(), JsonObject.class));
                finalResult.addAllParams(result.getAllParams());
            }
        }
        finalResult.addParam("customerAccounts", customerAccounts.toString());
        return finalResult;
    }
}

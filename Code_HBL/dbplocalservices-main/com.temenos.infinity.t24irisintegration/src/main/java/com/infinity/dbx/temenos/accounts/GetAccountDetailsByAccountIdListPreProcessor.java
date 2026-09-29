package com.infinity.dbx.temenos.accounts;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.InfinityConstants;

public class GetAccountDetailsByAccountIdListPreProcessor implements DataPreProcessor2, TemenosConstants {

    @SuppressWarnings({ "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        String loginUserId = null;
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            loginUserId = HelperMethods.getCustomerIdFromSession(request);
        } else {
            loginUserId = (String) params.get(InfinityConstants.id);
        }
        
        HashMap<String, Object> inputParams = new HashMap<>();

        String accounts = request.getParameter("accounts");

        if (StringUtils.isNotBlank(loginUserId)) {
            inputParams.put("accounts", accounts);
            request.addRequestParam_("accounts", accounts);
            inputParams.put("customerId", loginUserId);
            request.addRequestParam_("customerId", loginUserId);
            Result newAccountProcessing =
                    CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                            SERVICE_BACKEND_PRODUCTSERVICE, OP_NEW_ACCOUNT_PROCESSING, true);

            String accountsString = newAccountProcessing.getParamValueByName("accounts");
            String newAccounts = newAccountProcessing.getParamValueByName("newAccounts");
            if (StringUtils.isNotBlank(accountsString)) {
                params.put("Account_id", URLEncoder.encode(accountsString, "UTF-8"));
                params.put("newAccounts",
                        StringUtils.isNotBlank(newAccounts) ? URLEncoder.encode(newAccounts, "UTF-8") : "");
                request.addRequestParam_("newAccounts", newAccounts);
                return Boolean.TRUE;
            }
        } else {
            if (StringUtils.isNotBlank(accounts)) {
                JsonArray jsonarray = new JsonParser().parse(accounts).getAsJsonArray();
                StringBuilder accountList = new StringBuilder();
                for (JsonElement jsonelement : jsonarray) {
                    if (StringUtils.isNotBlank(JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"))) {
                        accountList.append(JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"));
                        accountList.append(" ");
                    }
                }

                if(StringUtils.isNotBlank(accountList.toString().trim())) {
                    params.put("Account_id", URLEncoder.encode(accountList.toString().trim(), "UTF-8"));
                    return Boolean.TRUE;
                }
            }
        }
        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        return Boolean.FALSE;
    }
}

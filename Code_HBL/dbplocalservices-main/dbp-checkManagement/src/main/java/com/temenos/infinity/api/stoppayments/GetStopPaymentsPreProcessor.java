package com.temenos.infinity.api.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetStopPaymentsPreProcessor extends TemenosBasePreProcessor {
    @SuppressWarnings("unchecked")
    public boolean execute(@SuppressWarnings("rawtypes")HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        try {
            super.execute(params, request, response, result);
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
            String limit = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_LIMIT);
            if (accounts != null) {
                String accountId = CommonUtils.getParamValue(params,
                        StopPaymentConstants.PARAM_T24CHECKREASON_ACCOUNT_ID);
                String[] accountIds = null;
                if (accountId.contains("-")) {
                    accountIds = accountId.split("-");
                    if (accountIds != null && accountIds.length > 0) {
                        accountId = accountIds[1];
                    }
                }
                Account account = accounts.get(accountId);
                String InputAccountId = account.getAccountId(); 
                if (StringUtils.isNotBlank(InputAccountId)) {
                    params.put(StopPaymentConstants.PARAM_T24CHECKREASON_ACCOUNT_ID, InputAccountId);
                    if (StringUtils.isNotBlank(limit)) {
                        request.addRequestParam_(StopPaymentConstants.PARAM_PAGE_SIZE, limit);
                    }
                    request.getHeaderMap().put("accountId", accountId);
                    return Boolean.TRUE;
                } else {
                    result.addOpstatusParam(0);
                    return Boolean.FALSE;
                }
            }

            return Boolean.FALSE; 
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}

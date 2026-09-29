package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookRequestsPreProcessor extends TemenosBasePreProcessor {
    @SuppressWarnings("unchecked")
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
        try {
            super.execute(params, request, response, result);
//            TemenosUtils temenosUtils = TemenosUtils.getInstance();
//          HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
            String limit = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_LIMIT);
            String chequeIssueId = CommonUtils.getParamValue(params, StopPaymentConstants.EXTERNAL_ID);
            String filterValue = "";
            String filter = StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_FILTER;
            String accountId = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_T24CHECKREASON_ACCOUNT_ID);
            String[] accountIds = null;
            if (accountId.contains("-")) {
                accountIds = accountId.split("-");
                if (accountIds != null && accountIds.length > 0) {
                    accountId = accountIds[1];
                }
            }
			/*
			 * if (accounts != null && StringUtils.isNotBlank(accountId)) { Account account
			 * = accounts.get(accountId); String InputAccountId = account.getAccountId(); if
			 * (!StringUtils.isNotBlank(InputAccountId)) { result.addOpstatusParam(0);
			 * return Boolean.FALSE; }
			 * 
			 * }
			 */
            // add account Id in request
            if (StringUtils.isNotBlank(accountId))
                filterValue = StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_ACCOUNT_ID + "=" + accountId;

            // Add cheque Issue Id in request if available
            if (StringUtils.isNotBlank(chequeIssueId)) {
                if (Pattern.matches("^[a-zA-Z0-9.]+$", chequeIssueId)) {
                    if (StringUtils.isNotBlank(filterValue)) {
                        filterValue = filterValue + "&" + StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID + "="
                                + chequeIssueId;
                    } else {
                        filterValue = StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID + "=" + chequeIssueId;
                    }
                } else {
                    return Boolean.FALSE;
                }
            }
            params.put(filter, filterValue);
            if (StringUtils.isNotBlank(limit)) {
                request.addRequestParam_(StopPaymentConstants.PARAM_PAGE_SIZE, limit);
            }
            return Boolean.TRUE;

        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}

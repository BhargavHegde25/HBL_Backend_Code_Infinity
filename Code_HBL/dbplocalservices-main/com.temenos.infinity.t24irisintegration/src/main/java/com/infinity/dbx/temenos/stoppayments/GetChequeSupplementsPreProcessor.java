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
public class GetChequeSupplementsPreProcessor extends TemenosBasePreProcessor {
    @SuppressWarnings("unchecked")
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
        try {
            super.execute(params, request, response, result);
            TemenosUtils temenosUtils = TemenosUtils.getInstance();

            // Get Param Values from request
            String accountId = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_T24CHECKREASON_ACCOUNT_ID);
            String chequeNumber = CommonUtils.getParamValue(params,
                    StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER);
            String chequeRange = CommonUtils.getParamValue(params,
                    StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_RANGE);
            String status = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS);
            String limit = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_LIMIT);

            String filter = StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_FILTER;
            String filterValue = null;
            HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
            if (accounts != null) {

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

                    // add account Id in request
                    filterValue = StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_ACCOUNT_ID + "=" + InputAccountId;

                    // Add cheque number in request if available
                    if (StringUtils.isNotBlank(chequeNumber)) {
                        if (StringUtils.isNumeric(chequeNumber)) {
                            filterValue = filterValue + "&" + StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER
                                    + "=" + chequeNumber;
                        } else {
                            return Boolean.FALSE;
                        }

                    }

                    // Add cheque range in request if available
                    if (StringUtils.isNotBlank(chequeRange)) {
                        String chequeRangeFinal = StringUtils.EMPTY;
                        if ((Pattern.matches("^[0-9-]+$", chequeRange))) {
                            if (chequeRange.contains("-")) {
                                chequeRangeFinal = chequeRange.replaceFirst("-", " ");
                            } else {
                                chequeRangeFinal = chequeRange;
                            }
                            filterValue = filterValue + "&" + StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_RANGE
                                    + "=" + chequeRangeFinal;
                        } else {
                            return Boolean.FALSE;
                        }
                    }

                    // Amount Filter logic needs to be done in post processor
                    // result.

                    // Add Status in request if available
                    if (StringUtils.isNotBlank(status)) {
                        if (Pattern.matches("^[A-Z]+$", status)) {
                            filterValue = filterValue + "&" + StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS + "="
                                    + status;
                        } else {
                            return Boolean.FALSE;
                        }
                    } 
                    params.put(filter, filterValue);
                    if (StringUtils.isNotBlank(limit)) {
                        if (StringUtils.isNumeric(limit)) {
                            request.addRequestParam_(StopPaymentConstants.PARAM_PAGE_SIZE, limit);
                        } else {
                            return Boolean.FALSE;
                        } 
                    }
                    return Boolean.TRUE;
                }
            }

            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}

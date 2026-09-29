package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetBlockedFundsPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        try {
            String accountId = params.get(TransactionConstants.ACCOUNTID) != null
                    ? params.get(TransactionConstants.ACCOUNTID).toString() : "";
            if (StringUtils.isBlank(accountId)) {
                result.addOpstatusParam(0);
                result.addHttpStatusCodeParam(200);
                result.addErrMsgParam("Misssing input param " + TransactionConstants.ACCOUNTID);
                return Boolean.FALSE;
            }
            super.execute(params, request, response, result);
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            boolean accAvailable = temenosUtils.checkCustomerAccount(request, accountId);
            if (!accAvailable) {
                result.addOpstatusParam(0);
                result.addHttpStatusCodeParam(200);
                result.addErrMsgParam("Account " + accountId + " is not related to customer");
                return Boolean.FALSE;
            }

            // Construct the filter variable(Iris query Parameters)
            String filterValue = TransactionConstants.ACCOUNT_ID + "=" + accountId;
            String lockReason = params.get(TransactionConstants.BLOCKED_FUNDS_LOCK_REASON) != null
                    ? params.get(TransactionConstants.BLOCKED_FUNDS_LOCK_REASON).toString() : "";
            String lockedEventId = params.get(TransactionConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID) != null
                    ? params.get(TransactionConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID).toString() : "";
            String searchStartDate = params.get(TransactionConstants.SEARCH_START_DATE_PARAM) != null
                    ? params.get(TransactionConstants.SEARCH_START_DATE_PARAM).toString() : "";
            String searchEndDate = params.get(TransactionConstants.SEARCH_END_DATE_PARAM) != null
                    ? params.get(TransactionConstants.SEARCH_END_DATE_PARAM).toString() : "";

            if (StringUtils.isNotBlank(lockReason)) {
                if (Pattern.matches("^[a-zA-Z0-9_\\s]+$", lockReason)) {
                    filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_LOCK_REASON + "=" + lockReason;
                } else {
                    return Boolean.FALSE;
                } 
            }
            if (StringUtils.isNotBlank(lockedEventId)) {
                if (Pattern.matches("^[a-zA-Z0-9]+$", lockedEventId)) {
                    filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_EVENT_ID + "=" + lockedEventId;
                } else {
                    return Boolean.FALSE;
                }
            }
            if (StringUtils.isNotBlank(searchStartDate) && StringUtils.isNotBlank(searchEndDate)) {
                filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_DATE_RANGE + "=" + searchStartDate.replace("-", "") + " "
                        + searchEndDate.replace("-", "");
            }
            params.put(TransactionConstants.PARAM_FILTER, filterValue);
            return Boolean.TRUE;
        } catch (Exception e) {
            alert.prepareError("Exception occured in GetBlockedFunds PreProcessor" + e).log();
            return Boolean.FALSE;
        }
    }
}

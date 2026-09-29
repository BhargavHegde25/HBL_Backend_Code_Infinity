package com.temenos.infinity.api.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

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
public class CreateChequeBookRequestPreProcessor extends TemenosBasePreProcessor {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
    	diagnostic.prepareDebug("Entering createchequebookrequest T24 preprocessor").log();
    	diagnostic.prepareDebug("input params "+request).log();
        super.execute(params, request, response, result);

        diagnostic.prepareDebug("params "+params).log();
        // set Cheque Status as 70 - cheque issued
        params.put(StopPaymentConstants.PARAM_CHEQUE_STATUS, StopPaymentConstants.CHEQUE_STATUS);
        String chequeIssueId = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID);
        String validateRequest = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_ISSUE_VALIDATE);
        String ChequeIssueIds[] = chequeIssueId.split("\\.");
        String AccountId = "";
        if (ChequeIssueIds != null && ChequeIssueIds.length > 0) {
            AccountId = ChequeIssueIds[1];
        }
        /*TemenosUtils temenosUtils = TemenosUtils.getInstance();
        HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
        if (accounts != null) {
            String[] accountIds = null;
            if (AccountId.contains("-")) {
                accountIds = AccountId.split("-");
                if (accountIds != null && accountIds.length > 0) {
                    AccountId = accountIds[1];
                }
            }
            Account account = accounts.get(AccountId);*/
            String InputAccountId = AccountId;
            if (StringUtils.isNotBlank(InputAccountId)) {
                if (StringUtils.isNotBlank(validateRequest) && validateRequest.equalsIgnoreCase("true")) {
                    request.addRequestParam_(StopPaymentConstants.PARAM_VALIDATE_ONLY, validateRequest);
                    params.put(StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID, ChequeIssueIds[0] + "." + InputAccountId);
                } else {
                    if (ChequeIssueIds.length > 2) {
                        params.put(StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID,
                                ChequeIssueIds[0] + "." + InputAccountId + "." + ChequeIssueIds[2]);
                    } else {
                        params.put(StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID, chequeIssueId);
                        return Boolean.TRUE;
                    } 
                }
			/*
			 * } else { result.addOpstatusParam(0); result.addHttpStatusCodeParam(200);
			 * return Boolean.FALSE; }
			 */
            return Boolean.TRUE;
        } else {
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            return Boolean.FALSE;

        }
    }
}

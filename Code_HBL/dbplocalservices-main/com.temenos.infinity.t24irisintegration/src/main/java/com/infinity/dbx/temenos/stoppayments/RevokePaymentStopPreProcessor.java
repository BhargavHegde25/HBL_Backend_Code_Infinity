package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

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
public class RevokePaymentStopPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
        try{
        super.execute(params, request, response, result);
        
        String accountId = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_T24REVOKE_ACCOUNT_ID);
        /*TemenosUtils temenosUtils = TemenosUtils.getInstance();
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
            String InputAccountId = account.getAccountId();*/
        
        if (StringUtils.isNotBlank(accountId)) {
            params.put(StopPaymentConstants.PARAM_T24REVOKE_ACCOUNT_ID, accountId); 
        }
        
        String validateRequest = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_ISSUE_VALIDATE);
        
        if (StringUtils.isNotBlank(validateRequest) && validateRequest.equalsIgnoreCase("true")) {
            request.addRequestParam_(StopPaymentConstants.PARAM_VALIDATE_ONLY, validateRequest);
        } 
        
        } catch (Exception e) { 
            alert.prepareError("Exception occurred in Revoke Payment Stop PreProcesor:" + e).log();
            return false;
        }

        return Boolean.TRUE;
}
}

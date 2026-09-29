package com.temenos.infinity.api.stoppayments;

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
public class CreatePaymentStopPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
        try{
        super.execute(params, request, response, result);
        
        String accountId = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT);
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
                params.put(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT, accountId); 
            }
        

        String validateRequest = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_ISSUE_VALIDATE);
        
        if (StringUtils.isNotBlank(validateRequest) && validateRequest.equalsIgnoreCase("true")) {
            request.addRequestParam_(StopPaymentConstants.PARAM_VALIDATE_ONLY, validateRequest);
        } 
        
        //set stop Payment Reason 
        //Cheque reason is sent from UI: 
        /*String checkReason = CommonUtils.getParamValue(params, StopPaymentConstants.CHECK_REASON);
            if (checkReason.equalsIgnoreCase(StopPaymentConstants.PARAM_CHECKREASON_DUPLICATE)) {
                params.put(StopPaymentConstants.CHECK_REASON, StopPaymentConstants.PARAM_T24STOPREASON_DUPLICATE);
            } else if (checkReason.equalsIgnoreCase(StopPaymentConstants.PARAM_CHECKREASON_INSUFFICIENT_FUNDS)) {
                params.put(StopPaymentConstants.CHECK_REASON, StopPaymentConstants.PARAM_T24CHECKREASON_INSUFFICIENT_FUNDS);
            } else if (checkReason.equalsIgnoreCase(StopPaymentConstants.PARAM_CHECKREASON_LOST_OR_STOLEN)) {
                params.put(StopPaymentConstants.CHECK_REASON, StopPaymentConstants.PARAM_T24STOPREASON_STOLEN);
            } else if (checkReason.equalsIgnoreCase(StopPaymentConstants.PARAM_CHECKREASON_DEFECTIVE_GOODS)) {
                params.put(StopPaymentConstants.CHECK_REASON, StopPaymentConstants.PARAM_T24STOPREASON_DEFECTIVE_GOODS);
            } else {
                params.put(StopPaymentConstants.CHECK_REASON, StopPaymentConstants.PARAM_T24STOPREASON_OTHERS);
            }*/
        } catch (Exception e) { 
            alert.prepareError("Exception occurred in One Time Transfer PreProcesor:" + e).log();
            return false;
        }

        return Boolean.TRUE;
}
}

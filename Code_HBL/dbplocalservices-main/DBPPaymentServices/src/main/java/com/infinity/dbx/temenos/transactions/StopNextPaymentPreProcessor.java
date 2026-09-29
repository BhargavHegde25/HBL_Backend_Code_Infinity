package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class StopNextPaymentPreProcessor extends TemenosBasePreProcessor{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {

        try {
        	diagnostic.prepareDebug("In " + StopNextPaymentPreProcessor.class.getName()).log();
            super.execute(params, request, response, result);
            String Id = (String) params.get(TransactionConstants.PAYMENT_ID);
            if(Id.isEmpty()) {
            	result.addOpstatusParam(0);
    			result.addHttpStatusCodeParam(200);
    			result.addErrMsgParam("Missing ID");
    			return Boolean.FALSE;
    		}
    		else {
    			int index = Id.indexOf(".");
                params.put(TransactionConstants.ACCOUNT_ID,Id.substring(0, index));
    		}     
            
        } catch (Exception e) {
        	alert.prepareError("Exception in " + StopNextPaymentPreProcessor.class.getName(), e).log();
        }

        return Boolean.TRUE;
    }
}

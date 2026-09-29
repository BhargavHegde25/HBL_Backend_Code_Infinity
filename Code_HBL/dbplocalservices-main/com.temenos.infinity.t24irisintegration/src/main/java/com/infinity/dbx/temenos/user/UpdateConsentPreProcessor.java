package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import org.json.JSONObject;

public class UpdateConsentPreProcessor extends TemenosBasePreProcessor {

    private static final String CONSENTS = "consent";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean execute(  HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {

        try {
            diagnostic.prepareDebug("In " + UpdateConsentPreProcessor.class.getName()).log();
            JSONObject consent = null;
            super.execute(params, request, response, result);
            
            params.put(CONSENTS,((String) params.get(CONSENTS)).replace("\'","\""));
	}catch (Exception e) {
        alert.prepareError("Exception in " + UpdateConsentPreProcessor.class.getName(), e).log();
    }
        return Boolean.TRUE;   
	}

}

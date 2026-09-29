package com.temenos.infinity.wealth.mock.processor.post;
import org.json.JSONObject;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ConfirmOrdersIPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	try {
        JSONObject assetObj = new JSONObject();
        assetObj.put("message", "Orders placed successfully.");
        Result final_result = Utilities.constructResultFromJSONObject(assetObj);
        final_result.addOpstatusParam("0");
        final_result.addHttpStatusCodeParam("200");
        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        diagnostic.prepareDebug("==========> ConfirmOrdersIPPostProcessor Mock - Executed").log();
        return final_result;
    	}
    	 catch (Exception e) {
    	        e.getMessage();
    	        alert.prepareError("==========> ConfirmOrdersIPPostProcessor Mock - Error: " + e.getMessage()).log();
    	   
    	    }
    	return result;
    }
        }
    
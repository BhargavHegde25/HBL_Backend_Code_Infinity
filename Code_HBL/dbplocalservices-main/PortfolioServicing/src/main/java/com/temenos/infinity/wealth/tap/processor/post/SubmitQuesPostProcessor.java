package com.temenos.infinity.wealth.tap.processor.post;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class SubmitQuesPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	try {
    		 diagnostic.prepareDebug("==========> SubmitQuesPostProcessor TAP - Entered ").log();
    		Record bodyRec =  result.getRecordById("innerdata");
	       // String portfolioId = request.getParameterValues("portfolioCode")[0];
	        if(bodyRec != null) {
	        JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
	        diagnostic.prepareDebug("==========> SubmitQuesPostProcessor TAP - Data available ").log();
	        String score = bodyObj.get("card").toString();
	        JSONObject assetObj = new JSONObject();
	        
	        assetObj.put("score", score);
	        Result final_result = Utilities.constructResultFromJSONObject(assetObj);
	        final_result.addOpstatusParam("0");
	        final_result.addHttpStatusCodeParam("200");
	        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
	        diagnostic.prepareDebug("==========> SubmitQuesPostProcessor TAP - Exited ").log();
	        return final_result;
	     }

	    }
	    catch (Exception e) {
	    	alert.prepareError("==========> SubmitQuesPostProcessor TAP - Error: " + e.getMessage()).log();
	        e.getMessage();
	   
	    }
    	result.removeParamByName("errmsg");
	    return result;
		}
	  }
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	try {

    		Record bodyRec =  result.getRecordById("innerdata");
	       // String portfolioId = request.getParameterValues("portfolioCode")[0];
	        if(bodyRec != null) {
	        JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
	        
	        String score = bodyObj.get("card").toString();
	        JSONObject assetObj = new JSONObject();
	        
	        assetObj.put("score", score);
	        Result final_result = Utilities.constructResultFromJSONObject(assetObj);
	        final_result.addOpstatusParam("0");
	        final_result.addHttpStatusCodeParam("200");
	        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
	        
	        return final_result;
	     }

	    }
	    catch (Exception e) {
	        e.getMessage();
	   
	    }
    	result.removeParamByName("errmsg");
	    return result;
		}
	  }
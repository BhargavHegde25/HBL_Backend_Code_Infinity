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
public class GetQuestionCodePostProcessor implements DataPostProcessor2 {
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	try {
    	Dataset quesSet = result.getDatasetById("body");
    	JSONArray res = ResultToJSON.convertDataset(quesSet);
    	ArrayList<HashMap<String, String>> pastProposalList = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> hMap = new HashMap<String, String>();
        JSONObject bodyObj = res.getJSONObject(res.length()-1);
        JSONObject assetObj = new JSONObject();
        String levelss = (bodyObj.get("code").toString()!=null || bodyObj.get("code").toString()!= "")?bodyObj.get("code").toString():"";
        assetObj.put("code", levelss);
        Result final_result = Utilities.constructResultFromJSONObject(assetObj);
        final_result.addOpstatusParam("0");
        final_result.addHttpStatusCodeParam("200");
        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        return final_result;
    	}
    	 catch (Exception e) {
    	        e.getMessage();
    	   
    	    }
    	return result;
    }
        }
    
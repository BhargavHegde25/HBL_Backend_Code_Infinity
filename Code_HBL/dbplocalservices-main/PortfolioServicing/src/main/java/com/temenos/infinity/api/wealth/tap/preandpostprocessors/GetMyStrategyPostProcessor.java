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

public class GetMyStrategyPostProcessor implements DataPostProcessor2 {
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	try {
        String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
                request);
        Dataset bodySet = result.getDatasetById("body");
             Record bodyRec = bodySet.getRecord(0);
        String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
        if(bodyRec != null) {
        	JSONObject assetObj = new JSONObject();
        JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
        
        if(bodyObj.has("extname")) {
        String extnames = bodyObj.get("extname").toString();
        String scodes = bodyObj.get("strategyId").toString();
        String level = bodyObj.has("extrisklevel") ? bodyObj.getString("extrisklevel") : "20";

        ArrayList<HashMap<String, String>> strategyList = strategyList(INF_WLTH_STRATEGIES, level);
        
        

		int strategyList_size = strategyList.size();
		HashMap<String, String> recStrategyName = strategyList.get(strategyList_size - 1);
        if(extnames == null) {
            assetObj.put("isNewUser", "true");
        } else {
            assetObj.put("isNewUser", "false");
        }
        assetObj.put("strategyId", scodes);
      
        	assetObj.put("myRiskLevel",level );

        assetObj.put("myStrategyName",recStrategyName.get("myStrategyName"));
        strategyList.remove(strategyList_size - 1);
        JSONArray jsArray = new JSONArray(strategyList);
        assetObj.put("strategyList", sortingArray(jsArray,"minVal"));
        }
        else {
        	 assetObj.put("isNewUser", "true");
        }
        
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
    return result;
}
     private ArrayList<HashMap<String, String>> strategyList(String INF_WLTH_STRATEGIES, String myRiskLevel) {
         ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
         String myStrategyName = "";
         if (INF_WLTH_STRATEGIES != null && INF_WLTH_STRATEGIES.length() > 0) {
             JSONObject json = new JSONObject(INF_WLTH_STRATEGIES);
             Iterator<String> keys = json.keys();
             while (keys.hasNext()) {
                 String key = keys.next();
                 String values = json.get(key).toString();
                 String value[] = values.split("~");
                 int min = Integer.parseInt(value[0]);
                 int max = Integer.parseInt(value[1]);
                 HashMap<String, String> strategyList_hm = new HashMap<String, String>();
                 strategyList_hm.put("strategyName", key);
                 strategyList_hm.put("minVal", value[0]);
                 strategyList_hm.put("maxVal", value[1]);
                 al.add(strategyList_hm);
                
                 if (min <= Integer.parseInt(myRiskLevel) && Integer.parseInt(myRiskLevel) <= max) {
 					myStrategyName = key;
 				}
 			}
 			HashMap<String, String> myStrategyName_hm = new HashMap<String, String>();
 			myStrategyName_hm.put("myStrategyName", myStrategyName);
 			al.add(myStrategyName_hm);
             
         }
         return al;
     }
     public JSONArray sortingArray(JSONArray jsonArr, String sortBy) {
         JSONArray sortedJsonArray = new JSONArray();
         List<JSONObject> jsonValues = new ArrayList<JSONObject>();
         for (int i = 0; i < jsonArr.length(); i++) {
             jsonValues.add(jsonArr.getJSONObject(i));
         }
         Collections.sort( jsonValues, new Comparator<JSONObject>() {
             private final String KEY_NAME = sortBy;
             @Override
             public int compare(JSONObject a, JSONObject b) {
                 Integer valA = null;
                 Integer valB = null;
                 try {
                     valA = Integer.parseInt(a.get(KEY_NAME).toString());
                     valB = Integer.parseInt(b.get(KEY_NAME).toString());
                 } 
                 catch (JSONException e) {
                     e.getMessage();
                 }
                 return valA.compareTo(valB);
             }
         });
         for (int i = 0; i < jsonArr.length(); i++) {
             sortedJsonArray.put(jsonValues.get(i));
         }
         return sortedJsonArray;
         }
 }
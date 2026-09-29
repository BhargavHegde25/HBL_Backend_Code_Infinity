package com.temenos.infinity.wealth.tap.processor.post;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.temenos.logger.Logger;
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
public class GetRecomStratAssetPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	diagnostic.prepareDebug("==========> GetRecomStratAssetPostProcessor TAP - Entered ").log();
    	Dataset assetSet = result.getDatasetById("body");
    	if(assetSet!=null) {
        JSONArray assetArr = ResultToJSON.convertDataset(assetSet);
        JSONArray assetArray = new JSONArray();
        Record bodyRec = assetSet.getRecord(0);
        JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
    	diagnostic.prepareDebug("==========> GetRecomStratAssetPostProcessor TAP - No. of records returned initially: " + assetArr.length() ).log();
         for(int i=0;i<assetArr.length();i++) {
        	 JSONObject bodyJSON = assetArr.getJSONObject(i);
        	 String weight = (bodyJSON.has("valueN"))?bodyJSON.get("valueN").toString():"0";
        	 String assetName = (bodyJSON.has("structureDenom")) ?bodyJSON.get("structureDenom").toString():null;
        	// String[] resultd = assetName.split("_");
        	// assetName = resultd[1].toLowerCase();
        	 //bodyJSON.remove("valueN");
        	 //bodyJSON.remove("marketSegmentDenom");
        	 bodyJSON.put("assetName",assetName);
        	 bodyJSON.put("weight",weight);
             //pastProposalList.put(hMap);
            if(!bodyJSON.has("valueN")) {
          
                assetArr.remove(i);
         
            }
            bodyJSON.remove("valueN");
       	    bodyJSON.remove("structureDenom");
            if(bodyJSON.has("LEVEL_N")) {
                if(Integer.parseInt(bodyJSON.get("LEVEL_N").toString()) == 1) {

                    //assetArr.remove(i);
                	
                	assetArray.put(bodyJSON);
                } 
            }
            
        }
        
        
        JSONObject assetObj = new JSONObject();
        assetObj.put("assets", assetArray);
        diagnostic.prepareDebug("==========> GetRecomStratAssetPostProcessor TAP - No. of records returned in recommended: " + assetArray.length() ).log();
        //result.removeDatasetById("assets");
        Result final_result = Utilities.constructResultFromJSONObject(assetObj);
        final_result.addOpstatusParam("0");
        final_result.addHttpStatusCodeParam("200");
        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        diagnostic.prepareDebug("==========> GetRecomStratAssetPostProcessor TAP - Exited ").log();
        return final_result;
    	}
    	else
    	{
    		Result final_result = new Result();	
    		final_result.addOpstatusParam("0");
            final_result.addHttpStatusCodeParam("200");
            final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
    		 diagnostic.prepareDebug("==========> GetRecomStratAssetPostProcessor TAP - No data from TAP ").log();
    	     return final_result;
    	}
    }
        }
    
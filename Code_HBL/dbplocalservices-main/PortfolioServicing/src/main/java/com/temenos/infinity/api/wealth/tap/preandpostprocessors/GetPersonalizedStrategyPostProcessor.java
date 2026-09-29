/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetPersonalizedStrategyPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset bodySet = result.getDatasetById("body");
		JSONArray bodyArr = ResultToJSON.convertDataset(bodySet);
		JSONArray personalizedArr = new JSONArray();
		JSONObject personalizedObj = new JSONObject();
		String idString = "";
		for (int i = 0; i < bodyArr.length(); i++) {
			JSONObject bodyObj = bodyArr.getJSONObject(i);
			if (bodyObj.has("parentId")) {
				if (Integer.parseInt(bodyObj.getString("level")) >= 1) {
					if (!bodyObj.has("targetWeight")) {
						bodyObj.put("targetWeight", "0");
					}
					if(bodyObj.has("targetWeight")) {
						 String valType = bodyObj.getString("targetWeight");
			        	 Double wght = Double.parseDouble(valType);
			        	 Double wght1 = (double) Math.round(wght * 100) / 100;
			        	 bodyObj.put("targetWeight",wght1.toString());
					}
					/*
					 * if (!bodyObj.has("marketSegmentId")) { bodyObj.put("marketSegmentId", "0"); }
					 */
					personalizedArr.put(bodyObj);
					if (bodyObj.has("constraintId")) {
						String constraintID = bodyObj.getString("constraintId");
						idString = idString.equals("") ? constraintID : idString.concat(",").concat(constraintID);
					}
				}
			}
		}
		String[] idVal = idString.split(",");
		personalizedObj.put("personalizedStrategy", personalizedArr);
		personalizedObj.put("idVal", idString);
		personalizedObj.put("idCnt", idVal.length);
		Result personalizedStrategy = Utilities.constructResultFromJSONObject(personalizedObj);
		personalizedStrategy.addOpstatusParam("0");
		personalizedStrategy.addHttpStatusCodeParam("200");
		personalizedStrategy.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return personalizedStrategy;
	}

}

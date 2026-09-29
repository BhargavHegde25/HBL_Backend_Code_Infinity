package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetStrategyQuestionsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
	
		JSONObject bodyObj = ResultToJSON.convertRecord(result.getRecordById("body"));
		JSONArray elementArr =bodyObj.getJSONArray("elements");
		JSONArray questionArr = new JSONArray();
		for (int i = 0; i < elementArr.length(); i++) {
			JSONObject questionObj = new JSONObject();
			JSONObject elementObj = elementArr.getJSONObject(i);
			String questionCode = elementObj.getString("denom");
			JSONArray labelArr = elementObj.getJSONArray("label");
			String question = "";
			for (int j = 0; j < labelArr.length(); j++) {
				JSONObject labelObj = labelArr.getJSONObject(j);
				if (labelObj.getString("languageCode").equalsIgnoreCase("en")) {
					question = labelObj.getString("denom");
				}
			}
			if(!questionCode.equalsIgnoreCase("Profile Description")) {
			questionObj.put("question", question);
			questionObj.put("questionCode", questionCode);
			JSONArray optionArr = new JSONArray();
			JSONArray permValuesArr = elementObj.getJSONArray("permValues");
			for (int l = 0; l < permValuesArr.length(); l++) {
				JSONObject permValuesObj = permValuesArr.getJSONObject(l);
				String defValueF = permValuesObj.get("defValueF").toString();
				String name = permValuesObj.getString("name");
				String desc = "";
				JSONArray pVlabelArr = permValuesObj.getJSONArray("label");
				for (int k = 0; k < pVlabelArr.length(); k++) {
					JSONObject pVlabelObj = pVlabelArr.getJSONObject(k);
					if (pVlabelObj.getString("languageCode").equalsIgnoreCase("en")) {
						desc = pVlabelObj.getString("denom");
					}

				}
				if(!desc.equalsIgnoreCase("Not Defined")) {
				JSONObject optionObj = new JSONObject();
				optionObj.put("desc", desc);
				optionObj.put("defValueF", defValueF);
				optionObj.put("name", name);
				optionArr.put(optionObj);
				}
			}
			questionObj.put("option", optionArr);
			questionArr.put(questionObj);
			}
		}
		JSONObject resultObj = new JSONObject();
		resultObj.put("code", "PCK_DS_INVEST_PROF");
		resultObj.put("status", "success");
		resultObj.put("questions", questionArr);
		Result linkRes = Utilities.constructResultFromJSONObject(resultObj);
		linkRes.addOpstatusParam("0");
		linkRes.addHttpStatusCodeParam("200");
		linkRes.addParam("getStrategy_flag", "true");
		linkRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return linkRes;

	}
}

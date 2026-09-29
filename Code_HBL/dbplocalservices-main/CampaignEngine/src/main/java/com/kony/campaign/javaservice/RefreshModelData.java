package com.kony.campaign.javaservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RefreshModelData implements JavaService2 {
	
	private static Map<String, String> modelData = null;
	private static final String serviceName = "CampaignDBService";
	private static final String operationName = "dbxdb_model_get";
	private static final Logger LOGGER = Logger.getLogger(RefreshModelData.class);
	Result result = new Result();
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		GetDatafromDB(true);
		return result;
	}

	public synchronized void GetDatafromDB(boolean flag) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("$select", "id, source");
		if (flag || modelData == null ) {
			try {
				Result dbResponse = CampaignUtil.invokeService(serviceName, operationName, requestParameters);
				JSONObject responseObj = new JSONObject(ResultToJSON.convert(dbResponse));
				if (responseObj.has("model") && responseObj.optJSONArray("model").length() > 0) {
					JSONArray modelArray = responseObj.optJSONArray("model");
					modelData = new HashMap<>();
					for (int i = 0; i < modelArray.length(); i++) {
						String id = modelArray.optJSONObject(i).optString("id");
						String source = modelArray.optJSONObject(i).optString("source");
						modelData.put(id, source);
					}
					result.addParam(new Param("model", modelData.toString()));
					result.addParam(new Param("success", "true"));
				} else {
					LOGGER.error("Error while fetching model data!!");
					result.addParam(new Param("success", "false"));
				}

			} catch (Exception e) {
				LOGGER.error("Caught exception at get Model Source : ", e);
				result.addParam(new Param("success", "false"));
			}
		}

	}
	
	public static Map<String, String> getModelData() {
		return modelData;
	}

	public static void setModelData(Map<String, String> modelData) {
		RefreshModelData.modelData = modelData;
	}


}
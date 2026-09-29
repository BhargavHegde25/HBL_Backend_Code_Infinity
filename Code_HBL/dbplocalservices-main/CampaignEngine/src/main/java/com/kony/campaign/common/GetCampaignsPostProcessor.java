package com.kony.campaign.common;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.campaign.util.CAMPAIGN_COLUMN_REPLACER;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCampaignsPostProcessor implements DataPostProcessor2{

	@Override
	public Object execute(Result res, DataControllerRequest arg1, DataControllerResponse arg2) throws Exception {
		
		if(Integer.parseInt(res.getParamValueByName(CampaignConstants.OPSTATUS)) == 0 && 
			res.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET) != null) {
				res.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET).getAllRecords().forEach(
			campRec -> { 
				if(campRec.getParamValueByName(CampaignConstants.ONLINE_CONTENT_STRING) != null ) {					
					Dataset onlineDS = campRec.getDatasetById(CampaignConstants.GETCAMPAIGNS_ONLINE_CONTENT);
					JsonArray oCArray = new JsonParser().parse(campRec.getParam(CampaignConstants.ONLINE_CONTENT_STRING).getObjectValue().toString()).getAsJsonArray();
					Map<String, JsonObject> ocMap = CampaignUtil.getOCMap(oCArray);
                    overrideColNames(onlineDS, ocMap);                    
                    campRec.removeParam(campRec.getParam(CampaignConstants.ONLINE_CONTENT_STRING));
				  }	
				});
		}
		return res;
	}

	private void overrideColNames(Dataset onlineDS, Map<String, JsonObject> ocMap) {
		for (Record ocRec : onlineDS.getAllRecords()) {                    
		  JsonObject ocJsonObject = ocMap.get(ocRec.getParamValueByName(CampaignConstants.ONLINE_CONTENT_ID));
		  for ( CAMPAIGN_COLUMN_REPLACER onlineContentReplacerObj : CAMPAIGN_COLUMN_REPLACER.values()) {
			// current OnlineContent dataset as has non null value then check replace it with onlineContentString value 
			  // to solve the isue ARB-15131
			  if(ocRec.getParamValueByName(onlineContentReplacerObj.getColName()) != null){  
				ocRec.addParam(onlineContentReplacerObj.getColName(),
				 ocJsonObject.get(onlineContentReplacerObj.getColName()) != null ? 
						 ocJsonObject.get(onlineContentReplacerObj.getColName()).getAsString() : "");
			  }			
		  }                      
		}
	}
	
}

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
import com.konylabs.middleware.dataobject.Result;

public class DefaultCampaignsPostProcessor implements DataPostProcessor2{

	@Override
	public Object execute(Result res, DataControllerRequest arg1, DataControllerResponse arg2) throws Exception {
		
		if(Integer.parseInt(res.getParamValueByName(CampaignConstants.OPSTATUS)) == 0 && 
			res.getDatasetById(CampaignConstants.DEFAULTCAMPAIGN_DATASET) != null) {
	
			if(res.getParamValueByName(CampaignConstants.ONLINE_CONTENT_STRING) != null ) {
				JsonArray oCArray = new JsonParser().parse(
						res.getParamByName(CampaignConstants.ONLINE_CONTENT_STRING).getObjectValue().toString()).getAsJsonArray();
				Map<String, JsonObject> ocMap = CampaignUtil.getOCMap(oCArray);
				res.getDatasetById(CampaignConstants.DEFAULTCAMPAIGN_DATASET).getAllRecords().forEach(
						campRec -> {
							JsonObject ocJsonObject = ocMap.get(campRec.getParamValueByName(CampaignConstants.ONLINE_CONTENT_ID));
							if(campRec.getParamValueByName(CAMPAIGN_COLUMN_REPLACER.OC_TARGET_URL.getColName()) != null){
								campRec.addStringParam(CampaignConstants.GETCAMPAIGNS_TARGET_URL,
								ocJsonObject.get(CAMPAIGN_COLUMN_REPLACER.OC_TARGET_URL.getColName())!= null ? 
									ocJsonObject.get(CAMPAIGN_COLUMN_REPLACER.OC_TARGET_URL.getColName()).getAsString(): "");
							}
						});

			    res.removeParam(res.getParamByName(CampaignConstants.ONLINE_CONTENT_STRING));
				}
		   }
		
		return res;
	}
	

}

package com.kony.dbputilities.campaignservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbp.dto.CampaignSpecification;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CampaignHandler {

	public static List<String> incrementCampaignDisplayCount(DataControllerRequest dcRequest, List<CampaignSpecification> campaignSpecifications) throws Exception {
		
		for(int i=0; i<campaignSpecifications.size(); ++i) {
			
			Map<String, String> procParameterMap = new HashMap<>();
			procParameterMap.put("_campaignId", campaignSpecifications.get(i).getCampaignId());
			procParameterMap.put("_campaignPlaceholderId", campaignSpecifications.get(i).getCampaignPlaceholderId());
			procParameterMap.put("_imageIndex", campaignSpecifications.get(i).getImageIndex());

			HelperMethods.callApiJson(dcRequest, procParameterMap, HelperMethods.getHeaders(dcRequest), URLConstants.CAMPAIGN_DBP_DISPLAY_COUNT_UPDATE_PROC);
		}
		
		return null;
	}
}

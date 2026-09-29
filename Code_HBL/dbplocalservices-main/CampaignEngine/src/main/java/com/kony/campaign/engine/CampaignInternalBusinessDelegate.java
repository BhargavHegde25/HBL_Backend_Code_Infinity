package com.kony.campaign.engine;

import java.util.List;

import com.kony.campaign.CacheRealTimeCampaignEvalutor;
import com.kony.campaign.RealTimeCampaignEvalutor;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.CampaignRequestType;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Result;

public final class CampaignInternalBusinessDelegate {
		
	public static boolean getCampaignsForCacheUpdateRealTime(EventDTO eventInput,
			List<Campaign> campaignIdList,Result campRes) {		
		CampaignProcessor.processCampaignRequest(getCampReqForCacheUpdateTask(eventInput,campaignIdList,campRes));
        return true;
	}
	
	private static CampaignRequest getCampReqForCacheUpdateTask(EventDTO eventInput,
			List<Campaign> campaignIdList,Result campRes) {		
		EventDTO event = new EventDTO();
        event.setEventId(eventInput.getUserID());
        event.setCoreCustId(eventInput.getCoreCustId());
        CampaignRequest cr = new CampaignRequest(CampaignRequestType.CACHE_REALTIME,event,true);
        CacheRealTimeCampaignEvalutor evalutor = new CacheRealTimeCampaignEvalutor();
        evalutor.setCampaignResult(campRes);
        evalutor.setCampaignList(campaignIdList);
        cr.setCampaignEvalutor(evalutor);       
		return cr;
	  }
	
	public static boolean executeInternalNonPlaceHolderReq(EventDTO inputevent) {
		CampaignProcessor.processCampaignRequest(getInternalNonPlaceHolderCampReq(inputevent));
		return true;		
	}	
	
	public static CampaignRequest getInternalNonPlaceHolderCampReq(EventDTO inputevent) {
		EventDTO event = new EventDTO();
		event.setEventId(inputevent.getEventId());
		event.setUserID(inputevent.getUserID());
	    event.setCoreCustId(inputevent.getCoreCustId());
	 	CampaignRequest cr =new CampaignRequest(CampaignRequestType.ONLINE_NONPLACEHOLDER_EVENT,event
						,CampaignUtil.isCacheUpdateRequired());
		cr.setCampaignEvalutor(new RealTimeCampaignEvalutor());
		return cr;
	}
	
	
	private  CampaignInternalBusinessDelegate() {}

}

package com.kony.campaign.engine;

import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.campaign.CampaignEvalutor;
import com.kony.campaign.CampaignException;
import com.kony.campaign.InternalResponseProcessor;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.CampaignRequestType;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public final class CampaignProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static final String CAMPAIGN_RESPONSE_RETURNED = "CampaignResponse returned";
	private static final String PROCESS_ELIGIBLE_CAMPAIGNS_RESPONSE = "processEligibleCampaigns Response";
	private static final String HIT_ANALYTICS_RESPONSE = "hitAnalytics Response";
	private static final String FETCH_CAMPAIGNS_RESPONSE = "fetchCampaigns Response";
	private static final String CAMPAIGN_PROCESSING_ERROR = "Campaign processing error ";
	private static final String DEFAULT_CAMPAIGN_CAMPAIGN_RESPONSE = "DefaultCampaign Campaign Response";

	public static Result processCampaignRequest(CampaignRequest campaignRequest) {		
		Result finalRes = null;
		try {
			CampaignEvalutor evalutor = campaignRequest.getCampaignEvalutor();
			Result getCampaignRes = evalutor.fetchCampaigns(campaignRequest);
			logCampaignResultResponse(getCampaignRes,FETCH_CAMPAIGNS_RESPONSE);
			List<Campaign> campaignList = evalutor.processCampaignResponse(campaignRequest, getCampaignRes);
			if( getCampaignRes.getParamValueByName(CampaignConstants.DBP_ERROR_MESSAGE) == null) {
				if(campaignList != null && !campaignList.isEmpty()) {
					Result analyticsRes = evalutor.hitAnalytics(campaignRequest, campaignList);		
					logCampaignResultResponse(analyticsRes,HIT_ANALYTICS_RESPONSE);
					List<Campaign> eligibleCampaginList = evalutor.processAnalyticsResponse(campaignRequest, analyticsRes, campaignList);
					logSelectedList(eligibleCampaginList);
					if(eligibleCampaginList != null && !eligibleCampaginList.isEmpty()) {
						finalRes = evalutor.processEligibleCampaigns(campaignRequest, eligibleCampaginList, getCampaignRes);
						logCampaignResultResponse(finalRes,PROCESS_ELIGIBLE_CAMPAIGNS_RESPONSE);			
					}
				}	
				finalRes = getCampaignFromDefaultCampaigns(campaignRequest, finalRes, evalutor, getCampaignRes);
				logCampaignResultResponse(finalRes,CAMPAIGN_RESPONSE_RETURNED);	
			}else {
				throw new CampaignException(getCampaignRes.getParamValueByName(CampaignConstants.DBP_ERROR_MESSAGE),
						ErrorCodes.ERR_17002.getErrorCode());
			}
			return finalRes;		  
		}catch (CampaignException ce) {
			alert.prepareError(CAMPAIGN_PROCESSING_ERROR, ce).log();
			Result errRes = new Result();	
			CampaignUtil.addDBPErrCodeAndmsg(errRes, ce.getMessage(), ce.getErrorCode());
			return errRes; 			
		} catch (Exception e) {
			alert.prepareError(CAMPAIGN_PROCESSING_ERROR, e).log();
			Result errRes = new Result();		
			CampaignUtil.addDBPErrCodeAndmsg(errRes, e.getMessage(), ErrorCodes.ERR_17011.getErrorCode());		
			return errRes; 			
		}		
	}


	protected static void logSelectedList(List<Campaign> eligibleCampaginList) {
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("eligibleCampaginList" + eligibleCampaginList).log();
		} 
	}

	private static void logCampaignResultResponse(Result getCampaignRes, String msg) {
		if(diagnostic.isDebugEnabled()) {
			if(getCampaignRes != null) {
				diagnostic.prepareDebug(msg + ResultToJSON.convert(getCampaignRes)).log();
			}else {
				diagnostic.prepareDebug(msg + "returned Null").log();
			}
		}
	}

	private static Result getCampaignFromDefaultCampaigns(CampaignRequest campaignRequest, Result finalRes,
			CampaignEvalutor evalutor, Result getCampaignRes) throws CampaignException {
		if(CampaignRequestType.ONLINE_PLACEHOLDER_EVENT.equals(campaignRequest.getRequestType()) ||
				CampaignRequestType.PRELOGIN_EVENT.equals(campaignRequest.getRequestType())) {
			if(finalRes == null) {
				finalRes = InternalResponseProcessor.getEmptyCampaignResult();
			}
			int currentCampains = finalRes.getDatasetById(CampaignConstants.RESPONSE_CAMPAIGN_SPECIFICATIONS).getAllRecords().size();
			int respCampaignCount = currentCampains < campaignRequest.getCampaignCountInResponse() ? campaignRequest.getCampaignCountInResponse() - currentCampains: 0;
			if(respCampaignCount > 0) {
				campaignRequest.setCampaignCountInResponse(respCampaignCount);
				evalutor.processDefaultCampaigns(campaignRequest, getCampaignRes, finalRes);
			}
			logCampaignResultResponse(finalRes,DEFAULT_CAMPAIGN_CAMPAIGN_RESPONSE);		
		}
		return finalRes;
	}

	private CampaignProcessor() {}
}

package com.kony.campaign;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class RealTimeCampaignEvalutor extends AbstractCampaignEvalutor {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<Campaign> processAnalyticsResponse(CampaignRequest campaignRequest, Result analyticsCampaignResult,
			List<Campaign> totalcampaignList) throws CampaignException {
		List<Campaign> finalEligibleList;
		try {
			Map<Object, Optional<Boolean>> campaignEligibilityMap = analyticsCampaignResult.getDatasetById(CampaignConstants.LOOP_DATASET)
					.getAllRecords().stream().collect(
							Collectors.groupingBy(r2 -> r2.getParamByName(CampaignConstants.GETCAMPAIGNS_CAMPAIGN_ID).getValue(),
									Collectors.mapping(CampaignUtil.funcRecordToboolean, Collectors.reducing(Boolean::logicalAnd))));	

			List<Object> eligibleCampaignList = campaignEligibilityMap.entrySet().stream()
					.filter(entry-> entry.getValue().get().booleanValue())
					.map(Map.Entry::getKey).collect(Collectors.toList());

			List<Object> noteligiblecampaignList = campaignEligibilityMap.entrySet().stream()
					.filter(entry-> !entry.getValue().get().booleanValue())
					.map(Map.Entry::getKey).collect(Collectors.toList());

			//add campaigns with no dataContext and by default eligible
			List<String> defaultEligibleCampaign = totalcampaignList.stream().filter(cd -> cd.getDcList() == null)
					.map(Campaign::getCampaignId).collect(Collectors.toList());

			for (Object object : eligibleCampaignList) {
				defaultEligibleCampaign.add((String)object);
			}			 
			finalEligibleList = totalcampaignList.stream().
					filter(c -> defaultEligibleCampaign.contains(c.getCampaignId())).collect(Collectors.toList());

			finalEligibleList.sort(Comparator.comparingLong(Campaign::getPriority));
			if(campaignRequest.isUpdateCache()) {
			   updateCache(campaignRequest.getEvent().getCoreCustId(), noteligiblecampaignList,defaultEligibleCampaign);
			}
			return finalEligibleList;	
		}catch (Exception e) {
			alert.prepareError(ErrorCodes.ERR_17005.getMessage() , e).log();
			throw new CampaignException(ErrorCodes.ERR_17005.getMessage(),e,ErrorCodes.ERR_17005.getErrorCode());
		}
	}
	
	protected void updateCache(String coreCustId, List<Object> noteligiblecampaignList,
			List<String> defaultEligibleCampaign) throws CampaignException {	
		try {
			validateAndUpdateCache(coreCustId, noteligiblecampaignList, defaultEligibleCampaign,CampaignUtil.getCacheExpiry());
		} catch (MiddlewareException e) {
			alert.prepareError("Error while updating the cache " + e).log();
			throw new CampaignException("Error while updating the cache ",e,ErrorCodes.ERR_17010.getErrorCode());
		}
	}	
	
	
	private synchronized void validateAndUpdateCache(String userId, List<Object> noteligiblecampaignList,
			List<String> eligibleCampaigns, int cacheExpiryTimeInSec) 
			throws MiddlewareException
	{		
		if (noteligiblecampaignList != null && eligibleCampaigns != null) {			
			String cacheCampaign = (String) CampaignUtil.getFromCache(userId);
			if(diagnostic.isDebugEnabled()){
				diagnostic.prepareDebug("Current cache Value is"+ userId + ":" + cacheCampaign).log();
			}
			String cacheValue = null;
			if (StringUtils.isNotBlank(cacheCampaign)) {
				List<String> alleligibleCampaignList = Stream.concat(eligibleCampaigns.stream(), 
																	Arrays.asList(cacheCampaign.split(",", -1)).stream())
						.distinct().collect(Collectors.toList());
				cacheValue =alleligibleCampaignList.stream()
						.filter(e -> !noteligiblecampaignList.contains(e))
						.collect(Collectors.joining(","));				
			}else {
				cacheValue = eligibleCampaigns.stream().collect(Collectors.joining(","));
			}
			if(StringUtils.isNotBlank(cacheValue)) {

				CampaignUtil.updateCache(userId, cacheValue,cacheExpiryTimeInSec);
			}
		}
	}
}
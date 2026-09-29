package com.kony.campaign;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.util.CAMPAIGN_COLUMN_REPLACER;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public final class InternalResponseProcessor {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	public static void getResponseForEligibleCamapaign(Campaign eligibleCampaign ,
			EventDTO eventDTO , Record campaignRecord, Result result) throws CampaignException{
		try {				
			extractAndAddPlaceholderToResult(eligibleCampaign, eventDTO, campaignRecord, result);			
		} catch (Exception e) {
			alert.prepareError(ErrorCodes.ERR_17006.getMessage()+ e.getMessage()).log();
			throw new CampaignException(ErrorCodes.ERR_17006.getMessage(),e,ErrorCodes.ERR_17006.getErrorCode());
		}	
	}	

	public static Result getEmptyCampaignResult() {
		Result r = new Result();
		Dataset d = new Dataset(CampaignConstants.RESPONSE_CAMPAIGN_SPECIFICATIONS);
		r.addDataset(d);
		return r;
	}
	
	private static void extractAndAddPlaceholderToResult(Campaign eligibleCampaign, EventDTO eventDTO,
			Record campaignRecord, Result result) throws CampaignException {
		List<Record> placeholderrecordList = getPlaceHolderMatchingRecord(eventDTO, campaignRecord);
		if (placeholderrecordList!= null && !placeholderrecordList.isEmpty()) {
			Dataset ds = null;
			for (Record placeholderrecord : placeholderrecordList) {				
				ds = result.getDatasetById(CampaignConstants.RESPONSE_CAMPAIGN_SPECIFICATIONS);
				int index =	ds.getAllRecords().size();
				Record imageRec = new Record();
				imageRec.addParam(CampaignConstants.RESPONSE_CAMPAIGN_ID, eligibleCampaign.getCampaignId());
				addCommonOnlineParamsToResponse(eventDTO, placeholderrecord, imageRec);
				addAdditionalOptionalParams(placeholderrecord, imageRec);	
				imageRec.addParam(CampaignConstants.RESPONSE_IMAGE_INDEX,
						String.valueOf(++index)); // imageIndex from record
				ds.addRecord(imageRec);

			}
			result.addDataset(ds);
		}
	}


private static void addAdditionalOptionalParams(Record placeholderrecord, Record imageRec) throws CampaignException {
		for (  CAMPAIGN_COLUMN_REPLACER onlineContentCol : CAMPAIGN_COLUMN_REPLACER.values()) {
			if(CAMPAIGN_COLUMN_REPLACER.OC_TARGET_URL != onlineContentCol && 
				placeholderrecord.getParamValueByName(onlineContentCol.getColName()) != null) {
					if(onlineContentCol.isDecodable()) { 
						try {
							imageRec.addParam(onlineContentCol.getColName(),
									CampaignUtil.decodeValue(placeholderrecord.getParamValueByName(onlineContentCol.getColName())));
						}catch (UnsupportedEncodingException e) {
							throw new CampaignException("Error while decoding ", e ,ErrorCodes.ERR_17006.getErrorCode());
						}						
					}else {
						imageRec.addParam(onlineContentCol.getColName(),
								placeholderrecord.getParamValueByName(onlineContentCol.getColName()));
					}						
			}
		}
	}

	public static void addCommonOnlineParamsToResponse(EventDTO eventDTO, Record placeholderrecord, Record imageRec) throws CampaignException {
		imageRec.addParam(CampaignConstants.RESPONSE_PLACEHOLDER_CODE, eventDTO.getPlaceholderCode());
		imageRec.addParam(CampaignConstants.RESPONSE_CAMPAIGN_PLACEHOLDER_ID, 
				placeholderrecord.getParamValueByName(CampaignConstants.GETCAMPAIGNS_PLACEHOLDER_ID));
		try {
			if(placeholderrecord.getParamValueByName(CampaignConstants.GETCAMPAIGNS_IMAGE_URL) != null) {
			   imageRec.addParam(CampaignConstants.RESPONSE_IMAGE_URL,
					CampaignUtil.decodeValue(placeholderrecord.getParamValueByName(CampaignConstants.GETCAMPAIGNS_IMAGE_URL)));
			}else {
				 imageRec.addParam(CampaignConstants.RESPONSE_IMAGE_URL, "");
			}
			if(placeholderrecord.getParamValueByName(CampaignConstants.GETCAMPAIGNS_TARGET_URL) != null) {
			   imageRec.addParam(CampaignConstants.RESPONSE_DESTINATION_URL,
				CampaignUtil.decodeValue(placeholderrecord.getParamValueByName(CampaignConstants.GETCAMPAIGNS_TARGET_URL)));
			}else {
				imageRec.addParam(CampaignConstants.RESPONSE_DESTINATION_URL , "");
			}
		}catch (UnsupportedEncodingException e) {
			  throw new CampaignException("Error while decoding ", e ,ErrorCodes.ERR_17006.getErrorCode());
		}
	}

	private static List<Record> getPlaceHolderMatchingRecord(EventDTO eventDTO, Record campaignRecord) {
		return campaignRecord.getDatasetById(CampaignConstants.GETCAMPAIGNS_ONLINE_CONTENT).getAllRecords().stream()
				.filter(recimage ->
				eventDTO.getChannel().equalsIgnoreCase(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_CHANNEL_SUB_TYPE).getValue())
				 && eventDTO.getPlaceholderCode().equals(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_PLACEHOLDER_IDENTIFIER).getValue())
				 && eventDTO.getScale().equals(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_IMAGE_SCALE).getValue()))
				.collect(Collectors.toList());		
	}
	

	private InternalResponseProcessor() {}
}

package com.kony.campaign;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.CampaignConstants.CampaignFilterTypes;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.CampaignRequestType;
import com.kony.campaign.dto.DataContext;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.engine.CampaignThreadPoolExecutor;
import com.kony.campaign.engine.ExternalEventPushTask;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;
import static com.kony.campaign.common.ErrorCodes.*;

public abstract class AbstractCampaignEvalutor implements CampaignEvalutor {	
	private static final String US = "_";
	protected Result campaignResult = null;
	protected List<Campaign> campaignList = null;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result fetchCampaigns(CampaignRequest campaignRequest) throws CampaignException {		
		try {			
			return invokeGetCampaignMS(campaignRequest.getRequestType().getServiceName(),
					campaignRequest.getRequestType().getOperationName(), getFetchCampaignsInputMap(campaignRequest));

		} catch (Exception e) {
			alert.prepareError(ERR_17002.getMessage() , e).log();
			throw new CampaignException(ERR_17002.getMessage(),e,ERR_17002.getErrorCode());
		}
	}	

	public List<Campaign> processCampaignResponse( CampaignRequest cReq, Result campaignResult) throws CampaignException {	
		List<Campaign> campList=new ArrayList<>();
		try {
			String errmsg = chkAndGetErrorMsgForGetCampaigns(campaignResult,cReq.getRequestType().getOperationName());
			if(StringUtils.isBlank(errmsg)) {
				if(campaignResult.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET) != null)
				{
					if(CampaignFilterTypes.NO_FILTER.name().equalsIgnoreCase(cReq.getCampaignFilter())) {
						campaignResult.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET)
						.getAllRecords().forEach(
								getCampaignList(cReq, campList));
					}else if(CampaignFilterTypes.FILTER_ON_PLACEHOLDER.name().equalsIgnoreCase(cReq.getCampaignFilter())) {
						campaignResult.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET)
						.getAllRecords().stream().filter(rec -> placeHolderFilter.apply(rec,cReq))
						.forEach(getCampaignList(cReq, campList));
					}
				   campList = filterIgnoredCampaigns(campList,campaignResult);
				}
			}else {
				CampaignUtil.addDBPErrCodeAndmsg(campaignResult, 
						errmsg,ERR_17002.getErrorCode());
			}
		}catch (Exception e) {
			alert.prepareError(ERR_17002.getMessage()+ e).log();
			throw new CampaignException(ERR_17002.getMessage(),e,ERR_17002.getErrorCode());
		}
		return campList;
	}

	protected List<Campaign> filterIgnoredCampaigns(List<Campaign> campList, Result campResult)
											throws CampaignException {
		List<Campaign> filteredcampList=new ArrayList<>();
		try {
			String errmsg = chkAndGetErrorMsgForIgnoreCampaigns(campResult);
			if(StringUtils.isBlank(errmsg) ) { 
				if(campResult.getDatasetById(CampaignConstants.GETIGNORECAMPAIGNS_DATASET) != null) {
					List<String> ignoreCampList = campResult.getDatasetById(CampaignConstants.GETIGNORECAMPAIGNS_DATASET)
							.getAllRecords().stream().map(filterIgnoreCampaigns)
							.collect(Collectors.toList());				
					filteredcampList = campList.stream().
							filter(c ->  !ignoreCampList.contains(c.getCampaignId())).collect(Collectors.toList());								
				}else {
					filteredcampList = campList ;
				}
			}else {
				CampaignUtil.addDBPErrCodeAndmsg(campResult, 
						errmsg,ERR_17003.getErrorCode());
			}
		}catch (Exception e) {
			alert.prepareError(ERR_17003.getMessage()+ e).log();
			throw new CampaignException(ERR_17003.getMessage(),
					e,ERR_17003.getErrorCode());
		}
		return filteredcampList;
	}

	Function<Record, String> filterIgnoreCampaigns = rec -> rec.getParamValueByName(CampaignConstants.GETIGNORECAMPAIGNS_CAMPAIGNID);
	
	private String chkAndGetErrorMsgForIgnoreCampaigns(Result fetchCampaignRes) {
		String errmsg = null;
		if(Integer.parseInt(fetchCampaignRes.getParamValueByName(CampaignConstants.OPSTATUS+US+CampaignConstants.SERVICE_GETCUSTIGNORE_CAMPAIGNS)) != 0 && 
				fetchCampaignRes.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GETCUSTIGNORE_CAMPAIGNS) != null ) {
				errmsg = "getCustomerIgnoreCampaign service failed with error " +fetchCampaignRes.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GETCUSTIGNORE_CAMPAIGNS);
				alert.prepareError("getCustomerIgnoreCampaign failed with error "+ errmsg).log();			
		}				
		return errmsg;	
	}

	private String chkAndGetErrorMsgForGetCampaigns(Result campaignResult, String operationName) {		
		StringBuilder s = new StringBuilder();
		operationName = operationName.contains("Event") ? 
				CampaignConstants.SERVICE_GET_CAMPAIGNS_FOR_EVENT : CampaignConstants.SERVICE_GET_ALL_CAMPAIGNS;
		switch(operationName) {
		case CampaignConstants.SERVICE_GET_CAMPAIGNS_FOR_EVENT: 
							getErrorForOrchestratedGetEventCampaings(campaignResult, operationName, s);
							break;
		
		case CampaignConstants.SERVICE_GET_ALL_CAMPAIGNS: 
							getErrmsgForgetAllCampains(campaignResult,  s);
							break;			
		}
		
		
		if(StringUtils.isNotBlank(s.toString())) {
			alert.prepareError(s.toString()).log();
		}
		return s.toString();
	}

	private void getErrorForOrchestratedGetEventCampaings(Result campaignResult, String operationName,
			StringBuilder s) {
		if( StringUtils.isNotBlank(campaignResult.getParamValueByName(CampaignConstants.ERRCODE))
					&& Integer.parseInt(campaignResult.getParamValueByName(CampaignConstants.ERRCODE)) !=400 ) {
			
				alert.prepareError("Campaign service has returned errcode " +
					campaignResult.getParamValueByName(CampaignConstants.ERRCODE)) .log();
				
				if(campaignResult.getParamValueByName(CampaignConstants.ERRMSG) != null ) {
					alert.prepareError("Campaign service has returned errmsg " +
							campaignResult.getParamValueByName(CampaignConstants.ERRMSG)) .log();			
				}
				
				if(campaignResult.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GET_CAMPAIGNS_FOR_EVENT) != null ) {
					s.append(campaignResult.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GET_CAMPAIGNS_FOR_EVENT));
					if(campaignResult.getParamValueByName(CampaignConstants.ERRMSG) != null ) {
						alert.prepareError("Campaign service has returned errmsg " +
								campaignResult.getParamValueByName(CampaignConstants.ERRMSG)) .log();			
					}
				} 
		}
	}	
	
	private void getErrmsgForgetAllCampains(Result campaignResult,StringBuilder s) {					
			if( campaignResult.getParamValueByName(CampaignConstants.ERRCODE) != null ) {	
			alert.prepareError("Campaign service has returned errcode " +
					campaignResult.getParamValueByName(CampaignConstants.ERRCODE)) .log();
			
				
				if( campaignResult.getParamValueByName(CampaignConstants.ERRMSG) != null ) {
					s.append(CampaignConstants.SERVICE_GET_ALL_CAMPAIGNS+ " failed with error " +
							campaignResult.getParamValueByName(CampaignConstants.ERRMSG));	
					alert.prepareError(CampaignConstants.SERVICE_GET_ALL_CAMPAIGNS + " service has returned errmsg " +
							campaignResult.getParamValueByName(CampaignConstants.ERRMSG)) .log();	
				}
				
			}			
	}
	
	@Override
	public Result hitAnalytics(CampaignRequest campaignRequest, List<Campaign> campaignList) throws CampaignException{
		Result res = null;
		try {
			List<Campaign> validDCCampaignList = campaignList.stream().filter(cd -> cd.getDcList() != null)
					.collect(Collectors.toList());
			List<String> endpointURLlist = new ArrayList<>();
			List<String> filterList = new ArrayList<>();
			List<String> campaignIdList = new ArrayList<>();
			List<String> dataContextIdList = new ArrayList<>();
			prepareLoopParams(validDCCampaignList, endpointURLlist, filterList, campaignIdList,dataContextIdList);
			res = invokeAnalyticsService(endpointURLlist, filterList,campaignIdList,dataContextIdList);
		} catch (AppRegistryException e) {
			alert.prepareError("No able to find the Analytics app ", e).log();
			throw new CampaignException("No able to find the Analytics app ",e,ERR_17005.getErrorCode());
		} catch (Exception e) {
			alert.prepareError(ERR_17005.getMessage(), e).log();
			throw new CampaignException(ERR_17005.getMessage(),e,ERR_17005.getErrorCode());
		}
		return res;		
	}	

	@Override
	public void processDefaultCampaigns(CampaignRequest campaignRequest, Result campaignResult,
			Result campaignsSelected) throws CampaignException	{	
		try {
			String errmsgString = chkAndGetErrmsgForDefaultCampaign(campaignResult);
			if(StringUtils.isBlank(errmsgString)  ) {
					if(campaignResult.getDatasetById(CampaignConstants.DEFAULTCAMPAIGN_DATASET) != null)
					{
						addDefaultCampToResponse(campaignRequest, campaignResult, campaignsSelected);
					}
			}else {
				CampaignUtil.addDBPErrCodeAndmsg(campaignsSelected, 
						errmsgString,ERR_17004.getErrorCode());
			}					
		} catch (Exception e) {
			alert.prepareError(ERR_17004.getMessage(), e).log();
			throw new CampaignException(ERR_17004.getMessage(), e ,
					ERR_17004.getErrorCode());
		}		
	}

	private void addDefaultCampToResponse(CampaignRequest campaignRequest, Result campaignResult,
			Result campaignsSelected) throws CampaignException {
		List<Record> eligibleRecords = campaignResult.getDatasetById(
											CampaignConstants.DEFAULTCAMPAIGN_DATASET).getAllRecords();			
		if(!eligibleRecords.isEmpty()) {
			int reqCampCount = eligibleRecords.size() > campaignRequest.getCampaignCountInResponse()
					             ? campaignRequest.getCampaignCountInResponse() : eligibleRecords.size(); 
			Dataset ds = campaignsSelected.getDatasetById(CampaignConstants.RESPONSE_CAMPAIGN_SPECIFICATIONS);
			for(int i=1; i<= reqCampCount; i++) {	
				final int reqImageIndex = i;
				Optional<Record> optcurrentRecord = eligibleRecords.stream()
						.filter(r -> Integer.parseInt(
								r.getParamValueByName(CampaignConstants.GETCAMPAIGNS_IMAGE_INDEX))== reqImageIndex).findAny();
				if (optcurrentRecord.isPresent()) {						 
					ds.addRecord(getDefaultCampaignRecord(campaignRequest, ds, optcurrentRecord.get()));
				}else {
					String err= "DefaultCampaign with imageIndex " + i + " not found";
					alert.prepareError(err).log();
					throw new CampaignException(err,ERR_17004.getErrorCode());
				}
			}
		}
	}

	private Record getDefaultCampaignRecord(CampaignRequest campaignRequest, Dataset ds, Record placeholderrecord) throws CampaignException {
		Record imageRec = new Record();
		imageRec.addParam(CampaignConstants.RESPONSE_CAMPAIGN_ID, CampaignConstants.DEFAULT_CAMPAIGN);
		InternalResponseProcessor.addCommonOnlineParamsToResponse(campaignRequest.getEvent(), placeholderrecord, imageRec);
		imageRec.addParam(CampaignConstants.RESPONSE_IMAGE_INDEX,
				String.valueOf(ds.getAllRecords().size() + 1));
		return imageRec;
	}

	private String chkAndGetErrmsgForDefaultCampaign(Result campaignResult) {
		StringBuilder s = new StringBuilder();
		if(StringUtils.isNotBlank(campaignResult.getParamValueByName(CampaignConstants.ERRCODE_FOR_DEFAULT)) && 
					Integer.parseInt(campaignResult.getParamValueByName(CampaignConstants.ERRCODE_FOR_DEFAULT)) !=400) {
				alert.prepareError("Default Campaign service has returned errcode " 
						+ campaignResult.getParamValueByName(CampaignConstants.ERRCODE_FOR_DEFAULT)) .log();
			
			if(campaignResult.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GET_DEFAULT_CAMPAIGNS) != null ) {
				s.append(campaignResult.getParamValueByName(CampaignConstants.ERRMSG+US+CampaignConstants.SERVICE_GET_DEFAULT_CAMPAIGNS));
			}				 

			if(campaignResult.getParamValueByName(CampaignConstants.ERRMSG_FOR_DEFAULT) != null ) {
				s.append(campaignResult.getParamValueByName(CampaignConstants.ERRMSG_FOR_DEFAULT));

			}			
			
		}
		String  errmsg = s.toString();
		if(StringUtils.isNotBlank(errmsg)) {
			alert.prepareError("DefaultCampaign error " + errmsg).log();
		}		
		return errmsg;		
	}	

	@Override
	public Result processEligibleCampaigns(CampaignRequest campaignRequest, List<Campaign> campList, Result campaignResult) throws CampaignException {
		Result res = new Result();
		try {			
			int respCampaignCount = campList.size() > campaignRequest.getCampaignCountInResponse() ? campaignRequest.getCampaignCountInResponse() : campList.size();
			for(int i=0;i<respCampaignCount;i++) {
				Campaign campaign = campList.get(i);
				Optional<Record> optRec = getSelctedCampaignFromResult(campaignResult, campaign);			
				if (optRec.isPresent()) {
					Record campaignRecord = optRec.get();
					List<String> channelList = campaignRecord.getDatasetById(CampaignConstants.GETCAMPAIGNS_CHANNEL_DETAILS)
							.getAllRecords().stream().map(recimage -> recimage
									.getParamByName(CampaignConstants.GETCAMPAIGNS_CHANNEL_SUB_TYPE).getValue())
							.collect(Collectors.toList());
					if (CampaignRequestType.ONLINE_PLACEHOLDER_EVENT.equals(campaignRequest.getRequestType())) {
						res = res.getAllDatasets().isEmpty()  ? InternalResponseProcessor.getEmptyCampaignResult() : res;
						prepareOnlineResponse(campaignRequest, res, campaign, campaignRecord, channelList);
					}						
					if(i==0) {
						sendEventToOfflineChanels(campaignRequest, campaignRecord, channelList);
					}
				}
			}

		} catch (Exception e) {
			alert.prepareError(ERR_17009.getMessage(), e).log();
			throw new CampaignException(ERR_17009.getMessage(),e,ERR_17009.getErrorCode());
		}
		return res;		
	}

	private void prepareOnlineResponse(CampaignRequest campaignRequest, Result res, Campaign campaign,
			Record campaignRecord, List<String> channelList) throws CampaignException {
		if (campaignRequest.getEvent().getChannel() != null
				&& channelList.contains(campaignRequest.getEvent().getChannel())) {
			InternalResponseProcessor.getResponseForEligibleCamapaign(campaign,
					campaignRequest.getEvent(), campaignRecord,res);
		}
	}

	protected Optional<Record> getSelctedCampaignFromResult(Result campaignResult, Campaign campaign) {
		return campaignResult.getDatasetById(CampaignConstants.GETCAMPAIGNS_CAMPAIGNDATASET).getAllRecords().stream().filter(
				resrec -> resrec.getParamByName(CampaignConstants.GETCAMPAIGNS_CAMPAIGN_ID).getValue().equals(campaign.getCampaignId())).findAny();
	}

	public Result getCampaignResult() {
		return campaignResult;
	}

	public void setCampaignResult(Result campaignResult) {
		this.campaignResult = campaignResult;
	}

	public List<Campaign> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<Campaign> campaignList) {
		this.campaignList = campaignList;
	}

	protected Map<String, Object> getFetchCampaignsInputMap(CampaignRequest campaignRequest) {
		Map<String, Object> inputMap = new HashMap<>();	   
		EventDTO event = campaignRequest.getEvent();
		inputMap.put(CampaignConstants.PARAM_EVENT_CODE,event.getEventId());		
		inputMap.put(CampaignConstants.PARAM_PLACEHOLDER_CODE,event.getPlaceholderCode());		
		inputMap.put(CampaignConstants.PARAM_SCALE,event.getScale());
		inputMap.put(CampaignConstants.PARAM_CHANNEL_TYPE,event.getChannel());
		inputMap.put(CampaignConstants.PARAM_CORE_CUSTOMER_ID,event.getCoreCustId());
		if(campaignRequest.getRequestType().equals("JOB")) {
			inputMap.put(CampaignConstants.CAMPAIGN_STATUS,"SCHEDULED_ACTIVE_COMPLETED");
		}
		return inputMap;
	}	

	protected boolean sendEventToOfflineChanels(CampaignRequest campaignRequest, Record campaignRecord,
			List<String> channelList) {
		try {
			CampaignThreadPoolExecutor.execute(new ExternalEventPushTask(campaignRequest, campaignRecord, channelList));
		} catch (Exception e) {
			alert.prepareError("Error while pushing external event ", e).log();			
		}
		return true;
	}	

	private Result invokeGetCampaignMS(String serviceName, String operationName,
			Map<String, Object> inputMap) throws MiddlewareException {	
		return CampaignUtil.invokeService(serviceName, operationName, inputMap);
	}

	private void prepareLoopParams(List<Campaign> validDCCampaignList, List<String> endpointURLlist,
			List<String> filterList, List<String> campaignIdList, List<String> dataContextIdList) {
		for (Campaign campaign2 : validDCCampaignList) {
			for (DataContext dc : campaign2.getDcList()) {
				endpointURLlist.add(dc.getEndPointURL());
				filterList.add(dc.getFilter());
				campaignIdList.add(campaign2.getCampaignId());
				dataContextIdList.add(dc.getName());
			}			
		}
	}

	private List<Record> getPlaceHolderMatchingRecords(Result cres , EventDTO eventDTO) {
		return cres.getDatasetById(CampaignConstants.DEFAULTCAMPAIGN_DATASET).getAllRecords().stream()
				.filter(rec -> eventDTO.getPlaceholderCode()
						.equals(rec.getParamByName(CampaignConstants.GETCAMPAIGNS_PLACEHOLDER_IDENTIFIER).getValue()) && 
						eventDTO.getScale().equals(rec.getParamByName(CampaignConstants.GETCAMPAIGNS_IMAGE_SCALE).getValue()))
				.collect(Collectors.toList());		
	}


	private Result invokeAnalyticsService(List<String> endpointURLlist,
			List<String> filterList, List<String> campaignNameList, List<String> dataContextIdList) throws MiddlewareException {
		Map<String, Object> inputMapdc = new HashMap<>();
		inputMapdc.put(CampaignConstants.LOOP_COUNT, endpointURLlist.size());
		inputMapdc.put(CampaignConstants.LOOP_SEPERATOR, ",");
		inputMapdc.put(CampaignConstants.LOOP_PARAM_ENDPOINT_URL, endpointURLlist.stream().collect(Collectors.joining(",")));
		inputMapdc.put(CampaignConstants.LOOP_PARAM_FILTER, filterList.stream().collect(Collectors.joining(",")));
		inputMapdc.put(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID, campaignNameList.stream().map(String::valueOf).collect(Collectors.joining(",")));
		inputMapdc.put(CampaignConstants.LOOP_PARAM_DATACONTEXT_ID,dataContextIdList.stream().collect(Collectors.joining(",")));
		return CampaignUtil.invokeService(CampaignConstants.LOOPING_SERVICE_NAME,CampaignConstants.LOOPING_OPERATION_NAME, inputMapdc);		
	}

	private BiFunction<Record,CampaignRequest,Boolean> placeHolderFilter = (rec,cr) -> isPlaceHolderPresent(cr.getEvent(), rec);


	private static boolean isPlaceHolderPresent(EventDTO eventDTO, Record campaignRecord) {
		return campaignRecord.getDatasetById(CampaignConstants.GETCAMPAIGNS_ONLINE_CONTENT).getAllRecords().stream()
				.anyMatch(recimage -> eventDTO.getPlaceholderCode()
						.equals(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_PLACEHOLDER_IDENTIFIER).getValue()) && 
						eventDTO.getScale().equals(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_IMAGE_SCALE).getValue()));

	}

	private Consumer<? super Record> getCampaignList(CampaignRequest campaignRequest, List<Campaign> campaignList) {
		return record -> {
			Campaign campaign = new Campaign(record.getParamByName(CampaignConstants.GETCAMPAIGNS_CAMPAIGN_ID).getValue());
			campaign.setPriority(Long.parseLong(record.getParamByName(CampaignConstants.GETCAMPAIGNS_CAMPAIGN_PRIORITY).getValue()));
			Dataset dataContextDS = record.getDatasetById(CampaignConstants.GETCAMPAIGNS_DATACONTEXT);
			List<DataContext> dclist = new ArrayList<>();
			if(dataContextDS != null)
			{
				dataContextDS.getAllRecords().forEach(dcrecord -> {
					DataContext dc = new DataContext(dcrecord.getParamByName(CampaignConstants.GETCAMPAIGNS_DATA_CONTEXT_NAME).getValue());
					dc.setEndPointURL(dcrecord.getParamByName(CampaignConstants.GETCAMPAIGNS_END_POINT_URL).getValue());
					String profileCondition = dcrecord.getParamByName(CampaignConstants.GETCAMPAIGNS_CONDITION_EXPRESSION).getValue();
					StringBuilder filters = new StringBuilder();
					boolean filterOnCustomer = campaignRequest.getRequestType().isFilterOnCustomer();
					if(filterOnCustomer && StringUtils.isNotBlank(campaignRequest.getEvent().getCoreCustId())) {
						filters.append(CampaignConstants.CAMPAIN_REQ_CUSTOMER_FILTER)
						.append(campaignRequest.getEvent().getCoreCustId()).append("'");
					}
					if(StringUtils.isNotBlank(profileCondition)) {
						profileCondition = getDecodedConditionExpression(profileCondition);
						if(filters.toString().length() > 0) {
							filters.append(CampaignConstants.CAMPAIN_REQ_FILTER_AND).append(profileCondition);								
						} else {
							filters.append(profileCondition);	
						}							
					}			
					dc.setFilter(filters.toString()); // String could be empty
					dclist.add(dc);
				});
			}
			if (!dclist.isEmpty()) {
				campaign.setDcList(dclist);
			}
			campaignList.add(campaign);
		};
	}

	private String getDecodedConditionExpression(String profileCondition) {
		try {
			profileCondition =  CampaignUtil.decodeValue(profileCondition);
		} catch (UnsupportedEncodingException e) {
			alert.prepareError("Encoding the condition failed " + profileCondition).log();
		}
		return profileCondition;
	}
}

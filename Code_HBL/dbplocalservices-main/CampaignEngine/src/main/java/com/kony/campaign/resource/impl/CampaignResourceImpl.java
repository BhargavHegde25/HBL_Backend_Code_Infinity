package com.kony.campaign.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.campaign.CampaignException;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.resource.api.CampaignResource;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class CampaignResourceImpl implements CampaignResource {	
	
	private static final String ERROR_WHILE_GETTING_INTERNAL_EVENT_CAMPAIGN = "Error while getting campaign , ";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getCampaignsForInternalEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String channelId = extractChannelId(request, result);
			if(StringUtils.isNotBlank(channelId)) {
				EventDTO event = populateInternalEventDetails(inputArray,channelId);				
				if(!event.getEventId().equals(CampaignConstants.PRELOGIN)) {				
						String corebackendId = getCoreBackendId(request);
						if(StringUtils.isNotBlank(corebackendId)) {
							event.setCoreCustId(corebackendId);						
						}else {
							alert.prepareError(CampaignConstants.CAMPAIGN_CORETYPE_HAS_TO_BE_SET_PROPERLY).log();
							result = getErrResult(CampaignConstants.CAMPAIGN_CORETYPE_HAS_TO_BE_SET_PROPERLY);
							return result;
						}
					}				
					result = sendInternalEvent(event);									 
			}
		}catch (CampaignException e) {			
			alert.prepareError("Error while getting campaign " , e).log();
			CampaignUtil.addDBPErrCodeAndmsg(result, e.getMessage(), e.getErrorCode());
		}
		catch (Exception e) {			
			alert.prepareError("Error while getting campaign " , e).log();
			CampaignUtil.addDBPErrCodeAndmsg(result, e.getMessage(),  ErrorCodes.ERR_17011.getErrorCode());
		}
		return result;
	}

	private Result sendInternalEvent(EventDTO event) {
		CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);
		return campaignBusinessDelegate.getInternalCampaigns(event);
	}

	protected Result getErrResult(String errmsg) {
		Result result;
		result = new Result();
		result.addParam(CampaignConstants.DBP_ERROR_MESSAGE,ERROR_WHILE_GETTING_INTERNAL_EVENT_CAMPAIGN + errmsg);
		
		return result;
	}

	@Override
	public Result getCampaignsForExternalEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();	
		try {			
			EventDTO event = populateExternalEventDetails(inputArray,result);				
			if(event != null) {			
				CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);

				result = campaignBusinessDelegate.getExternalCampaigns(event);
			}
		} catch (Exception e) {
			alert.prepareError("Error while processing external event " , e).log();
			result.addParam(new Param("dbpErrMsg","Error while processing external event "+e.getMessage()));		
		}
		return result;
	}

	private String extractChannelId(DataControllerRequest request, Result result) throws AppRegistryException {
		String channelId = null;
		String reportingParams  = request.getServicesManager().getDeviceRequestData().getReportingParams();		
		if (StringUtils.isNotBlank(reportingParams)) {
			JsonObject reportingParamsJson = new JsonParser().parse(reportingParams).getAsJsonObject();
			channelId = reportingParamsJson.get(CampaignConstants.REPORTINGPARAMS_CHNL).getAsString();
			if (StringUtils.isBlank(channelId) ||
					!(channelId.equalsIgnoreCase(CampaignConstants.MOBILE) || channelId.equalsIgnoreCase(CampaignConstants.DESKTOP))) {
				CampaignUtil.addDBPErrCodeAndmsg(result, CampaignConstants.CHANNEL_IS_NOT_VALID, ErrorCodes.ERR_17001.getErrorCode());				
			}

			channelId = channelId.equalsIgnoreCase(CampaignConstants.DESKTOP) ? CampaignConstants.InternalChannels.WEB.name() :
				CampaignConstants.InternalChannels.MOBILE.name();				
		}else {
			CampaignUtil.addDBPErrCodeAndmsg(result, CampaignConstants.CHANNEL_IS_NOT_VALID, ErrorCodes.ERR_17001.getErrorCode());
		}
		return channelId;
	}

	private EventDTO populateInternalEventDetails(Object[] inputArray,String channelId) throws CampaignException {
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		if(StringUtils.isBlank(inputParams.get(CampaignConstants.PARAM_EVENT_CODE))){			
			throw new CampaignException(CampaignConstants.EVENT_CODE_IS_MANDATORY,ErrorCodes.ERR_17001.getErrorCode());
		} 
		EventDTO event = new EventDTO();
		event.setEventId(inputParams.get(CampaignConstants.PARAM_EVENT_CODE));			
		event.setPlaceholderCode(inputParams.get(CampaignConstants.PARAM_PLACEHOLDER_CODE));
		event.setScale(inputParams.get(CampaignConstants.PARAM_SCALE));
		event.setChannel(channelId);
		return event;
	}


	private EventDTO populateExternalEventDetails(Object[] inputArray, Result res) {
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		if(StringUtils.isBlank(inputParams.get(CampaignConstants.PARAM_EVENT_CODE))){
			CampaignUtil.addDBPErrCodeAndmsg(res,CampaignConstants.EVENT_CODE_IS_MANDATORY,ErrorCodes.ERR_17001.getErrorCode());
			alert.prepareError(CampaignConstants.EVENT_CODE_IS_MANDATORY).log();
			return null;
		} 
		if(StringUtils.isBlank(inputParams.get(CampaignConstants.PARAM_CORE_CUSTOMER_ID))){
			CampaignUtil.addDBPErrCodeAndmsg(res,CampaignConstants.CORE_CUSTOMER_ID_IS_MANDATORY_ERRMSG,ErrorCodes.ERR_17001.getErrorCode());
			alert.prepareError(CampaignConstants.CUSTOM_PARAMS_SHOULD_BE_A_PROPER_JSON_OBJECT).log();
			return null;
		}		 
		EventDTO event = new EventDTO();
		event.setEventId(inputParams.get(CampaignConstants.PARAM_EVENT_CODE));			
		event.setCoreCustId(inputParams.get(CampaignConstants.PARAM_CORE_CUSTOMER_ID));
		JsonObject eventData = null;
		try {
			eventData = new JsonObject();	
			if(StringUtils.isNotBlank(inputParams.get(CampaignConstants.PARAM_CUSTOM_PARAMS))){
			 JsonObject customParams = new JsonParser().parse(inputParams.get(CampaignConstants.PARAM_CUSTOM_PARAMS)).getAsJsonObject();
			eventData.add(CampaignConstants.EVENT_CUSTOM_PARAMS, customParams);
			}
			
		}catch(Exception e) {
			alert.prepareError(CampaignConstants.CUSTOM_PARAMS_SHOULD_BE_A_PROPER_JSON_OBJECT).log();
			CampaignUtil.addDBPErrCodeAndmsg(res,CampaignConstants.CUSTOM_PARAMS_SHOULD_BE_A_PROPER_JSON_OBJECT,ErrorCodes.ERR_17001.getErrorCode());
			return null;
		}
		event.setEventData(eventData);
		return event;
	}

	private String getCoreBackendId(DataControllerRequest dcreq) throws CampaignException {		
		try {
			String backendId = null;
			if (dcreq.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userAttributesMap = dcreq.getServicesManager().getIdentityHandler().getUserAttributes();               
				String backendIdentifier = (String)userAttributesMap.get(CampaignConstants.IDENITY_BACKENDIDENTIFIERS);
				if(diagnostic.isDebugEnabled()){
					diagnostic.prepareDebug("backendIdentifier is" + backendIdentifier).log();
				}
				if(StringUtils.isNotBlank(backendIdentifier)) { 				
					backendId = getCoreIDFromJson(backendIdentifier);				
				}
			}
			return backendId;
		} catch (Exception e) {
			alert.prepareError(CampaignConstants.ERROR_WHILE_EXTRACTING_CORE_USER_ID , e).log();	
			throw new CampaignException(CampaignConstants.ERROR_WHILE_EXTRACTING_CORE_USER_ID, e,ErrorCodes.ERR_17001.getErrorCode());
		}		

	}

	protected static String getCoreIDFromJson(String backendIdentifier) {
		String backendId = null;
		JsonObject backendIdentifiersJSON = new JsonParser().parse(backendIdentifier).getAsJsonObject();
		String coreType = CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_CORETYPE, CampaignConstants.CORE_TYPE_DEFAULT_VALUE);			
		if(StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)){					
				backendId = getBackendIdFromCoreType(backendIdentifiersJSON,coreType);
		}		
		return backendId;
	}

	protected static String getBackendIdFromCoreType(JsonObject backendIdentifiersJSON, String key) {
		JsonArray backendTypeObj = backendIdentifiersJSON.get(key).getAsJsonArray();
		String backendId = null;
		if(backendTypeObj.size() > 0) {
			backendId  =	backendTypeObj.get(0).getAsJsonObject().get(CampaignConstants.IDENITY_BACKENDID).getAsString();
		}
		return backendId;
	}

	@Override
	public Result insertCustCompletedCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Result res = new Result();
		try {
			Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
			String campaignId = inputParams.get(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID);
			if(StringUtils.isBlank(campaignId)) {
				CampaignUtil.addDBPErrCodeAndmsg(res, CampaignConstants.CAMPAIGNID_CANNOT_BE_EMPTY, ErrorCodes.ERR_17001.getErrorCode());
			}			
			String userId = getUserID(request, res);		
			if(userId == null) {
				CampaignUtil.addDBPErrCodeAndmsg(res, CampaignConstants.ERROR_WHILE_FINDING_USERID, ErrorCodes.ERR_17001.getErrorCode());	
			}
			
			CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);
			
			boolean upsertSuccessful = campaignBusinessDelegate.insertCustCompletedCampaigns(userId , campaignId);
			res.addParam("Success", String.valueOf(upsertSuccessful));
		}catch (CampaignException e) {			
			alert.prepareError("Error while getting campaign " , e).log();
			CampaignUtil.addDBPErrCodeAndmsg(res, e.getMessage(), e.getErrorCode());
		}
		catch (Exception e) {			
			alert.prepareError("Error while getting campaign " , e).log();
			CampaignUtil.addDBPErrCodeAndmsg(res, e.getMessage(),  ErrorCodes.ERR_17011.getErrorCode());
		}
		return res;
	}

	private String getUserID(DataControllerRequest request, Result res) {
		 String userId = null;
		try {
			if (request.getServicesManager().getIdentityHandler() != null) {
			    userId = request.getServicesManager().getIdentityHandler().getUserId();
			}
		} catch (AppRegistryException e) {
			alert.prepareError(CampaignConstants.ERROR_WHILE_FINDING_USERID , e).log();
			CampaignUtil.addDBPErrCodeAndmsg(res, CampaignConstants.ERROR_WHILE_FINDING_USERID, ErrorCodes.ERR_17001.getErrorCode());
		} 
		return userId;
	}

	@Override
	public Result getAllCampaignsForAnEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);
		
		String eventCode = request.getParameter(CampaignConstants.PARAM_EVENT_CODE);
		
		result = campaignBusinessDelegate.getAllCampaignsForAnEvent(request);
		
		return result;
	}

	@Override
	public Result getDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);
		
		String eventCode = request.getParameter(CampaignConstants.PARAM_EVENT_CODE);
		
		result = campaignBusinessDelegate.getDefaultCampaigns(request);
		
		return result;
	}	

}

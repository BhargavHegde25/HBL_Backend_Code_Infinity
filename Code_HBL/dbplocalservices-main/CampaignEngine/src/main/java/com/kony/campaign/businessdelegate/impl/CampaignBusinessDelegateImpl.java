package com.kony.campaign.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.campaign.CacheCampaignEvalutor;
import com.kony.campaign.CampaignEvalutor;
import com.kony.campaign.CampaignException;
import com.kony.campaign.InternalResponseProcessor;
import com.kony.campaign.JobCampaignEvalutor;
import com.kony.campaign.PreLoginCampaignEvalutor;
import com.kony.campaign.RealTimeCampaignEvalutor;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.CampaignConstants.CampaignFilterTypes;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.CampaignRequestType;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.engine.CampaignProcessor;
import com.kony.campaign.engine.CampaignThreadPoolExecutor;
import com.kony.campaign.engine.DelayedRealTimeExecutorTask;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CampaignBusinessDelegateImpl implements CampaignBusinessDelegate{	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getInternalCampaigns(EventDTO event) {
		CampaignRequest cr = null;
		if(event.getEventId().equals(CampaignConstants.PRELOGIN)) {
			 cr = new CampaignRequest(CampaignRequestType.PRELOGIN_EVENT,event,false);
			 cr.setCampaignFilter(CampaignFilterTypes.FILTER_ON_PLACEHOLDER.name());
			 cr.setCampaignEvalutor(new PreLoginCampaignEvalutor());
		}else { 
			CampaignEvalutor ce = null;
			if(StringUtils.isNotBlank(event.getPlaceholderCode())){
				String campFetchType = CampaignUtil.getOnlineCampaignResponsiveProperty();
				cr = new CampaignRequest(CampaignRequestType.ONLINE_PLACEHOLDER_EVENT,event,
											CampaignUtil.isCacheUpdateRequired(campFetchType));
				cr.setCampaignFilter(CampaignFilterTypes.FILTER_ON_PLACEHOLDER.name());
				ce = getCampaignEvalutor(campFetchType,true);
				cr.setCampaignEvalutor(ce);
			}
			if(ce == null) {
				return processOnlineNonPlaceHolderEvent(event);
			}	
		}
		cr.setCampaignCountInResponse(CampaignUtil.getIntServerProperty(CampaignConstants.CAMPAIGNS_CAROUSEL_NUMBER, 1));
		return CampaignProcessor.processCampaignRequest(cr);	
	}		
	
	@Override
	public Result getExternalCampaigns(EventDTO event) {		
		CampaignRequest cr =new CampaignRequest(CampaignRequestType.OFFLINE_EVENT,event,
										CampaignUtil.isCacheUpdateRequired());
		cr.setCampaignEvalutor(new RealTimeCampaignEvalutor());		
		return processOfflineCampainRequest(cr);
	}
	
	@Override
	public Result getCampaignsForJob() {
		CampaignRequest cr = new CampaignRequest(CampaignRequestType.JOB);
        cr.setCampaignEvalutor(new JobCampaignEvalutor());
        return processOfflineCampainRequest(cr);
	}
	
	private Result processOnlineNonPlaceHolderEvent(EventDTO event) {
		try {
			CampaignThreadPoolExecutor.execute(new DelayedRealTimeExecutorTask(event));
		} catch (Exception e) {
			alert.prepareError("Interupt while invoking DelayedRealTimeExecutor" , e).log();
		}
		return InternalResponseProcessor.getEmptyCampaignResult();
	}
	
	private Result processOfflineCampainRequest(CampaignRequest c) {
		Result res = CampaignProcessor.processCampaignRequest(c);
        if(res == null ||  res.getParamByName(CampaignConstants.DBP_ERROR_MESSAGE) != null ) {
        	Result resfinal = res != null ? res : new Result();
        	alert.prepareError("Error while processing external event "+ resfinal.getParamValueByName(CampaignConstants.DBP_ERROR_MESSAGE)).log();
        	resfinal.addParam("success", Boolean.FALSE.toString());
        	return resfinal;
        }       
        res.addParam("success",Boolean.TRUE.toString());
        return res;
	}
		

	private CampaignEvalutor getCampaignEvalutor(String campaignFetchType, boolean isPlaceHolderPresent) {
		CampaignEvalutor ce = null;
		if(isPlaceHolderPresent) {
		    if(campaignFetchType.equalsIgnoreCase(CampaignConstants.CACHE)) {
		        	ce = new CacheCampaignEvalutor();
		    }else {
			  ce = new RealTimeCampaignEvalutor();
		    }
	    }		
		return ce;
	}

	@Override
	public boolean insertCustCompletedCampaigns(String userId, String campaignId) throws CampaignException {
		try {
			Map<String, Object> inputMap = new HashMap<>();	
			inputMap.put(CampaignConstants.PARAM_CUSTOMER_ID, userId);
			inputMap.put(CampaignConstants.PARAM_CAMPAIGN_ID, campaignId);
			Result dbRes = CampaignUtil.invokeService(CampaignConstants.CAMPAIGN_DB_SERVICE, 
					CampaignUtil.getDatabaseServiceNames(CampaignConstants.CUST_COMPLETED_CAMPAIGN_POST_OPERATION), inputMap);
			if(dbRes.getParamValueByName(CampaignConstants.ERRMSG) != null) {
				alert.prepareError(ErrorCodes.ERR_17012.getMessage() + " " + dbRes.getParamValueByName(CampaignConstants.ERRMSG)).log();
				throw new CampaignException(ErrorCodes.ERR_17012.getMessage() + dbRes.getParamValueByName(CampaignConstants.ERRMSG),ErrorCodes.ERR_17012.getErrorCode());
			}
		} catch (Exception e) {
			alert.prepareError(ErrorCodes.ERR_17012.getMessage() , e).log();
			throw new CampaignException(ErrorCodes.ERR_17012.getMessage(),e,ErrorCodes.ERR_17012.getErrorCode());
		}
		return true;
	}


	@Override
	public Result getAllCampaignsForAnEvent(DataControllerRequest request) {
		
		Result result = new Result();
		Map<String, Object> inputMap = new HashMap<>();	   
		inputMap.put(CampaignConstants.PARAM_EVENT_CODE,request.getParameter(CampaignConstants.PARAM_EVENT_CODE));		
		inputMap.put(CampaignConstants.PARAM_PLACEHOLDER_CODE,request.getParameter(CampaignConstants.PARAM_PLACEHOLDER_CODE));		
		inputMap.put(CampaignConstants.PARAM_SCALE,request.getParameter(CampaignConstants.PARAM_SCALE));
		inputMap.put(CampaignConstants.PARAM_CHANNEL_TYPE,request.getParameter(CampaignConstants.PARAM_CHANNEL_TYPE));
		inputMap.put(CampaignConstants.PARAM_CORE_CUSTOMER_ID,request.getParameter(CampaignConstants.PARAM_CORE_CUSTOMER_ID));
		inputMap.put(CampaignConstants.PARAM_CAMPAIGN_STATUS,"SCHEDULED_ACTIVE_COMPLETED");
		try {	
		String response = DBPServiceExecutorBuilder.builder().
				withServiceId(CampaignConstants.CAMPAIGN_MANAGEMENT_JAVA).
				withOperationId(CampaignConstants.GET_CAMPAIGNS_OPERATION).
				withDataControllerRequest(request).
				withRequestParameters(inputMap).
				build().
				getResponse();
	
		diagnostic.debug("response...."+ response.toString());
		JSONObject obj = new JSONObject(response);
		obj = mapResponse(obj);
		result = JSONToResult.convert(obj.toString());
		}
		catch(Exception e) {
			alert.prepareError(ErrorCodes.ERR_17013.getMessage() , e).log();
		}
		return result;
	}

	@Override
	public Result getDefaultCampaigns(DataControllerRequest request) {
		Result result = new Result();
		try {
			
			
			String placeHolderCode = request.getParameter(CampaignConstants.PARAM_PLACEHOLDER_CODE);		
			String scale = request.getParameter(CampaignConstants.PARAM_SCALE);
			String channelType = request.getParameter(CampaignConstants.PARAM_CHANNEL_TYPE);
			
			
			Map<String, Object> inputMap = new HashMap<>();	  
			String defaultCampaignsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(CampaignConstants.CAMPAIGN_MANAGEMENT_JAVA).
					withOperationId(CampaignConstants.GET_ALL_DEFAULT_CAMPAIGNS).
					withDataControllerRequest(request).
					withRequestParameters(inputMap).
					build().
					getResponse();
			
			inputMap.clear();
			
			String filter = getFilter(placeHolderCode, scale, channelType, inputMap);
			
			diagnostic.debug("filter for placeholder...."+ filter);
			List<String> placeHoldersList =  getPlaceHoldersList(request, inputMap, filter);

			JSONObject resultObj = getFilterDefaultCampaigns(defaultCampaignsResponse, placeHoldersList);
			
			result = JSONToResult.convert(resultObj.toString());
			}
			catch(Exception e) {
				alert.prepareError(ErrorCodes.ERR_17004.getMessage() , e).log();
			}
			return result;
	}

	private JSONObject getFilterDefaultCampaigns(String response,
			List<String> placeHoldersList) {

		JSONObject obj = new JSONObject(response);
		JSONArray defaultCampignsArray =  obj.optJSONArray(CampaignConstants.CAMPAIGNLIST);
		JSONArray resultCampignsArray =  new JSONArray();
		for(int i=0; (defaultCampignsArray!=null 
						&& !defaultCampignsArray.isEmpty() 
							&& i<defaultCampignsArray.length()); i++) {
			JSONObject camp = defaultCampignsArray.getJSONObject(i);
			if(placeHoldersList.contains(camp.getString(CampaignConstants.PLACEHOLDERID))){
				resultCampignsArray.put(camp);
			}
		}

		JSONObject resultObj = new JSONObject(response);
		resultObj.put(CampaignConstants.CAMPAIGNLIST, resultCampignsArray);
		resultObj = mapResponseForDefaultCampaigns(resultObj);
		return resultObj;
	}

	private List<String> getPlaceHoldersList(DataControllerRequest request,
			Map<String, Object> inputMap, String filter) throws DBPApplicationException {
		List<String> placeHoldersList = new ArrayList<>();
		inputMap.put(CampaignConstants.ODATA_FILTER, filter);
		String placeHolders = DBPServiceExecutorBuilder.builder().
				withServiceId(CampaignConstants.CRUDLAYER).
				withOperationId(CampaignConstants.DBXDB_PLACEHOLDER_GET).
				withDataControllerRequest(request).
				withRequestParameters(inputMap).
				build().
				getResponse();
		JSONObject placeHoldersResponseObj = new JSONObject(placeHolders);
		JSONArray placeHoldersArray = placeHoldersResponseObj.getJSONArray(CampaignConstants.PLACEHOLDER);
		for(int i=0;i <placeHoldersArray.length(); i++) {
			placeHoldersList.add(placeHoldersArray.getJSONObject(i).getString(CampaignConstants.PLACEHOLDERID));
		}
		return placeHoldersList;
	}

	private String getFilter(String placeHolderCode, String scale, String channelType, Map<String, Object> inputMap) {
		String filter= "";
		if(StringUtils.isNotBlank(channelType)) {
			filter = filter + "channelSubType eq " +channelType;
		}
		if(StringUtils.isNotBlank(placeHolderCode)) {
			if(filter.length()> 0) {
				filter = filter+ " and ";
			}
			filter = filter + "placeholderIdentifier eq " + placeHolderCode;
		}
		if(StringUtils.isNotBlank(scale)) {
			if(filter.length()> 0) {
				filter = filter+ " and ";
			}
			filter = filter + "imageScale eq " + scale;
		}
		inputMap.put(CampaignConstants.ODATA_FILTER, filter);
		return filter;
	}
	
	private JSONObject mapResponse(JSONObject obj) {
		JSONArray campaignList = obj.optJSONArray(CampaignConstants.CAMPAIGNLIST);
		JSONArray campaignList1 = new JSONArray();
		for(int i=0;campaignList!=null && i<campaignList.length();i++) {
			JSONObject campaign = campaignList.optJSONObject(i);
			JSONObject campaign1 = new JSONObject();
			if(campaign!=null && !campaign.isEmpty()) {
				campaign1.put("campaignType", campaign.optString("campaignType"));
				campaign1.put("productId", campaign.optString("productId"));
				campaign1.put("endDate", campaign.optString("endDate"));
				campaign1.put("campaignId", campaign.optString("campaignId"));
				campaign1.put("objectiveId", campaign.optString("objectiveId"));
				campaign1.put("objectiveType", campaign.optString("objectiveType"));
				campaign1.put("channelType", campaign.optJSONArray("channelType"));
				campaign1.put("onlineContent", campaign.optJSONArray("onlineContent"));
				campaign1.put("channelDetails", campaign.optJSONArray("channelDetails"));
				campaign1.put("campaignPriority", campaign.optString("campaignPriority"));
				campaign1.put("campaignStatus", campaign.optString("campaignStatus"));
				campaign1.put("campaignDescription", campaign.optString("campaignDescription"));
				campaign1.put("campaignName", campaign.optString("campaignName"));
				campaign1.put("startDate", campaign.optString("startDate"));
				campaign1.put("onlineContentString", campaign.optJSONArray("onlineContents"));
				JSONArray profileDetails = campaign.optJSONArray("profileDetails");
				
				JSONArray dataContext1 = new JSONArray();
				for(int j=0;profileDetails!=null && j<profileDetails.length();j++) {
					
					JSONObject profileDetail = profileDetails.optJSONObject(j);
					if(profileDetail!=null) {
						JSONArray profileConditions = 	profileDetail.optJSONArray("profileConditions");
						for(int k=0;profileConditions!=null && k<profileConditions.length();k++) {
							JSONObject dc = new JSONObject();
							JSONObject profileCondition = profileConditions.optJSONObject(k);
							if(profileCondition!= null) {
								dc.put("attributes", profileConditions.optJSONObject(k).optString("conditionExpression"));
								dc.put("name", profileConditions.optJSONObject(k).optString("dataContextId"));
								dc.put("EndPointURL", profileConditions.optJSONObject(k).optString("dataContextEndPoints"));
								dataContext1.put(dc);
							}
						}
					}
				}
				campaign1.put("DataContext", dataContext1);
				JSONArray offlineTemplate = campaign.optJSONArray("offlineTemplate");
				JSONArray offlineTemplate1 = new JSONArray();
				for(int j=0;offlineTemplate!=null && j<offlineTemplate.length();j++) {
					JSONObject ot = offlineTemplate.optJSONObject(j);
					ot.put("content", ot.optString("messageContent"));
					ot.remove("messageContent");
					offlineTemplate1.put(ot);
				}
				campaign1.put("offlineTemplate", offlineTemplate1);
				campaign1.remove("profileDetails");
				campaign1.remove("eventTriggerDetails");
				if(campaign.has("message") || campaign.has("code")) {
					campaign.put("errmsgForDefault", campaign.optJSONArray("message"));
					campaign.put("errcodeForDefault", campaign.optJSONArray("code"));
					campaign1.remove("message");
					campaign1.remove("code");
				}
			}
			campaignList1.put(campaign1);
		}
		JSONObject response = new JSONObject();
		response.put("CampaignList", campaignList1);
		return response;
	}
	private JSONObject mapResponseForDefaultCampaigns(JSONObject obj) {
		JSONArray campaignList = obj.optJSONArray("campaignList");
		for(int i=0;campaignList!=null && i<campaignList.length();i++) {
			JSONObject campaign = campaignList.optJSONObject(i);
			if(campaign!=null && !campaign.isEmpty()) {
				campaign.put("onlineContentString", campaign.optJSONArray("onlineContents"));
				campaign.remove("onlineContents");
				if(campaign.has("message") || campaign.has("code")) {
					campaign.put("errmsgForDefault", campaign.optJSONArray("message"));
					campaign.put("errcodeForDefault", campaign.optJSONArray("code"));
					campaign.remove("message");
					campaign.remove("code");
				}
				
				
			}
		}
		JSONObject response = new JSONObject();
		response.put("DefaultcampaignList", campaignList);
		return response;
	}
}

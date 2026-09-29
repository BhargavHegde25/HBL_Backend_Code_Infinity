package com.kony.campaignsmanagement.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.resource.api.CampaignsManagementResource;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;

import com.kony.campaignsmanagement.utils.ErrorCodesEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CampaignsManagementResourceImpl implements CampaignsManagementResource {

	private static final Alert alert = Logger.forAlert().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);
	
	private static final String EVENT_TRIGGERID_LIST = "eventTriggerIdList";
	private static final String PROFILEID_LIST = "profileIdList";
	private static final String CHANNEL_TYPE = "channelType";
	private static final String OFFLINE_TEMPLATE = "offlineTemplate";
	private static final String ONLINE_CONTENT = "onlineContent";
	private static final String CHANNEL_DETAILS = "channelDetails";
	
	
	private static final String CAMPAIGN_ID = "campaignId";
	private static final String CAMPAIGN_NAME = "campaignName";
	private static final String CAMPAIGN_DESCRIPTION = "campaignDescription";
	private static final String CAMPAIGN_PRIORITY = "campaignPriority";
	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String CAMPAIGN_TYPE = "campaignType";
	private static final String OBJECTIVE_TYPE = "objectiveType";
	private static final String PRODUCTID = "productId";
	private static final String PRODUCT_GROUPID = "productGroupId";
	private static final String CAMPAIGN_STATUS = "campaignStatus";

	@Override
	public Result getAllPlaceHolders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);

			String campaignsBackend = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
					CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);
			
			JSONArray responseArray = new JSONArray();
			if (campaignsBackend.equalsIgnoreCase(CMConstants.MS)) {
				responseArray = businessDelegate.getAllPlaceHoldersMS(request);
			} else if(campaignsBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
				responseArray = businessDelegate.getAllPlaceHolders(request);
			}

			if (responseArray != null && responseArray.length() > 0) {
				Dataset ds = CommonUtilities.constructDatasetFromJSONArray(responseArray);
				ds.setId(CMConstants.PLACEHOLDERS_MS);
				result.setDataSet(ds);
			} else {
				result.addParam(CMConstants.PLACEHOLDERS_MS, new JSONArray().toString(), CMConstants.STRING);
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to fetch placeholders: " + e.getMessage()).log();
			diagnostic.prepareDebug("Failed to fetch placeholders : " + e.getMessage()).log();
			return ErrorCodeEnum.ERR_22239.setErrorCode(result);
		}
	}

	@Override
	public Result getEventTriggers(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);

			String campaignsBackend = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
					CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);
			
			JSONArray responseArray = new JSONArray();
			if (campaignsBackend.equalsIgnoreCase(CMConstants.MS)) {
				responseArray = businessDelegate.getEventTriggersMS(request);
			} else if(campaignsBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
				responseArray = businessDelegate.getEventTriggers(request);
			}

			if (responseArray != null && responseArray.length() > 0) {
				Dataset ds = CommonUtilities.constructDatasetFromJSONArray(responseArray);
				ds.setId(CMConstants.EVENTTRIGGERS_MS);
				result.setDataSet(ds);
			} else {
				result.addParam(CMConstants.EVENTTRIGGERS_MS, new JSONArray().toString(), CMConstants.STRING);
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to fetch eventtriggers: " + e.getMessage()).log();
			diagnostic.prepareDebug("Failed to fetch eventtriggers : " + e.getMessage()).log();
			return ErrorCodeEnum.ERR_22240.setErrorCode(result);
		}
	}
	
	
	@Override
	public Result createCampaign(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			if (dcRequest.getParameter(CAMPAIGN_NAME) == null || dcRequest.getParameter(CAMPAIGN_DESCRIPTION) == null
					|| dcRequest.getParameter(CAMPAIGN_PRIORITY) == null || dcRequest.getParameter(START_DATE) == null
					|| dcRequest.getParameter(END_DATE) == null || dcRequest.getParameter(CAMPAIGN_TYPE) == null
					|| dcRequest.getParameter(OBJECTIVE_TYPE) == null || dcRequest.getParameter(PRODUCTID) == null
					|| dcRequest.getParameter(PRODUCT_GROUPID) == null) {
				ErrorCodeEnum.ERR_22245.setErrorCode(result);
				return result;
			} else if (dcRequest.getParameter(EVENT_TRIGGERID_LIST) == null) {
				ErrorCodeEnum.ERR_22249.setErrorCode(result);
				return result;
			} else if (dcRequest.getParameter(PROFILEID_LIST) == null) {
				ErrorCodeEnum.ERR_22250.setErrorCode(result);
				return result;
			}
			else if (dcRequest.getParameter(CHANNEL_TYPE) == null) {
				ErrorCodeEnum.ERR_22241.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(OFFLINE_TEMPLATE) == null) {
				ErrorCodeEnum.ERR_22242.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(ONLINE_CONTENT) == null) {
				ErrorCodeEnum.ERR_22243.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(CHANNEL_DETAILS) == null) {
				ErrorCodeEnum.ERR_22244.setErrorCode(result);
				return result;
			}else {
				Map<String, Object> postParametersMap = new HashMap<>();
				readCampaignCommonInputParams(postParametersMap, dcRequest);
				postParametersMap.put(CAMPAIGN_STATUS, ("SCHEDULED_ACTIVE_COMPLETED"));
				CampaignsManagementBusinessDelegate campaignManagementManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
				JSONObject createCampaignResponse = campaignManagementManagementBusinessDelegate
						.createCampaign(postParametersMap, dcRequest.getHeaderMap());
				if (createCampaignResponse == null || !createCampaignResponse.has(FabricConstants.OPSTATUS)
						|| createCampaignResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22246.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Campaign creation failed");
					return result;
				} else if (createCampaignResponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(createCampaignResponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					result = CommonUtilities.constructResultFromJSONObject(createCampaignResponse);
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(
							new Param("id", createCampaignResponse.getString("id"), FabricConstants.STRING));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create campaign: ", e).log();
			result.addParam(new Param("FailureReason", "Error in create campaign", FabricConstants.STRING));
			ErrorCodeEnum.ERR_22246.setErrorCode(result);
		}
		return result;

	}
	
	@Override
	public Result updateCampaign(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			if (dcRequest.getParameter(CAMPAIGN_NAME) == null || dcRequest.getParameter(CAMPAIGN_DESCRIPTION) == null
					|| dcRequest.getParameter(CAMPAIGN_PRIORITY) == null || dcRequest.getParameter(START_DATE) == null
					|| dcRequest.getParameter(END_DATE) == null || dcRequest.getParameter(CAMPAIGN_TYPE) == null
					|| dcRequest.getParameter(OBJECTIVE_TYPE) == null || dcRequest.getParameter(PRODUCTID) == null
					|| dcRequest.getParameter(PRODUCT_GROUPID) == null || dcRequest.getParameter(CAMPAIGN_ID) == null
					||dcRequest.getParameter(CAMPAIGN_STATUS) == null) {
				ErrorCodeEnum.ERR_22245.setErrorCode(result);
				return result;
			} else if (dcRequest.getParameter(EVENT_TRIGGERID_LIST) == null) {
				ErrorCodeEnum.ERR_22249.setErrorCode(result);
				return result;
			} else if (dcRequest.getParameter(PROFILEID_LIST) == null) {
				ErrorCodeEnum.ERR_22250.setErrorCode(result);
				return result;
			}
			else if (dcRequest.getParameter(CHANNEL_TYPE) == null) {
				ErrorCodeEnum.ERR_22241.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(OFFLINE_TEMPLATE) == null) {
				ErrorCodeEnum.ERR_22242.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(ONLINE_CONTENT) == null) {
				ErrorCodeEnum.ERR_22243.setErrorCode(result);
				return result;
			}else if (dcRequest.getParameter(CHANNEL_DETAILS) == null) {
				ErrorCodeEnum.ERR_22244.setErrorCode(result);
				return result;
			}else {
				Map<String, Object> postParametersMap = new HashMap<>();
				readCampaignCommonInputParams(postParametersMap,dcRequest);
				postParametersMap.put(CAMPAIGN_ID, dcRequest.getParameter(CAMPAIGN_ID));
				postParametersMap.put(CAMPAIGN_STATUS, dcRequest.getParameter(CAMPAIGN_STATUS));
				
				CampaignsManagementBusinessDelegate campaignManagementManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
				
				JSONObject createCampaignResponse = campaignManagementManagementBusinessDelegate
						.updateCampaign(postParametersMap, dcRequest.getHeaderMap());
				if (createCampaignResponse == null || !createCampaignResponse.has(FabricConstants.OPSTATUS)
						|| createCampaignResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22252.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Campaign creation failed");
					return result;
				} else if (createCampaignResponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(createCampaignResponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(
							new Param("id", createCampaignResponse.getString("id"), FabricConstants.STRING));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create campaign: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22252.setErrorCode(result);
		}
		return result;

	}
	
	private void readCampaignCommonInputParams(Map<String, Object> postParametersMap, DataControllerRequest dcRequest) {

		postParametersMap.put(EVENT_TRIGGERID_LIST, dcRequest.getParameter(EVENT_TRIGGERID_LIST));
		postParametersMap.put(PROFILEID_LIST, dcRequest.getParameter(PROFILEID_LIST));
		postParametersMap.put(CHANNEL_TYPE, dcRequest.getParameter(CHANNEL_TYPE));
		postParametersMap.put(OFFLINE_TEMPLATE, dcRequest.getParameter(OFFLINE_TEMPLATE));
		postParametersMap.put(ONLINE_CONTENT, dcRequest.getParameter(ONLINE_CONTENT));
		postParametersMap.put(CHANNEL_DETAILS, dcRequest.getParameter(CHANNEL_DETAILS));

		postParametersMap.put(CAMPAIGN_NAME, dcRequest.getParameter(CAMPAIGN_NAME));
		postParametersMap.put(CAMPAIGN_DESCRIPTION, dcRequest.getParameter(CAMPAIGN_DESCRIPTION));
		postParametersMap.put(CAMPAIGN_PRIORITY, dcRequest.getParameter(CAMPAIGN_PRIORITY));
		postParametersMap.put(START_DATE, dcRequest.getParameter(START_DATE));
		postParametersMap.put(END_DATE, dcRequest.getParameter(END_DATE));
		postParametersMap.put(CAMPAIGN_TYPE, dcRequest.getParameter(CAMPAIGN_TYPE));
		postParametersMap.put(OBJECTIVE_TYPE, dcRequest.getParameter(OBJECTIVE_TYPE));
		postParametersMap.put(PRODUCTID, dcRequest.getParameter(PRODUCTID));
		postParametersMap.put(PRODUCT_GROUPID, dcRequest.getParameter(PRODUCT_GROUPID));

	}
	
	@Override
	public Result getCampaigns(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
		Result result = new Result();
		try {
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("_eventCode",StringUtils.isNotBlank(dcRequest.getParameter("eventCode")) ? dcRequest.getParameter("eventCode") : "");
				postParametersMap.put("_status",StringUtils.isNotBlank(dcRequest.getParameter("campaignStatus")) ? dcRequest.getParameter("campaignStatus") : "");
				CampaignsManagementBusinessDelegate campaignManagementManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
				
				JSONObject getCampaignsResponse = campaignManagementManagementBusinessDelegate
						.getCampaigns(postParametersMap, dcRequest.getHeaderMap());
				if (getCampaignsResponse == null || !getCampaignsResponse.has(FabricConstants.OPSTATUS)
						|| getCampaignsResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22251.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Campaigns get failed");
					return result;
				} else if (getCampaignsResponse.has("dbpErrMsg")) {
					//result = CommonUtilities.constructResultFromJSONObject(getCampaignsResponse);
					result = JSONToResult.convert(getCampaignsResponse.toString());
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					result = JSONToResult.convert(getCampaignsResponse.toString());
					//result = CommonUtilities.constructResultFromJSONObject(getCampaignsResponse);
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(new Param("opstatus", getCampaignsResponse.get("opstatus").toString(),
							FabricConstants.STRING));
					
				}
			
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in get campaigns: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22251.setErrorCode(result);
		}
		return result;

	}
	@Override
	public Result createProfile(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();
		CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
		try {
			 String campaignBackendConfig = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
	    				CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);
			//validate mandatory fields  for profile
			String profileName = dataControllerRequest.getParameter(CMConstants.PROFILE_NAME);
            String profileDesc = dataControllerRequest.getParameter(CMConstants.PROFILE_DESC);
            String profileConditions = dataControllerRequest.getParameter(CMConstants.PROFILE_CONDITION);
            String profileId = "PRF" + Long.toString(CommonUtilities.getNumericId());
            dataControllerRequest.addRequestParam_(CMConstants.PROFILE_ID, profileId);
            
            if(StringUtils.isBlank(profileName) || StringUtils.isBlank(profileDesc) || 
            		StringUtils.isBlank(profileId) || StringUtils.isBlank(profileConditions)) {
            	alert.prepareError("Profile validation failed").log();
            	AuditHandler.auditAdminActivity(dataControllerRequest, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Profile validation failed");
            	return ErrorCodesEnum.ERR_10006.setErrorCode(result);
            }
			if (campaignBackendConfig.equalsIgnoreCase(CMConstants.DBXDB)) {
				JSONArray existingProfiles = getProfilesInDB(profileName, null);
				if (existingProfiles != null && existingProfiles.length() > 0) {
					alert.prepareError("Profile with similar name exists").log();
					AuditHandler.auditAdminActivity(dataControllerRequest, ModuleNameEnum.CAMPAIGN,
								EventEnum.CREATE, ActivityStatusEnum.FAILED, "Profile validation failed");
					return ErrorCodesEnum.ERR_10007.setErrorCode(result);
				}
			}
            //validate mandatory fields for profile conditions
            JSONArray profileCondArr = new JSONArray(profileConditions);
            for(int i=0; i<profileCondArr.length(); i++) {
            	JSONObject profileCondObj = profileCondArr.getJSONObject(i);
            	String dataContextId = profileCondObj.get(CMConstants.DATACONTEXT_ID).toString();
                String profileCondExpr = profileCondObj.get(CMConstants.CONDITION_EXPRESSION).toString();
              
                if(StringUtils.isBlank(dataContextId) || StringUtils.isBlank(profileCondExpr)) {
                	alert.prepareError("Profile conditions validation failed").log();
                	AuditHandler.auditAdminActivity(dataControllerRequest, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
                            ActivityStatusEnum.FAILED, "Profile condition validation failed");
                	return ErrorCodesEnum.ERR_10006.setErrorCode(result);
                }
            }
            
            JSONObject createProfileResponseObj = null;
            //create profile based on the configuration
            if(campaignBackendConfig.equalsIgnoreCase(CMConstants.DBXDB)) {
            	createProfileResponseObj = businessDelegate.createProfileDBXDB(dataControllerRequest);
			} else if(campaignBackendConfig.equalsIgnoreCase(CMConstants.MS)) {
				createProfileResponseObj = businessDelegate.createProfileMS(dataControllerRequest);
			}
            
			if(createProfileResponseObj != null && createProfileResponseObj.has("id")) {
				//append profile id to response
				result.addParam("id", createProfileResponseObj.getString("id"));
				return result;
			} else {
				alert.prepareError("Error creating profile").log();
				return ErrorCodesEnum.ERR_10003.setErrorCode(result);
			}
		} catch (Exception e) {
			alert.prepareError("Exception in create profile validation", e).log();
			result.addParam("errorMsg", "Error occurred while validating profile data");
			return ErrorCodesEnum.ERR_10003.setErrorCode(result);
		}
	}
	
	@Override
	public JSONArray getProfilesInDB(String profileName, String profileId) {
		CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
		JSONArray responseArray = new JSONArray();
		try {
			responseArray = businessDelegate.getProfilesDB(profileName, profileId);
			
			if (responseArray != null && responseArray.length() > 0) {
				return responseArray;
			}
			
		}catch (Exception e) {
			alert.prepareError("Failed to fetch profiles from DB: " + e.getMessage()).log();
			diagnostic.prepareDebug("Failed to fetch profiles from DB : " + e.getMessage()).log();
		}
		return null;
	}
	
	@Override
	public Result getAllDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
			String campaignBackend = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
					CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);

			
			JSONArray responseArray = new JSONArray();
			if (campaignBackend.equalsIgnoreCase(CMConstants.MS)) {
				responseArray = businessDelegate.getAllDefaultCampaignsMS(request);
			} else if(campaignBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
				responseArray = businessDelegate.getAllDefaultCampaignsDBXDB(request);
			}

			if (responseArray != null && responseArray.length() > 0) {
				Dataset ds = CommonUtilities.constructDatasetFromJSONArray(responseArray);
				ds.setId(CMConstants.DEFAULT_CAMPAIGNS_MS);
				result.setDataSet(ds);
			} else {
				result.addParam(CMConstants.DEFAULT_CAMPAIGNS_MS, new JSONArray().toString(), CMConstants.STRING);
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to default campaigns: " + e.getMessage()).log();
			diagnostic.prepareDebug("Failed to default campaigns : " + e.getMessage()).log();
			return ErrorCodeEnum.ERR_22239.setErrorCode(result);
		}
	}

	@Override
	public Result getProfiles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		JSONArray profilesResponseArr = null;
		Result result = new Result();
		try {
			String campaignBackendConfig = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
					CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);
			
			CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
			// fetch profiles based on the configuration
			if (campaignBackendConfig.equalsIgnoreCase(CMConstants.DBXDB)) {
				profilesResponseArr = businessDelegate.getProfilesDBXDB(request);
			} else if (campaignBackendConfig.equalsIgnoreCase(CMConstants.MS)) {
				profilesResponseArr = businessDelegate.getProfilesMS(request);
			} 
			Dataset ds = new Dataset();
			if (profilesResponseArr != null && profilesResponseArr.length() > 0) {
				ds = CommonUtilities.constructDatasetFromJSONArray(profilesResponseArr);
				
			} else {
				ds = CommonUtilities.constructDatasetFromJSONArray(new JSONArray());
			}
			ds.setId(CMConstants.ALL_PROFILES);
			result.setDataSet(ds);
			return result;
			
		}catch (Exception e) {
			alert.prepareError("Exception while fetching profiles: " + e.getMessage()).log();
			diagnostic.prepareDebug("Exception while fetching profiles: " + e.getMessage()).log();
			return ErrorCodesEnum.ERR_10016.setErrorCode(result);
		}
	}

    @Override
	public Result updateProfile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
		Result result = new Result();
		try {
		//String profile = request.getParameter(CMConstants.PROFILE_DBXDB);
		JSONObject profileJSON = new JSONObject();
		String profileId = request.getParameter(CMConstants.PROFILE_ID);
		String profileName = request.getParameter(CMConstants.PROFILE_NAME);
		String profileDescription = request.getParameter(CMConstants.PROFILE_DESC);
		String profileStatus = request.getParameter(CMConstants.PROFILE_STATUS);
		String profileConditionsString = request.getParameter(CMConstants.PROFILE_CONDITION);
		JSONArray profileConditions = new JSONArray(profileConditionsString);
		profileJSON.put(CMConstants.PROFILE_ID, profileId);
		profileJSON.put(CMConstants.PROFILE_NAME, profileName);
		profileJSON.put(CMConstants.PROFILE_DESC, profileDescription);
		profileJSON.put(CMConstants.PROFILE_STATUS, profileStatus);
		profileJSON.put(CMConstants.PROFILE_CONDITION, profileConditions);
		 
		String campaignsBackend = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
				CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);
		JSONObject responseObj = null;
		
		JSONObject obj = validateInputsForUpdateProfile(request, profileId, profileName, profileDescription,
				profileStatus, profileConditions, campaignsBackend);
		if(obj.has(CMConstants.DBP_ERR_CODE)) {
			return CommonUtilities.constructResultFromJSONObject(obj);
		}
		if (campaignsBackend.equalsIgnoreCase(CMConstants.MS)) {
			responseObj = businessDelegate.updateProfileMS(request, profileJSON);
		} else if(campaignsBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
			obj = validateProfileData(request, profileId, profileName);
			if(obj.has(CMConstants.DBP_ERR_CODE)) {
				return CommonUtilities.constructResultFromJSONObject(obj);
			}
			JSONObject removeProfileConditions = deleteRemovedProfileConditions(profileId, profileConditions);
			boolean isDeleted = removeProfileConditions.optBoolean("success", false);
			if(isDeleted) {
				responseObj = businessDelegate.updateProfile(profileJSON, request);
			}
			else {
				alert.prepareError("Failed to delete removed profile conditions.").log();
				return ErrorCodesEnum.ERR_10015.setErrorCode(result);
			}
		}
		if(responseObj != null && responseObj.has(CMConstants.PROFILE_ID)) {
			//append profile id to response
			result.addParam(CMConstants.PROFILE_ID, responseObj.getString(CMConstants.PROFILE_ID));
			return result;
		} else {
			alert.prepareError("Error updating profile").log();
			return ErrorCodesEnum.ERR_10009.setErrorCode(result);
		}
		}
		catch(Exception e) {
			alert.prepareError("Exception in updating profile", e).log();
			result.addParam("errorMsg", "Error occurred while updating profile ");
			return ErrorCodesEnum.ERR_10009.setErrorCode(result);
		}
	}

	public JSONObject validateProfileData(DataControllerRequest request, String profileId,
			String profileName) {
		JSONArray profileArr = getProfilesInDB(null, profileId);
		JSONObject result = new JSONObject();
		if(profileArr == null || profileArr.length()==0){
			alert.prepareError("No profile found to update").log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
		            ActivityStatusEnum.FAILED, "No profile found to update");
			return ErrorCodesEnum.ERR_10008.setErrorCode(result);
		}
		JSONObject profileObj = profileArr.optJSONObject(0);
		if(profileObj.optString(CMConstants.PROFILE_STATUS).equalsIgnoreCase(CMConstants.DELETED)) {
			return ErrorCodesEnum.ERR_10014.setErrorCode(result);
		}
		profileArr = getProfilesInDB(profileName, profileId);
		if(profileObj.optString(CMConstants.PROFILE_NAME).equals(profileName)) {
			if(profileArr != null && profileArr.length()>0) {
				return ErrorCodesEnum.ERR_10007.setErrorCode(result);
			}
		}
		else {
			for(int i=0;profileArr!=null && i<profileArr.length();i++) {
				JSONObject dbProfile = profileArr.getJSONObject(i);
				if(!dbProfile.optString(CMConstants.PROFILE_STATUS).equals(CMConstants.DELETED)) {
					return ErrorCodesEnum.ERR_10007.setErrorCode(result);
				}
			}
		}
		return result;
	}

	public JSONObject validateInputsForUpdateProfile(DataControllerRequest request, String profileId,
			String profileName, String profileDescription, String profileStatus, 
			JSONArray profileConditions, String campaignsBackend) {
		CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
		
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(profileId) || StringUtils.isBlank(profileName)
				||StringUtils.isBlank(profileDescription)|| profileConditions==null
				||profileConditions.length()==0) {
			alert.prepareError("Profile validation failed").log();
        	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Profile validation failed");
        	return ErrorCodesEnum.ERR_10006.setErrorCode(result);
		}
		List<String> conditionIds = new ArrayList<>();
		if (campaignsBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
			JSONArray profileConditionsDB = businessDelegate.getProfileConditionsDB(profileId);
			if (profileConditionsDB == null) {
				alert.prepareError("Profile validation failed").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Profile validation failed");
				return ErrorCodesEnum.ERR_10013.setErrorCode(result);
			}
			if (profileConditionsDB != null && profileConditionsDB.length() > 0) {
				for (int i = 0; i < profileConditionsDB.length(); i++) {
					conditionIds.add(profileConditionsDB.optJSONObject(i).optString(CMConstants.PROFILE_CONDITION_ID));
				}
			}
		}
		for(int i=0;i<profileConditions.length();i++) {
			JSONObject profileCondition = profileConditions.getJSONObject(i);
			String profileConditionId = profileCondition.optString(CMConstants.PROFILE_CONDITION_ID);
			String conditionExpression = profileCondition.optString(CMConstants.CONDITION_EXPRESSION);
			String dataContextId = profileCondition.optString(CMConstants.DATACONTEXT_ID);
			if(StringUtils.isBlank(dataContextId) || StringUtils.isBlank(conditionExpression)) {
            	alert.prepareError("Profile conditions validation failed").log();
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Profile condition validation failed");
            	return ErrorCodesEnum.ERR_10006.setErrorCode(result);
            }
			if(campaignsBackend.equalsIgnoreCase(CMConstants.DBXDB)
					&&StringUtils.isNotBlank(profileConditionId)&&(!conditionIds.contains(profileConditionId))) {
				alert.prepareError("Profile conditions validation failed").log();
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Profile condition validation failed");
            	return ErrorCodesEnum.ERR_10012.setErrorCode(result);
			}
		}
		return result;
	}
	
	@Override
	public JSONObject deleteRemovedProfileConditions(String profileId, JSONArray profileConditions) {
		CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
		List<String> deletedConditions = new ArrayList<>();
		JSONArray profileConditionsDB = businessDelegate.getProfileConditionsDB(profileId);
		List<String> conditionIds = new ArrayList<>();
		for (int i = 0; i < profileConditions.length(); i++) {
			conditionIds.add(profileConditions.optJSONObject(i).optString(CMConstants.PROFILE_CONDITION_ID));
		}
		for (int i = 0; i < profileConditionsDB.length(); i++) {
			String conditionId = profileConditionsDB.optJSONObject(i)
					.optString(CMConstants.PROFILE_CONDITION_ID);
			if(!conditionIds.contains(conditionId)) {
				deletedConditions.add(conditionId);
			}
		}
		JSONObject result = new JSONObject();
		if(deletedConditions.size()>0) {
			result = businessDelegate.deleteRemovedProfileConditions(profileId,deletedConditions);
		}
		else {
			result.put("success", true); 
		}
		return result;
		
	}
	
	@Override
	public Result updateDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			CampaignsManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CampaignsManagementBusinessDelegate.class);
			String campaignBackend = CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND,
					CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE);

			
			JSONObject updateDefaultCampaignsResponse = new JSONObject();
			if (campaignBackend.equalsIgnoreCase(CMConstants.MS)) {
				updateDefaultCampaignsResponse = businessDelegate.updateDefaultCampaignsMS(request);
			} else if(campaignBackend.equalsIgnoreCase(CMConstants.DBXDB)) {
				updateDefaultCampaignsResponse = businessDelegate.updateDefaultCampaignsDBXDB(request);
			}
				
			if (updateDefaultCampaignsResponse != null ) {
				result.addParam(new Param("message", updateDefaultCampaignsResponse.getString("message"), FabricConstants.STRING));
				result.addParam(
						new Param("onlineContentId", updateDefaultCampaignsResponse.getString("onlineContentId"), FabricConstants.STRING));
				result.addParam(new Param("status", updateDefaultCampaignsResponse.getString("status"), FabricConstants.STRING));
				
			} 
			
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to update default campaigns: " + e.getStackTrace()[0].toString()).log();
			diagnostic.prepareDebug("Failed to update default campaigns : " + e.getMessage()).log();
			return ErrorCodeEnum.ERR_22254.setErrorCode(result);
		}
	}
}


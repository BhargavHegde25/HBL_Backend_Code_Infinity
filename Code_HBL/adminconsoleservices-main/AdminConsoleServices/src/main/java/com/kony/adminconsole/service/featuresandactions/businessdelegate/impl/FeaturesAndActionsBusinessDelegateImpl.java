package com.kony.adminconsole.service.featuresandactions.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.api.FeaturesAndActionsBusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.dto.AccessPolicyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionLevelDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureActionsViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeaturesViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.LimitGroupDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class FeaturesAndActionsBusinessDelegateImpl implements FeaturesAndActionsBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public List<LimitGroupDTO> fetchAllLimitGroups() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_LIMITGROUP_VIEW;
       
		String limitgroupResponse = null;
		List<LimitGroupDTO> limitGroupDTOs = null;
		
		try {
			limitgroupResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					build().getResponse();
			JSONObject responseObj = new JSONObject(limitgroupResponse);
		    JSONArray jsonArray = responseObj.optJSONArray("limitgroups_view");
		    limitGroupDTOs = JSONUtils.parseAsList(jsonArray.toString(), LimitGroupDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch limit group from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAllLimitGroups: " + e).log();
			return null;
		}
		
		return limitGroupDTOs;
	}

	@Override
	public List<FeaturesViewDTO> fetchAllFeatures(String legalEntityId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_ALL_FEATURES_VIEW_GET;
       
        Map<String, Object> requestParameters = new HashMap<String, Object>();		
        String filter;
        
		if(StringUtils.isNotBlank(legalEntityId)) {
			filter = "companyLegalUnit eq '" + legalEntityId + "'" ;
			requestParameters.put(ODataQueryConstants.FILTER, filter);
		}
		
		String featureResponse = null;
		List<FeaturesViewDTO> featuresViewDTOs = null;
		
		try {			
			
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("get_all_features_view");
			featuresViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeaturesViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing features from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAllFeatures: " + e).log();
			return null;
		}
		
		return featuresViewDTOs;
	}

	@Override
	public LimitGroupDTO editLimitGroup(LimitGroupDTO limitGroupDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_LIMITGROUPDISPLAYDESCRIPTION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(limitGroupDTO).toString(), String.class, Object.class);
			requestParameters.put("limitGroupId",limitGroupDTO.getId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("limitgroupdisplaynamedescription");
			limitGroupDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), LimitGroupDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit limit group: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editLimitGroup method: " + e).log();
			return null;
		}

		return limitGroupDTO;
	}

	@Override
	public FeatureDTO editFeatureDetails(FeatureDTO featureDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATURE_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(featureDTO).toString(), String.class, Object.class);
			requestParameters.put("id",featureDTO.getFeatureId());
			requestParameters.put("Status_id",featureDTO.getStatusId());
			requestParameters.put("Service_Fee",featureDTO.getServiceFee());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("feature");
			featureDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), FeatureDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit feature " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editFeatureDetails method: " + e).log();
			return null;
		}

		return featureDTO;
	}

	@Override
	public FeatureDTO editFeatureDisplayNameDetails(FeatureDTO featureDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATUREDISPLAYDESCRIPTION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(featureDTO).toString(), String.class, Object.class);
			requestParameters.put("Feature_id",featureDTO.getId());
			requestParameters.put("Locale_id",featureDTO.getLocaleId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("featuredisplaynamedescription");
			featureDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), FeatureDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit feature display name " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editFeatureDisplayNameDetails method: " + e).log();
			return null;
		}

		return featureDTO;
	}

	@Override
	public ActionsDTO editActionDetails(ActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATUREACTION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("id",actionsDTO.getId());
			requestParameters.put("status",actionsDTO.getStatusId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("featureaction");
			actionsDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ActionsDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editActionDetails method: " + e).log();
			return null;
		}

		return actionsDTO;
	}

	@Override
	public boolean editActionLimits(ActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_ACTION_LIMITS_UPDATE_PROC;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("_action",actionsDTO.getId());
			requestParameters.put("_minTxLimit",actionsDTO.getMinTxLimit());
			requestParameters.put("_maxTxLimit",actionsDTO.getMaxTxLimit());
			requestParameters.put("_dailyLimit",actionsDTO.getDailyLimit());
			requestParameters.put("_weeklyLimit",actionsDTO.getWeeklyLimit());
			requestParameters.put("_companyLegalUnit",actionsDTO.getCompanyLegalUnit());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return false;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action limits " + e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editActionLimits method: " + e).log();
			return false;
		}

	}

	@Override
	public ActionsDTO editActionDisplayName(ActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_ACTIONDISPLAYDESCRIPTION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("Action_id",actionsDTO.getId());
			requestParameters.put("Locale_id",actionsDTO.getLocaleId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("actiondisplaynamedescription");
			actionsDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ActionsDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action display name " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editActionDisplayName method: " + e).log();
			return null;
		}

		return actionsDTO;
	}

	@Override
	public List<FeatureActionsViewDTO> fetchFeatureActions(String featureId, String legalEntityId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_FEATUREACTIONS_VIEW;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "featureId eq '" + featureId + "'";
		if(StringUtils.isNotBlank(legalEntityId)) {
			filter = filter + " and " + "companyLegalUnit eq '" + legalEntityId + "'";
		}
		requestParameters.put(ODataQueryConstants.FILTER, filter);
       
		String featureResponse = null;
		List<FeatureActionsViewDTO> featureActionsViewDTOs = null;
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("get_feature_actions_view");
			featureActionsViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeatureActionsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature actions from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchFeatureActions: " + e).log();
			return null;
		}
		
		return featureActionsViewDTOs;
	}

	@Override
	public List<FeatureActionsViewDTO> fetchMonetaryActions(String typeId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_FEATUREACTIONS_VIEW;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "typeId eq '" + typeId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
       
		String featureResponse = null;
		List<FeatureActionsViewDTO> featureActionsViewDTOs = null;
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("get_feature_actions_view");
			featureActionsViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeatureActionsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing monetary actions from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchFeatureActions: " + e).log();
			return null;
		}
		
		return featureActionsViewDTOs;
	}

	@Override
	public List<FeatureActionsViewDTO> fetchFeatureActionsByType(String roleTypeId,String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_FEATUREACTIONS_VIEW_PROC;

        

        HashMap<String, Object> params = new HashMap<String,Object>();

		params.put("_roleTypeId", roleTypeId);
		params.put("_companyLegalUnit", companyLegalUnit);

		
        
		String featureResponse = null;
		List<FeatureActionsViewDTO> featureActionsViewDTOs = null;
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(params).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("records");
			featureActionsViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeatureActionsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature actions by type from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchFeatureActionsByType: " + e).log();
			return null;
		}
		
		return featureActionsViewDTOs;
	}

	@Override
	public List<AccessPolicyDTO> fetchAccessPolicies() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACCESSPOLICY_GET;
        
		String response = null;
		List<AccessPolicyDTO> accessPolicyDTOs = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("accesspolicy");
			accessPolicyDTOs = JSONUtils.parseAsList(jsonArray.toString(), AccessPolicyDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing Access Policies from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAccessPolicies: " + e).log();
			return null;
		}
		
		return accessPolicyDTOs;
	}

	@Override
	public List<ActionDependencyDTO> fetchActionDependencies(String featureId, String legalEntityId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONDEPENDENCY_VIEW_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "featureId eq '" + featureId + "'";
		if(StringUtils.isNotBlank(legalEntityId)) {
			filter = filter + " and " + "companyLegalUnit eq '" + legalEntityId + "'";
		}

		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		List<ActionDependencyDTO> actionDependenciesDTOs = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("actiondependency_view");
			actionDependenciesDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionDependencyDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing action dependencies: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchActionDependencies: " + e).log();
			return null;
		}
		
		return actionDependenciesDTOs;
	}

	@Override
	public ActionsDTO editActionStatus(ActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATUREACTION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("Feature_id",actionsDTO.getId());
			requestParameters.put("status",actionsDTO.getStatusId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("featureaction");
			actionsDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ActionsDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editActionStatus method: " + e).log();
			return null;
		}

		return actionsDTO;
	}

	@Override
	public List<ActionLevelDTO> fetchActionLevels() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLEVEL_GET;
        
		String response = null;
		List<ActionLevelDTO> actionLevelDTOs = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("actionlevel");
			actionLevelDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionLevelDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing Access Policies from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAccessPolicies: " + e).log();
			return null;
		}
		
		return actionLevelDTOs;
	}
	
	@Override
	public List<FeaturesViewDTO> downloadFeaturesList(String filter) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_ALL_FEATURES_VIEW_GET;
       
		String featureResponse = null;
		List<FeaturesViewDTO> featuresViewDTOs = null;
		
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		requestParameters.put(ODataQueryConstants.ORDER_BY,"name");
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("get_all_features_view");
			featuresViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeaturesViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing features from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at downloadFeaturesList: " + e).log();
			return null;
		}
		
		return featuresViewDTOs;
	}

	@Override
	public FeatureDTO getFeatureName(String actionId, String legalEntityId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_FEATUREACTION_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "id eq '" + actionId + "'";
		if(StringUtils.isNotBlank(legalEntityId)) {
            filter = filter + " and " + "companyLegalUnit eq '" + legalEntityId + "'";
        }
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		FeatureDTO feature = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("featureaction");
			feature = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), FeatureDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getFeatureName: " + e).log();
			return null;
		}
		
		return feature;
	}
	
	@Override
	public FeatureDTO getFeatureDetails(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_FEATURE_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "id eq '" + featureId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		FeatureDTO feature = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("feature");
			feature = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), FeatureDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature details: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getFeatureDetails: " + e).log();
			return null;
		}
		
		return feature;
	}

	@Override
	public List<ActionsDTO> getActionDetails(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_FEATUREACTION_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Feature_id eq '" + featureId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		List<ActionsDTO> actions = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("featureaction");
			actions = JSONUtils.parseAsList(jsonArray.toString(), ActionsDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing action details: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getActionDetails: " + e).log();
			return null;
		}
		
		return actions;
	}

	@Override
	public JSONObject getServiceFee(String featureId, Map<String, Object> headerMap) {
		
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATURE_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "id eq " + featureId ;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		requestParameters.put(ODataQueryConstants.SELECT, "id, name, description, Type_id, Status_id, Service_Fee");

		String response="";
		JSONObject resp =new JSONObject(); 
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(response);
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("feature")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
				if ((readFeatureJSONArray != null) && (readFeatureJSONArray.length() > 0)) {
					JSONObject featureJSONObject = readFeatureJSONArray.getJSONObject(0);
					resp.put("featureId", featureId);
					String serviceFee = featureJSONObject.optString("Service_Fee");
					if (StringUtils.isNotBlank(serviceFee)) {
						serviceFee = String.format("%100.2f", Double.valueOf(serviceFee)).trim();
					}else {
						serviceFee ="0.00";
					}
					resp.put("Service_Fee", serviceFee);
				}else {
					
					resp.put("dbpErrCode","21375" );
					resp.put("dbpErrMsg","Invalid featureId");
				}
			} else {
				resp.put("dbpErrCode","21352" );
				resp.put("dbpErrMsg","Failed to fetch feature");
				
			}
			
		} catch (Exception e) {
			
			resp.put("dbpErrCode","21352" );
			resp.put("dbpErrMsg","Failed to fetch feature");
		}
		resp.put("opstatus", 0);
		return resp;
	}
	
	@Override
	public List<ActionsDTO> getActionsLimits(String actionId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Action_id eq '" + actionId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		List<ActionsDTO> actions = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("actionlimit");
			actions = JSONUtils.parseAsList(jsonArray.toString(), ActionsDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing action details: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getActionDetails: " + e).log();
			return null;
		}
		
		return actions;
	}
	
	@Override
	public boolean updateLimits(ActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_LIMITS_UPDATE_PROC;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("_action",actionsDTO.getId());
			requestParameters.put("_maxTxLimit",actionsDTO.getMaxTxLimit());
			requestParameters.put("_dailyLimit",actionsDTO.getDailyLimit());
			requestParameters.put("_weeklyLimit",actionsDTO.getWeeklyLimit());
			requestParameters.put("_companyLegalUnit",actionsDTO.getCompanyLegalUnit());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return false;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action limits " + e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updateLimits method: " + e).log();
			return false;
		}
	}
	
	@Override
	public List<FeatureActionsViewDTO> fetchAccountLevelActions(String isAccountLevel) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GET_FEATUREACTIONS_VIEW;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "isAccountLevel eq " + isAccountLevel ;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
       
		String featureResponse = null;
		List<FeatureActionsViewDTO> featureActionsViewDTOs = null;
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("get_feature_actions_view");
			featureActionsViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeatureActionsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing monetary actions from the table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchFeatureActions: " + e).log();
			return null;
		}
		
		return featureActionsViewDTOs;
	}
}
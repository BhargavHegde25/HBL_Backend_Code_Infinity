package com.kony.adminconsole.service.usermanagement.businessdelegate.impl;

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
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.UserFeatureBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsViewDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesViewDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class UserFeatureBusinessDelegateImpl implements UserFeatureBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
			
	@Override
	public List<InternalUserFeaturesViewDTO> getInternalUserFeatures(String featureId) {
		
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALUSERS_FEATURES_VIEW_GET;
       
		String featureResponse = null;
		List<InternalUserFeaturesViewDTO> featuresViewDTOs = null;
		Map<String, Object> requestParameter = new HashMap<>();
		if(StringUtils.isNotBlank(featureId)) {
			requestParameter.put(ODataQueryConstants.FILTER, "id eq '" + featureId + "'");
		}
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameter).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("internalusers_features_view");
			featuresViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), InternalUserFeaturesViewDTO.class);
		}
		catch (JSONException exp) {
			alert.prepareError("Failed to fetch existing internaluser's features from the table: " + exp).log();
			return null;
		}
		catch (Exception exp) {
			alert.prepareError("Caught exception at getInternalUserFeatures: " + exp).log();
			return null;
		}
		
		return featuresViewDTOs;
		
	}
	
	@Override
	public List<InternalUserActionsViewDTO> getInternalUserFeatureActions(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALUSERS_ACTIONS_VIEW;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(featureId)) {
        	
        	String filter = "featureId eq '" + featureId + "'";
    		requestParameters.put(ODataQueryConstants.FILTER, filter);
        }
		
       
		String featureResponse = null;
		List<InternalUserActionsViewDTO> internalUserActionsViewDTOs = null;
		
		try {
			featureResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(featureResponse);
			JSONArray jsonArray = responseObj.optJSONArray("internalusers_actions_view");
			internalUserActionsViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), InternalUserActionsViewDTO.class);
		}
		catch (JSONException exp) {
			alert.prepareError("Failed to fetch existing internal user's actions from the table: " + exp).log();
			return null;
		}
		catch (Exception exp) {
			alert.prepareError("Caught exception at getInternalUserFeatureActions: " + exp).log();
			return null;
		}
		
		return internalUserActionsViewDTOs;
	}
	
	@Override
	public InternalUserFeaturesDTO editInternalUserFeatureDetails(InternalUserFeaturesDTO featureDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_INTERNAL_FEATURE_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(featureDTO).toString(), String.class, Object.class);
			requestParameters.put("id",featureDTO.getFeatureId());
			requestParameters.put("Status_id",featureDTO.getStatusId());
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
			JSONArray responseArray = responseObj.getJSONArray("internalfeature");
			featureDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), InternalUserFeaturesDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit feature " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editInternalUserFeatureDetails method: " + e).log();
			return null;
		}

		return featureDTO;
	}
	
	@Override
	public InternalUserFeaturesDTO editInternalUserFeatureDisplayNameDetails(InternalUserFeaturesDTO featureDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_INTERNAL_FEATUREDISPLAYDESCRIPTION_UPDATE;

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
			JSONArray responseArray = responseObj.getJSONArray("internalfeaturedisplaynamedescription");
			featureDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), InternalUserFeaturesDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit feature display name " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editInternalUserFeatureDisplayNameDetails method: " + e).log();
			return null;
		}

		return featureDTO;
	}
	
	@Override
	public InternalUserFeaturesDTO getInternalFeatureDetails(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALFEATURE_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "id eq '" + featureId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		InternalUserFeaturesDTO feature = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("internalfeature");
			feature = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), InternalUserFeaturesDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature details: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getInternalFeatureDetails: " + e).log();
			return null;
		}
		
		return feature;
	}
	
	@Override
	public InternalUserActionsDTO editInternalUserActionDisplayName(InternalUserActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_INTERNAL_ACTIONDISPLAYDESCRIPTION_UPDATE;

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
			JSONArray responseArray = responseObj.getJSONArray("internalactiondisplaynamedescription");
			actionsDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), InternalUserActionsDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action display name " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editInternalUserActionDisplayName method: " + e).log();
			return null;
		}

		return actionsDTO;
	}
	
	@Override
	public List<ActionDependencyDTO> fetchInternalActionDependencies(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALACTIONDEPENDENCY_VIEW_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "featureId eq '" + featureId + "'";
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
			JSONArray jsonArray = responseObj.optJSONArray("internalactiondependency_view");
			actionDependenciesDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionDependencyDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing action dependencies: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchInternalActionDependencies: " + e).log();
			return null;
		}
		
		return actionDependenciesDTOs;
	}
	
	@Override
	public InternalUserActionsDTO editInternalUserActionDetails(InternalUserActionsDTO actionsDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_INTERNALFEATUREACTION_UPDATE;

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
			JSONArray responseArray = responseObj.getJSONArray("internalfeatureaction");
			actionsDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), InternalUserActionsDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit action " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editInternalUserActionDetails method: " + e).log();
			return null;
		}

		return actionsDTO;
	}
	
	@Override
	public List<InternalUserActionsDTO> getInternalUserActionDetails(String featureId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALFEATUREACTION_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Feature_id eq '" + featureId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		List<InternalUserActionsDTO> actions = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("internalfeatureaction");
			actions = JSONUtils.parseAsList(jsonArray.toString(), InternalUserActionsDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing action details: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getInternalUserActionDetails: " + e).log();
			return null;
		}
		
		return actions;
	}
	
	@Override
	public InternalUserFeaturesDTO getInternalUserFeatureName(String actionId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_INTERNALFEATUREACTION_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "id eq '" + actionId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		InternalUserFeaturesDTO feature = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("internalfeatureaction");
			feature = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), InternalUserFeaturesDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch existing feature: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getInternalUserFeatureName: " + e).log();
			return null;
		}
		
		return feature;
	}

}

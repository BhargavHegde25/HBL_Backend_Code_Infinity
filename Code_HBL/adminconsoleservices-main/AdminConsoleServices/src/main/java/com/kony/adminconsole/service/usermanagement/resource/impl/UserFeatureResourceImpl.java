package com.kony.adminconsole.service.usermanagement.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.UserFeatureBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.dto.DependentActionsDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsViewDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesViewDTO;
import com.kony.adminconsole.service.usermanagement.resource.api.UserFeatureResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UserFeatureResourceImpl implements UserFeatureResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	UserFeatureBusinessDelegate userFeatureBusinessBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(UserFeatureBusinessDelegate.class);
	
	@Override
	public Result getInternalUserFeatures(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		String featureId = StringUtils.EMPTY;
		
		if(StringUtils.isNotBlank(request.getParameter("featureId"))) {
			featureId = request.getParameter("featureId");
		}
		try {
			List<InternalUserFeaturesViewDTO> features = userFeatureBusinessBusinessDelegate.getInternalUserFeatures(featureId);
			if(features == null) {
				ErrorCodeEnum.ERR_22154.setErrorCode(result);
				return result;
			}
			result = convertFeatures(features);
		}
		catch(Exception exp) {
			ErrorCodeEnum.ERR_22154.setErrorCode(result);
			alert.prepareError("Exception occured in getInternalUserFeatures Resource service. Error: ", exp).log();
		}
		
		return result;
	}

	@Override
	public Result getAllInternalFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		try {
			List<InternalUserActionsViewDTO> useractions = userFeatureBusinessBusinessDelegate.getInternalUserFeatureActions(null);
			if(useractions == null) {
				ErrorCodeEnum.ERR_22155.setErrorCode(result);
				return result;
			}
			result = convertInternalUserFeatureActions(useractions);

		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_22155.setErrorCode(result);
			alert.prepareError("Exception occured in getInternalUserFeatureActions Resource service. Error: ", e).log();
		}
		
		return result;
	}
	
	@Override
	public Result getInternalUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		
		String featureId = request.getParameter("featureId");
		if (StringUtils.isBlank(featureId)) {
			
			return ErrorCodeEnum.ERR_21375.setErrorCode(new Result());
		}

		Result featureDetails = null;
		try {
			
			featureDetails = getInternalUserFeatures(methodId, inputArray, request,response);
			
		} catch(Exception e) {
			alert.prepareError("Exception occured in getInternalUserFeatureActions Resource while fetching feature details", e).log();
		}
		try {
			List<InternalUserActionsViewDTO> useractions = userFeatureBusinessBusinessDelegate.getInternalUserFeatureActions(featureId);
			if(useractions == null) {
				ErrorCodeEnum.ERR_22155.setErrorCode(result);
				return result;
			}
			result = convertActions(useractions);
			if(null !=featureDetails ) {
				Record featureRecord = featureDetails.getDatasetById("features") !=null? 
						featureDetails.getDatasetById("features").getRecord(0) : null;
				if(null !=featureRecord ) {
					
					result.addRecord(featureRecord.getRecordById("featureDisplayName"));
					result.addAllParams(featureRecord.getAllParams());
				}
			}
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_22155.setErrorCode(result);
			alert.prepareError("Exception occured in getInternalUserFeatureActions Resource service. Error: ", e).log();
		}
		
		return result;
	}
	
	@Override
	public Result updateInternalUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		
		UserDetailsBean loggedInUserDetails;
		try {
			loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
			inputParams.put("modifiedby", loggedInUserDetails.getUserName());
		} catch (ApplicationException e1) {
			//e1.printStackTrace();
		}
		
		InternalUserFeaturesDTO featureDTOs = validateInput(request);
		if(featureDTOs.getErrCode() != null) {
			return featureDTOs.getErrCode().setErrorCode(new Result());
		}

		String featureId = request.getParameter("featureId");
		String statusId = request.getParameter("statusId");
		
		inputParams.put("statusId",statusId);
		inputParams.put("featureId",featureId);
		inputParams.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		
		InternalUserFeaturesDTO featureDTO = new InternalUserFeaturesDTO();
		try {
			featureDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternalUserFeaturesDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_22157.setErrorCode(new Result());
		}
		
		featureDTO = userFeatureBusinessBusinessDelegate.editInternalUserFeatureDetails(featureDTO);
		if(featureDTO == null) {
			alert.prepareError("Unable to edit the feature").log();
			return ErrorCodeEnum.ERR_22157.setErrorCode(new Result());
		}
		
		JSONArray featureDisplay = new JSONArray();
		try {
			featureDisplay = new JSONArray(request.getParameter("featureDisplay"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21930.setErrorCode(new Result());
		}
		
		if(featureDisplay.length() > 0) {
			if(!updateFeatureDisplayName(featureDTO,featureDisplay)) {
				alert.prepareError("Error while editing feature display name and description ").log();
				return ErrorCodeEnum.ERR_21931.setErrorCode(new Result());
			}
		}
		
		JSONArray actions = new JSONArray();
		try {
			actions = new JSONArray(request.getParameter("actions"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21932.setErrorCode(new Result());
		}
		
		InternalUserActionsDTO actionsDTO = new InternalUserActionsDTO();
		try {
			actionsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternalUserActionsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		
		if (actions.length() > 0) {
			if (!editActionDetails(actionsDTO, actions)) {
				alert.prepareError("Caught exception while converting input params to DTO: ").log();
				return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
			}
			if (!editActionDependency(actionsDTO, actions, featureId,statusId)) {
				alert.prepareError("Caught exception while converting input params to DTO: ").log(); 
				return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
			}
		}
		
		Map<String,String> actionStatusMap = getActionStatus(featureId);
		Set<String> actionSet = new HashSet<>();
		if(StringUtils.isNotBlank(statusId) && statusId.equals("SID_FEATURE_INACTIVE")) {
			for(Map.Entry<String, String> entry : actionStatusMap.entrySet()) {
				
				if(entry.getValue().equals("SID_ACTION_ACTIVE")) {
					actionSet.add(entry.getKey());
				}
			}
			
			actionsDTO = new InternalUserActionsDTO();
			String actionStatus = "SID_ACTION_INACTIVE";
			editActionStatus( actionsDTO,actionSet, actionStatus);
			
			for(Map.Entry<String, String> entry : actionStatusMap.entrySet()) {
				
				if(actionSet.contains(entry.getKey())) {
					actionStatusMap.put(entry.getKey(), "SID_ACTION_INACTIVE");
				}
			}
			
		}
		result = JSONToResult.convert(new JSONObject(featureDTO).toString());
		result.addParam(new Param("status", "Success", FabricConstants.STRING));
		Dataset actionStatus = getActionDataSet(actionStatusMap, actions);
		result.addDataset(actionStatus);
		
		return result;
	}

	@Override
	public Result updateInternalUserActionStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		
		UserDetailsBean loggedInUserDetails;
		try {
			loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
			inputParams.put("modifiedby", loggedInUserDetails.getUserName());
		} catch (ApplicationException e1) {
			//e1.printStackTrace();
		}
		InternalUserActionsDTO action = new InternalUserActionsDTO();
		String actionId = request.getParameter("actionId");
		if (StringUtils.isBlank(actionId)) {
			alert.prepareError("Action Id is mandatory input").log();
			return ErrorCodeEnum.ERR_21988.setErrorCode(new Result());
		}
		inputParams.put("id",actionId);
		
		String status = request.getParameter("status");
		if ( StringUtils.isBlank(status) ||(!status.equalsIgnoreCase("SID_ACTION_ACTIVE") && !status.equalsIgnoreCase("SID_ACTION_INACTIVE"))) {
			alert.prepareError("status should be SID_ACTION_ACTIVE or SID_ACTION_INACTIVE").log();
			return ErrorCodeEnum.ERR_21989.setErrorCode(new Result());
		}
		inputParams.put("status",status);
		
		try {
			action = JSONUtils.parse(new JSONObject(inputParams).toString(), InternalUserActionsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		
		InternalUserFeaturesDTO feature = new InternalUserFeaturesDTO();
		feature = userFeatureBusinessBusinessDelegate.getInternalUserFeatureName(actionId);
		if(feature == null) {
			alert.prepareError("Error occurred while fetching feature details ").log();
			return ErrorCodeEnum.ERR_21937.setErrorCode(new Result());
		}
		
		String featureId = feature.getFeatureId();
		List<ActionDependencyDTO> actionDependencies = userFeatureBusinessBusinessDelegate
				.fetchInternalActionDependencies(featureId);
		if (actionDependencies == null) {
			alert.prepareError("Error occurred while fetching action dependencies").log();
			return ErrorCodeEnum.ERR_21998.setErrorCode(new Result());
		}
		
		if(status.equals("SID_ACTION_INACTIVE")) {
			Map<String, List<String>> inActivateActionsMap = getInActivateAction(actionDependencies);
			List<String> inactivateActions = new ArrayList<>();
			if(inActivateActionsMap.containsKey(actionId)) {
				inactivateActions = inActivateActionsMap.get(actionId);
			}
			inactivateActions.add(actionId);
			Set<String> inactivateSet = new HashSet<>(inactivateActions);
	        if(!editActionStatus(action,inactivateSet,"SID_ACTION_INACTIVE")) {
	        	alert.prepareError("Error occurred while editing action").log();
				return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
	        }
		}
		else if(status.equals("SID_ACTION_ACTIVE")) {
			if(!checkFeatureStatus(featureId)) {
				alert.prepareError("Feature is inactive").log();
				return ErrorCodeEnum.ERR_22021.setErrorCode(new Result());
			}else {
				Map<String,Map<String,String>> activateMap = getActivateAction(actionDependencies);
				if (activateMap.containsKey(actionId)) {
					Map<String, String> activateActions = activateMap.get(actionId);
					if (activateActions.containsValue("SID_ACTION_INACTIVE")) {
						alert.prepareError(
								"Cannot Activate the action, because the Dependant actions are in Inactive state. Please review the dependencies.").log();
						return ErrorCodeEnum.ERR_22022.setErrorCode(new Result());
					}else {
						action = userFeatureBusinessBusinessDelegate.editInternalUserActionDetails(action);
						if (action == null) {
							alert.prepareError("Error occurred while editing action").log();
							return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
						}
					}
				} else {
					action = userFeatureBusinessBusinessDelegate.editInternalUserActionDetails(action);
					if (action == null) {
						alert.prepareError("Error occurred while editing action").log();
						return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
					}
				}
			}
			
		}
		result.addParam(new Param("status", "Success", FabricConstants.STRING));
		return result;
	}
	
	private boolean checkFeatureStatus(String featureId) {
		InternalUserFeaturesDTO feature = new InternalUserFeaturesDTO();
		feature = userFeatureBusinessBusinessDelegate.getInternalFeatureDetails(featureId);
        if(feature == null) {
			return false;
		}
        String featureStatus = feature.getStatusId();
        if("SID_FEATURE_ACTIVE".equals(featureStatus)) {
        	return true;
        }
        return false;
	}
	
	private Result convertFeatures(List<InternalUserFeaturesViewDTO> groupActions) {
		Result result = new Result();
		Map<String,InternalUserFeaturesViewDTO> features = new LinkedHashMap<>();
		try {
		groupActions.forEach((featureObject) -> {
			InternalUserFeaturesViewDTO feature = featureObject;
          
			if (features.containsKey(feature.getId())) {
				InternalUserFeaturesViewDTO existingFeature = features.get(feature.getId());
				if (StringUtils.isNotBlank(feature.getDisplayName())
						&& StringUtils.isNotBlank(feature.getDisplayDescription()) && StringUtils.isNotBlank(feature.getLanguageId())) {
					existingFeature.insertFeatureDisplayName(feature.getLanguageId(), feature.getDisplayName(),feature.getDisplayDescription());
				}

			} else {

				if (StringUtils.isNotBlank(feature.getDisplayName())
						&& StringUtils.isNotBlank(feature.getDisplayDescription()) && StringUtils.isNotBlank(feature.getLanguageId())) {
					feature.insertFeatureDisplayName(feature.getLanguageId(), feature.getDisplayName(),feature.getDisplayDescription());
				}
				features.put(feature.getId(), feature);
			}
				
		});
		
		Dataset featuresDataset = new Dataset("features");
			for (Map.Entry<String, InternalUserFeaturesViewDTO> a : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", a.getKey()));
				InternalUserFeaturesViewDTO featuresViewDTO = a.getValue();
				
				feature.addParam(new Param("name", featuresViewDTO.getName()));
				feature.addParam(new Param("description", featuresViewDTO.getDescription()));
				feature.addParam(new Param("Status_id", featuresViewDTO.getStatusId()));
				feature.addParam(new Param("displaySequence", featuresViewDTO.getDisplaySequence()));
				feature.addParam(new Param("isPrimary", featuresViewDTO.getIsPrimary()));
				feature.addParam(new Param("numberOfActions", featuresViewDTO.getNumberOfActions()));
				
				Record featureDisplayName = new Record();
				featureDisplayName.setId("featureDisplayName");
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getFeatureDisplayName().entrySet()) {
					Record displayName = new Record();
					displayName.setId(l.getKey());
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
					displayName.addParam(new Param("displayName", m.getKey()));
					displayName.addParam(new Param("displayDescription", m.getValue()));
					}
					featureDisplayName.addRecord(displayName);
				}
				feature.addRecord(featureDisplayName);
				featuresDataset.addRecord(feature);
			}
			

		result.addDataset(featuresDataset);
        }
        catch(Exception exp) {
        	ErrorCodeEnum.ERR_21938.setErrorCode(result);
			alert.prepareError("Exception occured in convertFeatures JAVA service. Error: ", exp).log();
        }
		return result;
	}
	
	private Result convertActions(List<InternalUserActionsViewDTO> actions) {
		Result result = new Result();
		Map<String,InternalUserActionsViewDTO> featureActions = new HashMap<>();
		try {
			actions.forEach((actionObject) -> {
			InternalUserActionsViewDTO action = actionObject;
          
			if (featureActions.containsKey(action.getActionId())) {
				InternalUserActionsViewDTO existingAction = featureActions.get(action.getActionId());

				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLocaleId())) {
					existingAction.insertActionDisplayName(action.getLocaleId(), action.getDisplayName(),action.getDisplayDescription());
				}

				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					existingAction.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),
							action.getDependentFeatureName(), action.getDependentFeatureId());
				}

			} else {

				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLocaleId())) {
						action.insertActionDisplayName(action.getLocaleId(), action.getDisplayName(),action.getDisplayDescription());
				}

				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),
							action.getDependentFeatureName(), action.getDependentFeatureId());
				}
				featureActions.put(action.getActionId(), action);
			}
				
		});
		
		Dataset actionsDataSet = new Dataset("actions");
			for (Map.Entry<String, InternalUserActionsViewDTO> a : featureActions.entrySet()) {
				Record action = new Record();
				action.addParam(new Param("actionId", a.getKey()));
				InternalUserActionsViewDTO featureActionsViewDTO = a.getValue();

				action.addParam(new Param("actionName", featureActionsViewDTO.getActionName()));
				action.addParam(new Param("description", featureActionsViewDTO.getActionDescription()));
				action.addParam(new Param("status", featureActionsViewDTO.getActionStatus()));
				action.addParam(new Param("accesspolicyId", featureActionsViewDTO.getAccesspolicyId()));
				
				Record actionDisplayName = new Record();
				actionDisplayName.setId("actionDisplayName");
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getActionDisplayName().entrySet()) {
					Record displayName = new Record();
					displayName.setId(l.getKey());
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
					displayName.addParam(new Param("displayName", m.getKey()));
					displayName.addParam(new Param("displayDescription", m.getValue()));
					}
					actionDisplayName.addRecord(displayName);
				}
				action.addRecord(actionDisplayName);
				Dataset dependentActions = new Dataset("dependentActions");
				
				for(DependentActionsDTO dependentction : a.getValue().getDependentFeatureAndActions()) {
					
					Record dependentAction = new Record();
					dependentAction.addParam(new Param("dependentactionId", dependentction.getDependentactionId()));
					dependentAction.addParam(new Param("dependentActionName",dependentction.getDependentActionName()));
					dependentAction.addParam(new Param("dependentFeatureId",dependentction.getDependentFeatureId()));
					dependentAction.addParam(new Param("dependentFeatureName",dependentction.getDependentFeatureName()));
					dependentActions.addRecord(dependentAction);
				}
				action.addDataset(dependentActions);
				actionsDataSet.addRecord(action);
			}
			

		result.addDataset(actionsDataSet);
        }
        catch(Exception exp) {
        	ErrorCodeEnum.ERR_21944.setErrorCode(result);
			alert.prepareError("Exception occured in convertActions JAVA service. Error: ", exp).log();
        }
		return result;
	}
	
	private InternalUserFeaturesDTO validateInput(DataControllerRequest request) {
		InternalUserFeaturesDTO featureDTO = new InternalUserFeaturesDTO();
		String featureId = request.getParameter("featureId");
		if (StringUtils.isBlank(featureId)) {
			alert.prepareError("Id cannot be null ").log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21928);
			return featureDTO;
		}
		
		String statusId = request.getParameter("statusId");
		if ( StringUtils.isBlank(statusId) ||(!statusId.equalsIgnoreCase("SID_FEATURE_ACTIVE") && !statusId.equalsIgnoreCase("SID_FEATURE_INACTIVE"))) {
			alert.prepareError("status should be SID_FEATURE_ACTIVE or SID_FEATURE_INACTIVE").log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21915);
			return featureDTO;
		}
		
		JSONArray featureDisplay = new JSONArray();
		try {
			featureDisplay = new JSONArray(request.getParameter("featureDisplay"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21930);
			return featureDTO;
		}
		for(int index = 0;index < featureDisplay.length();index++) {
			JSONObject featureDisplayObj = featureDisplay.optJSONObject(index);
			String localeId = featureDisplayObj.optString("localeId");
			String displayName = featureDisplayObj.optString("displayName");
			String displayDescription = featureDisplayObj.optString("displayDescription");

			if (StringUtils.isBlank(localeId)) {
				alert.prepareError("Locale id cannot be empty").log();
				featureDTO.setErrCode(ErrorCodeEnum.ERR_21985); 
				return featureDTO;
			}

			if (StringUtils.isBlank(displayName) || containSpecialChars(displayName)) {
				alert.prepareError("Feature display name cannot be empty").log();
				featureDTO.setErrCode(ErrorCodeEnum.ERR_21986);
				return featureDTO;
			}
			
			if (StringUtils.isBlank(displayDescription) || containSpecialChars(displayDescription)) {
				alert.prepareError("Feature display description cannot be empty").log();
				featureDTO.setErrCode(ErrorCodeEnum.ERR_21987);
				return featureDTO;
			}
		}
		
		JSONArray actions = new JSONArray();
		try {
			actions = new JSONArray(request.getParameter("actions"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21932); 
			return featureDTO;
		}
		
		if(actions != null && actions.length() > 0) {
			for(int index = 0;index < actions.length();index++) {
				JSONObject actionObject = actions.optJSONObject(index);
				String actionId = actionObject.optString("actionId");
				if (StringUtils.isBlank(actionId)) {
					alert.prepareError("Action Id is mandatory input").log();
					featureDTO.setErrCode(ErrorCodeEnum.ERR_21988); 
					return featureDTO;
				}

				String actionStatus = actionObject.optString("statusId");
				if ( StringUtils.isBlank(actionStatus) ||(!actionStatus.equalsIgnoreCase("SID_ACTION_ACTIVE") && !actionStatus.equalsIgnoreCase("SID_ACTION_INACTIVE"))) {
					alert.prepareError("status should be SID_ACTION_ACTIVE or SID_ACTION_INACTIVE").log();
					featureDTO.setErrCode(ErrorCodeEnum.ERR_21989); 
					return featureDTO;
				}
				
				JSONArray actionDisplay = actionObject.getJSONArray("actionDisplay");
			if(actionDisplay != null && actionDisplay.length() >0) {
				for(int actionIndex = 0;actionIndex < actionDisplay.length();actionIndex++) {
					JSONObject actionDisplayObj = actionDisplay.optJSONObject(actionIndex);
					String localeId = actionDisplayObj.optString("localeId");
					String displayName = actionDisplayObj.optString("displayName");
					String displayDescription = actionDisplayObj.optString("displayDescription");

					if (StringUtils.isBlank(localeId)) {
						alert.prepareError("Locale id cannot be empty").log();
						featureDTO.setErrCode(ErrorCodeEnum.ERR_21985); 
						return featureDTO;
					}

					if (StringUtils.isBlank(displayName) || containSpecialChars(displayName)) {
						alert.prepareError("Action display name cannot be empty").log();
						featureDTO.setErrCode(ErrorCodeEnum.ERR_21986); 
						return featureDTO;
					}
					
					if (StringUtils.isBlank(displayDescription) || containSpecialChars(displayDescription)) {
						alert.prepareError("Action display description cannot be empty").log();
						featureDTO.setErrCode(ErrorCodeEnum.ERR_21987); 
						return featureDTO;
					}
				}
				
			}
			}
		}
		return featureDTO;
	}
	
	private boolean updateFeatureDisplayName(InternalUserFeaturesDTO featureDTO, JSONArray featureDisplay) {
		
		if(featureDisplay != null && featureDisplay.length() > 0) {
			String id = featureDTO.getId();
			for(int index = 0;index < featureDisplay.length();index++) {
				JSONObject featureDisplayObj = featureDisplay.optJSONObject(index);
				String localeId = featureDisplayObj.optString("localeId");
				String displayName = featureDisplayObj.optString("displayName");
				String displayDescription = featureDisplayObj.optString("displayDescription");
				featureDTO.setId(id);
				featureDTO.setLocaleId(localeId);
				featureDTO.setDisplayName(displayName);
				featureDTO.setDisplayDescription(displayDescription);
				featureDTO = userFeatureBusinessBusinessDelegate.editInternalUserFeatureDisplayNameDetails(featureDTO);
				if(featureDTO == null) {
					alert.prepareError("Error while editing display details").log();
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean editActionDetails(InternalUserActionsDTO actionsDTO, JSONArray actions) {
		if(actions != null && actions.length() > 0) {
			for(int index = 0;index < actions.length();index++) {
				JSONObject actionObject = actions.optJSONObject(index);
				String actionId = actionObject.optString("actionId");
				if (StringUtils.isBlank(actionId)) {
					alert.prepareError("Action Id is mandatory input").log();
					return false;
				}
				actionsDTO.setId(actionId);
				
				JSONArray actionDisplay = actionObject.getJSONArray("actionDisplay");
				if(!updateActionDisplayName(actionsDTO, actionDisplay)) {
					alert.prepareError("Error while updating the action details").log();
					return false;
				}
				
			
			}
		}
		return true;
	}
	
	private boolean updateActionDisplayName(InternalUserActionsDTO actionsDTO, JSONArray actionDisplay) {
		
		if(actionDisplay != null && actionDisplay.length() > 0) {
			String id = actionsDTO.getId();
			for(int index = 0;index < actionDisplay.length();index++) {
				JSONObject actionDisplayObj = actionDisplay.optJSONObject(index);
				String localeId = actionDisplayObj.optString("localeId");
				String displayName = actionDisplayObj.optString("displayName");
				String displayDescription = actionDisplayObj.optString("displayDescription");
				actionsDTO.setId(id);
				actionsDTO.setLocaleId(localeId);
				actionsDTO.setDisplayName(displayName);
				actionsDTO.setDisplayDescription(displayDescription);
				actionsDTO = userFeatureBusinessBusinessDelegate.editInternalUserActionDisplayName(actionsDTO);
				if(actionsDTO == null ) {
					alert.prepareError("Error while updating the action details").log();
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean editActionDependency(InternalUserActionsDTO actionsDTO, JSONArray actions, String featureId,String featureStatus) {

		List<ActionDependencyDTO> actionDependencies = userFeatureBusinessBusinessDelegate.fetchInternalActionDependencies(featureId);
		if (actionDependencies == null) {
			return false;
		}

		Map<String, List<String>> inActivateActionsMap = getInActivateAction(actionDependencies);
        Map<String,Map<String,String>> activateMap = getActivateAction(actionDependencies);
        List<String> activateList = new ArrayList<>();
        List<String> inActivateList = new ArrayList<>();
        for(int index=0; index < actions.length();index++) {
        	JSONObject actionObject = actions.optJSONObject(index);
        	String actionId = actionObject.optString("actionId");
        	String actionStatus = actionObject.optString("statusId");
        	if(actionStatus.equals("SID_ACTION_ACTIVE")) {
        		activateList.add(actionId);
        	}else if(actionStatus.equals("SID_ACTION_INACTIVE")) {
        		inActivateList.add(actionId);
        	}
        }
        inActivateList = getInactivateList(inActivateList,inActivateActionsMap);
        Set<String> inactivateSet = new HashSet<>(inActivateList);
        activateList.removeAll(inActivateList);
        activateList = getActivateList(activateList,activateMap);
        Set<String> activateSet = new HashSet<>(activateList);
        if(!editActionStatus(actionsDTO,inactivateSet,"SID_ACTION_INACTIVE")) {
        	return false;
        }
		if (("SID_FEATURE_ACTIVE").equals(featureStatus)) {
			if (!editActionStatus(actionsDTO, activateSet, "SID_ACTION_ACTIVE")) {
				return false;
			}
        }
		return true;
	}
	
	private Map<String,List<String>> getInActivateAction(List<ActionDependencyDTO> actionDependenciesDTOs) {

		Map<String,List<String>> inActivateActionsMap = new HashMap<>();
		for(ActionDependencyDTO action : actionDependenciesDTOs) {
			if(inActivateActionsMap.containsKey(action.getActionName())) {
				List<String> dependent = inActivateActionsMap.get(action.getActionName());
				dependent.add(action.getDependencyAction());
			}else {
				List<String> dependent = new ArrayList<>();
				dependent.add(action.getDependencyAction());
				inActivateActionsMap.put(action.getActionName(),dependent);
			}
		}
		
		return inActivateActionsMap;
	}
	
	private Map<String,Map<String,String>> getActivateAction(List<ActionDependencyDTO> actionDependenciesDTOs){
		Map<String,Map<String,String>> activateActionsMap = new HashMap<>();
		for(ActionDependencyDTO action : actionDependenciesDTOs) {
			if(activateActionsMap.containsKey(action.getDependencyAction())) {
				Map<String,String> dependentStatus = activateActionsMap.get(action.getDependencyAction());
				dependentStatus.put(action.getActionName(),action.getActionStatus());
			}else {
				Map<String,String> dependentStatus = new HashMap<>();
				dependentStatus.put(action.getActionName(),action.getActionStatus());
				activateActionsMap.put(action.getDependencyAction(),dependentStatus);
			}
		}
		return activateActionsMap;
	}
	
	private List<String> getInactivateList(List<String> inActivateList,Map<String,List<String>> inActivateActionsMap){
	
		List<String> inactivate = inActivateList;
		try {
			for(String action : inActivateList) {
				if(inActivateActionsMap.containsKey(action)) {
					inactivate.addAll(inActivateActionsMap.get(action));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Exception",e).log();
		}
		return inactivate;
	}
	
	private List<String> getActivateList(List<String> activateList,Map<String,Map<String,String>> activateMap){
		List<String> activate = activateList;
		try {
			for(String action : activateList) {
				if(activateMap.containsKey(action)) {
					Map<String,String> statusMap = activateMap.get(action);
					for (Map.Entry<String, String> actionStatus : statusMap.entrySet()) {
						if (!activate.contains(actionStatus.getKey())
								&& actionStatus.getValue().equals("SID_ACTION_INACTIVE")) {
							activate.remove(action);
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Exception",e).log();
		}
		return activate;
	}
	
	private boolean editActionStatus(InternalUserActionsDTO actionsDTO,Set<String> actions,String status) {
		
		for (String action : actions) {
			actionsDTO.setId(action);
			actionsDTO.setStatusId(status);
			actionsDTO = userFeatureBusinessBusinessDelegate.editInternalUserActionDetails(actionsDTO);
			if (actionsDTO == null) {
				alert.prepareError("Error while updating the action details").log();
				return false;
			}

		}
		return true;
	}
	
	private Map<String,String> getActionStatus(String featureId){
		Map<String,String> actionStatusMap = new HashMap<String, String>();
		List<InternalUserActionsDTO> actions = userFeatureBusinessBusinessDelegate.getInternalUserActionDetails(featureId);
		if(actions == null) {
			return null;
		}
		for(InternalUserActionsDTO action : actions) {
			actionStatusMap.put(action.getId(),action.getStatusId());
		}
		return actionStatusMap;
	}
	
	private Dataset getActionDataSet(Map<String,String> actionStatusMap,JSONArray actions) {
		Dataset actionStatus = new Dataset("actionStatus");
		for (int index = 0; index < actions.length(); index++) {
			JSONObject actionObject = actions.optJSONObject(index);
			String actStatus = actionObject.optString("statusId");
			String actionId = actionObject.optString("actionId");
			Record update = new Record();
			update.addParam(new Param("id", actionId));
			if (actionStatusMap.get(actionId).equals(actStatus)) {
				update.addParam(new Param("updatedStatus", "success"));
			} else {
				update.addParam(new Param("updatedStatus", "failure"));
			}
			actionStatus.addRecord(update);
		}
		return actionStatus;
	}

	private boolean containSpecialChars(String name) {
		Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>^*%!-]");
		if (regex.matcher(name).find()) {
		    return true;
		} 
		return false;
	}
	
	private Result convertInternalUserFeatureActions(List<InternalUserActionsViewDTO> groupActions) {
        Result result = new Result();
		
		Map<String, Map<String, InternalUserActionsViewDTO>> features = new HashMap<>();
        try {
		groupActions.forEach((actionObject) -> {
			InternalUserActionsViewDTO action = new InternalUserActionsViewDTO();
			try {
				action = actionObject;
			} catch (Exception e) {
				
				alert.prepareError("Error occured while fetching the input params: " , e).log();
			}
			if (features.containsKey(action.getFeatureId())) {
				Map<String, InternalUserActionsViewDTO> actionsMap = features.get(action.getFeatureId());
				if (actionsMap.containsKey(action.getActionId())) {
					InternalUserActionsViewDTO existingAction = actionsMap.get(action.getActionId());

					if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
						existingAction.insertDependentActions(action.getDependentactionId(), action.getDependentActionName()
								,action.getDependentFeatureName(), action.getFeatureId());
					}

				} else {

					if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
						action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName()
								,action.getDependentFeatureName(), action.getFeatureId());
					}
					actionsMap.put(action.getActionId(), action);
				}
			} else {
				Map<String, InternalUserActionsViewDTO> actionsMap = new HashMap<>();

				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName()
							,action.getDependentFeatureName(), action.getFeatureId());
				}
				actionsMap.put(action.getActionId(), action);
				features.put(action.getFeatureId(), actionsMap);
			}
           
		});

		Dataset featuresDataset = new Dataset("features");

		for (Map.Entry<String, Map<String, InternalUserActionsViewDTO>> f : features.entrySet()) {
			Record feature = new Record();
			feature.addParam(new Param("id", f.getKey()));
			Dataset actions = new Dataset("actions");

			for (Map.Entry<String, InternalUserActionsViewDTO> a : f.getValue().entrySet()) {
				InternalUserActionsViewDTO featureActionsViewDTO = a.getValue();

				if (feature.getParam("name") == null) {
					feature.addParam(new Param("name", featureActionsViewDTO.getFeatureName()));
					feature.addParam(new Param("description", featureActionsViewDTO.getFeatureDescription()));
					feature.addParam(new Param("status", featureActionsViewDTO.getFeatureStatus()));
//					feature.addParam(new Param("type", featureActionsViewDTO.getFeatureType()));
//					feature.addParam(
//							new Param("displaySequence", featureActionsViewDTO.getFeatureDisplaySequence()));
//					feature.addParam(new Param("isPrimary",featureActionsViewDTO.getIsFeaturePrimary()));

				}
				Record action = new Record();
				action.addParam(new Param("id", a.getKey()));
				action.addParam(new Param("name", featureActionsViewDTO.getActionName()));
				action.addParam(new Param("description", featureActionsViewDTO.getActionName()));
//				action.addParam(new Param("type", featureActionsViewDTO.getActionType()));
				action.addParam(new Param("status", featureActionsViewDTO.getActionStatus()));
//				action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitGroup()));
//				action.addParam(new Param("accessPolicy", featureActionsViewDTO.getAccessPolicy()));
//				action.addParam(new Param("actionlevel", featureActionsViewDTO.getActionlevel()));
//				if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroupId())) {
//					action.addParam(new Param("limitgroupId", "N/A"));
//				}else {
//					action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitGroupId()));
//				}
//				action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccessPolicyId()));
//				action.addParam(new Param("actionlevelId", featureActionsViewDTO.getActionlevelId()));
//				Dataset limits = new Dataset("limits");
//				for (Map.Entry<String, String> l : a.getValue().getActionLimits().entrySet()) {
//					Record limit = new Record();
//					limit.addParam(new Param("id", l.getKey()));
//					limit.addParam(new Param("value", l.getValue()));
//					limits.addRecord(limit);
//				}
//				if (limits.getAllRecords().size() > 0) {
//					action.addDataset(limits);
//				}
//				Dataset dependentActions = new Dataset("dependentActions");
//				for (Map.Entry<String, Map<String,String>> l : a.getValue().getDependentFeatureAndActions().entrySet()) {
//					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
//						Record dependentAction = new Record();
//						dependentAction.addParam(new Param("id", m.getKey()));
//						dependentAction.addParam(new Param("name",m.getValue()));
//						dependentAction.addParam(new Param("featureName",l.getKey()));
//						dependentActions.addRecord(dependentAction);
//					}
//					
//				}
//				action.addDataset(dependentActions);
//				actions.addRecord(action);
				
				Dataset dependentActions = new Dataset("dependentActions");
				
				for(DependentActionsDTO dependentction : a.getValue().getDependentFeatureAndActions()) {
					
					Record dependentAction = new Record();
					dependentAction.addParam(new Param("dependentactionId", dependentction.getDependentactionId()));
					dependentAction.addParam(new Param("dependentActionName",dependentction.getDependentActionName()));
					dependentAction.addParam(new Param("dependentFeatureId",dependentction.getDependentFeatureId()));
					dependentAction.addParam(new Param("dependentFeatureName",dependentction.getDependentFeatureName()));
					dependentActions.addRecord(dependentAction);
				}
				action.addDataset(dependentActions);
				actions.addRecord(action);
			}
			feature.addDataset(actions);
			featuresDataset.addRecord(feature);
		}

		result.addDataset(featuresDataset);
        }
        catch(Exception e) {
        	ErrorCodeEnum.ERR_21940.setErrorCode(result);
			alert.prepareError("Exception occured in convertMonetaryActions JAVA service. Error: ", e).log();
        }
		return result;
	}
}

package com.kony.adminconsole.service.featuresandactions.resource.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
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
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.api.FeaturesAndActionsBusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.dto.AccessPolicyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionLevelDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureActionsViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureNameSortComparator;
import com.kony.adminconsole.service.featuresandactions.dto.FeaturesViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.LimitGroupDTO;
import com.kony.adminconsole.service.featuresandactions.resource.api.FeaturesAndActionsResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FeaturesAndActionsResourceImpl implements FeaturesAndActionsResource{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	FeaturesAndActionsBusinessDelegate featuresAndActionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(FeaturesAndActionsBusinessDelegate.class);

	@Override
	public Result fetchAllLimitGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		try {
			List<LimitGroupDTO> limitGroupArray = featuresAndActionsBusinessDelegate.fetchAllLimitGroups();
			if(limitGroupArray == null) {
				ErrorCodeEnum.ERR_21945.setErrorCode(result);
				return result;
			}
			result = convertLimitGroups(limitGroupArray);
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllLimitGroups JAVA service. Error: ", e).log();
		}
		
		return result;
	}

	@Override
	public Result fetchAllFeatures(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {
			String legalEntityId = request.getParameter("legalEntityId");			
			List<FeaturesViewDTO> groupActions = featuresAndActionsBusinessDelegate.fetchAllFeatures(legalEntityId);
			if(groupActions == null) {
				ErrorCodeEnum.ERR_21937.setErrorCode(result);
				return result;
			}
			result = convertFeatureActionsLimits(groupActions);
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllFeatures JAVA service. Error: ", e).log();
		}
		
		return result;
	}

	/**
	 * Method to convert List of features to Result containing features details in hierarchical form
	 * @param groupActions - list of FeaturesViewDTO
	 * @return result
	 */
	private Result convertFeatureActionsLimits(List<FeaturesViewDTO> groupActions) {
		Result result = new Result();
		Map<String,FeaturesViewDTO> features = new HashMap<>();
		try {
		groupActions.forEach((actionObject) -> {
			FeaturesViewDTO action = actionObject;
          
			if (features.containsKey(action.getId())) {
				FeaturesViewDTO existingFeature = features.get(action.getId());
				if (StringUtils.isNotBlank(action.getRoleTypeId())
						&& StringUtils.isNotBlank(action.getRoleTypeName())) {
					existingFeature.insertRoleType(action.getRoleTypeId(), action.getRoleTypeName());
				}
				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLanguageId())) {
					existingFeature.insertFeatureDisplayName(action.getLanguageId(), action.getDisplayName(),action.getDisplayDescription());
				}

			} else {
				if (StringUtils.isNotBlank(action.getRoleTypeId()) && StringUtils.isNotBlank(action.getRoleTypeName())) {
					action.insertRoleType(action.getRoleTypeId(), action.getRoleTypeName());
				}
				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLanguageId())) {
						action.insertFeatureDisplayName(action.getLanguageId(), action.getDisplayName(),action.getDisplayDescription());
				}
				features.put(action.getId(), action);
			}
				
		});
		
		Dataset featuresDataset = new Dataset("features");
			for (Map.Entry<String, FeaturesViewDTO> a : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", a.getKey()));
				FeaturesViewDTO featuresViewDTO = a.getValue();

				
					feature.addParam(new Param("name", featuresViewDTO.getName()));
					feature.addParam(new Param("description", featuresViewDTO.getDescription()));
					feature.addParam(new Param("Status_id", featuresViewDTO.getStatusId()));
					feature.addParam(new Param("legalEntityId", featuresViewDTO.getCompanyLegalUnit()));
					feature.addParam(new Param("Type_id", featuresViewDTO.getTypeId()));
					if(StringUtils.isBlank(featuresViewDTO.getServiceFee())) {
						feature.addParam(new Param("Service_Fee","0.00"));
					}else {
					feature.addParam(new Param("Service_Fee",featuresViewDTO.getServiceFee()));
					}
					feature.addParam(new Param("monetaryActions",featuresViewDTO.getMonetaryActions()));
					feature.addParam(new Param("nonMonetaryActions",featuresViewDTO.getNonMonetaryActions()));
				Dataset roleTypes = new Dataset("roleTypes");
				for (Map.Entry<String, String> l : a.getValue().getRoleTypes().entrySet()) {
					Record roleType = new Record();
					roleType.addParam(new Param("id", l.getKey()));
					roleType.addParam(new Param("name", l.getValue()));
					roleTypes.addRecord(roleType);
				}
				if (roleTypes.getAllRecords().size() > 0) {
					feature.addDataset(roleTypes);
				}
				
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
        catch(Exception e) {
        	ErrorCodeEnum.ERR_21938.setErrorCode(result);
			alert.prepareError("Exception occured in convertFeatureActionsLimits JAVA service. Error: ", e).log();
        }
		return result;
	}

	@Override
	public Result editLimitGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		LimitGroupDTO limitGroupDTO = new LimitGroupDTO();

		String id = request.getParameter("id");
		if (StringUtils.isBlank(id)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_21935.setErrorCode(new Result());
		}
		limitGroupDTO.setId(id);
		
		JSONArray limitGroupDisplay = new JSONArray();
		try {
			limitGroupDisplay = new JSONArray(request.getParameter("limitGroupDisplay"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		
		if(limitGroupDisplay.length() > 0) {
		if (!editLimitGroupDisplayName(limitGroupDisplay, limitGroupDTO)) {
			alert.prepareError("Failed to edit limit group: ").log();
			return ErrorCodeEnum.ERR_21936.setErrorCode(new Result());
		}
		}
		result = JSONToResult.convert(new JSONObject(limitGroupDTO).toString());
		return result;
	}

	@Override
	public Result editFeatureAndActions(String methodID, Object[] inputArray, DataControllerRequest request,
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
		
		FeatureDTO featureDTOs = validateInput(request);
		if(featureDTOs.getErrCode() != null) {
			return featureDTOs.getErrCode().setErrorCode(new Result());
		}
		String serviceFee = request.getParameter("serviceFee");
		String featureId = request.getParameter("featureId");
		String statusId = request.getParameter("statusId");
		String legalEntityId = request.getParameter("legalEntityId");
		
		inputParams.put("serviceFee",serviceFee);
		inputParams.put("statusId",statusId);
		inputParams.put("featureId",featureId);
		inputParams.put("companyLegalUnit", legalEntityId);
		inputParams.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		
		FeatureDTO featureDTO = new FeatureDTO();
		try {
			featureDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FeatureDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		
		featureDTO = featuresAndActionsBusinessDelegate.editFeatureDetails(featureDTO);
		if(featureDTO == null) {
			alert.prepareError("Unable to edit the feature").log();
			return ErrorCodeEnum.ERR_21927.setErrorCode(new Result());
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
		
		ActionsDTO actionsDTO = new ActionsDTO();
		try {
			actionsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ActionsDTO.class);
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
		result = JSONToResult.convert(new JSONObject(featureDTO).toString());
		result.addParam(new Param("status", "Success", FabricConstants.STRING));
		Dataset actionStatus = getActionDataSet(actionStatusMap, actions);
		result.addDataset(actionStatus);
		return result;
	}

	/**
	 * Method to update the display name and description of the feature
	 * @param featureDTO contains the details of the feature
	 * @param featureDisplay contains the details like name.
	 * @return true on a successful edit
	 */
	private boolean updateFeatureDisplayName(FeatureDTO featureDTO, JSONArray featureDisplay) {
		
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
				featureDTO = featuresAndActionsBusinessDelegate.editFeatureDisplayNameDetails(featureDTO);
				if(featureDTO == null) {
					alert.prepareError("Error while editing display details").log();
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Updates the action status.
	 * @param actionsDTO contains the action details
	 * @param actions JSONArray of the details of the action
	 * @return true on successful update
	 */
	private boolean editActionDetails(ActionsDTO actionsDTO, JSONArray actions) {
		if(actions != null && actions.length() > 0) {
			for(int index = 0;index < actions.length();index++) {
				JSONObject actionObject = actions.optJSONObject(index);
				String actionId = actionObject.optString("actionId");
				if (StringUtils.isBlank(actionId)) {
					alert.prepareError("Action Id is mandatory input").log();
					return false;
				}
				actionsDTO.setId(actionId);

				JSONArray limits = actionObject.getJSONArray("limits");
				if(limits != null && limits.length() > 0) {
					if(!validateActionLimits(limits, actionsDTO)) {
						alert.prepareError("Error while updating the action details").log();
						return false;
					}
				}
				
				JSONArray actionDisplay = actionObject.getJSONArray("actionDisplay");
				if(!updateActionDisplayName(actionsDTO, actionDisplay)) {
					alert.prepareError("Error while updating the action details").log();
					return false;
				}
				
			
			}
		}
		return true;
	}
	
	/**
	 * Validates and updates the action limits
	 * @param limits - contains the limit of the action
	 * @param actionsDTO - contains the action details
	 * @return true on successful update of the action limits
	 */
	private boolean validateActionLimits(JSONArray limits, ActionsDTO actionsDTO) {
		ActionsDTO limitsDTO = getActionLimits(actionsDTO.getId());
		boolean flag = false;
		if(limits != null && limits.length() > 0) {
			Double minTxVal = 0.0, maxTxVal = 0.0, dailyTxVal = 0.0, weeklyTxVal = 0.0;
			for(int index = 0;index < limits.length(); index++) {
				JSONObject limitObj = limits.optJSONObject(index);
				String type = limitObj.optString("type");
				String value = limitObj.optString("value");

				if (StringUtils.equals(type, "MIN_TRANSACTION_LIMIT")) {
					minTxVal = Double.parseDouble(value);
					actionsDTO.setMinTxLimit(minTxVal);
				} else if (StringUtils.equals(type, "MAX_TRANSACTION_LIMIT")) {
					maxTxVal = Double.parseDouble(value);
					actionsDTO.setMaxTxLimit(maxTxVal);
					if (limitsDTO.getMaxTxLimit() > maxTxVal 
							&& limitsDTO.getMaxTxLimit() <= limitsDTO.getDailyLimit()
							&& limitsDTO.getDailyLimit() <= limitsDTO.getWeeklyLimit()) {
                          flag = true;
					}
				} else if (StringUtils.equals(type, "DAILY_LIMIT")) {
					dailyTxVal = Double.parseDouble(value);
					actionsDTO.setDailyLimit(dailyTxVal);
					if (limitsDTO.getDailyLimit() > dailyTxVal 
							&& limitsDTO.getMaxTxLimit() <= limitsDTO.getDailyLimit()
							&& limitsDTO.getDailyLimit() <= limitsDTO.getWeeklyLimit()) {
                        flag = true;
					}
				} else if (StringUtils.equals(type, "WEEKLY_LIMIT")) {
					weeklyTxVal = Double.parseDouble(value);
					actionsDTO.setWeeklyLimit(weeklyTxVal);
					if (limitsDTO.getWeeklyLimit() > weeklyTxVal 
							&& limitsDTO.getMaxTxLimit() <= limitsDTO.getDailyLimit()
							&& limitsDTO.getDailyLimit() <= limitsDTO.getWeeklyLimit()) {  
                        flag = true;
					}
				}
				
			}
			if (0 < minTxVal && minTxVal <= maxTxVal && maxTxVal <= dailyTxVal && dailyTxVal <= weeklyTxVal) {
				//return true;
			}else {
				alert.prepareError("Error while updating the action limits").log();
				return false;
			}
			if(!featuresAndActionsBusinessDelegate.editActionLimits(actionsDTO)) {
				alert.prepareError("Error while updating the action limits").log();
				return false;
			}
			
			if(flag) {
				if(!actionLimitUpdates(actionsDTO)) {
					alert.prepareError("Error while updating the action limits").log();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Updates the action display name and description.
	 * @param actionsDTO
	 * @param actionDisplay
	 * @return true on successful update
	 */
    private boolean updateActionDisplayName(ActionsDTO actionsDTO, JSONArray actionDisplay) {
		
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
				actionsDTO = featuresAndActionsBusinessDelegate.editActionDisplayName(actionsDTO);
				if(actionsDTO == null ) {
					alert.prepareError("Error while updating the action details").log();
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public Result fetchFeatureActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		String featureId = request.getParameter("featureId");
		String legalEntityId = request.getParameter("legalEntityId");
		if (StringUtils.isBlank(featureId)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_21928.setErrorCode(new Result());
		}

		try {
			List<FeatureActionsViewDTO> groupActions = featuresAndActionsBusinessDelegate.fetchFeatureActions(featureId, legalEntityId);
			if(groupActions == null) {
				ErrorCodeEnum.ERR_21943.setErrorCode(result);
				return result;
			}
			result = convertActionsLimits(groupActions);
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllFeatures JAVA service. Error: ", e).log();
		}
		
		return result;
	}
	
	/**
	 * Method to convert List of Actions to Result containing actions,limits details in hierarchical form
	 * @param groupActions - List of FeatureActionsViewDTO
	 * @return Result
	 */
	private Result convertActionsLimits(List<FeatureActionsViewDTO> groupActions) {
		Result result = new Result();
		Map<String,FeatureActionsViewDTO> featureActions = new HashMap<>();
		try {
		groupActions.forEach((actionObject) -> {
			FeatureActionsViewDTO action = actionObject;
          
			if (featureActions.containsKey(action.getActionId())) {
				FeatureActionsViewDTO existingAction = featureActions.get(action.getActionId());
				if (StringUtils.isNotBlank(action.getActionType())
						&& StringUtils.isNotBlank(action.getRoleTypeName())) {
					existingAction.insertRoleType(action.getActionType(), action.getRoleTypeName());
				}
				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLocaleId())) {
					existingAction.insertActionDisplayName(action.getLocaleId(), action.getDisplayName(),action.getDisplayDescription());
				}
				if (StringUtils.isNotBlank(action.getTermsAndConditionTitle())
						&& StringUtils.isNotBlank(action.getTermsAndConditionDescription())) {
					existingAction.insertTermsAndConditions(action.getTermsAndConditionTitle(), action.getTermsAndConditionTitle());
				}
				if (StringUtils.isNotBlank(action.getLimitTypeId())
						&& StringUtils.isNotBlank(action.getValue())) {
					existingAction.insertLimit(action.getLimitTypeId(), action.getValue());
				}
				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					existingAction.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
				}

			} else {
				if (StringUtils.isNotBlank(action.getActionType()) && StringUtils.isNotBlank(action.getRoleTypeName())) {
					action.insertRoleType(action.getActionType(), action.getRoleTypeName());
				}
				if (StringUtils.isNotBlank(action.getDisplayName())
						&& StringUtils.isNotBlank(action.getDisplayDescription()) && StringUtils.isNotBlank(action.getLocaleId())) {
						action.insertActionDisplayName(action.getLocaleId(), action.getDisplayName(),action.getDisplayDescription());
				}
				if (StringUtils.isNotBlank(action.getTermsAndConditionTitle())
						&& StringUtils.isNotBlank(action.getTermsAndConditionDescription())) {
					action.insertTermsAndConditions(action.getTermsAndConditionTitle(), action.getTermsAndConditionTitle());
				}
				if (StringUtils.isNotBlank(action.getLimitTypeId())
						&& StringUtils.isNotBlank(action.getValue())) {
					action.insertLimit(action.getLimitTypeId(), action.getValue());
				}
				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
				}
				featureActions.put(action.getActionId(), action);
			}
				
		});
		
		Dataset actionsDataSet = new Dataset("actions");
			for (Map.Entry<String, FeatureActionsViewDTO> a : featureActions.entrySet()) {
				Record action = new Record();
				action.addParam(new Param("actionId", a.getKey()));
				FeatureActionsViewDTO featureActionsViewDTO = a.getValue();

				action.addParam(new Param("actionName", featureActionsViewDTO.getActionName()));
				action.addParam(new Param("description", featureActionsViewDTO.getActionDescription()));
				action.addParam(new Param("status", featureActionsViewDTO.getActionStatus()));
				action.addParam(new Param("Type_id", featureActionsViewDTO.getTypeId()));
				action.addParam(new Param("isMFAApplicable", featureActionsViewDTO.getIsMFAApplicable()));
				action.addParam(new Param("legalEntityId", featureActionsViewDTO.getCompanyLegalUnit()));
				action.addParam(new Param("accesspolicy", featureActionsViewDTO.getAccessPolicy()));
				if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroup())) {
					action.addParam(new Param("limitgroup", "N/A"));
				}else {
					action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitGroup()));
				}
				action.addParam(new Param("actionlevel", featureActionsViewDTO.getActionlevel()));
				if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroupId())) {
					action.addParam(new Param("limitgroupId", "N/A"));
				}else {
					action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitGroupId()));
				}
				action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccessPolicyId()));
				action.addParam(new Param("actionlevelId", featureActionsViewDTO.getActionlevelId()));
				Dataset roleTypes = new Dataset("roleTypes");
				for (Map.Entry<String, String> l : a.getValue().getRoleTypes().entrySet()) {
					Record roleType = new Record();
					roleType.addParam(new Param("id", l.getKey()));
					roleType.addParam(new Param("name", l.getValue()));
					roleTypes.addRecord(roleType);
				}
				if (roleTypes.getAllRecords().size() > 0) {
					action.addDataset(roleTypes);
				}

				Record termandcondition = new Record();
				termandcondition.setId("termandcondition");
				for (Map.Entry<String, String> l : a.getValue().getTermsAndConditions().entrySet()) {
					Record termsAndCondition = new Record();
					termsAndCondition.addParam(new Param("id", l.getKey()));
					termsAndCondition.addParam(new Param("value", l.getValue()));
					termandcondition.addRecord(termsAndCondition);
				}
				action.addRecord(termandcondition);
				Dataset limits = new Dataset("limits");
				for (Map.Entry<String, String> l : a.getValue().getActionLimits().entrySet()) {
					Record actionLimit = new Record();
					actionLimit.addParam(new Param("type", l.getKey()));
					actionLimit.addParam(new Param("value", l.getValue()));
					limits.addRecord(actionLimit);
				}
				action.addDataset(limits);
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
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getDependentFeatureAndActions().entrySet()) {
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
						Record dependentAction = new Record();
						dependentAction.addParam(new Param("id", m.getKey()));
						dependentAction.addParam(new Param("name",m.getValue()));
						dependentAction.addParam(new Param("featureName",l.getKey()));
						dependentActions.addRecord(dependentAction);
					}
					
				}
				action.addDataset(dependentActions);
				actionsDataSet.addRecord(action);
			}
			

		result.addDataset(actionsDataSet);
        }
        catch(Exception e) {
        	ErrorCodeEnum.ERR_21944.setErrorCode(result);
			alert.prepareError("Exception occured in convertActionsLimits JAVA service. Error: ", e).log();
        }
		return result;
	}
	
	/**
	 * Method to convert List of limit groups to Result containing limitgroup details in hierarchical form
	 * @param limitGroupsList
	 * @return Result
	 */
	private Result convertLimitGroups(List<LimitGroupDTO> limitGroupsList) {
		Result result =new Result();
		Map<String,LimitGroupDTO> limitGroups = new HashMap<>();
		try {
			limitGroupsList.forEach((limitObject) -> {
				LimitGroupDTO limitGroup = limitObject;
          
			if (limitGroups.containsKey(limitGroup.getId())) {
				LimitGroupDTO existingLimit = limitGroups.get(limitGroup.getId());
				if ((StringUtils.isNotBlank(limitGroup.getDisplayName()) || containSpecialChars(limitGroup.getDisplayName()))
						&& (StringUtils.isNotBlank(limitGroup.getDisplayDescription()) || containSpecialChars(limitGroup.getDisplayDescription())) && StringUtils.isNotBlank(limitGroup.getLocaleId())) {
					existingLimit.insertLimitDisplayName(limitGroup.getLocaleId(), limitGroup.getDisplayName(),limitGroup.getDisplayDescription());
				}

			} else {
				if ((StringUtils.isNotBlank(limitGroup.getDisplayName()) || containSpecialChars(limitGroup.getDisplayName()))
						&& (StringUtils.isNotBlank(limitGroup.getDisplayDescription()) || containSpecialChars(limitGroup.getDisplayDescription())) && StringUtils.isNotBlank(limitGroup.getLocaleId())) {
					limitGroup.insertLimitDisplayName(limitGroup.getLocaleId(), limitGroup.getDisplayName(),limitGroup.getDisplayDescription());
				}
				limitGroups.put(limitGroup.getId(), limitGroup);
			}
				
		});
		
		Dataset limitGroupDataSet = new Dataset("limitGroups");
			for (Map.Entry<String, LimitGroupDTO> a : limitGroups.entrySet()) {
				Record limitGroup = new Record();
				limitGroup.addParam(new Param("id", a.getKey()));
				LimitGroupDTO limitGroupDTO = a.getValue();

				
				limitGroup.addParam(new Param("name", limitGroupDTO.getName()));
				limitGroup.addParam(new Param("description", limitGroupDTO.getDescription()));
				Record limitGroupDisplayName = new Record();
				limitGroupDisplayName.setId("limitGroupDisplayName");
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getLimitGroupDisplayName().entrySet()) {
					Record displayName = new Record();
					displayName.setId(l.getKey());
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
					displayName.addParam(new Param("displayName", m.getKey()));
					displayName.addParam(new Param("displayDescription", m.getValue()));
					}
					limitGroupDisplayName.addRecord(displayName);
				}
				limitGroup.addRecord(limitGroupDisplayName);
				limitGroupDataSet.addRecord(limitGroup);
			}
			

		result.addDataset(limitGroupDataSet);
        }
        catch(Exception e) {
        	ErrorCodeEnum.ERR_21946.setErrorCode(result);
			alert.prepareError("Exception occured in convertLimitGroups JAVA service. Error: ", e).log();
        }
		return result;
	}
	
	/**
	 * Method to edit the limit group display name and display description
	 * @param limitgroups
	 * @param limitgroupDTO
	 * @return true on successful update.
	 */
	private boolean editLimitGroupDisplayName(JSONArray limitgroups, LimitGroupDTO limitgroupDTO) {
		if(limitgroups != null && limitgroups.length() > 0) {
			String id = limitgroupDTO.getId();
			for(int index = 0;index < limitgroups.length();index++) {
				JSONObject actionDisplayObj = limitgroups.optJSONObject(index);
				String localeId = actionDisplayObj.optString("localeId");
				String displayName = actionDisplayObj.optString("displayName");
				String displayDescription = actionDisplayObj.optString("displayDescription");

				if (StringUtils.isBlank(localeId)) {
					alert.prepareError("Locale id cannot be empty").log();
					return false;
				}

				if (StringUtils.isBlank(displayName)) {
					alert.prepareError("limit group display name cannot be empty").log();
					return false;
				}
				
				if (StringUtils.isBlank(displayDescription)) {
					alert.prepareError("limit group display description cannot be empty").log();
					return false;
				}
				limitgroupDTO.setLocaleId(localeId);
				limitgroupDTO.setDisplayName(displayName);
				limitgroupDTO.setDisplayDescription(displayDescription);
				limitgroupDTO.setId(id);
				limitgroupDTO = featuresAndActionsBusinessDelegate.editLimitGroup(limitgroupDTO);
				if(limitgroupDTO == null ) {
					alert.prepareError("Error while editing the display name").log();
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public Result fetchAllMonetaryActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {
			List<FeatureActionsViewDTO> groupActions = featuresAndActionsBusinessDelegate.fetchMonetaryActions("MONETARY");
			if (groupActions == null) {
				ErrorCodeEnum.ERR_21939.setErrorCode(result);
				return result;
			}
			result = convertMonetaryActions(groupActions);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllMonetaryActions JAVA service. Error: ", e).log();
		}

		return result;
	}
	
	@Override
	public Result fetchAccountLevelActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {
			List<FeatureActionsViewDTO> groupActions = featuresAndActionsBusinessDelegate.fetchAccountLevelActions("1");
			if (groupActions == null) {
				ErrorCodeEnum.ERR_22127.setErrorCode(result);
				return result;
			}
			result = convertMonetaryActions(groupActions);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAccountLevelActions JAVA service. Error: ", e).log();
		}

		return result;
	}
	
	/**
	 * Method to convert List of monetary actions to Result containing features,actions and limits details in hierarchical form
	 * @param groupActions
	 * @return Result
	 */
	private Result convertMonetaryActions(List<FeatureActionsViewDTO> groupActions) {
        Result result = new Result();
		
		Map<String, Map<String, FeatureActionsViewDTO>> features = new HashMap<>();
        try {
		groupActions.forEach((actionObject) -> {
			FeatureActionsViewDTO action = new FeatureActionsViewDTO();
			try {
				action = actionObject;
			} catch (Exception e) {
				
				alert.prepareError("Error occured while fetching the input params: " , e).log();
			}
			if (features.containsKey(action.getFeatureId())) {
				Map<String, FeatureActionsViewDTO> actionsMap = features.get(action.getFeatureId());
				if (actionsMap.containsKey(action.getActionId())) {
					FeatureActionsViewDTO existingAction = actionsMap.get(action.getActionId());
					if (StringUtils.isNotBlank(action.getLimitTypeId())
							&& StringUtils.isNotBlank(action.getValue())) {
						existingAction.insertLimit(action.getLimitTypeId(), action.getValue());
					}
					if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
						existingAction.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
					}

				} else {
					if (StringUtils.isNotBlank(action.getLimitTypeId())
							&& StringUtils.isNotBlank(action.getValue())) {
						action.insertLimit(action.getLimitTypeId(), action.getValue());
					}
					if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
						action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
					}
					actionsMap.put(action.getActionId(), action);
				}
			} else {
				Map<String, FeatureActionsViewDTO> actionsMap = new HashMap<>();
				if (StringUtils.isNotBlank(action.getLimitTypeId()) && StringUtils.isNotBlank(action.getValue())) {
					action.insertLimit(action.getLimitTypeId(), action.getValue());
				}
				if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
					action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
				}
				actionsMap.put(action.getActionId(), action);
				features.put(action.getFeatureId(), actionsMap);
			}
           
		});

		Dataset featuresDataset = new Dataset("features");

		for (Map.Entry<String, Map<String, FeatureActionsViewDTO>> f : features.entrySet()) {
			Record feature = new Record();
			feature.addParam(new Param("id", f.getKey()));
			Dataset actions = new Dataset("actions");

			for (Map.Entry<String, FeatureActionsViewDTO> a : f.getValue().entrySet()) {
				FeatureActionsViewDTO featureActionsViewDTO = a.getValue();

				if (feature.getParam("name") == null) {
					feature.addParam(new Param("name", featureActionsViewDTO.getFeatureName()));
					feature.addParam(new Param("description", featureActionsViewDTO.getFeatureDescription()));
					feature.addParam(new Param("status", featureActionsViewDTO.getFeatureStatus()));
					feature.addParam(new Param("type", featureActionsViewDTO.getFeatureType()));
					feature.addParam(
							new Param("displaySequence", featureActionsViewDTO.getFeatureDisplaySequence()));
					feature.addParam(new Param("isPrimary",featureActionsViewDTO.getIsFeaturePrimary()));

				}
				Record action = new Record();
				action.addParam(new Param("id", a.getKey()));
				action.addParam(new Param("name", featureActionsViewDTO.getActionName()));
				action.addParam(new Param("description", featureActionsViewDTO.getActionName()));
				action.addParam(new Param("type", featureActionsViewDTO.getActionType()));
				action.addParam(new Param("status", featureActionsViewDTO.getActionStatus()));
				action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitGroup()));
				action.addParam(new Param("accessPolicy", featureActionsViewDTO.getAccessPolicy()));
				action.addParam(new Param("actionlevel", featureActionsViewDTO.getActionlevel()));
				if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroupId())) {
					action.addParam(new Param("limitgroupId", "N/A"));
				}else {
					action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitGroupId()));
				}
				action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccessPolicyId()));
				action.addParam(new Param("actionlevelId", featureActionsViewDTO.getActionlevelId()));
				Dataset limits = new Dataset("limits");
				for (Map.Entry<String, String> l : a.getValue().getActionLimits().entrySet()) {
					Record limit = new Record();
					limit.addParam(new Param("id", l.getKey()));
					limit.addParam(new Param("value", l.getValue()));
					limits.addRecord(limit);
				}
				if (limits.getAllRecords().size() > 0) {
					action.addDataset(limits);
				}
				Dataset dependentActions = new Dataset("dependentActions");
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getDependentFeatureAndActions().entrySet()) {
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
						Record dependentAction = new Record();
						dependentAction.addParam(new Param("id", m.getKey()));
						dependentAction.addParam(new Param("name",m.getValue()));
						dependentAction.addParam(new Param("featureName",l.getKey()));
						dependentActions.addRecord(dependentAction);
					}
					
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

	@Override
	public Result fetchFeatureActionsByType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		String roleTypeId= "";
		String companyLegalUnit= "";
		if (StringUtils.isBlank(request.getParameter("legalEntityId"))) {
			alert.prepareError("LegalEntity Id cannot be null or empty").log();	
		      ErrorCodeEnum.ERR_22232.setErrorCode(result);
		      return result;
		    } 
        if(!StringUtils.isBlank(request.getParameter("roleTypeId")) && !StringUtils.isBlank(request.getParameter("legalEntityId"))) {
        	roleTypeId = request.getParameter("roleTypeId");
        	companyLegalUnit = request.getParameter("legalEntityId");
		}
		try {
			List<FeatureActionsViewDTO> groupActions = featuresAndActionsBusinessDelegate.fetchFeatureActionsByType(roleTypeId,companyLegalUnit);
			if (groupActions == null) {
				ErrorCodeEnum.ERR_21941.setErrorCode(result);
				return result;
			}
			Map<String,List<FeatureActionsViewDTO>> groupMap = new HashMap<>();
			groupMap = convertToGroupMap(groupActions);
			result = convertFeatureActions(groupMap);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchFeatureActionsByType JAVA service. Error: ", e).log();
		}

		return result;
	}
	
	/**
	 * Converts to a map consisting of groupId and list of featureActionsView
	 * @param featureactions
	 * @return
	 */
	private Map<String,List<FeatureActionsViewDTO>> convertToGroupMap(List<FeatureActionsViewDTO> featureactions) {
		Map<String,List<FeatureActionsViewDTO>> groupMap = new HashMap<>();
		for (FeatureActionsViewDTO feature : featureactions) {
			if (!StringUtils.isBlank(feature.getActionType())) {
				if (groupMap.containsKey(feature.getActionType())) {
					List<FeatureActionsViewDTO> featuresList = groupMap.get(feature.getActionType());
					featuresList.add(feature);
				} else {
					List<FeatureActionsViewDTO> featuresList = new ArrayList<>();
					featuresList.add(feature);
					groupMap.put(feature.getActionType(), featuresList);
				}
			}
		}
		return groupMap;
	}
	
	/**
	 * Method to convert List of features to Result containing features,actions and limits details in hierarchical form
	 * @param groupMap
	 * @return Result
	 */
	private Result convertFeatureActions(Map<String,List<FeatureActionsViewDTO>> groupMap) {
		
		Result result = new Result();
		Dataset groupDataset = new Dataset("groups");
		for (Map.Entry<String, List<FeatureActionsViewDTO>> role : groupMap.entrySet()) {
			String groupId = role.getKey();
			List<FeatureActionsViewDTO> groupActions = role.getValue();
			
			Collections.sort(groupActions, new FeatureNameSortComparator());
			Map<String, Map<String, FeatureActionsViewDTO>> features = new LinkedHashMap<>();
	        try {
			groupActions.forEach((actionObject) -> {
				FeatureActionsViewDTO action = new FeatureActionsViewDTO();
				try {
					action = actionObject;
				} catch (Exception e) {
					
					alert.prepareError("Error occured while fetching the input params: " ,e).log();
				}
				if (features.containsKey(action.getFeatureId())) {
					Map<String, FeatureActionsViewDTO> actionsMap = features.get(action.getFeatureId());
					if (actionsMap.containsKey(action.getActionId())) {
						FeatureActionsViewDTO existingAction = actionsMap.get(action.getActionId());
						if (StringUtils.isNotBlank(action.getLimitTypeId())
								&& StringUtils.isNotBlank(action.getValue())) {
							existingAction.insertLimit(action.getLimitTypeId(), action.getValue());
						}
						if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
							existingAction.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
						}

					} else {
						if (StringUtils.isNotBlank(action.getLimitTypeId())
								&& StringUtils.isNotBlank(action.getValue())) {
							action.insertLimit(action.getLimitTypeId(), action.getValue());
						}
						if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
							action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
						}
						actionsMap.put(action.getActionId(), action);
					}
				} else {
					Map<String, FeatureActionsViewDTO> actionsMap = new HashMap<>();
					if (StringUtils.isNotBlank(action.getLimitTypeId()) && StringUtils.isNotBlank(action.getValue())) {
						action.insertLimit(action.getLimitTypeId(), action.getValue());
					}
					if(StringUtils.isNotBlank(action.getDependentactionId()) && StringUtils.isNotBlank(action.getDependentActionName()) && StringUtils.isNotBlank(action.getDependentFeatureName()) ) {
						action.insertDependentActions(action.getDependentactionId(), action.getDependentActionName(),action.getDependentFeatureName());
					}
					actionsMap.put(action.getActionId(), action);
					features.put(action.getFeatureId(), actionsMap);
				}
	           
			});

			Dataset featuresDataset = new Dataset("features");

			for (Map.Entry<String, Map<String, FeatureActionsViewDTO>> f : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", f.getKey()));
				Dataset actions = new Dataset("actions");

				for (Map.Entry<String, FeatureActionsViewDTO> a : f.getValue().entrySet()) {
					FeatureActionsViewDTO featureActionsViewDTO = a.getValue();

					if (feature.getParam("name") == null) {
						feature.addParam(new Param("name", featureActionsViewDTO.getFeatureName()));
						feature.addParam(new Param("description", featureActionsViewDTO.getFeatureDescription()));
						feature.addParam(new Param("status", featureActionsViewDTO.getFeatureStatus()));
						feature.addParam(new Param("type", featureActionsViewDTO.getFeatureType()));
						feature.addParam(
								new Param("displaySequence", featureActionsViewDTO.getFeatureDisplaySequence()));
						feature.addParam(new Param("isPrimary",featureActionsViewDTO.getIsFeaturePrimary()));
						feature.addParam(new Param("legalEntityId", featureActionsViewDTO.getCompanyLegalUnit()));

					}
					Record action = new Record();
					action.addParam(new Param("id", a.getKey()));
					action.addParam(new Param("name", featureActionsViewDTO.getActionName()));
					action.addParam(new Param("description", featureActionsViewDTO.getActionName()));
					action.addParam(new Param("type", featureActionsViewDTO.getTypeId()));
					if(!StringUtils.isBlank(featureActionsViewDTO.getLimitGroup())) {
						action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitGroup()));
					} else if(!StringUtils.isBlank(featureActionsViewDTO.getLimitgroup())) {
						action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitgroup()));
					} else {
						action.addParam(new Param("limitgroup", "N/A"));
					}
					/*if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroup())) {
						action.addParam(new Param("limitgroup", "N/A"));
					}else {
						action.addParam(new Param("limitgroup", featureActionsViewDTO.getLimitGroup()));
					}*/
					if(!StringUtils.isBlank(featureActionsViewDTO.getAccessPolicy())) {
						action.addParam(new Param("accessPolicy", featureActionsViewDTO.getAccessPolicy()));
					} else if(!StringUtils.isBlank(featureActionsViewDTO.getAccesspolicy())) {
						action.addParam(new Param("accessPolicy", featureActionsViewDTO.getAccesspolicy()));
					}
					// action.addParam(new Param("accessPolicy", featureActionsViewDTO.getAccessPolicy()));
					action.addParam(new Param("actionlevel", featureActionsViewDTO.getActionlevel()));
					if(!StringUtils.isBlank(featureActionsViewDTO.getLimitGroupId())) {
						action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitGroupId()));
					} else if(!StringUtils.isBlank(featureActionsViewDTO.getLimitgroupId())) {
						action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitgroupId()));
					} else {
						action.addParam(new Param("limitgroupId", "N/A"));
					}
					/*if(StringUtils.isBlank(featureActionsViewDTO.getLimitGroupId())) {
						action.addParam(new Param("limitgroupId", "N/A"));
					}else {
						action.addParam(new Param("limitgroupId", featureActionsViewDTO.getLimitGroupId()));
					}*/
					if(!StringUtils.isBlank(featureActionsViewDTO.getAccessPolicyId())) {
						action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccessPolicyId()));
					} else if(!StringUtils.isBlank(featureActionsViewDTO.getAccesspolicyId())) {
						action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccesspolicyId()));
					}
					// action.addParam(new Param("accessPolicyId", featureActionsViewDTO.getAccessPolicyId()));
					action.addParam(new Param("actionlevelId", featureActionsViewDTO.getActionlevelId()));
					action.addParam(new Param("isMFAApplicable", featureActionsViewDTO.getIsMFAApplicable()));
					action.addParam(new Param("status", featureActionsViewDTO.getActionStatus()));
					action.addParam(new Param("isAccountLevel", featureActionsViewDTO.getIsAccountLevel()));
					action.addParam(new Param("isPrimary", featureActionsViewDTO.getIsPrimary()));
					action.addParam(new Param("displaySequence", featureActionsViewDTO.getActionDisplaySequence()));
					if (StringUtils.isNotBlank(featureActionsViewDTO.getActionDependency())) {
						action.addParam(new Param("dependency", featureActionsViewDTO.getActionDependency()));
					}
					Dataset limits = new Dataset("limits");
					for (Map.Entry<String, String> l : a.getValue().getActionLimits().entrySet()) {
						Record limit = new Record();
						limit.addParam(new Param("id", l.getKey()));
						limit.addParam(new Param("value", l.getValue()));
						limits.addRecord(limit);
					}
					if (limits.getAllRecords().size() > 0) {
						action.addDataset(limits);
					}
					Dataset dependentActions = new Dataset("dependentActions");
					for (Map.Entry<String, Map<String,String>> l : a.getValue().getDependentFeatureAndActions().entrySet()) {
						for (Map.Entry<String, String> m : l.getValue().entrySet()) {
							Record dependentAction = new Record();
							dependentAction.addParam(new Param("id", m.getKey()));
							dependentAction.addParam(new Param("name",m.getValue()));
							dependentAction.addParam(new Param("featureName",l.getKey()));
							dependentActions.addRecord(dependentAction);
						}
						
					}
					action.addDataset(dependentActions);
					actions.addRecord(action);
				}
				feature.addDataset(actions);
				featuresDataset.addRecord(feature);
			}

			Record group = new Record();
			group.addParam(new Param("groupid", groupId));
			group.addDataset(featuresDataset);
			groupDataset.addRecord(group);
	        }
	        catch(Exception e) {
	        	ErrorCodeEnum.ERR_21942.setErrorCode(result);
				alert.prepareError("Exception occured in convertFeatureActions JAVA service. Error: ", e).log();
	        }
			
		}
		result.addDataset(groupDataset);
		return result;
	}

	@Override
	public Result fetchAccessPolicies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		List<AccessPolicyDTO> AccessPolicyDTOs = featuresAndActionsBusinessDelegate.fetchAccessPolicies();
		if(AccessPolicyDTOs == null) {
			alert.prepareError("Error occurred while fetching access policy ").log();
			return ErrorCodeEnum.ERR_21954.setErrorCode(new Result());
		}
		
		if(AccessPolicyDTOs.size() == 0){
	        return JSONToResult.convert(new JSONObject().put("AccessPolicyRecords", new JSONArray()).toString());
        }
		
		try {
	        JSONArray records = new JSONArray(AccessPolicyDTOs);
	        JSONObject resultObject = new JSONObject();
	        resultObject.put("AccessPolicyRecords",records);
	        result = JSONToResult.convert(resultObject.toString());
		}
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }
		
		return result;
	}
	
	private FeatureDTO validateInput(DataControllerRequest request) {
		FeatureDTO featureDTO = new FeatureDTO();
		String featureId = request.getParameter("featureId");
		if (StringUtils.isBlank(featureId)) {
			alert.prepareError("Id cannot be null ").log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21928);
			return featureDTO;
		}
		
		String serviceFee = request.getParameter("serviceFee");
		if (StringUtils.isNotBlank(serviceFee)) {
			try {
				Double.parseDouble(serviceFee);
			}
			catch(Exception e) {
				alert.prepareError("serviceFee is not numeric").log();
				featureDTO.setErrCode(ErrorCodeEnum.ERR_21929);
				return featureDTO;
			}
		}
		if (StringUtils.isBlank(serviceFee) || (Double.parseDouble(serviceFee) < 0.0)) {
			alert.prepareError("serviceFee cannot be null or negative").log();
			featureDTO.setErrCode(ErrorCodeEnum.ERR_21929);
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

			if (StringUtils.isBlank(displayName) || containSpecialChars(displayName)
					|| !hasValidNameLength(displayName)) {
				alert.prepareError("Feature display name cannot be empty").log();
				featureDTO.setErrCode(ErrorCodeEnum.ERR_21986);
				return featureDTO;
			}
			
			if (StringUtils.isBlank(displayDescription) || containSpecialChars(displayDescription)
					|| !hasValidDescriptionLength(displayDescription)) {
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
				JSONArray limits = actionObject.getJSONArray("limits");
				if(limits != null && limits.length() > 0) {
					if(!validateLimits(limits)) {
						alert.prepareError("Error while updating the action details").log();
						featureDTO.setErrCode(ErrorCodeEnum.ERR_21990); 
						return featureDTO;
					}
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

						if (StringUtils.isBlank(displayName) || containSpecialChars(displayName)
								|| !hasValidNameLength(displayName)) {
						alert.prepareError("Action display name cannot be empty").log();
						featureDTO.setErrCode(ErrorCodeEnum.ERR_21986); 
						return featureDTO;
					}
					
						if (StringUtils.isBlank(displayDescription) || containSpecialChars(displayDescription)
								|| !hasValidDescriptionLength(displayDescription)) {
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
	
	private boolean validateLimits(JSONArray limits) {
		if(limits != null && limits.length() > 0) {
			Double minTxVal = 0.0, maxTxVal = 0.0, dailyTxVal = 0.0, weeklyTxVal = 0.0;
			for(int index = 0;index < limits.length(); index++) {
				JSONObject limitObj = limits.optJSONObject(index);
				String type = limitObj.optString("type");
				String value = limitObj.optString("value");

				if (StringUtils.isBlank(value)) {
					alert.prepareError("Limit cannot be empty").log();
					return false;
				}

				if (StringUtils.equals(type, "MIN_TRANSACTION_LIMIT")) {
					minTxVal = Double.parseDouble(value);
				} else if (StringUtils.equals(type, "MAX_TRANSACTION_LIMIT")) {
					maxTxVal = Double.parseDouble(value);
				} else if (StringUtils.equals(type, "DAILY_LIMIT")) {
					dailyTxVal = Double.parseDouble(value);
				} else if (StringUtils.equals(type, "WEEKLY_LIMIT")) {
					weeklyTxVal = Double.parseDouble(value);
				}
				
			}
			if (0 < minTxVal && minTxVal <= maxTxVal && maxTxVal <= dailyTxVal && dailyTxVal <= weeklyTxVal) {
				//return true;
			}else {
				alert.prepareError("Error while updating the action limits").log();
				return false;
			}
		}
		return true;
	}

	private boolean editActionDependency(ActionsDTO actionsDTO, JSONArray actions, String featureId,String featureStatus) {

		String legalEntityId = actionsDTO.getCompanyLegalUnit();
		List<ActionDependencyDTO> actionDependencies = featuresAndActionsBusinessDelegate
				.fetchActionDependencies(featureId, legalEntityId);
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
	
		List<String> inactivate = new ArrayList<String>(inActivateList);
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
	
	private boolean editActionStatus(ActionsDTO actionsDTO,Set<String> actions,String status) {
		
		for (String action : actions) {
			actionsDTO.setId(action);
			actionsDTO.setStatusId(status);
			actionsDTO = featuresAndActionsBusinessDelegate.editActionDetails(actionsDTO);
			if (actionsDTO == null) {
				alert.prepareError("Error while updating the action details").log();
				return false;
			}

		}
		return true;
	}

	@Override
	public Result fetchActionLevels(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		List<ActionLevelDTO> actionLevelDTOs = featuresAndActionsBusinessDelegate.fetchActionLevels();
		if(actionLevelDTOs == null) {
			alert.prepareError("Error occurred while fetching action levels ").log();
			return ErrorCodeEnum.ERR_21998.setErrorCode(new Result());
		}
		
		if(actionLevelDTOs.size() == 0){
	        return JSONToResult.convert(new JSONObject().put("ActionLevelRecords", new JSONArray()).toString());
        }
		
		try {
	        JSONArray records = new JSONArray(actionLevelDTOs);
	        JSONObject resultObject = new JSONObject();
	        resultObject.put("ActionLevelRecords",records);
	        result = JSONToResult.convert(resultObject.toString());
		}
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }
		
		return result;
	}

	@Override
	public Result downloadFeaturesList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, String> queryParamsMap = (Map<String, String>) request.getAttribute("queryparams");
			String searchText = queryParamsMap.containsKey("searchText") ? queryParamsMap.get("searchText") : null;
    		String type = queryParamsMap.containsKey("type") ? queryParamsMap.get("type") : null;

			String authToken = CommonUtilities.getAuthToken(request);
			if (StringUtils.isBlank(authToken)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20000);
			}
			StringBuilder filterString = getFilterForFeatures(queryParamsMap);
			List<FeaturesViewDTO> featureDTOs = featuresAndActionsBusinessDelegate.downloadFeaturesList(filterString.toString());

			
			if ( featureDTOs != null) {
				
				HashMap<String,FeaturesViewDTO> featuresMap = new HashMap<String, FeaturesViewDTO>();
				for(FeaturesViewDTO feature:featureDTOs) {
					if(featuresMap.containsKey(feature.getId())) {
						FeaturesViewDTO f = featuresMap.get(feature.getId());
						 if(!StringUtils.isBlank(feature.getRoleTypeId()) && !StringUtils.isBlank(feature.getRoleTypeName())) {
						    	f.insertRoleType(feature.getRoleTypeId(), feature.getRoleTypeName());
						    }
						
					}else {
					    if(!StringUtils.isBlank(feature.getRoleTypeId()) && !StringUtils.isBlank(feature.getRoleTypeName())) {
					    	feature.insertRoleType(feature.getRoleTypeId(), feature.getRoleTypeName());
					    }
						featuresMap.put(feature.getId(),feature);
					}
				}

				List<FeaturesViewDTO> featureViewDTOs = new ArrayList<>(featuresMap.values());
				StringBuilder responseCsvBuilder = new StringBuilder(); 
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name", "Code", "Type", "Status", "Number of monetary actions","Number of Non monetary actions")
						.print(responseCsvBuilder);

				featureViewDTOs.forEach((features) ->{
					String nameColumn = features.getName();
					String codeColumn = features.getId();
					List<String> typesList = new ArrayList<>(features.getRoleTypes().values());
					String typeColumn = String.join(",", typesList);
					String statusColumn = features.getStatusId();
					String monetaryColumn = features.getMonetaryActions();
					String nonMonetaryColumn = features.getNonMonetaryActions();

					if (searchText == null
							|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
						try {
							 if (type == null) {
							responseCsvPrinter.printRecord(nameColumn, codeColumn, typeColumn,
									statusColumn, monetaryColumn,nonMonetaryColumn);
							 }else {
								 String[] types = type.split(",");
		                            for (int j = 0; j < types.length; j++) {
		                                if (typesList.contains(types[j])) {
		                                	responseCsvPrinter.printRecord(nameColumn, codeColumn, typeColumn,
		        									statusColumn, monetaryColumn,nonMonetaryColumn);
		                                }
		                            }
							 }
						} catch (IOException e) {
							alert.prepareError("Failed while downloading groups list", e).log();
							ErrorCodeEnum.ERR_21984.setErrorCode(result);
						}
					}
				});
				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"Features_List.csv\"");

				response.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				response.getHeaders().putAll(customHeaders);
			} else {
				alert.prepareError("Failed to fetch features").log();
				ErrorCodeEnum.ERR_21937.setErrorCode(result);
			}

		} catch (Exception e) {
			alert.prepareError("Failed while downloading features list", e).log();
			ErrorCodeEnum.ERR_20687.setErrorCode(result);
		}
		return result;
	}
	
	private StringBuilder getFilterForFeatures(Map<String,String> queryParamsMap) {
	  String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status") : null;
	  StringBuilder filterString= new StringBuilder();
	  if (status != null) {
          String[] statuses = status.split(",");
          filterString.append("(");
          for (int i = 0; i < statuses.length - 1; ++i) {
              filterString.append("Status_id eq '" + statuses[i] + "'");
              filterString.append(" or ");
          }
          filterString.append("Status_id eq '" + statuses[statuses.length - 1] + "')");
      }
		return filterString;
	}

	@Override
	public Result manageActionStatus(String methodID, Object[] inputArray, DataControllerRequest request,
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
		ActionsDTO action = new ActionsDTO();
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
		
		String legalEntityId = request.getParameter("legalEntityId");
		if (StringUtils.isBlank(legalEntityId)) {
			alert.prepareError("legalEntityId is mandatory input").log();
			return ErrorCodeEnum.ERR_22230.setErrorCode(new Result());	
		}
		inputParams.put("companyLegalUnit",legalEntityId);
		
		try {
			action = JSONUtils.parse(new JSONObject(inputParams).toString(), ActionsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		
		FeatureDTO feature = new FeatureDTO();
		feature = featuresAndActionsBusinessDelegate.getFeatureName(actionId, legalEntityId);
		if(feature == null) {
			alert.prepareError("Error occurred while fetching feature details ").log();
			return ErrorCodeEnum.ERR_21937.setErrorCode(new Result());
		}
		
		String featureId = feature.getFeatureId();
		List<ActionDependencyDTO> actionDependencies = featuresAndActionsBusinessDelegate
				.fetchActionDependencies(featureId,legalEntityId);
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
					activateActions.remove(actionId);
					if (activateActions.containsValue("SID_ACTION_INACTIVE")) {
						alert.prepareError(
								"Cannot Activate the action, because the Dependant actions are in Inactive state. Please review the dependencies.").log();
						return ErrorCodeEnum.ERR_22022.setErrorCode(new Result());
					}else {
						action = featuresAndActionsBusinessDelegate.editActionDetails(action);
						if (action == null) {
							alert.prepareError("Error occurred while editing action").log();
							return ErrorCodeEnum.ERR_21933.setErrorCode(new Result());
						}
					}
				} else {
					action = featuresAndActionsBusinessDelegate.editActionDetails(action);
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
	
	/**
	 * Checks the status of the feature
	 * @param featureId
	 * @return true if feature is active
	 */
	private boolean checkFeatureStatus(String featureId) {
		FeatureDTO feature = new FeatureDTO();
		feature = featuresAndActionsBusinessDelegate.getFeatureDetails(featureId);
        if(feature == null) {
			return false;
		}
        String featureStatus = feature.getStatusId();
        if("SID_FEATURE_ACTIVE".equals(featureStatus)) {
        	return true;
        }
        return false;
	}
	
	/**
	 * Fetches the action and their respective status based on the feature ID 
	 * @param featureId
	 * @return
	 */
	private Map<String,String> getActionStatus(String featureId){
		Map<String,String> actionStatusMap = new HashMap<String, String>();
		List<ActionsDTO> actions = featuresAndActionsBusinessDelegate.getActionDetails(featureId);
		if(actions == null) {
			return null;
		}
		for(ActionsDTO action : actions) {
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
	
	private boolean hasValidNameLength(String name) {
		return name.length() <= 60 && name.length() > 0;
	}
	
	private boolean hasValidDescriptionLength(String name) {
		return name.length() <= 100 && name.length() > 0;
	}
	
	private boolean actionLimitUpdates(ActionsDTO actionsDTO) {
		Callable<Boolean> updateLimits = new Callable<Boolean>() {
			 @Override
			 public Boolean call() throws Exception{
				 return featuresAndActionsBusinessDelegate.updateLimits(actionsDTO);
			 }
		}; try {
            ThreadExecutor.execute(updateLimits);
        } catch (InterruptedException e) {
            alert.prepareError("actionLimitUpdates throw error ", e).log();
            Thread.currentThread().interrupt();
        }
		return true;
	}
	
	private ActionsDTO getActionLimits(String actionId) {
		ActionsDTO limitsDTO = new ActionsDTO();
		List<ActionsDTO> limitsList = featuresAndActionsBusinessDelegate.getActionsLimits(actionId);
		for(ActionsDTO action : limitsList) {
			if(action.getLimitTypeId().equals("MAX_TRANSACTION_LIMIT")) {
				limitsDTO.setMaxTxLimit(Float.parseFloat(action.getValue()));
			}else if(action.getLimitTypeId().equals("DAILY_LIMIT")) {
				limitsDTO.setDailyLimit(Float.parseFloat(action.getValue()));
			}else if(action.getLimitTypeId().equals("WEEKLY_LIMIT")) {
				limitsDTO.setWeeklyLimit(Float.parseFloat(action.getValue()));
			}
		}
		return limitsDTO;
	}

	@Override
	public Result getServiceFee(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String featureId = StringUtils.EMPTY;
		try {
	    	if (StringUtils.isBlank(requestInstance.getParameter("featureId"))) {
	            ErrorCodeEnum.ERR_21350.setErrorCode(result);
	            return result;
	        }
	    	
	    	featureId = requestInstance.getParameter("featureId");
	        
	        JSONObject serviceResponse =
	        		featuresAndActionsBusinessDelegate.getServiceFee(featureId, requestInstance.getHeaderMap());
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21352.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FEATURES, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Search feature by featureId failed : "+featureId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                result.addParam(new Param("dbpErrMsg", serviceResponse.getString("dbpErrMsg"),
                        FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getServiceFee", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21352.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FEATURES, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Search feature by featureId failed: "+featureId);
        }

        return result;
	}
}
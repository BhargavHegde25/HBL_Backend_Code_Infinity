package com.kony.adminconsole.service.featuresandactions;

/**
 * Service to Manage Requests related to Feature
 * 
 * @author Chandan Gupta - KH2516
 *
 */

import com.hbl.adminconsole.getlistcache.GetListCacheInvalidator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.GroupActionLimitView;
import com.kony.adminconsole.handler.FeatureandActionHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FeatureManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String GET_ALL_FEATURES_METHOD_NAME = "getAllFeatures";
	private static final String EDIT_FEATURE_AND_ACTION_LIMITS_METHOD_NAME = "editFeatureAndActionLimits";
	private static final String GET_ACTIONS_METHOD_NAME = "getFeatureActions";
	private static final String GET_ALL_FEATURES_AND_ACTIONS_METHOD_NAME = "getAllFeaturesAndActions";
	private static final String GET_ALL_MONETARY_ACTIONS_METHOD_NAME = "getAllMonetaryActions";

	private static final String MAX_DAILY_LIMIT_PARAM = "DAILY_LIMIT";
	private static final String MAX_TRANSACTION_LIMIT_PARAM = "MAX_TRANSACTION_LIMIT";
	private static final String MIN_TRANSACTION_LIMIT_PARAM = "MIN_TRANSACTION_LIMIT";
	private static final String MAX_WEEKLY_LIMIT_PARAM = "WEEKLY_LIMIT";

	private static final String FEATURE_ACTIVE_STATUS = "SID_FEATURE_ACTIVE";
	private static final String FEATURE_INACTIVE_STATUS = "SID_FEATURE_INACTIVE";
	private static final String FEATURE_DOWN_STATUS = "SID_FEATURE_DOWN";

	private static final String INPUT_FEATURES = "features";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			return invokeOperation(methodID, inputArray, requestInstance, responseInstance);
		} finally {
			// Online-banking getList cache: this operation changes data getList returns. Runs even
			// after a part-way failure, because some rows may already be written.
			GetListCacheInvalidator.permissionsChanged();
		}
	}

	private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALL_FEATURES_METHOD_NAME)) {
				return getAllFeatures(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_ACTIONS_METHOD_NAME)) {
				return getFeatureActions(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_FEATURE_AND_ACTION_LIMITS_METHOD_NAME)) {
				return editFeatureAndActionLimits(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_ALL_FEATURES_AND_ACTIONS_METHOD_NAME)) {
				return getAllFeaturesAndActionsByType(requestInstance);// getAllFeaturesAndActions(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_ALL_MONETARY_ACTIONS_METHOD_NAME)) {
				return getAllMonetaryActions(requestInstance);// getAllFeaturesAndActions(requestInstance);
			}
			return null;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

	public Result getAllFeaturesAndActionsByType(DataControllerRequest requestInstance) {
		Result result = new Result();

		try {
			Map<String, String> roleIdNameMap = new HashMap<String, String>();
			roleIdNameMap = createRoleIdNameMap(requestInstance);
			Dataset groupDataset = new Dataset("groups");
			for (Map.Entry<String, String> role : roleIdNameMap.entrySet()) {
				String groupId = role.getKey();
				JSONArray groupActions = FeatureandActionHandler.getTypeFeatureActions(groupId, requestInstance,
						result);
				Map<String, Map<String, GroupActionLimitView>> features = new HashMap<>();
				for (int i = 0; i < groupActions.length(); i++) {
					GroupActionLimitView action = FeatureandActionHandler
							.getGroupActionObjectFromAction(groupActions.getJSONObject(i));
					updateFeatures(action, features);
				}
				;

				Dataset featuresDataset = new Dataset("features");

				for (Map.Entry<String, Map<String, GroupActionLimitView>> f : features.entrySet()) {
					Record feature = new Record();
					feature.addParam(new Param("id", f.getKey()));
					Dataset actions = new Dataset("actions");

					for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
						GroupActionLimitView groupActionLimitView = a.getValue();

						if (feature.getParam("name") == null) {
							feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
							feature.addParam(new Param("description", groupActionLimitView.getFeature_description()));
							feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
							feature.addParam(new Param("type", groupActionLimitView.getFeature_Type_id()));
							feature.addParam(
									new Param("displaySequence", groupActionLimitView.getFeature_displaysequence()));
							feature.addParam(new Param("isPrimary", groupActionLimitView.getFeature_isPrimary()));
						}
						Record action = new Record();
						action.addParam(new Param("id", a.getKey()));
						action.addParam(new Param("name", groupActionLimitView.getAction_name()));
						action.addParam(new Param("description", groupActionLimitView.getAction_name()));
						action.addParam(new Param("type", groupActionLimitView.getAction_Type_id()));
						action.addParam(new Param("isMFAApplicable", groupActionLimitView.getIsMFAApplicable()));
						action.addParam(new Param("isAccountLevel", groupActionLimitView.getIsAccountLevel()));
						action.addParam(new Param("isPrimary", groupActionLimitView.getIsPrimary()));
						action.addParam(new Param("displaySequence", groupActionLimitView.getAction_displaysequence()));
						if (StringUtils.isNotBlank(groupActionLimitView.getAction_dependency())) {
							action.addParam(new Param("dependency", groupActionLimitView.getAction_dependency()));
						}
						Dataset limits = new Dataset("limits");
						for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
							Record limit = new Record();
							limit.addParam(new Param("id", l.getKey()));
							limit.addParam(new Param("value", l.getValue()));
							limits.addRecord(limit);
						}
						if (limits.getAllRecords().size() > 0) {
							action.addDataset(limits);
						}
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
			result.addDataset(groupDataset);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. Error: ", e).log();
		}

		return result;
	}

	public Result getAllFeaturesAndActions(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			String filterForAllActions = "";
			Map<String, Map<String, GroupActionLimitView>> features = new HashMap<>();
			JSONArray allActions = FeatureandActionHandler.getAllFeatureActions(filterForAllActions, requestInstance,
					result);
			for (int i = 0; i < allActions.length(); i++) {
				GroupActionLimitView action = FeatureandActionHandler
						.getGroupActionObjectFromAction(allActions.getJSONObject(i));
				updateFeatures(action, features);
			}
			;

			Dataset featuresDataset = new Dataset("features");

			for (Map.Entry<String, Map<String, GroupActionLimitView>> f : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", f.getKey()));
				Dataset actions = new Dataset("actions");

				for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
					GroupActionLimitView groupActionLimitView = a.getValue();

					if (feature.getParam("name") == null) {
						feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
						feature.addParam(new Param("description", groupActionLimitView.getFeature_description()));
						feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
						feature.addParam(new Param("type", groupActionLimitView.getFeature_Type_id()));
						feature.addParam(new Param("isPrimary", groupActionLimitView.getFeature_isPrimary()));

					}
					Record action = new Record();
					action.addParam(new Param("id", a.getKey()));
					action.addParam(new Param("name", groupActionLimitView.getAction_name()));
					action.addParam(new Param("description", groupActionLimitView.getAction_name()));
					action.addParam(new Param("type", groupActionLimitView.getAction_Type_id()));

					Dataset limits = new Dataset("limits");
					for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
						Record limit = new Record();
						limit.addParam(new Param("id", l.getKey()));
						limit.addParam(new Param("value", l.getValue()));
						limits.addRecord(limit);
					}
					if (limits.getAllRecords().size() > 0) {
						action.addDataset(limits);
					}
					actions.addRecord(action);
				}
				feature.addDataset(actions);
				featuresDataset.addRecord(feature);
			}

			result.addDataset(featuresDataset);
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Action. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21310.setErrorCode(result);
		}
		return result;
	}

	public Result getAllMonetaryActions(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {
			String featuresStr = requestInstance.getParameter(INPUT_FEATURES);
			if (StringUtils.isBlank(featuresStr)) {
				ErrorCodeEnum.ERR_21029.setErrorCode(result);
				return result;
			}

			JSONArray featuresList = CommonUtilities.getStringAsJSONArray(featuresStr);
			if (featuresList.length() == 0) {
				ErrorCodeEnum.ERR_21029.setErrorCode(result);
				return result;
			}
			Map<String, Map<String, GroupActionLimitView>> features = new HashMap<>();
			JSONArray actionDetails = FeatureandActionHandler.getMonetaryActions(requestInstance, result, featuresList);
			for (int i = 0; i < actionDetails.length(); i++) {
				GroupActionLimitView action = FeatureandActionHandler
						.getGroupActionObjectFromAction(actionDetails.getJSONObject(i));
				updateFeatures(action, features);
			}
			;

			Dataset featuresDataset = new Dataset("features");

			for (Map.Entry<String, Map<String, GroupActionLimitView>> f : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", f.getKey()));
				Dataset actions = new Dataset("actions");

				for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
					GroupActionLimitView groupActionLimitView = a.getValue();

					if (feature.getParam("name") == null) {
						feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
						feature.addParam(new Param("description", groupActionLimitView.getFeature_description()));
						feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
						feature.addParam(new Param("type", groupActionLimitView.getFeature_Type_id()));
					}
					Record action = new Record();
					action.addParam(new Param("id", a.getKey()));
					action.addParam(new Param("name", groupActionLimitView.getAction_name()));
					action.addParam(new Param("description", groupActionLimitView.getAction_name()));
					action.addParam(new Param("type", groupActionLimitView.getAction_Type_id()));

					Dataset limits = new Dataset("limits");
					for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
						Record limit = new Record();
						limit.addParam(new Param("id", l.getKey()));
						limit.addParam(new Param("value", l.getValue()));
						limits.addRecord(limit);
					}
					if (limits.getAllRecords().size() > 0) {
						action.addDataset(limits);
					}
					actions.addRecord(action);
				}
				feature.addDataset(actions);
				featuresDataset.addRecord(feature);
			}

			result.addDataset(featuresDataset);

		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. Error: ", e).log();
		}

		return result;
	}

	public static void updateFeatures(GroupActionLimitView action,
			Map<String, Map<String, GroupActionLimitView>> features) {

		if (features.containsKey(action.getFeature_id())) {
			Map<String, GroupActionLimitView> actionsMap = features.get(action.getFeature_id());
			if (actionsMap.containsKey(action.getAction_id())) {
				GroupActionLimitView existingAction = actionsMap.get(action.getAction_id());
				if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
					existingAction.insertLimit(action.getLimitType_id(), action.getValue());
				}

			} else {
				if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
					action.insertLimit(action.getLimitType_id(), action.getValue());
				}
				actionsMap.put(action.getAction_id(), action);
			}
		} else {
			Map<String, GroupActionLimitView> actionsMap = new HashMap<>();
			if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
				action.insertLimit(action.getLimitType_id(), action.getValue());
			}
			actionsMap.put(action.getAction_id(), action);
			features.put(action.getFeature_id(), actionsMap);
		}
	}

	public Result getFeatureActions(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			String featureId = requestInstance.getParameter("featureId");
			if (StringUtils.isBlank(featureId)) {
				ErrorCodeEnum.ERR_21350.setErrorCode(result);
				alert.prepareError("Feature Id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch action list from featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "Feature_id eq '" + featureId + "'");
			inputMap.put(ODataQueryConstants.SELECT,
					"id, name, isMFAApplicable, Type_id, description, TermsAndConditions_id");
			inputMap.put(ODataQueryConstants.ORDER_BY, "DisplaySequence asc");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
					requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("featureaction")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
				Dataset actionDataset = new Dataset();
				actionDataset.setId("actions");
				if ((readActionJSONArray != null) && (readActionJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readActionJSONArray.length(); indexVar++) {
						JSONObject actionJSONObject = readActionJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String actionId = actionJSONObject.optString("id");
						Param actionId_Param = new Param("actionId", actionId, FabricConstants.STRING);
						currRecord.addParam(actionId_Param);
						String actionName = actionJSONObject.optString("name");
						Param actionName_Param = new Param("actionName", actionName, FabricConstants.STRING);
						currRecord.addParam(actionName_Param);
						String isMFAApplicable = actionJSONObject.optString("isMFAApplicable");
						Param isMFAApplicable_Param = new Param("isMFAApplicable", isMFAApplicable,
								FabricConstants.STRING);
						currRecord.addParam(isMFAApplicable_Param);
						String typeId = actionJSONObject.optString("Type_id");
						Param typeId_Param = new Param("Type_id", typeId, FabricConstants.STRING);
						currRecord.addParam(typeId_Param);
						String description = actionJSONObject.optString("description");
						Param description_Param = new Param("description", description, FabricConstants.STRING);
						currRecord.addParam(description_Param);
						Dataset limits = createLimitRecord(actionId, requestInstance, result);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
						currRecord.addDataset(limits);
						String termsAndConditionsId = actionJSONObject.optString("TermsAndConditions_id");
						Record termAndConditionRecord = createTermAndConditionRecord(termsAndConditionsId,
								requestInstance, result);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
						currRecord.addRecord(termAndConditionRecord);

						actionDataset.addRecord(currRecord);
					}
					result.addDataset(actionDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readActionResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Action Response: " + readActionResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21310.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Action. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21310.setErrorCode(result);
		}
		return result;
	}

	public Result getAllFeatures(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			HashMap<String, String> roleIdNameMap = new HashMap<String, String>();
			roleIdNameMap = createRoleIdNameMap(requestInstance);

			// Fetch feature list from feature table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, name, description, Type_id, Status_id, Service_Fee");

			String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
					requestInstance);

			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("feature")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
				Dataset featureDataset = new Dataset();
				featureDataset.setId("features");
				if ((readFeatureJSONArray != null) && (readFeatureJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFeatureJSONArray.length(); indexVar++) {
						JSONObject featureJSONObject = readFeatureJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String featureId = featureJSONObject.optString("id");
						Param featureId_Param = new Param("id", featureId, FabricConstants.STRING);
						currRecord.addParam(featureId_Param);
						String featureName = featureJSONObject.optString("name");
						Param featureName_Param = new Param("name", featureName, FabricConstants.STRING);
						currRecord.addParam(featureName_Param);
						String featureDescription = featureJSONObject.optString("description");
						Param featureDescription_Param = new Param("description", featureDescription,
								FabricConstants.STRING);
						currRecord.addParam(featureDescription_Param);
						String featureType = featureJSONObject.optString("Type_id");
						Param featureType_Param = new Param("Type_id", featureType, FabricConstants.STRING);
						currRecord.addParam(featureType_Param);
						String featureStatus = featureJSONObject.optString("Status_id");
						Param featureStatus_Param = new Param("Status_id", featureStatus, FabricConstants.STRING);
						currRecord.addParam(featureStatus_Param);
						String serviceFee = featureJSONObject.optString("Service_Fee");
						if (StringUtils.isNotBlank(serviceFee)) {
							serviceFee = String.format("%100.2f", Double.valueOf(serviceFee)).trim();
						}
						Param serviceFee_Param = new Param("Service_Fee", serviceFee, FabricConstants.STRING);
						currRecord.addParam(serviceFee_Param);
						Dataset featureTypeDataset = createFeatureTypeDataset(featureId, requestInstance, roleIdNameMap,
								result);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
						currRecord.addDataset(featureTypeDataset);
						Record featureDisplayNameRecord = createFeatureDisplayNameRecord(featureId, requestInstance,
								result);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
						currRecord.addRecord(featureDisplayNameRecord);

						featureDataset.addRecord(currRecord);
					}
					result.addDataset(featureDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readFeatureResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Response: " + readFeatureResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21352.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21352.setErrorCode(result);
		}
		return result;
	}

	public Dataset createLimitRecord(String actionId, DataControllerRequest requestInstance, Result result) {

		Dataset limitDataset = new Dataset();
		limitDataset.setId("limits");

		try {

			// Fetch action limits from actionlimit table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "LimitType_id, value");
			inputMap.put(ODataQueryConstants.FILTER, "Action_id eq '" + actionId + "'");

			String readActionLimitResponse = Executor.invokeService(ServiceURLEnum.ACTIONLIMIT_READ, inputMap, null,
					requestInstance);

			JSONObject readActionLimitResponseJSON = CommonUtilities.getStringAsJSONObject(readActionLimitResponse);
			if (readActionLimitResponseJSON != null && readActionLimitResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionLimitResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionLimitResponseJSON.has("actionlimit")) {
				JSONArray readActionLimitJSONArray = readActionLimitResponseJSON.optJSONArray("actionlimit");
				if ((readActionLimitJSONArray != null) && (readActionLimitJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readActionLimitJSONArray.length(); indexVar++) {
						Record limitRecord = new Record();
						JSONObject actionLimitJSONObject = readActionLimitJSONArray.getJSONObject(indexVar);
						String type = actionLimitJSONObject.optString("LimitType_id");
						String value = actionLimitJSONObject.optString("value");
						if (StringUtils.isNotBlank(value)) {
							value = String.format("%100.2f", Double.valueOf(value)).trim();
						}
						Param type_Param = new Param("type", type, FabricConstants.STRING);
						limitRecord.addParam(type_Param);
						Param value_Param = new Param("value", value, FabricConstants.STRING);
						limitRecord.addParam(value_Param);
						limitDataset.addRecord(limitRecord);
					}
					return limitDataset;
				}
			} else {
				result.addParam(new Param("message", readActionLimitResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature limits Response: " + readActionLimitResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21356.setErrorCode(result);
				return limitDataset;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature limits. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21356.setErrorCode(result);
			return limitDataset;
		}
		return limitDataset;
	}

	public Record createTermAndConditionRecord(String termAndConditionId, DataControllerRequest requestInstance,
			Result result) {

		Record tAndCRecord = new Record();
		tAndCRecord.setId("termandcondition");

		try {

			// Fetch term and condition list from termandconditiontext table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "LanguageCode, Content, ContentType_id");
			inputMap.put(ODataQueryConstants.FILTER,
					"TermAndConditionId eq '" + termAndConditionId + "' and Status_id eq '" + "SID_TANDC_ACTIVE'");

			String readTAndCResponse = Executor.invokeService(ServiceURLEnum.TERMANDCONDITIONTEXT_READ, inputMap, null,
					requestInstance);

			JSONObject readTandCResponseJSON = CommonUtilities.getStringAsJSONObject(readTAndCResponse);
			if (readTandCResponseJSON != null && readTandCResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTandCResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTandCResponseJSON.has("termandconditiontext")) {
				JSONArray readTandCJSONArray = readTandCResponseJSON.optJSONArray("termandconditiontext");
				if ((readTandCJSONArray != null) && (readTandCJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readTandCJSONArray.length(); indexVar++) {
						JSONObject tAndCJSONObject = readTandCJSONArray.getJSONObject(indexVar);
						String localeId = tAndCJSONObject.optString("LanguageCode");
						String content = tAndCJSONObject.optString("Content");
						String contentType = tAndCJSONObject.optString("ContentType_id");

						Record contentLangRecord = new Record();
						contentLangRecord.setId(localeId);
						Param content_Param = new Param("content", content, FabricConstants.STRING);
						contentLangRecord.addParam(content_Param);
						Param contentType_Param = new Param("contentType", contentType, FabricConstants.STRING);
						contentLangRecord.addParam(contentType_Param);
						tAndCRecord.addRecord(contentLangRecord);
					}
					return tAndCRecord;
				}
			} else {
				result.addParam(new Param("message", readTAndCResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch action term and condition Response: " + readTAndCResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21357.setErrorCode(result);
				return tAndCRecord;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching action term and condition. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21357.setErrorCode(result);
			return tAndCRecord;
		}
		return tAndCRecord;
	}

	public Record createFeatureDisplayNameRecord(String featureId, DataControllerRequest requestInstance,
			Result result) {

		Record displayRecord = new Record();
		displayRecord.setId("featureDisplayName");

		try {

			// Fetch feature display name list from featuredisplaynamedescription table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "Locale_id, DisplayName, DisplayDescription");
			inputMap.put(ODataQueryConstants.FILTER, "Feature_id eq '" + featureId + "'");

			String readFeatureDisplayResponse = Executor
					.invokeService(ServiceURLEnum.FEATUREDISPLAYNAMEDESCRIPTION_READ, inputMap, null, requestInstance);

			JSONObject readFeatureDisplayResponseJSON = CommonUtilities
					.getStringAsJSONObject(readFeatureDisplayResponse);
			if (readFeatureDisplayResponseJSON != null && readFeatureDisplayResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureDisplayResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureDisplayResponseJSON.has("featuredisplaynamedescription")) {
				JSONArray readFeatureDisplayJSONArray = readFeatureDisplayResponseJSON
						.optJSONArray("featuredisplaynamedescription");
				if ((readFeatureDisplayJSONArray != null) && (readFeatureDisplayJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFeatureDisplayJSONArray.length(); indexVar++) {
						JSONObject featureDisplayJSONObject = readFeatureDisplayJSONArray.getJSONObject(indexVar);
						String localeId = featureDisplayJSONObject.optString("Locale_id");
						String displayName = featureDisplayJSONObject.optString("DisplayName");
						String displayDescription = featureDisplayJSONObject.optString("DisplayDescription");

						Record contentLangRecord = new Record();
						contentLangRecord.setId(localeId);
						Param displayName_Param = new Param("displayName", displayName, FabricConstants.STRING);
						contentLangRecord.addParam(displayName_Param);
						Param displayDescription_Param = new Param("displayDescription", displayDescription,
								FabricConstants.STRING);
						contentLangRecord.addParam(displayDescription_Param);
						displayRecord.addRecord(contentLangRecord);
					}
					return displayRecord;
				}
			} else {
				result.addParam(new Param("message", readFeatureDisplayResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Display Response: " + readFeatureDisplayResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21355.setErrorCode(result);
				return displayRecord;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature Display. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21355.setErrorCode(result);
			return displayRecord;
		}
		return displayRecord;
	}

	HashMap<String, String> createRoleIdNameMap(DataControllerRequest requestInstance) {

		HashMap<String, String> roleIdNameMap = new HashMap<String, String>();

		try {

			// Fetch Role type list from membergrouptype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, description");

			String readMemberGroupTypeResponse = Executor.invokeService(ServiceURLEnum.MEMBERGROUPTYPE_READ, inputMap,
					null, requestInstance);

			JSONObject readMemberGroupTypeResponseJSON = CommonUtilities
					.getStringAsJSONObject(readMemberGroupTypeResponse);
			if (readMemberGroupTypeResponseJSON != null && readMemberGroupTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMemberGroupTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMemberGroupTypeResponseJSON.has("membergrouptype")) {
				JSONArray readMemberGroupTypeJSONArray = readMemberGroupTypeResponseJSON
						.optJSONArray("membergrouptype");
				if ((readMemberGroupTypeJSONArray != null) && (readMemberGroupTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readMemberGroupTypeJSONArray.length(); indexVar++) {
						JSONObject memberGroupTypeJSONObject = readMemberGroupTypeJSONArray.getJSONObject(indexVar);
						String featureTypeId = memberGroupTypeJSONObject.optString("id");
						String featureTypeName = memberGroupTypeJSONObject.optString("description");
						roleIdNameMap.put(featureTypeId, featureTypeName);
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Member Group Type. Exception: ", e).log();
		}
		return roleIdNameMap;
	}

	public Dataset createFeatureTypeDataset(String featureId, DataControllerRequest requestInstance,
			HashMap<String, String> roleIdNameMap, Result result) {

		Dataset featureTypeDataset = new Dataset();
		featureTypeDataset.setId("roleTypes");
		try {

			// Fetch Role type list from featureroletype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "RoleType_id");
			inputMap.put(ODataQueryConstants.FILTER, "Feature_id eq '" + featureId + "'");

			String readFeatureTypeResponse = Executor.invokeService(ServiceURLEnum.FEATUREROLETYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readFeatureTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureTypeResponse);
			if (readFeatureTypeResponseJSON != null && readFeatureTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureTypeResponseJSON.has("featureroletype")) {
				JSONArray readFeatureTypeJSONArray = readFeatureTypeResponseJSON.optJSONArray("featureroletype");
				if ((readFeatureTypeJSONArray != null) && (readFeatureTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFeatureTypeJSONArray.length(); indexVar++) {
						JSONObject featureTypeJSONObject = readFeatureTypeJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String featureTypeId = featureTypeJSONObject.optString("RoleType_id");
						Param featureTypeId_Param = new Param("id", featureTypeId, FabricConstants.STRING);
						currRecord.addParam(featureTypeId_Param);
						Param featureTypeName_Param = new Param("name", roleIdNameMap.get(featureTypeId),
								FabricConstants.STRING);
						currRecord.addParam(featureTypeName_Param);
						featureTypeDataset.addRecord(currRecord);
					}
					return featureTypeDataset;
				}
			} else {
				result.addParam(new Param("message", readFeatureTypeResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Type Response: " + readFeatureTypeResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21354.setErrorCode(result);
				return featureTypeDataset;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature Type. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21354.setErrorCode(result);
			return featureTypeDataset;
		}
		return featureTypeDataset;
	}

	public Result editFeatureAndActionLimits(DataControllerRequest requestInstance) {

		Result result = new Result();

		try {

			String loggedInUserId = null;
			UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);
			if (loggedInUserDetails != null) {
				loggedInUserId = loggedInUserDetails.getId();
			}

			// Validate FeatureId
			String featureId = requestInstance.getParameter("featureId");
			if (StringUtils.isBlank(featureId)) {
				alert.prepareError("Feature Id is a mandatory input").log();
				ErrorCodeEnum.ERR_21350.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate ServiceFee
			String serviceFee = requestInstance.getParameter("serviceFee");
			if (StringUtils.isBlank(serviceFee)) {
				alert.prepareError("Service fee is a mandatory input").log();
				ErrorCodeEnum.ERR_21358.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			if (!isDecimalNumber(serviceFee)) {
				alert.prepareError("Service fee should be decimal").log();
				ErrorCodeEnum.ERR_21368.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate StatusId
			String statusId = requestInstance.getParameter("statusId");
			if (StringUtils.isBlank(statusId)) {
				alert.prepareError("Status id cannot be empty").log();
				ErrorCodeEnum.ERR_20186.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			if (!(StringUtils.equals(statusId, FEATURE_ACTIVE_STATUS)
					|| StringUtils.equals(statusId, FEATURE_INACTIVE_STATUS)
					|| StringUtils.equals(statusId, FEATURE_DOWN_STATUS))) {
				alert.prepareError("Status id can be active or inactive or down").log();
				ErrorCodeEnum.ERR_21369.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			Map<String, String> inputMap = new HashMap<String, String>();
			// Updating statusId and serviceFee in feature table
			inputMap.put("id", featureId);
			inputMap.put("Status_id", statusId);
			inputMap.put("Service_Fee", serviceFee);
			inputMap.put("modifiedby", loggedInUserDetails.getUserName());
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

			String editFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_UPDATE, inputMap, null,
					requestInstance);

			JSONObject editFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(editFeatureResponse);
			if ((editFeatureResponseJSON != null) && editFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& editFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				diagnostic.prepareDebug("Feature updated successfully.").log();
			} else {
				alert.prepareError("Failed to edit Feature").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21365.setErrorCode(result);
				return result;
			}

			String featureDisplayString = requestInstance.getParameter("featureDisplay");
			JSONArray featureDisplayJSONArray = CommonUtilities.getStringAsJSONArray(featureDisplayString);

			if (featureDisplayJSONArray != null && featureDisplayJSONArray.length() > 0) {
				for (int indexVar = 0; indexVar < featureDisplayJSONArray.length(); indexVar++) {
					JSONObject featureDisplayObj = featureDisplayJSONArray.optJSONObject(indexVar);

					String localeId = featureDisplayObj.optString("localeId");
					String displayName = featureDisplayObj.optString("displayName");
					String displayDescription = featureDisplayObj.optString("displayDescription");
					// Validate LocaleId
					if (StringUtils.isBlank(localeId)) {
						alert.prepareError("Locale id cannot be empty").log();
						ErrorCodeEnum.ERR_21109.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}

					// Validate DisplayName
					if (StringUtils.isBlank(displayName)) {
						alert.prepareError("Feature display name cannot be empty").log();
						ErrorCodeEnum.ERR_21359.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}

					// Validate DisplayDescription
					if (StringUtils.isBlank(displayDescription)) {
						alert.prepareError("Feature display description cannot be empty").log();
						ErrorCodeEnum.ERR_21360.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}

					// Updating displayName and displayDescription in feature table
					inputMap.clear();
					inputMap.put(ODataQueryConstants.SELECT, "Feature_id");
					inputMap.put(ODataQueryConstants.FILTER,
							"Feature_id eq '" + featureId + "' and Locale_id eq '" + localeId + "'");
					String readFeatureDisplayResponse = Executor.invokeService(
							ServiceURLEnum.FEATUREDISPLAYNAMEDESCRIPTION_READ, inputMap, null, requestInstance);

					inputMap.clear();
					inputMap.put("Feature_id", featureId);
					inputMap.put("Locale_id", localeId);
					inputMap.put("DisplayName", displayName);
					inputMap.put("DisplayDescription", displayDescription);
					JSONObject readFeatureDisplayResponseJSON = CommonUtilities
							.getStringAsJSONObject(readFeatureDisplayResponse);
					if (readFeatureDisplayResponseJSON != null
							&& readFeatureDisplayResponseJSON.has(FabricConstants.OPSTATUS)
							&& readFeatureDisplayResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
							&& readFeatureDisplayResponseJSON.has("featuredisplaynamedescription")) {
						JSONArray readFeatureTypeJSONArray = readFeatureDisplayResponseJSON
								.optJSONArray("featuredisplaynamedescription");
						if (!((readFeatureTypeJSONArray != null) && (readFeatureTypeJSONArray.length() > 0))) {
							inputMap.put("createdby", loggedInUserId);
							inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							String createFeatureDisplayResponse = Executor.invokeService(
									ServiceURLEnum.FEATUREDISPLAYNAMEDESCRIPTION_CREATE, inputMap, null,
									requestInstance);

							JSONObject createFeatureDisplayResponseJSON = CommonUtilities
									.getStringAsJSONObject(createFeatureDisplayResponse);
							if ((createFeatureDisplayResponseJSON != null)
									&& createFeatureDisplayResponseJSON.has(FabricConstants.OPSTATUS)
									&& createFeatureDisplayResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
								diagnostic.prepareDebug("Feature display created successfully.").log();
							} else {
								alert.prepareError("Failed to created Feature display").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								ErrorCodeEnum.ERR_21374.setErrorCode(result);
								return result;
							}
						} else {
							inputMap.put("modifiedby", loggedInUserDetails.getUserName());
							inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

							String editFeatureDisplayResponse = Executor.invokeService(
									ServiceURLEnum.FEATUREDISPLAYNAMEDESCRIPTION_UPDATE, inputMap, null,
									requestInstance);

							JSONObject editFeatureDisplayResponseJSON = CommonUtilities
									.getStringAsJSONObject(editFeatureDisplayResponse);
							if ((editFeatureDisplayResponseJSON != null)
									&& editFeatureDisplayResponseJSON.has(FabricConstants.OPSTATUS)
									&& editFeatureDisplayResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
								diagnostic.prepareDebug("Feature display updated successfully.").log();
							} else {
								alert.prepareError("Failed to edit Feature display").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								ErrorCodeEnum.ERR_21366.setErrorCode(result);
								return result;
							}
						}
					}
				}
			}

			String actionString = requestInstance.getParameter("actions");
			JSONArray actionJSONArray = CommonUtilities.getStringAsJSONArray(actionString);

			if (actionJSONArray != null && actionJSONArray.length() > 0) {
				for (int indexVar = 0; indexVar < actionJSONArray.length(); indexVar++) {
					JSONObject actionObj = actionJSONArray.optJSONObject(indexVar);

					String actionId = actionObj.optString("actionId");

					// Validate ActionId
					if (StringUtils.isBlank(actionId)) {
						alert.prepareError("Action Id is mandatory input").log();
						ErrorCodeEnum.ERR_20866.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}

					String limitString = actionObj.optString("limits");
					JSONArray limitJSONArray = CommonUtilities.getStringAsJSONArray(limitString);
					if (limitJSONArray != null && limitJSONArray.length() > 0) {
						Double minTxVal = 0.0, maxTxVal = 0.0, dailyTxVal = 0.0, weeklyTxVal = 0.0;
						for (int index = 0; index < limitJSONArray.length(); index++) {
							JSONObject limitObj = limitJSONArray.optJSONObject(index);
							String type = limitObj.optString("type");
							String value = limitObj.optString("value");

							// Validate Limit
							if (StringUtils.isBlank(value)) {
								alert.prepareError("Limit cannot be empty").log();
								ErrorCodeEnum.ERR_21361.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							}

							if (!isDecimalNumber(value)) {
								alert.prepareError("Limit should be decimal").log();
								ErrorCodeEnum.ERR_21371.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							}
							if (StringUtils.equals(type, MIN_TRANSACTION_LIMIT_PARAM)) {
								minTxVal = Double.parseDouble(value);
							} else if (StringUtils.equals(type, MAX_TRANSACTION_LIMIT_PARAM)) {
								maxTxVal = Double.parseDouble(value);

							} else if (StringUtils.equals(type, MAX_DAILY_LIMIT_PARAM)) {
								dailyTxVal = Double.parseDouble(value);
							} else if (StringUtils.equals(type, MAX_WEEKLY_LIMIT_PARAM)) {
								weeklyTxVal = Double.parseDouble(value);
							}
						}
						if (!validateLimits(minTxVal, maxTxVal, dailyTxVal, weeklyTxVal)) {
							alert.prepareError("Limits validation failed").log();
							ErrorCodeEnum.ERR_21372.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
						inputMap.clear();
						inputMap.put("_action", actionId);
						inputMap.put("_minTxLimit", minTxVal.toString());
						inputMap.put("_maxTxLimit", maxTxVal.toString());
						inputMap.put("_dailyLimit", dailyTxVal.toString());
						inputMap.put("_weeklyLimit", weeklyTxVal.toString());
						String editActionLimitMinTransResponse = Executor.invokeService(
								ServiceURLEnum.ACTION_LIMITS_UPDATE_PROC, inputMap, null, requestInstance);

						JSONObject editActionLimitMinTransResponseJSON = CommonUtilities
								.getStringAsJSONObject(editActionLimitMinTransResponse);
						if ((editActionLimitMinTransResponseJSON != null)
								&& editActionLimitMinTransResponseJSON.has(FabricConstants.OPSTATUS)
								&& editActionLimitMinTransResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
							diagnostic.prepareDebug("Action limit updated successfully.").log();
						} else {
							alert.prepareError("Failed to edit action limit").log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							ErrorCodeEnum.ERR_21367.setErrorCode(result);
							return result;
						}

					}

				}
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in edit feature flow. Exception: ", e).log();
			ErrorCodeEnum.ERR_21365.setErrorCode(result);
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
		}

		return result;
	}

	public static boolean validateLimits(Double minTxVal, Double maxTxVal, Double dailyTxVal, Double weeklyTxVal) {

		if (0 < minTxVal && minTxVal <= maxTxVal && maxTxVal <= dailyTxVal && dailyTxVal <= weeklyTxVal)
			return true;
		return false;

	}

	public static boolean isDecimalNumber(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
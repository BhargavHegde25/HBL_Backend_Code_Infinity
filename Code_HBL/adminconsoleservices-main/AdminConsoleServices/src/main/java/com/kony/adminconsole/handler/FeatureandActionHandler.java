package com.kony.adminconsole.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.GroupActionLimitView;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.featuresandactions.FeatureManageService;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * FeatureandActionHandler is used to maintain the modular code for performing
 * actions on feature and actions module
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class FeatureandActionHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	public static HashMap<String, JSONObject> getFeatureDisplayContent(String filter,
			DataControllerRequest requestInstance, Result processedResult) {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, filter);
		alert.prepareError("Feature Filter:" + filter).log();

		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.FEATUREDISPLAYNAMEDESCRIPTION_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("featuredisplaynamedescription")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21400.setErrorCode(processedResult);
			return null;
		}

		HashMap<String, JSONObject> featureContentMap = new HashMap<>();
		JSONArray featureDisplayContent = readCustomerResponse.getJSONArray("featuredisplaynamedescription");
		for (Object featureObject : featureDisplayContent) {
			JSONObject featureJSON = (JSONObject) featureObject;
			featureContentMap.put(featureJSON.getString("Feature_id"), featureJSON);
		}
		return featureContentMap;
	}

	public static HashMap<String, JSONObject> getActionDisplayContent(String filter,
			DataControllerRequest requestInstance, Result processedResult) {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, filter);
		alert.prepareError("Action Filter:" + filter).log();
		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.ACTIONDISPLAYNAMEDESCRIPTION_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("actiondisplaynamedescription")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21401.setErrorCode(processedResult);
			return null;
		}

		HashMap<String, JSONObject> actionContentMap = new HashMap<>();
		JSONArray actionDisplayContent = readCustomerResponse.getJSONArray("actiondisplaynamedescription");
		for (Object actionObject : actionDisplayContent) {
			JSONObject actionJSON = (JSONObject) actionObject;
			actionContentMap.put(actionJSON.getString("Action_id"), actionJSON);
		}
		return actionContentMap;
	}

	public static JSONArray getCustomerGroups(String customerId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + "'");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERGROUPINFO_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("customergroupinfo_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21852);
		}
		return readResponse.getJSONArray("customergroupinfo_view");
	}

	public static JSONArray getCustomerRetailGroups(String customerId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		StringBuffer filterQueryBuffer = new StringBuffer();
		filterQueryBuffer.append("Customer_id eq '" + customerId + "' and Group_Type_id eq 'TYPE_ID_RETAIL'");
		filterQueryBuffer.trimToSize();
		String filterQuery = filterQueryBuffer.toString();
		filterQuery = filterQuery.trim();

		inputMap.put(ODataQueryConstants.FILTER, filterQuery);
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERGROUPINFO_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("customergroupinfo_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21852);
		}
		return readResponse.getJSONArray("customergroupinfo_view");
	}

	public static JSONArray getGroupActionLimits(String filter, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, filter);
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GROUPACTIONLIMIT_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("groupactionlimit")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21853);
		}
		return readResponse.getJSONArray("groupactionlimit");
	}

	public static Map<String, JSONObject> getAllActionDetails(JSONArray actions,
			Map<String, Integer> featureActionsCount) throws ApplicationException {

		Map<String, JSONObject> actionsMap = new HashMap<>();
		for (Object actionObject : actions) {
			JSONObject action = (JSONObject) actionObject;
			if (actionsMap.containsKey(action.getString("id"))) {
				JSONObject actionInMap = actionsMap.get(action.getString("id"));
				if (action.has("LimitType_id")) {
					JSONObject limits = actionInMap.getJSONObject("limits");
					limits.put(action.getString("LimitType_id"), action.getString("value"));
				}
			} else {
				featureActionsCount.put(action.getString("Feature_id"),
						featureActionsCount.get(action.getString("Feature_id")) != null
								? featureActionsCount.get(action.getString("Feature_id")) + 1
								: 1);
				if (action.has("LimitType_id")) {
					JSONObject limits = new JSONObject();
					limits.put(action.getString("LimitType_id"), action.getString("value"));
					action.put("limits", limits);
					action.remove("LimitType_id");
					action.remove("value");
				}
				actionsMap.put(action.getString("id"), action);
			}

		}
		return actionsMap;
	}

	public static JSONArray getAllRetailActions(JSONArray actions, Map<String, String> retailActionsMap)
			throws ApplicationException {
		JSONArray retailAction = new JSONArray();
		for (Object actionObject : actions) {
			JSONObject action = (JSONObject) actionObject;
			if (retailActionsMap.containsKey(action.getString("id"))) {
				retailAction.put(action);
			}
		}
		return retailAction;
	}

	public static Dataset getOtherFeaturesAndActions(JSONArray allActions,
			Map<String, ArrayList<String>> actionToGroups, JSONObject directActions,
			Map<String, Integer> featureActionsCount) {
		try {
			Dataset featuresDataset = new Dataset("features");

			Map<String, Map<String, GroupActionLimitView>> features = new HashMap<>();
			for (int i = 0; i < allActions.length(); i++) {
				GroupActionLimitView action = FeatureandActionHandler
						.getGroupActionObjectFromAction(allActions.getJSONObject(i));
				FeatureManageService.updateFeatures(action, features);
			}

			for (Map.Entry<String, Map<String, GroupActionLimitView>> f : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", f.getKey()));
				Dataset actions = new Dataset("actions");
				for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
					GroupActionLimitView groupActionLimitView = a.getValue();
					if (!actionToGroups.containsKey(groupActionLimitView.getAction_id())) {
						if (feature.getParam("name") == null) {
							feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
							feature.addParam(new Param("description", groupActionLimitView.getFeature_description()));
							feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
							feature.addParam(new Param("type", groupActionLimitView.getFeature_Type_id()));
							feature.addParam(
									new Param("displaySequence", groupActionLimitView.getFeature_displaysequence()));
							feature.addParam(
									new Param("isPrimary", groupActionLimitView.getFeature_isPrimary().toString()));
							feature.addParam(new Param("totalActions",
									featureActionsCount.get(groupActionLimitView.getFeature_id()).toString()));

						}
						Record action = new Record();
						action.addParam(new Param("id", a.getKey()));
						action.addParam(new Param("name", groupActionLimitView.getAction_name()));
						action.addParam(new Param("description", groupActionLimitView.getAction_name()));
						action.addParam(new Param("type", groupActionLimitView.getAction_Type_id()));
						action.addParam(new Param("displaySequence", groupActionLimitView.getAction_displaysequence()));
						action.addParam(new Param("isPrimary", groupActionLimitView.getIsPrimary()));
						if (StringUtils.isNotBlank(groupActionLimitView.getAction_dependency())) {
							action.addParam(new Param("dependency", groupActionLimitView.getAction_dependency()));
						}
						if (directActions.has(groupActionLimitView.getAction_id())) {
							action.addParam(new Param("isAssigned", "1"));
							if (feature.getParam("isAssigned") == null) {
								feature.addParam(new Param("isAssigned", "1"));
							}
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
				}

				if (null != actions.getAllRecords() && actions.getAllRecords().size() > 0) {
					feature.addDataset(actions);
					featuresDataset.addRecord(feature);
				}
			}
			return featuresDataset;
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONArray getAllFeatureActions(String filterForAllActions, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(filterForAllActions)) {
			inputMap.put(ODataQueryConstants.FILTER, filterForAllActions);
		}
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.FEATURE_ACTIONS_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("feature_actions_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21856);
		}
		return readResponse.getJSONArray("feature_actions_view");
	}

	public static Map<String, String> getLimitSubTypeMap(DataControllerRequest requestInstance, Result processedResult)
			throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.LIMITSUBTYPE_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("limitsubtype")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21855);
		}
		JSONArray limits = readCustomerResponse.getJSONArray("limitsubtype");
		Map<String, String> limitsMap = new HashMap<>();
		for (Object limitObject : limits) {
			JSONObject limit = (JSONObject) limitObject;
			limitsMap.put(limit.getString("id"), limit.getString("LimitType_id"));
		}
		return limitsMap;
	}

	public static JSONArray getBusinessAccountsOfCustomer(String customerId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + "'");
		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERACCOUNTS_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("customeraccounts")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21854);
		}
		return readCustomerResponse.getJSONArray("customeraccounts");
	}

	public static JSONArray getCustomerDirectPermissions(String customerId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + "'");
		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("customeraction")) {
			return null;
		}
		return readCustomerResponse.getJSONArray("customeraction");
	}

	public static JSONArray getGroupFeatureActions(String groupId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + groupId + "'");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.GROUP_FEATURES_ACTIONS_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readResponse.has("group_features_actions_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21857);
		}
		return readResponse.getJSONArray("group_features_actions_view");
	}

	public static String getGroupType(String groupId, DataControllerRequest requestInstance, Result processedResult)
			throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "id eq '" + groupId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "Type_id");

		String readGroupResponse = Executor.invokeService(ServiceURLEnum.MEMBERGROUP_READ, inputMap, null,
				requestInstance);

		String groupTypeId = null;

		JSONObject readGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readGroupResponse);
		if (readGroupResponseJSON != null && readGroupResponseJSON.has(FabricConstants.OPSTATUS)
				&& readGroupResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readGroupResponseJSON.has("membergroup")) {
			JSONArray readGroupJSONArray = readGroupResponseJSON.optJSONArray("membergroup");
			if (!(readGroupJSONArray == null || readGroupJSONArray.length() < 1)) {
				JSONObject groupObj = readGroupJSONArray.getJSONObject(0);
				groupTypeId = groupObj.getString("Type_id");
			}
		}
		return groupTypeId;
	}

	public static JSONArray getGroupFeatureActionsByType(String TypeId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<String, String>();
		String[] arrOfTypes = TypeId.split(",");

		StringBuilder filterQueryBuffer = new StringBuilder();

		for (String Type : arrOfTypes) {
			filterQueryBuffer.append("Type_id eq '" + Type + "' or ");
		}
		if (filterQueryBuffer.toString().trim().endsWith("or")) {
			filterQueryBuffer.delete(filterQueryBuffer.lastIndexOf("or"), filterQueryBuffer.length());
			filterQueryBuffer.trimToSize();
		}
		inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString().trim());
		inputMap.put(ODataQueryConstants.SELECT, "");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.GROUP_FEATURES_ACTIONS_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readResponse.has("group_features_actions_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21857);
		}
		return readResponse.getJSONArray("group_features_actions_view");
	}

	public static JSONArray getTypeFeatureActions(String typeId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		JSONObject currJSONObject;
		inputMap.put(ODataQueryConstants.FILTER, "RoleType_id eq '" + typeId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "Feature_id");
		JSONObject currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.FEATUREROLETYPE_READ, inputMap, null, requestInstance));
		if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
				|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !currOperationResponseJSON.has("featureroletype")) {
			alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.FEATUREROLETYPE_READ.name()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20906);
		}
		JSONArray featuresJSONArray = currOperationResponseJSON.optJSONArray("featureroletype");
		StringBuffer filterQueryBuffer = new StringBuffer();
		String currFeatureId;
		filterQueryBuffer.append("(");

		for (int index = 0; index < featuresJSONArray.length(); index++) {

			if (featuresJSONArray.get(index) instanceof JSONObject) {
				currJSONObject = featuresJSONArray.getJSONObject(index);
				currFeatureId = currJSONObject.optString("Feature_id");
				if (StringUtils.isNotBlank(currFeatureId)) {

					filterQueryBuffer.append("Feature_id eq '" + currFeatureId + "'");
				}
				if (index < featuresJSONArray.length() - 1) {
					filterQueryBuffer.append(" or ");
				}
			}

		}
		filterQueryBuffer.append(")");
		inputMap.clear();
		if (StringUtils.isNotBlank(filterQueryBuffer)) {
			inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
		}
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.FEATURE_ACTIONS_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("feature_actions_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21856);
		}
		return readResponse.getJSONArray("feature_actions_view");
	}

	public static Map<String, JSONObject> getCompanyActions(String companyId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Organisation_id eq '" + companyId + "'");

		JSONObject readCompanyResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.ORGANISATION_ACTION_LIMITS_VIEW_READ, inputMap, null, requestInstance));
		if (readCompanyResponse == null || !readCompanyResponse.has(FabricConstants.OPSTATUS)
				|| readCompanyResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCompanyResponse.has("organisation_action_limits_view")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCompanyResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21851);
		}

		Map<String, JSONObject> actionsMap = new HashMap<>();
		JSONArray actionsArray = readCompanyResponse.getJSONArray("organisation_action_limits_view");
		for (Object actionObject : actionsArray) {
			JSONObject actionJSON = (JSONObject) actionObject;
			if (actionsMap.containsKey(actionJSON.getString("Action_id"))) {
				JSONObject actionInMap = actionsMap.get(actionJSON.getString("Action_id"));
				if (actionJSON.has("LimitType_id")) {
					JSONObject limits = actionInMap.getJSONObject("limits");
					limits.put(actionJSON.getString("LimitType_id"), actionJSON.getString("value"));
				}

			} else {
				JSONObject action = new JSONObject();
				action.put("code", actionJSON.getString("Action_id"));
				action.put("isAccountLevel", actionJSON.getString("isAccountLevel"));
				if (actionJSON.has("LimitType_id")) {
					JSONObject limits = new JSONObject();
					limits.put(actionJSON.getString("LimitType_id"), actionJSON.getString("value"));
					actionJSON.remove("LimitType_id");
					actionJSON.remove("value");
					action.put("limits", limits);
				}
				actionsMap.put(actionJSON.getString("Action_id"), action);
			}
		}
		return actionsMap;
	}

	public static GroupActionLimitView getGroupActionObject(JSONObject actionObject) {
		GroupActionLimitView groupActionLimitView = new GroupActionLimitView();
		groupActionLimitView.setGroup_id(actionObject.optString("Group_id"));
		groupActionLimitView.setGroup_name(actionObject.optString("Group_name"));
		groupActionLimitView.setGroup_type(actionObject.optString("Type_id"));
		groupActionLimitView.setGroup_description(actionObject.optString("Group_description"));
		groupActionLimitView.setAction_id(actionObject.optString("Action_id"));
		groupActionLimitView.setLimitType_id(actionObject.optString("LimitType_id"));
		groupActionLimitView.setValue(actionObject.optString("value"));
		groupActionLimitView.setGroupactionlimit_id(actionObject.optString("groupactionlimit_id"));
		groupActionLimitView.setAction_name(actionObject.optString("Action_name"));
		groupActionLimitView.setAction_description(actionObject.optString("Action_description"));
		groupActionLimitView.setAction_Type_id(actionObject.optString("Action_Type_id"));
		groupActionLimitView.setAction_displaysequence(actionObject.optString("Action_displaysequence"));
		groupActionLimitView.setAction_dependency(actionObject.optString("Action_dependency"));
		groupActionLimitView.setFeature_id(actionObject.optString("Feature_id"));
		groupActionLimitView.setIsMFAApplicable(actionObject.optString("isMFAApplicable"));
		groupActionLimitView.setIsAccountLevel(actionObject.optString("isAccountLevel"));
		groupActionLimitView.setIsPrimary(actionObject.optString("isPrimary"));
		groupActionLimitView.setFeature_name(actionObject.optString("Feature_name"));
		groupActionLimitView.setFeature_description(actionObject.optString("Feature_description"));
		groupActionLimitView.setFeature_Type_id(actionObject.optString("Feature_Type_id"));
		groupActionLimitView.setFeature_Status_id(actionObject.optString("Feature_Status_id"));
		groupActionLimitView.setFeature_displaysequence(actionObject.optString("Feature_displaysequence"));
		groupActionLimitView.setFeature_isPrimary(actionObject.optString("Feature_isPrimary"));
		return groupActionLimitView;
	}

	public static GroupActionLimitView getGroupActionObjectFromAction(JSONObject actionObject) {
		GroupActionLimitView groupActionLimitView = new GroupActionLimitView();
		groupActionLimitView.setAction_id(actionObject.optString("id"));
		groupActionLimitView.setLimitType_id(actionObject.optString("LimitType_id"));
		groupActionLimitView.setValue(actionObject.optString("value"));
		groupActionLimitView.setAction_name(actionObject.optString("action_name"));
		groupActionLimitView.setAction_description(actionObject.optString("action_description"));
		groupActionLimitView.setAction_Type_id(actionObject.optString("action_Type_id"));
		groupActionLimitView.setAction_displaysequence(actionObject.optString("action_displaysequence"));
		groupActionLimitView.setAction_dependency(actionObject.optString("action_dependency"));
		groupActionLimitView.setFeature_id(actionObject.optString("Feature_id"));
		groupActionLimitView.setIsMFAApplicable(actionObject.optString("isMFAApplicable"));
		groupActionLimitView.setIsAccountLevel(actionObject.optString("isAccountLevel"));
		groupActionLimitView.setIsPrimary(actionObject.optString("isPrimary"));
		groupActionLimitView.setFeature_name(actionObject.optString("feature_name"));
		groupActionLimitView.setFeature_description(actionObject.optString("feature_description"));
		groupActionLimitView.setFeature_Type_id(actionObject.optString("feature_Type_id"));
		groupActionLimitView.setFeature_Status_id(actionObject.optString("feature_status_id"));
		groupActionLimitView.setFeature_displaysequence(actionObject.optString("feature_displaysequence"));
		groupActionLimitView.setFeature_isPrimary(actionObject.optString("feature_isPrimary"));
		return groupActionLimitView;
	}

	public static JSONArray getMonetaryActions(DataControllerRequest requestInstance, Result result,
			JSONArray featuresList) throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, String> postParametersMap = new HashMap<String, String>();

		List<String> $filter = new ArrayList<String>();
		for (Object featureObject : featuresList) {
			JSONObject feature = (JSONObject) featureObject;
			if (StringUtils.isNotBlank(feature.getString("id"))) {
				$filter.add(" (Feature_id eq '" + feature.getString("id") + "' and action_Type_id eq 'MONETARY')");
			}
		}
		if (!$filter.isEmpty()) {
			postParametersMap.put(ODataQueryConstants.FILTER, StringUtils.join($filter, " or "));
		}
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor
				.invokeService(ServiceURLEnum.FEATURE_ACTIONS_VIEW_READ, postParametersMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("feature_actions_view")) {
			result.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21856);
		}
		return readResponse.getJSONArray("feature_actions_view");
	}

	public static JSONArray getCompanyFeatures(String organizationId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "organisationId eq '" + organizationId + "'");

		JSONObject readCompanyResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.ORGANISATIONFEATURES_READ, inputMap, null, requestInstance));
		if (readCompanyResponse == null || !readCompanyResponse.has(FabricConstants.OPSTATUS)
				|| readCompanyResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCompanyResponse.has("organisationfeatures")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCompanyResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21851);
		}

		return readCompanyResponse.getJSONArray("organisationfeatures");
	}

	public static JSONArray getOrganisationAccounts(String orgId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {
		// TODO Auto-generated method stub
        JSONObject getCompanyAccountsresponse = DBPServices.getCompanyAccounts(orgId,
                requestInstance);
        if (getCompanyAccountsresponse.has("errmsg")) {
            processedResult.addParam(new Param("errMsg", getCompanyAccountsresponse.getString("errmsg"),
                    FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Company accounts. Organization_id: " + orgId);
            processedResult
			.addParam(new Param("FailureReason", String.valueOf(getCompanyAccountsresponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21851);
        } else if (getCompanyAccountsresponse == null
                || !getCompanyAccountsresponse.has(FabricConstants.OPSTATUS)
                || getCompanyAccountsresponse.getInt(FabricConstants.OPSTATUS) != 0) {
            ErrorCodeEnum.ERR_21016.setErrorCode(processedResult);
            processedResult
			.addParam(new Param("FailureReason", String.valueOf(getCompanyAccountsresponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21851);
        } else {                    
            // Creating Dataset and adding to result
            return getCompanyAccountsresponse.getJSONArray("OgranizationAccounts");
        }
	}

	public static JSONArray getRetailAccountsOfCustomer(String customerId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<String, String>();

		inputMap.clear();
		inputMap.put("_customerId", customerId);

		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.RETAIL_ACCOUNTS_PROC, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readCustomerResponse.has("records")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21854);
		}
		return readCustomerResponse.getJSONArray("records");
	}

	public static void getFIActionTypes(Map<String, String> businessActions, Map<String, String> retailActions,
			DataControllerRequest requestInstance, Result processedResult) throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, String> inputMap = new HashMap<String, String>();

		JSONObject readCustomerResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.FEATUREACTIONROLETYPE_READ, inputMap, null, requestInstance));
		if (readCustomerResponse == null || !readCustomerResponse.has(FabricConstants.OPSTATUS)
				|| readCustomerResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerResponse.has("featureactionroletype")) {
			processedResult
					.addParam(new Param("FailureReason", String.valueOf(readCustomerResponse), FabricConstants.STRING));
			throw new ApplicationException(ErrorCodeEnum.ERR_21854);
		}

		JSONArray actions = readCustomerResponse.getJSONArray("featureactionroletype");
		for (int i = 0; i < actions.length(); i++) {
			JSONObject record = actions.getJSONObject(i);
			String role = record.getString("RoleType_id");
			String action = record.getString("Action_id");
			if (role.equalsIgnoreCase("TYPE_ID_BUSINESS")) {
				businessActions.put(action, role);
			} else {
				retailActions.put(action, role);
			}
		}
	}

}

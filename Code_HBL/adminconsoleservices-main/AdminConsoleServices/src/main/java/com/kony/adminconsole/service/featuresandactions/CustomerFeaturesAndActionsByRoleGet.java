package com.kony.adminconsole.service.featuresandactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.FeatureandActionHandler;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerFeaturesAndActionsByRoleGet implements JavaService2 {
	private static final String INPUT_USERNAME = "username";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();

		try {
			String username = requestInstance.getParameter(INPUT_USERNAME);
			JSONArray finalPermissions = new JSONArray();
			Map<String, ArrayList<String>> actionToGroups = new HashMap<>();
			Map<String, Integer> featureActionsCount = new HashMap<>();
			String customerId = null;
			if (StringUtils.isBlank(username)) {
				try {

					JSONObject getResponseJSON = DBPServices.computeCustomerBasicInformation(customerId, username,
							requestInstance);
					if (getResponseJSON != null && getResponseJSON.has(FabricConstants.OPSTATUS)
							&& getResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
							&& getResponseJSON.has("customerbasicinfo_view")) {
						JSONObject customerViewJson = getResponseJSON.getJSONObject("customerbasicinfo_view");
						customerId = customerViewJson.optString("Customer_id");
					}
				} catch (DBPAuthenticationException dbpException) {
					alert.prepareError("DBP login failed. " + dbpException.getMessage()).log();
					ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
					processedResult
							.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
					return processedResult;
				}

			}
			JSONArray allActions = FeatureandActionHandler.getAllFeatureActions(null, requestInstance, processedResult);
			JSONArray updateActions = new JSONArray(allActions.toString());
			Map<String, JSONObject> actionDetails = FeatureandActionHandler.getAllActionDetails(updateActions,
					featureActionsCount);
			JSONArray customergroups = FeatureandActionHandler.getCustomerGroups(customerId, requestInstance,
					processedResult);

			// Process all associate roles
			String filter = StringUtils.EMPTY;
			for (Object groupObj : customergroups) {
				JSONObject groupJSONObj = (JSONObject) groupObj;
				String groupType = groupJSONObj.optString("Group_Type_id");
				if (StringUtils.isNotBlank(groupType) && groupType.equalsIgnoreCase("TYPE_ID_BUSINESS")) {
					continue;
				}
				JSONObject group = new JSONObject();
				group.put("id", groupJSONObj.getString("Group_id"));
				group.put("name", groupJSONObj.getString("Group_name"));
				group.put("description", groupJSONObj.getString("Group_Desc"));
				group.put("status", groupJSONObj.getString("GroupStatus_id"));
				group.put("features", new JSONArray());
				finalPermissions.put(group);
				if (StringUtils.isNotBlank(filter)) {
					filter += " or ";
				}
				filter += "Group_id eq '" + groupJSONObj.getString("Group_id") + "'";
			}
			JSONArray groupactions = new JSONArray();
			if (StringUtils.isNotBlank(filter)) {
				// Process indirect permissions via roles
				groupactions = FeatureandActionHandler.getGroupActionLimits(filter, requestInstance, processedResult);
			}
			JSONArray current_features = null;
			for (int i = 0; i < groupactions.length(); i++) {
				JSONObject current_object = groupactions.getJSONObject(i);
				for (Object grpObj : finalPermissions) {
					JSONObject groupObj = (JSONObject) grpObj;
					if (StringUtils.isNotBlank(groupObj.optString("id"))
							&& groupObj.optString("id").equals(current_object.getString("Group_id"))) {
						current_features = groupObj.getJSONArray("features");
					}
				}
				String current_action = current_object.getString("Action_id");
				ArrayList<String> groups = new ArrayList<String>();

				String current_group = current_object.getString("Group_id");
				if (!actionToGroups.containsKey(current_action)) {
					groups.add(current_group);
					actionToGroups.put(current_action, groups);

				} else {
					ArrayList<String> groupsArray = actionToGroups.get(current_action);
					if (!groupsArray.contains(current_group)) {
						groupsArray.add(current_group);
					}
				}
				processIndirectPermission(current_object, current_features, actionDetails, featureActionsCount);
			}

			// constructFinalResult(finalPermissions, processedResult, actionDetails);
			Dataset groupDataset = CommonUtilities.constructDatasetFromJSONArray(finalPermissions);
			groupDataset.setId("groups");
			processedResult.addDataset(groupDataset);
			// Process direct permissions
			JSONArray customerDirectPermissions = FeatureandActionHandler.getCustomerDirectPermissions(customerId,
					requestInstance, processedResult);
			JSONObject directActions = new JSONObject();

			if (customerDirectPermissions != null) {
				for (int i = 0; i < customerDirectPermissions.length(); i++) {
					JSONObject action = customerDirectPermissions.getJSONObject(i);
					String current_action = action.getString("Action_id");
					String current_isallowed = action.getString("isAllowed");
					String current_limit = action.optString("LimitType_id");
					String current_limit_value = action.optString("value");
					if (current_isallowed.equalsIgnoreCase("true")) {

						if (!directActions.has(current_action)) {
							JSONObject actionObject = new JSONObject();
							actionObject.put("code", current_action);

							if (StringUtils.isNotBlank(current_limit)) {
								JSONObject limits = new JSONObject();
								limits.put(current_limit, current_limit_value);
								actionObject.put("limits", limits);
							}
							directActions.put(current_action, actionObject);
						} else {
							// update existing action
							JSONObject actionObject = directActions.getJSONObject(current_action);

							if (StringUtils.isNotBlank(current_limit)) {
								JSONObject limits = new JSONObject();
								if (actionObject.has("limits")) {
									limits = actionObject.getJSONObject("limits");
								}
								if (limits.has(current_limit)) {
									// Choose min of both
									current_limit_value = String
											.valueOf(Math.min(Double.parseDouble(current_limit_value),
													Double.parseDouble(limits.getString(current_limit))));
								}
								limits.put(current_limit, current_limit_value);
								actionObject.put("limits", limits);
							}
						}
					}
				}
			}
			Dataset otherFeaturesAndActions = FeatureandActionHandler.getOtherFeaturesAndActions(allActions,
					actionToGroups, directActions, featureActionsCount);
			if (otherFeaturesAndActions != null) {
				otherFeaturesAndActions.setId("otherFeaturesAndActions");
				processedResult.addDataset(otherFeaturesAndActions);
			}
			return processedResult;

		} catch (ApplicationException ae) {
			alert.prepareError("Unexpected error", ae).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
		} catch (Exception e) {
			alert.prepareError("Unexpected error", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
		}
		return processedResult;
	}

	private void processIndirectPermission(JSONObject action, JSONArray features, Map<String, JSONObject> actionDetails,
			Map<String, Integer> featureActionsCount) {

		String current_action = action.getString("Action_id");
		String current_limit = action.optString("LimitType_id");
		String current_limit_value = action.optString("value");
		JSONObject objFromAllActions = actionDetails.get(current_action);
		String current_feature = objFromAllActions.getString("Feature_id");
		boolean featureNotFound = true;
		for (Object ftrObject : features) {
			JSONObject featureObject = (JSONObject) ftrObject;
			if (StringUtils.isNotBlank(featureObject.optString("id"))
					&& featureObject.optString("id").equals(current_feature)) {
				featureNotFound = false;
				JSONArray actionObjectArray = featureObject.optJSONArray("actions");
				boolean actionNotFound = true;
				for (Object actionObject : actionObjectArray) {
					JSONObject actionJSONObject = (JSONObject) actionObject;
					if (StringUtils.isNotBlank(actionJSONObject.optString("id"))
							&& actionJSONObject.optString("id").equals(current_action)) {
						if (StringUtils.isNotBlank(current_limit)) {
							JSONObject limits = new JSONObject();
							if (actionJSONObject.has("limits")) {
								limits = actionJSONObject.getJSONObject("limits");
							}
							if (limits.has(current_limit)) {
								current_limit_value = String.valueOf(Math.min(Double.parseDouble(current_limit_value),
										Double.parseDouble(limits.getString(current_limit))));
							}
							limits.put(current_limit, current_limit_value);
							actionJSONObject.put("limits", limits);
						}
						actionNotFound = false;
						break;
					}
				}
				if (actionNotFound == true) {
					JSONObject acObj = new JSONObject();
					acObj.put("id", current_action);
					acObj.put("FeatureId", current_feature);
					assignActionDetails(objFromAllActions, acObj, current_limit, current_limit_value);
					actionObjectArray.put(acObj);

				}
				break;
			}
		}
		if (featureNotFound == true) {
			JSONObject ftObject = new JSONObject();
			JSONArray actionArray = new JSONArray();
			ftObject.put("totalActions", featureActionsCount.get(current_feature));
			ftObject.put("id", current_feature);
			ftObject.put("name", objFromAllActions.optString("feature_name"));
			ftObject.put("description", objFromAllActions.optString("feature_description"));
			ftObject.put("status", objFromAllActions.optString("feature_status_id"));
			ftObject.put("type", objFromAllActions.optString("feature_Type_id"));
			ftObject.put("displaySequence", objFromAllActions.optString("feature_displaysequence"));
			JSONObject acObj = new JSONObject();
			acObj.put("id", current_action);
			acObj.put("FeatureId", current_feature);
			assignActionDetails(objFromAllActions, acObj, current_limit, current_limit_value);
			actionArray.put(acObj);
			ftObject.put("actions", actionArray);
			features.put(ftObject);
		}
	}

	private void assignActionDetails(JSONObject objFromAllActions, JSONObject acObj, String current_limit,
			String current_limit_value) {
		// TODO Auto-generated method stub
		acObj.put("name", objFromAllActions.optString("action_name"));
		acObj.put("description", objFromAllActions.optString("action_description"));
		acObj.put("type", objFromAllActions.optString("action_Type_id"));
		acObj.put("isMFAApplicable", objFromAllActions.optString("isMFAApplicable"));
		acObj.put("isAccountLevel", objFromAllActions.optString("isAccountLevel"));
		acObj.put("isPrimary", objFromAllActions.optString("isPrimary"));
		acObj.put("displaySequence", objFromAllActions.optString("action_displaysequence"));
		if (StringUtils.isNotBlank(objFromAllActions.optString("action_dependency"))) {
			acObj.put("dependency", objFromAllActions.optString("action_dependency"));
		}
		if (StringUtils.isNotBlank(current_limit)) {
			JSONObject limits = new JSONObject();
			limits.put(current_limit, current_limit_value);
			acObj.put("limits", limits);
		}
	}

	private static Record customconstructRecordFromJSONObject(JSONObject jsonObject,
			Map<String, JSONObject> actionDetails) {
		Record response = new Record();
		if (jsonObject == null || jsonObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (jsonObject.get(key) instanceof Integer) {
				Param param = new Param(key, jsonObject.get(key).toString(), FabricConstants.INT);
				response.addParam(param);

			} else if (jsonObject.get(key) instanceof Boolean) {
				Param param = new Param(key, jsonObject.get(key).toString(), FabricConstants.BOOLEAN);
				response.addParam(param);

			} else if (jsonObject.get(key) instanceof JSONArray) {
				Dataset dataset = customconstructDatasetFromJSONArray(jsonObject.getJSONArray(key), actionDetails);
				dataset.setId(key);
				response.addDataset(dataset);
			} else if (jsonObject.get(key) instanceof JSONObject) {
				// convert object to array for features and actions
				if (key == "features" || key == "actions") {
					JSONArray arrayOfValues = new JSONArray();
					Iterator<String> objectkeys = jsonObject.getJSONObject(key).keys();
					while (objectkeys.hasNext()) {
						arrayOfValues.put(jsonObject.getJSONObject(key).getJSONObject(objectkeys.next()));
					}
					Dataset dataset = customconstructDatasetFromJSONArray(arrayOfValues, actionDetails);
					dataset.setId(key);
					response.addDataset(dataset);
				} else {
					Record record = customconstructRecordFromJSONObject(jsonObject.getJSONObject(key), actionDetails);
					record.setId(key);
					response.addRecord(record);
				}

			} else {
				Param param = new Param(key, jsonObject.optString(key), FabricConstants.STRING);
				response.addParam(param);
			}
		}

		return response;
	}

	private static Dataset customconstructDatasetFromJSONArray(JSONArray JSONArray,
			Map<String, JSONObject> actionDetails) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			JSONObject obj = (JSONObject) JSONArray.get(count);
			if (obj.has("limits")) {
				processFILevelLimits(obj, actionDetails);
			}
			Record record = customconstructRecordFromJSONObject(obj, actionDetails);
			dataset.addRecord(record);
		}
		return dataset;
	}

	private static void processFILevelLimits(JSONObject obj, Map<String, JSONObject> actionDetails) {
		JSONObject limitsAtFILevel;
		if (actionDetails.get(obj.getString("code")).has("limits")) {
			limitsAtFILevel = actionDetails.get(obj.getString("code")).getJSONObject("limits");
		} else {
			return;
		}

		JSONObject customerlimits = obj.getJSONObject("limits");

		Iterator<String> keys = limitsAtFILevel.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = "";
			if (customerlimits.has(key)) {
				value = String.valueOf(Math.min(Double.parseDouble(customerlimits.getString(key)),
						Double.parseDouble(limitsAtFILevel.getString(key))));

			} else {
				value = limitsAtFILevel.getString(key);
			}
			customerlimits.put(key, value);
		}
	}

}

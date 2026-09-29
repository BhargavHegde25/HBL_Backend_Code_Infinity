package com.kony.adminconsole.service.featuresandactions;

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
import com.konylabs.middleware.dataobject.Result;

/*
  TODO:
	1. Read Customer direct permissions: default limits needs to be applied

 */
public class CustomerFeaturesAndActionsCombinedGet implements JavaService2 {
	private static final String INPUT_USERNAME = "username";
	// private static final String INPUT_ORGANISATION_ID = "organisationId";
	// private static final String INPUT_ROLE_ID = "roleId";

	private static final String ROLE_BUSINESS = "TYPE_ID_BUSINESS";

	private static final String ROLE_RETAIL = "TYPE_ID_RETAIL";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();

		try {
			String orgId = null;
			String username = requestInstance.getParameter(INPUT_USERNAME);
			JSONArray customergroups = null;
			JSONArray finalPermissions = new JSONArray();
			Map<String, String> businessActions = new HashMap<>();
			Map<String, String> retailActions = new HashMap<>();
			Map<String, Integer> featureActionsCount = new HashMap<>();
			String customerId = null;
			String customerType = null;
			Map<String, JSONObject> companyActions = null;
			JSONArray businessAccounts = new JSONArray();
			JSONArray retailAccounts = new JSONArray();

			if (!StringUtils.isBlank(username)) {
				try {

					JSONObject getResponseJSON = DBPServices.computeCustomerBasicInformation(customerId, username,
							requestInstance);
					if (getResponseJSON != null && getResponseJSON.has(FabricConstants.OPSTATUS)
							&& getResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
							&& getResponseJSON.has("customerbasicinfo_view")) {
						JSONObject customerViewJson = getResponseJSON.getJSONObject("customerbasicinfo_view");
						customerId = customerViewJson.optString("Customer_id");
						orgId = customerViewJson.optString("organisation_id");
//						isCombinedUser = customerViewJson.optString("combinedUserId");
						customerType = customerViewJson.optString("CustomerType_id");
					}
				} catch (DBPAuthenticationException dbpException) {
					alert.prepareError("DBP login failed. " + dbpException.getMessage()).log();
					ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
					processedResult
							.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
					return processedResult;
				}

				customergroups = FeatureandActionHandler.getCustomerGroups(customerId, requestInstance,
						processedResult);
			}
			// Edit flow

			JSONObject dbpaccounts = DBPServices.getAccounts(username, requestInstance);
			// Validate response
			if (dbpaccounts == null || !dbpaccounts.has(FabricConstants.OPSTATUS)
					|| (dbpaccounts.getInt(FabricConstants.OPSTATUS) != 0)) {
				ErrorCodeEnum.ERR_20656.setErrorCode(processedResult);
				return processedResult;
			}
			JSONArray accountsArray = null;

			if (dbpaccounts.has("Accounts")) {
				accountsArray = dbpaccounts.getJSONArray("Accounts");
			} else {
				accountsArray = new JSONArray();
			}
			for (Object account : accountsArray) {
				JSONObject accountObject = (JSONObject) account;
				if (StringUtils.isNotBlank(accountObject.optString("isBusinessAccount"))) {
					String isBusinessAccount = accountObject.optString("isBusinessAccount");
					if (isBusinessAccount.equals("true")) {
						businessAccounts.put(accountObject);
					} else {
						retailAccounts.put(accountObject);
					}
				} else if (StringUtils.isNotBlank(customerType) && customerType.equals("TYPE_ID_RETAIL")) {
					retailAccounts.put(accountObject);
				} else {
					businessAccounts.put(accountObject);
				}
			}
			companyActions = FeatureandActionHandler.getCompanyActions(orgId, requestInstance, processedResult);
			/*
			 * businessAccounts =
			 * FeatureandActionHandler.getBusinessAccountsOfCustomer(customerBean.getId(),
			 * requestInstance, processedResult);
			 */
			FeatureandActionHandler.getFIActionTypes(businessActions, retailActions, requestInstance, processedResult);
			/*
			 * retailAccounts =
			 * FeatureandActionHandler.getRetailAccountsOfCustomer(customerBean.getId(),
			 * requestInstance, processedResult);
			 */
			JSONArray allActions = FeatureandActionHandler.getAllFeatureActions(null, requestInstance, processedResult);
			Map<String, JSONObject> actionDetails = FeatureandActionHandler.getAllActionDetails(allActions,
					featureActionsCount);

			// Process all associate roles
			Map<String, String> groupTypeMap = new HashMap<>();
			String filter = StringUtils.EMPTY;
			JSONObject group = new JSONObject();
			group.put("retailFeatures", new JSONArray());
			group.put("businessFeatures", new JSONArray());
			for (Object groupObj : customergroups) {
				JSONObject groupJSONObj = (JSONObject) groupObj;
				groupTypeMap.put(groupJSONObj.optString("Group_id"), groupJSONObj.optString("Group_Type_id"));

				if (groupJSONObj.optString("Group_Type_id").equalsIgnoreCase(ROLE_RETAIL)) {
					if (retailAccounts != null) {
						JSONArray accounts = new JSONArray();
						for (int i = 0; i < retailAccounts.length(); i++) {
							JSONObject account = new JSONObject();
							account.put("id", retailAccounts.optJSONObject(i).optString("accountID"));
							account.put("name", retailAccounts.optJSONObject(i).optString("nickName"));
							account.put("features", new JSONArray());
							accounts.put(account);
						}
						group.put("retailAccounts", accounts);
					}
				}
				if (groupJSONObj.optString("Group_Type_id").equalsIgnoreCase(ROLE_BUSINESS)) {
					if (businessAccounts != null) {
						JSONArray accounts = new JSONArray();
						for (int i = 0; i < businessAccounts.length(); i++) {
							JSONObject account = new JSONObject();
							account.put("id", businessAccounts.optJSONObject(i).optString("accountID"));
							account.put("name", businessAccounts.optJSONObject(i).optString("nickName"));
							account.put("features", new JSONArray());
							accounts.put(account);
						}
						group.put("businessAccounts", accounts);
					}
				}

				if (StringUtils.isNotBlank(filter)) {
					filter += " or ";
				}
				filter += "Group_id eq '" + groupJSONObj.optString("Group_id") + "'";
			}
			finalPermissions.put(group);
			// Process indirect permissions via roles
			JSONArray groupactions = new JSONArray();
			if (StringUtils.isNotBlank(filter)) {
				groupactions = FeatureandActionHandler.getGroupActionLimits(filter, requestInstance, processedResult);
			}
			JSONArray current_features = null;
			JSONArray current_features_business = null;
			for (int i = 0; i < groupactions.length(); i++) {
				String current_group_type = groupTypeMap.get(groupactions.optJSONObject(i).optString("Group_id"));

				for (Object grpObj : finalPermissions) {
					JSONObject groupObj = (JSONObject) grpObj;
					if (current_group_type.equalsIgnoreCase(ROLE_RETAIL)) {
						JSONObject current_object = groupactions.optJSONObject(i);
						String current_action = current_object.optString("Action_id");
						if (actionDetails.get(current_action).optString("isAccountLevel").equalsIgnoreCase("TRUE")) {

							if (groupObj.has("retailAccounts")) {
								JSONArray accArray = groupObj.optJSONArray("retailAccounts");
								for (Object accObj : accArray) {
									JSONObject accountObj = (JSONObject) accObj;
									if (StringUtils.isNotBlank(accountObj.optString("features"))) {
										current_features = accountObj.optJSONArray("features");
										processIndirectPermission(current_object, current_features, actionDetails,
												featureActionsCount);
									}
								}
							}
						} else {
							current_features = groupObj.optJSONArray("retailFeatures");
							processIndirectPermission(current_object, current_features, actionDetails,
									featureActionsCount);
						}
					} else if (current_group_type.equalsIgnoreCase(ROLE_BUSINESS)) {
						JSONObject current_object_business = groupactions.optJSONObject(i);
						String current_action_business = current_object_business.optString("Action_id");
						if (!companyActions.containsKey(current_action_business)) {
							continue;
						}
						JSONObject orgaction_object = companyActions.get(current_action_business);
						if (actionDetails.get(current_action_business).optString("isAccountLevel")
								.equalsIgnoreCase("TRUE")) {

							if (groupObj.has("businessAccounts")) {
								JSONArray accArray = groupObj.optJSONArray("businessAccounts");
								for (Object accObj : accArray) {
									JSONObject accountObj = (JSONObject) accObj;
									if (StringUtils.isNotBlank(accountObj.optString("features"))) {
										current_features_business = accountObj.optJSONArray("features");
										processIndirectPermissionBusiness(current_object_business,
												current_features_business, actionDetails, featureActionsCount,
												orgaction_object);
									}
								}
							}

						} else {
							current_features_business = groupObj.optJSONArray("businessFeatures");
							processIndirectPermissionBusiness(current_object_business, current_features_business,
									actionDetails, featureActionsCount, orgaction_object);
						}
					}
				}
			}
			JSONArray customerDirectPermissions = FeatureandActionHandler.getCustomerDirectPermissions(customerId,
					requestInstance, processedResult);

			if (customerDirectPermissions != null) {
				for (int i = 0; i < customerDirectPermissions.length(); i++) {
					JSONObject action = customerDirectPermissions.optJSONObject(i);
					String current_action = action.optString("Action_id");
					String current_account = action.optString("Account_id");
					for (Object grpObj : finalPermissions) {
						JSONObject groupObj = (JSONObject) grpObj;
						if (actionDetails.get(current_action).optString("isAccountLevel").equalsIgnoreCase("TRUE")
								&& StringUtils.isNotBlank(current_account)) {

							if (groupObj.has("businessAccounts")) {
								JSONArray accArray = groupObj.optJSONArray("businessAccounts");
								for (Object accObj : accArray) {
									JSONObject accountObj = (JSONObject) accObj;
									if (current_account.equals(accountObj.optString("id"))) {
										if (StringUtils.isNotBlank(accountObj.optString("features"))) {
											current_features_business = accountObj.optJSONArray("features");
											processDirectPermission(action, current_features_business, actionDetails);
										}
									}
								}
							}
						} else if (actionDetails.get(current_action).optString("isAccountLevel")
								.equalsIgnoreCase("TRUE") && StringUtils.isBlank(current_account)) {

							if (groupObj.has("retailAccounts")) {
								JSONArray accArray = groupObj.optJSONArray("retailAccounts");
								for (Object accObj : accArray) {
									JSONObject accountObj = (JSONObject) accObj;
									if (StringUtils.isNotBlank(accountObj.optString("features"))) {
										current_features = accountObj.optJSONArray("features");
										processDirectPermission(action, current_features, actionDetails);
									}
								}
							}
						} else {
							if (retailActions.containsKey(current_action)) {
								current_features = groupObj.optJSONArray("retailFeatures");
								processDirectPermission(action, current_features, actionDetails);
							}
							if (businessActions.containsKey(current_action)) {
								current_features_business = groupObj.optJSONArray("businessFeatures");
								processDirectPermission(action, current_features_business, actionDetails);
							}
						}
					}
				}
			}
			Dataset groupDataset = CommonUtilities.constructDatasetFromJSONArray(finalPermissions);
			groupDataset.setId("records");
			processedResult.addDataset(groupDataset);
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

		String current_action = action.optString("Action_id");
		String current_limit = action.optString("LimitType_id");
		String current_limit_value = action.optString("value");
		if (StringUtils.isNotBlank(current_limit)) {
			current_limit_value = String.valueOf(Double.parseDouble(current_limit_value));
		}
		JSONObject objFromAllActions = actionDetails.get(current_action);
		String current_feature = objFromAllActions.optString("Feature_id");
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
								limits = actionJSONObject.optJSONObject("limits");
							}
							if (limits.has(current_limit)) {
								current_limit_value = String.valueOf(Math.min(Double.parseDouble(current_limit_value),
										Double.parseDouble(limits.optString(current_limit))));
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
			ftObject.put("isAssigned", "1");

			JSONObject acObj = new JSONObject();
			acObj.put("id", current_action);
			acObj.put("FeatureId", current_feature);
			assignActionDetails(objFromAllActions, acObj, current_limit, current_limit_value);
			actionArray.put(acObj);
			ftObject.put("actions", actionArray);
			features.put(ftObject);
		}
	}

	private void processIndirectPermissionBusiness(JSONObject action, JSONArray features,
			Map<String, JSONObject> actionDetails, Map<String, Integer> featureActionsCount, JSONObject orgaction) {

		String current_action = action.optString("Action_id");
		String current_limit = action.optString("LimitType_id");
		String current_limit_value = action.optString("value");
		JSONObject orgaction_limits = orgaction.optJSONObject("limits");
		String orgaction_limit_value;
		if (StringUtils.isNotBlank(current_limit) && orgaction_limits != null && orgaction_limits.has(current_limit)) {
			orgaction_limit_value = orgaction_limits.optString(current_limit);
			current_limit_value = String.valueOf(
					Math.min(Double.parseDouble(current_limit_value), Double.parseDouble(orgaction_limit_value)));
		}
		JSONObject objFromAllActions = actionDetails.get(current_action);
		String current_feature = objFromAllActions.optString("Feature_id");
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
								limits = actionJSONObject.optJSONObject("limits");
							}
							if (limits.has(current_limit)) {
								current_limit_value = String.valueOf(Math.min(Double.parseDouble(current_limit_value),
										Double.parseDouble(limits.optString(current_limit))));
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
					assignActionDetailsBusiness(objFromAllActions, acObj, current_limit, current_limit_value);
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
			ftObject.put("isAssigned", "0");
			ftObject.put("numberOfActions", "0");
			JSONObject acObj = new JSONObject();
			acObj.put("id", current_action);
			acObj.put("FeatureId", current_feature);
			assignActionDetailsBusiness(objFromAllActions, acObj, current_limit, current_limit_value);
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
		acObj.put("isAssigned", "1");
		if (StringUtils.isNotBlank(objFromAllActions.optString("action_dependency"))) {
			acObj.put("dependency", objFromAllActions.optString("action_dependency"));
		}

		if (StringUtils.isNotBlank(current_limit)) {
			JSONObject limits = new JSONObject();
			limits.put(current_limit, current_limit_value);
			acObj.put("limits", limits);
		}
	}

	private void assignActionDetailsBusiness(JSONObject objFromAllActions, JSONObject acObj, String current_limit,
			String current_limit_value) {
		// TODO Auto-generated method stub
		acObj.put("name", objFromAllActions.optString("action_name"));
		acObj.put("description", objFromAllActions.optString("action_description"));
		acObj.put("type", objFromAllActions.optString("action_Type_id"));
		acObj.put("isMFAApplicable", objFromAllActions.optString("isMFAApplicable"));
		acObj.put("isAccountLevel", objFromAllActions.optString("isAccountLevel"));
		acObj.put("isPrimary", objFromAllActions.optString("isPrimary"));
		acObj.put("displaySequence", objFromAllActions.optString("action_displaysequence"));
		acObj.put("isAssigned", "0");
		if (StringUtils.isNotBlank(objFromAllActions.optString("action_dependency"))) {
			acObj.put("dependency", objFromAllActions.optString("action_dependency"));
		}

		if (StringUtils.isNotBlank(current_limit)) {
			JSONObject limits = new JSONObject();
			limits.put(current_limit, current_limit_value);
			acObj.put("limits", limits);
		}
	}

	private void processDirectPermission(JSONObject action, JSONArray features, Map<String, JSONObject> actionDetails) {

		String current_action = action.optString("Action_id");
		String current_isallowed = action.optString("isAllowed");
		String current_limit = action.optString("LimitType_id");
		String current_limit_value = action.optString("value");
		JSONObject objFromAllActions = actionDetails.get(current_action);
		String current_feature = objFromAllActions.optString("Feature_id");
		for (int i = 0; i < features.length(); i++) {
			JSONObject featureObject = (JSONObject) features.get(i);
			if (StringUtils.isNotBlank(featureObject.optString("id"))
					&& featureObject.optString("id").equals(current_feature)) {
				JSONArray actionObjectArray = featureObject.optJSONArray("actions");
				for (Object actionObject : actionObjectArray) {
					JSONObject actionJSONObject = (JSONObject) actionObject;
					if (StringUtils.isNotBlank(actionJSONObject.optString("id"))
							&& actionJSONObject.optString("id").equals(current_action)) {
						if (current_isallowed.equalsIgnoreCase("true")) {
							if (featureObject.optInt("numberOfActions") == 0) {
								featureObject.put("isAssigned", "1");
							}
							if (actionJSONObject.optString("isAssigned").equals("0")) {
								featureObject.put("numberOfActions", featureObject.optInt("numberOfActions") + 1);
							}
							actionJSONObject.put("isAssigned", "1");
						}

						if (current_isallowed.equalsIgnoreCase("false")) {
							actionJSONObject.put("isAssigned", "0");
						}
						if (StringUtils.isNotBlank(current_limit)) {
							JSONObject limits = new JSONObject();
							if (actionJSONObject.has("limits")) {
								limits = actionJSONObject.optJSONObject("limits");
							}
							if (limits.has(current_limit)) {
								current_limit_value = String.valueOf(Math.min(Double.parseDouble(current_limit_value),
										Double.parseDouble(limits.optString(current_limit))));
							}
							limits.put(current_limit, current_limit_value);
							actionJSONObject.put("limits", limits);
						}
						break;
					}
				}
			}
		}
	}
}

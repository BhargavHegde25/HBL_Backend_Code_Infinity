package com.kony.dbputilities.customersecurityservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.dto.FeatureActionDTO;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganizationEmployeeDetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		JsonObject empViewResponse = new JsonObject();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			String Username = inputParams.get("Username");
			dcRequest.setAttribute("Username", Username);

			String filter = "Username" + DBPUtilitiesConstants.EQUAL + "'" + Username + "'";

			Map<String, String> inputParamsnew = new HashMap<>();
			inputParamsnew.put(DBPUtilitiesConstants.FILTER, filter);
			empViewResponse = HelperMethods.callApiJson(dcRequest, inputParamsnew, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATIONEMPLOYEEDETAILS_GET);
			JSONObject jsonResponse = new JSONObject();
			JsonArray resultsToParse = empViewResponse.get("orgemployeedetails").getAsJsonArray();

			if (resultsToParse.size() == 0) {
				ErrorCodeEnum.ERR_12404.setErrorCode(result);
				return result;
			} else {
				JsonObject response = resultsToParse.get(0).getAsJsonObject();
				String Customer_id = response.get("id").getAsString();
				jsonResponse.put("id", Customer_id);
				jsonResponse.put("Username", response.get("Username").getAsString());
				jsonResponse.put("UserName", response.get("Username").getAsString());
				if (response.has("FirstName")) {
					jsonResponse.put("FirstName", response.get("FirstName").getAsString());
				}
				if (response.has("MiddleName")) {
					jsonResponse.put("MiddleName", response.get("MiddleName").getAsString());
				}
				if (response.has("LastName")) {
					jsonResponse.put("LastName", response.get("LastName").getAsString());
				}
				if (response.has("DateOfBirth")) {
					jsonResponse.put("DateOfBirth", response.get("DateOfBirth").getAsString());
				}
				if (response.has("DrivingLicenseNumber")) {
					jsonResponse.put("DrivingLicenseNumber", response.get("DrivingLicenseNumber").getAsString());
				}
				if (response.has("Ssn")) {
					jsonResponse.put("Ssn", response.get("Ssn").getAsString());
				}
				if (response.has("UserCompany")) {
					jsonResponse.put("UserCompany", response.get("UserCompany").getAsString());
				}
				if (response.has("Lastlogintime")) {
					jsonResponse.put("Lastlogintime", response.get("Lastlogintime").getAsString());
				}
				if (response.has("createdby")) {
					jsonResponse.put("createdby", response.get("createdby").getAsString());
				}

				JSONArray accountsArray = new JSONArray();
				List<String> accountsAdded = new ArrayList<>();

				for (int i = 0; i < resultsToParse.size(); i++) {
					JsonObject account = resultsToParse.get(i).getAsJsonObject();
					JSONObject accountChild = new JSONObject();
					if (account.has("Account_id") && !accountsAdded.contains(account.get("Account_id").getAsString())) {
						accountChild.put("Account_id", account.get("Account_id").getAsString());
						accountChild.put("AccountName",
								(account.has("AccountName")) ? account.get("AccountName").getAsString() : "");
						accountsAdded.add(account.get("Account_id").getAsString());
						accountsArray.put(accountChild);
					}
				}
				jsonResponse.put("accounts", accountsArray);
				Map<String, String> statusMap = HelperMethods.getStatusMap();
				if (response.has("Status")) {
					jsonResponse.put("Status", statusMap.get(response.get("Status").getAsString()));
				}
				Map<String, String> phnEmail = HelperMethods.getCommunicationInfo(Customer_id, dcRequest);
				for (String key : phnEmail.keySet()) {
					jsonResponse.put(key, phnEmail.get(key));
				}
			}
			result = ConvertJsonToResult.convert(jsonResponse.toString());

			processCustomerActionLimits(inputParams, dcRequest, result);
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, result);
		}

		return result;
	}

	private void processCustomerActionLimits(Map<String, String> inputParams, DataControllerRequest dcRequest,
			Result result) {

		JsonObject jsonresponse = LimitsHandler.getCustomerGroupOrganisationActionLimit(inputParams.get("customerId"),
				inputParams.get("organisationId"), "", dcRequest, result);

//		alert.prepareError(jsonresponse).log();
		if (jsonresponse == null) {
			return;
		}

		try {
			JsonArray jsonArray = jsonresponse.get("records").getAsJsonArray();

			Map<String, Map<String, FeatureActionDTO>> customerActionLimitsMap = new HashMap<>();
			Map<String, FeatureActionDTO> featureInformation = new HashMap<>();

			for (int i = 0; i < jsonArray.size(); i++) {

				if (!jsonArray.get(i).getAsJsonObject().has("isAllowedForCustomer")) {
					continue;
				}
				String featureId = jsonArray.get(i).getAsJsonObject().get("featureId").getAsString();
				String actionId = jsonArray.get(i).getAsJsonObject().get("actionId").getAsString();
				String actionType = jsonArray.get(i).getAsJsonObject().get("actionType").getAsString();
				String isAccountLevel = jsonArray.get(i).getAsJsonObject().get("isAccountLevel").getAsString();
				String isActionAllowed = jsonArray.get(i).getAsJsonObject().get("isActionAllowed").getAsString();

				if ("false".equalsIgnoreCase(isActionAllowed))
					continue;
				String isAllowedForCustomer = jsonArray.get(i).getAsJsonObject().get("isAllowedForCustomer")
						.getAsString();
				String actionDescription = jsonArray.get(i).getAsJsonObject().get("actionDescription").getAsString();
				String actionName = jsonArray.get(i).getAsJsonObject().get("actionName").getAsString();

				Map<String, FeatureActionDTO> actionsMap = new HashMap<>();
				if (customerActionLimitsMap.containsKey(featureId))
					actionsMap = customerActionLimitsMap.get(featureId);

				FeatureActionDTO actionDTO = new FeatureActionDTO();

				if ("NON_MONETARY".equalsIgnoreCase(actionType) && "true".equalsIgnoreCase(isActionAllowed)) {
					if (actionsMap.containsKey(actionId)) {
						actionDTO = actionsMap.get(actionId);
					} else {
						actionDTO.setActionId(actionId);
						actionDTO.setIsAccountLevel(isAccountLevel);
						actionDTO.setActiontype(actionType);
						actionDTO.setActionName(actionName);
						actionDTO.setActionDescription(actionDescription);
						if ("true".equalsIgnoreCase(isAccountLevel)) {
							actionDTO.setIsAllowedForCustomer(isActionAllowed);
						} else if ("false".equalsIgnoreCase(isAccountLevel)) {
							actionDTO.setIsAllowedForCustomer(isAllowedForCustomer);
						}
					}
					if ("true".equalsIgnoreCase(isAccountLevel)) {
						if (jsonArray.get(i).getAsJsonObject().has("accountId")
								&& !jsonArray.get(i).getAsJsonObject().get("accountId").isJsonNull()) {
							String accountId = jsonArray.get(i).getAsJsonObject().get("accountId").getAsString();
							if ("true".equalsIgnoreCase(isAllowedForCustomer))
								actionDTO.setEnabledAccountActions(accountId);
							else if ("false".equalsIgnoreCase(isAllowedForCustomer))
								actionDTO.setDisabledAccountLevelActions(accountId);
						}
					}
				} else if ("MONETARY".equalsIgnoreCase(actionType) && "true".equalsIgnoreCase(isActionAllowed)) {
					String accountId = jsonArray.get(i).getAsJsonObject().get("accountId").getAsString();

					if (actionsMap.containsKey(actionId))
						actionDTO = actionsMap.get(actionId);
					else {
						actionDTO.setActionId(actionId);
						actionDTO.setIsActionAllowed(isActionAllowed);
						actionDTO.setIsAccountLevel(isAccountLevel);
						actionDTO.setActiontype(actionType);
						actionDTO.setActionName(actionName);
						actionDTO.setActionDescription(actionDescription);
					}

					if ("true".equalsIgnoreCase(isAllowedForCustomer)) {
						String limitTypeId = jsonArray.get(i).getAsJsonObject().get("limitTypeId").getAsString();
						String value = jsonArray.get(i).getAsJsonObject().get("value").getAsString();
						actionDTO.setAccountActionLimit(accountId, limitTypeId, value);
					} else if ("false".equalsIgnoreCase(isAllowedForCustomer)) {
						actionDTO.setDisabledAccountLevelActions(accountId);
					}
				}
				actionsMap.put(actionId, actionDTO);
				customerActionLimitsMap.put(featureId, actionsMap);
				featureDetailsInformation(featureInformation, jsonArray.get(i).getAsJsonObject());
			}

			Dataset customeraActionLimitsDataset = new Dataset();
			customeraActionLimitsDataset.setId("customerActionLimits");
			for (Entry<String, Map<String, FeatureActionDTO>> featureEntry : customerActionLimitsMap.entrySet()) {

				Record featureRecord = new Record();
				featureRecord.addStringParam("featureId", featureEntry.getKey());
				featureRecord.addStringParam("featureName",
						featureInformation.get(featureEntry.getKey()).getFeatureName());
				featureRecord.addStringParam("featureDescription",
						featureInformation.get(featureEntry.getKey()).getFeatureDescritpion());
				Dataset actionsDataset = new Dataset();
				actionsDataset.setId("actions");
				for (Entry<String, FeatureActionDTO> actionsEntry : featureEntry.getValue().entrySet()) {

					Record actionRecord = new Record();
					actionRecord.addStringParam("actionId", actionsEntry.getKey());

					FeatureActionDTO currFeatureActionDTO = actionsEntry.getValue();
					actionRecord.addParam("actionDescription", currFeatureActionDTO.getActionDescription());
					actionRecord.addParam("actionType", currFeatureActionDTO.getActionType());
					actionRecord.addParam("actionName", currFeatureActionDTO.getActionName());
					actionRecord.addStringParam("isEnabled", currFeatureActionDTO.getIsAllowedForCustomer());
					actionRecord.addStringParam("actionType", currFeatureActionDTO.getActionType());
					actionRecord.addStringParam("isAccountLevel", currFeatureActionDTO.getIsAccountLevel());

					if ("NON_MONETARY".equalsIgnoreCase(currFeatureActionDTO.getActionType())) {

						List<String> enabledAccounts = currFeatureActionDTO.getEnabledAccountActions();
						List<String> disabledAccounts = currFeatureActionDTO.getDisabledAccountLevelActions();

						Dataset accountActionDataset = new Dataset();
						accountActionDataset.setId("accounts");

						if (enabledAccounts != null) {
							for (String accountId : enabledAccounts) {
								Record accountRecord = new Record();
								accountRecord.addStringParam("accountId", accountId);
								accountRecord.addStringParam("isEnabled", "true");
								accountActionDataset.addRecord(accountRecord);
							}
							actionRecord.addDataset(accountActionDataset);
						}

						if (disabledAccounts != null) {

							for (String accountId : disabledAccounts) {
								Record accountRecord = new Record();
								accountRecord.addStringParam("accountId", accountId);
								accountRecord.addStringParam("isEnabled", "false");
								accountActionDataset.addRecord(accountRecord);
							}
							actionRecord.addDataset(accountActionDataset);
						}
					}

					else if ("MONETARY".equalsIgnoreCase(currFeatureActionDTO.getActionType())) {

						Dataset accountActionDataset = new Dataset();
						accountActionDataset.setId("accounts");
						List<String> disabledAccounts = currFeatureActionDTO.getDisabledAccountLevelActions();

						if (disabledAccounts != null) {

							for (String accountId : disabledAccounts) {
								Record accountRecord = new Record();
								accountRecord.addStringParam("accountId", accountId);
								accountRecord.addStringParam("isEnabled", "false");
								accountActionDataset.addRecord(accountRecord);
							}
						}

						if (currFeatureActionDTO.getAccountActionLimit() != null) {
							for (Entry<String, Map<String, String>> accountActionLimits : currFeatureActionDTO
									.getAccountActionLimit().entrySet()) {

								Record accountRecord = new Record();
								accountRecord.addStringParam("accountId", accountActionLimits.getKey());
								accountRecord.addStringParam("isEnabled", "true");
								Dataset limitsRecord = new Dataset();
								limitsRecord.setId("limits");
								for (Entry<String, String> limitsEntry : accountActionLimits.getValue().entrySet()) {
									Record limitRecord = new Record();
									limitRecord.addStringParam("id", limitsEntry.getKey());
									limitRecord.addStringParam("value", limitsEntry.getValue());
									limitsRecord.addRecord(limitRecord);
								}
								accountRecord.addDataset(limitsRecord);
								accountActionDataset.addRecord(accountRecord);
							}
						}
						actionRecord.addDataset(accountActionDataset);
					}
					actionsDataset.addRecord(actionRecord);
				}
				featureRecord.addDataset(actionsDataset);
				customeraActionLimitsDataset.addRecord(featureRecord);
			}
			result.addDataset(customeraActionLimitsDataset);
		} catch (

		Exception e) {
			alert.prepareError("Exception occured wile parsing customer action limits :" + e.getMessage()).log();
		}
		return;
	}

	private void featureDetailsInformation(Map<String, FeatureActionDTO> featureInformation, JsonObject jsonObject) {

		String featureId = jsonObject.get("featureId").getAsString();
		if (featureInformation.containsKey(featureId))
			return;
		FeatureActionDTO featureActionInformation = new FeatureActionDTO();
		featureActionInformation.setFeatureId(featureId);
		featureActionInformation.setFeatureName(jsonObject.get("featureName").getAsString());
		featureActionInformation.setFeatureDescription(jsonObject.get("featureDescription").getAsString());
		featureInformation.put(featureId, featureActionInformation);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws Exception {
		Set<String> userPermissions1 = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
		String Username = inputParams.get("Username");
		if (StringUtils.isBlank(Username)) {
			ErrorCodeEnum.ERR_12402.setErrorCode(result);
			return false;
		}

		Result userRec = HelperMethods.getUserRecordByName(Username, dcRequest);
		String requested_user_id = HelperMethods.getFieldValue(userRec, "id");
		String orgIdOfUserUnderUpdate = HelperMethods.getFieldValue(userRec, "Organization_Id");
		inputParams.put("customerId", requested_user_id);
		inputParams.put("organisationId", orgIdOfUserUnderUpdate);
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String loggedInUserId = loggedInUserInfo.get("customer_id");
		String loggedInUserOrgId = HelperMethods.getOrganizationIDForUser(loggedInUserId, dcRequest);

		if ((!requested_user_id.equalsIgnoreCase(loggedInUserId))
				&& HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
			if (userPermissions.contains("USER_MANAGEMENT")) {
				if (StringUtils.isNotBlank(orgIdOfUserUnderUpdate)
						&& !loggedInUserOrgId.equals(orgIdOfUserUnderUpdate)) {
					ErrorCodeEnum.ERR_12403.setErrorCode(result);
					return false;
				}
			} else {
				ErrorCodeEnum.ERR_12401.setErrorCode(result);
				return false;
			}
		}

		return true;
	}
}

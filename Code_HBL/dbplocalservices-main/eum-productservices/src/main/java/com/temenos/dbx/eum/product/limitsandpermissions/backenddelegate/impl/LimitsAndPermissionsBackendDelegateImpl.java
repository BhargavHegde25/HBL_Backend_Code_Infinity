package com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class LimitsAndPermissionsBackendDelegateImpl implements LimitsAndPermissionsBackendDelegate {

	@Override
	public boolean addFeaturesToContract(ActionLimitsDTO actionLimit, Map<String, Object> headerMap, JsonObject log) {

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(InfinityConstants.contractId, actionLimit.getContractId());
		inputParams.put(InfinityConstants.coreCustomerId, actionLimit.getCoreCustomerId());
		inputParams.put(InfinityConstants.featureId, actionLimit.getFeatureId());
		inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
		inputParams.put(InfinityConstants.isNewFeature, actionLimit.getIsNewFeature());
		inputParams.put(InfinityConstants.LegalEntityId, actionLimit.getCompanyLegalUnit());
		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.CONTRACTFEATURES_CREATE);
		if (!jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTFEATURES)) {
			log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
			return false;
		}
		JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTFEATURES);
		if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addActionsToContract(ActionLimitsDTO actionLimit, Map<String, Object> headerMap, JsonObject log, Set<String> contractCustomerAccounts) {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(InfinityConstants.contractId, actionLimit.getContractId());
		inputParams.put(InfinityConstants.coreCustomerId, actionLimit.getCoreCustomerId());
		inputParams.put(InfinityConstants.featureId, actionLimit.getFeatureId());
		inputParams.put(InfinityConstants.actionId, actionLimit.getActionId());
		inputParams.put(InfinityConstants.isNewAction, actionLimit.getIsNewAction());
		inputParams.put(InfinityConstants.LegalEntityId, actionLimit.getCompanyLegalUnit());
		JsonObject jsonObject = new JsonObject();	
		if (actionLimit.isAccountLevel()) {
			for (String account : contractCustomerAccounts) {
				inputParams.put(InfinityConstants.accountId, account);
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.ACCOUNTLEVELACTIONLIMIT_CREATE);
			}
			inputParams.remove(InfinityConstants.accountId);
		}
		if (!actionLimit.isMonetory()) {
			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CONTRACTACTIONLIMIT_CREATE);
		} else {
			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			inputParams.put(InfinityConstants.limitTypeId, InfinityConstants.MAX_TRANSACTION_LIMIT);
			inputParams.put(InfinityConstants.value, actionLimit.getMaxTransactionLimitValue());
			jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CONTRACTACTIONLIMIT_CREATE);
			if (!jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)) {
				log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
				return false;
			}
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT);
			if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
				log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
				return false;
			}

			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			inputParams.put(InfinityConstants.limitTypeId, InfinityConstants.DAILY_LIMIT);
			inputParams.put(InfinityConstants.value, actionLimit.getDailyLimitValue());
			jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CONTRACTACTIONLIMIT_CREATE);
			if (!jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)) {
				log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
				return false;
			}
			jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT);
			if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
				log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
				return false;
			}

			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			inputParams.put(InfinityConstants.limitTypeId, InfinityConstants.WEEKLY_LIMIT);
			inputParams.put(InfinityConstants.value, actionLimit.getWeeklyLimitValue());
			jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CONTRACTACTIONLIMIT_CREATE);
			
		}
		
		if (!jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)) {
			log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
			return false;
		}
		JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT);
		if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
			log.add(ErrorCodeEnum.ERROR_MESSAGE_KEY, jsonObject);
			return false;
		}

		return true;
	}

	@Override
	public boolean addActionsToCustomer(ActionLimitsDTO actionLimit, Map<String, Object> headerMap) {
		Map<String, Object> inputParams = new HashMap<String, Object>();

		inputParams.put(InfinityConstants.Customer_id, actionLimit.getCustomerId());
		inputParams.put(InfinityConstants.contractId, actionLimit.getContractId());
		inputParams.put(InfinityConstants.coreCustomerId, actionLimit.getCoreCustomerId());
		inputParams.put(InfinityConstants.featureId, actionLimit.getFeatureId());
		inputParams.put(InfinityConstants.Action_id, actionLimit.getActionId());
		inputParams.put(InfinityConstants.isAllowed, "1");
		inputParams.put(InfinityConstants.RoleType_id, actionLimit.getRoleId());
		inputParams.put("companyLegalUnit", actionLimit.getCompanyLegalUnit());
		if (!actionLimit.isMonetory() && !actionLimit.isAccountLevel()) {
			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CUSTOMERACTION_CREATE);
			if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
				return false;
			}
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
			if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
				return false;
			}
		} else if (actionLimit.isAccountLevel() || actionLimit.isMonetory()) {
			inputParams.put(InfinityConstants.Account_id, actionLimit.getAccountId());
			if (!actionLimit.isMonetory()) {
					inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
					JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
							URLConstants.CUSTOMERACTION_CREATE);
					if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
						return false;
					}
					JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
					if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
						return false;
					}
			} else {
				inputParams.put(InfinityConstants.limitGroupId, actionLimit.getLimitGroupId());
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.MAX_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxTransactionLimitValue());
				JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.AUTO_DENIED_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.PRE_APPROVED_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxTransactionLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean addActionsToCustomRole(ActionLimitsDTO actionLimit, Map<String, Object> headerMap) {
		Map<String, Object> inputParams = new HashMap<String, Object>();

		inputParams.put(InfinityConstants.customRole_id, actionLimit.getCustomRoleId());
		inputParams.put(InfinityConstants.contractId, actionLimit.getContractId());
		inputParams.put(InfinityConstants.coreCustomerId, actionLimit.getCoreCustomerId());
		inputParams.put(InfinityConstants.featureId, actionLimit.getFeatureId());
		inputParams.put(InfinityConstants.action_id, actionLimit.getActionId());
		inputParams.put(InfinityConstants.isAllowed, "1");

		if (!actionLimit.isMonetory() && !actionLimit.isAccountLevel()) {
			inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.CUSTOMERACTION_CREATE);
			if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
				return false;
			}
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
			if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
				return false;
			}
		} else if (actionLimit.isAccountLevel()) {
			inputParams.put(InfinityConstants.account_id, actionLimit.getAccountId());
			if (!actionLimit.isMonetory()) {
					inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
					JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
							URLConstants.CUSTOMERACTION_CREATE);
					if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
						return false;
					}
					JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
					if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
						return false;
					}
			} else {
				inputParams.put(InfinityConstants.limitGroupId, actionLimit.getLimitGroupId());
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.MAX_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxTransactionLimitValue());
				JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitTypeId, InfinityConstants.AUTO_DENIED_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.PRE_APPROVED_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, "0.0");
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}

				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxTransactionLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMROlEACTIONLIMIT_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}
			}
		}

		return true;
	}
}

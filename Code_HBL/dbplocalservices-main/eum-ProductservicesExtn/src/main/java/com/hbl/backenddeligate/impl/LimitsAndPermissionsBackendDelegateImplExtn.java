package com.hbl.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbl.dto.ActionLimitsDTOExtn;
import com.hbl.resource.constants.HBLConstants;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.impl.LimitsAndPermissionsBackendDelegateImpl;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class LimitsAndPermissionsBackendDelegateImplExtn extends LimitsAndPermissionsBackendDelegateImpl{
	
	public boolean addActionsToCustomer(ActionLimitsDTOExtn actionLimit, Map<String, Object> headerMap) {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		String minTxVal="1.0";
		String minMBTxVal="1.0";
		if(String.valueOf(actionLimit.getMinTransactionLimitValue()) != null ){
			minTxVal=String.valueOf(actionLimit.getMinTransactionLimitValue());
		}
		if(String.valueOf(actionLimit.getMinMBTransactionLimitValue()) != null ){
			minMBTxVal=String.valueOf(actionLimit.getMinMBTransactionLimitValue());
		}

		inputParams.put(InfinityConstants.Customer_id, actionLimit.getCustomerId());
		inputParams.put(InfinityConstants.contractId, actionLimit.getContractId());
		inputParams.put(InfinityConstants.coreCustomerId, actionLimit.getCoreCustomerId());
		inputParams.put(InfinityConstants.featureId, actionLimit.getFeatureId());
		inputParams.put(InfinityConstants.Action_id, actionLimit.getActionId());
		inputParams.put(InfinityConstants.isAllowed, "1");
		inputParams.put(InfinityConstants.RoleType_id, actionLimit.getRoleId());
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
				inputParams.put(InfinityConstants.value, minTxVal);
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
				inputParams.put(InfinityConstants.value, minTxVal);
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
				inputParams.put(InfinityConstants.value, minTxVal);
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
				
				
				
				//MB Limits
				/*
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.AUTO_DENIED_MB_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyMBLimitValue());
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.PRE_APPROVED_MB_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, minMBTxVal);
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.PRE_APPROVED_MB_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, minMBTxVal);
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.AUTO_DENIED_MB_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyMBLimitValue());
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.MB_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyMBLimitValue());
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.MB_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyMBLimitValue());
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}
				inputParams.put(InfinityConstants.limitGroupId, actionLimit.getLimitGroupId());
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.MB_MAX_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxMBTransactionLimitValue());
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.AUTO_DENIED_MB_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxMBTransactionLimitValue());
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
				inputParams.put(InfinityConstants.LimitType_id, HBLConstants.PRE_APPROVED_MB_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, minMBTxVal);
				jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMERACTION_CREATE);
				if (!jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					return false;
				}
				jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() <= 0) {
					return false;
				}
				*/
				
			}
		}

		return true;
	}


	public boolean addActionsToCustomRole(ActionLimitsDTOExtn actionLimit, Map<String, Object> headerMap) {
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
				
				inputParams.put(InfinityConstants.limitGroupId, actionLimit.getLimitGroupId());
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.MB_MAX_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxMBTransactionLimitValue());
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.MB_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyMBLimitValue());
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.MB_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyMBLimitValue());
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
				inputParams.put(InfinityConstants.limitTypeId, HBLConstants.AUTO_DENIED_MB_DAILY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getDailyMBLimitValue());
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.PRE_APPROVED_MB_DAILY_LIMIT);
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.PRE_APPROVED_MB_WEEKLY_LIMIT);
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.AUTO_DENIED_MB_WEEKLY_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getWeeklyMBLimitValue());
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
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.PRE_APPROVED_MB_TRANSACTION_LIMIT);
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
				
				inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
				inputParams.put(InfinityConstants.limitType_id, HBLConstants.AUTO_DENIED_MB_TRANSACTION_LIMIT);
				inputParams.put(InfinityConstants.value, actionLimit.getMaxMBTransactionLimitValue());
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

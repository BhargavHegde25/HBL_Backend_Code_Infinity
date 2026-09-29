package com.temenos.dbx.product.limitsandpermissions.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.limitsandpermissions.businessdelegate.api.LimitsAndPermissionsBusinessDelegate;
import com.temenos.dbx.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.limitsandpermissions.dto.ContractDTO;
import com.temenos.dbx.product.limitsandpermissions.dto.CustomerGroupDTO;
import com.temenos.dbx.product.limitsandpermissions.resource.api.LimitsAndPermissionsResource;

public class LimitsAndPermissionsResourceImpl implements LimitsAndPermissionsResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	LimitsAndPermissionsBusinessDelegate limitsAndPermissionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(LimitsAndPermissionsBusinessDelegate.class);
	
	@Override
	public Result UpdateServiceDefinitionLimits(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String id = inputParams.get("id");
		if (StringUtils.isBlank(id)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
		}
		JSONArray actionLimits = new JSONArray();
		try {
			actionLimits = new JSONArray(request.getParameter("actionLimits"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
		}
		
		List<String> contracts = _getContractIds(id);
		if (contracts.size() > 0) {
			String contractIds = String.join(",", contracts);
			if (actionLimits.length() > 0) {
				for (int index = 0; index < actionLimits.length(); index++) {
					JSONObject actionObject = actionLimits.optJSONObject(index);
					ActionLimitsDTO actionLimit = _convertToActionLimitDTO(actionObject);
					actionLimit.setServiceDefinitionId(id);
					actionLimit.setContractId(contractIds);
					if(!limitsAndPermissionsBusinessDelegate.editServiceDefinitionLimits(actionLimit)) {
						return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
					}

				}
			}
			JSONArray removedActionsArray = new JSONArray();
			try {
				removedActionsArray = new JSONArray(request.getParameter("removedActions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
			}
			if (removedActionsArray != null && removedActionsArray.length() > 0) {
				List<String> removedActionList = _getRemovedActionsList(removedActionsArray);
				for (String action : removedActionList) {
					String actions[] = action.split("\\.");
					if (!limitsAndPermissionsBusinessDelegate.deleteServiceDefinitionActions(actions[0], contractIds)) {
						return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
					}
				}

			}
		}
		return result;
	}
	
	@Override
	public Result UpdateCustomerRoleLimitsAndPermissions(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String id = inputParams.get("id");
		if (StringUtils.isBlank(id)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
		}
		JSONArray actionLimits = new JSONArray();
		try {
			actionLimits = new JSONArray(request.getParameter("actionLimits"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
		}
		
		List<String> customerIdList = _getCustomerIds(id);
		if (customerIdList.size() > 0) {
			String customerIds = String.join(",", customerIdList);
			if (actionLimits.length() > 0) {
				for (int index = 0; index < actionLimits.length(); index++) {
					JSONObject actionObject = actionLimits.optJSONObject(index);
					ActionLimitsDTO actionLimit = _convertToActionLimitDTO(actionObject);
					actionLimit.setCustomerId(customerIds);
					if(!limitsAndPermissionsBusinessDelegate.editCustomerRoleLimits(actionLimit)) {
						return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
					}

				}
			}
			JSONArray removedActionsArray = new JSONArray();
			try {
				removedActionsArray = new JSONArray(request.getParameter("removedActions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
			}
			if (removedActionsArray != null && removedActionsArray.length() > 0) {
				List<String> removedActionList = _getRemovedActionsList(removedActionsArray);
				for (String action : removedActionList) {
					String actions[] = action.split("\\.");
					if (!limitsAndPermissionsBusinessDelegate.deleteCustomerRoleActions(actions[0], customerIds)) {
						return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
					}
				}

			}
		}
		return result;
	}

	private ActionLimitsDTO _convertToActionLimitDTO(JSONObject actionObject) {
		ActionLimitsDTO actionLimit = new ActionLimitsDTO();
		String actionId = actionObject.optString("id");
		if (StringUtils.isBlank(actionId)) {
			alert.prepareError("Action Id is mandatory input").log();
		}
		actionLimit.setActionId(actionId);
		JSONArray limits = actionObject.getJSONArray("limits");
		if(limits != null && limits.length() > 0) {
			Double minTxVal = 0.0, maxTxVal = 0.0, dailyTxVal = 0.0, weeklyTxVal = 0.0;
			for(int index = 0;index < limits.length(); index++) {
				JSONObject limitObj = limits.optJSONObject(index);
				String type = limitObj.optString("id");
				String value = limitObj.optString("value");
				
				if (StringUtils.equals(type, "MIN_TRANSACTION_LIMIT")) {
					minTxVal = Double.parseDouble(value);
					actionLimit.setMinTransactionLimitValue(minTxVal);
				} else if (StringUtils.equals(type, "MAX_TRANSACTION_LIMIT")) {
					maxTxVal = Double.parseDouble(value);
					actionLimit.setMaxTransactionLimitValue(maxTxVal);
				} else if (StringUtils.equals(type, "DAILY_LIMIT")) {
					dailyTxVal = Double.parseDouble(value);
					actionLimit.setDailyLimitValue(dailyTxVal);
				} else if (StringUtils.equals(type, "WEEKLY_LIMIT")) {
					weeklyTxVal = Double.parseDouble(value);
					actionLimit.setWeeklyLimitValue(weeklyTxVal);
				}
				
			}
		}
		return actionLimit;
	}
	private List<String> _getContractIds (String id) {
		List<ContractDTO> contractList = limitsAndPermissionsBusinessDelegate.fetchContracts(id);
		List<String> contractIdsList = new ArrayList<>();
		if(contractList!=null) {
			contractIdsList = contractList.stream().map(ContractDTO::getId).collect(Collectors.toList());
		}
		return contractIdsList;
	}
	
	private List<String> _getRemovedActionsList(JSONArray actions){
		List<String> actionsList = new ArrayList<>();
		for(int i=0;i<actions.length();i++) {
			actionsList.add(actions.getString(i));
		}
		return actionsList;
	}
	
	private List<String> _getCustomerIds (String id) {
		List<CustomerGroupDTO> customerGroupList = limitsAndPermissionsBusinessDelegate.fetchCustomerIds(id);
		List<String> customerIdList = new ArrayList<>();
		if(customerGroupList != null) {
			for(CustomerGroupDTO customer : customerGroupList ) {
				customerIdList.add(customer.getCustomerId()+"."+customer.getCoreCustomerId());
			}
		}
		return customerIdList;
	}
}

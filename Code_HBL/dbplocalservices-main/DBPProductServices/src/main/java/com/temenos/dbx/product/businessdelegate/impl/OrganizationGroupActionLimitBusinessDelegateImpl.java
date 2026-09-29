package com.temenos.dbx.product.businessdelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.businessdelegate.api.OrganizationGroupActionLimitBusinessDelegate;
import com.temenos.dbx.product.dto.GroupActionsDTO;
import com.temenos.dbx.product.dto.OrganizationActionsDTO;

/**
 * 
 * @author KH2627
 * @version 1.0 Implements the
 *          {@link OrganizationGroupActionLimitBusinessDelegate}
 */

public class OrganizationGroupActionLimitBusinessDelegateImpl implements OrganizationGroupActionLimitBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<GroupActionsDTO> getGroupActionLimits(GroupActionsDTO inputDTO, Map<String, Object> headersMap) {

		Map<String, Object> inputParams = new HashMap<>();

		inputParams.put("_groupId", inputDTO.getGroupId());
		if (StringUtils.isNotBlank(inputDTO.getActionType()))
			inputParams.put("_actionType", inputDTO.getActionType());
		else
			inputParams.put("_actionType", "");

		if (StringUtils.isNotBlank(inputDTO.getActionId()))
			inputParams.put("_actionId", inputDTO.getActionId());
		else
			inputParams.put("_actionId", "");

		inputParams.put("_isOnlyPremissions", "");
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
				URLConstants.GROUP_ACTIONS_PROC);

		List<GroupActionsDTO> groupActionsDTOList = new ArrayList<>();

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
			// exception while fetching the data
			return null;
		}

		if (response.get("records").getAsJsonArray().size() == 0) {
			// no group actions present
			return groupActionsDTOList;
		}
		try {
			groupActionsDTOList = JSONUtils.parseAsList(response.get("records").getAsJsonArray().toString(),
					GroupActionsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occured while parse groupActionLimits :" + e.getMessage()).log();
		}
		return groupActionsDTOList;
	}

	@Override
	public List<OrganizationActionsDTO> getOrganizationActionLimits(OrganizationActionsDTO inputDTO,
			Map<String, Object> headersMap, String url) {

		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("_organizationId", inputDTO.getOrganizationId());
		if (StringUtils.isNotBlank(inputDTO.getActionType()))
			inputParams.put("_actionType", inputDTO.getActionType());
		else
			inputParams.put("_actionType", "");

		if (StringUtils.isNotBlank(inputDTO.getActionId()))
			inputParams.put("_actionId", inputDTO.getActionId());
		else
			inputParams.put("_actionId", "");

		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, url);

		List<OrganizationActionsDTO> organizationActionDTOList = new ArrayList<>();

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
			// exception while fetching the data
			return null;
		}

		if (response.get("records").getAsJsonArray().size() == 0) {
			// no organization action limits
			return organizationActionDTOList;
		}
		try {
			organizationActionDTOList = JSONUtils.parseAsList(response.get("records").getAsJsonArray().toString(),
					OrganizationActionsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occured while parsing organizationActionLimtis :" + e.getMessage()).log();
		}
		return organizationActionDTOList;
	}

	@Override
	public JsonObject getGroupPermissions(GroupActionsDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("_groupId", inputDTO.getGroupId());
		inputParams.put("_actionType", "");
		inputParams.put("_actionId", "");
		inputParams.put("_isOnlyPremissions", "true");

		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
				URLConstants.GROUP_ACTIONS_PROC);

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10732);
		}

		if (response.get("records").getAsJsonArray().size() == 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10732);
		}
		return response;
	}

	@Override
	public JsonObject getCustomerGroupOrganizationPermissions(Map<String, Object> inputParams,
			Map<String, Object> headersMap) throws ApplicationException {
		String customerId = inputParams.get("customerId").toString();
		String organizationId = inputParams.get("organizationId").toString();
		inputParams.clear();
		inputParams.put("_customerId", customerId);
		inputParams.put("_organisationId", organizationId);
		inputParams.put("_isOnlyPremissions", "true");
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
				URLConstants.CUSTOMER_ORG_GROUP_ACTION_LIMITS);

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10733);
		}

		if (response.get("records").getAsJsonArray().size() == 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10733);
		}
		return response;
	}

}

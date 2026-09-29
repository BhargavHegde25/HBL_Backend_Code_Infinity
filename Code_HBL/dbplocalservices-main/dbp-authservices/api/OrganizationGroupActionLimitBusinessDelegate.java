package com.temenos.dbx.product.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.GroupActionsDTO;
import com.temenos.dbx.product.dto.OrganizationActionsDTO;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for OrganizationGroupActionLimitBusinessDelegate
 *          extends {@link BusinessDelegate}
 *
 */

public interface OrganizationGroupActionLimitBusinessDelegate extends BusinessDelegate {

	/**
	 * method to get the group action limits return list of {@link GroupActionsDTO}
	 */
	List<GroupActionsDTO> getGroupActionLimits(GroupActionsDTO inputDTO, Map<String, Object> headersMap);

	/**
	 * method to get the action limits present at both the organization and FI level
	 * return list of {@link OrganizationActionsDTO}
	 */

	List<OrganizationActionsDTO> getOrganizationActionLimits(OrganizationActionsDTO inputDTO,
			Map<String, Object> headersMap, String url);

	/**
	 * 
	 * @param inputParams
	 * @param headersMap
	 * @return groupLevel permissions specific to groupId
	 * @throws ApplicationException
	 */
	JsonObject getGroupPermissions(GroupActionsDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException;

	/**
	 * 
	 * @param inputParams
	 * @param headersMap
	 * @return Business user permissions
	 * @throws ApplicationException
	 */
	JsonObject getCustomerGroupOrganizationPermissions(Map<String, Object> inputParams, Map<String, Object> headersMap)
			throws ApplicationException;
}

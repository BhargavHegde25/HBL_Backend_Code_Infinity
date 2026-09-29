package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonArray;
import com.temenos.dbx.product.dto.CustomRoleActionLimitDTO;
import com.temenos.dbx.product.dto.CustomRoleDTO;

public interface CustomRoleBusinessDelegate extends BusinessDelegate{

	/**
	 * @param companyId
	 * @param headerMap
	 * @return
	 */
	public List<CustomRoleDTO> getAllCustomRoles(String companyId, String customRoleId, Map<String, Object> headerMap); 

	/**
	 * @param customRoleName
	 * @param parentRole
	 * @param accounts
	 * @param limits
	 * @return
	 */
	public String createCustomRole(CustomRoleDTO customRole, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap);
	
	/**
	 * @param id
	 * @param accounts
	 * @param createdby
	 * @param features
	 * @param headerMap
	 * @return
	 */
	public String createCustomRoleActionLimits(String id, String createdby, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap);
	
	/**
	 * @param customRoleId
	 * @param headerMap
	 * @return
	 */
	public List<CustomRoleActionLimitDTO> getCustomRoleActionLimits(String customRoleId,
			Map<String, Object> headerMap);
	
	/**
	 * @param customRoleId
	 * @param headerMap
	 * @return
	 */
	public boolean deleteCustomRole(String customRoleId, Map<String, Object> headerMap);
	
	/**
	 * @param comapyId
	 * @param groupId
	 * @param headerMap
	 * @return
	 */
	public Map<String , HashMap<String, ArrayList<Double>>> getFiOrgGroupLevelActionLimits(String companyId,
			String groupId, Map<String, Object> headerMap);
	
	/**
	 * @param request
	 * @param headerMap
	 * @return
	 */
	public boolean applyCustomRoleForUsers(Map<String, Object> request, Map<String, Object> headerMap, String konyAuthToken);
	
	/**
	 * @param request
	 * @param headerMap
	 * @return
	 */
	public JsonArray getListOfUserDetails(Map<String, Object> request, Map<String, Object> headerMap, String konyAuthToken);
	
	/**
	 * @param customRole
	 * @param accounts
	 * @param features
	 * @param headerMap
	 * @return
	 */
	String updateCustomRole(CustomRoleDTO customRole, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap);

	/**
	 * @param templateName
	 * @param organizationId
	 * @param headerMap
	 * @return
	 */	
	CustomRoleDTO getCustomRole(String templateName, String organizationId, Map<String, Object> headerMap);
	
}
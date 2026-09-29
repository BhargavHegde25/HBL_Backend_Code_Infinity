package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.CustomRoleActionLimitDTO;
import com.temenos.dbx.product.dto.CustomRoleDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomRoleBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomRoleBusinessDelegateImpl implements CustomRoleBusinessDelegate{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<CustomRoleDTO> getAllCustomRoles(String companyId, String customRoleId, Map<String, Object> headerMap) {
		
		List<CustomRoleDTO>  customRoles = null;	
		
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("organisationId", companyId);
		
		String roleId = (customRoleId == null) ? "" : customRoleId;
		inputParams.put("customRoleId", roleId);
		
		try {
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.FETCH_ORG_CUSTOM_ROLES_PROC);
		if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
				&& response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
			JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
			customRoles = new ArrayList<CustomRoleDTO>();
			for (JsonElement element : responseArray) {
                	if (element.isJsonObject()) {
                		CustomRoleDTO dto =
                            (CustomRoleDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                            		CustomRoleDTO.class, true);
                    	if (dto != null) {
                    		customRoles.add(dto);
                    	}
                	}
            	}
			}
		}
		catch (Exception exp) {
		    alert.prepareError("Exception occured while fetching data from custom roles", exp).log();
	    }
		return customRoles;
	}
	
	@Override
	public CustomRoleDTO getCustomRole(String templateName, String organizationId, Map<String, Object> headerMap) {
		CustomRoleDTO customRoleDTO = null;
		Map<String, Object> inputParams = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		boolean isAndAppended = false;
		if (StringUtils.isNoneBlank(templateName)) {
			sb.append("name").append(DBPUtilitiesConstants.EQUAL).append("\'").append(templateName).append("\'");
			isAndAppended = true;
		}
		if (StringUtils.isNoneBlank(organizationId)) {
			if (isAndAppended)
				sb.append(DBPUtilitiesConstants.AND);
			sb.append("organization_id").append(DBPUtilitiesConstants.EQUAL).append(organizationId);
			isAndAppended = true;
		}
		if (StringUtils.isNoneBlank(sb.toString())) {
			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		JsonObject response = new JsonObject();
		try {
			response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.CUSTOM_ROLE_GET);

			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
					&& response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
				JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray();
				customRoleDTO = responseArray.size() > 0
						? (CustomRoleDTO) DTOUtils.loadJsonObjectIntoObject(responseArray.get(0).getAsJsonObject(),
								CustomRoleDTO.class, true)
						: null;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching data from custom roles", e).log();
		}

		return customRoleDTO;
	}	

	@Override
	public String createCustomRole(CustomRoleDTO customRole, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap) {
		
		String id = null;
		try {
			
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("name", customRole.getName());
			input.put("parent_id", customRole.getParent_id());
			input.put("organization_id", customRole.getOrganization_id());
			input.put("createdby", customRole.getCreatedby());
			input.put("status_id", DBPUtilitiesConstants.SID_ACTIVE);
			input.put("description", customRole.getDescription());

			CustomRoleDTO existingCustomRole = getCustomRole(customRole.getName(), customRole.getOrganization_id(),
					headerMap);	
			
			if( existingCustomRole != null ) {
				return ErrorConstants.UNIQUE_CUSTOM_ROLE_NAME;
			}
			
			if( accounts.length() == 0 ) {
				return ErrorConstants.EMPTY_ACCOUNT_LIST;
			}

			if(!validateAccounts(accounts, customRole.getOrganization_id(), headerMap)) {
				return ErrorConstants.INVALID_ORG_ACCOUNTS;
			}
			
			if(!checkAndUpdateFiOrgGroupLevelLimits(features, customRole.getOrganization_id(), 
					customRole.getParent_id(), headerMap)) {
				return ErrorConstants.INVALID_TRANSACTION_LIMITS;
			}
			
			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
	                URLConstants.CUSTOM_ROLE_CREATE);
			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
					&& response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
				JsonElement element = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray().get(0);
				CustomRoleDTO dto =
                        (CustomRoleDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                        		CustomRoleDTO.class, true);
				id = dto.getId();				
				String result = createCustomRoleActionLimits(id, customRole.getCreatedby(), accounts, features, headerMap);
				if(result == null) {
					deleteCustomRole(id, headerMap);
					return null;
				}
			}
		}
		catch (Exception exp) {
			alert.prepareError("Exception occured while creating data for custom role table", exp).log();
		}
		return id;
	}

	@Override
	public String createCustomRoleActionLimits(String id, String createdby, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap) {
		
		String result = id;

		try {
			String queryInput = getQueryInputForCreateCustomRoleActionLimits(id, createdby, accounts, features);
			
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("_queryInput", queryInput);
			input.put("_customRoleId", Integer.parseInt(id));
			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
	                URLConstants.CREATE_ORG_CUSTOM_ROLES_PROC);
			if(response.get("opstatus").getAsInt() == 0 && response.get("httpStatusCode").getAsInt() == 0) {
				return result;
			}
			return null;
		}
		catch (Exception exp) {
			alert.prepareError("Exception occured while creating data for custom role action limits", exp).log();
		}
		return result;
	}

	private String getQueryInputForCreateCustomRoleActionLimits(String id, String createdby, JSONArray accounts, JSONArray features) {
		
		List<String> finalQuery = new ArrayList<>();
		int featuresLength = features.length();
		
		for(int i=0;i<featuresLength;i++) {
			JSONObject feature = features.getJSONObject(i);
			JSONArray actions = feature.getJSONArray("Actions");
			int actionsLength = actions.length();
			for(int j=0;j<actionsLength;j++) {
				StringBuilder query = new StringBuilder("");
				JSONObject action = actions.getJSONObject(j);
				query.append(id+",");
				query.append("\""+action.getString("actionId")+"\"");				
				
				if(action.has("Accounts")) {
					JSONArray accountsInfo = action.getJSONArray("Accounts");
					int accountsLength = accountsInfo.length();
					for(int acct=0;acct<accountsLength;acct++) {
						JSONObject accountObject = accountsInfo.getJSONObject(acct);
						String queryTemp = query.toString()+","+accountObject.getString("id");
					
						int isAllowed = 0;
						if(accountObject.getString("isEnabled").equals("true")) {
							isAllowed = 1;
						}
						else {
							isAllowed = 0;
						}
						queryTemp += ","+isAllowed;
					
						if(accountObject.has("limits") && action.get("actionType").equals("MONETARY")) {
							JSONObject limitObject = accountObject.getJSONObject("limits");
							for(String key : limitObject.keySet()) {
								String limitQuery = queryTemp+",\""+key+"\","+limitObject.getString(key);
								finalQuery.add(limitQuery);
							}
						}
						else {
							queryTemp += ",null,null";
							finalQuery.add(queryTemp);
						}
					}
				}
				else {
					query.append(",null");
					if(action.has("isEnabled") && action.getString("isEnabled").contentEquals("true")) {
						query.append(",1");
					}
					else {
						query.append(",0");
					}
					query.append(",null,null");
					finalQuery.add(query.toString());
				}
			}
		}
		
		StringBuilder result = new StringBuilder("");
		int queries = finalQuery.size(); 
		for(int query=0;query<queries;query++) {
			String temp = finalQuery.get(query);
			temp += ","+createdby+",null";
			if(query < queries-1)
				result.append(temp+"|");
			else
				result.append(temp);
		}
		return result.toString();
	}

	@Override
	public List<CustomRoleActionLimitDTO> getCustomRoleActionLimits(String customRoleId,
			Map<String, Object> headerMap) {
		
		List<CustomRoleActionLimitDTO> actionLimits = null;
		
		try {
			
			Map<String, Object> input = new HashMap<>();
			input.put("customRoleID", customRoleId);
			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
	                URLConstants.FETCH_CUSTOM_ROLE_ACTION_LIMITS_PROC);
			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
					&& response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
				JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
				actionLimits = new ArrayList<CustomRoleActionLimitDTO>();
				for (JsonElement element : responseArray) {
	                if (element.isJsonObject()) {
	                	CustomRoleActionLimitDTO dto =
	                           (CustomRoleActionLimitDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
	                          		CustomRoleActionLimitDTO.class, true);
	                  	if (dto != null) {
	                   		actionLimits.add(dto);
	                   	}
	                }
	           	}
			}
		}
		catch (Exception exp) {
			alert.prepareError("Exception occured while fetching data for custom role action limits", exp).log();
		}
		return actionLimits;
	}

	@Override
	public boolean deleteCustomRole(String customRoleId, Map<String, Object> headerMap) {
		
		try {
			Map<String, Object> input = new HashMap<>();
			input.put("id", customRoleId);
			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
	                URLConstants.CUSTOM_ROLE_DELETE);
			if (JSONUtil.isJsonNotNull(response) && 
					JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_DELETED_RECORDS)) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while deleteing a custom role", exp).log();
		}
		return false;
	}

	@Override
	public Map<String, HashMap<String, ArrayList<Double>>> getFiOrgGroupLevelActionLimits(String companyId,
			String groupId, Map<String, Object> headerMap) {

		Map<String, HashMap<String, ArrayList<Double>>> featureLimitMap = null;
		
		try {
			Map<String, Object> input = new HashMap<>();
			input.put("_companyId", companyId);
			input.put("_groupId", groupId);
			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
	                URLConstants.FETCH_FI_ORG_GROUP_LIMITS);
			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
					&& response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
				JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
				featureLimitMap = new HashMap<>();
				for(JsonElement record : responseArray) {
					String actionId = record.getAsJsonObject().get("Action_id").getAsString();
					String limitType = record.getAsJsonObject().get("LimitType_id").getAsString();
					HashMap<String, ArrayList<Double>> map = null;
					ArrayList<Double> list = null;
					if(featureLimitMap.containsKey(actionId)) {
						map = featureLimitMap.get(actionId);
						if(!map.containsKey(limitType)) {
							list = new ArrayList<Double>();
						}
					}
					else {
						map = new HashMap<>();
						list = new ArrayList<Double>();
					}
					list.add(record.getAsJsonObject().get("fiValue").getAsDouble());
					list.add(record.getAsJsonObject().get("orgValue").getAsDouble());
					list.add(record.getAsJsonObject().get("groupValue").getAsDouble());
					map.put(limitType, list);
					featureLimitMap.put(actionId, map);
				}
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching data for fi, org and group level limits", exp).log();
		}
		return featureLimitMap;
	}

	@Override
	public boolean applyCustomRoleForUsers(Map<String, Object> users, Map<String, Object> headerMap, String konyAuthToken) {
		try {			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(users, headerMap,
	                URLConstants.CUSTOM_ROLE_APPLY_LOOP, konyAuthToken);
			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, "LoopDataset")) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while applying a custom role", exp).log();
		}
		return false;
	}

	@Override
	public JsonArray getListOfUserDetails(Map<String, Object> users, Map<String, Object> headerMap, String konyAuthToken) {
		JsonArray result = null;
		try {			
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(users, headerMap,
	                URLConstants.CUSTOM_ROLE_LOOP_USER_DETAILS, konyAuthToken);
			if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, "LoopDataset")) {
				result = response.get("LoopDataset").getAsJsonArray();
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while applying a custom role", exp).log();
		}
		return result;		
	}
	
	private boolean checkAndUpdateFiOrgGroupLevelLimits(JSONArray features, String companyId, String groupId,
			Map<String, Object> headerMap) {
		
		Map<String, HashMap<String, ArrayList<Double>>> map = 
				getFiOrgGroupLevelActionLimits(companyId, groupId, headerMap);
	
		int featuresLength = features.length();
		for(int i=0; i<featuresLength; i++) {
			JSONArray actionsArray = features.getJSONObject(i).getJSONArray("Actions");
			int actionsLength = actionsArray.length();
			for(int j=0; j<actionsLength; j++) {
				JSONObject actionObject = actionsArray.getJSONObject(j);
				String actionId = actionObject.getString("actionId");
				if(actionObject.has("Accounts")) {
					JSONArray accountsArray = actionObject.getJSONArray("Accounts");
					int accountsLength = accountsArray.length();
					for(int k=0; k<accountsLength; k++) {
						JSONObject accountObject = accountsArray.getJSONObject(k);
						if(accountObject.has("limits")) {
							JSONObject limitsObject = accountObject.getJSONObject("limits");
							for(String limitKey : limitsObject.keySet()) {
								if(map.containsKey(actionId)) {
									HashMap<String, ArrayList<Double>> limitMap = map.get(actionId);
									if(limitMap.containsKey(limitKey)) {
										limitsObject.put(limitKey, Collections.min(limitMap.get("limitKey")));
									}
									else if(!isValidLimit(limitMap, limitKey, limitsObject)) {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	private boolean isValidLimit(HashMap<String, ArrayList<Double>> limitMap, String limitKey,
			JSONObject limitsObject) {
		
		String limit = "";
		if(limitKey.equals("AUTO_DENIED_TRANSACTION_LIMIT") || 
				limitKey.equals("PRE_APPROVED_TRANSACTION_LIMIT")) {
			limit = "MAX_TRANSACTION_LIMIT";
		}
		else if(limitKey.equals("AUTO_DENIED_DAILY_LIMIT") || 
				limitKey.equals("PRE_APPROVED_DAILY_LIMIT")) {
			limit = "DAILY_LIMIT";
		}
		else if(limitKey.equals("AUTO_DENIED_WEEKLY_LIMIT") || 
				limitKey.equals("PRE_APPROVED_WEEKLY_LIMIT")) {
			limit = "WEEKLY_LIMIT";
		}
		Double limitValue = limitMap.get(limit).get(0); //FI level limit
		return Double.parseDouble(limitsObject.getString(limitKey)) <= limitValue;
	}
	

	private boolean validateAccounts(JSONArray accounts, String organization_id, Map<String, Object> headerMap) {
		String filter  = "Organization_id" + DBPUtilitiesConstants.EQUAL + organization_id;
		Map<String, Object> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, filter);

		Set<String> set = new HashSet<String>();
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.ACCOUNT_GET);
		if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_ACCOUNTS)
				&& response.get(DBPDatasetConstants.DATASET_ACCOUNTS).isJsonArray()) {
			JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_ACCOUNTS).getAsJsonArray();
			for(JsonElement ele : responseArray) {
				set.add(ele.getAsJsonObject().get("Account_id").getAsString());
			}
		}
		int accountsLength = accounts.length();
		for(int i=0; i<accountsLength; i++) {
			JSONObject account = accounts.getJSONObject(i);
			if(!set.contains(account.get("accountId"))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String updateCustomRole(CustomRoleDTO customRole, JSONArray accounts, JSONArray features,
			Map<String, Object> headerMap) {

		String id = null;
		String result = null;
		try {

			Map<String, Object> input = new HashMap<String, Object>();
			input.put("id", customRole.getId());
			input.put("name", customRole.getName());
			input.put("parent_id", customRole.getParent_id());
			input.put("organization_id", customRole.getOrganization_id());
			input.put("modifiedby", customRole.getModifiedby());
			input.put("description", customRole.getDescription());

			if (!validateAccounts(accounts, customRole.getOrganization_id(), headerMap)) {
				return ErrorConstants.INVALID_ORG_ACCOUNTS;
			}

			if (!checkAndUpdateFiOrgGroupLevelLimits(features, customRole.getOrganization_id(),
					customRole.getParent_id(), headerMap)) {
				return ErrorConstants.INVALID_TRANSACTION_LIMITS;
			}

			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
					URLConstants.CUSTOM_ROLE_UPDATE);

			JsonElement element = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray().get(0);
			CustomRoleDTO dto = (CustomRoleDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
					CustomRoleDTO.class, true);
			id = dto.getId();

			result = createCustomRoleActionLimits(id, customRole.getModifiedby(), accounts, features, headerMap);

		} catch (Exception exp) {
			alert.prepareError("Exception occured while updating data for custom role table", exp).log();
		}
		return result == null ? result : id;
	}		
	
}

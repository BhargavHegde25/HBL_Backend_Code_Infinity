package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.PermissionDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;


public class AuthorizationChecksBusinessDelegateImpl implements AuthorizationChecksBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public  boolean isUserAuthorizedForFeatureAction (String userId, String featureAction, String accountIds, boolean isCombinedUser) {
		
		try {
			List<String> accountNumbers = new ArrayList<>();
		    if(StringUtils.isNotBlank(accountIds)) {
		    	accountNumbers = Arrays.asList(accountIds.split(","));
		    }
		    
			JSONObject myAccounts = fetchMyAccounts(userId);
			for(String account : accountNumbers) {
				if(!myAccounts.has(account))
					return false;
			}
			
			//return isUserAuthorizedForFeatureActionForAccount(userId, featureAction, accountIds, isCombinedUser);
			return true;
			
		} catch (Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			//return false;
			return true;
		}
	}
	
	
	@Override
	public  boolean isUserAuthorizedForFeatureActionForAccount(String userId, String featureAction, String accountIds, boolean isCombinedUser) {
		
		AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		
		try {
			List<String> accountNumbers = new ArrayList<>();
		    if(StringUtils.isNotBlank(accountIds)) {
		    	accountNumbers = Arrays.asList(accountIds.split(","));
		    }
		    
			Set<String> businessAccounts = new HashSet<>();
			Set<String> retailAccounts = new HashSet<>();
			for(String account : accountNumbers) {
				if(actDelegate.isBusinessAccount(account)) {
					businessAccounts.add(account);
				} else {
					retailAccounts.add(account);
				}
		    }
			
			Set<String> authorizedAccounts = new HashSet<>();
			List<PermissionDTO> permissions = _fetchPermissionsFromCustomerActionsProc(userId, featureAction);
			if(permissions != null && permissions.size() > 0) {
				for(PermissionDTO permission : permissions) {
					String isAllowedString = permission.getIsAllowed();
					boolean isAllowed = isAllowedString != null ? isAllowedString.toLowerCase().equals("true") : false;
					String isAccountLevelString = permission.getIsAccountLevel();
					boolean isAccountLevel = isAccountLevelString != null ? isAccountLevelString.toLowerCase().equals("true") : false;
					String featureStatus = permission.getFeature_Status_id();
					if(isAllowed && Constants.SID_FEATURE_ACTIVE.equals(featureStatus)) {
						String accountId = permission.getAccount_id();
						
						if(!isAccountLevel) {
							return true;
						}
						else if (StringUtils.isNotBlank(accountId)) {
							authorizedAccounts.add(accountId);
						}
						else if(!isCombinedUser && businessAccounts.isEmpty() && !retailAccounts.isEmpty()) {
							return true;
						}
					}
				}
			}
			Boolean businesspassed = false;
			if(!businessAccounts.isEmpty() && authorizedAccounts.containsAll(businessAccounts))
				businesspassed = true;
			if(isCombinedUser) {
				List<PermissionDTO> retailPermissions = _fetchPermissionsFromCustomerGroupActionsProc(userId, featureAction);
				if(retailPermissions != null && retailPermissions.size() > 0) {
					for(PermissionDTO permission : retailPermissions) {
						String isAllowedString = permission.getIsAllowed();
						boolean isAllowed = isAllowedString != null ? isAllowedString.toLowerCase().equals("true") : false;
						String featureStatus = permission.getFeature_Status_id();
						String isAccountLevelString = permission.getIsAccountLevel();
						boolean isAccountLevel = isAccountLevelString != null ? isAccountLevelString.toLowerCase().equals("true") : false;
						if(isAccountLevel && retailAccounts.isEmpty() && businessAccounts.isEmpty()) 
							return false;
						else if (isAccountLevel && retailAccounts.isEmpty() && businesspassed)
							return true;
						else if(!isAccountLevel && isAllowed && Constants.SID_FEATURE_ACTIVE.equals(featureStatus))
							return true;
						else if(isAccountLevel && isAllowed && Constants.SID_FEATURE_ACTIVE.equals(featureStatus) && !retailAccounts.isEmpty() && businessAccounts.isEmpty())
							return true;
						else if(isAccountLevel && isAllowed && Constants.SID_FEATURE_ACTIVE.equals(featureStatus) && !retailAccounts.isEmpty() && businesspassed)
							return true;
					}
				}
				else return businesspassed;
			}
			else return businesspassed;
		} catch (Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			return false;
		}
		return false;
	}
	
    /**
     * Retrieves the customer json array actions based on the feature action provided
     * @param userId
     * @param featureAction
     * @return
     */
    private List<PermissionDTO> _fetchCustomerActions(String userId, String featureAction, boolean isCombinedUser) {
    	
		List<PermissionDTO> permissions = _fetchPermissionsFromCustomerActionsProc(userId, featureAction);
		if(isCombinedUser) {
			List<PermissionDTO> retailPermissions = _fetchPermissionsFromCustomerGroupActionsProc(userId, featureAction);
			retailPermissions.forEach(permission -> permission.setCombinedPermission(true));
			if(permissions != null) {
				permissions.addAll(retailPermissions);
			}
			else {
				permissions = retailPermissions;
			}
		}
		return permissions;
    }
    
    private List<PermissionDTO> _fetchPermissionsFromCustomerActionsProc(String userId, String featureAction) {
    	Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_customerId", userId);
		requestParams.put("_actionId", featureAction);
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMER_ACTIONS_PROC;
		String response = new String();
		List<PermissionDTO> permissions;
        
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			
			JSONObject responseJson = new JSONObject(response);
			if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has("records")) {
				return null;
			}
			
			permissions = JSONUtils.parseAsList(responseJson.getJSONArray("records").toString(), PermissionDTO.class);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			return null;
		}
		return permissions;
    }
    
    private List<PermissionDTO> _fetchPermissionsFromCustomerGroupActionsProc(String userId, String featureAction) {
    	Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_customerId", userId);
		requestParams.put("_actionId", featureAction);
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMER_GROUP_ACTIONS_PROC;
		String response = new String();
		List<PermissionDTO> retailPermissions;
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			
			JSONObject responseJson = new JSONObject(response);
			if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has("records")) {
				return null;
			}
			
			retailPermissions = JSONUtils.parseAsList(responseJson.getJSONArray("records").toString(), PermissionDTO.class);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			return null;
		}
		return retailPermissions;
    }
    
    /*
	 * Method to fetch User Permissions
	 */
    private JSONObject _fetchUserPermissions(Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.DBP_PRODUCT_SERVICES;
		String operationName = OperationName.GET_COMBINED_USER_PERMISSIONS;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String response = null;
        
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject responseJson = new JSONObject(response);
			if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !(responseJson.has(Constants.BUSINESS_PERMISSIONS) || responseJson.has(Constants.RETAIL_PERMISSIONS))) {
				return null;
			}
			
			return responseJson;
		}
		catch(Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			return null;
		}
    }
    
	/*
	 * Method to convert permission object to list of permissions
	 */
    private List<String> getListFromResponseObject(Object permissionsObj){
		String permissions = permissionsObj.toString();
		permissions = permissions.replaceAll("\"", "");
		permissions = permissions.substring(1, permissions.length()-1);
		List<String> permissionList = Arrays.asList(permissions.trim().split("\\s*,\\s*"));
		return permissionList;
    }

	public boolean isUserAuthorizedForPayeeOperations(String featureAction, String isBusinessPayee, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		if(featureAction == null || isBusinessPayee == null) {
			return false;
		}
		
		JSONObject responseJson = _fetchUserPermissions(headerParams, dcRequest);
		if(responseJson == null)
			return false;
		
		if("1".equals(isBusinessPayee)) {
			List<String> businessPermissions = new ArrayList<String>();
			if(responseJson.has(Constants.BUSINESS_PERMISSIONS))
				businessPermissions = getListFromResponseObject(responseJson.get(Constants.BUSINESS_PERMISSIONS));
			if(businessPermissions.contains(featureAction))
				return true;
		}
		else if ("0".equals(isBusinessPayee)) {
			List<String> retailPermissions = new ArrayList<String>();
			if(responseJson.has(Constants.RETAIL_PERMISSIONS))
				retailPermissions = getListFromResponseObject(responseJson.get(Constants.RETAIL_PERMISSIONS));
			if(retailPermissions.contains(featureAction))
				return true;
		}
		
		return false;
	}

	public boolean isUserAuthorizedForFetchPayees(String featureAction, String permissionType, Map<String, Object> headerParams, DataControllerRequest dcRequest) {

		if(featureAction == null || permissionType == null) {
			return false;
		}

		if(permissionType.equals(Constants.RETAIL_PERMISSIONS)) {
			diagnostic.prepareDebug("fetching retail permissions map").log();
		}else if(permissionType.equals(Constants.BUSINESS_PERMISSIONS)){
			diagnostic.prepareDebug("fetching business permissions map").log();
		}else{
			alert.prepareError("Unknown permission Type").log();
			return false;
		}


		JSONObject responseJson = _fetchUserPermissions(headerParams, dcRequest);
		if(responseJson == null)
			return false;

		List<String> permissions = new ArrayList<String>();
		if(responseJson.has(permissionType))
			permissions = getListFromResponseObject(responseJson.get(permissionType));
		if(permissions.contains(featureAction))
			return true;

		return false;
	}
	
	@Override
	public Set<String> fetchUnAuthorizedAccounts(String userId, String featureAction, boolean isCombinedUser) {
		Set<String> notAuthorizedAccounts;
        try {
            List<PermissionDTO> permissions = _fetchCustomerActions(userId, featureAction, isCombinedUser);
            notAuthorizedAccounts = new HashSet<String>();
            if(CollectionUtils.isNotEmpty(permissions)) {
                for(PermissionDTO permission : permissions) {
                    String isAllowedString = permission.getIsAllowed();
                    boolean isAllowed = isAllowedString != null ? isAllowedString.toLowerCase().equals("true") : false;
                    if(!isAllowed && StringUtils.isNotEmpty(permission.getAccount_id())) {
                        notAuthorizedAccounts.add(permission.getAccount_id());
                    }
                }
            }
        } catch (Exception e) {
        	alert.prepareError("Caught exception while getting feature actions: " + e).log();
        	return new HashSet<String>();
        }
        return notAuthorizedAccounts;
	}

	@Override
	public boolean isOneOfMyAccounts(String createdby, String fromAccount) {
		
		JSONObject myAccountsjson = fetchMyAccounts(createdby);
		alert.prepareError("FromAccount that is getting validated in cache"+fromAccount).log();
		
		if(myAccountsjson.has(fromAccount))
			return true;
		
		return false;
	}
	
	@Override
	public boolean isOneOfMyAccounts(String createdby, List<String> fromAccounts) {
		
		JSONObject myAccountsjson = fetchMyAccounts(createdby);
		alert.prepareError("FromAccount that is getting validated in cache"+fromAccounts.toString()).log();
		
		for(String fromAccount: fromAccounts)
			if(!myAccountsjson.has(fromAccount))
				return false;
		
		return true;
	}
	
	@Override
	public JSONObject fetchMyAccounts(String createdby) {
		
		JSONObject myAccountsjson = new JSONObject();
		
		try {
			String key = "INTERNAL_BANK_ACCOUNTS" + createdby;
			ResultCache resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
			
			if (resultCache != null && StringUtils.isNotBlank(key)) {
				String myAccounts = (String) resultCache.retrieveFromCache(key);
				alert.prepareError("cache data for this user"+myAccounts).log();
				if(myAccounts != null) {
					myAccountsjson = new JSONObject(myAccounts);
				}
			}
		}
		catch(MiddlewareException e) {
			alert.prepareError("Error occured while fetching the list from cache", e).log();
		}
		return myAccountsjson;
	}
	
	
	@Override
	public boolean isUserAuthorizedForFeatureAction(List<String> featureActions, Map<String, List<String>> contractCifMap, Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		if(contractCifMap == null || contractCifMap.isEmpty()) {
			return false;
		}
		
		JSONArray jsonArray = getInfinityUserContractCustomersPermissions(headerParams,dcRequest);
		if(jsonArray == null || jsonArray.length() == 0) {
			return false;
		}
		
		Map<String, List<String>> authContractObject = new HashMap<String, List<String>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject contractObj = jsonArray.getJSONObject(i);
			String contractId = contractObj.getString("contractId");
			JSONArray contractCustomers = contractObj.getJSONArray("contractCustomers");
			for(int j = 0; j < contractCustomers.length(); j++) {
				JSONObject cifObj = contractCustomers.getJSONObject(j);
				String coreCustomerId = cifObj.getString("coreCustomerId");
				authContractObject.put(contractId+"_"+coreCustomerId, getListFromResponseObject(cifObj.get("actions")));
			}
        }
		
		for (Map.Entry<String,List<String>> contractCif : contractCifMap.entrySet())  {
			String contractId = contractCif.getKey();
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	List<String> cifPermissions = authContractObject.get(contractId+"_"+coreCustomerIds.get(j));
            	if(cifPermissions == null || !cifPermissions.containsAll(featureActions))
            		return false;
			}
    	} 
		return true;
	}

	private JSONArray getInfinityUserContractCustomersPermissions(Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.EUM_PRODUCT_SERVICES;
		String operationName = OperationName.GETINFINITYUSERCONTRACTCUSTOMERS;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String response = null;
        
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			return jsonArray;
		}
		catch(Exception e) {
			alert.prepareError("Caught exception while getting feature actions: " + e).log();
			return null;
		}
	}
	
	@Override
	public Set<String> getUserAuthorizedCifsForFeatureAction(List<String> featureActions, Map<String, Object> headerMap,
			DataControllerRequest request) {
		JSONArray jsonArray = getInfinityUserContractCustomersPermissions(headerMap, request);
		if(jsonArray == null || jsonArray.length() == 0) {
			return null;
		}
		
		Set<String> authCifs = new HashSet<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject contractObj = jsonArray.getJSONObject(i);
			JSONArray cifFeatures = contractObj.getJSONArray("contractCustomers");
			for(int j = 0; j < cifFeatures.length(); j++) {
				JSONObject cifObj = cifFeatures.getJSONObject(j);
				List<String> cifPermissions = getListFromResponseObject(cifObj.get("actions"));
				if(cifPermissions.containsAll(featureActions))
					authCifs.add(cifObj.getString("coreCustomerId"));
			}
        }
		
		return authCifs;
	}
	
	@Override
	public Map<String, List<String>> getAuthorizedCifs(List<String> featureActions, Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		JSONArray jsonArray = getInfinityUserContractCustomersPermissions(headerParams,dcRequest);
		if(jsonArray == null || jsonArray.length() == 0) {
			return null;
		}
		
		Map<String, List<String>> authContractObject = new HashMap<String, List<String>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject contractObj = jsonArray.getJSONObject(i);
			String contractId = contractObj.getString("contractId");
			JSONArray contractCustomers = contractObj.getJSONArray("contractCustomers");
			List<String> customers = new ArrayList<String>();
			for(int j = 0; j < contractCustomers.length(); j++) {
				JSONObject cifObj = contractCustomers.getJSONObject(j);
				String coreCustomerId = cifObj.getString("coreCustomerId");
				List<String> customerActions = getListFromResponseObject(cifObj.get("actions"));
				if(customerActions.containsAll(featureActions))
					customers.add(coreCustomerId);
					
			}
			if(!customers.isEmpty())
				authContractObject.put(contractId, customers);
        }
		
		return authContractObject;
	}

}

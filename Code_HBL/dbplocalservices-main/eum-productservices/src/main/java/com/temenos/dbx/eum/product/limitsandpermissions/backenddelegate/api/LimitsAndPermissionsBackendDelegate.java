package com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;

public interface LimitsAndPermissionsBackendDelegate extends BackendDelegate{
	
	/**
	 * Adds new feature to contract
	 * @param actionLimit
	 * @param headerMap 
	 * @param log 
	 * @return
	 */
	public boolean addFeaturesToContract(ActionLimitsDTO actionLimit, Map<String, Object> headerMap, JsonObject log);
	
	/**
	 * Adds new Action to contract
	 * @param actionLimit
	 * @param headerMap
	 * @param log 
	 * @return
	 */
	public boolean addActionsToContract(ActionLimitsDTO actionLimit, Map<String, Object> headerMap, JsonObject log, Set<String> contractCustomerAccounts);
	
	/**
	 * Adds new Action to customer
	 * @param actionLimit
	 * @param headerMap
	 * @return
	 */
	public boolean addActionsToCustomer(ActionLimitsDTO actionLimit, Map<String, Object> headerMap);
	
	/**
	 * Adds new Action to customRole
	 * @param actionLimit
	 * @param headerMap
	 * @return
	 */
	public boolean addActionsToCustomRole(ActionLimitsDTO actionLimit, Map<String, Object> headerMap);
	

}

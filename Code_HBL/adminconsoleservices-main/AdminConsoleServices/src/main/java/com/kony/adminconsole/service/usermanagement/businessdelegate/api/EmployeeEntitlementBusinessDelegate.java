package com.kony.adminconsole.service.usermanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface EmployeeEntitlementBusinessDelegate extends BusinessDelegate {
	
	public JSONObject createEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getEntitlement(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateEntitlement(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject deleteEntitlement(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;

}
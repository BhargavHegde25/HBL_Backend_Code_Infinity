package com.kony.adminconsole.service.usermanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface EmployeeRoleBusinessDelegate extends BusinessDelegate {
	
	public JSONObject getEmployeeRoles(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject getEmployeeRoleDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject createEmployeeRole(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeeRoleDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeeRoleStatus(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeeRoleBasicDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;

}
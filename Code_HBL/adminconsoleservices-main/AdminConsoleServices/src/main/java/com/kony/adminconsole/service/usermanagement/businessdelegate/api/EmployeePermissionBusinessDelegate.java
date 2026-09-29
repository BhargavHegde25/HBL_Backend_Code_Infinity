package com.kony.adminconsole.service.usermanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface EmployeePermissionBusinessDelegate extends BusinessDelegate {
	
	public JSONObject getEmployeePermissionDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject createEmployeePermission(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeePermissionDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeePermissionStatus(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject fetchLegalEntityList(Map<String, Object> postParametersMap)
            throws DBPApplicationException;
	
	public JSONObject updateEmployeePermissionBasicDetails(Map<String, Object> postParametersMap)
            throws DBPApplicationException;

	public JSONObject getPermissionsByLegalEnities(Map<String, Object> postParametersMap)
			throws DBPApplicationException;

	

}

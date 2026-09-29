package com.kony.adminconsole.service.productmanagement.backenddelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface FacilityBackendDelegate extends BackendDelegate {

	public JSONObject createFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject createFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject editFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject editFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
    public JSONObject deleteFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
    public JSONObject getFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
}

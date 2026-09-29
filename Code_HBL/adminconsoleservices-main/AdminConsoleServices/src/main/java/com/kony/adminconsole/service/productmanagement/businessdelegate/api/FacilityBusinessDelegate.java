package com.kony.adminconsole.service.productmanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface FacilityBusinessDelegate extends BusinessDelegate {

	public JSONObject createFacility(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
	
	public JSONObject editFacility(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
	
	public JSONObject getFacility(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
}

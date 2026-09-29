package com.kony.adminconsole.service.customer.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface PartyUserManagementBusinessDelegate extends BusinessDelegate {
	
	public JSONObject createPartyUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject updatePartyUser(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject searchPartyUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

}

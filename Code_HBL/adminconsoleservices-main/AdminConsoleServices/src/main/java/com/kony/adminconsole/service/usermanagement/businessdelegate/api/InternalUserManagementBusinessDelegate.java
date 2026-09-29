package com.kony.adminconsole.service.usermanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface InternalUserManagementBusinessDelegate extends BusinessDelegate {

    public JSONObject createInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject editInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject downloadUsersList(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateUserStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

}

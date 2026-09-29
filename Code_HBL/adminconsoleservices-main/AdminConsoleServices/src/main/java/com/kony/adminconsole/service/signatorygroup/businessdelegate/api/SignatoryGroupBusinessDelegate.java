package com.kony.adminconsole.service.signatorygroup.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface SignatoryGroupBusinessDelegate extends BusinessDelegate {

    public JSONObject createSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateSignatoryGroups(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject deleteSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getNoGroupUsers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getApprovalPermissionsForUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getAllSignatoryGroupsbyCoreCustomerIds(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getAllSignatoryGroups(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getSignatoryGroupDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject fetchApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject deleteApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject isSignatoryGroupEligibleForDelete(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

}

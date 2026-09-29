package com.kony.adminconsole.service.customer.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface InfinityUserManagementBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param contractCustomerDTO
     * @param headerMap
     * @return DBXResult.response containing the list of customers information
     * @throws DBPApplicationException
     */
    public JSONObject getAssociatedCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getAllEligibleRelationalCustomers(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject createInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject editInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getCoreCustomerRoleFeatureActionLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getCoreCustomerProductRolesFeatureActionLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getRelativeCoreCustomerContractDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getCoreCustomerContractDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getInfinityUserContractDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getInfinityUserAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getInfinityUserFeatureActions(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getInfinityUserLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getInfinityUserAccountsForCorecustomer(Map<String, Object> postParametersMap, String dbpServicesClaimsToken) 
			throws DBPApplicationException;

    public JSONObject getInfinityUserServicedefsRoles(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException;

}

package com.kony.adminconsole.service.contract.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

/**
 * Handles all the operations on Contract
 * 
 * @author extends {@link BusinessDelegate}
 */

public interface ContractBusinessDelegate extends BusinessDelegate {

    public JSONObject createContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject editContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject searchContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateContractStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getListOfContractsByStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject searchCoreCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getCoreRelativeCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getCoreCustomerAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getContractDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

    public JSONObject getContractFeatureActionLimits(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken) throws DBPApplicationException;

    public JSONObject getContractInfinityUsers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getContractAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getCoreCustomerDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
    		throws DBPApplicationException;

}

package com.kony.adminconsole.service.approvalmatrix.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface ApprovalMatrixBusinessDelegate extends BusinessDelegate {

    public JSONObject getApprovalMatrix(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject createApprovalRuleSGLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject createApprovalRuleUserLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getApprovalRules(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateApprovalRuleUserLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateApprovalRuleSGLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getApproversInSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getApprovalMatrixByContractId(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject getAccountActionCustomerApproverList(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject updateApprovalMatrixStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
    
    public JSONObject isApprovalMatrixDisabled(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
}

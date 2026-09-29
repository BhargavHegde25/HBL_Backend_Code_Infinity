package com.temenos.dbx.eum.product.contract.backenddelegate.api;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface ContractFeatureActionsBackendDelegate extends BackendDelegate {

    public Set<String> createContractFeatures(Set<String> featuresList, String contractId, String customerId,
            String serviceTypeID, String isDefaultActionsEnabled, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public void createContractActionLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException;

    public String getActionsWithApproveFeatureAction(String actionsCSV, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public void decreaseUserLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException;
    
    public void createDefaultContractActionLimits(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;
}

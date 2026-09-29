package com.temenos.dbx.eum.product.contract.businessdelegate.api;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface ContractFeatureActionsBusinessDelegate extends BusinessDelegate {

    public Set<String> createContractFeatures(Set<String> featuresList, String contractId, String customerId,
            String serviceTypeID, String isDefaultActionsEnabled, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public void createContractActionLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException;

    public String getActionsWithApproveFeatureAction(String actionsCSV, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public void decreaseUserLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException;

    public void createDefaultContractActionLimits(String contractId, String legalEntityId, Map<String, Object> headerMap) throws ApplicationException;
}

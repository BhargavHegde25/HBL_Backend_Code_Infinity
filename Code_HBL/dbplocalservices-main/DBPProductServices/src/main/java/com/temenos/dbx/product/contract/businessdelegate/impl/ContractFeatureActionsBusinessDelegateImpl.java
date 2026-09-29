package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractFeatureActionsBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;

public class ContractFeatureActionsBusinessDelegateImpl implements ContractFeatureActionsBusinessDelegate {

    @Override
    public Set<String> createContractFeatures(Set<String> featuresList, String contractId, String customerId,
            String serviceTypeID, String isDefaultActionsEnabled, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractFeatureActionsBackendDelegate featureActionsBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractFeatureActionsBackendDelegate.class);
        return featureActionsBD.createContractFeatures(featuresList, contractId, customerId, serviceTypeID,
                isDefaultActionsEnabled, headersMap);
    }

    @Override
    public void createContractActionLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractFeatureActionsBackendDelegate featureActionsBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractFeatureActionsBackendDelegate.class);
        featureActionsBD.createContractActionLimits(queryString, headersMap);

    }

    @Override
    public String getActionsWithApproveFeatureAction(String actionsCSV, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractFeatureActionsBackendDelegate featureActionsBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractFeatureActionsBackendDelegate.class);
        return featureActionsBD.getActionsWithApproveFeatureAction(actionsCSV, headersMap);
    }

    @Override
    public void decreaseUserLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractFeatureActionsBackendDelegate featureActionsBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractFeatureActionsBackendDelegate.class);
        featureActionsBD.decreaseUserLimits(queryString, headersMap);
    }

}

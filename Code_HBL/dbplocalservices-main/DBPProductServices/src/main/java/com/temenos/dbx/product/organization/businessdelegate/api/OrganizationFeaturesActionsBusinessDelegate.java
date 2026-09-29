package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganizationsFeatureActionsDTO;

public interface OrganizationFeaturesActionsBusinessDelegate extends BusinessDelegate {

    public boolean createOrganizationFeatures(OrganizationsFeatureActionsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public String createOrganizationActionLimits(OrganizationsFeatureActionsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public String getMonetaryActions(String actionsCSV, Map<String, Object> headersMap)
            throws ApplicationException;

    public String createCustomerActions(String actionsCSV, String accountsCSV, String customerId, String businessTypeId,
            String groupId, Map<String, Object> headersMap) throws ApplicationException;

}

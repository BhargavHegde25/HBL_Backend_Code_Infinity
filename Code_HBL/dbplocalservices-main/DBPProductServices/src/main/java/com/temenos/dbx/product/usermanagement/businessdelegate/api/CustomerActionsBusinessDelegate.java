package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.dto.CustomerActionsProcDTO;

public interface CustomerActionsBusinessDelegate extends BusinessDelegate {

    public List<CustomerActionsProcDTO> getCustomerActions(CustomerActionsProcDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public void createCustomerActions(String userId, String contractId, String coreCustomerId, String groupId,
            Set<String> accounts,
            Map<String, Object> headersMap) throws ApplicationException;

    public Map<String, Set<String>> getSecurityAttributes(String customerId, Map<String, Object> headersMap,String legalEntityId)
            throws ApplicationException;

    public void createCustomerLimitGroupLimits(String userId, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    public List<CustomerActionDTO> getCustomerActions(CustomerActionDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public void createAccountDefaultCustomerActions(String queryInput, String customerId,
            Map<String, Object> headersMap) throws ApplicationException;

}

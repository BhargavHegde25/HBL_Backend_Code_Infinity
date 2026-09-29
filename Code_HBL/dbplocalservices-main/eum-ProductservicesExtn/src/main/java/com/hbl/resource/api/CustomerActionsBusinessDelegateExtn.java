package com.hbl.resource.api;

import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;

public interface CustomerActionsBusinessDelegateExtn extends CustomerActionsBusinessDelegate{
	public void createCustomerLimitGroupLimits(String userId, String contractId, String coreCustomerId, String legalEntityId,
            Map<String, Object> headersMap) throws ApplicationException;

}

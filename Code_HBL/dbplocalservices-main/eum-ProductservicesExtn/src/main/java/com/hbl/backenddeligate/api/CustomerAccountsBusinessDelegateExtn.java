package com.hbl.backenddeligate.api;

import java.util.Map;
import java.util.Set;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;

public interface CustomerAccountsBusinessDelegateExtn extends CustomerAccountsBusinessDelegate{

	public void createCustomerAccounts(String userId, String contractId, String coreCustomerId, String legalEntityId,
			Set<String> accounts, Map<String, Object> headersMap, String defaultAccount) throws ApplicationException;
	
	

}

package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.HashMap;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.api.AccountClosureBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.AccountClosureBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class AccountClosureBusinessDelegateImpl  implements AccountClosureBusinessDelegate {

	public AccountClosureDTO CloseAccount(AccountClosureDTO accountClosureDTO,
			HashMap<String, Object> headerMap) throws ApplicationException {
		AccountClosureBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(AccountClosureBackendDelegate.class);
		AccountClosureDTO accountClosure = orderBackendDelegate.CloseAccount(accountClosureDTO,
				headerMap);
		return accountClosure;
	}
	
}


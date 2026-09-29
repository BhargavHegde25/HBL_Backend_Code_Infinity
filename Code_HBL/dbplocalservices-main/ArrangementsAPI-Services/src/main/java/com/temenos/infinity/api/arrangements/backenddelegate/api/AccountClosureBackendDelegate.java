package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.util.HashMap;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface AccountClosureBackendDelegate extends BackendDelegate {
	
	AccountClosureDTO CloseAccount(AccountClosureDTO accountClosureDTO,
			HashMap<String, Object> headerMap) throws ApplicationException;

}

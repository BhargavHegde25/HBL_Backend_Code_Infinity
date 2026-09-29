package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.HashMap;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface AccountClosureBusinessDelegate extends BusinessDelegate {

	AccountClosureDTO CloseAccount(AccountClosureDTO accountClosureDTO,
			HashMap<String, Object> headerMap) throws ApplicationException;

}

package com.temenos.dbx.product.commons.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface UserBackendDelegate extends BackendDelegate {

	String fetchUsernameFromCustomerId(String customerId, Map<String, Object> headersMap) throws ApplicationException;

	String fetchFirstnameAndLastnameFromCustomerId(String customerId, Map<String, Object> headersMap)
			throws ApplicationException;

}

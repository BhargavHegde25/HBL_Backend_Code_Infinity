package com.temenos.auth.usermanagement.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface AuthUserManagementBackendDelegate extends BackendDelegate {
	public Result getCustomerActiveLegalEntities(String customerId) 
			throws ApplicationException;
}

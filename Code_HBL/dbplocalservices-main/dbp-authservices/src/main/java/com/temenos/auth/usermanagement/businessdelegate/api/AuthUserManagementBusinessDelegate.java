package com.temenos.auth.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface AuthUserManagementBusinessDelegate extends BusinessDelegate {
	
	public Result getCustomerActiveLegalEntities(String customerId) throws ApplicationException;
	
	public Result getCustomerFeatureAndPermissions(String legalEntityId, String cacheKey,
			Map<String, String> userInfo) throws ApplicationException;

}

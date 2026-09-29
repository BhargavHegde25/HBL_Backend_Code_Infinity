package com.bct.custom.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface FavoriteMerchantManageResource extends Resource{
	public Result favoriteMerchantCRUDOperations(String methodId, Object[] inputArray, DataControllerRequest dcRequest) throws ApplicationException;

}

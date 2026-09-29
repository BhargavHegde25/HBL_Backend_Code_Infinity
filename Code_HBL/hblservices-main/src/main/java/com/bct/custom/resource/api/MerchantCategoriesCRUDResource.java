package com.bct.custom.resource.api;

import java.io.IOException;

import org.json.JSONArray;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MerchantCategoriesCRUDResource extends Resource {
	public Result merchantCategoriesCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

	public Result createMerchantCategories(JSONArray requestArray, DataControllerRequest dcRequest)
			throws ApplicationException, IOException, Exception;

	public Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest);

}

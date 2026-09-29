package com.temenos.dbx.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PartyRelationsUserManagementResource extends Resource
{

	public Result partyRelationCreate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	public Result partyRelationsGet(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	public Result partyRelationsUpdate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

}

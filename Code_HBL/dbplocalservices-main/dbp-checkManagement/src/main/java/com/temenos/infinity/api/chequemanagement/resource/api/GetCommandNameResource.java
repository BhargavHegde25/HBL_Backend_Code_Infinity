package com.temenos.infinity.api.chequemanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface GetCommandNameResource extends Resource {
	Result getCommandName(DataControllerRequest request);
}

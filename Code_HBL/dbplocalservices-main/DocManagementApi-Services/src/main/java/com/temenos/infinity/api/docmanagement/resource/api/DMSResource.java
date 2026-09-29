package com.temenos.infinity.api.docmanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface DMSResource extends Resource {

	Result loginAndSearchFiles(DataControllerRequest request, String page, String accountNnumber, String customerNumber, String year, String subType,
			String authToken)throws ApplicationException;

	Result loginAndDownload(String id, String revision, String operation, String auth_token);

}

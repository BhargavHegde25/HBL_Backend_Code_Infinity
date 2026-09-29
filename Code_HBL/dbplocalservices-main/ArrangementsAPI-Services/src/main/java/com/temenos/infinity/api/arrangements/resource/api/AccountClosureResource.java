package com.temenos.infinity.api.arrangements.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface AccountClosureResource extends Resource {

	Result CloseAccount(AccountClosureDTO accountClosureDTO, DataControllerRequest request,
			HashMap<String, Object> headerMap) throws ApplicationException;
}

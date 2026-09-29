package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetCommandNameBusinessDelegate extends BusinessDelegate {
	Result  getCommandName( DataControllerRequest request) throws ApplicationException;
}

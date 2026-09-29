package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetCommandNameBackendDelegate extends BackendDelegate {

	Result getCommandName( DataControllerRequest request) throws ApplicationException;
}

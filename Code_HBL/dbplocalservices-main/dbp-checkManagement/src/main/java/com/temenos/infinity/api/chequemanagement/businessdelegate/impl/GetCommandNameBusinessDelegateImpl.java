package com.temenos.infinity.api.chequemanagement.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetCommandNameBackendDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetCommandNameBusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetCommandNameBusinessDelegateImpl implements GetCommandNameBusinessDelegate {

	@Override
	public Result getCommandName(DataControllerRequest request) throws ApplicationException {
		// TODO Auto-generated method stub
		GetCommandNameBackendDelegate GetCommandNameBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(GetCommandNameBackendDelegate.class);
		Result command = GetCommandNameBackendDelegate.getCommandName(request);
		return command;
	
	}

}

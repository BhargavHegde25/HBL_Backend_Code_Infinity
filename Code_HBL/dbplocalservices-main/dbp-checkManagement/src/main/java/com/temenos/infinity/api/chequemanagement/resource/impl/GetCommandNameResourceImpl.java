package com.temenos.infinity.api.chequemanagement.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetCommandNameBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCommandNameResource;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetCommandNameResourceImpl implements GetCommandNameResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getCommandName(DataControllerRequest request) {
		Result result = new Result();
		GetCommandNameBusinessDelegate getCommandNameBusinessDelegate= DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetCommandNameBusinessDelegate.class);
		Result command=new Result();
		try {
			diagnostic.prepareDebug("Entering GetCommandName resource layer").log();
			diagnostic.prepareDebug("input params "+request).log();
			command = getCommandNameBusinessDelegate.getCommandName(request);
//			JSONObject json = new JSONObject(command);
//			result = JSONToResult.convert(json.toString());
			return command;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError("exception occured in the getcommandnameresource layer").log();
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
		}
		
	}

}

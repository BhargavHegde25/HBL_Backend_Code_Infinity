package com.temenos.infinity.api.chequemanagement.javaservice;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCommandNameResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public class GetCommandName implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			diagnostic.prepareDebug("Entering GetCommandName java layer").log();
			diagnostic.prepareDebug("input params "+request).log();
			GetCommandNameResource getCommandNameResource = DBPAPIAbstractFactoryImpl.getResource(GetCommandNameResource.class);
			Result result =  getCommandNameResource.getCommandName(request);	
			return result;
		}
		catch (Exception e) { 
			alert.prepareError("exception occured in the getcommandname java layer").log();
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}
	}
}

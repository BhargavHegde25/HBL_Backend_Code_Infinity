package com.temenos.infinity.api.savingspot.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.savingspot.resource.api.SavingsPotResource;

public class CreateSavingsPot implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			//Initializing of savingsPotResource through Abstract factory method
			SavingsPotResource savingsPotResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(SavingsPotResource.class);
			result  = savingsPotResource.createSavingsPot(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of getAllSavingsPot: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

		return result;
	}
}

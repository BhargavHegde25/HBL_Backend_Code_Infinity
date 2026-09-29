package com.temenos.infinity.tradefinanceservices.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateLetterOfCreditsResource;

public class ApproveLetterOfCreditsOperation implements JavaService2{
	//Change the error code
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			CreateLetterOfCreditsResource letterOfCreditResource = DBPAPIAbstractFactoryImpl
					.getResource(CreateLetterOfCreditsResource.class);

			Result result = letterOfCreditResource.executeLetterOfCreditsRequest(methodId, inputArray, request, response);
			return result;
		} catch (Exception e) { 
			alert.prepareError("Unable to create order : "+e).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}
	}

}

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
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditsResource;

public class WithdrawLetterOfCreditsOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			LetterOfCreditsResource letterOfCreditsResource = DBPAPIAbstractFactoryImpl
					.getResource(LetterOfCreditsResource.class);
			result = letterOfCreditsResource.withdrawLetterOfCredit(request);
			return result;
		} catch (Exception e) { //Change error code
			alert.prepareError("Unable to withdraw letter of credit : "+e).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}

	}

}




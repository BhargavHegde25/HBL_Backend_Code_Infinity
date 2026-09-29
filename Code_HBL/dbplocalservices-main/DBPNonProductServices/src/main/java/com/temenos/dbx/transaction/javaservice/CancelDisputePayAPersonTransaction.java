package com.temenos.dbx.transaction.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.transservices.DeleteTransaction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CancelDisputePayAPersonTransaction implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			result = (Result) new DeleteTransaction().invoke("DeleteTransaction", inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke : "+e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}


}

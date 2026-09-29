package com.temenos.dbx.bulkpaymentservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentSampleFilesDBOperation;
import com.temenos.dbx.bulkpaymentservices.utilities.CancellationReasonDBOperation;

public class FetchCancellationReasons implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		// TODO Auto-generated method stub
		Result result;	
		
		try {
			CancellationReasonDBOperation bulkPaymentCancellationReasonsDBOperation = new CancellationReasonDBOperation();
			result = bulkPaymentCancellationReasonsDBOperation.fetchCancellationReasons();
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking fetchCancellationReasons", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}

}

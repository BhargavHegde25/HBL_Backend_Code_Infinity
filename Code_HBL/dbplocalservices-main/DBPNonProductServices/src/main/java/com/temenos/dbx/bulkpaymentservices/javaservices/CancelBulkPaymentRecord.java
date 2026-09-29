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
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentRecordDBOperations;

public class CancelBulkPaymentRecord implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result;

		try {
			BulkPaymentRecordDBOperations bulkPaymentRecordDBOperations = new BulkPaymentRecordDBOperations();
			String recordId = request.getParameter("recordId");
			String comments = request.getParameter("comments");
			String cancellationreason = request.getParameter("cancellationreason");
			
			result  = bulkPaymentRecordDBOperations.cancelBulkPaymentRecord(recordId, comments, cancellationreason);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking cancelBulkPaymentRecord: ", e).log();
			return ErrorCodeEnum.ERR_21235.setErrorCode(new Result());
		}	
		return result;
	}

}


package com.temenos.dbx.bulkpaymentservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentPODBOperations;

public class ApproveBulkPaymentOrder implements JavaService2  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {		
			BulkPaymentPODBOperations bulkPaymentPODBOperations = new BulkPaymentPODBOperations();			
			String paymentOrderId = dcRequest.getParameter("paymentOrderId");		
			
			result  = bulkPaymentPODBOperations.updateBulkPaymentOrderStatus(paymentOrderId, TransactionStatusEnum.APPROVED.getStatus());				
			
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking updatePaymentOrder at Backend: ", e).log();
			return ErrorCodeEnum.ERR_21212.setErrorCode(new Result());
		}
		return result;
	}

}

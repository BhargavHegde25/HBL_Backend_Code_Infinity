package com.temenos.dbx.bulkpaymentservices.javaservices;


import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentTransfer;


public class InitiatePayment implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	BulkPaymentTransfer bulkPaymentTransfer = new BulkPaymentTransfer();

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();				
		try {					
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			String recordId = inputParams.get("recordId").toString();

			String paymentId = null ;
			boolean isPaymentSucess = bulkPaymentTransfer.initiatePayment(recordId,request);

			if(isPaymentSucess) {
				paymentId = String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp());
			} else {
				return ErrorCodeEnum.ERR_21229.setErrorCode(new Result());
			}

			result.addParam(new Param("paymentId", paymentId, MWConstants.STRING));
		}
		catch(Exception e) {
			alert.prepareError("Error occured while initiating payments for BulkPayment File at Backend", e).log();
			return ErrorCodeEnum.ERR_21229.setErrorCode(new Result());
		}
		return result;
	}
}

package com.temenos.dbx.bulkpaymentservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentPODBOperations;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public class DeletePaymentOrder implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			
			String recordId = dcRequest.getParameter("recordId");
			String paymentOrderId = dcRequest.getParameter("paymentOrderId");
			
			BulkPaymentPODBOperations bulkPaymentPODBOperations = new BulkPaymentPODBOperations();
			BulkPaymentPODTO outputObj = bulkPaymentPODBOperations.deletePaymentOrder(recordId,paymentOrderId);

			JSONObject resultObject = new JSONObject(outputObj);
			result = JSONToResult.convert(resultObject.toString());
			result.addParam(new Param("paymentOrderId", paymentOrderId, MWConstants.STRING));
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking deletePaymentOrder at Backend: ", e).log();
			return ErrorCodeEnum.ERR_21211.setErrorCode(new Result());
		}
		return result;
	}


}

package com.temenos.dbx.bulkpaymentservices.javaservices;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentPODBOperations;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public class FetchPaymentOrders implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result;

		try {
			BulkPaymentPODBOperations bulkPaymentPODBOperations = new BulkPaymentPODBOperations();

			String recordId = dcRequest.getParameter("recordId");

			List<BulkPaymentPODTO> poLi = bulkPaymentPODBOperations.fetchBulkPaymentSubRecords(recordId);
			
			JSONArray records =new JSONArray(poLi);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.PAYMENTORDERS, records);
			result = JSONToResult.convert(resultObject.toString());
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking fetchPaymentOrders", e).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}

		return result;
	}
}

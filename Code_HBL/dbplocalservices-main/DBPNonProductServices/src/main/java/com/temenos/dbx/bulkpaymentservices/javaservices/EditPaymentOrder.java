package com.temenos.dbx.bulkpaymentservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentPODBOperations;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public class EditPaymentOrder implements JavaService2  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {

			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);

			inputParams.put("modifiedby", CustomerSession.getCustomerId(customer));			
			
			BulkPaymentPODBOperations bulkPaymentPODBOperations = new BulkPaymentPODBOperations();			
			BulkPaymentPODTO inputObj = JSONUtils.parse(new JSONObject(inputParams).toString(), BulkPaymentPODTO.class);
			BulkPaymentPODTO oldPO = bulkPaymentPODBOperations.fetchBulkPaymentOrderbyId(inputObj.getPaymentOrderId());
			String oldStatus = oldPO.getStatus();
			if(!StringUtils.equals(oldStatus, TransactionStatusEnum.NEWLY_ADDED.getStatus()))
			{
				inputObj.setStatus(TransactionStatusEnum.MODIFIED.getStatus());
			}			
			BulkPaymentPODTO outputObj = bulkPaymentPODBOperations.updatePaymentOrder(inputObj);

			JSONObject resultObject = new JSONObject(outputObj);
			result = JSONToResult.convert(resultObject.toString());
			
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking updatePaymentOrder at Backend: ", e).log();
			return ErrorCodeEnum.ERR_21212.setErrorCode(new Result());
		}
		return result;
	}

}

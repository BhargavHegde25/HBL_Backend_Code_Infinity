package com.temenos.infinity.api.transactservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class T24_DeleteTransactionWithApprover implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>)inputArray[1]; 
		String frequencyType = (requestparameters.get(Constants.PARAM_FREQUENCY_TYPE) != null) ? requestparameters.get(Constants.PARAM_FREQUENCY_TYPE).toString() : null;
        String transactionId=(requestparameters.get("referenceId") != null) ? requestparameters.get("referenceId").toString() : null;
		if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(frequencyType)) {
			return cancelOneTimeTransactionWithApproval(transactionId, request);
		} else {
			return cancelRecurringTransactionWithApproval(transactionId, request);
		}
	}
	private static Result cancelOneTimeTransactionWithApproval(String transactionId, DataControllerRequest request) {
		Result result = new Result();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId",transactionId);
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			String response  = DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_REVERSE_PAYMENT_WITH_APPROVER).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			result = JSONToResult.convert(response);
			return result;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at approve transaction: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			return result;
		}
	}
	
	private static Result cancelRecurringTransactionWithApproval(String transactionId, DataControllerRequest request) {
		Result result = new Result();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId",transactionId);
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			String response =  DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.SERVICE_T24IS_STANDINGORDERS).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_REVERSE_STANDINGORDER_WITH_APPROVER).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			result = JSONToResult.convert(response);
			return result;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at approve transaction: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			return result;
		}
	}
}

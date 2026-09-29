package com.temenos.infinity.api.transactservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class T24CancelPaymentWithoutApprover implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];
		String transactionId = (requestparameters.get(Constants.PARAM_TRANSACTION_ID) != null)
				? requestparameters.get(Constants.PARAM_TRANSACTION_ID).toString()
				: null;
		return cancelOneTimeTransactionWithoutApproval(transactionId, request);

	}

	private static Result cancelOneTimeTransactionWithoutApproval(String transactionId, DataControllerRequest request) {
		Result result=new Result();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			String operationName = TemenosConstants.OP_REVERSE_PAYMENT_WITHOUT_APPROVER;
			String fetchTransactionbyId = callfetchTransactionById(transactionId, request);
			JSONObject responseObj = new JSONObject(fetchTransactionbyId);
			JSONArray jsonArray = TemenosUtils.getFirstOccuringArray(responseObj);
			if (jsonArray == null || jsonArray.length() < 1) {
				alert.prepareError("Transaction not found").log();
				result.addParam(new Param("errormsg","Transaction not found"));
				return result;	
			}

			requestParameters.put("transactionId", transactionId);
			alert.prepareError("INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
			String response= DBPServiceExecutorBuilder.builder().withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build()
					.getResponse();
			result = JSONToResult.convert(response);
			return result;
			
		} catch (Exception e) {
			alert.prepareError("Caught exception at approve transaction: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
	}

	private static String callfetchTransactionById(String referenceId, DataControllerRequest request) {

		try {
			referenceId = referenceId.replaceAll("_PSD2", "");
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", referenceId);
			alert.prepareError("INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder().withServiceId(TemenosConstants.T24_IS_PAYMENTS_VIEW)
					.withObjectId(null).withOperationId(TemenosConstants.OP_GET_PAYMENT_ORDER_TRANSACTION_DETAILS)
					.withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request).build().getResponse();

			JSONObject resObj = new JSONObject(response);
			JSONArray transArray = TemenosUtils.getFirstOccuringArray(resObj);
			stringifyCharges(transArray);
			return resObj.toString();
		} catch (Exception e) {
			alert.prepareError("Caught exception fetching transaction details by Id: ", e).log();
			return "{\"errormsg\":\"" + e.getMessage() + "\"}";
		}
	}

	private static JSONArray stringifyCharges(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject js = (jsonArray.getJSONObject(i));
			String charges = js.optString("charges");
			if (!StringUtils.isBlank(charges)) {
				js.put("charges", charges.toString());
			}
		}
		return jsonArray;
	}
	/*
	 * private static String cancelRecurringTransactionWithoutApproval(String
	 * transactionId, DataControllerRequest request) { try { Map<String, Object>
	 * requestParameters = new HashMap<String, Object>();
	 * requestParameters.put("transactionId",transactionId);
	 * alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
	 * return DBPServiceExecutorBuilder.builder().
	 * withServiceId(TemenosConstants.SERVICE_T24IS_STANDINGORDERS).
	 * withObjectId(null).
	 * withOperationId(TemenosConstants.OP_REVERSE_STANDINGORDER_WITHOUT_APPROVER).
	 * withRequestParameters(requestParameters).
	 * withRequestHeaders(request.getHeaderMap()).
	 * withDataControllerRequest(request). build().getResponse(); } catch (Exception
	 * e) { alert.prepareError("Caught exception at approve transaction: ", e).log(); return
	 * "{\"errormsg\":\""+e.getMessage()+"\"}"; } }
	 */
}

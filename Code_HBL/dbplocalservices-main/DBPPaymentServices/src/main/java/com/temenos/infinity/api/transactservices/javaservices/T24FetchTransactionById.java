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
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.transactservices.constants.Constants;

public class T24FetchTransactionById implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		//we should send an requestparams map  with reference id from base class
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];
		String transactionId=(requestparameters.get(Constants.PARAM_TRANSACTION_ID) != null) ? requestparameters.get(Constants.PARAM_TRANSACTION_ID).toString() : null;
		Result result = new Result();
		try {
			//String paymentResponse =  callfetchTransactionById(transactionId, request);
			String paymentResponse =  callfetchHBLTransactionById(transactionId, request);
			JSONObject paymentResponseObj = new JSONObject(paymentResponse);
			JSONArray paymentJsonArray = TemenosUtils.getFirstOccuringArray(paymentResponseObj);
			
			String standingResponse =  callfetchRecurringTransactionById(transactionId, request);
			JSONObject standingResponseObj = new JSONObject(standingResponse);
			JSONArray standingJsonArray = TemenosUtils.getFirstOccuringArray(standingResponseObj);
			
			if(paymentJsonArray != null && paymentJsonArray.length() > 0 && paymentJsonArray.get(0) != null) {
				result = JSONToResult.convert(paymentResponse);
			}
			if(standingJsonArray != null && standingJsonArray.length() > 0 && standingJsonArray.get(0) != null) {
				result = JSONToResult.convert(standingResponse);
			}
			else {
				result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"No records returned\"}"));
				return result;
			}
			return result;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception fetching transaction details by Id: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			return result;
		}
	}
	private static String callfetchTransactionById(String referenceId, DataControllerRequest request) {
         
		try {
			referenceId = referenceId.replaceAll("_PSD2","");
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", referenceId);
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.T24_IS_PAYMENTS_VIEW).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_GET_PAYMENT_ORDER_TRANSACTION_DETAILS).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			
			JSONObject resObj = new JSONObject(response);
			JSONArray transArray = TemenosUtils.getFirstOccuringArray(resObj);
			stringifyCharges(transArray);
			return resObj.toString();
		}
		catch (Exception e) {
			alert.prepareError("Caught exception fetching transaction details by Id: ", e).log();
			return "{\"errormsg\":\""+e.getMessage()+"\"}";
		}
	}
	private static String callfetchRecurringTransactionById(String referenceId, DataControllerRequest request) {

		try {
			referenceId = referenceId.replaceAll("_PSD2","");
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", referenceId);
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.T24_IS_PAYMENTS_VIEW).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_GET_STANDING_ORDER_TRANSACTION_DETAILS).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			
			JSONObject resObj = new JSONObject(response);
			JSONArray transArray = TemenosUtils.getFirstOccuringArray(resObj);
			stringifyCharges(transArray);
			return resObj.toString();
		}
		catch (Exception e) {
			alert.prepareError("Caught exception fetching transaction details by Id: ", e).log();
			return "{\"errormsg\":\""+e.getMessage()+"\"}";
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
	private static String callfetchHBLTransactionById(String referenceId, DataControllerRequest request) {

		try {
			JSONArray array = new JSONArray();
			JSONObject reponse = new JSONObject();
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("paymentOrderId", referenceId);
			alert.prepareError("callfetchHBLTransactionById INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder().withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS)
					.withObjectId(null).withOperationId("getTransactionStatus")
					.withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resObj = new JSONObject(response);
			alert.prepareError("callfetchHBLTransactionById:response of transaction status response " + resObj).log();
			array=resObj.optJSONArray("body");
			if(array!=null) {
			array.getJSONObject(0).put("fromAccountNumber", resObj.get("fromAccountNumber"));
			array.getJSONObject(0).put("creditAccountId", resObj.get("creditAccountId"));
			reponse.put("response", array);
			}
			return reponse.toString();
		} catch (Exception e) {
			alert.prepareError("Caught exception fetching transaction details by Id: ", e).log();
			return "{\"errormsg\":\"" + e.getMessage() + "\"}";
		}
	}
}

package com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public class BulkPaymentPOBackendDelegateImpl implements BulkPaymentPOBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public BulkPaymentPODTO addPaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.ADDBULKPAYMENTPO;
		String response = null;
		BulkPaymentPODTO poResponseDTO ;
		Map<String, Object> requestParameters;
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentPODTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			poResponseDTO = JSONUtils.parse(responseObj.toString(), BulkPaymentPODTO.class);
		} catch (JSONException e) {
			alert.prepareError("Error in creating Bulk Payment payment order", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in addPaymentOrder", e).log();
			return null;
		}
		return poResponseDTO;
	}
	
	@Override
	public List<BulkPaymentPODTO> fetchPaymentOrders(String recordId, DataControllerRequest dcr) {

		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.FETCHBULKPAYMENTPO;
		String response = null;
		List<BulkPaymentPODTO> paymentOrderLi;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId",recordId);
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			paymentOrderLi = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentPODTO.class);
		} catch (JSONException e) {
			alert.prepareError("Error in fetching Bulk Payment payment orders", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetchPaymentOrders", e).log();
			return null;
		}

		return paymentOrderLi;
	}
	
	@Override
	public BulkPaymentPODTO updatePaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr) {

		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.EDITBULKPAYMENTPO;
		
		BulkPaymentPODTO outputObj;
		Map<String, Object> requestParameters;

		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentPODTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}
		String response = null;

		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();

			JSONObject responseObj = new JSONObject(response);
			outputObj = JSONUtils.parse(responseObj.toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to edit payment order: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updatePaymentOrder: ", e).log();
			return null;
		}
		return outputObj;
	}
	
	@Override
	public BulkPaymentPODTO deletePaymentOrder(String recordId, String paymentOrderId, DataControllerRequest dcr) {

		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.DELETEBULKPAYMENTPO;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", recordId);
		requestParams.put("paymentOrderId", paymentOrderId);
		
		String deleteResponse = null;
		BulkPaymentPODTO outputObj;

		try {
			deleteResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			
			JSONObject responseObj = new JSONObject(deleteResponse);
			outputObj = JSONUtils.parse(responseObj.toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to delete bulk payment payment Order", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at deletePaymentOrder", e).log();
			return null;
		}
		return outputObj;
	}
	
	@Override
	public List<BulkPaymentPODTO> approvePaymentOrders(String recordId, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSORCH;
		String operationName = OperationName.APPROVEPAYMENTORDERS;
						
		List<BulkPaymentPODTO> paymentOrders = fetchPaymentOrders(recordId,dcr);		
		if(paymentOrders == null) {
			return null;
		}
		
		Set<String> paymentOrderIdsList = new HashSet<String>();				
		for(BulkPaymentPODTO dto : paymentOrders)
			paymentOrderIdsList.add(dto.getPaymentOrderId());
		
		if(paymentOrderIdsList.size() == 0) {
			return null;
		}
		
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("paymentOrderId", String.join(",", paymentOrderIdsList));
		inputParams.put("loop_count", String.valueOf(paymentOrderIdsList.size()));
		List<BulkPaymentPODTO> output = new ArrayList<BulkPaymentPODTO>();
		
		try {
			String createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), BulkPaymentPODTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception at " + operationName + " entry: " + e).log();
			return null;
		}
		return output;
	}
}

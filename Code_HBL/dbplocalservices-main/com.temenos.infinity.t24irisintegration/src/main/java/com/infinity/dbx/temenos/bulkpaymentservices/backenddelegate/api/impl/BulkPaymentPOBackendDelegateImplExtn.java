package com.infinity.dbx.temenos.bulkpaymentservices.backenddelegate.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.controller.DataControllerRequest;

import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl.BulkPaymentPOBackendDelegateImpl;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;


public class BulkPaymentPOBackendDelegateImplExtn extends BulkPaymentPOBackendDelegateImpl {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public BulkPaymentPODTO addPaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_CREATE_PO;
		String response = null;
		BulkPaymentPODTO poResponseDTO ;
		Map<String, Object> requestParameters;
		
		String feesPaidBy = bulkPaymentPODTO.getFeesPaidBy();
		String accountNumber = bulkPaymentPODTO.getAccountNumber();
		String paymentMethod = bulkPaymentPODTO.getPaymentMethod();		
		String batchMode = bulkPaymentPODTO.getBatchMode();
		String paymentOrderProduct = bulkPaymentPODTO.getPaymentOrderProduct();
		String productPaymentId ="";		
		String creditAccountId = "";
		String creditAccountIBAN = "";
		String beneficiaryAccountId = "";				

		if (StringUtils.equals(batchMode, "MULTI")) {
			if (StringUtils.equals(paymentMethod, "Internal")) {
				//productPaymentId = "ACOTHER";
				feesPaidBy = "";
				if (StringUtils.isNumeric(accountNumber)) {
					beneficiaryAccountId = accountNumber;
					bulkPaymentPODTO.setAccountNumber("");
				}
				//else {
				//	creditAccountIBAN = accountNumber;
				//}
				//bulkPaymentPODTO.setAccountNumber("");			
				//bulkPaymentPODTO.setRecipientName("");
			} else if (StringUtils.equals(paymentMethod, "Domestic")) {
				if (StringUtils.isNumeric(accountNumber)) {
					beneficiaryAccountId = accountNumber;
					bulkPaymentPODTO.setAccountNumber("");
				}
				//productPaymentId = "SEPA";
			} else if (StringUtils.equals(paymentMethod, "International")) {
				if (StringUtils.isNumeric(accountNumber)) {
					beneficiaryAccountId = accountNumber;
					bulkPaymentPODTO.setAccountNumber("");
				}
				//productPaymentId = "INATIONAL";
			}
			productPaymentId = paymentOrderProduct;
		}

		else {			
			if (StringUtils.equals(paymentMethod, "Internal")) {				
				feesPaidBy = "";											
			}	
			if (StringUtils.isNumeric(accountNumber)) {
				beneficiaryAccountId = accountNumber;
				bulkPaymentPODTO.setAccountNumber("");
			}
			productPaymentId = paymentOrderProduct;
		}
		
		String chargeBearer = _getChargeBearer(feesPaidBy);			
		
			
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentPODTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}
		
		requestParameters.put("paymentMethod", productPaymentId);		
		requestParameters.put("feesPaidBy", chargeBearer);
		requestParameters.put("creditAccountId", creditAccountId);
		requestParameters.put("creditAccountIBAN", creditAccountIBAN);
		requestParameters.put("beneficiaryAccountId", beneficiaryAccountId);
		requestParameters.put("sourceType", "Manual");
				
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
			
			if (poResponseDTO != null 
					&& StringUtils.isEmpty(poResponseDTO.getDbpErrMsg())
					&& StringUtils.isNotEmpty(poResponseDTO.getPaymentOrderId())) {
				BulkPaymentPODTO authResponseDTO = authorisePaymentOrder(poResponseDTO.getPaymentOrderId(),dcr);
			}
			
		} catch (JSONException e) {
			alert.prepareError("Error in creating Bulk Payment payment order", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in addPaymentOrder", e).log();
			return null;
		}
		return poResponseDTO;

	}
	
	private String _getChargeBearer(String feesPaidBy) {
		
		String chargeBearer = "";
		
		if (StringUtils.equals(feesPaidBy, "Shared")) {
			chargeBearer = "SHA";
		}
		else if (StringUtils.equals(feesPaidBy, "Beneficiary")) {
			chargeBearer = "BEN";
		}
		else if (StringUtils.equals(feesPaidBy, "Me")) {
			chargeBearer = "OUR";
		}			
		return chargeBearer;
	}
	
	
	@Override
	public List<BulkPaymentPODTO> fetchPaymentOrders(String recordId, DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_FETCH_PO;
		String response = null;
		List<BulkPaymentPODTO> paymentOrderList;
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
			JSONArray jsonArray = TemenosUtils.getFirstOccuringArray(responseObj);
			paymentOrderList = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentPODTO.class);
		} catch (JSONException e) {
			alert.prepareError("Error in fetching Bulk Payment payment orders", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetchPaymentOrders", e).log();
			return null;
		}

		return paymentOrderList;		
	}
	
	@Override
	public BulkPaymentPODTO updatePaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_EDIT_PO;
				
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

		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_REMOVE_PO;

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
	

	public BulkPaymentPODTO authorisePaymentOrder(String paymentOrderId, DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_AUTHORISE_PO;

		Map<String, Object> requestParams = new HashMap<String, Object>();		
		requestParams.put("paymentOrderId", paymentOrderId);
		
		String authResponse = null;
		BulkPaymentPODTO outputObj;

		try {
			authResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			
			JSONObject responseObj = new JSONObject(authResponse);
			outputObj = JSONUtils.parse(responseObj.toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to authorise bulk payment payment Order", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at authorisePaymentOrder", e).log();
			return null;
		}
		return outputObj;
	}
	
	@Override
	public List<BulkPaymentPODTO> approvePaymentOrders(String recordId, DataControllerRequest dcr) {
		
		String serviceName = TemenosConstants.SERVICE_T24OS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_APPROVE_PO;
						
		List<BulkPaymentPODTO> paymentOrders = fetchPaymentOrders(recordId,dcr);		
		if(paymentOrders == null) {
			return null;
		}
		
		Set<String> paymentOrderIdsList = new HashSet<String>();
		for (BulkPaymentPODTO dto : paymentOrders) {
			if (StringUtils.isEmpty(dto.getStatus()))
				paymentOrderIdsList.add(dto.getPaymentOrderId());
		}
		
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
			JSONArray responseArray = TemenosUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), BulkPaymentPODTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception at " + operationName + " entry: " + e).log();
			return null;
		}
		return output;
	}
}

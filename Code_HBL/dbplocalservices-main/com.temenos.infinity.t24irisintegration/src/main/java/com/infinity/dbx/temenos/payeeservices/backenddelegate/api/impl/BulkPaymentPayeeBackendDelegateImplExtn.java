package com.infinity.dbx.temenos.payeeservices.backenddelegate.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import com.temenos.dbx.product.payeeservices.backenddelegate.impl.BulkPaymentsPayeeBackendDelegateImpl;
import com.temenos.dbx.product.payeeservices.dto.BulkPaymentPayeeDTO;

public class BulkPaymentPayeeBackendDelegateImplExtn extends  BulkPaymentsPayeeBackendDelegateImpl{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public BulkPaymentPayeeDTO getBeneficiaryNameFromAccountId(String accountNumber,DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24IS_ACCOUNTS;
		String operationName = TemenosConstants.OP_GET_BENEFICIARY_NAME;
		String response = null;
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("accountNumber",accountNumber);		
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(responseObj);			
			bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);	
			bulkPaymentPayeeDTO.setAccountNumber(accountNumber);
			if(StringUtils.isBlank(bulkPaymentPayeeDTO.getBeneficiaryName()))
			{
				bulkPaymentPayeeDTO.setDbpErrMsg(TemenosConstants.INVALID_ACCOUNT_NUMBER);	
			}
			
		} catch (JSONException e) {
			alert.prepareError("Error occured in fetching Beneficiary name.", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching Beneficiary name", e).log();
			return null;
		}
		return bulkPaymentPayeeDTO;
	}		
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public BulkPaymentPayeeDTO validateIBANandGetBankDetails(String iban, DataControllerRequest dcr) {
						
		String serviceName = TemenosConstants.SERVICE_T24OS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_VALIDATE_IBAN_AND_GET_BANK_DETAILS;
		String response = null;
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("iban", iban);
		

		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();

			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(responseObj);
			bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);

			if (StringUtils.isNotBlank(bulkPaymentPayeeDTO.getDbpErrMsg())) {				
				bulkPaymentPayeeDTO.setDbpErrMsg(TemenosConstants.INVALID_IBAN);
			}
			if (StringUtils.isNotBlank(bulkPaymentPayeeDTO.getBIC())) {				
				bulkPaymentPayeeDTO.setSwift(bulkPaymentPayeeDTO.getBIC());
			}
			
				} catch (JSONException e) {
					alert.prepareError("Error occured in fetching Bank details.", e).log();
					return null;
				} catch (Exception e) {
					alert.prepareError("Error occured in fetching Bank details", e).log();
					return null;
				}

		return bulkPaymentPayeeDTO;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public BulkPaymentPayeeDTO getBankDetailsFromBIC(String swiftCode, DataControllerRequest dcr) {
				
		String serviceName = TemenosConstants.SERVICE_T24IS_TRANSACTIONS;
		String operationName = TemenosConstants.OP_VALIDATE_BIC_AND_GET_BANK_NAME;
		String response = null;
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("swiftCode", swiftCode);
		

		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();

			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(responseObj);
			bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);
					
			if(StringUtils.isBlank(bulkPaymentPayeeDTO.getBankName()))
			{
				bulkPaymentPayeeDTO.setDbpErrMsg(TemenosConstants.INVALID_BIC);	
			}
			
		} catch (JSONException e) {
			alert.prepareError("Error occured in fetching Bank details.", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching Bank details", e).log();
			return null;
		}
		return bulkPaymentPayeeDTO;
	}
}
	

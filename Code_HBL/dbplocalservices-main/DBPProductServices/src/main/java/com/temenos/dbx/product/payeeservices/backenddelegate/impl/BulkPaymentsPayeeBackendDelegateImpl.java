package com.temenos.dbx.product.payeeservices.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;

import org.apache.commons.lang.StringUtils;
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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BulkPaymentsPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.BulkPaymentPayeeDTO;

public class BulkPaymentsPayeeBackendDelegateImpl implements BulkPaymentsPayeeBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public BulkPaymentPayeeDTO getBeneficiaryNameFromAccountId(String accountNumber,
			DataControllerRequest dcr) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_USERACCOUNTS_GET;
		String response = null;
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		Map<String, Object> requestParameters = new HashMap<String, Object>();		

		String filter = "";
		if (accountNumber != null && !accountNumber.isEmpty()) {
			filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNumber;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

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
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);			
			bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);
			
			if (StringUtils.isNotBlank(bulkPaymentPayeeDTO.getBeneficiaryName())) {
				JSONObject jsonObj = new JSONObject(bulkPaymentPayeeDTO.getBeneficiaryName());
				bulkPaymentPayeeDTO.setBeneficiaryName(jsonObj.getString("fullname"));
			}
			
		} catch (JSONException e) {
			alert.prepareError("Error occured in fetching Beneficiary name.", e).log();
			bulkPaymentPayeeDTO.setDbpErrMsg(ErrorConstants.INVALID_ACCOUNT_NUMBER);				
			return bulkPaymentPayeeDTO;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching Beneficiary name", e).log();
			return null;
		}
		return bulkPaymentPayeeDTO;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public BulkPaymentPayeeDTO validateIBANandGetBankDetails(String iban, DataControllerRequest dcr) {
				
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		
		if (iban != null && !iban.isEmpty()) {
			
			if(StringUtils.equals(iban, "00000"))
			{	
				bulkPaymentPayeeDTO.setDbpErrMsg(ErrorConstants.INVALID_IBAN);				
				return bulkPaymentPayeeDTO;
			}
			else {

				try {
					SecureRandom secureRand = new SecureRandom();
					String bic = "BICIND" + (10 + secureRand.nextInt(89));
					String bankName = "BANK" + (1 + secureRand.nextInt(2) * 10000 + secureRand.nextInt(10000));

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("swift", bic);
					jsonObject.put("bankName", bankName);
					JSONArray jsonArray = new JSONArray();
					jsonArray.put(jsonObject);
					bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);

				} catch (JSONException e) {
					alert.prepareError("Error occured in fetching Bank details.", e).log();
					return null;
				} catch (Exception e) {
					alert.prepareError("Error occured in fetching Bank details", e).log();
					return null;
				}
			}
		}
		return bulkPaymentPayeeDTO;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public BulkPaymentPayeeDTO getBankDetailsFromBIC(String bic, DataControllerRequest dcr) {
				
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = new BulkPaymentPayeeDTO();
		
		if (bic != null && !bic.isEmpty()) {
			
			if(StringUtils.equals(bic, "11111"))
			{
				bulkPaymentPayeeDTO.setDbpErrMsg(ErrorConstants.INVALID_SWIFT_BIC);				
				return bulkPaymentPayeeDTO;
			}
			else {

				try {
					SecureRandom secureRand = new SecureRandom();				
					String bankName = "BANK" + (1 + secureRand.nextInt(2) * 10000 + secureRand.nextInt(10000));

					JSONObject jsonObject = new JSONObject();					
					jsonObject.put("bankName", bankName);
					JSONArray jsonArray = new JSONArray();
					jsonArray.put(jsonObject);
					bulkPaymentPayeeDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentPayeeDTO.class);

				} catch (JSONException e) {
					alert.prepareError("Error occured in fetching Bank details.", e).log();
					return null;
				} catch (Exception e) {
					alert.prepareError("Error occured in fetching Bank details", e).log();
					return null;
				}
			}
		}
		return bulkPaymentPayeeDTO;
	}
	
	@Override
	public List<BulkPaymentPayeeDTO> getAllBICsAndBankDetails(DataControllerRequest dcr) {
		
		List<BulkPaymentPayeeDTO> bankDetails = null;
		
		try {
			SecureRandom secureRand = new SecureRandom();
			
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < 10; i++) {
				String bic = "BICIND" + (10 + secureRand.nextInt(89));
				String bankName = "BANK" + (1 + secureRand.nextInt(2) * 10000 + secureRand.nextInt(10000));
				String city = "CITY" + (1 + secureRand.nextInt(2) * 10000 + secureRand.nextInt(10000));
				String country = "COUNTRY" + (1 + secureRand.nextInt(2) * 10000 + secureRand.nextInt(10000));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("swift", bic);
				jsonObject.put("bankName", bankName);
				jsonObject.put("city", city);
				jsonObject.put("country", country);
				jsonArray.put(jsonObject);
			}
			
			bankDetails = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentPayeeDTO.class);

		} catch (JSONException e) {
			alert.prepareError("Error occured in fetching Bank details.", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching Bank details", e).log();
			return null;
		}
		return bankDetails;
	}
}

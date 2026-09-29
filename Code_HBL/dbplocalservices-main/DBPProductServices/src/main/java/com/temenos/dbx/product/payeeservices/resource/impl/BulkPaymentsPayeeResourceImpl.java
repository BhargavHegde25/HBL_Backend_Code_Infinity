package com.temenos.dbx.product.payeeservices.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import org.json.JSONArray;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BulkPaymentsPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.BulkPaymentPayeeDTO;
import com.temenos.dbx.product.payeeservices.resource.api.BulkPaymentsPayeeResource;

public class BulkPaymentsPayeeResourceImpl implements BulkPaymentsPayeeResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	BulkPaymentsPayeeBackendDelegate bulkPaymentsPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentsPayeeBackendDelegate.class);
	
	@Override
	public Result getBeneficiaryNameFromAccountId(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
				
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String accountNumber = inputParams.get("accountNumber") != null ? inputParams.get("accountNumber").toString() : null;
		
		if (accountNumber == null || accountNumber.isEmpty()) {
			alert.prepareError("Account number is Empty.").log();
			return ErrorCodeEnum.ERR_21262.setErrorCode(new Result());
		}
		Result result = new Result();
		
		BulkPaymentPayeeDTO bulkPaymentPayeeDTO = bulkPaymentsPayeeBackendDelegate.getBeneficiaryNameFromAccountId(accountNumber, request);
		
		if (bulkPaymentPayeeDTO == null) {
			alert.prepareError("Error occurred while fetching  beneficiary details from backend").log();
			return ErrorCodeEnum.ERR_21261.setErrorCode(new Result());
		}
		
		if (StringUtils.isNotBlank(bulkPaymentPayeeDTO.getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching beneficiary details from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), bulkPaymentPayeeDTO.getDbpErrMsg());
		}
		
		try {			
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.BANK_DETAILS, new JSONObject(bulkPaymentPayeeDTO));
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		
		return result;
	}
	
	@Override
	public Result validateIBANandGetBankDetails(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
				
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String accountNumber = inputParams.get("iban") != null ? inputParams.get("iban").toString() : null;
		
		if (accountNumber == null || accountNumber.isEmpty()) {
			alert.prepareError("Account number is Empty.").log();
			return ErrorCodeEnum.ERR_21262.setErrorCode(new Result());
		}
		Result result = new Result();
		
		BulkPaymentPayeeDTO bankDetails = bulkPaymentsPayeeBackendDelegate.validateIBANandGetBankDetails(accountNumber, request);
		
		if (bankDetails == null) {
			alert.prepareError("Error occurred while fetching  bank details from backend").log();
			return ErrorCodeEnum.ERR_21260.setErrorCode(new Result());
		}
		
		if (StringUtils.isNotBlank(bankDetails.getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bank details from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), bankDetails.getDbpErrMsg());
		}
		
		try {			
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.BANK_DETAILS, new JSONObject(bankDetails));
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		
		return result;
	}
	
	@Override
	public Result getBankDetailsFromBIC(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
				
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String swiftCode = inputParams.get("swiftCode") != null ? inputParams.get("swiftCode").toString() : null;
		
		if (swiftCode == null || swiftCode.isEmpty()) {
			alert.prepareError("Swift/BIC code is Empty.").log();
			return ErrorCodeEnum.ERR_21262.setErrorCode(new Result());
		}
		Result result = new Result();
		
		BulkPaymentPayeeDTO bankDetails = bulkPaymentsPayeeBackendDelegate.getBankDetailsFromBIC(swiftCode, request);
		
		if (bankDetails == null) {
			alert.prepareError("Error occurred while fetching  bank details from backend").log();
			return ErrorCodeEnum.ERR_21260.setErrorCode(new Result());
		}
		
		if (StringUtils.isNotBlank(bankDetails.getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bank details from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), bankDetails.getDbpErrMsg());
		}
		
		try {			
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.BANK_DETAILS, new JSONObject(bankDetails));
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		
		return result;
	}
	
	@Override
	public Result getAllBICsAndBankDetails(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {

		Result result = new Result();
		
		List<BulkPaymentPayeeDTO> bankDetails = bulkPaymentsPayeeBackendDelegate.getAllBICsAndBankDetails(request);
		
		if (bankDetails == null) {
			alert.prepareError("Error occurred while fetching  bank details from backend").log();
			return ErrorCodeEnum.ERR_21260.setErrorCode(new Result());
		}
		
		if (bankDetails.size() > 0 && StringUtils.isNotBlank(bankDetails.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bank details from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), bankDetails.get(0).getDbpErrMsg());
		}
		
		try {
			JSONArray resArray = new JSONArray(bankDetails);
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.BANK_DETAILS, resArray);
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		
		return result;
	}

}

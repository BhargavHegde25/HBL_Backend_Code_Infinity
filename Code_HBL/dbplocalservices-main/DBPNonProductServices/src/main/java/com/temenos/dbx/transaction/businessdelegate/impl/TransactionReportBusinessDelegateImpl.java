package com.temenos.dbx.transaction.businessdelegate.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.transaction.businessdelegate.api.TransactionReportBusinessDelegate;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public class TransactionReportBusinessDelegateImpl implements TransactionReportBusinessDelegate {

	public static final String TRANSACTION_DATASET_NAME = "Transactions";
	public static final String DEFAULT_TOKEN = "STRING-TO-FAIL-AUTHENTICATION";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public TransactionDTO getTransactionById(String trsactionId, String claimsToken) {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("transactionId", trsactionId);
		diagnostic.prepareDebug("claimsToken :"+claimsToken).log();
		diagnostic.prepareDebug("trsactionId :"+trsactionId).log();
		if(StringUtils.isBlank(claimsToken)) {
			claimsToken = DEFAULT_TOKEN;
		}
		try {
			JsonObject transaction = ServiceCallHelper.invokeServiceAndGetJson(inputParams, 
					null, URLConstants.TRANSACTIONS_POST, claimsToken);
			if(!HelperMethods.hasErrorOpstatus(transaction)) {
				return getTransactionDTO(transaction);
			}
		} catch (Exception e) {
			alert.prepareError("Error while calling get transaction service", e).log();
		}
		return null;
	}
	
	public Map<String, String> getHeaders(DataControllerRequest request) {
        Map<String, String> headers = new HashMap<>();
        if(request != null) {
        	headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, request.getParameter("authToken"));
        }
        return headers;
    }
	
	public String getTransactionDatasetName() {
		return TRANSACTION_DATASET_NAME;
	}
	
	public TransactionDTO getTransactionDTO(JsonObject transaction) throws IOException {
		if(JSONUtil.hasKey(transaction, getTransactionDatasetName())) {
			JsonArray transJsonArray = transaction.getAsJsonArray(getTransactionDatasetName());
			if(transJsonArray.size() > 0) {
				TransactionDTO transactionDTO = JSONUtils.parse(transJsonArray.get(0).toString(),
						TransactionDTO.class);
				updateFromAccountDetails(transactionDTO);
				updateToRecipientDetails(transactionDTO);
				updateTransactionDetails(transactionDTO);
				return transactionDTO;
			}
		}
		return null;
	}
	
	public void updateTransactionDetails(TransactionDTO transactionDTO) {
		if(StringUtils.isNotBlank(transactionDTO.getFrequencyStartDate())) {
			transactionDTO.setFrequencyStartDate(
					getFormattedTransactionDate(transactionDTO.getFrequencyStartDate()));
		}
		if(StringUtils.isNotBlank(transactionDTO.getFrequencyEndDate())) {
			transactionDTO.setFrequencyEndDate(
					getFormattedTransactionDate(transactionDTO.getFrequencyEndDate()));
		}
		if(StringUtils.isNotBlank(transactionDTO.getScheduledDate())) {
			transactionDTO.setScheduledDate(
					getFormattedTransactionDate(transactionDTO.getScheduledDate()));
		}
		transactionDTO.setTransactionDate(
				getFormattedTransactionDate(transactionDTO.getTransactionDate()));
		transactionDTO.setTransactionCurrency(
				getCurrencySymbol(transactionDTO.getTransactionCurrency()));
	}


	public void updateToRecipientDetails(TransactionDTO transactionDTO) {
		String toAccountNum = "";
		if(StringUtils.isNotBlank(transactionDTO.getToAccountNumber())) {
			toAccountNum = transactionDTO.getToAccountNumber();
		}
		if(StringUtils.isNotBlank(transactionDTO.getToNickName())) {
			transactionDTO.setToNickName(transactionDTO.getToNickName()
					+" "+toAccountNum);
		}else if(StringUtils.isNotBlank(transactionDTO.getToAccountName())) {
			transactionDTO.setToAccountName(transactionDTO.getToAccountName()
					+" "+toAccountNum);
		}
	}


	public void updateFromAccountDetails(TransactionDTO transactionDTO) {
		String fromAccountNickName = transactionDTO.getFromNickName();
		
		if(StringUtils.isBlank(fromAccountNickName)) {
			fromAccountNickName = transactionDTO.getFromAccountType();
		}
		
		if(StringUtils.isNotBlank(transactionDTO.getFromAccountNumber())) {
			String fromAccountNumber = StringUtils.substring(transactionDTO.getFromAccountNumber(),
					transactionDTO.getFromAccountNumber().length()-4);
			fromAccountNickName = fromAccountNickName + " " + fromAccountNumber;
		}
		
		transactionDTO.setFromNickName(fromAccountNickName);
	}
	
	public String getCurrencySymbol(String currencyCode) {
		Locale locale = Locale.FRANCE;
		
		if("USD".equalsIgnoreCase(currencyCode)) {
			locale = Locale.US;
		}
		Currency curr = Currency.getInstance(locale);
		return curr.getSymbol(locale);
	}
	
	public String getFormattedTransactionDate(String transactionDate) {
		try {
			return HelperMethods.convertDateFormat(transactionDate, "MM/dd/YYYY");
		} catch (ParseException e) {
			alert.prepareError("Error while converting transaction date format",e).log();
			return transactionDate;
		}
	}

}

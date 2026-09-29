package com.bct.custom.businessdeligate.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.impl.BillPaymentHistoryBackendDelegateImpl;
import com.bct.custom.businessdeligate.api.BillPaymentHistoryBusinessDelegate;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class BillPaymentHistoryBusinessDeligateImpl implements BillPaymentHistoryBusinessDelegate {
	public static final String TRANSACTION_DATASET_NAME = "billpaytransfers";//"Transactions";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	BillPaymentHistoryBackendDeligate backendDeligate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(BillPaymentHistoryBackendDeligate.class);

	@Override
	public JSONArray getBillPaymentHistory(String biilerId, Map<String, Object> inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		return backendDeligate.getBillPaymentHistory(biilerId, inputArray, dcRequest);
	}
	@Override
	public TransactionDTO getBillTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)throws ApplicationException {
		try {
			JsonObject transaction = backendDeligate.getBillTransactionById(inputParams, dcRequest);
			if(!HelperMethods.hasErrorOpstatus(transaction)) {
				return getTransactionDTO(transaction);
			}
		} catch (Exception e) {
			alert.prepareError("Error while getBillTransactionById", e).log();
		}
		return null;
	}
	
	
	public String getTransactionDatasetName() {
		return TRANSACTION_DATASET_NAME;
	}
	
	
	public TransactionDTO getTransactionDTO(JsonObject transaction) throws IOException {
		if(JSONUtil.hasKey(transaction, getTransactionDatasetName())) {
			JsonArray transJsonArray = transaction.getAsJsonArray(getTransactionDatasetName());
			if(transJsonArray.size() > 0) {
				JsonObject transactionRecord =  transJsonArray.get(0).getAsJsonObject();
				TransactionDTO transactionDTO = JSONUtils.parse(transactionRecord.toString(),
						TransactionDTO.class);
				
					transactionDTO.setTransactionId(transactionRecord.get("confirmationNumber").getAsString());	
					transactionDTO.setPayeeNickName(transactionRecord.get("billerId").getAsString());	
					transactionDTO.setTransactionsNotes(transactionRecord.get("notes").getAsString());	
					transactionDTO.setRecurrenceDesc(transactionRecord.get("status").getAsString());	
					transactionDTO.setTransactionDate(transactionRecord.get("createdts").getAsString());
					transactionDTO.setFromNickName(transactionDTO.getFromAccountNumber());
					transactionDTO.setToNickName(transactionDTO.getToAccountNumber());
					transactionDTO.setToAccountName(transactionDTO.getToAccountNumber());
					//transactionDTO.setAmount(transactionRecord.get("transactionAmount").toString());
					
				//updateFromAccountDetails(transactionDTO);
				//updateToRecipientDetails(transactionDTO);
				updateTransactionDetails(transactionDTO);
				return transactionDTO;
			}
		}
		return null;
	}
	public TransactionDTO getTransactionDTO(JsonObject transaction, DataControllerRequest request) throws IOException {
		if(JSONUtil.hasKey(transaction, getTransactionDatasetName())) {
			JsonArray transJsonArray = transaction.getAsJsonArray(getTransactionDatasetName());
			if(transJsonArray.size() > 0) {
				TransactionDTO transactionDTO = JSONUtils.parse(transJsonArray.get(0).toString(),
						TransactionDTO.class);
				if (transactionDTO.getTransactionId() == null) {
					transactionDTO.setTransactionId(transJsonArray.get(0).getAsJsonObject().get("Id").toString());	
				}
				updateFromAccountDetails(transactionDTO, request);
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
		//transactionDTO.setTransactionCurrency(
				//getCurrencySymbol(transactionDTO.getTransactionCurrency()));
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
			return HelperMethods.convertDateFormat(transactionDate, "dd/MM/YYYY");
		} catch (ParseException e) {
			alert.prepareError("Error while converting transaction date format",e).log();
			return transactionDate;
		}
	}
	public void updateFromAccountDetails(TransactionDTO transactionDTO, DataControllerRequest request) {
		String fromAccountNickName = transactionDTO.getFromNickName();
		
		if(StringUtils.isBlank(fromAccountNickName)) {
			fromAccountNickName = transactionDTO.getFromAccountType();
		}
		if(StringUtils.isBlank(fromAccountNickName)) {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String customerId = CustomerSession.getCustomerId(customer);
			JSONObject intAccounts = getInternalBankAccountsFromSession(customerId);
			fromAccountNickName = getAccountName(intAccounts, transactionDTO.getFromAccountNumber());
		}
		
		if(StringUtils.isNotBlank(transactionDTO.getFromAccountNumber())) {
			String fromAccountNumber = StringUtils.substring(transactionDTO.getFromAccountNumber(),
					transactionDTO.getFromAccountNumber().length()-4);
			fromAccountNickName = fromAccountNickName + " " + fromAccountNumber;
		}
		
		transactionDTO.setFromNickName(fromAccountNickName);
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
	public JSONObject getInternalBankAccountsFromSession(String customerId) {
		SessionMap internalAccntsMap = (SessionMap) MemoryManagerUtils.retrieve("INTERNAL_BANK_ACCOUNTS" + customerId);
		if (StringUtils.isNotBlank(internalAccntsMap.toString()))
			return new JSONObject(internalAccntsMap.toString());
		else
			return new JSONObject();
	}

	public String getAccountName(JSONObject internalAccounts, String accountId) {
		String accountName = "";
		if (internalAccounts.has(accountId)) {
			JSONObject account = new JSONObject(internalAccounts.get(accountId).toString());
			if (account.has("accountName")) {
				return account.getString("accountName");
			}
		}
		return accountName;
	}


}

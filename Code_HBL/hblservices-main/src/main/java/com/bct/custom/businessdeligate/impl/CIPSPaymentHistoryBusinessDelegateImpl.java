package com.bct.custom.businessdeligate.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.api.CIPSPaymentHistoryBackendDeligate;
import com.bct.custom.businessdeligate.api.CIPSPaymentHistoryBusinessDelegate;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CIPSPaymentHistoryBusinessDelegateImpl implements CIPSPaymentHistoryBusinessDelegate{
	public static final String TRANSACTION_DATASET_NAME = "interbankfundtransfers";//"Transactions";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CIPSPaymentHistoryBackendDeligate backendDeligate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(CIPSPaymentHistoryBackendDeligate.class);
	@Override
	public JSONArray getCIPSPaymentHistory(String biilerId, Map<String, Object> inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionDTO getCIPSTransactionById(Map<String, Object> inputParams, DataControllerRequest request)
			throws ApplicationException {
		try {
			JsonObject transaction = backendDeligate.getCIPSTransactionById(inputParams, request);
			if(!HelperMethods.hasErrorOpstatus(transaction)) {
				return getTransactionDTO(transaction);
			}
		} catch (Exception e) {
			alert.prepareError("Error while getBillTransactionById", e).log();
		}
		return null;
	}
	public TransactionDTO getTransactionDTO(JsonObject transaction) throws IOException {
		TransactionDTO transactionDTO =null;
		if(JSONUtil.hasKey(transaction, TRANSACTION_DATASET_NAME)) {
			JsonArray transJsonArray = transaction.getAsJsonArray(TRANSACTION_DATASET_NAME);
			if(transJsonArray.size() > 0) {
				JsonObject transactionRecord =  transJsonArray.get(0).getAsJsonObject();
				 transactionDTO = JSONUtils.parse(transactionRecord.toString(),
						TransactionDTO.class);
					transactionDTO.setTransactionId(transactionRecord.get("requestId").getAsString());	
					transactionDTO.setPayeeNickName(transactionRecord.get("beneficiaryName").getAsString());	
					transactionDTO.setTransactionsNotes(transactionRecord.get("notes").getAsString());	
					transactionDTO.setRecurrenceDesc(transactionRecord.get("status").getAsString());	
					transactionDTO.setTransactionDate(transactionRecord.get("scheduledDate").getAsString());
					transactionDTO.setFromNickName(transactionDTO.getFromAccountNumber());
					transactionDTO.setToNickName(transactionDTO.getToAccountNumber());
					transactionDTO.setTransactionAmount(transactionRecord.get("transactionAmount")!=null?transactionRecord.get("transactionAmount").getAsString():"");
					transactionDTO.setToAccountName(transactionRecord.get("beneficiaryName")!=null?transactionRecord.get("beneficiaryName").getAsString():"");
					transactionDTO.setToAccountType(transactionRecord.get("bankName")!=null?transactionRecord.get("bankName").getAsString():"");
					//transactionDTO.setAmount(transactionRecord.get("transactionAmount").toString());
					
				//updateFromAccountDetails(transactionDTO);
				//updateToRecipientDetails(transactionDTO);
				updateTransactionDetails(transactionDTO);
			}
		}
		return transactionDTO;
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
		}
	public String getFormattedTransactionDate(String transactionDate) {
		try {
			return HelperMethods.convertDateFormat(transactionDate, "dd/MM/YYYY");
		} catch (ParseException e) {
			alert.prepareError("Error while converting transaction date format",e).log();
			return transactionDate;
		}
	}

	}

package com.bct.eSewa;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.businessdeligate.api.BillPaymentHistoryBusinessDelegate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.javaservices.BillpayHistoryManageService;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public class EsewaDownloadManageService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(BillpayHistoryManageService.class);
	BillPaymentHistoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(BillPaymentHistoryBusinessDelegate.class);
	final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
	// public static final String TRANSACTION_ID = "transactionId";
	public static final String FILE_ID = "fileId";
	public static final String TRANSACTION_DATASET_NAME = "esewaTransactionLog";// "Transactions";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		LOG.debug("HBL:EsewaDownloadManageService::");
		try {
			result = geteSewaPaymentHistory(methodId, inputArray, request, response);
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			LOG.error("Exception occured while fetching the eSewa history  :" + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the eSewa history  :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
		}
		return result;
	}

	public Result geteSewaPaymentHistory(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		String transactionId = inputParams.get("transactionId") != null ? inputParams.get("transactionId").toString()
				: "";
		if (methodID.equalsIgnoreCase("geteSewaTransactionById")) {
			result = geteSewaTransactionById(inputParams, dcRequest);
		}
		return result;
	}

	public Result geteSewaTransactionById(Map<String, Object> inputParams, DataControllerRequest request) {
		if (inputParams.get("transactionId") == null) {
			return null;
		}
		Result result = new Result();
		LOG.debug("geteSewaTransactionById: inputParams" + inputParams);
		try {
			TransactionDTO transaction = eSewaTransactionById(inputParams, request);

			String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
			JsonObject transactionJson = DTOUtils.getJsonObjectFromObject(transaction);
			MemoryManager.saveIntoCache(fileId, transactionJson.toString());
			LOG.debug("getBillTransactionById: transactionJson:" + transactionJson);
			result.addParam(FILE_ID, fileId, DBPUtilitiesConstants.STRING_TYPE);
		} catch (Exception e) {
			LOG.error("Error while generating transaction report" + e.getMessage().toString());
			ErrorCodeEnum.ERR_13525.setErrorCode(result);
		}
		return result;
	}

	public TransactionDTO eSewaTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)
			throws ApplicationException {
		try {
			JsonObject transaction = geteSewaTransById(inputParams, dcRequest);
			if (!HelperMethods.hasErrorOpstatus(transaction)) {
				return geteSewaTransactionDTO(transaction);
			}
		} catch (Exception e) {
			LOG.debug("Error while getBillTransactionById", e);
		}
		return null;
	}

	public JsonObject geteSewaTransById(Map<String, Object> inputParams, DataControllerRequest dcRequest)
			throws ApplicationException {
		JsonObject responseJSON = null;
		String filter = "";
		String transactionId = inputParams.get("transactionId") != null ? inputParams.get("transactionId").toString()
				: "";
		if (StringUtils.isNotBlank(transactionId)) {
			filter = "OriginatingUniqueId  eq '" + transactionId + "'";
		}
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put(HBLURLConstants.FILTER, filter);
		LOG.debug("BCT::geteSewaTransById:getBillTransactionById: inputmap:" + inputmap);
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder().withOperationId("dbxdb_esewaTransactionLog_get")
					.withRequestParameters(inputmap).withServiceId("HBLMerchantCRUDService")
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			LOG.debug("BCT::geteSewaTransById:  response:" + dbresponse);
			responseJSON = new JsonParser().parse(dbresponse).getAsJsonObject();
		} catch (Exception e) {
			LOG.error("Exception caught while fetching merchant categories:" + e.toString());

		}
		return responseJSON;
	}

	public TransactionDTO geteSewaTransactionDTO(JsonObject transaction) throws IOException {
		if (JSONUtil.hasKey(transaction, getTransactionDatasetName())) {
			JsonArray transJsonArray = transaction.getAsJsonArray(getTransactionDatasetName());
			if (transJsonArray.size() > 0) {
				JsonObject transactionRecord = transJsonArray.get(0).getAsJsonObject();
				TransactionDTO transactionDTO = JSONUtils.parse(transactionRecord.toString(), TransactionDTO.class);
				transactionDTO.setFrequencyType("eSewa");
				transactionDTO.setFromAccountNumber(HBLCommonUtility.maskAccountNumber(transactionRecord.get("SourceAccountNo").getAsString(), 0, transactionRecord.get("SourceAccountNo").getAsString().length() - 4, "X"));
				transactionDTO.setFromNickName(transactionRecord.get("SenderName").getAsString());
				transactionDTO.setServiceCharge(transactionRecord.get("EsewaId").getAsString());
				transactionDTO.setAmount("NPR "+ transactionRecord.get("Amount").getAsString());
				transactionDTO.setRecurrenceDesc(transactionRecord.get("SwiftCode").getAsString());
				transactionDTO.setTransactionDate(transactionRecord.get("TransactionDate").getAsString());
				transactionDTO.setCashlessPersonName(transactionRecord.get("channelName").getAsString());
				transactionDTO.setTransactionId(transactionRecord.get("OriginatingUniqueId").getAsString());
				transactionDTO.setPayeeName(transactionRecord.get("Status").getAsString());
				transactionDTO.setDescription(transactionRecord.get("TransactionPurpose").getAsString());
				transactionDTO.setTransactionCurrency(
						transactionRecord.get("TransactionDetailOriginatingUniqueId").getAsString());

				/*
				 * SourceAccountNo, fromAccountNumber SenderName, fromNickName EsewaId,
				 * serviceCharge Amount, amount SwiftCode, recurrenceDesc TransactionDate,
				 * transactionDate channelName, cashlessPersonName OriginatingUniqueId,
				 * transactionId Status, payeeName TransactionPurpose, description
				 * TransactionDetailOriginatingUniqueId, transactionCurrency
				 */

				updateTransactionDetails(transactionDTO);
				return transactionDTO;
			}
		}
		return null;
	}
	
	public void updateTransactionDetails(TransactionDTO transactionDTO) {
		if(StringUtils.isNotBlank(transactionDTO.getTransactionDate())) {
			transactionDTO.setTransactionDate(
					getFormattedTransactionDate(transactionDTO.getTransactionDate()));
		}
	}
	
	public String getFormattedTransactionDate(String transactionDate) {
		try {
			return HelperMethods.convertDateFormat(transactionDate, "dd/MM/YYYY");
		} catch (ParseException e) {
			LOG.debug("Error while converting transaction date format",e);
			return transactionDate;
		}
	}

	public String getTransactionDatasetName() {
		return TRANSACTION_DATASET_NAME;
	}
}

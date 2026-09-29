package com.bct.custom.resource.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.CIPSPaymentHistoryBusinessDelegate;
import com.bct.custom.resource.api.CIPSTransactionHistoryResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public class CIPSTransactionHistoryResourceImpl implements CIPSTransactionHistoryResource{
	private static final Logger LOG = LogManager.getLogger(CIPSTransactionHistoryResourceImpl.class);
	CIPSPaymentHistoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(CIPSPaymentHistoryBusinessDelegate.class);
	 final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
	// public static final String TRANSACTION_ID = "transactionId";
	 public static final String FILE_ID = "fileId";
	@Override
	public Result getCIPSPaymentHistoryById(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		 Result result = new Result();
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		if(inputParams.get("transactionId")==null) {
	                CommonUtils.setErrMsg(result, "No input parameters provided");
	                CommonUtils.setOpStatusError(result);
	                return result;
		}
		LOG.debug("getCIPSTransactionById: inputParams" + inputParams);
		try {
			 TransactionDTO transaction = businessDelegate.getCIPSTransactionById(inputParams, dcRequest);
			 	if(transaction!=null) {
			 		JSONObject response = new JSONObject();
			 		JSONArray array= new JSONArray();
			 		String transactionString = new Gson().toJson(transaction, TransactionDTO.class);
			 		JSONObject transactionRecord = new JSONObject(transactionString);
			 		transactionRecord.put("paymentCurrencyId", transactionRecord.optString("transactionCurrency"));
			 		Double amount=Double.valueOf(transactionRecord.optString("amount"));
					BigDecimal amountDecimal= new BigDecimal(amount).setScale(2);
			 		transactionRecord.put("totalDebitAmount", String.valueOf(amountDecimal));
			 		transactionRecord.put("status",transactionRecord.optString("recurrenceDesc").equalsIgnoreCase("Executed")?"Success":transactionRecord.optString("recurrenceDesc"));
			 		transactionRecord.remove("frequencyEndDate");
			 		transactionRecord.remove("amount");
			 		array.put(transactionRecord);
			 		response.put("Transactions", array);
			 		result.appendJson(response.toString());
	           /* String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
	            JsonObject transactionJson = DTOUtils.getJsonObjectFromObject(transaction);
	            MemoryManager.saveIntoCache(fileId, transactionJson.toString());
	            LOG.debug("getCIPSTransactionById: transactionJson:" + transactionJson);
	            result.addParam(FILE_ID, fileId, DBPUtilitiesConstants.STRING_TYPE);
	            */
			 	}else {
			 		result.addParam(new Param("dbpErrCode", "30001"));
					result.addParam(new Param("dbpErrMsg", "There is no transactions"));
			 	}
	        } catch (Exception e) {
	        	LOG.error("Error while generating transaction report" + e.getMessage().toString());
	            ErrorCodeEnum.ERR_13525.setErrorCode(result);
	        }
	        return result;
	}
	@Override
	public Result getCIPSPaymentHistory(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}

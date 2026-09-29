package com.bct.custom.resource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.backenddeligate.impl.PaymentAggregatorBackendDeligateImpl;
import com.bct.custom.businessdeligate.api.BillPaymentHistoryBusinessDelegate;
import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.businessdeligate.impl.BillPaymentHistoryBusinessDeligateImpl;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.bct.custom.resource.api.BillPaymentHistoryResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public class BillPaymentHistoryResourceImpl implements BillPaymentHistoryResource{
	private static final Logger LOG = LogManager.getLogger(BillPaymentHistoryResourceImpl.class);
	private static JSONArray merchants=new JSONArray();
	BillPaymentHistoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(BillPaymentHistoryBusinessDelegate.class);
	 final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
	// public static final String TRANSACTION_ID = "transactionId";
	 public static final String FILE_ID = "fileId";
	@Override
	public Result getBillPaymentHistory(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		String billerId = inputParams.get("billerId")!=null?inputParams.get("billerId").toString():"";
		if(methodID.equalsIgnoreCase("getBillTransactionById")) {
			result=getBillTransactionById(inputParams, dcRequest);
		}
		else{
			try {
			LOG.debug("BCT::GetBillPaymentHistoryResourceImpl::inputParams: " + inputParams.toString());
			JSONArray response = businessDelegate.getBillPaymentHistory(billerId, inputParams, dcRequest, dcResponse);
			LOG.debug("BCT::GetBillPaymentHistoryResourceImpl::response: " + response.toString());
			Dataset ds = new Dataset();
			getAllMerchants(dcRequest);
			ds = constructDatasetFromJSONArray(response);
			ds.setId("paymentHistory");
			result.addDataset(ds);
			LOG.debug("Result paymentHistory:" + ds);
			result.addParam(new Param("opstatus", "0"));
			result.addParam(new Param("httpStatusCode", "200"));
			result.addParam(new Param("success", "true"));
		} catch (Exception e) {
			LOG.error("BCT::GetBillPaymentHistoryResourceImpl::exception: " + e.toString());
			result.addParam(new Param("dbpErrCode","1001"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		}	
		return result;
	}
	
	public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			String billerId=record.getParamValueByName("billerId");
			if(StringUtils.isBlank(billerId)) {
				record.addParam("billerId", "");
			}
			if(StringUtils.isBlank(record.getParamValueByName("profileId"))) {
				record.addParam("profileId", "External");
			}
			if(StringUtils.isBlank(record.getParamValueByName("paidBy"))) {
				record.addParam("paidBy", getPaymentAggregatorName(billerId));
			}
			dataset.addRecord(record);
		}
		return dataset;
	}

	
	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}

		return response;
	}
	public static String getPaymentAggregatorName(String billerId){
		String aggregatorName="";
		LOG.error("BCT::GetBillPaymentHistoryResourceImpl::getPaymentAggregatorName: aggregatorId:" + billerId);
		JSONArray response = merchants;
		if(StringUtils.isNotBlank(billerId) && response.length()>0) {
		for (int count = 0; count < response.length(); count++) {
			JSONObject jsonObject = response.getJSONObject(count);
			if(jsonObject.has("code") && jsonObject.has("paymentaggregator") && jsonObject.get("code").toString().equals(billerId)) {
				aggregatorName=jsonObject.get("paymentaggregator").toString();
			}
		  }
		}
		return aggregatorName;
	}
	public void getAllMerchants(DataControllerRequest dcRequest) {
		MerchantDetailsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
		.getBackendDelegate(MerchantDetailsBackendDeligate.class);
		try {
			 merchants = backendDelegate.getAllMerchants(dcRequest.getHeaderMap());
		} catch (com.temenos.infinity.api.commons.exception.ApplicationException e) {
			LOG.error("Exception caught while fetching getAllMerchants:" + e.getMessage().toString());
		}
		//return allPaymentAggregators;
	}
	public Result getBillTransactionById(Map<String, Object> inputParams, DataControllerRequest request) {
		if(inputParams.get("transactionId")==null) {
			return null;
		}
		 Result result = new Result();
		LOG.debug("getBillTransactionById: inputParams" + inputParams);
		try {
			 TransactionDTO transaction = businessDelegate.getBillTransactionById(inputParams, request);

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

}

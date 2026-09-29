package com.bct.custom.backenddeligate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.error.DBPError;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class BillPaymentHistoryBackendDelegateImpl implements BillPaymentHistoryBackendDeligate{
	private static final Logger logger = LogManager.getLogger(GetAllMerchantOperationsBackendDeligateImpl.class);
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String DBPERRCODE = "dbpErrCode";


	//@Override
	public JSONArray getBillPaymentHistory_old(String billerId, Map<String, Object> inputArray, DataControllerRequest dcRequest) throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter ="";
		if(StringUtils.isNotBlank(billerId)) {
		 filter = "billerId  eq '" + billerId + "'";
		}
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl:getBillPaymentHistory: inputmap:"+
				inputmap.toString());
		JSONArray response = new JSONArray();
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.BILL_PAY_HISTORY_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl: getBillPaymentHistory response:"+dbresponse);
			JSONObject responseJSON = new JSONObject(dbresponse);
			response = responseJSON.getJSONArray("billpaytransfers");
			
			
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant categories:" +e.toString());
			
		}
		return response;
	}
	@Override
	public JSONArray getBillPaymentHistory(String billerId, Map<String, Object> inputArray, DataControllerRequest dcRequest) throws ApplicationException {
	JSONArray response = new JSONArray();
	 Map inputParams = new HashMap();
	 StringBuffer filter = new StringBuffer();
	 String legalEntityId=CommonUtils.getUserAttributeFromIdentity(dcRequest, Constants.COMPANY_ID);
	 String customerId = CommonUtils.getUserAttributeFromIdentity(dcRequest, Constants.PARAM_USER_ID);
	 if(StringUtils.isNotBlank(customerId)) {
	 filter.append("SELECT * FROM billpaytransfers as t1 where t1.legalEntityId = '"+legalEntityId+"' and t1.fromAccountNumber in (select Account_id from customeraccounts where Customer_id = '"+customerId+"')");
	 inputParams.put("transactions_query", filter.toString());
	 logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl:getBillPaymentHistory: inputParams:"+
			 inputParams.toString());
	 try {
         Result result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),URLConstants.ACCOUNT_TRANSACTION_PROC);
         logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl: getBillPaymentHistory response:"+ResultToJSON.convert(result));
         if (HelperMethods.hasRecords(result)) {
            Dataset dataSet = result.getDatasetById("records");
            response=convertDatasetToJSONArray(dataSet);
         }
		}
	 catch (Exception e) {
		logger.error("Exception caught while fetching transaction history:" +e.getMessage());
		throw new ApplicationException(null);
	 	}
	 }

     return response;
     
	}
	
	public static JSONArray convertDatasetToJSONArray(Dataset dataset) {
		JSONArray array = new JSONArray();
		List<Record> records = new ArrayList<>();

		if (dataset != null && dataset.getAllRecords().size() != 0) {
			records = dataset.getAllRecords();
		}

		for (int i = 0; i < records.size(); i++) {
			array.put(convertRecordToJSONObject(records.get(i)));
		}
		return array;
	}

	public static JSONObject convertRecordToJSONObject(Record record) {

		JSONObject jsonObj = new JSONObject();

		List<Param> arList = record.getAllParams();

		Iterator<Param> it = arList.iterator();
		while (it.hasNext()) {
			Param p = it.next();
			String key = p.getName();
			jsonObj.put(key, p.getValue());
		}
		return jsonObj;
	}
	@Override
	public JsonObject getBillTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)
			throws ApplicationException {
		JsonObject responseJSON = null;
		String filter ="";
		String transactionId=inputParams.get("transactionId")!=null?inputParams.get("transactionId").toString():"";
		if(StringUtils.isNotBlank(transactionId)) {
		 filter = "transactionId  eq '" + transactionId + "'";
		}
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl:getBillTransactionById: inputmap:"+
				inputmap);
		JSONArray response = new JSONArray();
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.BILL_PAY_HISTORY_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::GetBillPaymentHistoryBackendDeligateImpl: getBillTransactionById response:"+dbresponse);
			 responseJSON =  new JsonParser().parse(dbresponse).getAsJsonObject();
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant categories:" +e.toString());
			
		}
		return responseJSON;
	}
     

}

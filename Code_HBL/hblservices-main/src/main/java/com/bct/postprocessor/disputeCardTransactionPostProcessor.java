package com.bct.postprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class disputeCardTransactionPostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(disputeCardTransactionPostProcessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("Result:###" + ResultToJSON.convert(result));
			Dataset Transactions = result.getDatasetById("pendingAuthInfo_out");
			logger.debug("Card Transactions DS Result:###" + Transactions.toString());
			List<Record> transactionRecords = Transactions != null ? Transactions.getAllRecords() : null;
			logger.debug("transactionRecords size:" + transactionRecords.size() + "");
			if (transactionRecords.size() != 0) {
				for (Record record : transactionRecords) {
					String transactionId = record.getParamValueByName("referenceNumber");
					logger.debug("referenceNumber:" + transactionId);
					if(verifyDisputeFromDB(request, transactionId)) {
						//isDisputed true
						record.addParam("isDisputed", "true");
					}else {
						//isDisputed false
						record.addParam("isDisputed", "false");
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

	public boolean verifyDisputeFromDB(DataControllerRequest request, String TransactionId) {
		try {
			Result dispute = new Result();
			Dataset disputeDataset = new Dataset();
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			dispute = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "dbpRbLocalServicesdb",
					"dbxdb_disputeTransactions_get", false);
			logger.debug("DisputeTransactions result##" + ResultToJSON.convert(dispute));
			disputeDataset = dispute.getDatasetById("disputeTransactions");
			logger.debug("getDisputeTrans :" + disputeDataset.toString());
			logger.debug("TransactionId to check :" + TransactionId);
			for (int i = 0; i < disputeDataset.getAllRecords().size(); i++) {
				logger.debug("transactionId##" + disputeDataset.getRecord(i).getParamValueByName("transactionId"));
				if (TransactionId.equalsIgnoreCase(disputeDataset.getRecord(i).getParamValueByName("transactionId")))
					return true;
			}
		} catch (Exception e) {
			logger.debug("Error while retrieving CustomerType_id for Customer" + e.toString());
		}
		return false;
	}

	public Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}

	public Record constructRecordFromJSONObject(JSONObject JSONObject) {
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
}

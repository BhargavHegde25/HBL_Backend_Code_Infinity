package com.kony.achservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateACHTransactionStatus implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        HashMap<String, String> searchParams = new HashMap<>();
        searchParams.put(DBPUtilitiesConstants.FILTER, "Status" + DBPUtilitiesConstants.EQUAL + "2");

        Result transactions = HelperMethods.callApi(dcRequest, searchParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.BB_TRANSACTION_GET);

        if (HelperMethods.hasRecords(transactions)) {
            List<Record> records = transactions.getDatasetById("bbtransaction").getAllRecords();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                String transactionId = record.getParamByName("Transaction_id").getValue();
                JSONObject recordObj = CommonUtils.convertRecordToJSONObject(record);
                obj.put(transactionId, recordObj);
            }

            searchParams = new HashMap<>();
            searchParams.put("Requests", obj.toString());

            Result statusResponse = HelperMethods.callApi(dcRequest, searchParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.FETCH_TRANSACTION_STATUS);

            String response = statusResponse.getParamByName("Requests").getValue();

            if (response != null) {
                JSONObject resobj = new JSONObject(response);

                Iterator<String> keysIt = resobj.keys();

                while (keysIt.hasNext()) {
                    String key = keysIt.next();
                    JSONObject fileData = resobj.getJSONObject(key);
                    HashMap<String, String> inputParams = new HashMap<>();
                    inputParams.put("Transaction_id", key);
                    inputParams.put("Status", CommonUtils.getStatusid(dcRequest, fileData.getString("Status")));
                    updateACHTransactionStatus(dcRequest, inputParams);
                }
            }

        }

        return transactions;
    }

    private Result updateACHTransactionStatus(DataControllerRequest dcRequest, HashMap<String, String> inputParams)
            throws HttpCallException {

        String transactionId = inputParams.get("Transaction_id");

        if (transactionId == null) {
            Result result = new Result();
            result.addParam(new Param("Status", "Transaction_id is missing in the input"));
            return result;
        }

        if (inputParams.get("Status") == null) {
            Result result = new Result();
            result.addParam(new Param("Status", "Status id is missing in the input"));
            return result;
        }

        Result output = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.BB_TRANSACTION_UPDATE);

        if (!HelperMethods.hasRecords(output)) {
            Result result = new Result();
            result.addParam(new Param("Status", "Updating status for given Transaction_id failed"));
            return result;
        }

        return output;
    }

}

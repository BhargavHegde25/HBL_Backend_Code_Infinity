package com.kony.achservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class SaveTransactionAsTemplate implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

            HashMap<String, String> fetchTransactionMap = new HashMap<>();
            String transaction_id = inputParams.get("Transaction_id");
            String templateName = inputParams.get("TemplateName");

            if (templateName == null) {
                return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
            }
            if (transaction_id == null) {
                return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
            }

            fetchTransactionMap.put(DBPUtilitiesConstants.FILTER,
                    "Transaction_id" + DBPUtilitiesConstants.EQUAL + transaction_id);

            Result transactionDetails = HelperMethods.callApi(dcRequest, fetchTransactionMap,
                    HelperMethods.getHeaders(dcRequest), URLConstants.BB_TRANSACTION_GET);

            Map<String, String> transactionParams = new HashMap<>();
            String companyId = CommonUtils.getUserCompanyId(dcRequest);

            // If it is save as Template request, Transaction_id exists, so check for
            // company_id and whether it belongs to logged in user or not?
            if (!companyId.equals(HelperMethods.getFieldValue(transactionDetails, "Company_id"))) {
                return ErrorCodeEnum.ERR_12004.setErrorCode(new Result());
            }

            if (HelperMethods.hasRecords(transactionDetails)) {

                transactionParams = CommonUtils
                        .convertRecordToMap(transactionDetails.getAllDatasets().get(0).getRecord(0));
                transactionParams.put("TemplateName", templateName);
                transactionParams.put("TemplateDescription", inputParams.get("TemplateDescription"));
                Result transactionRecords = HelperMethods.callApi(dcRequest, fetchTransactionMap,
                        HelperMethods.getHeaders(dcRequest), URLConstants.BB_TRANSACTION_RECORD_GET);

                if (HelperMethods.hasRecords(transactionRecords)) {

                    List<Record> records = transactionRecords.getAllDatasets().get(0).getAllRecords();
                    JSONArray recordsJsonArray = new JSONArray();

                    for (int i = 0; i < records.size(); i++) {
                        Record record = records.get(i);
                        JSONObject recordObject = CommonUtils.convertRecordToJSONObject(record);

                        HashMap<String, String> paramsForFetchSubRecords = new HashMap<>();
                        paramsForFetchSubRecords.put(DBPUtilitiesConstants.FILTER,
                                "TransactionRecord_id" + DBPUtilitiesConstants.EQUAL
                                        + HelperMethods.getFieldValue(record, "TransactionRecord_id"));
                        Result transactionSubRecords = HelperMethods.callApi(dcRequest, paramsForFetchSubRecords,
                                HelperMethods.getHeaders(dcRequest), URLConstants.BB_TRANSACTION_SUB_RECORD_GET);

                        recordObject.put("SubRecords", CommonUtils.extractRecordsFromResult(transactionSubRecords));

                        recordsJsonArray.put(recordObject);
                    }

                    transactionParams.put("Records", recordsJsonArray.toString());
                } else {
                    return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
                }
            } else {
                return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
            }

            inputParams = CommonUtils.addBasicParamsForCreateService(dcRequest, inputParams);
            inputParams.putAll(transactionParams);

            Result template = CommonUtils.invokeHTTPCall(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TEMPLATE_DATA_ADD);

            return template;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}

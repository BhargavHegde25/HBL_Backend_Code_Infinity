package com.kony.achservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ExecuteTemplate implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String template_id = inputParams.get("Template_id");
            String effectiveDate = inputParams.get("EffectiveDate");
            String recordsIP = inputParams.get("Records");

            if (template_id == null) {
                return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
            }
            if (effectiveDate == null) {
                return ErrorCodeEnum.ERR_12021.setErrorCode(new Result());
            }
            if (recordsIP == null) {
                return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
            }

            JSONArray executeRecords = new JSONArray(recordsIP.toString());

            String companyId = CommonUtils.getUserCompanyId(dcRequest);

            HashMap<String, String> fetchTemplateMap = new HashMap<>();
            Map<String, String> templateParams = new HashMap<>();

            fetchTemplateMap.put(DBPUtilitiesConstants.FILTER,
                    "Template_id" + DBPUtilitiesConstants.EQUAL + template_id);
            Result templateDetails = HelperMethods.callApi(dcRequest, fetchTemplateMap,
                    HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_GET);

            if (HelperMethods.hasRecords(templateDetails)) {

                // If it is save as Template request, Transaction_id exists, so check for
                // company_id and whether it belongs to logged in user or not?
                /**
                 * Moved Company validation to Object service Pre-Processor.
                 * 
                 * if (!companyId.equals(HelperMethods.getFieldValue(templateDetails, "Company_id"))) { return
                 * ErrorCodeEnum.ERR_12004.setErrorCode(new Result()); }
                 */

                templateParams = CommonUtils
                        .convertRecordToMap(templateDetails.getDatasetById("bbtemplate").getRecord(0));
                templateParams.put("EffectiveDate", effectiveDate);

                boolean doesSubRecordExists = "8".equals(templateParams.get("TemplateRequestType_id"));

                Result templateRecordsResult = HelperMethods.callApi(dcRequest, fetchTemplateMap,
                        HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_RECORD_GET);

                if (HelperMethods.hasRecords(templateRecordsResult)) {

                    List<Record> templateRecords = templateRecordsResult.getDatasetById("bbtemplaterecord")
                            .getAllRecords();
                    HashMap<String, Record> recordMap = new HashMap<>();

                    Iterator<Record> it = templateRecords.iterator();

                    while (it.hasNext()) {
                        Record p = it.next();
                        recordMap.put(p.getParam("TemplateRecord_id").getValue(), p);
                    }

                    JSONArray recordsJsonArray = new JSONArray();

                    for (int i = 0; i < executeRecords.length(); i++) {

                        JSONObject executeRecord = executeRecords.getJSONObject(i);
                        Record record = recordMap.get(executeRecord.get("TemplateRecord_id"));

                        if (!doesSubRecordExists) {
                            executeRecord = updateRecordAndReturnJSONObject(record, executeRecord, "Amount");
                        } else {

                            JSONArray executeSubRecords = new JSONArray();
                            try {
                                executeSubRecords = executeRecord.getJSONArray("SubRecords");
                            } catch (NullPointerException exp) {
                                // No subRecords found;
                            } catch (JSONException exp1) {
                                // No subRecords found;
                            }

                            executeRecord = CommonUtils.convertRecordToJSONObject(record);
                            HashMap<String, String> paramsForFetchSubRecords = new HashMap<>();
                            paramsForFetchSubRecords.put(DBPUtilitiesConstants.FILTER, "TemplateRecord_id"
                                    + DBPUtilitiesConstants.EQUAL + executeRecord.get("TemplateRecord_id"));
                            Result templateSubRecords = HelperMethods.callApi(dcRequest, paramsForFetchSubRecords,
                                    HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_SUB_RECORD_GET);

                            if (HelperMethods.hasRecords(templateSubRecords)) {
                                List<Record> subRecordsList = templateSubRecords.getDatasetById("bbtemplatesubrecord")
                                        .getAllRecords();
                                JSONArray subRecords = updateRecordListAndReturnJSONArray(subRecordsList,
                                        executeSubRecords, "TemplateSubRecord_id", "Amount");
                                executeRecord.put("SubRecords", subRecords);
                            } else {
                                return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
                            }

                        }
                        recordsJsonArray.put(executeRecord);
                    }
                    templateParams.put("Records", recordsJsonArray.toString());
                } else {
                    return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
                }
            } else {
                return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
            }

            inputParams = CommonUtils.addBasicParamsForCreateService(dcRequest, inputParams);
            inputParams.putAll(templateParams);

            Result transaction = CommonUtils.invokeHTTPCall(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TRANSACTION_DATA_ADD);

            // Update Effective Date to last used date
            HashMap<String, String> updateTemplateParams = new HashMap<>();
            updateTemplateParams.put("Template_id", template_id);
            updateTemplateParams.put("EffectiveDate", HelperMethods.getCurrentTimeStamp());

            HelperMethods.callApi(dcRequest, updateTemplateParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TEMPLATE_UPDATE);

            return transaction;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }

    private JSONArray updateRecordListAndReturnJSONArray(List<Record> recList, JSONArray updateArr,
            String searchRecordKey, String updateParamKey) {

        JSONArray jsonArray = new JSONArray();

        HashMap<String, Record> recordMap = new HashMap<>();

        Iterator<Record> it = recList.iterator();

        while (it.hasNext()) {
            Record p = it.next();
            recordMap.put(p.getParam(searchRecordKey).getValue(), p);
        }

        for (int i = 0; i < updateArr.length(); i++) {
            JSONObject obj = updateArr.getJSONObject(i);
            obj = updateRecordAndReturnJSONObject(recordMap.get(obj.get(searchRecordKey)), obj, updateParamKey);
            jsonArray.put(obj);
        }

        return jsonArray;
    }

    private JSONObject updateRecordAndReturnJSONObject(Record record, JSONObject updateObj, String updateKey) {

        JSONObject jsonObj = new JSONObject();

        List<Param> arList = record.getAllParams();

        Iterator<Param> it = arList.iterator();

        while (it.hasNext()) {
            Param p = it.next();
            String key = p.getName();
            jsonObj.put(key, p.getValue());
        }

        Object updateValue = updateObj.get(updateKey);
        jsonObj.put(updateKey, updateValue.toString());

        return jsonObj;
    }
}

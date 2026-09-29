package com.kony.achservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateTemplate implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            HashSet<String> accounts = CommonUtils.getAllAccounts(dcRequest);
            Map<String, String> inputs = HelperMethods.getInputParamMap(inputArray);

            String templateType_id = inputs.get("TemplateType_id");
            String templateRequestTypeId = inputs.get("TemplateRequestType_id");
            String maxAmount = inputs.get("MaxAmount");
            String transactionType_id = inputs.get("TransactionType_id");
            String debitAccount = inputs.get("DebitAccount");
            String templateName = inputs.get("TemplateName");
            String recordsIP = inputs.get("Records");

            // Input validations

            if (templateType_id == null) {
                inputs.put("TemplateType_id", "1");
            }
            if (templateName == null) {
                return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
            }
            if (templateRequestTypeId == null) {
                return ErrorCodeEnum.ERR_12027.setErrorCode(new Result());
            }
            if (transactionType_id == null) {
                return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
            }
            if (debitAccount == null) {
                return ErrorCodeEnum.ERR_12022.setErrorCode(new Result());
            }
            if (maxAmount == null) {
                return ErrorCodeEnum.ERR_12029.setErrorCode(new Result());
            }
            if (recordsIP == null) {
                return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
            }

            boolean doesSubRecordExists = "8".equals(templateRequestTypeId);

            // Authorization checks
            if (!accounts.contains(debitAccount)) {
                return ErrorCodeEnum.ERR_12002.setErrorCode(new Result());
            }

            String transactionType = CommonUtils.getTransactionType(dcRequest, transactionType_id);

            HashMap<String, JSONObject> list = SessionScope.getUserCreateServices(dcRequest);

            if (transactionType.equals("Payment") && !list.containsKey("6")) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            } else if (transactionType.equals("Collection") && !(list.containsKey("7"))) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            JSONArray records = new JSONArray(recordsIP.toString());
            String totalAmount = CommonUtils.getTotalAmount(records, doesSubRecordExists);
            inputs.put("TotalAmount", totalAmount);

            double total = Double.parseDouble(totalAmount);

            if (total <= 0) {
                return ErrorCodeEnum.ERR_12301.setErrorCode(new Result());
            }

            if (total > Double.parseDouble(maxAmount)) {
                return ErrorCodeEnum.ERR_12302.setErrorCode(new Result());
            }

            // Create Template
            inputs = CommonUtils.addBasicParamsForCreateService(dcRequest, inputs);
            Result template = HelperMethods.callApi(dcRequest, inputs, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TEMPLATE_CREATE);

            if (!HelperMethods.hasRecords(template) || !HelperMethods.hasField(template, "Template_id")) {
                return ErrorCodeEnum.ERR_12101.setErrorCode(template);
            }

            String template_id = HelperMethods.getFieldValue(template, "Template_id");
            for (int i = 0; i < records.length(); i++) {

                JSONObject recordInputs = (JSONObject) records.get(i);
                JSONArray subRecords = new JSONArray();
                try {
                    subRecords = new JSONArray(recordInputs.get("SubRecords").toString());
                } catch (JSONException exp) {
                    // No subRecords found;
                } catch (NullPointerException exp1) {
                    // No subRecords found;
                }

                // Create TemplateRecords

                HashMap<String, String> recordDataMap = CommonUtils.toMap(recordInputs);
                recordDataMap.put("Template_id", template_id);
                recordDataMap.put("TemplateRequestType_id", inputParams.get("TemplateRequestType_id").toString());

                Result templateRecord = HelperMethods.callApi(dcRequest, recordDataMap,
                        HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_RECORD_CREATE);

                if (!HelperMethods.hasRecords(templateRecord)
                        || !HelperMethods.hasField(templateRecord, "TemplateRecord_id")) {
                    return ErrorCodeEnum.ERR_12102.setErrorCode(templateRecord);
                }

                String templateRecord_id = HelperMethods.getFieldValue(templateRecord, "TemplateRecord_id");

                for (int j = 0; j < subRecords.length(); j++) {

                    JSONObject subRecordInputs = (JSONObject) subRecords.get(j);

                    // Create TemplateSubRecords
                    HashMap<String, String> subRecordDataMap = CommonUtils.toMap(subRecordInputs);
                    subRecordDataMap.put("TemplateRecord_id", templateRecord_id);

                    Result templateSubRecord = HelperMethods.callApi(dcRequest, subRecordDataMap,
                            HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_SUB_RECORD_CREATE);

                    if (!HelperMethods.hasRecords(templateSubRecord)) {
                        return ErrorCodeEnum.ERR_12103.setErrorCode(templateSubRecord);
                    }
                }
            }

            return template;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

    }
}

package com.kony.achservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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

public class CreateTransaction implements JavaService2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        try {
            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            Map<String, String> inputParam = HelperMethods.getInputParamMap(inputArray);
            HashSet<String> accounts = CommonUtils.getAllAccounts(dcRequest);

            diagnostic.prepareDebug("No Of accounts for in Create Transaction: " + accounts.size()).log();

            String templateRequestTypeId = inputParam.get("TemplateRequestType_id");
            String maxAmount = inputParam.get("MaxAmount");
            String transactionType_id = inputParam.get("TransactionType_id");
            String debitAccount = inputParam.get("DebitAccount");
            String effectiveDate = inputParam.get("EffectiveDate");
            String recordsIP = inputParam.get("Records");
            String vendorService = "";

            // Input validations

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
            if (effectiveDate == null) {
                return ErrorCodeEnum.ERR_12021.setErrorCode(new Result());
            }

            boolean doesSubRecordExists = "8".equals(templateRequestTypeId);

            /**
             * Account Validations are moved to object pre-processor.
             * 
             * if (!accounts.contains(debitAccount)) { return ErrorCodeEnum.ERR_12002.setErrorCode(new Result()); }
             */

            HashMap<String, JSONObject> list = SessionScope.getUserCreateServices(dcRequest);
            String transactionType = CommonUtils.getTransactionType(dcRequest, transactionType_id);

            if (transactionType.equals("Payment")) {
                vendorService = URLConstants.ACH_VENDOR_SERVICE_FOR_PAYMENTS;
                transactionType = "6";
            } else if (transactionType.equals("Collection")) {
                vendorService = URLConstants.ACH_VENDOR_SERVICE_FOR_COLLECTIONS;
                transactionType = "7";
            }

            JSONArray records = new JSONArray(recordsIP.toString());

            String totalAmount = CommonUtils.getTotalAmount(records, doesSubRecordExists);
            inputParam.put("TotalAmount", totalAmount);

            double total = Double.parseDouble(totalAmount);

            if (total <= 0) {
                return ErrorCodeEnum.ERR_12301.setErrorCode(new Result());
            }

            if (total > Double.parseDouble(maxAmount)) {
                return ErrorCodeEnum.ERR_12302.setErrorCode(new Result());
            }

            inputParam = CommonUtils.addBasicParamsForCreateService(dcRequest, inputParam);
            inputParam.put("BBGeneralTransactionType_id", transactionType);

            inputParam = CommonUtils.addEntryToRequestsIfLimitExceeds(dcRequest, totalAmount, inputParam);

            // Create Transaction
            Result transaction = HelperMethods.callApi(dcRequest, inputParam, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_TRANSACTION_CREATE);

            if (!HelperMethods.hasRecords(transaction) || !HelperMethods.hasField(transaction, "Transaction_id")) {
                return ErrorCodeEnum.ERR_12104.setErrorCode(transaction);
            }

            String transaction_id = HelperMethods.getFieldValue(transaction, "Transaction_id");

            for (int i = 0; i < records.length(); i++) {

                JSONObject recordInputs = (JSONObject) records.get(i);

                JSONArray subRecords = new JSONArray();
                try {
                    subRecords = new JSONArray(recordInputs.get("SubRecords").toString());
                } catch (NullPointerException exp) {
                    // No subRecords found;
                } catch (JSONException exp1) {
                    // No subRecords found;
                }

                HashMap<String, String> recordDataMap = CommonUtils.toMap(recordInputs);

                recordDataMap.put("Transaction_id", transaction_id);
                recordDataMap.put("TemplateRequestType_id", templateRequestTypeId.toString());

                Result transactionRecord = HelperMethods.callApi(dcRequest, recordDataMap,
                        HelperMethods.getHeaders(dcRequest), URLConstants.BB_TRANSACTION_RECORD_CREATE);

                if (!HelperMethods.hasRecords(transactionRecord)
                        || !HelperMethods.hasField(transactionRecord, "TransactionRecord_id")) {
                    return ErrorCodeEnum.ERR_12105.setErrorCode(transactionRecord);
                }

                String transactionRecord_id = HelperMethods.getFieldValue(transactionRecord, "TransactionRecord_id");

                for (int j = 0; j < subRecords.length(); j++) {
                    HashMap<String, String> subRecordDataMap = CommonUtils.toMap((JSONObject) subRecords.get(j));

                    subRecordDataMap.put("TransactionRecord_id", transactionRecord_id);

                    Result subrecord = HelperMethods.callApi(dcRequest, subRecordDataMap,
                            HelperMethods.getHeaders(dcRequest), URLConstants.BB_TRANSACTION_SUB_RECORD_CREATE);
                    if (!HelperMethods.hasRecords(subrecord)) {
                        return ErrorCodeEnum.ERR_12106.setErrorCode(subrecord);
                    }
                }
            }

            if (CommonUtils.getStatusid(dcRequest, "New").equals(inputParam.get("Status"))) {
                Result achVendorServiceResponse = HelperMethods.callApi(dcRequest, inputParams,
                        HelperMethods.getHeaders(dcRequest), vendorService);
                String statusFromService = achVendorServiceResponse.getParamByName("Status").getValue();
                inputParam.put("Transaction_id", transaction_id);
                inputParam.put("Status", CommonUtils.getStatusid(dcRequest, statusFromService));
                inputParam.put("ReferenceID", achVendorServiceResponse.getParamByName("ReferenceID").getValue());
                transaction = HelperMethods.callApi(dcRequest, inputParam, HelperMethods.getHeaders(dcRequest),
                        URLConstants.BB_TRANSACTION_UPDATE);
            }

            return transaction;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

    }
}

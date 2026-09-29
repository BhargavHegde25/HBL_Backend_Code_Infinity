package com.infinity.dbx.temenos.stoppayments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class GetStopPaymentsPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    String sortKey = null;

    @SuppressWarnings("rawtypes")
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
        Dataset stopPayments = result.getDatasetById(StopCheckPaymentUtils.PARAM_BODY);
        String ErrMsg = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) != "" ? result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) : "";
        List<Record> stopPaymentsRecords = stopPayments != null ? stopPayments.getAllRecords() : null;
        List<Record> stopPaymentsRecordsFinals = new ArrayList<Record>();
        if (stopPaymentsRecords.isEmpty() && StringUtils.isEmpty(ErrMsg)) {
            alert.prepareError("Stop Payments empty return result " + result.getAllParams()).log();
            return TemenosUtils.getEmptyResult(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT);
        } 
        if (StringUtils.isEmpty(ErrMsg)) {
            for (Record product : stopPaymentsRecords) {

                String transactionStopStatus = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_STOP_STATUS);
                String transactionDate = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_CREATE_DATE);
                String transactionId = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_STOP_INSRUCTION);
                String checkReason = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_STOP_REASON);
                String requestValidity = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_EXPIRY_DATE);
                String fromAccountNumber = CommonUtils.getParamValue(product, StopCheckPaymentUtils.PARAM_PAYMENT_STOP_ACCOUNT);
                if (accounts != null && StringUtils.isNotBlank(fromAccountNumber)) {
                    Account account = accounts.containsKey(fromAccountNumber) ? accounts.get(fromAccountNumber) : null;
                    if (account != null) {
                        product.addParam(new Param(StopCheckPaymentUtils.PARAM_NICK_NAME, account.getAccountName(),
                                Constants.PARAM_DATATYPE_STRING));
                    }
                }
                JSONArray multigroup = new JSONArray();
                JSONObject obj = ResultToJSON.convertRecord(product);
                String amount = null;
                String checkNumber1 = null;
                String checkNumber2 = null;
                List<String> checks = new ArrayList<String>();
                multigroup = obj.getJSONArray(StopCheckPaymentUtils.PARAM_MULTI_GROUP);
                for (int i = 0; i < multigroup.length(); i++) {
                    String name = "";
                    if ((String) ((JSONObject) multigroup.get(i)).get(StopPaymentConstants.PARAM_ATTRIBUTE_NAME) != null)
                        name = (String) ((JSONObject) multigroup.get(i)).get(StopPaymentConstants.PARAM_ATTRIBUTE_NAME);
                    if (((JSONObject) multigroup.get(i)).has(StopPaymentConstants.PARAM_ATTRIBUTE_VALUES)) {
                        JSONArray values = (JSONArray) ((JSONObject) multigroup.get(i)).get(StopPaymentConstants.PARAM_ATTRIBUTE_VALUES);
                        for (int j = 0; j < values.length(); j++) {
                            if (name.equals(StopPaymentConstants.PARAM_STOP_PAYMENT_AMOUNT)) {
                                amount = (String) ((JSONObject) values.get(j)).get(StopPaymentConstants.PARAM_ATTRIBUTE_VALUE);
                            } else if (name.equals(StopPaymentConstants.PARAM_PAYMENT_STOP_CHEQUE_NUMBER)) {
                                if (values.length() == 1) {
                                    for (int k = 0; k < values.length(); k++) {
                                        checks.add((String) ((JSONObject) values.get(k)).get(StopPaymentConstants.PARAM_ATTRIBUTE_VALUE));
                                    }
                                } else {
                                    checks.add((String) ((JSONObject) values.get(j)).get(StopPaymentConstants.PARAM_ATTRIBUTE_VALUE));
                                }
                            }
                        }
                    }
                }
                if (checks.size() > 1) {
                    checkNumber1 = checks.get(0);
                    checkNumber2 = checks.get(1);
                    product.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SERIES);
                } else if (checks.size() == 1) {
                    checkNumber1 = checks.get(0);
                    product.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SINGLE);
                }
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE, transactionDate); 
                product.addParam(StopPaymentConstants.PARAM_ID, transactionId);
                product.addParam(StopPaymentConstants.CHECK_REASON, StopCheckPaymentUtils.convertCheckReasonToDBX(checkReason));
                product.addParam(StopPaymentConstants.PARAM_REQUEST_VALIDITY, requestValidity); 
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER, checkNumber1);
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO, checkNumber2); 
                product.addParam(StopPaymentConstants.PARAM_AMOUNT, amount);
                product.addParam(StopPaymentConstants.PARAM_STATUS_DESCRIPTION, transactionStopStatus);
                product.addParam(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT, fromAccountNumber);
                stopPaymentsRecordsFinals.add(product);
            }
            
            List<JSONObject> jsonValues = new ArrayList();
            for (int i = 0; i < stopPaymentsRecordsFinals.size(); i++) {
                jsonValues.add(ResultToJSON.convertRecord(stopPaymentsRecordsFinals.get(i)));
            }

            sortKey = request.getParameter(StopPaymentConstants.PARAM_SORY_BY);
            JSONArray records = new JSONArray();
            List<JSONObject> amountRecords = new ArrayList(); 
            List<JSONObject> nonAmountRecords = new ArrayList();
            if (sortKey.equals(StopPaymentConstants.PARAM_AMOUNT)) {
                for (JSONObject obj : jsonValues) {
                    if (obj.has(StopPaymentConstants.PARAM_AMOUNT)) {
                        amountRecords.add(obj);
                    } else {
                        nonAmountRecords.add(obj);
                    }
                }
                records = DoubleSort(amountRecords, request.getParameter(StopPaymentConstants.PARAM_ORDER));
                for (int i = 0; i < nonAmountRecords.size(); i++) {
                    records.put(nonAmountRecords.get(i));
                }
            } else {
                records = sort(jsonValues, request.getParameter(StopPaymentConstants.PARAM_ORDER));
            }
            /*
             * if (request.getParameter("offset") != null &&
             * request.getParameter("limit") != null) { records =
             * filterRecords(records, request.getParameter("offset"),
             * request.getParameter("limit")); }
             */
 
            JSONObject responseObj = new JSONObject();
            Result finalResult = new Result();
            responseObj.put(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT, records);
            finalResult = JSONToResult.convert(responseObj.toString());
            finalResult.addOpstatusParam(0);
            finalResult.addHttpStatusCodeParam(200);
            finalResult.addStringParam("success", "Records found");
            result = finalResult;
        }
        return result;
    }

    public JSONArray sort(List<JSONObject> jsonValues, String order) {

        JSONArray sortedJsonArray = new JSONArray();

        Collections.sort(jsonValues, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(sortKey);
                    valB = (String) b.get(sortKey);
                } catch (JSONException e) {
                    alert.prepareError("Caught exception at invoke of sorting: " + e).log();
                }

                if (order.equals("asc")) {
                    return valA.compareTo(valB);
                } else {
                    return valB.compareTo(valA);
                }
            }
        });

        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    public JSONArray DoubleSort(List<JSONObject> jsonValues, String order) {

        JSONArray sortedJsonArray = new JSONArray();

        if (order.equals("desc")) {
            jsonValues.sort((o1, o2) -> Double.compare(Double.parseDouble((String) o2.get(sortKey)),
                    Double.parseDouble((String) o1.get(sortKey))));
        } else {
            jsonValues.sort((o1, o2) -> Double.compare(Double.parseDouble((String) o1.get(sortKey)),
                    Double.parseDouble((String) o2.get(sortKey))));
        }
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    public JSONArray filterRecords(JSONArray records, String offset, String limit) {
        JSONArray filteredJSONArray = new JSONArray();
        int startIndex = Integer.parseInt(offset);
        int lastIndex = startIndex + Integer.parseInt(limit);
        for (int i = startIndex; i < lastIndex && i < records.length(); i++) {
            filteredJSONArray.put(records.get(i));
        }
        return filteredJSONArray;
    }

}

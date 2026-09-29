package com.temenos.infinity.api.stoppayments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetPaymentStopsPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    String sortKey = null;
    private Object Object;

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
        Object body = result.getParamByName(StopPaymentConstants.PARAM_BODY) != null ? result.getParamByName(StopPaymentConstants.PARAM_BODY).getObjectValue() : null;
        String ErrMsg = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) != "" ? result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) : "";
        
        if (body == null && StringUtils.isEmpty(ErrMsg)) {
            return TemenosUtils.getEmptyResult(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT);
        } 
        if (body != null) {
            JSONArray paymentStopsRecords = new JSONArray(body.toString());
            List<Record> paymentStopsRecordsFinals = new ArrayList<Record>();
            for (int p = 0; p < paymentStopsRecords.length(); p++) {
                Record product = new Record();
                JSONObject obj = (JSONObject) paymentStopsRecords.get(p);
                String chequeDate = null;
                String transactionId = null;
                String customerId = null;
                String checkReason = null;
                if (obj.has(StopCheckPaymentUtils.PARAM_CHEQUE_SUPPLEMENT_ACCOUNT_ID)) {
                    transactionId = obj.getString(StopCheckPaymentUtils.PARAM_CHEQUE_SUPPLEMENT_ACCOUNT_ID);
                }
                if (obj.has(StopCheckPaymentUtils.PARAM_PAYMENT_STOP_REASON)) {
                    checkReason = obj.getString(StopCheckPaymentUtils.PARAM_PAYMENT_STOP_REASON);
                }
                if (obj.has(StopCheckPaymentUtils.PARAM_CUSTOMER_ID)) {
                    customerId = obj.getString((StopCheckPaymentUtils.PARAM_CUSTOMER_ID)); 
                }
                if (obj.has(StopCheckPaymentUtils.PARAM_CHEQUE_DATE)) {
                    chequeDate = obj.getString(StopCheckPaymentUtils.PARAM_CHEQUE_DATE);
                }

                if (customerId != null) {
                    product.addParam(StopCheckPaymentUtils.PARAM_CUSTOMER_ID, customerId);
                }

                String[] accountIds = null;
                String accountId = null;
                if (transactionId.contains(";")) {
                    accountIds = transactionId.split(";");
                    if (accountIds != null && accountIds.length > 0) {
                        accountId = accountIds[0];
                    }
                } else {
                    accountId = transactionId;
                }

                if (accounts != null && StringUtils.isNotBlank(accountId)) {
                    Account account = accounts.containsKey(accountId) ? accounts.get(accountId) : null;
                    if (account != null) {
                        product.addParam(new Param(StopCheckPaymentUtils.PARAM_NICK_NAME, account.getAccountName(),
                                Constants.PARAM_DATATYPE_STRING));
                    }
                }

                JSONArray stops = new JSONArray();

                Double amount = 0.0;
                Double fee = 0.0;
                String checkNumber1 = null;
                String checkNumber2 = null;
                String stopDate = null;
                String payeeName = null;
                stops = obj.getJSONArray(StopPaymentConstants.PARAM_STOPS);
                for (int i = 0; i < stops.length(); i++) {
                    JSONObject paymentStopsObject = (JSONObject) stops.get(i);
                    if (paymentStopsObject.has(StopPaymentConstants.PARAM_FIRST_CHEQUE)) {
                        if ((String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_FIRST_CHEQUE) != null)
                            checkNumber1 = (String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_FIRST_CHEQUE);
                    }
                    if (paymentStopsObject.has(StopPaymentConstants.PARAM_LAST_CHEQUE)) {
                        if ((String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_LAST_CHEQUE) != null)
                            checkNumber2 = (String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_LAST_CHEQUE);
                    }
                    if (paymentStopsObject.has(StopPaymentConstants.PARAM_STOP_DATE)) {
                        if ((String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_STOP_DATE) != null)
                            stopDate = (String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_STOP_DATE);
                    }
                    if (paymentStopsObject.has(StopPaymentConstants.PARAM_BENEFICIARY_ID)) {
                        if ((String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_BENEFICIARY_ID) != null)
                            payeeName = (String) ((JSONObject) stops.get(i)).get(StopPaymentConstants.PARAM_BENEFICIARY_ID);
                    }
                    if (checkNumber1 != null && checkNumber2 != null) {
                        product.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SERIES);
                    } else {
                        product.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SINGLE);
                    }

                    // Process Amount
                    try {
                        if (paymentStopsObject.has(StopPaymentConstants.PARAM_AMOUNT_FROM)) {
                            Object stopAmount = paymentStopsObject.get(StopPaymentConstants.PARAM_AMOUNT_FROM);
                            if (stopAmount instanceof Integer) {
                                amount = ((Number) stopAmount).doubleValue();
                            } else if (stopAmount instanceof String) {
                                amount = Double.parseDouble((String) stopAmount);
                            } else if (stopAmount instanceof BigDecimal) {
                                amount = ((BigDecimal) stopAmount).doubleValue();
                            } else {
                                amount = (double) stopAmount;
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Error Parsing the data" + e).log();
                    }

                    // Process charges - Calculate fees
                    if (paymentStopsObject.has(StopPaymentConstants.PARAM_CHARGE_AMOUNTS)) {
                        JSONArray charges = paymentStopsObject.getJSONArray(StopPaymentConstants.PARAM_CHARGE_AMOUNTS);
                        Double chargeAmount = 0.0;
                        for (int j = 0; j < charges.length(); j++) {
                            JSONObject chargesObject = (JSONObject) charges.get(j);
                            if (chargesObject.has(StopPaymentConstants.PARAM_CHARGE_AMOUNT)) {
                                Object chgAmount = chargesObject.get(StopPaymentConstants.PARAM_CHARGE_AMOUNT);
                                if (chgAmount instanceof Integer) { 
                                    chargeAmount = ((Number) chgAmount).doubleValue();
                                } else if (chgAmount instanceof String) {
                                    chargeAmount = Double.parseDouble((String) chgAmount);
                                } else if (chgAmount instanceof BigDecimal) {
                                    chargeAmount = ((BigDecimal) chgAmount).doubleValue();
                                } else {
                                    chargeAmount = (double) chgAmount;
                                }
                            }
                            fee = fee + chargeAmount;
                        }
                    }
                }

                // Process Transaction Notes
                if (obj.has(StopPaymentConstants.PARAM_REMARKS)) {
                    JSONArray remarks = obj.getJSONArray(StopPaymentConstants.PARAM_REMARKS);
                    String remark = null;
                    if (remarks.length() > 0) {
                        JSONObject remarksObject = (JSONObject) remarks.get(0);
                        if (remarksObject.has(StopPaymentConstants.PARAM_REMARK))
                            if ((String) ((JSONObject) remarks.get(0)).get(StopPaymentConstants.PARAM_REMARK) != null)
                                remark = (String) ((JSONObject) remarks.get(0)).get(StopPaymentConstants.PARAM_REMARK);
                        product.addParam(StopPaymentConstants.PARAM_TRANSACTION_NOTES, remark);
                    }
                }

                product.addParam(StopPaymentConstants.PARAM_CHEQUE_FEE, Double.toString(fee));
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE, chequeDate);
                product.addParam(StopPaymentConstants.PARAM_CHECK_DATE_OF_ISSUE, stopDate);
                product.addParam(StopPaymentConstants.PARAM_ID, transactionId);
                product.addParam(StopPaymentConstants.CHECK_REASON, checkReason);
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER, checkNumber1);
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO, checkNumber2);
                product.addParam(StopPaymentConstants.PARAM_AMOUNT, Double.toString(amount));
                product.addParam(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT, accountId);
                product.addParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_PAYEE_NAME, payeeName);
                paymentStopsRecordsFinals.add(product);
            }
            List<JSONObject> jsonValues = new ArrayList();
            for (int i = 0; i < paymentStopsRecordsFinals.size(); i++) {
                jsonValues.add(ResultToJSON.convertRecord(paymentStopsRecordsFinals.get(i)));
            }

            sortKey = request.getParameter(StopPaymentConstants.PARAM_SORY_BY);
            JSONArray records = new JSONArray();
            List<JSONObject> amountRecords = new ArrayList();
            List<JSONObject> nonAmountRecords = new ArrayList();
            if (sortKey != null) {
                if (sortKey.equals(StopPaymentConstants.PARAM_AMOUNT)) {
                    for (JSONObject objValue : jsonValues) {
                        if (objValue.has(StopPaymentConstants.PARAM_AMOUNT)) {
                            amountRecords.add(objValue);
                        } else {
                            nonAmountRecords.add(objValue);
                        }
                    }
                    records = DoubleSort(amountRecords, request.getParameter(StopPaymentConstants.PARAM_ORDER));
                    for (int i = 0; i < nonAmountRecords.size(); i++) {
                        records.put(nonAmountRecords.get(i));
                    }
                } else {
                    records = sort(jsonValues, request.getParameter(StopPaymentConstants.PARAM_ORDER));
                }
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

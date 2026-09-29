package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeleteTransactionsForLockedCard implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARD_UPDATE);
        }

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String cardId = (String) inputParams.get("cardId");
        if (StringUtils.isBlank(cardId)) {
            return false;
        }
        inputParams.put("Id", cardId);
        inputParams.put("card_Status", "Locked");
        inputParams.put("action", "Lock");
        deleteScheduledTransactions(dcRequest, cardId);
        return true;
    }

    private void deleteScheduledTransactions(DataControllerRequest dcRequest, String cardId) throws HttpCallException {
        if (StringUtils.isBlank(cardId)) {
            return;
        }

        String cardNumber = getCardNumber(dcRequest, cardId);

        if (StringUtils.isBlank(cardNumber)) {
            return;
        }
        String accountId = getAccountId(dcRequest, cardNumber);

        if (StringUtils.isBlank(accountId)) {
            return;
        }

        Result transResult = getTransactions(dcRequest, accountId);

        if (HelperMethods.hasRecords(transResult)) {
            List<Record> transactions = transResult.getAllDatasets().get(0).getAllRecords();
            Map<String, String> input = new HashMap<>();
            for (Record transaction : transactions) {
                input.put("id", HelperMethods.getFieldValue(transaction, "Id"));
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_DELETE);
            }
        }
    }

    private Result getTransactions(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "fromAccountNumber" + DBPUtilitiesConstants.EQUAL + accountId + DBPUtilitiesConstants.AND
                + "isScheduled" + DBPUtilitiesConstants.EQUAL + "1";
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);
    }

    private String getAccountId(DataControllerRequest dcRequest, String cardNumber) throws HttpCallException {
        String filter = "CreditCardNumber" + DBPUtilitiesConstants.EQUAL + cardNumber;
        Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        return HelperMethods.getFieldValue(account, "Account_id");
    }

    private String getCardNumber(DataControllerRequest dcRequest, String cardId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + cardId;
        Result card = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CARDS_GET);
        return HelperMethods.getFieldValue(card, "cardNumber");
    }
}

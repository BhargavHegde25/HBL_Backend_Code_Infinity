package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SaveRecipientAfterWireTransfer implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = process(dcRequest, inputParams);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private Result process(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
        String transactionId = (String) inputParams.get("transactionId");
        String payeeId = getPayeeId(dcRequest, transactionId);
        updatePayee(dcRequest, payeeId);
        updateTransaction(dcRequest, transactionId);
        Result result = new Result();
        result.addParam(new Param("payeeId", payeeId, DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    private void updateTransaction(DataControllerRequest dcRequest, String transactionId) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Id", transactionId);
        input.put("isPayeeDeleted", "0");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.TRANSACTION_UPDATE);
    }

    private void updatePayee(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Id", payeeId);
        input.put("softDelete", "0");
        input.put("isWiredRecepient", "1");
        String id = HelperMethods.getUserIdFromSession(dcRequest);
        if (StringUtils.isNotBlank(id)) {
            input.put("User_Id", id);
        }
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.PAYEE_UPDATE);
    }

    private String getPayeeId(DataControllerRequest dcRequest, String transactionId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + transactionId;
        Result transaction = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);
        return HelperMethods.getFieldValue(transaction, "Payee_id");
    }
}

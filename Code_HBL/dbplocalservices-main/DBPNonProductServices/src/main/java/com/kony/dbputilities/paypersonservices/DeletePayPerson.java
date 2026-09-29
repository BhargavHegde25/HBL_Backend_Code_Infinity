package com.kony.dbputilities.paypersonservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeletePayPerson implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_DELETE);
        } else {
            result.addParam(new Param("errmsg", "Not a valid user", "String"));
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        if (checkRecordExists(dcRequest, inputParams, result)) {
            String payPersonId = (String) inputParams.get("PayPersonId");
            List<Record> transactions = getTransactions(dcRequest, payPersonId);
            deleteTransactions(dcRequest, transactions);
            inputParams.put("id", payPersonId);
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    private boolean checkRecordExists(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {

        boolean status;
        String id = HelperMethods.getCustomerIdFromSession(dcRequest);
        if(inputParams.get("userId") != null) {
        	id = inputParams.get("userId").toString();
        }
        String filter = "id" + DBPUtilitiesConstants.EQUAL + inputParams.get("PayPersonId") + DBPUtilitiesConstants.AND
                + "User_id" + DBPUtilitiesConstants.EQUAL + id;
        Result chkResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYPERSON_GET);
        if (HelperMethods.hasRecords(chkResult)) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    private void deleteTransactions(DataControllerRequest dcRequest, List<Record> transactions)
            throws HttpCallException {
        if (null != transactions) {
            Map<String, String> input = new HashMap<>();
            for (Record transaction : transactions) {
                input.put("Id", HelperMethods.getFieldValue(transaction, "Id"));
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_DELETE);
            }
        }
    }

    private List<Record> getTransactions(DataControllerRequest dcRequest, String payPersonId) throws HttpCallException {
        List<Record> result = null;
        String filter = "Person_Id" + DBPUtilitiesConstants.EQUAL + payPersonId;
        Result transactions = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);
        if (HelperMethods.hasRecords(transactions)) {
            result = transactions.getAllDatasets().get(0).getAllRecords();
        }
        return result;
    }
}

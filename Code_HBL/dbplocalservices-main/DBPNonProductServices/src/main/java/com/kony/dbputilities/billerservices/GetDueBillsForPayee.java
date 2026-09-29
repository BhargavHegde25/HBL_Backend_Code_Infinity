package com.kony.dbputilities.billerservices;

import java.util.Iterator;
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

public class GetDueBillsForPayee implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_PAYEE_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(inputParams, result);
        }
        return result;
    }

    private void postProcess(Map<String, String> inputParams, Result result) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Iterator<Record> itr = transactions.iterator();
        while (itr.hasNext()) {
            Record transaction = itr.next();
            String paidAmount = HelperMethods.getFieldValue(transaction, "paidAmount");
            String dueAmount = HelperMethods.getFieldValue(transaction, "dueAmount");
            paidAmount = StringUtils.isBlank(paidAmount) ? "0" : paidAmount;
            dueAmount = StringUtils.isBlank(dueAmount) ? "0" : dueAmount;
            if (Double.valueOf(dueAmount).compareTo(Double.valueOf(paidAmount)) <= 0) {
                itr.remove();
            }
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        boolean status = true;

        String payeeId = inputParams.get("payeeId");
        String today = HelperMethods.getCurrentTimeStamp();

        StringBuilder filter = new StringBuilder();
        filter.append("payeeId").append(DBPUtilitiesConstants.EQUAL).append(payeeId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("billDueDate").append(DBPUtilitiesConstants.GREATER_EQUAL).append(today);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");
        String id = HelperMethods.getCustomerIdFromSession(dcRequest);
        if (StringUtils.isNotBlank(id)) {
            filter.append(DBPUtilitiesConstants.AND);
            filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(id);
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());

        return status;
    }

}

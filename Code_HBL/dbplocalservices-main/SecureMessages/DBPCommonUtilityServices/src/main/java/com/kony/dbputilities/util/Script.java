package com.kony.dbputilities.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class Script implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

        String createdts1 = inputmap.get("createdts1");
        String createdts2 = inputmap.get("createdts2");
        String filterDate = "createdts" + " gt " + createdts1 + " and " + "createdts" + " lt " + createdts2;

        String select = "id, Password";
        inputmap.put(DBPUtilitiesConstants.SELECT, select);
        inputmap.put(DBPUtilitiesConstants.FILTER, filterDate);

        Result customer = HelperMethods.callApi(dcRequest, inputmap, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_GET);

        List<Record> customers = customer.getAllDatasets().get(0).getAllRecords();

        for (Record record : customers) {

            String customerId = HelperMethods.getFieldValue(record, "id");
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
            Result passwordHistory = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PASSWORDHISTORY_GET);

            if (!HelperMethods.hasRecords(passwordHistory)) {
                Map<String, String> hashMap = new HashMap<>();
                String id = UUID.randomUUID().toString();
                hashMap.put("id", id);
                hashMap.put("Customer_id", customerId);
                hashMap.put("PreviousPassword", HelperMethods.getFieldValue(record, "Password"));
                HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                        URLConstants.PASSWORDHISTORY_CREATE);
            }
        }
        return result;
    }

}

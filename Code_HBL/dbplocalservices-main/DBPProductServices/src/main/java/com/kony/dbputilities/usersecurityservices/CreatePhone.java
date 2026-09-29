package com.kony.dbputilities.usersecurityservices;

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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreatePhone implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERCOMMUNICATION_CREATE);
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String isPrimary = inputParams.get("isPrimary");
        if ("1".equals(isPrimary) || "true".equals(isPrimary)) {
            modifyOldPrimaryPhone(dcRequest, userId);
        }
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("Customer_id", userId);
        inputParams.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        inputParams.put("countryType", "domestic");
        inputParams.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
        return true;
    }

    private void modifyOldPrimaryPhone(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "isPrimary"
                + DBPUtilitiesConstants.EQUAL + "1" + DBPUtilitiesConstants.AND + "Type_id eq COMM_TYPE_PHONE"
                + DBPUtilitiesConstants.AND + "softdeleteflag eq 0";
        Result primaryPhones = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_GET);
        if (HelperMethods.hasRecords(primaryPhones)) {
            List<Record> phones = primaryPhones.getAllDatasets().get(0).getAllRecords();
            for (Record phone : phones) {
                updatePhone(dcRequest, phone);
            }
        }
    }

    private void updatePhone(DataControllerRequest dcRequest, Record phone) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("id", HelperMethods.getFieldValue(phone, "id"));
        input.put("isPrimary", "0");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_UPDATE);
    }
}
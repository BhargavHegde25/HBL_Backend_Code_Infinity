package com.kony.dbputilities.customersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class EditCustomerCommunication {

    public static void invoke(String id, Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws Exception {

        JSONObject existingCommunicationValues = new JSONObject();
        JSONObject existingCommunicationIds = new JSONObject();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + id;

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_GET);
        Map<String, String> map = HelperMethods.getCommunicationMap();
        if (HelperMethods.hasRecords(result)) {
            for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
                String commId = HelperMethods.getFieldValue(record, "id");
                String commType = map.get(HelperMethods.getFieldValue(record, "Type_id"));
                String commValue = HelperMethods.getFieldValue(record, "Value");
                existingCommunicationValues.put(commType, commValue);
                existingCommunicationIds.put(commType, commId);
            }
        }

        if (inputParams.containsKey("Email") && existingCommunicationValues.has("Email")) {
            updateCommunication(id, existingCommunicationIds.getString("Email"), "Email", inputParams.get("Email"),
                    dcRequest);
        } else if (inputParams.containsKey("Email")) {
            createCommunication(id, "Email", inputParams.get("Email"), dcRequest);
        }

        if (inputParams.containsKey("Phone") && existingCommunicationValues.has("Phone")) {
            updateCommunication(id, existingCommunicationIds.getString("Phone"), "Phone", inputParams.get("Phone"),
                    dcRequest);
        } else if (inputParams.containsKey("Phone")) {
            createCommunication(id, "Phone", inputParams.get("Phone"), dcRequest);
        }

    }

    private static void updateCommunication(String userId, String recordId, String type, String value,
            DataControllerRequest dcRequest) throws HttpCallException {

        Map<String, String> input = null;
        Map<String, String> communicationTypes = HelperMethods.getCommunicationTypes();

        input = new HashMap<>();
        input.put("id", recordId);
        input.put("Customer_id", userId);
        input.put("Type_id", communicationTypes.get(type));
        input.put("isPrimary", "1");
        input.put("Value", value);
        input.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        input.put("Description", type);
        input.put("IsPreferredContactMethod", "1");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_UPDATE);

    }

    private static void createCommunication(String userId, String type, String value, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, String> input = null;
        Map<String, String> communicationTypes = HelperMethods.getCommunicationTypes();

        input = new HashMap<>();
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        input.put("id", "CUS_" + type + "_" + idformatter.format(new Date()));
        input.put("Customer_id", userId);
        input.put("Type_id", communicationTypes.get(type));
        input.put("isPrimary", "1");
        input.put("Value", value);
        input.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        input.put("Description", type);
        if ("Phone".equals(type)) {
            input.put("countryType", "domestic");
        }
        input.put("IsPreferredContactMethod", "1");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
    }

}

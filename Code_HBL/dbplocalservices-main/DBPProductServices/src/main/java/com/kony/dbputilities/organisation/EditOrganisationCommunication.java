package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class EditOrganisationCommunication {

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
        Result result = new Result();
        String communication = dcRequest.getParameter("Communication");
        List<HashMap<String, String>> list = HelperMethods.getAllRecordsMap(communication);
        if (list.isEmpty()) {
            return new Result();
        }
        HashMap<String, String> input = list.get(0);
        Map<String, String> communicationTypes = HelperMethods.getCommunicationTypes();
        String id = dcRequest.getParameter("id");

        HashMap<String, String> hashMap = null;
        for (String key : input.keySet()) {
            String value = input.get(key);
            String typeId = communicationTypes.get(key);
            String commID = getCommunicationId(id, dcRequest, typeId);
            hashMap = new HashMap<>();
            if (!StringUtils.isBlank(value)) {
                hashMap.put("Value", value);
            } else {
                hashMap.put("Value", "");
            }
            if (StringUtils.isNotBlank(commID)) {
                hashMap.put("id", commID);
                result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATIONCOMMUNICATION_UPDATE);
            } else if (StringUtils.isNotBlank(value)) {
                hashMap.put("Organization_id", id);
                hashMap.put("Type_id", typeId);
                result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATIONCOMMUNICATION_CREATE);
            }
            if (HelperMethods.hasError(result)) {
                return result;
            }
        }
        return result;
    }

    private static String getCommunicationId(String id, DataControllerRequest dcRequest, String Type_id)
            throws HttpCallException {
        StringBuilder sb = new StringBuilder();
        Map<String, String> inputParams = new HashMap<>();
        sb.append("Organization_id").append(DBPUtilitiesConstants.EQUAL).append(id);
        sb.append(DBPUtilitiesConstants.AND).append("Type_id").append(DBPUtilitiesConstants.EQUAL).append(Type_id);
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        Result organisation = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONCOMMUNICATION_GET);
        if (HelperMethods.hasRecords(organisation)) {
            return HelperMethods.getFieldValue(organisation, "id");
        }
        return null;
    }

}

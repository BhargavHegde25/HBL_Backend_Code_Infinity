package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class EditOrganisationAddress {

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
        Result result = null;

        String string = dcRequest.getParameter("Address");
        List<HashMap<String, String>> list = HelperMethods.getAllRecordsMap(string);
        if (list.isEmpty()) {
            return new Result();
        }
        HashMap<String, String> input = list.get(0);
        String orgId = dcRequest.getParameter("id");
        String id = getORGAddressID(orgId, dcRequest);
        if (StringUtils.isNotBlank(id)) {
            input.put("id", id);
            HelperMethods.removeNullValues(input);
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_UPDATE);
        } else {
            id = UUID.randomUUID().toString();
            input.put("id", id);
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_CREATE);
            if (HelperMethods.hasRecords(result)) {
                id = HelperMethods.getFieldValue(result, "id");

                input = new HashMap<>();
                input.put("Organization_id", orgId);
                input.put("Address_id", id);
                result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATIONADDRESS_CREATE);
            }

        }

        return result;
    }

    private static String getORGAddressID(String id, DataControllerRequest dcRequest) throws HttpCallException {
        String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + id;
        Result organisationAddress = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONADDRESS_GET);
        if (HelperMethods.hasRecords(organisationAddress)) {
            return HelperMethods.getFieldValue(organisationAddress, "Address_id");
        }
        return null;
    }

}

package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationAddress {

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
        Result result = new Result();
        Map<String, String> input = new HashMap<>();
        String orgId = inputParams.get("id");

        String id = createAddress(inputParams, dcRequest);

        if (StringUtils.isNotBlank(id)) {
            input.put("Organization_id", orgId);
            input.put("Address_id", id);
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONADDRESS_CREATE);
        }
        return result;
    }

    private static String createAddress(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result result = new Result();

        String address = dcRequest.getParameter("Address");
        List<HashMap<String, String>> list = HelperMethods.getAllRecordsMap(address);
        if (list.isEmpty()) {
            return null;
        }
        Map<String, String> input = list.get(0);
        String id = UUID.randomUUID().toString();
        input.put("id", id);
        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ADDRESS_CREATE);

        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "id");
        }

        return null;
    }
}

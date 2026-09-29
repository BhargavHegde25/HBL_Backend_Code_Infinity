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

public class EditOrganisationOwner {

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {

        Result result = new Result();
        String orgId = dcRequest.getParameter("id");
        String string = dcRequest.getParameter("Owner");
        List<HashMap<String, String>> list = HelperMethods.getAllRecordsMap(string);
        if (list.isEmpty()) {
            return new Result();
        }
        HashMap<String, String> hashMap = list.get(0);
        String ownerId = "";
        if (StringUtils.isBlank(hashMap.get("id"))) {
            ownerId = getOwnerID(orgId, dcRequest);
        }
        if (StringUtils.isNotBlank(ownerId)) {
            hashMap.put("id", ownerId);
            if (hashMap.containsKey("DOB") && StringUtils.isNotBlank(hashMap.get("DOB"))) {
                hashMap.put("DateOfBirth", hashMap.get("DOB"));
                hashMap.remove("DOB");
            }
            if (hashMap.containsKey("EmailId")) {
                hashMap.put("Email", hashMap.get("EmailId"));
                hashMap.remove("EmailId");
            }
            if (hashMap.containsKey("PhoneNumber")) {
                hashMap.put("Phone", hashMap.get("PhoneNumber"));
                hashMap.remove("PhoneNumber");
            }
            if (hashMap.containsKey("IdType")) {
                hashMap.put("IDType_id", hashMap.get("IdType"));
                hashMap.remove("IdType");
            }
            if(hashMap.containsKey("Ssn")) {
                hashMap.remove("Ssn");
            }
            HelperMethods.removeNullValues(hashMap);
            return HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONOWNER_UPDATE);
        }
        return result;
    }

    private static String getOwnerID(String orgId, DataControllerRequest dcRequest) throws HttpCallException {
        Result result = new Result();
        if (StringUtils.isNotBlank(orgId)) {
            String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_OWNER_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "id");
        }
        return "";
    }

}

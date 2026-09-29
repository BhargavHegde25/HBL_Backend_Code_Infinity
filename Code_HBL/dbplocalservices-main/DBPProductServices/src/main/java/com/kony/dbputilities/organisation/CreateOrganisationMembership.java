package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationMembership {
    private CreateOrganisationMembership() {

    }

    public static Result invoke(String organisationId, List<HashMap<String, String>> list,
            DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, String> inputParams = null;
        for (HashMap<String, String> hashMap : list) {
            inputParams = new HashMap<>();
            inputParams.put("Membership_id", hashMap.get("Membership_id"));
            if (StringUtils.isNotBlank(hashMap.get("Taxid"))) {
                inputParams.put("Taxid", hashMap.get("Taxid"));
            } else {
                inputParams.put("Taxid", hashMap.get("TaxId"));
            }
            hashMap.put("id", HelperMethods.getNumericId() + "");
            hashMap.put("Organization_id", organisationId);
            HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONMEMBERSHIP_CREATE);
        }

        return new Result();
    }
}

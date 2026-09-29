package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationOwner {

	public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		String string = dcRequest.getParameter("Owner");
		HashMap<String, String> map = HelperMethods.getAllRecordsMap(string).get(0);
		map.put("Organization_id", inputParams.get("id"));
		if (map.containsKey("DOB")) {
			map.put("DateOfBirth", map.get("DOB"));
			map.remove("DOB");
		}
		if (map.containsKey("EmailId")) {
			map.put("Email", map.get("EmailId"));
			map.remove("EmailId");
		}
		if (map.containsKey("PhoneNumber")) {
			map.put("Phone", map.get("PhoneNumber"));
			map.remove("PhoneNumber");
		}
		if (map.containsKey("IdType")) {
			map.put("IDType_id", map.get("IdType"));
			map.remove("IdType");
		}
		HelperMethods.removeNullValues(map);
		return HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
				URLConstants.ORGANISATIONOWNER_CREATE);
	}
}

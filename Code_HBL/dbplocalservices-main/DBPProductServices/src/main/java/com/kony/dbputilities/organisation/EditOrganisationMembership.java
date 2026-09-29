package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class EditOrganisationMembership {

	public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Result result = new Result();
		Map<String, String> map = new HashMap<>();
		map.put("Membership_id", inputParams.get("Membership_id"));
		//map.put("Taxid", inputParams.get("Taxid"));
		map.put("Organization_id", inputParams.get("id"));
		result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
				URLConstants.ORGANISATIONMEMBERSHIP_UPDATE);

		return result;
	}

}

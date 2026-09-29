package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationCommunication {

	public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
		Map<String, String> input = null;
		Result result = new Result();
		String communicaiton = dcRequest.getParameter("Communication");
		Map<String, String> params = HelperMethods.getAllRecordsMap(communicaiton).get(0);
		Map<String, String> communicationTypes = HelperMethods.getCommunicationTypes();
		String id = inputParams.get("id");

		for (String key : params.keySet()) {
			String value = params.get(key);
			if (!StringUtils.isBlank(value)) {
				input = new HashMap<>();
				input.put("Type_id", communicationTypes.get(key));
				input.put("Organization_id", id);
				input.put("Sequence", "1");
				input.put("Value", value);
				input.put("Extension", "Personal");
				result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.ORGANISATIONCOMMUNICATION_CREATE);
				if (HelperMethods.hasError(result)) {
					return result;
				}
			}
		}

		return result;
	}
}

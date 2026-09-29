package com.kony.dbputilities.organisation;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganisationName implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		HashMap<String, String> inputParams = new HashMap<>();
		String orgId = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);
		if (StringUtils.isBlank(orgId)) {
			orgId = dcRequest.getParameter("Organization_id");
		}
		if (!StringUtils.isBlank(orgId)) {
			inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + orgId);
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_GET);
		}
		return result;
	}

	public boolean isValidOrgID(DataControllerRequest dcRequest, String id) throws Exception {
		Result result = new Result();
		HashMap<String, String> inputParams = new HashMap<>();
		if (!StringUtils.isBlank(id)) {
			inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + id);
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_GET);
		}

		return HelperMethods.hasRecords(result);
	}

	public String getBusinessTypeId(DataControllerRequest dcRequest, String id) {
		Result result = new Result();
		HashMap<String, String> inputParams = new HashMap<>();
		if (!StringUtils.isBlank(id)) {
			inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + id);
			try {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.ORGANISATION_GET);
			} catch (HttpCallException e) {
			}
		}

		return HelperMethods.getFieldValue(result, "BusinessType_id");
	}
}

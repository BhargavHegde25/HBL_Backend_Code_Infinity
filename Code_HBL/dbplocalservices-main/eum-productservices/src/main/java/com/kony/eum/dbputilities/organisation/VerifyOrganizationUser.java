package com.kony.eum.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyOrganizationUser implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_ORGANISATION_VIEW_GET);
		}

		return result;
	}

	public Object invoke(DataControllerRequest dcRequest, Map<String, String> inputParams) throws Exception {
		Result result = new Result();
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_ORGANISATION_VIEW_GET);
		}

		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;

		String[] list = { "Ssn", "Phone", "Email", "LastName", "DateOfBirth", "Membership_id", "Taxid",
				"Organization_Id" };

		String filter = "";

		for (int i = 0; i < list.length; i++) {
			String filterKey = list[i];
			String filtervalue = inputParams.get(filterKey);
			if (StringUtils.isNotBlank(filtervalue)) {
				if (!filter.isEmpty()) {
					filter += DBPUtilitiesConstants.AND;
				}

				filter += filterKey + DBPUtilitiesConstants.EQUAL + filtervalue;
			}
		}

		if (filter.isEmpty()) {
			Record record = new Record();
			record.setId(DBPUtilitiesConstants.USR_ATTR);
			ErrorCodeEnum.ERR_10038.setErrorCode(record);
			result.addRecord(record);
			status = false;
		}

		if (status) {
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		}
		return status;
	}
}

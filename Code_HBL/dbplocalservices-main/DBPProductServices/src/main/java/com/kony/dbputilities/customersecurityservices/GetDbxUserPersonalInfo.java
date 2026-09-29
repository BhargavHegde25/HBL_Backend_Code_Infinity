package com.kony.dbputilities.customersecurityservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetDbxUserPersonalInfo implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.PERSONAL_INFO_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			postProcess(result);
		}
		return result;
	}

	private void postProcess(Result result) {
		List<Record> persons = result.getAllDatasets().get(0).getAllRecords();
		for (Record person : persons) {
			updateDateFormat(person);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String userId = HelperMethods.getUserIdFromNUOSession(dcRequest);
		String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + userId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}

	private void updateDateFormat(Record person) {
		String dateOfBirth = HelperMethods.getFieldValue(person, "dateOfBirth");
		try {
			if (StringUtils.isNotBlank(dateOfBirth)) {
				person.addParam(new Param("dateOfBirth",
						HelperMethods.convertDateFormat(dateOfBirth, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
		} catch (Exception e) {
		}
	}
}
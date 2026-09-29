package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FetchUserName implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		if (!HelperMethods.hasRecords(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Record user = new Record();
		user.addParam(new Param("userName", "konybankingdev", "String"));
		Dataset data = new Dataset("user");
		data.addRecord(user);
		result.addDataset(data);
		/*
		 * Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR,
		 * "user not found.",DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		 * retVal.addParam(p);
		 */
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) throws ParseException {
		boolean status = true;
		String ssn = (String) inputParams.get(DBPUtilitiesConstants.SSN);
		String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);
		String lastname = (String) inputParams.get(DBPUtilitiesConstants.LAST_NAME);
		if (!StringUtils.isNotBlank(ssn) && !StringUtils.isNotBlank(dob) && !StringUtils.isNotBlank(lastname)) {
			HelperMethods.setValidationMsg("Please provide last name and/or ssn and/or date of birth.", dcRequest,
					result);
			status = false;
		}
		if (status) {
			StringBuilder sb = new StringBuilder();
			boolean isAdded = false;
			if (StringUtils.isNotBlank(ssn)) {
				sb.append("Ssn").append(DBPUtilitiesConstants.EQUAL).append(ssn);
				isAdded = true;
			}
			if (StringUtils.isNotBlank(dob)) {
				dob = HelperMethods.convertDateFormat(dob, null);
				if (isAdded) {
					sb.append(DBPUtilitiesConstants.AND);
				}
				sb.append("DateOfBirth").append(DBPUtilitiesConstants.EQUAL).append(dob);
				isAdded = true;
			}
			if (StringUtils.isNotBlank(lastname)) {
				if (isAdded) {
					sb.append(DBPUtilitiesConstants.AND);
				}
				sb.append("LastName").append(DBPUtilitiesConstants.EQUAL).append(lastname);
			}
			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		return status;
	}
}